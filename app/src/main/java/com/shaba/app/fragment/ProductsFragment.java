package com.shaba.app.fragment;

import android.os.Bundle;
import android.os.Handler;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.listener.OnItemClickListener;
import com.google.gson.Gson;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.shaba.app.R;
import com.shaba.app.adapter.ProductsRecycleAdapter;
import com.shaba.app.been.ProductsEntity.DataBean;
import com.shaba.app.been.ProductsEntity;
import com.shaba.app.fragment.base.BaseLoadingFragment;
import com.shaba.app.fragment.base.FragmentUtils;
import com.shaba.app.utils.SBLog;
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
public class ProductsFragment extends BaseLoadingFragment implements SwipeRefreshLayout.OnRefreshListener {


    //    @Bind(R.id.lv_industry)
//    ListView lvIndustry;
    @Bind(R.id.recycleView_products)
    RecyclerView recyclerView;
    @Bind(R.id.swipeRefresh)
    SwipeRefreshLayout refreshLayout;


    private List<DataBean> mDatas = new ArrayList<>();

    private int type_id;
    private int current_page;
    private int total;
    private ProductsRecycleAdapter adapter;
    private boolean isPullToDown = true;
    private List<DataBean> dataBeen;


    @Override
    public View initFragment() {
        View view = inflater.inflate(R.layout.fragment_products, null);
        ButterKnife.bind(this, view);
        type_id = getArguments().getInt("type_id");
        refreshLayout.setOnRefreshListener(this);
        recyclerView.addOnItemTouchListener(new OnItemClickListener() {
            @Override
            public void onSimpleItemClick(BaseQuickAdapter adapter, View view, int position) {
                ProductsUserFragment fragment = new ProductsUserFragment();
                Bundle bundle = new Bundle();
                bundle.putInt("type_id",type_id);
                bundle.putString("name",dataBeen.get(position).getName());
                fragment.setArguments(bundle);
                FragmentUtils.startCoolFragment(mActivity, fragment, R.id.fl_container_industry,"industry_payment");
            }
        });

        return view;
    }

    @Override
    public void requestData() {
        RequestParams params = new RequestParams();
        params.put("type_id", type_id);
        appUtil.selectFromType(new SelectAllPaymentResponseHandler(), params, token);
    }

    private void getMoreProduct(int countPage) {
        RequestParams params = new RequestParams();
        params.put("type_id", type_id);
        params.put("current-page", countPage);
        appUtil.selectFromType(new SelectAllPaymentResponseHandler(), params, token);
    }

    private class SelectAllPaymentResponseHandler extends AsyncHttpResponseHandler {

        @Override
        public void onSuccess(int i, Header[] headers, byte[] bytes) {
            try {
                JSONObject obj = new JSONObject(new String(bytes));
                SBLog.json("obj", new String(bytes));
                if (obj.getBoolean("success")) {
                    ProductsEntity products =  new Gson().fromJson(obj.getJSONObject("data").toString(), ProductsEntity.class);
                    //首次仅此以及下拉刷新
                    if (products != null && isPullToDown) {
                        current_page = products.getCurrent_page();
                        total = products.getTotal();
                        dataBeen = products.getData();
                        if (dataBeen != null && dataBeen.size() > 0) {
                            if (mDatas.size() > 0) {
                                mDatas.clear();
                            }
                            mDatas.addAll(dataBeen);
                            Log.e("SelectAllPaymentResponseHandler", "List: " + mDatas.toString());
                            adapter = new ProductsRecycleAdapter(mDatas);
                            //加载动画
                            adapter.openLoadAnimation(BaseQuickAdapter.ALPHAIN);
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
                                        recyclerView.postDelayed(new Runnable() {
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
                                                    getMoreProduct(current_page);
                                                }

                                            }

                                        }, 400);
                                    }
                                });
                            }
                            LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
                            layoutManager.setSmoothScrollbarEnabled(true);
                            layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
                            recyclerView.setLayoutManager(layoutManager);
                            recyclerView.setNestedScrollingEnabled(false);
                            recyclerView.setAdapter(adapter);
                        }
                        //如果正在下拉刷新
                        if (refreshLayout.isRefreshing()) {
                            new Handler().postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    ToastUtils.showToast("刷新成功");
                                    refreshLayout.setRefreshing(false);
                                }
                            }, 1000);
                        }
                    } else {//上拉加载成功
                        mDatas.addAll(products.getData());
                        adapter.addData(products.getData());
                        if (adapter.isLoading()) {
                            adapter.loadMoreComplete();
                        }
                    }
                } else {
                    loadingPager.showErrorView();
                }
                loadingPager.showSuccessView();
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

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // TODO: inflate a fragment view
        View rootView = super.onCreateView(inflater, container, savedInstanceState);
        ButterKnife.bind(this, rootView);
        return rootView;
    }

    @Override
    public void onRefresh() {
        isPullToDown = true;
        requestData();
    }

    @Override
    public void onPause() {
        super.onPause();
        if (refreshLayout.isRefreshing())
            refreshLayout.setRefreshing(false);
    }


}
