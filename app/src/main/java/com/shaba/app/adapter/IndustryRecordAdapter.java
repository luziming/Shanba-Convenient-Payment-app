package com.shaba.app.adapter;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.shaba.app.R;
import com.shaba.app.been.IndustryRecordEntity;

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
public class IndustryRecordAdapter extends BaseQuickAdapter<IndustryRecordEntity, BaseViewHolder> {

    public IndustryRecordAdapter(List<IndustryRecordEntity> data) {
        super(R.layout.item_industry_record, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, IndustryRecordEntity item) {

        helper.setText(R.id.tv_record_name, item.getUser_name())
                .

                        setText(R.id.tv_record_amount, "+ " + item.getAmount()

                        )
                .

                        setText(R.id.tv_record_what, item.getDescp()

                                .

                                        isEmpty()

                                ? "暂无描述" : item.getDescp())
                .

                        setText(R.id.tv_record_time, item.getUpdated_at()

                        )
                .

                        setText(R.id.tv_record_status, item.getStatus()

                                == 2 ? "交易成功" : "交易失败")
                .

                        setImageResource(R.id.iv_point, item.getStatus()

                                == 2 ? R.drawable.shape_green : R.drawable.shape_red);
    }
}
