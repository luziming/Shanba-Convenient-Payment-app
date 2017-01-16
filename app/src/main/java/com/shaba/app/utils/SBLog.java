package com.shaba.app.utils;

import android.support.annotation.Nullable;

import com.orhanobut.logger.Logger;

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
public class SBLog {

    private static boolean mIsDubug = true;

    public static void d(@Nullable String info, Object... args) {
        if (!mIsDubug) {
            return;
        }
        Logger.d(info,args);
    }
    public static void d(Object... args) {
        if (!mIsDubug) {
            return;
        }
        Logger.d(args);
    }
    public static void e(@Nullable String info, Object... args) {
        if (!mIsDubug) {
            return;
        }
        Logger.e(info,args);
    }

    public static void json(String tag,String json) {
        if (!mIsDubug) {
            return;
        }
        Logger.t(tag).json(json);
    }

}
