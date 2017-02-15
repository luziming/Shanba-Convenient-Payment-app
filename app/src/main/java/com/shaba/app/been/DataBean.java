package com.shaba.app.been;

import android.os.Parcel;
import android.os.Parcelable;

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
public class DataBean implements Parcelable{

    /**
     * amount : 14401
     * product_id : 2
     * name : xx中学学费
     * org_id : 1
     * merchant_id : 1
     * mer_name : XX学校
     * updated_at : 2017-02-04T16:38:46.000Z
     * status : 1
     * id : 1
     * base_num : null
     * user_id : 55
     * user_name : 孙丹
     * descp : 2017 上学期
     * user_code : 123123
     * created_at : 2017-02-04T16:38:46.000Z
     */

    private String amount;
    private int product_id;
    private String name;
    private int org_id;
    private int merchant_id;
    private String mer_name;
    private String updated_at;
    private int status;
    private int id;
    private Object base_num;
    private String user_id;
    private String user_name;
    private String descp;
    private String user_code;
    private String created_at;

    protected DataBean(Parcel in) {
        amount = in.readString();
        product_id = in.readInt();
        name = in.readString();
        org_id = in.readInt();
        merchant_id = in.readInt();
        mer_name = in.readString();
        updated_at = in.readString();
        status = in.readInt();
        id = in.readInt();
        user_id = in.readString();
        user_name = in.readString();
        descp = in.readString();
        user_code = in.readString();
        created_at = in.readString();
    }

    public static final Creator<DataBean> CREATOR = new Creator<DataBean>() {
        @Override
        public DataBean createFromParcel(Parcel in) {
            return new DataBean(in);
        }

        @Override
        public DataBean[] newArray(int size) {
            return new DataBean[size];
        }
    };

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public int getProduct_id() {
        return product_id;
    }

    public void setProduct_id(int product_id) {
        this.product_id = product_id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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

    public String getMer_name() {
        return mer_name;
    }

    public void setMer_name(String mer_name) {
        this.mer_name = mer_name;
    }

    public String getUpdated_at() {
        return updated_at;
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

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(amount);
        dest.writeInt(product_id);
        dest.writeString(name);
        dest.writeInt(org_id);
        dest.writeInt(merchant_id);
        dest.writeString(mer_name);
        dest.writeString(updated_at);
        dest.writeInt(status);
        dest.writeInt(id);
        dest.writeString(user_id);
        dest.writeString(user_name);
        dest.writeString(descp);
        dest.writeString(user_code);
        dest.writeString(created_at);
    }
}
