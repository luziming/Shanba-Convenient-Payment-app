package com.shaba.app.holder;

import android.os.SystemClock;
import android.view.View;
import android.widget.TextView;

import com.shaba.app.R;

import butterknife.Bind;
import butterknife.ButterKnife;

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
public class IndustryTypeHolder extends BasicHolder<String> {

    @Bind(R.id.tv_payment_name)
    TextView tv_payment_name;
    @Override
    public void bindData(String data) {
        tv_payment_name.setText("北京时间：" + SystemClock.currentThreadTimeMillis());
    }

    @Override
    public View initHolderView() {
        View view = inflater.inflate(R.layout.item_products,null);
        ButterKnife.bind(this,view);
        return view;
    }
}
