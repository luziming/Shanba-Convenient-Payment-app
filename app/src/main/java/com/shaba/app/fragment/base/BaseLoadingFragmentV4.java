package com.shaba.app.fragment.base;

import android.app.Activity;
import android.app.AlertDialog;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.LinearLayout;

import com.shaba.app.utils.CommonTools;
import com.shaba.app.utils.PrefUtils;


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
public abstract class BaseLoadingFragmentV4 extends Fragment {

    //将加载成功,失败,加载中的View抽取
    protected LoadingPager loadingPager;
    protected Activity mActivity;
    protected LayoutInflater inflater;
    protected String token;
    protected AlertDialog alertDialog;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //获取上下文,方便使用
        mActivity = getActivity();
        inflater = LayoutInflater.from(mActivity);
        getToken();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (loadingPager == null) {
            loadingPager = new LoadingPager(getActivity()) {

                @Override
                protected void loadData() {
                    requestData();
                }

                @Override
                public View createSuccessView() {
                    return initFragment();
                }
            };
        } else {
            //Android5.0之后的FragmentManager已经不会在Fragment的view外边包裹一层，这意味着不用移除也不会报错;
            CommonTools.removeSelfFromParent(loadingPager);
        }
        requestData();

        return loadingPager;
    }


    public void requestData(){

    }
    public abstract View initFragment();

    public void getToken(){
        token = PrefUtils.getString(mActivity, "token", "");
    }

    public Window initDialog(int layoutResID) {
        alertDialog = new AlertDialog.Builder(mActivity).create();
        alertDialog.getWindow().setLayout(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        alertDialog.show();
        Window window = alertDialog.getWindow();
        window.setContentView(layoutResID);
        window.clearFlags(WindowManager.LayoutParams.FLAG_ALT_FOCUSABLE_IM);
        //让内容外区域透明
//        window.setDimAmount(0);
        return window;
    }

}
