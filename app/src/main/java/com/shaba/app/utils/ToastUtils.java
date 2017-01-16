package com.shaba.app.utils;

import android.widget.Toast;

import com.shaba.app.global.MyApplication;


/**
 * Created by Wubj on 2016/8/28.
 */
public class ToastUtils {

    private static Toast sToast;

    /**
     * 当屏幕上存在一个吐司时,会直接更改吐内容而不是重建一个吐司
     */
    public static void showToast(String text) {
        if (sToast == null) {
            sToast = Toast.makeText(MyApplication.context, text, Toast.LENGTH_SHORT);
        }
        sToast.setText(text);
        sToast.show();
    }
}