package com.shaba.app.global;

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
public interface ConstantUtil {

    /*****************************************************************
     * mMode参数解释： "00"-启动银联正式环境 "01"-连接银联测试环境
     *****************************************************************/
    String mMode = "01";
    // 地区ID（目前写死）100_10_10:磴口县
    public static final String AREA_ID = "100_10_10";
    // 空字符串
    public static final String NULL_STRING = "";
    // 空格
    public static final String SPACE_STRING = " ";
    // 字符串补零12位
    public static final String ZERO_STR = "000000000000";
    // 手机号码充值页面，默认选择充值金额数组第3项[0,1,2,3......]
    public static final int RECHANGE_AMOUNT_DEFAULT = 0;
}
