package com.shaba.app.holder;

import android.view.View;

import com.shaba.app.R;

import butterknife.ButterKnife;
import butterknife.OnClick;

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
public class MapHolder extends BasicHolder<String> {

    private OnMapClickListener mOnMapClickListener;

    @OnClick({R.id.tv_branches, R.id.tv_atm, R.id.ll_bank})
    public void onClick(View view) {
        if (mOnMapClickListener != null){
            switch (view.getId()) {
                case R.id.tv_branches:
                    mOnMapClickListener.onMapListClick(0);
                    break;
                case R.id.tv_atm:
                    mOnMapClickListener.onMapListClick(1);
                    break;
                case R.id.ll_bank:
                    mOnMapClickListener.onMapListClick(2);
                    break;
            }
        }

    }

    public interface OnMapClickListener {
        void onMapListClick(int position);
    }

    public void setOnMapClickListener(OnMapClickListener mOnMapClickListener) {
        this.mOnMapClickListener = mOnMapClickListener;
    }

    @Override
    public void bindData(String data) {

    }

    @Override
    public View initHolderView() {
        View view = inflater.inflate(R.layout.holder_map, null);
        ButterKnife.bind(this, view);
        return view;
    }
}
