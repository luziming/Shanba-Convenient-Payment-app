package com.shaba.app.fragment;

import android.content.ContentResolver;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.text.Editable;
import android.text.InputType;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
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
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.Bind;
import butterknife.ButterKnife;

import static com.shaba.app.R.id.et_other;

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
public class PhoneChargeFragment extends BaseFragment implements View.OnClickListener, AdapterView.OnItemClickListener {


    @Bind(R.id.rg_yys_type)
    RadioGroup type;
    @Bind(R.id.recharge_tel_num)
    AutoCompleteTextView phoneNumer;
    @Bind(R.id.constract)
    TextView constract;
    @Bind(R.id.recharge_amount)
    MyGridView gridView;
    @Bind(R.id.elec_recharge_btn)
    Button submit;
    @Bind(R.id.ll_yunyingshang)
    LinearLayout ll_type;
    @Bind(et_other)
    EditText et_amount;

    //缴费金额列表
    private List<String> amountList = new ArrayList<String>() {
        {
            add("10元");
            add("20元");
            add("30元");
            add("50元");
            add("100元");
            add("200元");
        }
    };
    //缴费模式  phone  tel  broadBand
    private String mMode;
    private String chargeAmount = "";
    private PhoneChargeAdapter chargeAdapter;
    private int yys_type = 0;
    private boolean isCheckedAmount = false;

    @Override
    public View initViews() {
        View view = inflater.inflate(R.layout.fragment_flow_charge, null);
        ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void initData() {
        mMode = getArguments().getString("MODE");
        if (mMode.equals("phone")) {
            ll_type.setVisibility(View.GONE);
            phoneNumer.setHint(R.string.phone_num);
            initAutoComplete("phone", phoneNumer);
            //添加监听
            phoneNumer.addTextChangedListener(new MyTextWatcher());
        } else if (mMode.equals("tel")) {
            phoneNumer.setHint(R.string.tel_num);
            initAutoComplete("tel", phoneNumer);
        } else {
            phoneNumer.setHint(R.string.broadband_num);
            initAutoComplete("broadband", phoneNumer);
        }
        chargeAdapter = new PhoneChargeAdapter(mActivity, amountList);
        gridView.setAdapter(chargeAdapter);
        //提交按钮
        submit.setOnClickListener(this);
        //获取联系人
        constract.setOnClickListener(this);
        //gridView
        gridView.setOnItemClickListener(this);

        type.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId) {
                    case R.id.rb_type_yidong:
                        yys_type = 1;
                        break;
                    case R.id.rb_type_liantong:
                        yys_type = 2;
                        break;
                    case R.id.rb_type_dianxin:
                        yys_type = 3;
                        break;
                }
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
        if (StringUtil.isFastClick())
            return;
        switch (v.getId()) {
            case R.id.elec_recharge_btn:
                String phone = phoneNumer.getText().toString().trim().replaceAll(ConstantUtil.SPACE_STRING, "");
                if (TextUtils.isEmpty(phone)) {
                    ToastUtils.showToast("请选择充值号码");
                    return;
                }
                if (!mMode.equals("phone")) {
                    if (yys_type == 0) { // 运营商为空
                        ToastUtils.showToast("请选择运营商");
                        return;
                    }
                    if (yys_type != 2) {
                        ToastUtils.showToast("暂不支持移动电信充值");
                        return;
                    }
                }
                chargeAmount = et_amount.getText().toString().trim();
                if (TextUtils.isEmpty(chargeAmount)) {      //是否为自定义金额
                    if (isCheckedAmount) {
                        chargeAmount = et_amount.getHint().toString().trim();
                    } else {
                        ToastUtils.showToast("请选择充值金额");
                        return;
                    }
                }
                if (TextUtils.isEmpty(chargeAmount)) {
                    ToastUtils.showToast("请选择充值金额");
                    return;
                }

                if (mMode.equals("phone")) {
                    if (!RegexUtil.telRegex(phone)) {
                        ToastUtils.showToast(mActivity.getResources().getString(R.string.recharge_num_error));
                        return;
                    }
                } else if (mMode.equals("tel")) {
                    if (!RegexUtil.telRegex2(phone)) {
                        ToastUtils.showToast( mActivity.getResources().getString(R.string.recharge_telnum_error));
                        return;
                    }
                } else if (mMode.equals("broadband")) {
                    if (!RegexUtil.telRegex2(phone)) {
                        ToastUtils.showToast(mActivity.getResources().getString(R.string.recharge_broadband_error));
                        return;
                    }
                }
                Map<Object, Object> params = new HashMap<Object, Object>();
                params.put("phone", phone);
                params.put("txnAmt", chargeAmount);
                if (!mMode.equals("phone")) {
                    params.put("type_id", yys_type);
                }
                Log.e("PhoneChargeFragment", "phone : " + phone + ",mMode:" + mMode);
                UnionPayUtils.getInstance().showDialog(mActivity);
                appUtil.phoneRecharge(params, new PhoneChargeResponseHandler(), mMode, token);
                saveHistory(mMode, phone);
                break;
            case R.id.constract:
                constract.setInputType(InputType.TYPE_NULL);
                startActivityForResult(new Intent(Intent.ACTION_PICK, ContactsContract.Contacts.CONTENT_URI), 1);
                break;
        }
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        if (!isCheckedAmount)
            isCheckedAmount = true;
        TextView tv = (TextView) view.findViewById(R.id.item_amount);
        chargeAmount = tv.getText().toString().split(ConstantUtil.RMB)[0];
        et_amount.setHint(chargeAmount);
        submit.setText("立即充值:" + ConstantUtil.CHINA_RMB + chargeAmount + ConstantUtil.RMB);
        chargeAdapter.setSelectItem(position);
        //如果不走这个选择器不会生效,性能方面不是很好,有待改进
        chargeAdapter.notifyDataSetChanged();
    }

