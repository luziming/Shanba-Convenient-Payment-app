package com.shaba.app.fragment.base;

import android.app.Fragment;
import android.os.Bundle;
import android.os.SystemClock;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.shaba.app.http.AppUtil;
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
public abstract class BaseLoadingFragment extends Fragment {

    //将加载成功,失败,加载中的View抽取
    protected LoadingPager loadingPager;
    protected AppCompatActivity mActivity;
    protected LayoutInflater inflater;
    protected String token;
    protected AppUtil appUtil;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //获取上下文,方便使用
        mActivity = (AppCompatActivity) getActivity();
        inflater = LayoutInflater.from(mActivity);
        appUtil = new AppUtil();
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

    protected void Zz() {
        new Thread() {
            @Override
            public void run() {
                super.run();
                SystemClock.sleep(500);
                CommonTools.runOnUIThread(new Runnable() {
                    @Override
                    public void run() {
                        loadingPager.showSuccessView();
                    }
                });
            }
        }.start();
    }
}
