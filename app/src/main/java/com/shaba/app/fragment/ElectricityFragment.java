package com.shaba.app.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Spinner;

import com.loopj.android.http.JsonHttpResponseHandler;
import com.shaba.app.R;
import com.shaba.app.activity.LoginActivity;
import com.shaba.app.been.PaymentCompany;
import com.shaba.app.fragment.base.BaseFragment;
import com.shaba.app.fragment.base.FragmentUtils;
import com.shaba.app.utils.ToastUtils;
import com.shaba.app.view.CustomProgressDialog;

import org.apache.http.Header;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
public class ElectricityFragment extends BaseFragment implements View.OnClickListener, AdapterView.OnItemClickListener {

    @Bind(R.id.spinner_choose)
    Spinner spinnerChoose;
    @Bind(R.id.ccav_electricity_code)
    AutoCompleteTextView ccavElectricityCode;
    @Bind(R.id.electricity_next)
    Button electricityNext;
    @Bind(R.id.ll_elec_notif)
    LinearLayout llElecNotif;

    private int companyID = 1;
    private CustomProgressDialog dialog;
    private String code;

    @Override
    public View initViews() {
        View view = inflater.inflate(R.layout.fragment_electricity, null);
        ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void initData() {
        //下一步
        electricityNext.setOnClickListener(this);
        ccavElectricityCode.setOnItemClickListener(this);
        initAutoComplete("electricity", ccavElectricityCode);
        //初始化Spinner
        List<PaymentCompany> companies = new ArrayList<>();
        companies.add(new PaymentCompany(1, "蒙西电力"));
        companies.add(new PaymentCompany(2, "杭后农电"));
        ArrayAdapter<PaymentCompany> spinnerAdapter = new ArrayAdapter<>(mActivity, android.R.layout.simple_spinner_item, companies);
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerChoose.setAdapter(spinnerAdapter);
        spinnerChoose.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position == 0) {
                    companyID = 1;
                    llElecNotif.setVisibility(View.GONE);
                } else {
                    companyID = 2;
                    llElecNotif.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
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

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.electricity_next:
                code = ccavElectricityCode.getText().toString().trim();
                if (TextUtils.isEmpty(code)) {
                    ToastUtils.showToast("供电编号不能为空！");
                    return;
                }
                dialog = CustomProgressDialog.createDialog(mActivity);
                dialog.setMessage("正在查询数据,请等待...");
                dialog.setCancelable(false);
                dialog.show();
                Map<Object, Object> map = new HashMap<Object, Object>();
                map.put("usr_num", code);
                appUtil.queryBillElec(map, token, new MyResponseHandler(), companyID + "");
            case R.id.ccav_electricity_code:
                break;
        }
    }

    private class MyResponseHandler extends JsonHttpResponseHandler {
        @Override
        public void onSuccess(int statusCode, Header[] headers,
                              JSONObject response) {
            try {
                if (dialog != null && dialog.isShowing()) {
                    dialog.dismiss();
                    dialog = null;
                }
                if (!response.getBoolean("success")) {
                    if (!response.isNull("error")) {
                        if ("noauth".equals(response.getString("error"))) {
                            ToastUtils.showToast("登陆已失效,请重新登陆");
                            Intent intent = new Intent(mActivity, LoginActivity.class);
                            startActivity(intent);
                            return;
                        }
                    }
                    if (!response.isNull("message")) {
                        ToastUtils.showToast(response.getString("message"));
                        return;
                    }
                }
                if (response != null) {
                    JSONObject obj = response.getJSONObject("obj");
                    ElectricityDetailFragment detailFragment = new ElectricityDetailFragment();
                    Bundle bundle = new Bundle();
                    bundle.putString("data", response.toString());
                    bundle.putString("companyID", companyID + "");
                    bundle.putString("userCode", code);
                    detailFragment.setArguments(bundle);
                    FragmentUtils.startCoolFragment(mActivity, detailFragment, R.id.fl_container_content);
                    saveHistory("electricity", code);
                }
            } catch (Exception e) {
                e.getMessage();
            }
        }

        @Override
        public void onFailure(int statusCode, Header[] headers,
                              Throwable throwable, JSONObject errorResponse) {
            if (dialog != null && dialog.isShowing()) {
                dialog.dismiss();
                dialog = null;
            }
            ToastUtils.showToast("网络连接失败,请稍后重试!");
        }

        @Override
        public void onFinish() {
            if (dialog != null && dialog.isShowing()) {
                dialog.dismiss();
                dialog = null;
            }
        }
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        ccavElectricityCode.setFocusable(false);
        ccavElectricityCode.setFocusableInTouchMode(true);
    }
}
