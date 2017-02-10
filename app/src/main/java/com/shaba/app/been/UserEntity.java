package com.shaba.app.been;

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
public class UserEntity {


    /**
     * current_page : 0
     * total : 1
     * per_page : 10
     * data : [{"amount":4401,"product_id":4,"org_id":1,"merchant_id":2,"updated_at":"2017-02-04T16:38:46.000Z","status":1,"id":4,"base_num":null,"user_id":"12","user_name":"孙菲菲","descp":"2017 下半年物业费","user_code":"3123","created_at":"2017-02-04T16:38:46.000Z"}]
     */

    private int current_page;
    private int total;
    private int per_page;
    /**
     * amount : 4401
     * product_id : 4
     * org_id : 1
     * merchant_id : 2
     * updated_at : 2017-02-04T16:38:46.000Z
     * status : 1
     * id : 4
     * base_num : null
     * user_id : 12
     * user_name : 孙菲菲
     * descp : 2017 下半年物业费
     * user_code : 3123
     * created_at : 2017-02-04T16:38:46.000Z
     */

    private List<DataBean> data;

    public int getCurrent_page() {
        return current_page;
    }

    public void setCurrent_page(int current_page) {
        this.current_page = current_page;
    }

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }

    public int getPer_page() {
        return per_page;
    }

    public void setPer_page(int per_page) {
        this.per_page = per_page;
    }

    public List<DataBean> getData() {
        return data;
    }

    public void setData(List<DataBean> data) {
        this.data = data;
    }

}
