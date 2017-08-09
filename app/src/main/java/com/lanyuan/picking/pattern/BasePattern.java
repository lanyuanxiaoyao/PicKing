package com.lanyuan.picking.pattern;

import com.lanyuan.picking.ui.contents.ContentsActivity;
import com.lanyuan.picking.ui.detail.DetailActivity;
import com.lanyuan.picking.ui.menu.Menu;

import java.io.Serializable;
import java.io.UnsupportedEncodingException;
import java.util.List;
import java.util.Map;

public interface BasePattern extends Serializable {
    String getCategoryCoverUrl();

    int getBackgroundColor();

    String getBaseUrl(List<Menu> menuList, int position);

    List<Menu> getMenuList();

    boolean isSinglePic();

    Map<ContentsActivity.parameter, Object> getContent(String baseUrl, String currentUrl, byte[] result, Map<ContentsActivity.parameter, Object> resultMap) throws UnsupportedEncodingException;

    String getContentNext(String baseUrl, String currentUrl, byte[] result) throws UnsupportedEncodingException;

    String getSinglePicContent(String baseUrl, String currentUrl, byte[] result) throws UnsupportedEncodingException;

    Map<DetailActivity.parameter, Object> getDetailContent(String baseUrl, String currentUrl, byte[] result, Map<DetailActivity.parameter, Object> resultMap) throws UnsupportedEncodingException;

    String getDetailNext(String baseUrl, String currentUrl, byte[] result) throws UnsupportedEncodingException;
}
