package com.shaba.app.activity;

import android.content.Intent;
import android.os.SystemClock;

import com.loopj.android.http.AsyncHttpResponseHandler;
import com.shaba.app.R;
import com.shaba.app.utils.CommonTools;
import com.shaba.app.utils.ToastUtil;

import org.apache.http.Header;
import org.json.JSONException;
import org.json.JSONObject;

public class SplashActivity extends BaseActivity {


    @Override
    protected int getLayoutID() {
        return R.layout.activity_splash;
    }

    @Override
    protected void initView() {
        //检查登录状态
        appUtil.checkLoginState(new CheckLoginStateResponse(), token);
    }

    public void mainUI() {
        startActivity(new Intent(SplashActivity.this, MainActivity.class));
        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
        finish();
    }

    //检查登录状态
    private class CheckLoginStateResponse extends AsyncHttpResponseHandler {

        @Override
        public void onFailure(int arg0, Header[] arg1, byte[] arg2, Throwable arg3) {
            ToastUtil.show(SplashActivity.this, "请检查网络");
            new Thread(){
                @Override
                public void run() {
                    SystemClock.sleep(2000);
                    mainUI();
                }
            }.start();
        }

        @Override
        public void onSuccess(int arg0, Header[] arg1, byte[] arg2) {
            try {
                JSONObject json = new JSONObject(new String(arg2));
                boolean isLogin = json.getBoolean("token");
                if (!isLogin) {
                    startActivity(new Intent(SplashActivity.this, LoginActivity.class));
                    overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
                    finish();
                } else {
                    new Thread(){
                        @Override
                        public void run() {
                            SystemClock.sleep(2000);
                            CommonTools.runOnUIThread(new Runnable() {
                                @Override
                                public void run() {
                                    mainUI();
                                }
                            });

                        }
                    }.start();
                }
            } catch (JSONException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
    }
}
