package com.lanyuan.picking.pattern.custom;

import android.graphics.Color;

import com.lanyuan.picking.pattern.BasePattern;
import com.lanyuan.picking.ui.contents.ContentsActivity;
import com.lanyuan.picking.ui.detail.DetailActivity;
import com.lanyuan.picking.common.AlbumInfo;
import com.lanyuan.picking.ui.menu.Menu;
import com.lanyuan.picking.util.OkHttpClientUtil;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import okhttp3.Call;
import okhttp3.Request;
import okhttp3.Response;

public class RosiMM implements BasePattern {
    @Override
    public String getCategoryCoverUrl() {
        return "http://cdn-rosmm.b0.upaiyun.com/public/image/logo.png";
    }

    @Override
    public int getBackgroundColor() {
        return Color.WHITE;
    }

    @Override
    public String getBaseUrl(List<Menu> menuList, int position) {
        return "http://www.rosmm.com/";
    }

    @Override
    public List<Menu> getMenuList() {
        List<Menu> menuList = new ArrayList<>();
        menuList.add(new Menu("ROSI写真", "http://www.rosmm.com/rosimm"));
        return menuList;
    }

    @Override
    public boolean isSinglePic() {
        return false;
    }

    @Override
    public Map<ContentsActivity.parameter, Object> getContent(String baseUrl, String currentUrl, byte[] result, Map<ContentsActivity.parameter, Object> resultMap) throws UnsupportedEncodingException {
        List<AlbumInfo> urls = new ArrayList<>();
        Document document = Jsoup.parse(new String(result, "gbk"));
        Elements elements = document.select("#sliding li a:has(img)");
        for (Element element : elements) {
            AlbumInfo temp = new AlbumInfo();
            temp.setAlbumUrl(baseUrl + element.attr("href"));
            Elements elements1 = element.select("img");
            if (elements1.size() > 0)
                temp.setCoverUrl(elements1.get(0).attr("src"));
            urls.add(temp);
        }
        resultMap.put(ContentsActivity.parameter.CURRENT_URL, currentUrl);
        resultMap.put(ContentsActivity.parameter.RESULT, urls);
        return resultMap;
    }

    @Override
    public String getContentNext(String baseUrl, String currentUrl, byte[] result) throws UnsupportedEncodingException {
        Document document = Jsoup.parse(new String(result, "gbk"));
        Elements elements = document.select("script");
        for (Element element : elements) {
            String code = element.html();
            if (!element.html().equals("")) {
                Pattern pattern = Pattern.compile("index_\\d*.htm\">下一页");
                Matcher matcher = pattern.matcher(code);
                if (matcher.find()) {
                    String temp = matcher.group();
                    return baseUrl + "rosimm/" + temp.substring(0, temp.length() - 5);
                }
            }
        }
        return "";
    }

    @Override
    public String getSinglePicContent(String baseUrl, String currentUrl, byte[] result) {
        return null;
    }

    @Override
    public Map<DetailActivity.parameter, Object> getDetailContent(String baseUrl, String currentUrl, byte[] result, Map<DetailActivity.parameter, Object> resultMap) throws UnsupportedEncodingException {
        List<String> urls = new ArrayList<>();
        Document document = Jsoup.parse(new String(result, "gbk"));
        Elements elements = document.select("#imgString img");
        for (Element element : elements) {
            urls.add(element.attr("src"));
        }


        resultMap.put(DetailActivity.parameter.CURRENT_URL, currentUrl);
        resultMap.put(DetailActivity.parameter.RESULT, urls);
        return resultMap;
    }

    @Override
    public String getDetailNext(String baseUrl, String currentUrl, byte[] result) throws UnsupportedEncodingException {
        Document document = Jsoup.parse(new String(result, "gbk"));
        Elements elements = document.select(".page_c a:containsOwn(下一页)");
        if (elements.size() > 0) {
            Pattern pattern = Pattern.compile("http://.*/");
            Matcher matcher = pattern.matcher(currentUrl);
            if (matcher.find()) {
                String prefix = matcher.group();
                return prefix + elements.get(0).attr("href");
            }
        }
        return "";
    }
}
