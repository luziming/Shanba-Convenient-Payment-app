package com.shaba.app.fragment.base;/*
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

import android.app.Activity;
import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;

import com.shaba.app.R;
import com.shaba.app.http.AppUtil;
import com.shaba.app.utils.CommonTools;
import com.shaba.app.utils.PrefUtils;


public abstract class BaseFragment extends Fragment {

    protected Activity mActivity;
    protected View mRootView;
    protected LayoutInflater inflater;
    protected String token;
    protected AppUtil appUtil;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //获取上下文,方便使用
        mActivity = getActivity();
        inflater = LayoutInflater.from(mActivity);
        appUtil = new AppUtil();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mRootView = initViews();
        getToken();
        initData();
        return mRootView;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    public void initData(){

    };

    public void getToken(){
        token = PrefUtils.getString(mActivity, "token", "");
    }
    //因为不知道具体实现,所以做成抽象,强制要求子类实现
    public abstract View initViews();


    /**
     * 把指定AutoCompleteTextView中内容保存到sharedPreference中指定的字符段
     */
    protected void saveHistory(String key, String value) {
        String username = PrefUtils.getString(mActivity, "username", "");
        String history = PrefUtils.getString(mActivity, username + "<" + key + ">", "");
        if (!history.contains(value + ",")) {
            StringBuilder sb = new StringBuilder(history);
            sb.insert(0, value + ",");
            PrefUtils.putString(mActivity, username + "<" + key + ">", sb.toString());
        }
    }

    /***
     * 初始化 autocompletetextview
     * @param key
     * @param auto
     */
    protected void initAutoComplete(String key,AutoCompleteTextView auto) {
        String username = PrefUtils.getString(mActivity, "username", "");
        String history = PrefUtils.getString(mActivity, username + "<" + key + ">", "");
        String[] hisArrays = history.split(",");
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(mActivity, R.layout.drop_item,hisArrays);
        //只存5条记录
        if(hisArrays.length > 5){
            String[] newArrays = new String[5];
            System.arraycopy(hisArrays, 0, newArrays, 0, 5);
            arrayAdapter =new ArrayAdapter<String>(mActivity,R.layout.drop_item,newArrays);
        }
        auto.setAdapter(arrayAdapter);
        auto.setDropDownHeight(CommonTools.dip2px(135));
        auto.setThreshold(1);
        auto.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                AutoCompleteTextView view = (AutoCompleteTextView) v;
                if (hasFocus) {
                    view.showDropDown();
                }
            }
        });
    }
}
