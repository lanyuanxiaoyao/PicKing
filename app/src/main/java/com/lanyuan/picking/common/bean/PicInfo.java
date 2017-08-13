package com.lanyuan.picking.common.bean;

public class PicInfo {

    private String picUrl;
    private String picThumb;
    private String gifThumbUrl;

    public String getGifThumbUrl() {
        return gifThumbUrl;
    }

    public void setGifThumbUrl(String gifThumbUrl) {
        this.gifThumbUrl = gifThumbUrl;
    }

    public PicInfo() {
    }

    public PicInfo(String picUrl) {
        this.picUrl = picUrl;
    }

    public String getPicUrl() {
        return picUrl;
    }

    public void setPicUrl(String picUrl) {
        this.picUrl = picUrl;
    }

    public String getPicThumb() {
        return picThumb;
    }

    public void setPicThumb(String picThumb) {
        this.picThumb = picThumb;
    }
}
