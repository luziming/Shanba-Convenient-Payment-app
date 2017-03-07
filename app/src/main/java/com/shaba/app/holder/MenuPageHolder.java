package com.shaba.app.holder;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.RadioGroup;

import com.shaba.app.R;
import com.shaba.app.adapter.MenuAdapter;
import com.shaba.app.view.WrapContentHeightViewPager;

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
public class MenuPageHolder extends BasicHolder<String> {

    @Bind(R.id.viewPager_menu)
    WrapContentHeightViewPager mViewPager;
    @Bind(R.id.rg_tab)
    RadioGroup mTab;

    private int page = 0;
    private Context context;
    public  MenuAdapter adapter;

    public int getPage() {
        return page;
    }

    public MenuPageHolder(Context context) {

        this.context = context;
    }

    @Override
    public void bindData(String data) {

    }

    @Override
    public View initHolderView() {
        View view = inflater.inflate(R.layout.holder_menu_page, null);
        ButterKnife.bind(this, view);
        adapter = new MenuAdapter(inflater, context);
        mViewPager.setAdapter(adapter);
        mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                mTab.check(position == 0 ? R.id.rb_tab_1 : R.id.rb_tab_2);
                page = position;
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        mTab.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if (checkedId == R.id.rb_tab_1) {
                    mViewPager.setCurrentItem(0);
                } else {
                    mViewPager.setCurrentItem(1);
                }
            }
        });
        return view;
    }
}
