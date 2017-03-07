package com.shaba.app.adapter;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.shaba.app.R;
import com.shaba.app.been.Menu;

import java.util.List;

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
public class Menu2RecycleAdapter extends BaseQuickAdapter<Menu, BaseViewHolder> {


    public Menu2RecycleAdapter(List<Menu> data) {
            super(R.layout.recycle_item,data);
    }

    @Override
    protected void convert(BaseViewHolder helper, Menu item) {
        helper.setText(R.id.tv_recycle_item, item.getName())
                .setImageResource(R.id.iv_recycle_item, item.getIcon());
    }
}
