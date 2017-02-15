package com.shaba.app.fragment;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.loopj.android.http.JsonHttpResponseHandler;
import com.shaba.app.R;
import com.shaba.app.fragment.base.BaseFragment;
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
public class ForgetSetPasswordFragment extends BaseFragment implements View.OnClickListener {


    @Bind(R.id.edit_pwd)
    EditText editPwd;
    @Bind(R.id.edit_pwd_s)
    EditText editPwdS;
    @Bind(R.id.reg_obt)
    Button regObt;
    private String phone_number;
    private String static_code;
    private String type;

    @Override
    public View initViews() {
        View view = inflater.inflate(R.layout.fragment_set_password, null);
        ButterKnife.bind(this, view);
        regObt.setOnClickListener(this);
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
    }

    @Override
    public void initData() {
        Bundle arguments = getArguments();
        phone_number = arguments.getString("phone_number");
        static_code = arguments.getString("static_code");
    }

    @Override
    public void onClick(View v) {
        if (StringUtil.isFastClick())
            return;
        String once = editPwd.getText().toString().trim();
        String twice = editPwdS.getText().toString().trim();
        if (TextUtils.isEmpty(once)) {
            ToastUtils.showToast("密码不能为空");
            return;
        }
        if (!once.matches("[A-Za-z0-9_]+")) {
            ToastUtil.show(context, R.string.rules);
            return;
        }
        if (!once.matches("[0-9]+[a-zA-Z]+[0-9a-zA-Z]*|[a-zA-Z]+[0-9]+[0-9a-zA-Z]*")) {
            ToastUtil.show(context, "密码必须由字母加数字组成");
            return;
        }
        if (once.length() < 6 || once.length() > 18) {
            ToastUtil.show(context, "请输入6到18位的密码组合");
            return;
        }
        if (!once.equals(twice)) {
            ToastUtil.show(context, "俩次密码输入不一致");
            return;
        }

        Map<Object, Object> params = new HashMap<Object, Object>();
        params.put("username", phone_number);
        params.put("password", once);
        params.put("password-confirm", twice);
        params.put("code", static_code);
        appUtil.findPassword(params, new MyResponseHandler());
        UnionPayUtils.getInstance().showDialog(mActivity);
    }

    /**
     *  获取注册结果
     */
    private class MyResponseHandler extends JsonHttpResponseHandler {

        @Override
        public void onSuccess(int statusCode, Header[] headers,
                              JSONObject response) {
            try {
                UnionPayUtils.getInstance().disMissDialog();
                boolean success = response.getBoolean("success");
                if (!success) {
                    ToastUtil.show(context,"服务器繁忙,请稍后再试!");
                    return;
                }
                ToastUtil.show(context,"找回密码成功,请重新登陆！");
                mActivity.finish();
                mActivity.overridePendingTransition(R.anim.activity_enter, R.anim.activity_exit);
            } catch (Exception e) {
                e.getMessage();
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
