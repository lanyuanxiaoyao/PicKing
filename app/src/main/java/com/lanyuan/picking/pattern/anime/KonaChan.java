package com.lanyuan.picking.pattern.anime;

import android.graphics.Color;

import com.lanyuan.picking.common.AlbumInfo;
import com.lanyuan.picking.pattern.BasePattern;
import com.lanyuan.picking.pattern.SinglePicturePattern;
import com.lanyuan.picking.ui.contents.ContentsActivity;
import com.lanyuan.picking.ui.detail.DetailActivity;
import com.lanyuan.picking.ui.menu.Menu;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class KonaChan implements SinglePicturePattern {
    @Override
    public String getCategoryCoverUrl() {
        return "http://konachan.net/images/konachan_net_lg_std.png";
    }

    @Override
    public int getBackgroundColor() {
        return Color.rgb(34, 34, 34);
    }

    @Override
    public String getBaseUrl(List<Menu> menuList, int position) {
        return "https://konachan.net";
    }

    @Override
    public List<Menu> getMenuList() {
        List<Menu> menuList = new ArrayList<>();
        menuList.add(new Menu("Posts", "https://konachan.net/post"));
        menuList.add(new Menu("Long Hair", "https://konachan.net/post?tags=long_hair"));
        menuList.add(new Menu("Breasts", "https://konachan.net/post?tags=breasts"));
        menuList.add(new Menu("Blush", "https://konachan.net/post?tags=blush"));
        menuList.add(new Menu("Short Hair", "https://konachan.net/post?tags=short_hair"));
        menuList.add(new Menu("Original", "https://konachan.net/post?tags=original"));
        menuList.add(new Menu("Dress", "https://konachan.net/post?tags=dress"));
        menuList.add(new Menu("Skirt", "https://konachan.net/post?tags=skirt"));
        menuList.add(new Menu("Underwear", "https://konachan.net/post?tags=underwear"));
        menuList.add(new Menu("Seifuku", "https://konachan.net/post?tags=seifuku"));
        menuList.add(new Menu("Twintails", "https://konachan.net/post?tags=twintails"));
        menuList.add(new Menu("Touhou", "https://konachan.net/post?tags=touhou"));
        return menuList;
    }

    @Override
    public Map<ContentsActivity.parameter, Object> getContent(String baseUrl, String currentUrl, byte[] result, Map<ContentsActivity.parameter, Object> resultMap) throws UnsupportedEncodingException {
        List<AlbumInfo> data = new ArrayList<>();
        Document document = Jsoup.parse(new String(result, "utf-8"));
        Elements elements = document.select("#post-list-posts li div.inner a:has(img)");
        for (Element element : elements) {
            AlbumInfo temp = new AlbumInfo();
            temp.setAlbumUrl(baseUrl + element.attr("href"));
            Elements elements1 = element.select("img");
            if (elements1.size() > 0)
                temp.setCoverUrl("https:" + elements1.get(0).attr("src"));
            data.add(temp);
        }

        resultMap.put(ContentsActivity.parameter.CURRENT_URL, currentUrl);
        resultMap.put(ContentsActivity.parameter.RESULT, data);
        return resultMap;
    }

    @Override
    public String getContentNext(String baseUrl, String currentUrl, byte[] result) throws UnsupportedEncodingException {
        Document document = Jsoup.parse(new String(result, "utf-8"));
        Elements elements = document.select(".pagination a.next_page");
        if (elements.size() > 0) {
            return baseUrl + elements.get(0).attr("href");
        }
        return "";
    }

    @Override
    public String getSinglePicContent(String baseUrl, String currentUrl, byte[] result) throws UnsupportedEncodingException {
        Document document = Jsoup.parse(new String(result, "utf-8"));
        Elements elements = document.select("#image");
        if (elements.size() > 0) {
            return "https:" + elements.get(0).attr("src");
        }
        return "";
    }
}
