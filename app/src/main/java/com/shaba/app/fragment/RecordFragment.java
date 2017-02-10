package com.shaba.app.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.text.format.Time;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Spinner;

import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.shaba.app.R;
import com.shaba.app.activity.LoginActivity;
import com.shaba.app.activity.MainActivity;
import com.shaba.app.adapter.BillListAdapater;
import com.shaba.app.been.BillListEntity;
import com.shaba.app.fragment.base.BaseFragment;
import com.shaba.app.fragment.base.FragmentUtils;
import com.shaba.app.utils.GsonTools;
import com.shaba.app.utils.SBLog;
import com.shaba.app.utils.StringUtil;
import com.shaba.app.utils.ToastUtil;

import org.apache.http.Header;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

import static com.shaba.app.global.MyApplication.context;

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
public class RecordFragment extends BaseFragment implements View.OnClickListener, AdapterView.OnItemSelectedListener {

    @Bind(R.id.spinner_year)
    Spinner spinnerYear;
    @Bind(R.id.spinner_month)
    Spinner spinnerMonth;
    @Bind(R.id.btn_query)
    Button btnQuery;
    @Bind(R.id.plv_billsList)
    PullToRefreshListView plvBillsList;

    private String mYearDefault = "选  择  年";
    private String mMonthDefault = "选  择  月";
    //当前年月
    private int mCurrentMonth;
    private int mCurrentYear;
    //查询年月
    private String mQueryTime;

    private ArrayList<String> mYears;
    private ArrayList<String> mMonths;

    private int mTotalPage;
    private int mCurrentPage;

    private List<BillListEntity> mAllBillList = new ArrayList<>();
    private BillListAdapater mListAdapater;
    //是否为下拉刷新
    private boolean isPullDownRefresh = true;
    private Time time;

    @Override
    public View initViews() {
        View view = inflater.inflate(R.layout.fragment_bill, null);
        ButterKnife.bind(this, view);
        btnQuery.setOnClickListener(this);
        return view;
    }

    @Override
    public void initData() {
        getDate();
        queryBill();
        //初始化Spinner
        initSpinner(mMonths, spinnerMonth);
        spinnerMonth.setOnItemSelectedListener(this);
        initSpinner(mYears, spinnerYear);
        spinnerYear.setOnItemSelectedListener(this);
        plvBillsList.setMode(PullToRefreshBase.Mode.BOTH);
        plvBillsList.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener2<ListView>() {
            @Override
            public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView) {
                isPullDownRefresh = true;
                plvBillsList.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        plvBillsList.onRefreshComplete();
                    }
                }, 500);
            }

            @Override
            public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView) {
                isPullDownRefresh = false;
                mCurrentPage++;
                if (mCurrentPage > mTotalPage) {
                    ToastUtil.show(context, "已显示全部数据!");
                    plvBillsList.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            plvBillsList.onRefreshComplete();
                        }
                    }, 500);
                    return;
                }
                plvBillsList.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        plvBillsList.onRefreshComplete();
                    }
                }, 500);
//                queryBill();
                getBillList(mCurrentPage, mQueryTime);
            }
        });
        //设置点击事件,点击进入详情界面
        plvBillsList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (StringUtil.isFastClick())
                    return;
                BillDetailFragment fragment = new BillDetailFragment();
                Bundle bundle = new Bundle();
                bundle.putString("url", mAllBillList.get(position - 1).getUrl());
                fragment.setArguments(bundle);
                FragmentUtils.startCoolFragment(mActivity, fragment, R.id.fl_container);
                //菜单键变返回键
                ((MainActivity)mActivity).mDrawerToggle.setDrawerIndicatorEnabled(false);
                ((MainActivity)mActivity).toolbar.setNavigationOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        mActivity.onBackPressed();
