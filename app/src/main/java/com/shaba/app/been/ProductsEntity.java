package com.shaba.app.been;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.List;

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
public class ProductsEntity implements Parcelable{


    /**
     * per_page : 10
     * total : 1
     * data : [{"id":1,"mer_name":"XX学校","name":"xx中学学费","amount":2500,"descp":"17年上学期","extra":null,"status":1,"created_at":"2017-02-04T16:39:16.000Z"},{"id":2,"mer_name":"XX学校","name":"xx中学学费","amount":2500,"descp":"17年下学期","extra":null,"status":1,"created_at":"2017-02-04T16:39:16.000Z"}]
     */

    private int per_page;
    private int current_page;


    public int getCurrent_page() {
        return current_page;
    }

    public void setCurrent_page(int current_page) {
        this.current_page = current_page;
    }

    private int total;
    /**
     * id : 1
     * mer_name : XX学校
     * name : xx中学学费
     * amount : 2500
     * descp : 17年上学期
     * extra : null
     * status : 1
     * created_at : 2017-02-04T16:39:16.000Z
     */

    private List<DataBean> data;

    protected ProductsEntity(Parcel in) {
        per_page = in.readInt();
        total = in.readInt();
    }

    public static final Creator<ProductsEntity> CREATOR = new Creator<ProductsEntity>() {
        @Override
        public ProductsEntity createFromParcel(Parcel in) {
            return new ProductsEntity(in);
        }

        @Override
        public ProductsEntity[] newArray(int size) {
            return new ProductsEntity[size];
        }
    };

    public int getPer_page() {
        return per_page;
    }

    public void setPer_page(int per_page) {
        this.per_page = per_page;
    }

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }

    public List<DataBean> getData() {
        return data;
    }

    public void setData(List<DataBean> data) {
        this.data = data;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(per_page);
        dest.writeInt(total);
    }

    public class DataBean{

        private String name;

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }
    }

}
