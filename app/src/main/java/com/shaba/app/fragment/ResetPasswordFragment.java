package com.shaba.app.fragment;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.loopj.android.http.JsonHttpResponseHandler;
import com.shaba.app.R;
import com.shaba.app.fragment.base.BaseFragment;
import com.shaba.app.utils.PrefUtils;
import com.shaba.app.utils.StringUtil;
import com.shaba.app.utils.ToastUtil;
import com.shaba.app.utils.ToastUtils;
import com.shaba.app.utils.UnionPayUtils;

import org.apache.http.Header;
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
public class ResetPasswordFragment extends BaseFragment implements View.OnClickListener {


    @Bind(R.id.oldpwd)
    EditText oldpwd;
    @Bind(R.id.newpwd)
    EditText newpwd;
    @Bind(R.id.repeatpwd)
    EditText repeatpwd;
    @Bind(R.id.confirm_btn)
    Button confirmBtn;
    @Bind(R.id.ll_reset)
    LinearLayout llReset;
    @Bind(R.id.rl_success)
    RelativeLayout rlSuccess;
    private String newPassword;

    @Override
    public View initViews() {
        View view = inflater.inflate(R.layout.fragment_reset_password, null);
        ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void initData() {
        confirmBtn.setOnClickListener(this);
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
        String oldPassword = oldpwd.getText().toString().trim();
        newPassword = newpwd.getText().toString().trim();
        String repeatPassword = repeatpwd.getText().toString().trim();
        if (TextUtils.isEmpty(oldPassword)) {
            ToastUtils.showToast("请输入旧密码。");
            return;
        }
        if (TextUtils.isEmpty(newPassword)) {
            ToastUtils.showToast("密码不能为空");
            return;
        }
        if (oldPassword.equals(newPassword)) {
            ToastUtils.showToast("新密码不能与旧密码相同");
            return;
        }
        if (!newPassword.matches("[A-Za-z0-9_]+")) {
            ToastUtil.show(context, R.string.rules);
            return;
        }
        if (!newPassword.matches("[0-9]+[a-zA-Z]+[0-9a-zA-Z]*|[a-zA-Z]+[0-9]+[0-9a-zA-Z]*")) {
            ToastUtil.show(context, "密码必须由字母加数字组成");
            return;
        }
        if (newPassword.length() < 6 || newPassword.length() > 18) {
            ToastUtil.show(context, "请输入6到18位的密码组合");
            return;
        }
        if (!newPassword.equals(repeatPassword)) {
            ToastUtil.show(context, "俩次密码输入不一致");
            return;
        }
        Map<Object, Object> p = new HashMap<>();
        p.put("current-password", oldPassword);
        p.put("password", newPassword);
        p.put("password-confirm", repeatpwd);
        appUtil.getUpdataPwd(p, new MyChangeData(),token);
        UnionPayUtils.getInstance().showDialog(mActivity);
    }

    private class MyChangeData extends JsonHttpResponseHandler {
        @Override
        public void onSuccess(int statusCode, Header[] headers,
                              JSONObject response) {
            UnionPayUtils.getInstance().disMissDialog();
            try {
                boolean success = response.getBoolean("success");
                if (!success) {
                    ToastUtil.show(mActivity,R.string.reset_pass_word_false);
                    return;
                }
                llReset.setVisibility(View.GONE);
                rlSuccess.setVisibility(View.VISIBLE);
                //设置成功后自动重新登录,刷新token
                String username = PrefUtils.getString(mActivity, "username", "");
                Map<String, String> p = new HashMap();
                p.put("username", username);
                p.put("password", newPassword);
                appUtil.login(p, new LoginResponseHandler());
            } catch (Exception e) {
                e.getMessage();
            }
        }

        @Override
        public void onFailure(int statusCode, Header[] headers,
                              Throwable throwable, JSONObject errorResponse) {
            UnionPayUtils.getInstance().disMissDialog();
        }
    }

    private class LoginResponseHandler extends JsonHttpResponseHandler {
        @Override
        public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
            try {
                boolean success = response.getBoolean("success");
                if (success == false) {
                    ToastUtils.showToast(response.getString("message"));
                    return;
                }
                if (success == true) {
                    String token = response.getString("token");
                    PrefUtils.putString(mActivity, "token", token);
                }
            } catch (Exception e) {
                e.getMessage();
            }
        }

        @Override
        public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {

        }
    }
}