//                        ((MainActivity)mActivity).mDrawerToggle.setDrawerIndicatorEnabled(true);
                        ((MainActivity)mActivity).setBackEnable();
                    }
                });
            }
        });

    }

    private void queryBill() {
        appUtil.getBillList(new BillListResponseHandler(), new RequestParams(), token);
    }

    protected void getBillList(int countPage, String date) {
        RequestParams requestParams = new RequestParams();
        requestParams.put("current-page", countPage);
        requestParams.put("query-time", date);
        appUtil.getBillList(new BillListResponseHandler(), requestParams, token);
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

    private void getDate() {
        time = new Time("GMT+8");
        time.setToNow();
        mCurrentYear = time.year;
        mCurrentMonth = time.month + 1;
        mYears = new ArrayList<>();
        mMonths = new ArrayList<>();
        mMonths.add(mMonthDefault);
        for (int i = 0; i < 12; i++) {
            mMonths.add((i + 1) + "月");
        }
        mYears.add(mYearDefault);
        for (int i = 0; i < 6; i++) {
            mYears.add("" + (mCurrentYear - i) + "年");
        }
        getQueryTime();
    }

    /**
     * 刷新查询时间
     */
    private void getQueryTime() {

        mCurrentYear = time.year;
        mCurrentMonth = time.month;

        if (mCurrentMonth < 10) {
            mQueryTime = mCurrentYear + "0" + mCurrentMonth;
        } else {
            mQueryTime = mCurrentYear + mCurrentMonth + "";
        }
    }

    @Override
    public void onClick(View v) {
        if (StringUtil.isFastClick())
            return;
        isPullDownRefresh = true;
        if (spinnerYear.getSelectedItemPosition() == 0) {
            mCurrentYear = time.year;
        }
        if (mCurrentMonth < 10) {
            mQueryTime = mCurrentYear + "0" + mCurrentMonth;
        } else {
            mQueryTime = mCurrentYear + "" + mCurrentMonth;
        }
        if (spinnerMonth.getSelectedItemPosition() == 0) {
            mQueryTime = mCurrentYear + "";
        }
        Log.e("RecordFragment", "当前年: " + mCurrentYear + ",当前月:" + mCurrentMonth + ",查询时间:" + mQueryTime);
        getBillList(0, mQueryTime);
    }

    /**
     * 初始化Spinner
     * @param list
     */
    private void initSpinner(List<String> list, Spinner spinner) {
        ArrayAdapter<String> spinnerAdapter = new ArrayAdapter<>(mActivity, android.R.layout.simple_spinner_item, list);
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(spinnerAdapter);
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        switch (parent.getId()) {
            case R.id.spinner_month:
                if (!mMonths.get(position).equals(mMonthDefault)) {
                    mCurrentMonth = Integer.valueOf(mMonths.get(position).replace("月", ""));
                }
                break;
            case R.id.spinner_year:
                if (!mYears.get(position).equals(mYearDefault)) {
                    mCurrentYear = Integer.valueOf(mYears.get(position).replace("年", ""));
                }
                break;
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    private class BillListResponseHandler extends AsyncHttpResponseHandler {
        @Override
        public void onSuccess(int arg0, Header[] arg1, byte[] arg2) {
            JSONObject response = null;
            try {
                response = new JSONObject(new String(arg2));
                SBLog.json("账单信息", response.toString());
                if (!response.getBoolean("success")) {
                    ToastUtil.show(context, "请重新登陆!");
                    startActivity(new Intent(mActivity, LoginActivity.class));
                    mActivity.overridePendingTransition(R.anim.activity_open, R.anim.activity_close);
                    mActivity.finish();
                    return;
                }
                JSONArray ary = response.getJSONArray("data");
                mTotalPage = response.getInt("total-page");
                mCurrentPage = response.getInt("current-page");
                List<BillListEntity> billList = GsonTools.getProdjects(ary.toString(), BillListEntity[].class);
                if (!isPullDownRefresh) {
                    mAllBillList.addAll(billList);
                } else {
                    mAllBillList.clear();
                    mAllBillList.addAll(billList);
                }
                if (mListAdapater == null) {
                    mListAdapater = new BillListAdapater(context, mAllBillList);
                    plvBillsList.setAdapter(mListAdapater);
                } else {
                    mListAdapater.notifyDataSetChanged();
                }
                if (plvBillsList.isRefreshing()) {
                    plvBillsList.onRefreshComplete();
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        @Override
        public void onFailure(int arg0, Header[] arg1, byte[] arg2,
                              Throwable arg3) {
            ToastUtil.show(context, R.string.error_wifi_disconnect);
        }
    }
}
