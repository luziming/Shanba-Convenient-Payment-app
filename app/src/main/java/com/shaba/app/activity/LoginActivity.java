package com.shaba.app.activity;

import android.content.Intent;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.loopj.android.http.JsonHttpResponseHandler;
import com.shaba.app.R;
import com.shaba.app.utils.EditTextClearTools;
import com.shaba.app.utils.PrefUtils;
import com.shaba.app.utils.SBLog;
import com.shaba.app.utils.ToastUtils;

import org.apache.http.Header;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import butterknife.Bind;
import butterknife.OnClick;

public class LoginActivity extends BaseActivity {

    @Bind(R.id.et_phonenumber)
    EditText etPhonenumber;
    @Bind(R.id.del_phonenumber)
    ImageView delPhonenumber;
    @Bind(R.id.et_password)
    EditText etPassword;
    @Bind(R.id.del_password)
    ImageView delPassword;
    @Bind(R.id.bt_login)
    Button bt_login;

    private String phoneNumber;
    private String password;


    @Override
    protected int getLayoutID() {
        return R.layout.activity_login;
    }

    @Override
    protected void initView() {
        String username = PrefUtils.getString(this, "username", "");
        if (!TextUtils.isEmpty(username)) {
            etPhonenumber.setText(username);
        }
        initData();
    }

    @Override
    protected void initData() {
        EditTextClearTools.addclerListener(etPhonenumber, delPhonenumber);
        EditTextClearTools.addclerListener(etPassword, delPassword);
    }

    @OnClick({R.id.tv_forgetPw, R.id.login_c, R.id.bt_login})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tv_forgetPw:
                Intent i = new Intent(this, SecondActivity.class);
                i.putExtra("type", "forget");
                lanuchActivity(i);
                break;
            case R.id.login_c:
                Intent intent = new Intent(this, SecondActivity.class);
                intent.putExtra("type", "register");
                lanuchActivity(intent);
                break;
            case R.id.bt_login:
                phoneNumber = etPhonenumber.getText().toString().trim();
                password = etPassword.getText().toString().trim();
                if (TextUtils.isEmpty(phoneNumber)) {
                    ToastUtils.showToast("用户名不能为空");
                    return;
                }
                if (TextUtils.isEmpty(password)) {
                    ToastUtils.showToast("密码不能为空");
                    return;
                }
                Map<String, String> p = new HashMap();
                p.put("username", phoneNumber);
                p.put("password", password);
                bt_login.setText("登陆中....");
                bt_login.setClickable(false);
                appUtil.login(p, new LoginResponseHandler());
                break;
        }
    }

    private class LoginResponseHandler extends JsonHttpResponseHandler {
        @Override
        public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
            try {
                boolean success = response.getBoolean("success");
                if (success == false) {
                    ToastUtils.showToast(response.getString("message"));
                    bt_login.setText("登陆");
                    bt_login.setClickable(true);
                    return;
                }
                if (success == true) {
                    String token = response.getString("token");
                    PrefUtils.putString(LoginActivity.this, "token", token);
                    PrefUtils.putString(LoginActivity.this, "username", phoneNumber);
                    SBLog.json("TOKEN", token);
                    bt_login.setText("登陆");
                    bt_login.setClickable(true);
                    startActivity(new Intent(LoginActivity.this, MainActivity.class));
                    finish();
                    overridePendingTransition(R.anim.activity_open, R.anim.activity_close);
                }
            } catch (Exception e) {
                e.getMessage();
            }
        }

        @Override
        public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
            bt_login.setText("登陆");
            bt_login.setClickable(true);
        }
    }
}
