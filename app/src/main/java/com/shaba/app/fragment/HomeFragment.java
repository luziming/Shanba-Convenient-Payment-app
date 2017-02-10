package com.shaba.app.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ScrollView;

import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.shaba.app.R;
import com.shaba.app.activity.IndustryPaymentActivity;
import com.shaba.app.activity.SecondActivity;
import com.shaba.app.adapter.MenuRecycleAdapter;
import com.shaba.app.been.NewsListEntity;
import com.shaba.app.been.NewsTopPicEntity;
import com.shaba.app.fragment.base.BaseLoadingFragment;
import com.shaba.app.holder.HomeBannerHolder;
import com.shaba.app.holder.MapHolder;
import com.shaba.app.holder.MenuHolder;
import com.shaba.app.holder.NewsBannerHolder;
import com.shaba.app.holder.NewsHolder;
import com.shaba.app.utils.GsonTools;
import com.shaba.app.utils.ToastUtils;

import org.apache.http.Header;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

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
public class HomeFragment extends BaseLoadingFragment implements MenuRecycleAdapter.OnItemClickListener, MapHolder.OnMapClickListener, NewsHolder.OnNewsClickListener {


    @Bind(R.id.ll_home_container)
    LinearLayout ll_home_container;
    @Bind(R.id.scroolView)
    ScrollView scrollView;

    //当所有数据加载完毕的时候才显示view
    private boolean homeBannerOk = false;
    private boolean newsBannerOk = false;

    private HomeBannerHolder bannerHolder;
    private NewsBannerHolder newsBannerHolder;
    private NewsHolder newsHolder;
    private List<NewsListEntity> newsList;

    @Override
    public View initFragment() {

        View view = inflater.inflate(R.layout.fragment_home, null);
        ButterKnife.bind(this, view);

        /********首页轮播图*********/
        bannerHolder = new HomeBannerHolder();
        ll_home_container.addView(bannerHolder.getHolderView());
        /********菜单栏************/
        MenuHolder menuHolder = new MenuHolder(mActivity);
        ll_home_container.addView(menuHolder.getHolderView());
        //设置菜单的点击事件
        menuHolder.recycleAdapter.setOnItemClickListener(this);
        /********地图栏************/
        MapHolder mapHolder = new MapHolder();
        ll_home_container.addView(mapHolder.getHolderView());
        //设置地图栏的点击事件
        mapHolder.setOnMapClickListener(this);
        /********新闻轮播图********/
        newsBannerHolder = new NewsBannerHolder();
        ll_home_container.addView(newsBannerHolder.getHolderView());
        /********新闻*************/
        newsHolder = new NewsHolder();
        ll_home_container.addView(newsHolder.getHolderView());
        //更多新闻
        newsHolder.setOnMapClickListener(this);

        return view;
    }

    @Override
    public void requestData() {
        Log.e("HomeFragment", "requestData:执行 ");
        loadingPager.showLoadingView();
        //获取首页轮播图
        RequestParams homeParams = new RequestParams();
        homeParams.put("type_id", 1);
        appUtil.getNewsTopPic(homeParams, new HomeBannerResponseHandler(), token);
        //获取新闻轮播图
        RequestParams params = new RequestParams();
        params.put("type_id", 2);
        appUtil.getNewsTopPic(params, new NewBannerResponseHandler(), token);
        //获取新闻
        appUtil.getNewList(new NewListResponseHandler(), token);
    }

    /**
     * 功能菜单的点击事件
     *
     * @param position
     */
    @Override
    public void onClick(int position) {
        String type = "";
        switch (position) {
            case 0:
                type = "electricity";
                break;
            case 1:
                type = "broadband-charge";
                break;
            case 2:
                type = "phone-charge";
                break;
            case 3:
                type = "tel-charge";
                break;
            case 4:
                type = "flow-charge";
                break;
            case 5:
                type = "social-security";
                break;
            case 6:
                ToastUtils.showToast("该功能即将上线!");
                type = null;
                break;
            case 7:
                type = "social-status";
                break;
            case 8:
                ToastUtils.showToast("暖气费");
                break;
            case 9:
                ToastUtils.showToast("公积金查询");
                break;
            case 10:
                ToastUtils.showToast("天然气");
                break;
            case 11:
                ToastUtils.showToast("快递查询");
                break;
            case 12:
                ToastUtils.showToast("火车票");
                break;
            case 13:
                ToastUtils.showToast("违章查询");
                break;
            case 14:
                ToastUtils.showToast("加油卡充值");
                break;
            case 15:
//                type = "industry-payment";
                Intent intent = new Intent(mActivity, IndustryPaymentActivity.class);
                lanuchActivity(intent);
                break;
        }
        if (!TextUtils.isEmpty(type)){
            Intent intent = new Intent(mActivity, SecondActivity.class);
            intent.putExtra("type", type);
            lanuchActivity(intent);
        }

    }

