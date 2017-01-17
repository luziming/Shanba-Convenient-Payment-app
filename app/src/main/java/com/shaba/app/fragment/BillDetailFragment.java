package com.shaba.app.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.loopj.android.http.AsyncHttpResponseHandler;
import com.shaba.app.R;
import com.shaba.app.fragment.base.BaseLoadingFragment;

import org.apache.http.Header;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;

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
public class BillDetailFragment extends BaseLoadingFragment {


    @Bind(R.id.tv_billDetailAmount)
    TextView tvBillDetailAmount;            //金额
    @Bind(R.id.tv_BillDetailType)
    TextView tvBillDetailType;              //类型
    @Bind(R.id.tv_BillDetailUsrID)
    TextView tvBillDetailUsrID;             //ID
    @Bind(R.id.tv_BillDetailTime)
    TextView tvBillDetailTime;              //时间
    @Bind(R.id.tv_BillDetailTn)
    TextView tvBillDetailTn;                //单号
    @Bind(R.id.tv_BillDetailState)
    TextView tvBillDetailState;             //状态

    @Override
    public View initFragment() {
        View view = inflater.inflate(R.layout.fragment_bill_detail, null);
        ButterKnife.bind(this, view);
        return view;
    }
    @Override
    public void requestData() {
        Bundle arguments = getArguments();
        String url = arguments.getString("url");
        appUtil.getBillDetail(url, token, new BillDetailResponseHandler());
    }

    /**
     * 处理账单详情
     */
    class BillDetailResponseHandler extends AsyncHttpResponseHandler {
        @Override
        public void onSuccess(int arg0, Header[] arg1, byte[] arg2) {
            /**
             {"success":true,
             * 	"data":{
             * "usr_num":"15248141905",
             * "payment_time":"20161009134201",
             * "payment_amount":30.0,
             * "type_id":1,
             * "order_id":"20161009134201981"
             * }}
             */
            try {
                JSONObject obj = new JSONObject(new String(arg2));
                if (!obj.getBoolean("success")) {
                    tvBillDetailAmount .setText("");
                    tvBillDetailType .setText("");
                    tvBillDetailUsrID.setText("");
                    tvBillDetailTime.setText("");
                    tvBillDetailTn.setText("");
                    tvBillDetailState.setText("交易失败");
                    return;
                }
                obj=obj.getJSONObject("data");
                String usr_num=obj.getString("usr_num");
                String payment_time=obj.getString("payment_time");
                payment_time=payment_time.replaceAll("[^0-9]","");
                String order_id=obj.getString("order_id");
                String type_id=obj.getString("type_id");
                String payment_amount=obj.getString("payment_amount");

                tvBillDetailAmount.setText("+"+payment_amount+"0");
                if (type_id=="null") {
                    tvBillDetailType.setText("未知类型");
                }else {
                    switch (Integer.valueOf(type_id)) {
                        case 1:
                            tvBillDetailType.setText("手机话费");
                            break;
                        case 2:
                            tvBillDetailType.setText("固话话费");
                            break;
                        case 3:
                            tvBillDetailType.setText("手机流量");
                            break;
                        case 4:
                            tvBillDetailType.setText("电费");
                            break;
                        case 5:
                            tvBillDetailType.setText("水费");
                            break;
                        case 6:
                            tvBillDetailType.setText("暖气费");
                            break;
                        case 7:
                            tvBillDetailType.setText("有线电视");
                            break;
                    }
                }
                tvBillDetailState.setText("交易成功");
                tvBillDetailUsrID.setText(usr_num);
                Object date = null;
                try {
                    date = new SimpleDateFormat("yyyyMMdd").parse(payment_time.substring(0, 8));
                } catch (ParseException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
                String now = new SimpleDateFormat("yyyy.MM.dd").format(date);
                tvBillDetailTime.setText(now);
                tvBillDetailTn.setText(order_id);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            Zz();
        }
        @Override
        public void onFailure(int arg0, Header[] arg1, byte[] arg2,
                              Throwable arg3) {
            loadingPager.showErrorView();
        }
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // TODO: inflate a fragment view
        View rootView = super.onCreateView(inflater, container, savedInstanceState);
        ButterKnife.bind(this, rootView);
        return rootView;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }
}
