package com.lanyuan.picking.pattern.anime;

import android.graphics.Color;
import android.util.Log;

import com.lanyuan.picking.common.AlbumInfo;
import com.lanyuan.picking.pattern.BasePattern;
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

public class Yande implements BasePattern {
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
        return menuList;
    }

    @Override
    public boolean isSinglePic() {
        return true;
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
    public String getSinglePicContent(String baseUrl, String currentUrl, byte[] result) throws UnsupportedEncodingException {
        Document document = Jsoup.parse(new String(result, "utf-8"));
        Elements elements = document.select("#right-col img");
        if (elements.size() > 0) {
            return elements.get(0).attr("src");
        }
        return "";
    }

    @Override
    public Map<DetailActivity.parameter, Object> getDetailContent(String baseUrl, String currentUrl, byte[] result, Map<DetailActivity.parameter, Object> resultMap) throws UnsupportedEncodingException {
        return null;
    }

    @Override
    public String getDetailNext(String baseUrl, String currentUrl, byte[] result) throws UnsupportedEncodingException {
        return null;
    }
}
