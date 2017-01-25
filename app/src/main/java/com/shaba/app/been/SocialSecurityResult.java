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
public class SocialSecurityResult implements Parcelable{

    private String state;
    private String total_unit_pay;
    private String pay_month_num;
    private String curr_balance;
    private String customer_id;

    public SocialSecurityResult() {
    }

    public SocialSecurityResult(Parcel in) {
        state = in.readString();
        total_unit_pay = in.readString();
        pay_month_num = in.readString();
        curr_balance = in.readString();
        customer_id = in.readString();
        total_person_pay = in.readString();
    }

    public static final Creator<SocialSecurityResult> CREATOR = new Creator<SocialSecurityResult>() {
        @Override
        public SocialSecurityResult createFromParcel(Parcel in) {
            return new SocialSecurityResult(in);
        }

        @Override
        public SocialSecurityResult[] newArray(int size) {
            return new SocialSecurityResult[size];
        }
    };

    public String getTotal_person_pay() {
        return total_person_pay;
    }

    public void setTotal_person_pay(String total_person_pay) {
        this.total_person_pay = total_person_pay;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getTotal_unit_pay() {
        return total_unit_pay;
    }

    public void setTotal_unit_pay(String total_unit_pay) {
        this.total_unit_pay = total_unit_pay;
    }

    public String getPay_month_num() {
        return pay_month_num;
    }

    public void setPay_month_num(String pay_month_num) {
        this.pay_month_num = pay_month_num;
    }

    public String getCurr_balance() {
        return curr_balance;
    }

    public void setCurr_balance(String curr_balance) {
        this.curr_balance = curr_balance;
    }

    public String getCustomer_id() {
        return customer_id;
    }

    public void setCustomer_id(String customer_id) {
        this.customer_id = customer_id;
    }

    private String total_person_pay;

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(state);
        dest.writeString(total_unit_pay);
        dest.writeString(pay_month_num);
        dest.writeString(curr_balance);
        dest.writeString(customer_id);
        dest.writeString(total_person_pay);
    }
}
