package com.shaba.app.utils;

import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;

import com.shaba.app.R;
import com.shaba.app.global.ConstantUtil;
import com.shaba.app.view.CustomProgressDialog;
import com.unionpay.UPPayAssistEx;

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
public class UnionPayUtils {

    private UnionPayUtils() {

    }

    private static UnionPayUtils c = new UnionPayUtils();

    private static CustomProgressDialog mLoadingDialog;

    public static UnionPayUtils getInstance() {
        return c;
    }

    public void showDialog(Context context) {
        mLoadingDialog = CustomProgressDialog.createDialog(context);
        mLoadingDialog.setMessage("请稍后...");
        mLoadingDialog.setCancelable(false);
        mLoadingDialog.show();
    }

    public void disMissDialog() {
        if (mLoadingDialog != null && mLoadingDialog.isShowing())
            mLoadingDialog.dismiss();
        mLoadingDialog = null;
    }

    /*************************************************
     * 步骤2：通过银联工具类启动支付插件
     ************************************************/
    public void unionPay(Context context, String tn) {
        UPPayAssistEx.startPay(context, null, null, tn, ConstantUtil.mMode);
    }

    public void checkTN(Context context) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("错误提示");
        builder.setMessage(R.string.error_wifi_disconnect);
        builder.setNegativeButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        builder.create().show();
    }
}
