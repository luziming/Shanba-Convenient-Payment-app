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
public class SocialStatusResult implements Parcelable {

    private String state;
    private String customer_id;
    private String customer_name;


    public SocialStatusResult() {
    }

    protected SocialStatusResult(Parcel in) {
        state = in.readString();
        customer_id = in.readString();
        customer_name = in.readString();
    }

    public static final Creator<SocialStatusResult> CREATOR = new Creator<SocialStatusResult>() {
        @Override
        public SocialStatusResult createFromParcel(Parcel in) {
            return new SocialStatusResult(in);
        }

        @Override
        public SocialStatusResult[] newArray(int size) {
            return new SocialStatusResult[size];
        }
    };

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getCustomer_id() {
        return customer_id;
    }

    public void setCustomer_id(String customer_id) {
        this.customer_id = customer_id;
    }

    public String getCustomer_name() {
        return customer_name;
    }

    public void setCustomer_name(String customer_name) {
        this.customer_name = customer_name;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(state);
        dest.writeString(customer_id);
        dest.writeString(customer_name);
    }
}
