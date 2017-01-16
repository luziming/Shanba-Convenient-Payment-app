package com.shaba.app.holder;/*
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

import android.view.View;
import android.widget.TextView;

import com.shaba.app.R;
import com.shaba.app.been.MapListEntity;

import butterknife.Bind;
import butterknife.ButterKnife;

public class BankMapHolder extends BasicHolder<MapListEntity> {


    @Bind(R.id.txt_str)
    TextView txtStr;

    @Override
    public void bindData(MapListEntity data) {
        txtStr.setText(data.getName());
    }

    @Override
    public View initHolderView() {
        View view = inflater.inflate(R.layout.item_bank_map, null);
        ButterKnife.bind(this, view);
        return view;
    }
}
