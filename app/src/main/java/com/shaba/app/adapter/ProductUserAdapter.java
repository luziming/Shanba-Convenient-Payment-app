package com.shaba.app.adapter;

import android.view.View;

import com.chad.library.adapter.base.BaseMultiItemQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.chad.library.adapter.base.entity.MultiItemEntity;
import com.shaba.app.R;
import com.shaba.app.been.LvFatherEntity;
import com.shaba.app.been.PersonEntity;

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
public class ProductUserAdapter extends BaseMultiItemQuickAdapter<MultiItemEntity, BaseViewHolder> {

    public static final int TYPE_LEVEL_0 = 0;
    public static final int TYPE_PERSON = 1;

    public ProductUserAdapter(List<MultiItemEntity> data) {
        super(data);
        addItemType(TYPE_LEVEL_0, R.layout.item_product_recycle_father);
        addItemType(TYPE_PERSON, R.layout.item_product_recycle_child);
    }

    @Override
    protected void convert(final BaseViewHolder helper, MultiItemEntity item) {
        switch (helper.getItemViewType()) {
            case TYPE_LEVEL_0:
                final LvFatherEntity lv0 = (LvFatherEntity) item;
                helper.setText(R.id.tv_user_name, lv0.getUsername())
                        .setImageResource(R.id.iv_arrow, lv0.isExpanded() ? R.drawable.ic_arrow_up : R.drawable.ic_arrow_bottom)
                        .setImageResource(R.id.iv_point, lv0.getStatus() == 2 ? R.drawable.shape_green : R.drawable.shape_red);
                helper.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        int pos = helper.getAdapterPosition();
                        if (lv0.isExpanded()) {
                            collapse(pos);
                        } else {
                            expand(pos);
                        }
                    }
                });
                break;
            case TYPE_PERSON:
                final PersonEntity lv1 = (PersonEntity) item;
                helper.setText(R.id.tv__child_user_name, lv1.getUser_name())
                        .setText(R.id.tv_product_amount, lv1.getAmount())
                        .setText(R.id.tv_product_company, lv1.getMer_name())
                        .setText(R.id.tv_product_status, lv1.getStatus() == 2 ? "已缴费":"未缴费")
                        .setText(R.id.tv_product_else, lv1.getName())
                        .setText(R.id.tv_product_code,lv1.getUser_code())
                        .setVisible(R.id.rl_buttons,lv1.getStatus() ==2 ? false : true)
                        .addOnClickListener(R.id.bt_payment);
                break;
        }
    }
}
