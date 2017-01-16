package com.shaba.app.been;

import java.io.Serializable;

public class NewsListEntity implements Serializable {

    private String id;
    private String title;
    private String created;
    private String url;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getCreated() {
        return created;
    }

    public void setCreated(String created) {
        this.created = created;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    @Override
    public String toString() {
        return "NewsListEntity [id=" + id + ", title=" + title + ", created="
                + created + ", url=" + url + "]";
    }


}