    private class MyTextWatcher implements TextWatcher {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            String contents = s.toString();
            int length = contents.length();
            //只有在手机充值的时候,格式化号码,因为宽带与固话并能确定其规律
            if (length == 4) {
                if (contents.substring(3).equals(new String(ConstantUtil.SPACE_STRING))) {
                    contents = contents.substring(0, 3);
                    phoneNumer.setText(contents);
                    phoneNumer.setSelection(contents.length());
                } else {
                    contents = contents.substring(0, 3) + ConstantUtil.SPACE_STRING + contents.substring(3);
                    phoneNumer.setText(contents);
                    phoneNumer.setSelection(contents.length());
                }
            } else if (length == 9) {
                if (contents.substring(8).equals(new String(ConstantUtil.SPACE_STRING))) {
                    contents = contents.substring(0, 8);
                    phoneNumer.setText(contents);
                    phoneNumer.setSelection(contents.length());
                } else {
                    contents = contents.substring(0, 8) + ConstantUtil.SPACE_STRING + contents.substring(8);
                    phoneNumer.setText(contents);
                    phoneNumer.setSelection(contents.length());
                }
            }
            if (11 == contents.replaceAll(ConstantUtil.SPACE_STRING, "").length()) {
                if (!ConstantUtil.SPACE_STRING.equals(contents.substring(3, 4))
                        || !ConstantUtil.SPACE_STRING.equals(contents.substring(8, 9))) {
                    contents = StringUtil.fomartPhoneNum(contents);
                    phoneNumer.setText(contents);
                    phoneNumer.setSelection(contents.length());
                }
            }
        }

        @Override
        public void afterTextChanged(Editable s) {

        }
    }

    /**
     * 缴电话费
     */
    private class PhoneChargeResponseHandler extends JsonHttpResponseHandler {
        @Override
        public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
            if (response != null) {
                try {
                    UnionPayUtils.getInstance().disMissDialog();
                    if (!response.getBoolean("sucess")) {
                        if (!response.isNull("error")) {
                            if ("noauth".equals(response.getString("error"))) {
                                ToastUtils.showToast(mActivity.getResources().getString(R.string.re_login));
                                startActivity(new Intent(mActivity, LoginActivity.class));
                                mActivity.overridePendingTransition(R.anim.activity_open, R.anim.activity_close);
                                mActivity.finish();
                                return;
                            } else {
                                ToastUtils.showToast("请输入正确的号码!");
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
                        if (TextUtils.isEmpty(tn)) {
                            UnionPayUtils.getInstance().checkTN(mActivity);
                            return;
                        }
                        UnionPayUtils.getInstance().unionPay(mActivity, tn);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }
        @Override
        public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
            Log.e("PhoneChargeResponseHandler", "Error: ");
            UnionPayUtils.getInstance().disMissDialog();
            ToastUtils.showToast(errorResponse.toString());
        }
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
                phoneNumer.setText(userNumber);
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