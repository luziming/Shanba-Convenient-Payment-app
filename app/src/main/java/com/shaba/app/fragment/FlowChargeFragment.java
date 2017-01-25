package com.shaba.app.fragment;

import android.content.ContentResolver;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.TextView;

import com.loopj.android.http.JsonHttpResponseHandler;
import com.shaba.app.R;
import com.shaba.app.activity.LoginActivity;
import com.shaba.app.adapter.PhoneChargeAdapter;
import com.shaba.app.fragment.base.BaseFragment;
import com.shaba.app.global.ConstantUtil;
import com.shaba.app.utils.RegexUtil;
import com.shaba.app.utils.StringUtil;
import com.shaba.app.utils.ToastUtils;
import com.shaba.app.utils.UnionPayUtils;
import com.shaba.app.view.MyGridView;

import org.apache.http.Header;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
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
public class FlowChargeFragment extends BaseFragment implements View.OnClickListener, AdapterView.OnItemClickListener {


    @Bind(R.id.recharge_tel_num)
    AutoCompleteTextView rechargeTelNum;
    @Bind(R.id.constract)
    TextView constract;
    @Bind(R.id.recharge_flow)
    MyGridView gridView;
    @Bind(R.id.elec_recharge_btn)
    Button submit;

    private String[] dianxin_citys = new String[]{"3,000", "3,001", "3,002", "3,105", "3,003", "3,004", "3,005", "3,103"};
    private String[] dianxin_prod = new String[]{"5M", "10M", "30M", "50M", "100M", "200M", "500M", "1G"};
    private String[] dianxin_mny = new String[]{"1元", "2元", "5元", "7元", "10元", "15元", "30元", "50元"};

    private String[] liantong_citys = new String[]{"3,007", "2,204", "3008", "2,205", "3,009", "1t1000"};
    private String[] liantong_prod = new String[]{"20M", "50M", "100M", "200M", "500M"};
    private String[] liantong_mny = new String[]{"4元", "6元", "15元", "10元", "30元"};

    private String[] yidong_citys = new String[]{"2,208", "2,118", "2,119", "2,120", "2,121"};
    private String[] yidong_prod = new String[]{"10M", "30M", "70M", "150M", "500M"};
    private String[] yidong_mny = new String[]{"3元", "5元", "10元", "20元", "30元"};
    // 电信 产品;
    private List<String> dianxin_prods;
    // 联通产品
    private List<String> liantong_prods;
    // 移动产品
    private List<String> yidong_prods;

    private List<String> chargeList = new ArrayList<>();
    private PhoneChargeAdapter adapter;
    private int CARRIEROPERATOR = 0;
    private String cityCod;
    private String phoneNumber;

    @Override
    public View initViews() {
        View view = inflater.inflate(R.layout.fragment_phone_bill, null);
        ButterKnife.bind(this, view);
        constract.setOnClickListener(this);
        submit.setOnClickListener(this);
        return view;
    }

