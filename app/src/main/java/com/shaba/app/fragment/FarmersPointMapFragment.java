package com.shaba.app.fragment;

import android.Manifest;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.TextView;

import com.loopj.android.http.AsyncHttpResponseHandler;
import com.shaba.app.R;
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

import java.util.ArrayList;
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
public class FarmersPointMapFragment extends BaseLoadingFragment {


    @Bind(R.id.elv_farmers)
    ExpandableListView elv_farmers;

    private ArrayList<String> group_list;
    private ArrayList<List<MapListEntity>> item_list;
    private FarmersPointAdapter adapter;
    private static final int REQUEST_CODE_SETTING = 300;

    @Override
    public View initFragment() {
        View view = inflater.inflate(R.layout.fragment_farmers_point, null);
        ButterKnife.bind(this, view);
        return view;
    }

    private int mGroupPosition;
    private int mChildPosition;

    @Override
    public void requestData() {
        appUtil.getBankPoint(new FarmersPointResponseHandler(), token);
        elv_farmers.setGroupIndicator(null);
        elv_farmers.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {
            @Override
            public boolean onChildClick(ExpandableListView parent, View v,
                                        int groupPosition, int childPosition, long id) {
                if (StringUtil.isFastClick())
                    return false;
                FarmersPointMapFragment.this.mGroupPosition = groupPosition;
                FarmersPointMapFragment.this.mChildPosition = childPosition;
                //6.0之后需要动态申请权限
                // 先判断是否有权限。
                if (AndPermission.hasPermission(mActivity, Manifest.permission.READ_PHONE_STATE, Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.ACCESS_FINE_LOCATION)) {
                    Log.e("BankMapFragment", "获取位置: 检查权限权限" + AndPermission.hasPermission(mActivity, Manifest.permission.READ_PHONE_STATE, Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.ACCESS_FINE_LOCATION));
                    // 有权限，直接do anything
                    startMapFragment(groupPosition,childPosition);
                } else {
                    Log.e("BankMapFragment", "获取位置: 申请权限");
                    AndPermission.with(FarmersPointMapFragment.this)
                            .requestCode(100)
                            .permission(Manifest.permission.READ_PHONE_STATE, Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.ACCESS_FINE_LOCATION)
                            .rationale(new RationaleListener() {
                                @Override
                                public void showRequestPermissionRationale(int requestCode, Rationale rationale) {
                                    // 这里使用自定义对话框，如果不想自定义，用AndPermission默认对话框：
                                    AndPermission.rationaleDialog(mActivity, rationale).show();
                                }
                            })
                            .send();
                }
                return false;
            }
        });
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
        // 有权限，直接do anything.
        startMapFragment(mGroupPosition, mChildPosition);
    }

    // 失败回调的方法，用注解即可，里面的数字是请求时的requestCode。
    @PermissionNo(100)
    private void getLocationNo(List<String> deniedPermissions) {
        // 用户否勾选了不再提示并且拒绝了权限，那么提示用户到设置中授权。
        if (AndPermission.hasAlwaysDeniedPermission(mActivity, deniedPermissions)) {
            // 第二种：用自定义的提示语。
            AndPermission.defaultSettingDialog(this, REQUEST_CODE_SETTING)
                    .setTitle("权限申请失败")
                    .setMessage("位置相关权限已被关闭，请您到设置页面手动授权，否则功能无法正常使用！")
                    .setPositiveButton("好，去设置")
                    .show();
        }
    }

    /**
     * 显示地图
     *
     * @param groupPosition
     * @param childPosition
     */
    private void startMapFragment(int groupPosition, int childPosition) {
        Bundle bundle = new Bundle();
        bundle.putParcelable("mapInfo", item_list.get(groupPosition).get(childPosition));
        MapFragment mapFragment = new MapFragment();
        mapFragment.setArguments(bundle);
        FragmentUtils.startCoolFragment(mActivity, mapFragment, R.id.fl_container_content);
        EventBus.getDefault().post(item_list.get(groupPosition).get(childPosition).getName());
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

    private class FarmersPointResponseHandler extends AsyncHttpResponseHandler {

        @Override
        public void onSuccess(int id, Header[] headers, byte[] bytes) {
            try {
                JSONObject obj = new JSONObject(new String(bytes));
                JSONArray jary = obj.getJSONArray("data");
                group_list = new ArrayList<String>();
                item_list = new ArrayList<List<MapListEntity>>();
                for (int i = 0; i < jary.length(); i++) {
                    JSONObject jobj = jary.getJSONObject(i);
                    group_list.add(jobj.getString("key"));
                    JSONArray ary = jobj.getJSONArray("value");
                    List<MapListEntity> mapListEntities = GsonTools.getProdjects(ary.toString(), MapListEntity[].class);
                    item_list.add(mapListEntities);
                }
                if (adapter == null) {
                    adapter = new FarmersPointAdapter();
                    if (elv_farmers != null)
                        elv_farmers.setAdapter(adapter);
                } else {
                    adapter.notifyDataSetChanged();
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
            Zz();
        }

        @Override
        public void onFailure(int i, Header[] headers, byte[] bytes, Throwable throwable) {
            loadingPager.showErrorView();
        }
    }

    private class FarmersPointAdapter extends BaseExpandableListAdapter {

        @Override
        public int getGroupCount() {
            return group_list.size();
        }

        @Override
        public int getChildrenCount(int groupPosition) {
            return item_list.get(groupPosition).size();
        }

        @Override
        public Object getGroup(int groupPosition) {
            return group_list.get(groupPosition);
        }

        @Override
        public Object getChild(int groupPosition, int childPosition) {
            return item_list.get(groupPosition).get(childPosition);
        }

        @Override
        public long getGroupId(int groupPosition) {
            return groupPosition;
        }

        @Override
        public long getChildId(int groupPosition, int childPosition) {
            return childPosition;
        }

        @Override
        public boolean hasStableIds() {
            return true;
        }

        @Override
        public View getGroupView(int groupPosition, boolean isExpanded,
                                 View convertView, ViewGroup parent) {
            GroupHolder groupHolder = null;
            if (convertView == null) {
                convertView = inflater.inflate(R.layout.item_farmers_group, null);
                groupHolder = new GroupHolder();
                groupHolder.tv_farmers_group = (TextView) convertView.findViewById(R.id.tv_farmers_group);
                convertView.setTag(groupHolder);
            } else {
                groupHolder = (GroupHolder) convertView.getTag();
            }
            groupHolder.tv_farmers_group.setText(group_list.get(groupPosition));
            return convertView;
        }

        @Override
        public View getChildView(int groupPosition, int childPosition,
                                 boolean isLastChild, View convertView, ViewGroup parent) {
            ItemHolder itemHolder = null;
            if (convertView == null) {
                convertView = inflater.inflate(R.layout.item_farmers_point, null);
                itemHolder = new ItemHolder();
                itemHolder.tv_farmers_list = (TextView) convertView.findViewById(R.id.tv_farmers_list);
                convertView.setTag(itemHolder);
            } else {
                itemHolder = (ItemHolder) convertView.getTag();
            }
            itemHolder.tv_farmers_list.setText(item_list.get(groupPosition).get(
                    childPosition).getAddress());
            return convertView;
        }

        @Override
        public boolean isChildSelectable(int groupPosition, int childPosition) {
            return true;
        }
    }

    class GroupHolder {
        public TextView tv_farmers_group;
    }

    class ItemHolder {
        public TextView tv_farmers_list;
    }
}
