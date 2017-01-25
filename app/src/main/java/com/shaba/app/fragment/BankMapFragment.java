package com.shaba.app.fragment;

import android.os.Bundle;
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

    @Override
    public View initFragment() {
        View view = inflater.inflate(R.layout.fragment_bank_map, null);
        ButterKnife.bind(this, view);
        lvBankMap.setOnItemClickListener(this);
        return view;
    }

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
        Bundle bundle = new Bundle();
        bundle.putParcelable("mapInfo",entities.get(position));
        MapFragment mapFragment = new MapFragment();
        mapFragment.setArguments(bundle);
        FragmentUtils.startCoolFragment(mActivity,mapFragment,R.id.fl_container_content);
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
