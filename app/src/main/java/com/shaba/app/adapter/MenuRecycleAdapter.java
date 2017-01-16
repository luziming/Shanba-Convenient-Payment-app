package com.shaba.app.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.shaba.app.R;
import com.shaba.app.global.MyApplication;
import com.shaba.app.holder.MenuAdapterHolder;
import com.shaba.app.utils.DeviceUtils;

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
public class MenuRecycleAdapter extends RecyclerView.Adapter<MenuAdapterHolder> {

    private LayoutInflater inflater;

    int imgs[] = {
            R.drawable.icon_menu1,
            R.drawable.icon_menu2,
            R.drawable.icon_menu5,
            R.drawable.icon_menu6,
            R.drawable.icon_menu7,
            R.drawable.icon_menu3,
            R.drawable.icon_menu4,
            R.drawable.icon_menu8,
            R.drawable.icon_menu9,
            R.drawable.icon_menu13,
            R.drawable.icon_menu10,
            R.drawable.icon_menu14,
            R.drawable.icon_menu11,
            R.drawable.icon_menu15,
            R.drawable.icon_menu12,
            R.drawable.icon_menu16};

    String imgs_str[] = new String[]{"电费", "宽带缴费", "手机话费", "固话缴费", "流量充值", "社保查询", "有线电视", "社保状态",
            "暖气费","公积金查询","天然气","快递查询","火车票","违章查询","加油卡充值","全行业缴费"};

    private OnItemClickListener mOnItemClickListener;

    public interface OnItemClickListener {
        void onClick(int position);
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.mOnItemClickListener = onItemClickListener;
    }

    public MenuRecycleAdapter(LayoutInflater mInflater) {
        this.inflater = mInflater;
    }

    @Override
    public MenuAdapterHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.recycle_item, null);
        return new MenuAdapterHolder(view);
    }

    @Override
    public void onBindViewHolder(MenuAdapterHolder holder, final int position) {
        ViewGroup.LayoutParams params = holder.ll_recycle_root.getLayoutParams();
        int screenWidth = DeviceUtils.getScreenWidth(MyApplication.context);
        params.width = (int) (screenWidth * 0.25);
//        params.height = (int) (screenWidth * 0.25);
        holder.ll_recycle_root.setLayoutParams(params);
        holder.tv_recycle_item.setText(imgs_str[position]);
        holder.iv_recycle_item.setImageResource(imgs[position]);
        holder.ll_recycle_root.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mOnItemClickListener != null) {
                    mOnItemClickListener.onClick(position);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return imgs.length;
    }
}
