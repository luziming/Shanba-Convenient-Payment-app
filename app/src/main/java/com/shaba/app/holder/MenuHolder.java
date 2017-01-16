package com.shaba.app.holder;

import android.app.Activity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.OrientationHelper;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.shaba.app.R;
import com.shaba.app.adapter.MenuRecycleAdapter;

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
public class MenuHolder extends BasicHolder<Integer> {

    @Bind(R.id.recycleView_menu)
    RecyclerView recycleView;

    private Activity mActivity;

    public MenuRecycleAdapter recycleAdapter;

    public MenuHolder(Activity mActivity) {
        this.mActivity = mActivity;
    }
//    private MenuRecycleAdapter.OnItemClickListener mOnItemClickListener;
//
//    public interface OnItemClickListener {
//        void onClick(int position);
//    }
//
//    public void setOnItemClickListener(MenuRecycleAdapter.OnItemClickListener onItemClickListener) {
//        this.mOnItemClickListener = onItemClickListener;
//    }

    @Override
    public void bindData(Integer data) {

    }

    @Override
    public View initHolderView() {
        View view = inflater.inflate(R.layout.holder_home_menu, null);
        ButterKnife.bind(this, view);
        recycleAdapter = new MenuRecycleAdapter(inflater);
        //设置布局管理器
        GridLayoutManager layoutManager = new GridLayoutManager(mActivity, 2);
        //设置为垂直布局，这也是默认的
        layoutManager.setOrientation(OrientationHelper. HORIZONTAL);
        //设置布局管理器给RecycleView
        recycleView.setLayoutManager(layoutManager);
        //设置adapter
        recycleView.setAdapter(recycleAdapter);

//        recycleAdapter.setOnItemClickListener(new MenuRecycleAdapter.OnItemClickListener() {
//            @Override
//            public void onClick(int position) {
//                if (mOnItemClickListener != null) {
//                    mOnItemClickListener.onClick(position);
//                }
//            }
//        });
        return view;
    }
}
