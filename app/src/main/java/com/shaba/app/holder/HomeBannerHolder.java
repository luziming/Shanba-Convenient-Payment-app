package com.shaba.app.holder;

import android.view.View;

import com.shaba.app.R;
import com.shaba.app.been.NewsTopPicEntity;

import java.util.ArrayList;
import java.util.List;

import me.wangyuwei.banner.BannerEntity;
import me.wangyuwei.banner.BannerView;

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
public class HomeBannerHolder extends BasicHolder<List<NewsTopPicEntity>> {

    public BannerView home_banner_view;

    @Override
    public void bindData(List<NewsTopPicEntity> list) {

        List<BannerEntity> entityList = new ArrayList<>();
        for (NewsTopPicEntity newsTopPicEntity : list) {
            BannerEntity entity = new BannerEntity();
            entity.imageUrl = newsTopPicEntity.getUrl();
            entityList.add(entity);
        }
        home_banner_view.setEntities(entityList);
    }

    @Override
    public View initHolderView() {
        View view = inflater.inflate(R.layout.holder_home_pic, null);
        home_banner_view = (BannerView) view.findViewById(R.id.home_banner_view);
        return view;
    }
}
