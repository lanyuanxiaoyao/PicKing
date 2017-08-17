package com.lanyuan.picking.pattern.girls;

import android.graphics.Color;

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

public class Mntu92 implements MultiPicturePattern {

    @Override
    public String getCategoryCoverUrl() {
        return "http://92mntu.com/templets/edc/images/logo.png";
    }

    @Override
    public int getBackgroundColor() {
        return Color.rgb(241, 113, 113);
    }

    @Override
    public String getBaseUrl(List<Menu> menuList, int position) {
        return "http://92mntu.com";
    }

    @Override
    public List<Menu> getMenuList() {
        List<Menu> menuList = new ArrayList<>();
        menuList.add(new Menu("清纯美女", "http://92mntu.com/qcmn/"));
        menuList.add(new Menu("性感美女", "http://92mntu.com/xgmn/"));
        menuList.add(new Menu("丝袜美腿", "http://92mntu.com/swmt/"));
        menuList.add(new Menu("日韩美女", "http://92mntu.com/rhmn/"));
        menuList.add(new Menu("美女车模", "http://92mntu.com/mncm/"));
        menuList.add(new Menu("美女明星", "http://92mntu.com/mnmx/"));
        return menuList;
    }

    @Override
    public Map<ContentsActivity.parameter, Object> getContent(String baseUrl, String currentUrl, byte[] result, Map<ContentsActivity.parameter, Object> resultMap) throws UnsupportedEncodingException {
        List<AlbumInfo> urls = new ArrayList<>();
        Document document = Jsoup.parse(new String(result, "utf-8"));
        Elements elements = document.select("#container a:has(img)");
        for (Element element : elements) {
            AlbumInfo temp = new AlbumInfo();
            temp.setAlbumUrl(baseUrl + element.attr("href"));
            Elements elements1 = element.select("img");
            if (elements1.size() > 0)
                temp.setCoverUrl(baseUrl + elements1.get(0).attr("src"));
            urls.add(temp);
        }
        resultMap.put(ContentsActivity.parameter.CURRENT_URL, currentUrl);
        resultMap.put(ContentsActivity.parameter.RESULT, urls);
        return resultMap;
    }

    @Override
    public String getContentNext(String baseUrl, String currentUrl, byte[] result) throws UnsupportedEncodingException {
        Document document = Jsoup.parse(new String(result, "utf-8"));
        Elements elements = document.select("#pager a:containsOwn(下一页)");
        if (elements.size() > 0) {
            Pattern pattern = Pattern.compile("http:.*/");
            Matcher matcher = pattern.matcher(currentUrl);
            if (matcher.find())
                return matcher.group() + elements.get(0).attr("href");
        }
        return "";
    }

    @Override
    public Map<DetailActivity.parameter, Object> getDetailContent(String baseUrl, String currentUrl, byte[] result, Map<DetailActivity.parameter, Object> resultMap) throws UnsupportedEncodingException {
        List<PicInfo> urls = new ArrayList<>();
        Document document = Jsoup.parse(new String(result, "utf-8"));
        PicInfo picInfo = new PicInfo();
        Elements elements = document.select("#bigpic img");
        for (Element element : elements) {
            picInfo.setPicUrl(baseUrl + element.attr("src"));
        }
        Elements title = document.select("#entry h1");
        if (title.size() > 0)
            picInfo.setTitle(title.text());
        Elements tags = document.select(".postinfo a");
        if (tags.size() > 0) {
            List<String> tagList = new ArrayList<>();
            for (Element t : tags)
                tagList.add(t.text());
            picInfo.setTags(tagList);
        }
        urls.add(picInfo);

        resultMap.put(DetailActivity.parameter.CURRENT_URL, currentUrl);
        resultMap.put(DetailActivity.parameter.RESULT, urls);
        return resultMap;
    }

    @Override
    public String getDetailNext(String baseUrl, String currentUrl, byte[] result) throws UnsupportedEncodingException {
        Document document = Jsoup.parse(new String(result, "utf-8"));
        Elements elements = document.select("div.pageart a:containsOwn(下一页)");
        if (elements.size() > 0) {
            if (elements.get(0).attr("href").equals("#"))
                return "";
            Pattern pattern = Pattern.compile("http:.*/");
            Matcher matcher = pattern.matcher(currentUrl);
            if (matcher.find())
                return matcher.group() + elements.get(0).attr("href");
        }
        return "";
    }
}
