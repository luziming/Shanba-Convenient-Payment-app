package com.shaba.app.holder;

import android.view.View;
import android.widget.TextView;

import com.shaba.app.R;
import com.shaba.app.been.NewsListEntity;

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
public class NewsListHolder extends BasicHolder<NewsListEntity>{

    @Bind(R.id.txt_str)
    TextView txtStr;

    @Override
    public void bindData(NewsListEntity data) {
        txtStr.setText(data.getTitle());
    }

    @Override
    public View initHolderView() {
        View view = inflater.inflate(R.layout.item_new_list, null);
        ButterKnife.bind(this, view);
        return view;
    }
}
