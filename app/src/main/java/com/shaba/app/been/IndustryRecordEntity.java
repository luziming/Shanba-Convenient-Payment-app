package com.shaba.app.been;

import java.text.ParseException;
import java.text.SimpleDateFormat;

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
public class IndustryRecordEntity {


    /**
     * amount : 4401
     * product_id : 4
     * org_id : 1
     * merchant_id : 2
     * updated_at : 2017-02-04T16:38:46.000Z
     * status : 2
     * id : 4
     * base_num : null
     * user_id : 13e59949-3cfb-4974-99b9-c74f68d6fdeb
     * user_name : 孙菲菲
     * descp : 2017 下半年物业费
     * user_code : 3123
     * created_at : 2017-02-04T16:38:46.000Z
     */

    private int amount;
    private int product_id;
    private int org_id;
    private int merchant_id;
    private String updated_at;
    private int status;
    private int id;
    private Object base_num;
    private String user_id;
    private String user_name;
    private String descp;
    private String user_code;
    private String created_at;

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    public int getProduct_id() {
        return product_id;
    }

    public void setProduct_id(int product_id) {
        this.product_id = product_id;
    }

    public int getOrg_id() {
        return org_id;
    }

    public void setOrg_id(int org_id) {
        this.org_id = org_id;
    }

    public int getMerchant_id() {
        return merchant_id;
    }

    public void setMerchant_id(int merchant_id) {
        this.merchant_id = merchant_id;
    }

    public String getUpdated_at() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy年MM月dd日 HH:mm:ss");
        String format = null;
        try {
            return updated_at == null ? "年代遥远,数据缺失" : formatter.format(sdf.parse(updated_at.replace("T", " ").substring(0, updated_at.length() - 5)));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return null;
    }

    public void setUpdated_at(String updated_at) {
        this.updated_at = updated_at;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Object getBase_num() {
        return base_num;
    }

    public void setBase_num(Object base_num) {
        this.base_num = base_num;
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public String getUser_name() {
        return user_name;
    }

    public void setUser_name(String user_name) {
        this.user_name = user_name;
    }

    public String getDescp() {
        return descp;
    }

    public void setDescp(String descp) {
        this.descp = descp;
    }

    public String getUser_code() {
        return user_code;
    }

    public void setUser_code(String user_code) {
        this.user_code = user_code;
    }

    public String getCreated_at() {
        return created_at;
    }

    public void setCreated_at(String created_at) {
        this.created_at = created_at;
    }
}
