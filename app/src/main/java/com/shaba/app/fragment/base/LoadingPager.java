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

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;

import com.shaba.app.R;

import me.wangyuwei.loadingview.LoadingView;


public abstract class LoadingPager extends FrameLayout {

    private View loadingView;
    private View errorView;
    private View successView;
    private LoadingView mLoadingView;

    private enum Status {
        Loading, Error, Success
    }

    private Status mStatu = Status.Loading;
    //默认状态

    public LoadingPager(Context context) {
        super(context);
        initView();
    }

    public LoadingPager(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView();
    }

    public LoadingPager(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView();
    }


    private void initView() {
        //为了避免根节点的属性被忽略掉,添加布局参数
        LayoutParams params = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
        //添加三钟状态的View
        //加载中
        if (loadingView == null) {
            loadingView = View.inflate(getContext(), R.layout.page_loading, null);
            mLoadingView = (LoadingView)loadingView.findViewById(R.id.loading_view);
            mLoadingView.start();
        }
        addView(loadingView, params);
        //加载失败
        if (errorView == null) {
            errorView = View.inflate(getContext(), R.layout.page_error, null);
            Button bt = (Button) errorView.findViewById(R.id.btn_reload);
            bt.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    //重新加载数据
                    mStatu = Status.Loading;
                    showView();
                    // loadDataAndRefreashPage();
                    loadData();
                }
            });
        }
        addView(errorView, params);

        //加载成功的View
        if (successView == null) {
            successView = createSuccessView();
        }
        if (successView == null) {
            throw new IllegalArgumentException("菜逼,滚吧");
        } else {
            addView(successView, params);
        }

        //根据当前状态来显示view
        showView();

        //加载数据
        //loadDataAndRefreashPage();
//        loadData();

    }

   /* public void loadDataAndRefreashPage() {
        new Thread(){//开启线程,请求网络数据
            @Override
            public void run() {
                SystemClock.sleep(1500);

                Object obj = loadData();

                mStatu = obj == null ? Status.Error : Status.Success;
                //刷新view
                CommonTools.runOnUIThread(new Runnable() {
                    @Override
                    public void run() {
                        //在主线程更新UI
                        showView();
                    }
                });
            }
        }.start();
    }*/


    //请求数据,我们并不关心数据,只要不为空就改变加载状态即可
    protected abstract void loadData();

    private void showView() {
        //现将所有的View隐藏,设为Invisible比gone效率更高一点,不需要重新layout
        loadingView.setVisibility(View.INVISIBLE);
        mLoadingView.stop();
        errorView.setVisibility(View.INVISIBLE);
        successView.setVisibility(View.INVISIBLE);
        switch (mStatu) {
            case Loading:
                loadingView.setVisibility(View.VISIBLE);
                mLoadingView.start();
                break;
            case Error:
                errorView.setVisibility(View.VISIBLE);
                break;
            case Success:
                successView.setVisibility(View.VISIBLE);
                break;
        }
    }

    /**
     * 加载成功我们并不关心,所以交给子类实现
     */
    public abstract View createSuccessView();

    /**
     * 加载失败
     */
    public void showErrorView() {
        mStatu = Status.Error;
        showView();
    }

    /**
     * 数据加载成功,手动调用,避免线程间通讯的麻烦
     */
    public void showSuccessView() {
        if (mStatu != Status.Success) {
            mStatu = Status.Success;
            showView();
        }
    }

    /**
     * 加载中
     */
    public void showLoadingView() {
        if (mStatu != Status.Loading) {
            mStatu = Status.Loading;
            showView();
        }
    }
}
