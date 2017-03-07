package com.shaba.app.adapter;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.support.v7.widget.OrientationHelper;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.shaba.app.R;
import com.shaba.app.been.Menu;

import java.util.ArrayList;
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
public class MenuAdapter extends PagerAdapter {

    private LayoutInflater inflater;
    private Context context;

    int imgs[] = {
            R.drawable.icon_menu1,
            R.drawable.icon_menu16,
            R.drawable.icon_menu5,
            R.drawable.icon_menu6,
            R.drawable.icon_menu7,
            R.drawable.icon_menu3,
            R.drawable.icon_menu4,
            R.drawable.icon_menu8
    };
    int imgs2[] = {
            R.drawable.icon_menu9,
            R.drawable.icon_menu13,
            R.drawable.icon_menu10,
            R.drawable.icon_menu14,
            R.drawable.icon_menu11,
            R.drawable.icon_menu15,
            R.drawable.icon_menu12,
            R.drawable.icon_menu2
    };

    String imgs_str[] = {"电费", "全行业缴费", "手机话费", "固话缴费", "流量充值", "社保查询", "有线电视", "社保状态"};
    String imgs_str2[] = {"暖气费", "公积金查询", "天然气", "快递查询", "火车票", "违章查询", "加油卡充值", "宽带缴费"};

    private List<Menu> menus_1 = new ArrayList<Menu>() {
        {
            for (int i = 0; i < imgs.length; i++) {
                Menu menu = new Menu(imgs[i], imgs_str[i]);
                add(menu);
            }
        }
    };

    private List<Menu> menus_2 = new ArrayList<Menu>() {
        {
            for (int i = 0; i < imgs2.length; i++) {
                Menu menu = new Menu(imgs2[i], imgs_str2[i]);
                add(menu);
            }
        }
    };

    public MenuAdapter(LayoutInflater inflater, Context context) {
        this.inflater = inflater;
        this.context = context;
    }

    @Override
    public int getCount() {
        return 2;
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {

        View view = inflater.inflate(R.layout.holder_home_menu, null);

        RecyclerView recyclerView = (RecyclerView) view.findViewById(R.id.recycleView_menu);

        MenuRecycleAdapter adapter = new MenuRecycleAdapter(inflater, position == 0 ? menus_1 : menus_2);

        CustomGridLayoutManager layoutManager = new CustomGridLayoutManager(context,2);
        //禁止滑动
        layoutManager.setScrollEnabled(false);
        //设置为垂直布局，这也是默认的
        layoutManager.setOrientation(OrientationHelper. HORIZONTAL);
        //设置布局管理器给RecycleView
        recyclerView.setLayoutManager(layoutManager);
        //设置adapter
        recyclerView.setAdapter(adapter);
        //点击事件
        adapter.setOnItemClickListener(new MenuRecycleAdapter.OnItemClickListener() {
            @Override
            public void onClick(int position) {
                if (mOnItemClickListener != null) {
                    mOnItemClickListener.onClick(position);
                }
            }
        });

        container.addView(view);

        return view;
    }

    private MenuRecycleAdapter.OnItemClickListener mOnItemClickListener;

    public interface OnItemClickListener {
        void onClick(int position);
    }

    public void setOnItemClickListener(MenuRecycleAdapter.OnItemClickListener onItemClickListener) {
        this.mOnItemClickListener = onItemClickListener;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((View)object);
    }

}
