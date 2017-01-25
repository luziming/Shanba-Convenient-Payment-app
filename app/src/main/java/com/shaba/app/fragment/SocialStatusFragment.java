package com.shaba.app.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;

import com.loopj.android.http.JsonHttpResponseHandler;
import com.shaba.app.R;
import com.shaba.app.activity.LoginActivity;
import com.shaba.app.been.SocialStatusResult;
import com.shaba.app.fragment.base.BaseFragment;
import com.shaba.app.fragment.base.FragmentUtils;
import com.shaba.app.utils.RegexUtil;
import com.shaba.app.utils.StringUtil;
import com.shaba.app.utils.ToastUtil;
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
public class SocialStatusFragment extends BaseFragment implements View.OnClickListener {


    @Bind(R.id.autoComplete_status_id)
    AutoCompleteTextView autoCompleteStatusId;
    @Bind(R.id.et_realNum)
    EditText et_real_name;
    @Bind(R.id.btn_query_social_security)
    Button bt_quert;

    @Override
    public View initViews() {
        View view = inflater.inflate(R.layout.fragment_social_status, null);
        ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void initData() {
        bt_quert.setOnClickListener(this);
        initAutoComplete("social-status",autoCompleteStatusId);
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
        String IDCard = autoCompleteStatusId.getText().toString().trim();
        String realName = et_real_name.getText().toString().trim();
        if (TextUtils.isEmpty(IDCard) || !RegexUtil.IdCardRegex(IDCard.replace("x", "X"))) {
            ToastUtil.show(mActivity, R.string.error_id_num);
            return;
        }
        if (TextUtils.isEmpty(realName)) {
            ToastUtil.show(mActivity, R.string.error_id_nm);
            return;
        }
        Map<Object, Object> params = new HashMap<>();
        params.put("usr_num", IDCard);
        params.put("usr_nm",realName);
        appUtil.querySocialSecurity(params, new QuerySocialSecurityHandler(),token);
        UnionPayUtils.getInstance().showDialog(mActivity);
        saveHistory("social-status", IDCard);
    }

    private class QuerySocialSecurityHandler extends JsonHttpResponseHandler {
        @Override
        public void onSuccess(int statusCode, Header[] headers,
                              JSONObject response) {
            try {
                UnionPayUtils.getInstance().disMissDialog();
                if(response.getBoolean("success")){
                    JSONObject obj = response.getJSONObject("obj");
                    SocialStatusResult result = new SocialStatusResult();
                    result.setState(obj.getString("state"));
                    result.setCustomer_id(obj.getString("customer_id"));
                    result.setCustomer_name(obj.getString("customer_name"));
                    SocialStatusDetailFragment fragment = new SocialStatusDetailFragment();
                    Bundle bundle = new Bundle();
                    bundle.putParcelable("result",result);
                    fragment.setArguments(bundle);
                    FragmentUtils.startCoolFragment(mActivity,fragment,R.id.fl_container_content);
                }else if(!response.getBoolean("success")){
                    if (!response.isNull("error")) {
                        if ("noauth".equals(response.getString("error"))) {
                            ToastUtil.show(context, R.string.re_login);
                            startActivity(new Intent(mActivity, LoginActivity.class));
                            mActivity.overridePendingTransition(R.anim.activity_open, R.anim.activity_close);
                            mActivity.finish();
                            return;
                        }
                    }
                    if (!response.isNull("message")) {
                        ToastUtil.show(context, response.getString("message"));
                        return;
                    }
                }
            } catch (JSONException e) {
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
