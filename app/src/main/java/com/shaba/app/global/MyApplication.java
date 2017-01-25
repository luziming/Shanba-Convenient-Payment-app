package com.shaba.app.global;

import android.app.Application;
import android.content.Context;
import android.os.Handler;

import com.tencent.bugly.Bugly;

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
public class MyApplication extends Application {

    public static Handler mHandler = new Handler();

    public static Context context;

    @Override
    public void onCreate() {
        super.onCreate();
        if (context == null) {
            context = this;
        }

        Bugly.init(getApplicationContext(), "注册时申请的APPID", false);
    }
}
