package com.shaba.app.fragment.base;

import android.app.Activity;
import android.app.Fragment;

import com.shaba.app.R;


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
public class FragmentUtils {
    /**
     * 以极其炫酷的方式开启Fragment
     */
    public static void startCoolFragment(Activity activity, Fragment fragment, int content) {
        activity.getFragmentManager()
                .beginTransaction()
                .setCustomAnimations(
                        R.animator.fragment_slide_left_enter,
                        R.animator.fragment_slide_left_exit,
                        R.animator.fragment_slide_right_enter,
                        R.animator.fragment_slide_right_exit)
                .add(content, fragment)
                .addToBackStack(null).commit();
    }

    /**
     * 以极其炫酷的方式开启Fragment,带tag
     */
    public static void startCoolFragment(Activity activity, Fragment fragment, int content, String tag) {
        activity.getFragmentManager()
                .beginTransaction()
                .setCustomAnimations(
                        R.animator.fragment_slide_left_enter,
                        R.animator.fragment_slide_left_exit,
                        R.animator.fragment_slide_right_enter,
                        R.animator.fragment_slide_right_exit)
                .add(content, fragment, tag)
                .addToBackStack(null).commit();
    }
}
