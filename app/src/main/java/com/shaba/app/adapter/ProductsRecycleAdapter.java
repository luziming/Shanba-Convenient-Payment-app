package com.shaba.app.adapter;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.shaba.app.R;
import com.shaba.app.been.ProductsEntity;

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
public class ProductsRecycleAdapter extends BaseQuickAdapter<ProductsEntity.DataBean, BaseViewHolder> {


    public ProductsRecycleAdapter(List<ProductsEntity.DataBean> mList) {
        super(R.layout.item_products, mList);
    }

    @Override
    protected void convert(BaseViewHolder helper, ProductsEntity.DataBean item) {
        helper.setText(R.id.tv_payment_name, item.getName());
    }
}
