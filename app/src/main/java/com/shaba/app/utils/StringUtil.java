package com.shaba.app.utils;

import com.shaba.app.global.ConstantUtil;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class StringUtil {

    private static long lastClickTime = 0L;

    public static boolean isEmpty(String str) {
        if (str == null || "".equals(str.trim())) {
            return true;
        }
        return false;
    }

    public static boolean isNotEmpty(String str) {
        if (str == null || "".equals(str.trim())) {
            return false;
        }
        return true;
    }

    /**
     * 判断重复点击
     * @return
     */
    public static boolean isFastClick() {
        long time = System.currentTimeMillis();
        if (time - lastClickTime < 1000) {
            return true;
        }
        lastClickTime = time;
        return false;
    }

    /**
     * 字符串左补零12位
     *
     * @param amount
     * @return
     */
    public static String fomartAmountTo12(String amount) {
        if (isEmpty(amount)) {
            return ConstantUtil.ZERO_STR;
        }
        int at = (int) (Double.parseDouble(amount) * 100);
        String amountStr = String.valueOf(at);
        String returnStr = ConstantUtil.ZERO_STR.substring(0,
                ConstantUtil.ZERO_STR.length() - amountStr.length())
                + amountStr;
        return returnStr;
    }

    /**
     * 字符串去空格
     *
     * @param str
     * @return
     */
    public static String removeNull(String str) {
        if (isEmpty(str)) {
            return str;
        } else {
            String returnStr = str.replaceAll(" ", "");
            return returnStr;
        }
    }

    /**
     * 手机号码格式化
     * @param phoneNum
     * @return
     */
    public static String fomartPhoneNum(String phoneNum) {
        String phoneStr = phoneNum.replace(ConstantUtil.SPACE_STRING, "");
        String returnStr = "";
        if (phoneStr.length() == 11) {
            returnStr = phoneStr.substring(0, 3) + ConstantUtil.SPACE_STRING
                    + phoneStr.substring(3, 7) + ConstantUtil.SPACE_STRING
                    + phoneStr.substring(7);
            return returnStr;
        } else {
            return phoneNum;
        }
    }

    public static String md5(String string) {
        byte[] hash;
        try {
            hash = MessageDigest.getInstance("MD5").digest(string.getBytes("UTF-8"));
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("Huh, MD5 should be supported?", e);
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException("Huh, UTF-8 should be supported?", e);
        }

        StringBuilder hex = new StringBuilder(hash.length * 2);
        for (byte b : hash) {
            if ((b & 0xFF) < 0x10) hex.append("0");
            hex.append(Integer.toHexString(b & 0xFF));
        }
        return hex.toString();
    }
}
