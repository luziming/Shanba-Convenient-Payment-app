package com.shaba.app.fragment;

import android.Manifest;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.loopj.android.http.AsyncHttpResponseHandler;
import com.shaba.app.R;
import com.shaba.app.adapter.BankMapAdapter;
import com.shaba.app.been.MapListEntity;
import com.shaba.app.fragment.base.BaseLoadingFragment;
import com.shaba.app.fragment.base.FragmentUtils;
import com.shaba.app.utils.GsonTools;
import com.shaba.app.utils.StringUtil;
import com.yanzhenjie.permission.AndPermission;
import com.yanzhenjie.permission.PermissionNo;
import com.yanzhenjie.permission.PermissionYes;
import com.yanzhenjie.permission.Rationale;
import com.yanzhenjie.permission.RationaleListener;

import org.apache.http.Header;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import de.greenrobot.event.EventBus;

/*
                   _ooOoo_
                  o8888888o
                  88" . "88
                  (| -_- |)
                  O\  =  /O
               ____/`---'\____
             .'  \\|     |//  `.
            /  \\|||  :  |||//  \
           /  _||||| -:- |||||-  \
           |   | \\\  -  /// |   |
           | \_|  ''\---/''  |   |
           \  .-\__  `-`  ___/-. /
         ___`. .'  /--.--\  `. . __
      ."" '<  `.___\_<|>_/___.'  >'"".
     | | :  `- \`.;`\ _ /`;.`/ - ` : | |
     \  \ `-.   \_ __\ /__ _/   .-` /  /
======`-.____`-.___\_____/___.-`____.-'======
                   `=---='
^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^
         佛祖保佑       永无BUG
*/
public class BankMapFragment extends BaseLoadingFragment implements AdapterView.OnItemClickListener {

    @Bind(R.id.lv_bank_map)
    ListView lvBankMap;

    private List<MapListEntity> entities;
    private int mPosition;
    private static final int REQUEST_CODE_SETTING = 300;


    @Override
    public View initFragment() {
        View view = inflater.inflate(R.layout.fragment_bank_map, null);
        ButterKnife.bind(this, view);
        lvBankMap.setOnItemClickListener(this);
//        PermissionUtils.requestMultiPermissions(mActivity, mPermissionGrant);

        return view;
    }

//    private PermissionUtils.PermissionGrant mPermissionGrant = new PermissionUtils.PermissionGrant() {
//        @Override
//        public void onPermissionGranted(int requestCode) {
//
//        }
//    };

    @Override
    public void requestData() {
        int map_type = getArguments().getInt("MAP_TYPE");
        if (map_type == 1) {
            appUtil.getMapist(new MapListResponseHandler(), token);
        } else {
            appUtil.getATMList(new MapListResponseHandler(), token);
        }
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        if (StringUtil.isFastClick())
            return;
        this.mPosition = position;
        //6.0之后需要动态申请权限

        // 先判断是否有权限。
//        if (AndPermission.hasPermission(mActivity, Manifest.permission.WRITE_CONTACTS)) {
//            Log.e("BankMapFragment", "获取位置: 检查权限权限" + AndPermission.hasPermission(mActivity, Manifest.permission.WRITE_CONTACTS));
//            // 有权限，直接do anything.
//            startMapFragment(position);
//        } else {
            Log.e("BankMapFragment", "获取位置: 申请权限");
            // 申请权限。
            AndPermission.with(this)
                    .requestCode(100)
                    .permission(Manifest.permission.READ_PHONE_STATE,Manifest.permission.READ_EXTERNAL_STORAGE,Manifest.permission.ACCESS_FINE_LOCATION)
                    .rationale(new RationaleListener() {
                        @Override
                        public void showRequestPermissionRationale(int requestCode, Rationale rationale) {
                            // 这里使用自定义对话框，如果不想自定义，用AndPermission默认对话框：
                            Log.e("BankMapFragment", "获取位置: 申请权限对话框");
                            AndPermission.rationaleDialog(mActivity, rationale).show();
                        }
                    })
                    .send();
//        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions,
                                           int[] grantResults) {
        // 只需要调用这一句，第一个参数是当前Acitivity/Fragment，回调方法写在当前Activity/Framgent。
        AndPermission.onRequestPermissionsResult(this, requestCode, permissions, grantResults);
    }

    // 全部成功回调的方法，用注解即可，里面的数字是请求时的requestCode。
    @PermissionYes(100)
    private void getLocationYes(List<String> grantedPermissions) {
        Log.e("BankMapFragment", "获取位置: 申请权限成功");
        // 有权限，直接do anything.
        startMapFragment(mPosition);
    }

    // 失败回调的方法，用注解即可，里面的数字是请求时的requestCode。
    @PermissionNo(100)
    private void getLocationNo(List<String> deniedPermissions) {
        // 用户否勾选了不再提示并且拒绝了权限，那么提示用户到设置中授权。
        if (AndPermission.hasAlwaysDeniedPermission(mActivity, deniedPermissions)) {
            // 第一种：用默认的提示语。
            Log.e("BankMapFragment", "获取位置: 用户否勾选了不再提示并且拒绝了权限");
//            AndPermission.defaultSettingDialog(this, REQUEST_CODE_SETTING).show();
            // 第二种：用自定义的提示语。
            AndPermission.defaultSettingDialog(this, REQUEST_CODE_SETTING)
                    .setTitle("权限申请失败")
                    .setMessage("位置相关权限已被关闭，请您到设置页面手动授权，否则功能无法正常使用！")
                    .setPositiveButton("好，去设置")
                    .show();
        }
    }

    private void startMapFragment(int position) {
        Bundle bundle = new Bundle();
        bundle.putParcelable("mapInfo", entities.get(position));
        MapFragment mapFragment = new MapFragment();
        mapFragment.setArguments(bundle);
        FragmentUtils.startCoolFragment(mActivity, mapFragment, R.id.fl_container_content);
        EventBus.getDefault().post(entities.get(position).getName());
    }

    private class MapListResponseHandler extends AsyncHttpResponseHandler {

        @Override
        public void onSuccess(int i, Header[] headers, byte[] bytes) {
            try {
                JSONObject response = new JSONObject(new String(bytes));
                JSONArray ary = response.getJSONArray("data");
                entities = GsonTools.getProdjects(ary.toString(), MapListEntity[].class);
                if (entities != null) {
                    BankMapAdapter bankMapAdapter = new BankMapAdapter(entities);
                    lvBankMap.setAdapter(bankMapAdapter);
                }
                Zz();
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        @Override
        public void onFailure(int i, Header[] headers, byte[] bytes, Throwable throwable) {
            loadingPager.showErrorView();
        }

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // TODO: inflate a fragment view
        View rootView = super.onCreateView(inflater, container, savedInstanceState);
        ButterKnife.bind(this, rootView);
        return rootView;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }
}
