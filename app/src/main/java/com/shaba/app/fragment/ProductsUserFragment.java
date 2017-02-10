package com.shaba.app.fragment;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.entity.MultiItemEntity;
import com.chad.library.adapter.base.listener.OnItemChildClickListener;
import com.google.gson.Gson;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.shaba.app.R;
import com.shaba.app.adapter.ProductUserAdapter;
import com.shaba.app.been.DataBean;
import com.shaba.app.been.LvFatherEntity;
import com.shaba.app.been.PersonEntity;
import com.shaba.app.been.UserEntity;
import com.shaba.app.fragment.base.BaseLoadingFragment;
import com.shaba.app.utils.SBLog;
import com.shaba.app.utils.StringUtil;
import com.shaba.app.utils.ToastUtils;
import com.shaba.app.utils.UnionPayUtils;
import com.shaba.app.view.CustomLoadMoreView;

import org.apache.http.Header;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
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
public class ProductsUserFragment extends BaseLoadingFragment implements SwipeRefreshLayout.OnRefreshListener {

    @Bind(R.id.recycleView_products)
    RecyclerView recycleView;
    @Bind(R.id.swipeRefresh)
    SwipeRefreshLayout swipeRefresh;
    @Bind(R.id.toolbar)
    Toolbar toolbar;

    private int current_page;
    private int total;
    private boolean isPullToDown = true;
    private List<DataBean> mDatas = new ArrayList<>();
    private ProductUserAdapter adapter;
    private String id;
    private int type;
    private Bundle arguments;

    @Override
    public View initFragment() {
        View view = inflater.inflate(R.layout.fragment_products_toolbar, null);
        ButterKnife.bind(this, view);
        arguments = getArguments();
        type = arguments.getInt("type");
        swipeRefresh.setOnRefreshListener(this);
        recycleView.addOnItemTouchListener(new OnItemChildClickListener() {
            @Override
            public void onSimpleItemChildClick(BaseQuickAdapter adapter, View view, int position) {
                if (StringUtil.isFastClick())
                    return;
                RequestParams params = new RequestParams();
                params.put("id",mDatas.get(position - 1).getId());
                params.put("merchant_id",mDatas.get(position - 1).getMerchant_id());
                UnionPayUtils.getInstance().showDialog(mActivity);
                appUtil.getIndustryTN(new GetTNResponseHandler(),params,token);
            }
        });
        initToolbar(view);
        return view;
    }

