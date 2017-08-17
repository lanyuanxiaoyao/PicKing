package com.lanyuan.picking.common.bean;

import java.util.List;

public class PicInfo {

    private String picUrl;
    private String picThumb;
    private String gifThumbUrl;
    private String title;
    private List<String> tags;
    private String time;

    public String getTime() {
        return time;
    }

    public PicInfo setTime(String time) {
        this.time = time;
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

    public String getGifThumbUrl() {
        return gifThumbUrl;
    }

    public PicInfo setGifThumbUrl(String gifThumbUrl) {
        this.gifThumbUrl = gifThumbUrl;
        return this;
    }

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

    public String getPicThumb() {
        return picThumb;
    }

    public PicInfo setPicThumb(String picThumb) {
        this.picThumb = picThumb;
        return this;
    }
}
