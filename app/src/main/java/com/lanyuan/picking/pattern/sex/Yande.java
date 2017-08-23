package com.lanyuan.picking.pattern.sex;

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

public class Yande implements SinglePicturePattern {
    @Override
    public String getWebsiteName() {
        return "Yan.de";
    }

    @Override
    public String getCategoryCoverUrl() {
        return "https://assets.yande.re/assets/logo_small-418e8d5ec0229f274edebe4af43b01aa29ed83b715991ba14bb41ba06b5b57b5.png";
    }

    @Override
    public int getBackgroundColor() {
        return Color.BLACK;
    }

    @Override
    public String getBaseUrl(List<Menu> menuList, int position) {
        return "https://yande.re";
    }

    @Override
    public List<Menu> getMenuList() {
        List<Menu> menuList = new ArrayList<>();
        menuList.add(new Menu("Posts", "https://yande.re/post"));
        menuList.add(new Menu("ThighHighs", "https://yande.re/post?tags=thighhighs"));
        menuList.add(new Menu("Seifuku", "https://yande.re/post?tags=seifuku"));
        menuList.add(new Menu("Nipples", "https://yande.re/post?tags=nipples"));
        menuList.add(new Menu("Dress", "https://yande.re/post?tags=dress"));
        menuList.add(new Menu("SwimSuits", "https://yande.re/post?tags=swimsuits"));
        menuList.add(new Menu("NoBra", "https://yande.re/post?tags=no_bra"));
        menuList.add(new Menu("OpenShirt", "https://yande.re/post?tags=open_shirt"));
        menuList.add(new Menu("AnimalEars", "https://yande.re/post?tags=animal_ears"));
        menuList.add(new Menu("Maid", "https://yande.re/post?tags=maid"));
        menuList.add(new Menu("Undressing", "https://yande.re/post?tags=undressing"));
        return menuList;
    }

    @Override
    public Map<ContentsActivity.parameter, Object> getContent(String baseUrl, String currentUrl, byte[] result, Map<ContentsActivity.parameter, Object> resultMap) throws UnsupportedEncodingException {
        List<AlbumInfo> data = new ArrayList<>();
        Document document = Jsoup.parse(new String(result, "utf-8"));
        Elements elements = document.select("#post-list-posts li div.inner a");
        for (Element element : elements) {
            AlbumInfo temp = new AlbumInfo();
            temp.setAlbumUrl(baseUrl + element.attr("href"));
            Elements elements1 = element.select("img");
            if (elements1.size() > 0)
                temp.setCoverUrl(elements1.get(0).attr("src"));
            data.add(temp);
        }

        resultMap.put(ContentsActivity.parameter.CURRENT_URL, currentUrl);
        resultMap.put(ContentsActivity.parameter.RESULT, data);
        return resultMap;
    }

    @Override
    public String getContentNext(String baseUrl, String currentUrl, byte[] result) throws UnsupportedEncodingException {
        Document document = Jsoup.parse(new String(result, "utf-8"));
        Log.e("Yande", "getSinglePicContent: " + new String(result, "utf-8"));
        Elements elements = document.select("div#paginator a.next_page");
        if (elements.size() > 0) {
            Log.e("Yande", "getContentNext: " + baseUrl + elements.get(0).attr("href"));
            return baseUrl + elements.get(0).attr("href");
        }
        return "";
    }

    @Override
    public PicInfo getSinglePicContent(String baseUrl, String currentUrl, byte[] result) throws UnsupportedEncodingException {
        Document document = Jsoup.parse(new String(result, "utf-8"));
        Elements elements = document.select("#right-col img");
        if (elements.size() > 0) {
            return new PicInfo(elements.get(0).attr("src"));
        }
        return null;
    }
}
