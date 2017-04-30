package com.shaba.app.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;

import com.loopj.android.http.JsonHttpResponseHandler;
import com.shaba.app.R;
import com.shaba.app.activity.LoginActivity;
import com.shaba.app.fragment.base.BaseFragment;
import com.shaba.app.fragment.base.FragmentUtils;
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
import butterknife.OnClick;

import static com.shaba.app.global.MyApplication.context;

/*
                 写字楼里写字间，写字间里程序员；  
                 程序人员写程序，又拿程序换酒钱。  
                 酒醒只在网上坐，酒醉还来网下眠；  
                 酒醉酒醒日复日，网上网下年复年。  
                 但愿老死电脑间，不愿鞠躬老板前；  
                 奔驰宝马贵者趣，公交自行程序员。  
                 别人笑我忒疯癫，我笑自己命太贱；  
                 不见满街漂亮妹，哪个归得程序员？

                                   Created by luziming on 2017/4/27
*/
public class CableTVFragment extends BaseFragment {

    @Bind(R.id.tv_yhbh)
    AutoCompleteTextView tvYhbh;
    @Bind(R.id.tv_jfys)
    EditText tvJfys;
    private String userCode;
    private String mounthCount;

    @Override
    public View initViews() {
        View view = inflater.inflate(R.layout.fragment_cable_tv, null);
        ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void initData() {
        initAutoComplete("cableTV", tvYhbh);
    }

    @OnClick(R.id.tv_next)
    public void onViewClicked() {
        if (StringUtil.isFastClick())
            return;
        userCode = tvYhbh.getText().toString().trim();
        mounthCount = tvJfys.getText().toString().trim();
        if (TextUtils.isEmpty(userCode)) {
            ToastUtils.showToast("请输入用户编号");
            return;
        }
        if (TextUtils.isEmpty(mounthCount)) {
            ToastUtils.showToast("请输入缴费月数");
        }
        int ysInt = Integer.valueOf(mounthCount);
        if (ysInt < 1 || ysInt > 24) {
            ToastUtil.show(context, R.string.tv_validate_ys);
            return;
        }

        Map<Object, Object> map = new HashMap<Object, Object>();
        map.put("usr_num", userCode);
        map.put("pay_month_num", mounthCount);
        map.put("product_tp", "002");
        saveHistory("cableTV", userCode);
        appUtil.queryBillTV(map, new MyResponseHandler(), token);
        UnionPayUtils.getInstance().showDialog(getActivity());
    }

    private class MyResponseHandler extends JsonHttpResponseHandler {
        @Override
        public void onSuccess(int statusCode, Header[] headers,
                              JSONObject response) {
            try {
                UnionPayUtils.getInstance().disMissDialog();
                if (!response.getBoolean("success")) {
                    if (!response.isNull("error")) {
                        if ("noauth".equals(response.getString("error"))) {
                            ToastUtil.show(context, R.string.re_login);
                            Intent intent = new Intent(getActivity(), LoginActivity.class);
                            startActivity(intent);
                            getActivity().overridePendingTransition(R.anim.activity_open, R.anim.activity_close);
                            return;
                        }
                    }
                    if (!response.isNull("message")) {
                        ToastUtil.show(context, response.getString("message"));
                        return;
                    }
                }
                if (response != null) {
                    SubmitCableTVFragment fragment = new SubmitCableTVFragment();
                    Bundle bundle = new Bundle();
                    bundle.putString("data", response.toString());
                    bundle.putString("usrNum", userCode + "");
                    bundle.putString("jfys", mounthCount + "");
                    fragment.setArguments(bundle);
                    FragmentUtils.startCoolFragment(mActivity,fragment,R.id.fl_container_content);
                }
            } catch (Exception e) {
                e.getMessage();
            }
        }

        @Override
        public void onFailure(int statusCode, Header[] headers,
                              Throwable throwable, JSONObject errorResponse) {
            UnionPayUtils.getInstance().disMissDialog();
            ToastUtil.show(context, R.string.error_wifi_disconnect);
        }

        @Override
        public void onFinish() {
            UnionPayUtils.getInstance().disMissDialog();
        }
    }
}