    private void initToolbar(View view) {
        toolbar.setTitle("");//设置Toolbar标题
        TextView title = (TextView) view.findViewById(R.id.toolbar_title);
        title.setText("用户列表");
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
        if (type == 1) {
            id = arguments.getString("id");
            RequestParams params = new RequestParams();
            params.put("id", id);
            appUtil.selectUserFromProduct(new AllUserResponseHandler(), params, token);
        } else {
            String search = arguments.getString("search");
            RequestParams params = new RequestParams();
            //如果输入的是数字
            if (search.matches("[0-9]*")) {
                params.put("user_code", search);
            } else {
                try {
                    params.put("user_name",  URLEncoder.encode(search, "UTF-8"));
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
            }
            appUtil.searchProduct(new SearchResultResponseHandler(), params, token);
        }

    }

    private void getMoreProduct(int countPage) {
        RequestParams params = new RequestParams();
        params.put("current_page", countPage);
        appUtil.selectUserFromProduct(new AllUserResponseHandler(), params, token);
    }

    private ArrayList<MultiItemEntity> generateData() {
        ArrayList<MultiItemEntity> res = new ArrayList<>();
        for (int i = 0; i < mDatas.size(); i++) {
            DataBean bean = mDatas.get(i);
            LvFatherEntity fatherEntity = new LvFatherEntity();
            fatherEntity.setStatus(bean.getStatus());
            fatherEntity.setUsername(bean.getUser_name());
            PersonEntity personEntity = new PersonEntity();
            personEntity.setUser_name(bean.getUser_name());
            personEntity.setMer_name(bean.getMer_name());
            personEntity.setAmount(bean.getAmount() + "");
            personEntity.setStatus(bean.getStatus());
            personEntity.setName(bean.getName());
            fatherEntity.addSubItem(personEntity);
            res.add(fatherEntity);
        }
        return res;
    }

    /**
     * 下拉刷新
     */
    @Override
    public void onRefresh() {
        isPullToDown = true;
        requestData();
    }

    private class AllUserResponseHandler extends AsyncHttpResponseHandler {

        @Override
        public void onSuccess(int i, Header[] headers, byte[] bytes) {
            try {
                JSONObject obj = new JSONObject(new String(bytes));
                if (obj.getBoolean("success")) {
                    UserEntity userEntity = new Gson().fromJson(obj.getJSONObject("data").toString(), UserEntity.class);
                    //首次仅此以及下拉刷新
                    if (isPullToDown) {
                        current_page = userEntity.getCurrent_page();
                        total = userEntity.getTotal();
                        List<DataBean> dataBeen = userEntity.getData();
//                        if (dataBeen != null && dataBeen.size() > 0) {
                        if (mDatas.size() > 0) {
                            mDatas.clear();
                        }
                        mDatas.addAll(dataBeen);
                        ArrayList<MultiItemEntity> list = generateData();

                        adapter = new ProductUserAdapter(list);
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
                                                getMoreProduct(current_page);
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
                        mDatas.addAll(userEntity.getData());
                        adapter.addData(generateData());
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

    private class SearchResultResponseHandler extends AsyncHttpResponseHandler {

        @Override
        public void onSuccess(int i, Header[] headers, byte[] bytes) {
            try {
                SBLog.json("jjjj",new String(bytes));
                JSONObject obj = new JSONObject(new String(bytes));
                if (obj.getBoolean("success")) {
                    UserEntity userEntity = new Gson().fromJson(obj.getJSONObject("data").toString(), UserEntity.class);
                    List<DataBean> dataBeen = userEntity.getData();
                    if (mDatas.size() > 0)
                        mDatas.clear();
                    mDatas.addAll(dataBeen);

                    ArrayList<MultiItemEntity> list = generateData();

                    adapter = new ProductUserAdapter(list);
                    //是否执行一次
                    adapter.isFirstOnly(false);
                    //设置空布局
                    adapter.setEmptyView(inflater.inflate(R.layout.recycle_empty_view, null));
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
                } else {
                    loadingPager.showErrorView();
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        @Override
        public void onFailure(int i, Header[] headers, byte[] bytes, Throwable throwable) {
            loadingPager.showErrorView();
        }
    }

    private class GetTNResponseHandler extends AsyncHttpResponseHandler {

        @Override
        public void onSuccess(int i, Header[] headers, byte[] bytes) {
            try {
                JSONObject obj = new JSONObject(new String(bytes));
                UnionPayUtils.getInstance().disMissDialog();
                if (obj.getBoolean("success")) {
                    String tn = obj.getJSONObject("obj").getString("tn");

                    if (TextUtils.isEmpty(tn)) {
                        UnionPayUtils.getInstance().checkTN(mActivity);
                        return;
                    }

                    UnionPayUtils.getInstance().unionPay(mActivity, tn);

                } else {
                    ToastUtils.showToast("请求无效,请稍后再试");
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }

        }

        @Override
        public void onFailure(int i, Header[] headers, byte[] bytes, Throwable throwable) {
            ToastUtils.showToast("请求失败,请稍后再试");
            UnionPayUtils.getInstance().disMissDialog();
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        /*************************************************
         * 步骤3：处理银联手机支付控件返回的支付结果
         ************************************************/
        if (data == null) {
            return;
        }

        String msg = "";
        /*
         * 支付控件返回字符串:success、fail、cancel 分别代表支付成功，支付失败，支付取消
         */
        String str = data.getExtras().getString("pay_result");

        if (str.equalsIgnoreCase("success")) {
            // 支付成功后，extra中如果存在result_data，取出校验
            // result_data结构见c）result_data参数说明
            if (data.hasExtra("result_data")) {
                String result = data.getExtras().getString("result_data");
                try {
                    JSONObject resultJson = new JSONObject(result);
                    String sign = resultJson.getString("sign");
                    String dataOrg = resultJson.getString("data");
                    // 验签证书同后台验签证书
                    // 此处的verify，商户需送去商户后台做验签
                    boolean ret = verify(dataOrg, sign, "00");
                    if (ret) {
                        // 验证通过后，显示支付结果
                        msg = "支付成功！";
                    } else {
                        // 验证不通过后的处理
                        // 建议通过商户后台查询支付结果
                        msg = "支付失败！";
                    }
                } catch (JSONException e) {

                }
            } else {
                // 未收到签名信息
                // 建议通过商户后台查询支付结果
                msg = "支付成功！";
            }
        } else if (str.equalsIgnoreCase("fail")) {
            msg = "支付失败！";
        } else if (str.equalsIgnoreCase("cancel")) {
            msg = "用户取消了支付";
        }
//        onBackPressed();
        AlertDialog.Builder builder = new AlertDialog.Builder(mActivity);
        builder.setTitle("支付结果通知");
        builder.setMessage(msg);
        builder.setInverseBackgroundForced(true);
        // builder.setCustomTitle();
        builder.setNegativeButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        builder.show();

        //刷新数据
        requestData();
    }
    private boolean verify(String msg, String sign64, String mode) {
        // 此处的verify，商户需送去商户后台做验签
        return true;
    }
}