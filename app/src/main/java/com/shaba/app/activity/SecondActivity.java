package com.shaba.app.activity;

import android.animation.AnimatorInflater;
import android.animation.AnimatorSet;
import android.app.Fragment;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.TextView;

import com.shaba.app.R;
import com.shaba.app.fragment.BankMapFragment;
import com.shaba.app.fragment.CableTVFragment;
import com.shaba.app.fragment.ElectricityFragment;
import com.shaba.app.fragment.FarmersPointMapFragment;
import com.shaba.app.fragment.FlowChargeFragment;
import com.shaba.app.fragment.ForgetPasswordFragment;
import com.shaba.app.fragment.MoreNewsFragment;
import com.shaba.app.fragment.NewsDetailFragment;
import com.shaba.app.fragment.PhoneChargeFragment;
import com.shaba.app.fragment.RegistProtocolFragment;
import com.shaba.app.fragment.SocialSecurityFragment;
import com.shaba.app.fragment.SocialStatusFragment;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Stack;

import butterknife.Bind;
import de.greenrobot.event.EventBus;

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
public class SecondActivity extends BaseActivity {

    @Bind(R.id.toolbar)
    Toolbar toolbar;

    private TextView toolbar_title;

    private String title = "陕坝缴费宝";

    private Stack<String> titleStack = new Stack<>();

    @Override
    protected int getLayoutID() {
        return R.layout.activity_second;
    }

    public void onEvent(String title) {
        //将标题加入栈中
        titleStack.push(title);
        checkTitleLength(title);
        AnimatorSet alphaSet = (AnimatorSet) AnimatorInflater.loadAnimator(this, R.animator.title_right_enter);
        alphaSet.setTarget(toolbar_title);
        alphaSet.start();
    }

    private void checkTitleLength(String title) {
        toolbar_title.setText(title);
    }

    @Override
    protected void initView() {
        EventBus.getDefault().register(this);
        String type = getIntent().getStringExtra("type");
        String t = getIntent().getStringExtra("title");
        Fragment fragment = null;
        Bundle bundle = new Bundle();
        switch (type) {
            case "register":
                title = "缴费宝协议";
                fragment = new RegistProtocolFragment();
                break;
            case "forget":
                title = "忘记密码";
                fragment = new ForgetPasswordFragment();
                break;
            case "electricity":
                title = "电费查询";
                fragment = new ElectricityFragment();
                break;
            case "phone-charge":
                title = "手机话费";
                fragment = new PhoneChargeFragment();
                bundle.putString("MODE", "phone");
                break;
            case "flow-charge":
                title = "流量充值";
                fragment = new FlowChargeFragment();
                break;
            case "broadband-charge":
                title = "宽带缴费";
                fragment = new PhoneChargeFragment();
                bundle.putString("MODE", "broadband");
                break;
            case "tel-charge":
                title = "固话缴费";
                fragment = new PhoneChargeFragment();
                bundle.putString("MODE", "tel");
                break;
            case "social-security":
                title = "社保查询";
                fragment = new SocialSecurityFragment();
                break;
            case "social-status":
                title = "社保状态";
                fragment = new SocialStatusFragment();
                break;
            case "bank-map":
                title = "网点地图";
                bundle.putInt("MAP_TYPE", 1);
                fragment = new BankMapFragment();
                break;
            case "atm-map":
                title = "ATM地图";
                bundle.putInt("MAP_TYPE", 2);
                fragment = new BankMapFragment();
                break;
            case "farmers-point-map":
                title = "助农点地图";
                fragment = new FarmersPointMapFragment();
                break;
            case "news-detail": //新闻内容
                title = t;
                bundle.putString("url", getIntent().getStringExtra("url"));
                fragment = new NewsDetailFragment();
                break;
            case "more-news":
                title = "更多新闻";
                fragment = new MoreNewsFragment();
                break;
            case "industry-payment":
                title = "全行业缴费";
//                fragment = new IndustryPayment();
                break;
            case "cable-tv":
                title = "有线电视";
                fragment = new CableTVFragment();
                break;
        }
        fragment.setArguments(bundle);
        getFragmentManager().beginTransaction()
                .replace(R.id.fl_container_content, fragment, title)
                .commit();
        //初始化Toolbar
        initToolbar();

        titleStack.push(title);
    }

    private void initToolbar() {
        toolbar.setTitle("");//设置Toolbar标题
        toolbar_title = (TextView) findViewById(R.id.toolbar_title);
        checkTitleLength(title);
        setSupportActionBar(toolbar);
        getSupportActionBar().setHomeButtonEnabled(true); //设置返回键可用
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        if (titleStack.size() > 1 && !titleStack.empty() && getFragmentManager().getBackStackEntryCount() != 0) {
            titleStack.pop();
            toolbar_title.setText(titleStack.peek());
        }
        super.onBackPressed();
        overridePendingTransition(R.anim.activity_enter, R.anim.activity_exit);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        /*************************************************
         * 步骤3：处理银联手机支付控件返回的支付结果
         ************************************************/
        if (data == null) {
            return;
        }
        if (requestCode == 1) {
            //选择联系人
            Fragment fragment = getFragmentManager().findFragmentByTag(title);
            fragment.onActivityResult(requestCode, resultCode, data);
            return;
        }

        String msg = "";
        /*
         * 支付控件返回字符串:success、fail、cancel 分别代表支付成功，支付失败，支付取消
         */
        String str = data.getExtras().getString("pay_result");

        if (str.equalsIgnoreCase("success")) {
            // 支付成功后，extra中如果存在result_data，取出校验
            // result_data结构见c）result_data参数说明
            if (data.hasExtra("result_data")) {
                String result = data.getExtras().getString("result_data");
                try {
                    JSONObject resultJson = new JSONObject(result);
                    String sign = resultJson.getString("sign");
                    String dataOrg = resultJson.getString("data");
                    // 验签证书同后台验签证书
                    // 此处的verify，商户需送去商户后台做验签
                    boolean ret = verify(dataOrg, sign, "01");
                    if (ret) {
                        // 验证通过后，显示支付结果
                        msg = "支付成功！";
                    } else {
                        // 验证不通过后的处理
                        // 建议通过商户后台查询支付结果
                        msg = "支付失败！";
                    }
                } catch (JSONException e) {

                }
            } else {
                // 未收到签名信息
                // 建议通过商户后台查询支付结果
                msg = "支付成功！";
            }
        } else if (str.equalsIgnoreCase("fail")) {
            msg = "支付失败！";
        } else if (str.equalsIgnoreCase("cancel")) {
            msg = "用户取消了支付";
        }
//        onBackPressed();
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("支付结果通知");
        builder.setMessage(msg);
        builder.setInverseBackgroundForced(true);
        // builder.setCustomTitle();
        builder.setNegativeButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        builder.show();
    }

    private boolean verify(String msg, String sign64, String mode) {
        // 此处的verify，商户需送去商户后台做验签
        return true;
    }
}
