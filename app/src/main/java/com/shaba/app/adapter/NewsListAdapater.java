package com.shaba.app.adapter;

import com.shaba.app.been.NewsListEntity;
import com.shaba.app.holder.BasicHolder;
import com.shaba.app.holder.NewsListHolder;

import java.util.List;

public class NewsListAdapater extends BasicAdapter<NewsListEntity> {


    public NewsListAdapater(List<NewsListEntity> list) {
        super(list);
    }

    @Override
    public BasicHolder<NewsListEntity> createViewHolder(int position) {
        return new NewsListHolder();
    }
}
