package com.lanyuan.picking.pattern.custom;

import com.lanyuan.picking.common.ContentsActivity;
import com.lanyuan.picking.common.DetailActivity;
import com.lanyuan.picking.common.Menu;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

public interface BasePattern extends Serializable {
    int getResourceId();

    int getBackgroundColor();

    String getBaseUrl(List<Menu> menuList, int position);

    List<Menu> getMenuList();

    Map<ContentsActivity.parameter, Object> getContent(String baseUrl, String currentUrl);

    String getNext(String baseUrl, String currentUrl);

    Map<DetailActivity.parameter, Object> getDetailContent(String baseUrl, String currentUrl);

    String getDetailNext(String baseUrl, String currentUrl);
}
