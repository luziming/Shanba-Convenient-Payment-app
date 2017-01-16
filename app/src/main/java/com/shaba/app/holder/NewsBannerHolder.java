package com.shaba.app.holder;

import android.view.View;

import com.shaba.app.R;
import com.shaba.app.been.NewsTopPicEntity;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
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
public class NewsBannerHolder extends BasicHolder<List<NewsTopPicEntity>> {

    @Bind(R.id.news_banner_view)
    public BannerView newsBannerView;

    @Override
    public void bindData(List<NewsTopPicEntity> list) {
        List<BannerEntity> entityList = new ArrayList<>();
        for (NewsTopPicEntity newsTopPicEntity : list) {
            BannerEntity entity = new BannerEntity();
            entity.imageUrl = newsTopPicEntity.getUrl();
            entityList.add(entity);
        }
        newsBannerView.setEntities(entityList);
    }

    @Override
    public View initHolderView() {
        View view = inflater.inflate(R.layout.holder_news_pic, null);
        ButterKnife.bind(this, view);
        return view;
    }
}
