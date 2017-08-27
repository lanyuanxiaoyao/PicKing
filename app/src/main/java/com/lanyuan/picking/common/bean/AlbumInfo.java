package com.lanyuan.picking.common.bean;

import java.util.List;

public class AlbumInfo extends BaseInfo {

    private String albumUrl;


    public String getPicUrl() {
        return picUrl;
    }

    public AlbumInfo setPicUrl(String picUrl) {
        this.picUrl = picUrl;
        return this;

    }

    public String getGifThumbUrl() {
        return gifThumbUrl;
    }

    public AlbumInfo setGifThumbUrl(String gifThumbUrl) {
        this.gifThumbUrl = gifThumbUrl;
        return this;
    }

    public String getTitle() {
        return title;
    }

    public AlbumInfo setTitle(String title) {
        this.title = title;
        return this;
    }

    public List<String> getTags() {
        return tags;
    }

    public AlbumInfo setTags(List<String> tags) {
        this.tags = tags;
        return this;
    }

    public String getTime() {
        return time;
    }

    public AlbumInfo setTime(String time) {
        this.time = time;
        return this;
    }

    public String getAlbumUrl() {
        return albumUrl;
    }

    public AlbumInfo setAlbumUrl(String albumUrl) {
        this.albumUrl = albumUrl;
        return this;
    }
}
