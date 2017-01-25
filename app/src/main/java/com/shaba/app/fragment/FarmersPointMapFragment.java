package com.shaba.app.fragment;

import android.os.Bundle;
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

    @Override
    public View initFragment() {
        View view = inflater.inflate(R.layout.fragment_farmers_point, null);
        ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void requestData() {
        appUtil.getBankPoint(new FarmersPointResponseHandler(),token);
        elv_farmers.setGroupIndicator(null);
        elv_farmers.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {
            @Override
            public boolean onChildClick(ExpandableListView parent, View v,
                                        int groupPosition, int childPosition, long id) {
                if (StringUtil.isFastClick())
                    return false;
                Bundle bundle = new Bundle();
                bundle.putParcelable("mapInfo",item_list.get(groupPosition).get(childPosition));
                MapFragment mapFragment = new MapFragment();
                mapFragment.setArguments(bundle);
                FragmentUtils.startCoolFragment(mActivity,mapFragment,R.id.fl_container_content);
                EventBus.getDefault().post(item_list.get(groupPosition).get(childPosition).getName());
                return false;
            }
        });
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
                JSONObject obj=new JSONObject(new String(bytes));
                JSONArray jary = obj.getJSONArray("data");
                group_list = new ArrayList<String>();
                item_list = new ArrayList<List<MapListEntity>>();
                for (int i = 0; i < jary.length(); i++) {
                    JSONObject jobj = jary.getJSONObject(i);
                    group_list.add(jobj.getString("key"));
                    JSONArray ary = jobj.getJSONArray("value");
                    List<MapListEntity> mapListEntities = GsonTools.getProdjects(ary.toString(),MapListEntity[].class );
                    item_list.add(mapListEntities);
                }
                if (adapter==null) {
                    adapter = new FarmersPointAdapter();
                    elv_farmers.setAdapter(adapter);
                }else {
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