    @Override
    public void initData() {
        dianxin_prods = Arrays.asList(dianxin_prod);
        liantong_prods = Arrays.asList(liantong_prod);
        yidong_prods = Arrays.asList(yidong_prod);
        //初始化auto
        initAutoComplete("phone_bill", rechargeTelNum);
        chargeList.addAll(yidong_prods);
        CARRIEROPERATOR = 3;
        adapter = new PhoneChargeAdapter(mActivity, chargeList);
        gridView.setAdapter(adapter);
        gridView.setOnItemClickListener(this);
        rechargeTelNum.addTextChangedListener(new TextWatcher() {

            @Override
            public void afterTextChanged(Editable arg0) {

            }

            @Override
            public void beforeTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) {
                System.out.println(arg0);
            }

            @Override
            public void onTextChanged(CharSequence cs, int start, int end, int count) {
                // editText值
                // start开始索引
                String contents = cs.toString();
                if (contents.substring(start).equals(ConstantUtil.SPACE_STRING)) {
                    rechargeTelNum.setText(contents.substring(0, start));
                    rechargeTelNum.setSelection(contents.length() - 1);
                    return;
                }
                int length = contents.length();
                if (length == 4) {
                    if (contents.substring(3).equals(new String(ConstantUtil.SPACE_STRING))) {
                        contents = contents.substring(0, 3);
                        rechargeTelNum.setText(contents);
                        rechargeTelNum.setSelection(contents.length());
                    } else {
                        contents = contents.substring(0, 3) + ConstantUtil.SPACE_STRING + contents.substring(3);
                        rechargeTelNum.setText(contents);
                        rechargeTelNum.setSelection(contents.length());
                    }
                } else if (length == 9) {
                    if (contents.substring(8).equals(new String(ConstantUtil.SPACE_STRING))) {
                        contents = contents.substring(0, 8);
                        rechargeTelNum.setText(contents);
                        rechargeTelNum.setSelection(contents.length());
                    } else {
                        contents = contents.substring(0, 8) + ConstantUtil.SPACE_STRING + contents.substring(8);
                        rechargeTelNum.setText(contents);
                        rechargeTelNum.setSelection(contents.length());
                    }
                }
                if (11 == contents.replaceAll(ConstantUtil.SPACE_STRING, "").length()) {
                    if (!ConstantUtil.SPACE_STRING.equals(contents.substring(3, 4))
                            || !ConstantUtil.SPACE_STRING.equals(contents.substring(8, 9))) {
                        contents = StringUtil.fomartPhoneNum(contents);
                        rechargeTelNum.setText(contents);
                        rechargeTelNum.setSelection(contents.length());
                    }
                }
                if (length != 13) {
                    submit.setText(R.string.elec_now_recharge_btn);
                } else {
                    if (RegexUtil.telRegex(contents)) {
                        // queryPhoneRechargeBill();
                        contents = contents.replace(" ", "");
                        switch (RegexUtil.matchesPhoneNumber(contents)) {
                            case 1:// 电信
//                                adapter = new PhoneChargeAdapter(mActivity,dianxin_prods);
//                                gv_rechargeFlow.setAdapter(adapter);
                                chargeList.clear();
                                chargeList.addAll(dianxin_prods);
                                submit.setText("立即充值:" + dianxin_prod[ConstantUtil.RECHANGE_AMOUNT_DEFAULT] + " - "
                                        + dianxin_mny[ConstantUtil.RECHANGE_AMOUNT_DEFAULT]);
                                CARRIEROPERATOR = 1;
                                cityCod = dianxin_citys[ConstantUtil.RECHANGE_AMOUNT_DEFAULT];
                                break;
                            case 2:// 联通
                                chargeList.clear();
                                chargeList.addAll(liantong_prods);
//                                adapter = new PhoneRechargeAdapter(RechargeFlowActivity.this, liantong_prods);
//                                gv_rechargeFlow.setAdapter(adapter);
//                                adapter.setSelectItem(ConstantUtil.RECHANGE_AMOUNT_DEFAULT);
                                submit.setText("立即充值:" + liantong_prod[ConstantUtil.RECHANGE_AMOUNT_DEFAULT] + " - "
                                        + liantong_mny[ConstantUtil.RECHANGE_AMOUNT_DEFAULT]);
                                CARRIEROPERATOR = 2;
                                cityCod = liantong_citys[ConstantUtil.RECHANGE_AMOUNT_DEFAULT];
                                break;
                            case 3:// 移动
                                chargeList.clear();
                                chargeList.addAll(yidong_prods);
//                                adapter = new PhoneRechargeAdapter(RechargeFlowActivity.this, yidong_prods);
//                                gv_rechargeFlow.setAdapter(adapter);
//                                adapter.setSelectItem(ConstantUtil.RECHANGE_AMOUNT_DEFAULT);
                                submit.setText("立即充值:" + yidong_prod[ConstantUtil.RECHANGE_AMOUNT_DEFAULT] + " - "
                                        + yidong_mny[ConstantUtil.RECHANGE_AMOUNT_DEFAULT]);
                                CARRIEROPERATOR = 3;
                                cityCod = yidong_citys[ConstantUtil.RECHANGE_AMOUNT_DEFAULT];
                                break;
                        }
                        adapter.notifyDataSetChanged();
                        adapter.setSelectItem(ConstantUtil.RECHANGE_AMOUNT_DEFAULT);

                    } else {
                        ToastUtils.showToast(mActivity.getResources().getString(R.string.recharge_num_error));
                    }
                }
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

    @Override
    public void onClick(View v) {
        if (StringUtil.isFastClick())
            return;
        switch (v.getId()) {
            case R.id.elec_recharge_btn:
                phoneNumber = rechargeTelNum.getText().toString().trim();
                phoneNumber = phoneNumber.replaceAll(" ", "");
                if (!RegexUtil.telRegex(phoneNumber)) {
                    ToastUtils.showToast(mActivity.getResources().getString(R.string.recharge_num_error));
                    return;
                }
                Map<Object, Object> params = new HashMap<Object, Object>();
                UnionPayUtils.getInstance().showDialog(mActivity);
                /*************************************************
                 * 步骤1：从网络开始,查询订单信息
                 ************************************************/
                params.put("phone", phoneNumber);
                params.put("city", cityCod);
                appUtil.getLiuliang(params, new FlowRechargeResponseHandler(), token);
                break;
            case R.id.constract:
                constract.setInputType(InputType.TYPE_NULL);
                startActivityForResult(new Intent(Intent.ACTION_PICK, ContactsContract.Contacts.CONTENT_URI), 1);
                break;
        }
    }

    class FlowRechargeResponseHandler extends JsonHttpResponseHandler {
        @Override
        public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
            try {
                if (!response.getBoolean("success")) {
                    if (!response.isNull("error")) {
                        if ("noauth".equals(response.getString("error"))) {
                            ToastUtils.showToast(mActivity.getResources().getString(R.string.re_login));
                            startActivity(new Intent(mActivity, LoginActivity.class));
                            mActivity.overridePendingTransition(R.anim.activity_open, R.anim.activity_close);
                            mActivity.finish();
                            return;
                        }
                    }
                    if (!response.isNull("message")) {
                        ToastUtils.showToast(response.getString("message"));
                        return;
                    }
                }
                if (response.getBoolean("success")) {
                    String tn = response.getJSONObject("obj").getString("tn");
                    String unionpayOrderId = response.getJSONObject("obj").getString("unionpayOrderId");
                    if (tn == null || tn.length() == 0) {
                        UnionPayUtils.getInstance().checkTN(mActivity);
                        return;
                    }
                    UnionPayUtils.getInstance().unionPay(mActivity, tn);
                }
            } catch (JSONException e) {

                e.printStackTrace();
            }
        }

        @Override
        public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {

        }

        @Override
        public void onFinish() {
            saveHistory("phone_bill", phoneNumber);
            UnionPayUtils.getInstance().disMissDialog();
        }
    }


    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        TextView tv = (TextView) view.findViewById(R.id.item_amount);
        String rechargeFlow = tv.getText().toString();

        if (StringUtil.isNotEmpty(rechargeTelNum.getText().toString())) {
            switch (CARRIEROPERATOR) {
                case 1:
                    submit.setText("立即充值:" + rechargeFlow + " - " + dianxin_mny[position]);
                    cityCod = dianxin_citys[position];
                    break;
                case 2:
                    cityCod = liantong_citys[position];
                    submit.setText("立即充值:" + rechargeFlow + " - " + liantong_mny[position]);
                    break;
                case 3:
                    submit.setText("立即充值:" + rechargeFlow + " - " + yidong_mny[position]);
                    cityCod = yidong_citys[position];
                    break;
            }
            // queryPhoneRechargeBill();
        }
        adapter.setSelectItem(position);
        adapter.notifyDataSetChanged();
    }

    private void getPhoneNum(Intent data) {

        ContentResolver contentResolver = mActivity.getContentResolver();
        // URI,每个ContentProvider定义一个唯一的公开的URI,用于指定到它的数据集
        Uri contactData = data.getData();
        // 查询就是输入URI等参数,其中URI是必须的,其他是可选的,如果系统能找到URI对应的ContentProvider将返回一个Cursor对象.
        @SuppressWarnings("deprecation")
        Cursor cursor = contentResolver.query(contactData, null, null, null, null);
        cursor.moveToFirst();
        // 获得DATA表中的名字
        String username = cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME));
        // 条件为联系人ID
        String contactId = cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts._ID));
        // 获得DATA表中的电话号码，条件为联系人ID,因为手机号码可能会有多个
        Cursor phone = contentResolver.query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null,
                ContactsContract.CommonDataKinds.Phone.CONTACT_ID + " = " + contactId, null, null);
        String userNumber = "";
        while (phone.moveToNext()) {
            userNumber = phone.getString(phone.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
            userNumber = userNumber.replace(" ", "");
            if (RegexUtil.telRegex(userNumber)) {
                rechargeTelNum.setText(userNumber);
            } else {
                ToastUtils.showToast("请选择正确的号码!");
            }
        }

        if (StringUtil.isEmpty(userNumber)) {
            ToastUtils.showToast("号码为空，请确认是否已开启通讯录权限!");
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (data != null)
        getPhoneNum(data);
    }
}

