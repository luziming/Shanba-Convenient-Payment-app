package com.shaba.app.holder;

import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;

import com.shaba.app.R;
import com.shaba.app.adapter.NewsListAdapater;
import com.shaba.app.been.NewsListEntity;
import com.shaba.app.view.NoScrollListView;

import java.util.List;

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
public class NewsHolder extends BasicHolder<List<NewsListEntity>> {

    @Bind(R.id.listdis)
    NoScrollListView listdis;
    @Bind(R.id.btn_seeAll)
    Button btnSeeAll;

    private OnNewsClickListener mOnMoreNewsClickListener;

    public interface OnNewsClickListener {
        void onMoreNewsClick();
        void onNewsItemClick(AdapterView<?> parent, View view, int position, long id);
    }

    public void setOnMapClickListener(OnNewsClickListener mOnMoreNewsClickListener) {
        this.mOnMoreNewsClickListener = mOnMoreNewsClickListener;
    }

    @Override
    public void bindData(List<NewsListEntity> data) {
        listdis.setAdapter(new NewsListAdapater(data));
        btnSeeAll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mOnMoreNewsClickListener != null) {
                    mOnMoreNewsClickListener.onMoreNewsClick();
                }
            }
        });
        listdis.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (mOnMoreNewsClickListener != null) {
                    mOnMoreNewsClickListener.onNewsItemClick(parent,view,position,id);
                }
            }
        });

    }

    @Override
    public View initHolderView() {
        View view = inflater.inflate(R.layout.holser_news, null);
        ButterKnife.bind(this, view);
        return view;
    }
}
