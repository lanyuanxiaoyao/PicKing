package com.lanyuan.picking.pattern.others;

import android.graphics.Color;
import android.util.Log;

import com.lanyuan.picking.common.bean.AlbumInfo;
import com.lanyuan.picking.common.bean.PicInfo;
import com.lanyuan.picking.pattern.MultiPicturePattern;
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
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class DuowanCos implements MultiPicturePattern {
    @Override
    public String getWebsiteName() {
        return "多玩图库";
    }

    @Override
    public String getCategoryCoverUrl() {
        return "http://tu.duowan.com/images/logo_v1.7.png";
    }

    @Override
    public int getBackgroundColor() {
        return Color.rgb(68, 68, 68);
    }

    @Override
    public String getBaseUrl(List<Menu> menuList, int position) {
        if (menuList == null)
            return "http://tu.duowan.com";
        String temp = menuList.get(position).getUrl();
        return temp.substring(0, temp.length() - 1);
    }

    @Override
    public List<Menu> getMenuList() {
        List<Menu> menuList = new ArrayList<>();
        menuList.add(new Menu("美女图片", "http://tu.duowan.com/m/meinv?offset=0"));
        menuList.add(new Menu("多玩囧图", "http://tu.duowan.com/tag/5037.html?offset=0"));
        menuList.add(new Menu("吐槽囧图", "http://tu.duowan.com/m/tucao?offset=0"));
        return menuList;
    }

    @Override
    public Map<ContentsActivity.parameter, Object> getContent(String baseUrl, String currentUrl, byte[] result, Map<ContentsActivity.parameter, Object> resultMap) throws UnsupportedEncodingException {
        List<AlbumInfo> data = new ArrayList<>();
        Document document = Jsoup.parse(new String(result, "utf-8"));
        Elements elements = document.select("li.box:not(.tags) a:has(img)");
        for (Element element : elements) {
            AlbumInfo temp = new AlbumInfo();
            temp.setAlbumUrl(element.attr("href").replaceAll("gallery", "scroll"));
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
        currentUrl = currentUrl.replace("5037", "*");
        Pattern pattern = Pattern.compile("[0-9]\\d*");
        Matcher matcher = pattern.matcher(currentUrl);
        if (matcher.find()) {
            String page_s = matcher.group();
            Integer page = Integer.parseInt(page_s) + 30;
            return baseUrl + page;
        }
        return "";
    }

    @Override
    public Map<DetailActivity.parameter, Object> getDetailContent(String baseUrl, String currentUrl, byte[] result, Map<DetailActivity.parameter, Object> resultMap) throws UnsupportedEncodingException {
        List<PicInfo> urls = new ArrayList<>();
        Document document = Jsoup.parse(new String(result, "utf-8"));
        Elements elements = document.select("span.pic-box-item");
        for (Element element : elements)
            urls.add(new PicInfo(element.attr("data-img")));

        resultMap.put(DetailActivity.parameter.CURRENT_URL, currentUrl);
        resultMap.put(DetailActivity.parameter.RESULT, urls);
        return resultMap;
    }

    @Override
    public String getDetailNext(String baseUrl, String currentUrl, byte[] result) throws UnsupportedEncodingException {
        return "";
    }
}