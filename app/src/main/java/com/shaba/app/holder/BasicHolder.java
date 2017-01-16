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

import android.view.LayoutInflater;
import android.view.View;

import com.shaba.app.global.MyApplication;


public abstract class BasicHolder<T> {

    public LayoutInflater inflater;
    //此View就相当于adapter的convertView
    public View holderView;

    public BasicHolder() {
        if (inflater == null) {
            inflater = LayoutInflater.from(MyApplication.context);
        }

        //具体交给子类实现
        holderView = initHolderView();

        //绑定holderView和自己holder
        holderView.setTag(this);


    }
    //提供绑定数据的方法,不知道具体数据类型,用泛型来表示
    public abstract void bindData(T data);

    //提供公共的get方法
    public View getHolderView(){
        return holderView;
    }

    public abstract View initHolderView();

}
