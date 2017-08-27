package com.lanyuan.picking.common.bean;

import java.io.Serializable;
import java.util.List;

public class BaseInfo implements Serializable{

    protected String picUrl;
    protected String gifThumbUrl;
    protected String title;
    protected List<String> tags;
    protected String time;
    protected Integer width;
    protected Integer height;

    public String getPicUrl() {
        return picUrl;
    }

    public String getGifThumbUrl() {
        return gifThumbUrl;
    }

    public String getTitle() {
        return title;
    }

    public List<String> getTags() {
        return tags;
    }

    public String getTime() {
        return time;
    }

    public Integer getWidth() {
        return width;
    }

    public void setWidth(Integer width) {
        this.width = width;
    }

    public Integer getHeight() {
        return height;
    }

    public void setHeight(Integer height) {
        this.height = height;
    }
}
