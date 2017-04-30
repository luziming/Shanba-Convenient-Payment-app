package com.shaba.app.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.loopj.android.http.JsonHttpResponseHandler;
import com.shaba.app.R;
import com.shaba.app.activity.LoginActivity;
import com.shaba.app.fragment.base.BaseFragment;
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

                                   Created by luziming on 2017/4/28
*/
public class SubmitCableTVFragment extends BaseFragment {


    @Bind(R.id.tv_jfxm)
    TextView tvJfxm;
    @Bind(R.id.tv_yhbh)
    TextView tvYhbh;
    @Bind(R.id.tv_jfys)
    TextView tvJfys;
    @Bind(R.id.tv_yjje)
    TextView tvYjje;
    @Bind(R.id.tv_username)
    TextView tvUsername;
    @Bind(R.id.tv_jfje)
    EditText tvJfje;
    private String custom_id;
    private String origQryId;

    @Override
    public View initViews() {
        View view = inflater.inflate(R.layout.fragment_cable_tv_submit, null);
        ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void initData() {
        Bundle arguments = getArguments();
        String data = arguments.getString("data");
        String usrNum = arguments.getString("usrNum");
        String jfys = arguments.getString("jfys");
        if (!StringUtil.isEmpty(data)) {
            try {
                JSONObject jo = new JSONObject(data);
                jo=jo.getJSONObject("obj");
                custom_id = jo.getString("customer_id");
                origQryId = jo.getString("orig-qry-id");
                //缴费项目
                String title = "";
                if (!jo.isNull("title")) {
                    title = jo.getString("title");
                    tvJfxm.setText(title);
                }

                //客户号
                tvYhbh.setText(usrNum);
                //应缴金额
                String yjje = "";
                if (!jo.isNull("amountstr")) {
                    yjje = jo.getString("amountstr");
                    tvYjje.setText(yjje);
                    tvJfje.setText(yjje);
                }
                //缴费月数
                tvJfys.setText(jfys);
                //用户名称
                String userName = "";
                if (!jo.isNull("customer_name")) {
                    userName = jo.getString("customer_name");
                    tvUsername.setText(userName);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    @OnClick(R.id.tv_submit)
    public void onViewClicked() {
        if (StringUtil.isFastClick())
            return;
        String amount=tvJfje.getText().toString();
        if (StringUtil.isEmpty(amount)){
            ToastUtil.show(context, "缴费金额不能为空！");
            return;
        }
        Map<Object, Object> map = new HashMap<Object, Object>();
        map.put("txnAmt",amount);
        map.put("usr_num", custom_id);
        map.put("origQryId", origQryId);
        appUtil.getTVTnFromUnionPay(map ,token ,new MyResponseHandler());
    }

    private class MyResponseHandler extends JsonHttpResponseHandler {

        private String unionpayOrderId;

        @Override
        public void onSuccess(int statusCode, Header[] headers,
                              JSONObject response) {
            try {
                if (!response.getBoolean("success")) {
                    if (!response.isNull("error")) {
                        if ("noauth".equals(response.getString("error"))) {
                            ToastUtil.show(context, R.string.re_login);
                            Intent intent = new Intent(mActivity,LoginActivity.class);
                            startActivity(intent);
                            return;
                        }
                    }
                    if (!response.isNull("message")) {
                        ToastUtil.show(context, response.getString("message"));
                        return;
                    }

                }
                String tn = "";
                response = response.getJSONObject("obj");
                if (!response.isNull("tn")) {
                    tn = response.getString("tn");
                }
                if (!response.isNull("unionPayOrderId")) {
                    unionpayOrderId = response.getString("unionPayOrderId");
                }
                UnionPayUtils.getInstance().unionPay(mActivity,tn);
            } catch (Exception e) {
            }
        }

        @Override
        public void onFailure(int statusCode, Header[] headers,
                              Throwable throwable, JSONObject errorResponse) {
        }



        @Override
        public void onFinish() {
        }


    }
}
