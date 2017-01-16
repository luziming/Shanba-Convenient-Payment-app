package com.shaba.app.adapter;

import com.shaba.app.been.MapListEntity;
import com.shaba.app.holder.BankMapHolder;
import com.shaba.app.holder.BasicHolder;

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
public class BankMapAdapter extends BasicAdapter<MapListEntity> {

    public BankMapAdapter(List<MapListEntity> list) {
        super(list);
    }

    @Override
    public BasicHolder<MapListEntity> createViewHolder(int position) {
        return new BankMapHolder();
    }
}
