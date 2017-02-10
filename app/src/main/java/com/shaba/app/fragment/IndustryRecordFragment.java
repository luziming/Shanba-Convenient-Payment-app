package com.shaba.app.fragment;

import android.os.Handler;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.google.gson.Gson;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.shaba.app.R;
import com.shaba.app.adapter.IndustryRecordAdapter;
import com.shaba.app.been.IndustryRecordEntity;
import com.shaba.app.been.IndustryRecords;
import com.shaba.app.fragment.base.BaseLoadingFragment;
import com.shaba.app.utils.ToastUtils;
import com.shaba.app.view.CustomLoadMoreView;

import org.apache.http.Header;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

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
public class IndustryRecordFragment extends BaseLoadingFragment implements SwipeRefreshLayout.OnRefreshListener {

    @Bind(R.id.recycleView_products)
    RecyclerView recycleView;
    @Bind(R.id.swipeRefresh)
    SwipeRefreshLayout swipeRefresh;
    @Bind(R.id.toolbar)
    Toolbar toolbar;
    private String token1 = "Token eyJhbGciOiJIUzUxMiIsInR5cCI6IkpXVCJ9.eyJ1c2VyIjoiMTNlNTk5NDktM2NmYi00OTc0LTk5YjktYzc0ZjY4ZDZmZGViIiwib3JnIjoxLCJ1dWlkIjoiZTYwMTQyYzItZDRmYy00NTU5LTgyNTUtMDFiNjk5NTQyMWQwIiwiZXhwIjoxNDg3MjA5NTE4fQ.rzruj4XZwBbv7Y8HKICjL_4IH1IOeB7CgV437X-4-Js6VOL61t4Y0C8CXiTJr8P6RSxqROsFB8eEHuruQYzAWg";
    private int current_page;
    private int total;
    private boolean isPullToDown = true;
    private List<IndustryRecordEntity> mDatas = new ArrayList<>();
    private IndustryRecordAdapter adapter;

    @Override
    public View initFragment() {
        View view = inflater.inflate(R.layout.fragment_products_toolbar, null);
        ButterKnife.bind(this, view);
        swipeRefresh.setOnRefreshListener(this);
        initToolbar(view);
        return view;
    }

    private void initToolbar(View view) {
        toolbar.setTitle("");//设置Toolbar标题
        TextView title = (TextView) view.findViewById(R.id.toolbar_title);
        title.setText("缴费记录");
        mActivity.setSupportActionBar(toolbar);
        setHasOptionsMenu(true);
        mActivity.getSupportActionBar().setHomeButtonEnabled(true); //设置返回键可用
        mActivity.getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        menu.clear();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            mActivity.onBackPressed();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void requestData() {
        getMoreRecord(0);
        loadingPager.showSuccessView();
    }

    @Override
    public void onRefresh() {
        isPullToDown = true;
        requestData();
    }
    private void getMoreRecord(int countPage) {
        RequestParams params = new RequestParams();
        params.put("current_page", countPage);
        appUtil.getPaymentRecord(new RecordResponseHandler(), params, token1);
    }
    private class RecordResponseHandler extends AsyncHttpResponseHandler{

        @Override
        public void onSuccess(int i, Header[] headers, byte[] bytes) {
            try {
                JSONObject obj = new JSONObject(new String(bytes));
                if (obj.getBoolean("success")) {
                    IndustryRecords recordEntity = new Gson().fromJson(obj.getJSONObject("data").toString(), IndustryRecords.class);
                    //首次仅此以及下拉刷新
                    if (isPullToDown) {
                        current_page = recordEntity.getCurrent_page();
                        total = recordEntity.getTotal();
                        List<IndustryRecordEntity> dataBeen = recordEntity.getData();
//                        if (dataBeen != null && dataBeen.size() > 0) {
                        if (mDatas.size() > 0) {
                            mDatas.clear();
                        }
                        mDatas.addAll(dataBeen);

                        adapter = new IndustryRecordAdapter(mDatas);
                        //是否执行一次
                        adapter.isFirstOnly(false);
                        //设置空布局
                        adapter.setEmptyView(inflater.inflate(R.layout.recycle_empty_view, null));
                        //自定义加载更多布局
                        adapter.setLoadMoreView(new CustomLoadMoreView());
                        //设置加载更多
                        if (mDatas.size() > 10) {
                            adapter.setOnLoadMoreListener(new BaseQuickAdapter.RequestLoadMoreListener() {
                                @Override
                                public void onLoadMoreRequested() {
                                    recycleView.postDelayed(new Runnable() {
                                        @Override
                                        public void run() {
                                            ToastUtils.showToast("加载完成");
                                            current_page++;
                                            isPullToDown = false;
                                            if (current_page >= total) {
                                                ToastUtils.showToast("已显示全部数据");
                                                adapter.loadMoreEnd();
                                                return;
                                            } else {
                                                getMoreRecord(current_page);
                                            }
                                        }

                                    }, 400);
                                }
                            });
                        }
                        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
                        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
                        recycleView.setLayoutManager(layoutManager);
                        //如果正在下拉刷新
                        if (swipeRefresh.isRefreshing()) {
                            new Handler().postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    ToastUtils.showToast("刷新成功");
                                    recycleView.setAdapter(adapter);
                                    swipeRefresh.setRefreshing(false);
                                }
                            }, 1000);
                        } else {
                            recycleView.setAdapter(adapter);
                        }
                        loadingPager.showSuccessView();
//                        }
                    } else {//上拉加载成功
                        mDatas.addAll(recordEntity.getData());
                        adapter.notifyDataSetChanged();
                        if (adapter.isLoading()) {
                            adapter.loadMoreComplete();
                        }
                    }
                } else {
                    loadingPager.showErrorView();
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        @Override
        public void onFailure(int i, Header[] headers, byte[] bytes, Throwable throwable) {
            if (adapter != null && adapter.isLoading()) {
                adapter.loadMoreFail();
            } else {
                loadingPager.showErrorView();
            }
        }
    }
}
