package com.lanyuan.picking.common.bean;

import java.io.Serializable;
import java.util.List;

public class AlbumInfo implements Serializable {

    private String coverUrl;
    private String albumUrl;
    private String gifThumbUrl;
    private String title;
    private String time;
    private List<String> tags;

    public String getTitle() {
        return title;
    }

    public AlbumInfo setTitle(String title) {
        this.title = title;
        return this;
    }

    public String getTime() {
        return time;
    }

    public AlbumInfo setTime(String time) {
        this.time = time;
        return this;
    }

    public List<String> getTags() {
        return tags;
    }

    public AlbumInfo setTags(List<String> tags) {
        this.tags = tags;
        return this;
    }

    public String getGifThumbUrl() {
        return gifThumbUrl;
    }

    public AlbumInfo setGifThumbUrl(String gifThumbUrl) {
        this.gifThumbUrl = gifThumbUrl;
        return this;
    }

    public String getCoverUrl() {
        return coverUrl;
    }

    public AlbumInfo setCoverUrl(String coverUrl) {
        this.coverUrl = coverUrl;
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
