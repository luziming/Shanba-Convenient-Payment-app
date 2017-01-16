package com.shaba.app.adapter;/*
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
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.shaba.app.holder.BasicHolder;

import java.util.List;

public abstract class BasicAdapter<T> extends BaseAdapter{

    public List<T> list;

    public BasicAdapter(List<T> list) {
        this.list = list;
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        BasicHolder<T> holder = null;
        if (convertView == null) {
            //1.由于每个ListView的实现大致相同,只有holder不同,所以作为抽取交由子类实现
            holder = createViewHolder(position);
        } else {
            holder = (BasicHolder<T>) convertView.getTag();
        }
        //绑定数据
        holder.bindData(list.get(position));

        return holder.getHolderView();
    }

    /**
     * @param position
     */
    public abstract BasicHolder<T> createViewHolder(int position);
}
