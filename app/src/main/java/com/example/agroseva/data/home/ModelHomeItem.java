package com.example.agroseva.data.home;

import java.io.Serializable;

public class ModelHomeItem implements Serializable {

    String desc;
    String image_url;
    String title;
    String url;

    @Override
    public String toString() {
        return "ModelHomeItem{" +
                "desc='" + desc + '\'' +
                ", image_url='" + image_url + '\'' +
                ", title='" + title + '\'' +
                ", url='" + url + '\'' +
                '}';
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public String getImage_url() {
        return image_url;
    }

    public void setImage_url(String image_url) {
        this.image_url = image_url;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public ModelHomeItem() {
    }
}
