package com.shaba.app.fragment;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.loopj.android.http.JsonHttpResponseHandler;
import com.shaba.app.R;
import com.shaba.app.fragment.base.BaseFragment;
import com.shaba.app.global.ConstantUtil;
import com.shaba.app.utils.PrefUtils;
import com.shaba.app.utils.RegexUtil;
import com.shaba.app.utils.ToastUtil;
import com.shaba.app.utils.ToastUtils;
import com.shaba.app.utils.UnionPayUtils;

import org.apache.http.Header;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

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
public class ReplacePhoneNumerFragment extends BaseFragment implements View.OnClickListener {


    @Bind(R.id.edit_phone)
    EditText editPhone;
    @Bind(R.id.edit_password)
    EditText editPassword;
    @Bind(R.id.code_number)
    EditText codeNumber;
    @Bind(R.id.obtain_text)
    TextView obtainText;
    @Bind(R.id.login_obt)
    Button loginObt;
    @Bind(R.id.ll_replace)
    LinearLayout llReplace;
    @Bind(R.id.rl_success)
    RelativeLayout rlSuccess;

    private int recLen = 60;
    final Handler handler = new Handler() {

        public void handleMessage(Message msg) { // handle message
            switch (msg.what) {
                case 1:
                    recLen--;
                    obtainText.setClickable(false);
                    obtainText.setText("" + recLen + "s后再次获取");
                    if (recLen > 0) {
                        Message message = handler.obtainMessage(1);
                        handler.sendMessageDelayed(message, 1000); // send message
                    } else {
                        obtainText.setText("点击获取验证码");
                        obtainText.setClickable(true);
                        recLen = 60;
                    }
            }
            super.handleMessage(msg);
        }
    };
    private String phoneNum;
    private String code;
    private String password;

    @Override
    public View initViews() {
        View view = inflater.inflate(R.layout.fragment_replece_phone, null);
        ButterKnife.bind(this, view);
        loginObt.setOnClickListener(this);
        obtainText.setOnClickListener(this);
        return view;
    }

    @Override
    public void initData() {

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
        switch (v.getId()) {
            case R.id.obtain_text://发送验证码
                String phone = editPhone.getText().toString().trim();
                if (TextUtils.isEmpty(phone) || !RegexUtil.telRegex(phone)) {
                    ToastUtils.showToast("请输入正确的手机号");
                    return;
                }
                Map<Object, Object> params = new HashMap<>();
                params.put("mobile", phone);
                params.put("type", ConstantUtil.SEND_SMS_REGIST);
                appUtil.getSMS(params, new MyResponseHandler());
                Message message = handler.obtainMessage(1); // Message
                handler.sendMessageDelayed(message, 1000);
                break;
            case R.id.login_obt://下一步
                phoneNum = editPhone.getText().toString().trim();
                code = codeNumber.getText().toString().trim();
                password = editPassword.getText().toString().trim();
                if (TextUtils.isEmpty(phoneNum) || !RegexUtil.telRegex(phoneNum)) {
                    ToastUtils.showToast("请输入正确的手机号");
                    return;
                }
                if (TextUtils.isEmpty(code)) {
                    ToastUtils.showToast("验证码为空");
                    return;
                }
                if (TextUtils.isEmpty(password)) {
                    ToastUtil.show(mActivity, "密码不能为空");
                    return;
                }
                Map<Object, Object> p = new HashMap<>();
                p.put("mobile", phoneNum);
                p.put("code", code);
                appUtil.checkVerificationCode(p, new checkVerificationCodeHandler());
                UnionPayUtils.getInstance().showDialog(mActivity);
                break;
        }
    }

    /**
     * 验证码发送结果处理
     */
    private class MyResponseHandler extends JsonHttpResponseHandler {

        @Override
        public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
            try {
                // progressDialog.dismiss();
                boolean success = response.getBoolean("success");
                if (success == false) {
                    if (!response.isNull("error")) {
                        ToastUtil.show(context, response.getString("error") + ",获取失败!");
                    }
                    if (!response.isNull("message")) {
                        ToastUtil.show(context, response.getString("message") + ",获取失败!");
                    }
                    return;
                } else {
                    ToastUtil.show(context, "短信发送成功，请查收!");
                }

            } catch (Exception e) {
                e.getMessage();
            }
        }
    }

    /**
     * 检测 验证码
     */
    private class checkVerificationCodeHandler extends JsonHttpResponseHandler {
        @Override
        public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
            try {
                boolean pass = response.getBoolean("success");
                if (pass) {
                    // intent = s Intent(context, PassWordActivity.class);
                    // // 验证码对比
                    // intent.putExtra("phone_number", strPhone);
                    // startActivity(intent);
                    // ChangeUserActivity.this.finish();
                    /**
                     * http://115.28.138.217:3000/api/change-mobile 必须登录状态 参数
                     * {:password "123123123", :mobile "15248141905", :code
                     * "839712"} 返回 {:success true}/{:success false :message ""}
                     */
                    Map<Object, Object> params = new HashMap<Object, Object>();
                    params.put("mobile", phoneNum);
                    params.put("password", password);
                    params.put("code", code);
                    appUtil.changeUser(params, new ChangeUserHandler(), token);
                } else {
                    ToastUtil.show(context, response.getString("message"));
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        @Override
        public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
            UnionPayUtils.getInstance().disMissDialog();
        }

        @Override
        public void onFinish() {

        }
    }

    /**
     * 处理用户切换
     * @author admin
     */
    class ChangeUserHandler extends JsonHttpResponseHandler {

        @Override
        public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
            UnionPayUtils.getInstance().disMissDialog();
            try {
                boolean pass = response.getBoolean("success");
                if (pass) {
                    ToastUtil.show(context, "更换手机成功");
                    String token = response.getString("token");
                    PrefUtils.putString(mActivity, "token", token);
                    PrefUtils.putString(mActivity, "username", phoneNum);
                    llReplace.setVisibility(View.GONE);
                    rlSuccess.setVisibility(View.VISIBLE);
                } else {
                    ToastUtil.show(context, response.getString("message"));
                }
            } catch (JSONException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }

        @Override
        public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
            UnionPayUtils.getInstance().disMissDialog();
        }

        @Override
        public void onFinish() {

        }
    }

}
