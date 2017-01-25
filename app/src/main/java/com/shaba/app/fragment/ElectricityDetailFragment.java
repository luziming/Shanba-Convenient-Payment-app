package com.shaba.app.fragment;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.loopj.android.http.JsonHttpResponseHandler;
import com.shaba.app.R;
import com.shaba.app.fragment.base.BaseFragment;
import com.shaba.app.global.ConstantUtil;
import com.shaba.app.utils.StringUtil;
import com.shaba.app.utils.ToastUtils;
import com.shaba.app.view.CustomProgressDialog;
import com.unionpay.UPPayAssistEx;

import org.apache.http.Header;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
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
public class ElectricityDetailFragment extends BaseFragment implements View.OnClickListener {

    @Bind(R.id.elec_jfxm)
    TextView jfxm;          //缴费项目
    @Bind(R.id.elec_gdbh)
    TextView gdbh;          //供电编号
    @Bind(R.id.elec_yhmc)
    TextView yhmc;          //用户名称
    @Bind(R.id.elec_jyje)
    TextView jyje;          //结余金额
    @Bind(R.id.elec_wyj)
    TextView wyj;           //违约金
    @Bind(R.id.ll_wyj)
    LinearLayout llWyj;
    @Bind(R.id.vw_wyj)
    View vwWyj;
    @Bind(R.id.elec_yjdf)
    TextView qfje;          //欠费金额
    @Bind(R.id.elec_sjje)
    EditText et_jfje;       //缴费金额
    @Bind(R.id.electricity_submit)
    Button submit;

    private String orig_qry_id;
    private String customer_id;
    private String oweTag;
    private CustomProgressDialog mLoadingDialog;
    private String companyID;

    @Override
    public View initViews() {
        View view = inflater.inflate(R.layout.fragment_elec_detail, null);
        ButterKnife.bind(this, view);
        submit.setOnClickListener(this);
        return view;
    }

    @Override
    public void initData() {
        try {
            Bundle arguments = getArguments();
            String data = arguments.getString("data");
            companyID = arguments.getString("companyID");
            String userCode = arguments.getString("userCode");
            JSONObject obj = new JSONObject(data);
            obj = obj.getJSONObject("obj");
            orig_qry_id = obj.getString("orig-qry-id");
            customer_id = obj.getString("customer_id");
            // 缴费项目
            if (!obj.isNull("title")) {
                String title = obj.getString("title");
                jfxm.setText(title);
            }
            // 供电编号
            gdbh.setText(userCode);
            //用户名称
            if (!obj.isNull("customer_name")) {
                String yhmc = obj.getString("customer_name");
                this.yhmc.setText(yhmc);
            }

            if (!obj.isNull("owe_tag")) {
                oweTag = obj.getString("owe_tag");
            }
            // 结余金额
            String jyjeStr = "";
            if (!obj.isNull("amountstr")) {
                if (companyID.equals("2")) {
                    //如果等于C，说明已经欠费
                    if (oweTag.equals("C")) {
                        jyjeStr = obj.getString("amountstr");
                        jyje.setText(jyjeStr + "元");
                        qfje.setText("0.0" + "元");
                    } else {
                        jyje.setText("0.0" + "元");
                        qfje.setText(jyjeStr + "元");
                    }
                } else {
                    qfje.setText(obj.getString("amountstr") + "元");
                }
            }
            if (!obj.isNull("balance_amount")) {//余额
                String balance = obj.getString("balance_amount");
                float balanceF = Float.parseFloat(balance);
                if (balanceF >= 0) {
                    jyje.setText(balance + "元");
                } else {
                    jyje.setText("0.0" + "元");
                    qfje.setText(balance + "元");
                }
            }
            //违约金
            if (!obj.isNull("penalty")) {
                llWyj.setVisibility(View.VISIBLE);
                vwWyj.setVisibility(View.VISIBLE);
                wyj.setText(obj.getString("penalty"));
            }
        } catch (JSONException e) {
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

    @Override
    public void onClick(View v) {
        String amount = et_jfje.getText().toString().trim();
        if (StringUtil.isFastClick())
            return;
        if (TextUtils.isEmpty(amount)) {
            ToastUtils.showToast("缴费金额不能为空!");
            return;
        }
        unionPay(amount);
    }

    private void unionPay(String amount) {
        mLoadingDialog = CustomProgressDialog.createDialog(mActivity);
        mLoadingDialog.setMessage("请稍后...");
        mLoadingDialog.setCancelable(false);
        mLoadingDialog.show();
        /*************************************************
         * 步骤1：从网络开始,获取交易流水号即TN
         ************************************************/
        Map<Object, Object> map = new HashMap<Object, Object>();
        map.put("txnAmt", amount);
        map.put("origQryId", orig_qry_id);
        map.put("usr_num", customer_id);
        appUtil.getTnFromUnionPay(map, new MyResponseHandler(),companyID , token);
    }

    private class MyResponseHandler extends JsonHttpResponseHandler {

        @Override
        public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
            try {
                if (!response.getBoolean("success")) {
                    ToastUtils.showToast(response.getString("error"));
                    return;
                }
                String tn = "";
                response = response.getJSONObject("obj");
                if (!response.isNull("tn")) {
                    tn = response.getString("tn");
                }
                if (!response.isNull("unionPayOrderId")) {
                    String unionPayOrderId = response.getString("unionPayOrderId");
                }
                inUnionPayPage(tn);
            } catch (Exception e) {
            }
        }

        @Override
        public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
            if (mLoadingDialog != null && mLoadingDialog.isShowing()) {
                ToastUtils.showToast(mActivity.getResources().getString(R.string.error_wifi_disconnect));
                mLoadingDialog.dismiss();
                mLoadingDialog = null;
            }
        }

        @Override
        public void onFinish() {
            if (mLoadingDialog != null && mLoadingDialog.isShowing()) {
                mLoadingDialog.dismiss();
                mLoadingDialog = null;
            }
        }
    }

    private void inUnionPayPage(String tn) {
        if (mLoadingDialog != null && mLoadingDialog.isShowing()) {
            mLoadingDialog.dismiss();
            mLoadingDialog = null;
        }
        if (tn == null || tn.length() == 0) {
            AlertDialog.Builder builder = new AlertDialog.Builder(mActivity);
            builder.setTitle("错误提示");
            builder.setMessage(R.string.error_wifi_disconnect);
            builder.setNegativeButton("确定", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                }
            });
            builder.create().show();
        } else {
            /*************************************************
             * 步骤2：通过银联工具类启动支付插件
             ************************************************/
            Log.e("ElectricityDetailFragment", "TN号: " + tn);
            UPPayAssistEx.startPay(mActivity, null, null, tn, ConstantUtil.mMode);
            getFragmentManager().popBackStack();
        }
    }
}