    /**
     * 地图的点击事件
     * @param position
     */
    @Override
    public void onMapListClick(int position) {
        Intent intent = new Intent(mActivity, SecondActivity.class);
        switch (position) {
            case 0:
                intent.putExtra("type", "bank-map");
                break;
            case 1:
                intent.putExtra("type", "atm-map");
                break;
            case 2:
                intent.putExtra("type", "farmers-point-map");
                break;
        }
        lanuchActivity(intent);
    }

    private void lanuchActivity(Intent intent) {
        startActivity(intent);
        mActivity.overridePendingTransition(R.anim.activity_open, R.anim.activity_close);
    }

    /**
     * 更多新闻
     */
    @Override
    public void onMoreNewsClick() {
        Intent intent = new Intent(mActivity, SecondActivity.class);
        intent.putExtra("type", "more-news");
        lanuchActivity(intent);
    }

    /**
     * 新闻列表
     */
    @Override
    public void onNewsItemClick(AdapterView<?> parent, View view, int position, long id) {
        String url = newsList.get(position).getUrl();
        String title = newsList.get(position).getTitle();
        Intent intent = new Intent(mActivity, SecondActivity.class);
        intent.putExtra("type", "news-detail")
                .putExtra("url", url)
                .putExtra("title", title);
        lanuchActivity(intent);
    }

    /**
     * 获取轮播图
     */
    private class HomeBannerResponseHandler extends AsyncHttpResponseHandler {

        @Override
        public void onSuccess(int i, Header[] headers, byte[] bytes) {
            JSONObject response = null;
            try {
                response = new JSONObject(new String(bytes));
                JSONArray ary = response.getJSONArray("data");
                List<NewsTopPicEntity> topPicList = GsonTools.getProdjects(ary.toString(), NewsTopPicEntity[].class);
                if (topPicList != null) {
                    bannerHolder.bindData(topPicList);
                    homeBannerOk = true;
                }
                if (homeBannerOk && newsBannerOk) {
                    Zz();
                    //scrollView滑动到顶部
                    scrollView.fullScroll(ScrollView.FOCUS_UP);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        @Override
        public void onFailure(int i, Header[] headers, byte[] bytes, Throwable throwable) {
            loadingPager.showErrorView();
        }
    }

    /**
     * 新闻轮播图
     */
    private class NewBannerResponseHandler extends AsyncHttpResponseHandler {

        @Override
        public void onSuccess(int i, Header[] headers, byte[] bytes) {
            JSONObject response = null;
            try {
                response = new JSONObject(new String(bytes));
                JSONArray ary = response.getJSONArray("data");
                List<NewsTopPicEntity> topPicList = GsonTools.getProdjects(ary.toString(), NewsTopPicEntity[].class);
                if (topPicList != null) {
                    newsBannerHolder.bindData(topPicList);
                    newsBannerOk = true;
                }
                if (homeBannerOk && newsBannerOk) {
                    Zz();
                    //scrollView滑动到顶部
                    if (scrollView != null)
                    scrollView.fullScroll(ScrollView.FOCUS_UP);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        @Override
        public void onFailure(int i, Header[] headers, byte[] bytes, Throwable throwable) {
            loadingPager.showErrorView();
        }
    }

    /**
     * 处理新闻信息:
     */
    class NewListResponseHandler extends AsyncHttpResponseHandler {

        @Override
        public void onSuccess(int arg0, Header[] arg1, byte[] arg2) {
            JSONObject response = null;
            try {
                response = new JSONObject(new String(arg2));
                JSONArray ary = response.getJSONArray("data");
                if (ary.length() > 4 && ary != null) {// 如果数据不为空
                    newsList = GsonTools.getProdjects(ary.toString(), NewsListEntity[].class);
                    ArrayList<NewsListEntity> newList2 = new ArrayList<NewsListEntity>();
                    for (int i = 0; i < 4; i++) {
                        newList2.add(newsList.get(i));
                    }
                    newsHolder.bindData(newList2);
                } else if (ary.length() <= 4 && ary != null) {
                    newsList = GsonTools.getProdjects(ary.toString(), NewsListEntity[].class);
                    newsHolder.bindData(newsList);
                }

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        @Override
        public void onFailure(int arg0, Header[] arg1, byte[] arg2, Throwable arg3) {
            loadingPager.showErrorView();
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        //开启自动轮播
        bannerHolder.home_banner_view.startAutoScroll();
        newsBannerHolder.newsBannerView.startAutoScroll();
    }

    @Override
    public void onPause() {
        super.onPause();
        //关闭自动轮播
        bannerHolder.home_banner_view.stopAutoScroll();
        newsBannerHolder.newsBannerView.stopAutoScroll();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // TODO: inflate a fragment view
        View rootView = super.onCreateView(inflater, container, savedInstanceState);
        ButterKnife.bind(this, rootView);
        return rootView;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        Log.e("HomeFragment", "onDestroyView:执行 ");
        ButterKnife.unbind(this);
    }
}

