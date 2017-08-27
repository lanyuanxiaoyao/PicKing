package com.lanyuan.picking.pattern.anime;

import android.graphics.Color;

import com.lanyuan.picking.common.bean.AlbumInfo;
import com.lanyuan.picking.common.bean.PicInfo;
import com.lanyuan.picking.pattern.SinglePicturePattern;
import com.lanyuan.picking.ui.contents.ContentsActivity;
import com.lanyuan.picking.ui.menu.Menu;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class AoJiao implements SinglePicturePattern {

    @Override
    public String getWebsiteName() {
        return "傲娇零次元";
    }

    @Override
    public String getCategoryCoverUrl() {
        return "https://www.aojiao.org/wp-content/uploads/2016/10/439c8112e53379d73724e80c93eccd0c.png";
    }

    @Override
    public int getBackgroundColor() {
        return Color.rgb(255, 125, 127);
    }

    @Override
    public String getBaseUrl(List<Menu> menuList, int position) {
        return "https://www.aojiao.org/";
    }

    @Override
    public List<Menu> getMenuList() {
        List<Menu> menuList = new ArrayList<>();
        menuList.add(new Menu("ACG图包", "https://www.aojiao.org/category/pic"));
        return menuList;
    }

    @Override
    public Map<ContentsActivity.parameter, Object> getContent(String baseUrl, String currentUrl, byte[] result, Map<ContentsActivity.parameter, Object> resultMap) throws UnsupportedEncodingException {
        List<AlbumInfo> data = new ArrayList<>();
        Document document = Jsoup.parse(new String(result, "utf-8"));
        Elements elements = document.select("section");
        for (Element element : elements) {
            AlbumInfo temp = new AlbumInfo();

            Elements album = element.select("a:has(img)");
            if (album.size() > 0) {
                temp.setAlbumUrl(album.attr("href"));
                Elements pic = album.select("img");
                if (pic.size() > 0)
                    temp.setPicUrl(pic.get(0).attr("data-src"));
            }

            data.add(temp);
        }

        resultMap.put(ContentsActivity.parameter.CURRENT_URL, currentUrl);
        resultMap.put(ContentsActivity.parameter.RESULT, data);
        return resultMap;
    }

    @Override
    public String getContentNext(String baseUrl, String currentUrl, byte[] result) throws UnsupportedEncodingException {
        Document document = Jsoup.parse(new String(result, "utf-8"));
        Elements elements = document.select("div.pager a.next");
        if (elements.size() > 0)
            return elements.get(0).attr("href");
        return "";
    }

    @Override
    public PicInfo getSinglePicContent(String baseUrl, String currentUrl, byte[] result) throws UnsupportedEncodingException {
        Document document = Jsoup.parse(new String(result, "utf-8"));
        PicInfo info = new PicInfo();
        Elements title = document.select("h1.entry-title");
        if (title.size() > 0)
            info.setTitle(title.get(0).text());

        Elements time = document.select("time.post-time");
        if (time.size() > 0)
            info.setTime(time.get(0).attr("datetime"));

        Elements elements = document.select(".entry-content img");
        if (elements.size() > 0) {
            info.setPicUrl(elements.get(0).attr("src"));
            return info;
        }
        return null;
    }
}
