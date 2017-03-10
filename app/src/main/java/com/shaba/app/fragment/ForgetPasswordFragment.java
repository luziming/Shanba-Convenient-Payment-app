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
import android.widget.TextView;

import com.loopj.android.http.JsonHttpResponseHandler;
import com.shaba.app.R;
import com.shaba.app.fragment.base.BaseFragment;
import com.shaba.app.fragment.base.FragmentUtils;
import com.shaba.app.global.ConstantUtil;
import com.shaba.app.utils.RegexUtil;
import com.shaba.app.utils.StringUtil;
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
import de.greenrobot.event.EventBus;

import static com.shaba.app.R.id.code_number;
import static com.shaba.app.R.id.obtain_text;
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
public class ForgetPasswordFragment extends BaseFragment implements View.OnClickListener {

    @Bind(R.id.edit_phone)
    EditText editPhone;
    @Bind(code_number)
    EditText codeNumber;
    @Bind(obtain_text)
    TextView obtainText;
    @Bind(R.id.btn_findPW)
    Button btnFindPW;

    private int recLen = 60;
    private String phone;
    private String checkCode;

    @Override
    public View initViews() {
        View view = inflater.inflate(R.layout.fragment_forget_password, null);
        ButterKnife.bind(this, view);
        obtainText.setOnClickListener(this);
        btnFindPW.setOnClickListener(this);
        return view;
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
        //移除所有的消息
        handler.removeCallbacksAndMessages(null);
    }

    @Override
    public void onClick(View v) {
        if (StringUtil.isFastClick())
            return;
        switch (v.getId()) {
            case obtain_text:
                phone = editPhone.getText().toString().trim();
                if (TextUtils.isEmpty(phone) || !RegexUtil.telRegex(phone)) {
                    ToastUtils.showToast("请输入正确的手机号");
                    return;
                }
                Map<Object, Object> params = new HashMap<>();
                params.put("mobile", phone);
                params.put("type", ConstantUtil.SEND_SMS_FORGET);
                appUtil.getSMS(params, new MyResponseHandler());
                break;
            case R.id.btn_findPW:
                phone = editPhone.getText().toString().trim();
                checkCode = codeNumber.getText().toString().trim();
                if (TextUtils.isEmpty(phone) || !RegexUtil.telRegex(phone)) {
                    ToastUtils.showToast("请输入正确的手机号");
                    return;
                }
                if (TextUtils.isEmpty(checkCode)) {
                    ToastUtils.showToast("验证码错误");
                    return;
                }
                Map<Object, Object> p = new HashMap<>();
                p.put("mobile", phone);
                p.put("code", checkCode);
                appUtil.checkVerificationCode(p, new checkVerificationCodeHandler());
                UnionPayUtils.getInstance().showDialog(mActivity);
                break;
        }
    }

    //倒计时
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

    private class MyResponseHandler extends JsonHttpResponseHandler {

        @Override
        public void onSuccess(int statusCode, Header[] headers,
                              JSONObject response) {
            try {
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
                    Message message = handler.obtainMessage(1); // Message
                    handler.sendMessageDelayed(message, 1000);
                    ToastUtil.show(context, "短信发送成功，请查收!");
                }

            } catch (Exception e) {
                e.getMessage();
            }
        }

        @Override
        public void onFailure(int statusCode, Header[] headers,
                              Throwable throwable, JSONObject errorResponse) {
            ToastUtil.show(context, R.string.reg_failure);
        }
    }

    /**
     * 检测验证码
     */
    private class checkVerificationCodeHandler extends JsonHttpResponseHandler {
        @Override
        public void onSuccess(int statusCode, Header[] headers,
                              JSONObject response) {
            try {
                UnionPayUtils.getInstance().disMissDialog();
                boolean pass = response.getBoolean("success");
                if (pass) {
                    ForgetSetPasswordFragment fragment = new ForgetSetPasswordFragment();
                    Bundle bundle = new Bundle();
                    bundle.putString("phone_number", phone);
                    bundle.putString("static_code", checkCode);
                    fragment.setArguments(bundle);
                    FragmentUtils.startCoolFragment(mActivity, fragment, R.id.fl_container_content);
                    EventBus.getDefault().post("设置密码");
                } else {
                    ToastUtil.show(context, response.getString("message"));
                }
            } catch (JSONException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }

        @Override
        public void onFailure(int statusCode, Header[] headers,
                              Throwable throwable, JSONObject errorResponse) {
            UnionPayUtils.getInstance().disMissDialog();
        }

        @Override
        public void onFinish() {
        }
    }
}
