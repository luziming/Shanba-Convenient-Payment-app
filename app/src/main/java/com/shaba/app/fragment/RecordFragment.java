package com.shaba.app.fragment;

import android.view.Gravity;
import android.view.View;
import android.widget.TextView;

import com.shaba.app.fragment.base.BaseFragment;

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
public class RecordFragment extends BaseFragment {


    @Override
    public View initViews() {
        TextView textView = new TextView(getActivity());
        textView.setText("账单查询");
        textView.setGravity(Gravity.CENTER);
        textView.setTextSize(28);
        return textView;
    }
}
