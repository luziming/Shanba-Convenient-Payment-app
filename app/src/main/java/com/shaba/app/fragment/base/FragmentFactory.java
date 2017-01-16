package com.shaba.app.fragment.base;/*
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


import android.app.Fragment;

import com.shaba.app.fragment.AboutUsFragment;
import com.shaba.app.fragment.HomeFragment;
import com.shaba.app.fragment.RecordFragment;

import java.util.HashMap;
import java.util.Map;


public class FragmentFactory {

    //为了提高效率,每次不用new出来
    public static Map<Integer, Fragment> map = new HashMap();
    /**
     * 根据不同的位置创建Fragment
     */
    public static Fragment create(int position) {
        Fragment fragment = map.get(position);
        if (fragment == null) {
            switch (position) {
                case 0:
                    fragment = new HomeFragment();
                    break;
                case 1:
                    fragment = new AboutUsFragment();
                    break;
                case 2:
                    fragment = new RecordFragment();
                    break;
                case 3:

                    break;
            }
            map.put(position,fragment);
        }
        return fragment;
    }

}
