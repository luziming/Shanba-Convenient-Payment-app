package com.shaba.app.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.handmark.pulltorefresh.library.ILoadingLayout;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.shaba.app.R;
import com.shaba.app.adapter.NewsListAdapater;
import com.shaba.app.been.NewsListEntity;
import com.shaba.app.fragment.base.BaseLoadingFragment;
import com.shaba.app.fragment.base.FragmentUtils;
import com.shaba.app.utils.GsonTools;
import com.shaba.app.utils.ToastUtils;

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
public class MoreNewsFragment extends BaseLoadingFragment {

    @Bind(R.id.plv_newsList)
    PullToRefreshListView plvNewsList;

    private ArrayList<NewsListEntity> allNewList = new ArrayList<>();
    private int countPage = 1;
    private int totalPage;
    private boolean isPullDown = false;
    private NewsListAdapater listAdapater;

    @Override
    public View initFragment() {
        View view = inflater.inflate(R.layout.fragment_more_news, null);
        ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void requestData() {
        getNewList(countPage);

        plvNewsList.setMode(PullToRefreshBase.Mode.BOTH);

        ILoadingLayout startLabelse = plvNewsList.getLoadingLayoutProxy(true, false);
        startLabelse.setPullLabel("下拉可以加载更多");// 刚下拉时，显示的提示
        startLabelse.setRefreshingLabel("加载中");// 刷新时
        startLabelse.setReleaseLabel("松开加载更多");// 下来达到一定距离时，显示的提示

        ILoadingLayout endLabelsr = plvNewsList.getLoadingLayoutProxy(false, true);
        endLabelsr.setPullLabel("上拉可以刷新");// 刚下拉时，显示的提示
        endLabelsr.setLastUpdatedLabel("正在刷新");// 刷新时
        endLabelsr.setReleaseLabel("松开后刷新");// 下来达到一定距离时，显示的提示
        plvNewsList.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                position = position - 1;
                String url = allNewList.get(position).getUrl();
                String title = allNewList.get(position).getTitle();
                NewsDetailFragment fragment = new NewsDetailFragment();
                Bundle bundle = new Bundle();
                bundle.putString("url",url);
                fragment.setArguments(bundle);
                FragmentUtils.startCoolFragment(mActivity,fragment,R.id.fl_container_content);
                EventBus.getDefault().post(title);
            }
        });
        plvNewsList.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener2<ListView>() {

            @Override
            public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView) {
                countPage = 1;
                isPullDown = true;
                getNewList(countPage);
            }

            @Override
            public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView) {
                countPage++;
                if (countPage > totalPage) {
                    ToastUtils.showToast("已显示全部数据");
                    plvNewsList.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            plvNewsList.onRefreshComplete();
                        }
                    }, 500);
                    return;
                }
                getNewList(countPage);
            }
        });
    }

    private void getNewList(int Page) {
        RequestParams params = new RequestParams();
        params.put("current-page", Page);
        appUtil.getNewListMore(new NewListResponseHandler(), params, token);
    }

    /**
     * 处理 新闻信息:
     */
    private class NewListResponseHandler extends AsyncHttpResponseHandler {

        @Override
        public void onSuccess(int arg0, Header[] arg1, byte[] arg2) {

            JSONObject response = null;
            try {
                response = new JSONObject(new String(arg2));
                JSONArray ary = response.getJSONArray("data");
                totalPage = response.getInt("total-page");
                List<NewsListEntity> newsList = GsonTools.getProdjects(ary.toString(), NewsListEntity[].class);
                if (isPullDown) {
                    if (allNewList != null) {
                        allNewList.clear();
                        isPullDown = false;
                    }
                }
                allNewList.addAll(newsList);
                if (listAdapater == null) {
                    listAdapater = new NewsListAdapater(allNewList);
                    plvNewsList.setAdapter(listAdapater);
                    Zz();
                } else {
                    listAdapater.notifyDataSetChanged();
                }
                plvNewsList.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        plvNewsList.onRefreshComplete();
                    }
                }, 500);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        @Override
        public void onFailure(int arg0, Header[] arg1, byte[] arg2, Throwable arg3) {
            loadingPager.showErrorView();
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
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
