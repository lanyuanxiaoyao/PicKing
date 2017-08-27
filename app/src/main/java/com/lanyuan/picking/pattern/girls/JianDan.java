package com.lanyuan.picking.pattern.girls;

import android.graphics.Color;
import android.util.Log;

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

public class JianDan implements SinglePicturePattern {

    @Override
    public String getWebsiteName() {
        return "煎蛋";
    }

    @Override
    public String getCategoryCoverUrl() {
        return "http://cdn.jandan.net/wp-content/themes/egg/images/logo-2015.gif";
    }

    @Override
    public int getBackgroundColor() {
        return Color.WHITE;
    }

    @Override
    public String getBaseUrl(List<Menu> menuList, int position) {
        return "http://jandan.net/ooxx";
    }

    @Override
    public List<Menu> getMenuList() {
        List<Menu> menuList = new ArrayList<>();
        menuList.add(new Menu("妹子图", "http://jandan.net/ooxx"));
        return menuList;
    }

    @Override
    public Map<ContentsActivity.parameter, Object> getContent(String baseUrl, String currentUrl, byte[] result, Map<ContentsActivity.parameter, Object> resultMap) throws UnsupportedEncodingException {
        List<AlbumInfo> urls = new ArrayList<>();
        Document document = Jsoup.parse(new String(result, "utf-8"));
        Elements elements = document.select(".commentlist div.text p:has(img)");
        for (Element element : elements) {
            AlbumInfo temp = new AlbumInfo();
            Elements elements1 = element.select("img");
            if (elements1.size() > 0) {
                String picOrgSrc = elements1.get(0).attr("org_src");
                String picSrc = elements1.get(0).attr("src");
                if (picOrgSrc == null || "".equals(picOrgSrc)) {
                    temp.setAlbumUrl("http:" + picSrc);
                    temp.setPicUrl("http:" + picSrc);
                } else {
                    temp.setAlbumUrl("http:" + picOrgSrc);
                    temp.setPicUrl("http:" + picOrgSrc);
                    temp.setGifThumbUrl("http:" + picSrc);
                }
            }
            urls.add(temp);
        }
        resultMap.put(ContentsActivity.parameter.CURRENT_URL, currentUrl);
        resultMap.put(ContentsActivity.parameter.RESULT, urls);
        return resultMap;
    }

    @Override
    public String getContentNext(String baseUrl, String currentUrl, byte[] result) throws UnsupportedEncodingException {
        Document document = Jsoup.parse(new String(result, "utf-8"));
        Elements elements = document.select(".cp-pagenavi a.previous-comment-page");
        if (elements.size() > 0)
            return elements.get(0).attr("href");
        return "";
    }

    @Override
    public PicInfo getSinglePicContent(String baseUrl, String currentUrl, byte[] result) throws UnsupportedEncodingException {
        return new PicInfo(new String(result));
    }
}
