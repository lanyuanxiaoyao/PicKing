package com.lanyuan.picking.common.bean;

import java.util.List;

public class PicInfo extends BaseInfo {

    private String picThumb;

    public PicInfo() {
    }

    public PicInfo(String picUrl) {
        this.picUrl = picUrl;
    }

    public String getPicUrl() {
        return picUrl;
    }

    public PicInfo setPicUrl(String picUrl) {
        this.picUrl = picUrl;
        return this;

    }

    public String getGifThumbUrl() {
        return gifThumbUrl;
    }

    public PicInfo setGifThumbUrl(String gifThumbUrl) {
        this.gifThumbUrl = gifThumbUrl;
        return this;
    }

    public String getTitle() {
        return title;
    }

    public PicInfo setTitle(String title) {
        this.title = title;
        return this;
    }

    public List<String> getTags() {
        return tags;
    }

    public PicInfo setTags(List<String> tags) {
        this.tags = tags;
        return this;
    }

    public String getTime() {
        return time;
    }

    public PicInfo setTime(String time) {
        this.time = time;
        return this;
    }

    public String getPicThumb() {
        return picThumb;
    }

    public void setPicThumb(String picThumb) {
        this.picThumb = picThumb;
    }
}
