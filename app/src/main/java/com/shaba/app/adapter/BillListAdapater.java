package com.shaba.app.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.shaba.app.R;
import com.shaba.app.been.BillListEntity;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.List;

public class BillListAdapater extends BaseAdapter {

    Context context;
    private List<BillListEntity> billList;
    private Object date;

    public BillListAdapater(Context context, List<BillListEntity> billList) {
        this.context = context;
        this.billList = billList;
    }

    @Override
    public int getCount() {
        return billList.size();
    }

    @Override
    public Object getItem(int arg0) {
        return billList.get(arg0);
    }

    @Override
    public long getItemId(int arg0) {
        return arg0;
    }

    @Override
    public View getView(int arg0, View arg1, ViewGroup arg2) {
        ViewHodler hodler;
        if (arg1 == null) {
            arg1 = LayoutInflater.from(context).inflate(R.layout.item_bill_list,
                    null);
            hodler = new ViewHodler();
            hodler.tv_BillAmount = (TextView) arg1.findViewById(R.id.tv_BillAmount);
            hodler.tv_BillType = (TextView) arg1.findViewById(R.id.tv_BillType);
            hodler.tv_BillDate = (TextView) arg1.findViewById(R.id.tv_BillDate);
            hodler.tv_BillState = (TextView) arg1.findViewById(R.id.tv_BillState);
            hodler.LL_billContent = (LinearLayout) arg1.findViewById(R.id.LL_billContent);
            arg1.setTag(hodler);
        } else {
            hodler = (ViewHodler) arg1.getTag();
        }
        if (billList.get(arg0).getType_id() == null) {
            hodler.tv_BillType.setText("未知类型");
        } else {
            switch (Integer.valueOf(billList.get(arg0).getType_id())) {

                case 1:
                    hodler.tv_BillType.setText("手机花费");
                    break;
                case 2:
                    hodler.tv_BillType.setText("固话花费");
                    break;
                case 3:
                    hodler.tv_BillType.setText("手机流量");
                    break;
                case 4:
                    hodler.tv_BillType.setText("电费");
                    break;
                case 5:
                    hodler.tv_BillType.setText("水费");
                    break;
                case 6:
                    hodler.tv_BillType.setText("暖气费");
                    break;
                case 7:
                    hodler.tv_BillType.setText("有线电视");
                    break;


                default:
                    break;
            }
        }
        hodler.tv_BillAmount.setText("¥  " + billList.get(arg0).getPayment_amount());

        String time = billList.get(arg0).getPayment_time().substring(4);//1009132424251
        String dateFilte = billList.get(arg0).getPayment_time().substring(0, 6);
        if (dateFilte.substring(4, 5).equals("0")) {
            dateFilte = dateFilte.substring(0, 4) + dateFilte.substring(5);
        }
        try {
            date = new SimpleDateFormat("MMddHHmmss").parse(time);
        } catch (ParseException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        String now = new SimpleDateFormat("MM月dd日 HH:mm").format(date);
        hodler.tv_BillDate.setText(now);
        switch (billList.get(arg0).getStatus()) {
            case 1:
                hodler.tv_BillState.setText("支付成功");
                hodler.tv_BillState.setTextColor(context.getResources().getColor(R.color.backwhite));
                break;
            case 2:
                hodler.tv_BillState.setText("支付失败");
                hodler.tv_BillState.setTextColor(context.getResources().getColor(R.color.red_lignt));
                break;

            default:
                break;
        }

        return arg1;
    }

    class ViewHodler {
        public TextView tv_BillState;
        public TextView tv_BillDate;
        public TextView tv_BillType;
        public TextView tv_BillAmount;
        public LinearLayout LL_billContent;

    }

    public void chang() {

    }

}
