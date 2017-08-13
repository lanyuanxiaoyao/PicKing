package com.lanyuan.picking.pattern.girls;

import android.graphics.Color;

import com.lanyuan.picking.common.bean.PicInfo;
import com.lanyuan.picking.pattern.MultiPicturePattern;
import com.lanyuan.picking.ui.contents.ContentsActivity;
import com.lanyuan.picking.ui.detail.DetailActivity;
import com.lanyuan.picking.common.bean.AlbumInfo;
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

public class MM131 implements MultiPicturePattern {
    @Override
    public String getCategoryCoverUrl() {
        return "https://ss1.baidu.com/6ONXsjip0QIZ8tyhnq/it/u=50538304,1525126075&fm=58&s=7C00763384B06D8210E8B5CE03004021&bpow=200&bpoh=75";
    }

    @Override
    public int getBackgroundColor() {
        return Color.WHITE;
    }

    @Override
    public String getBaseUrl(List<Menu> menuList, int position) {
        if (menuList == null)
            return "http://www.mm131.com";
        return menuList.get(position).getUrl();
    }

    @Override
    public List<Menu> getMenuList() {
        List<Menu> menuList = new ArrayList<>();
        menuList.add(new Menu("性感美女", "http://www.mm131.com/xinggan/"));
        menuList.add(new Menu("清纯美眉", "http://www.mm131.com/qingchun/"));
        menuList.add(new Menu("美女校花", "http://www.mm131.com/xiaohua/"));
        menuList.add(new Menu("性感车模", "http://www.mm131.com/chemo/"));
        menuList.add(new Menu("旗袍美女", "http://www.mm131.com/qipao/"));
        menuList.add(new Menu("明星写真", "http://www.mm131.com/mingxing/"));
        return menuList;
    }

    @Override
    public Map<ContentsActivity.parameter, Object> getContent(String baseUrl, String currentUrl, byte[] result, Map<ContentsActivity.parameter, Object> resultMap) throws UnsupportedEncodingException {
        List<AlbumInfo> data = new ArrayList<>();
        Document document = Jsoup.parse(new String(result, "gbk"));
        Elements elements = document.select("dd a img:not([border])");
        for (Element element : elements) {
            AlbumInfo temp = new AlbumInfo();
            temp.setCoverUrl(element.attr("src").replaceAll("0.jpg", "m.jpg"));
            Pattern pattern = Pattern.compile("/\\d{3,4}");
            Matcher matcher = pattern.matcher(element.attr("src"));
            if (matcher.find())
                temp.setAlbumUrl(baseUrl + matcher.group().substring(1) + ".html");
            data.add(temp);
        }
        resultMap.put(ContentsActivity.parameter.CURRENT_URL, currentUrl);
        resultMap.put(ContentsActivity.parameter.RESULT, data);
        return resultMap;
    }

    @Override
    public String getContentNext(String baseUrl, String currentUrl, byte[] result) throws UnsupportedEncodingException {
        Document document = Jsoup.parse(new String(result, "gbk"));
        Elements elements = document.select("dd.page a:containsOwn(下一页)");
        if (elements.size() > 0)
            return baseUrl + elements.get(0).attr("href");
        return "";
    }

    @Override
    public Map<DetailActivity.parameter, Object> getDetailContent(String baseUrl, String currentUrl, byte[] result, Map<DetailActivity.parameter, Object> resultMap) throws UnsupportedEncodingException {
        List<PicInfo> urls = new ArrayList<>();
        Document document = Jsoup.parse(new String(result, "gbk"));
        Elements elements = document.select("div.content-pic img");
        if (elements.size() > 0)
            urls.add(new PicInfo(elements.get(0).attr("src")));
        resultMap.put(DetailActivity.parameter.CURRENT_URL, currentUrl);
        resultMap.put(DetailActivity.parameter.RESULT, urls);
        return resultMap;
    }

    @Override
    public String getDetailNext(String baseUrl, String currentUrl, byte[] result) throws UnsupportedEncodingException {
        Document document = Jsoup.parse(new String(result, "gbk"));
        Elements elements = document.select("div.content-page a:containsOwn(下一页)");
        if (elements.size() > 0)
            return baseUrl + elements.get(0).attr("href");
        return "";
    }
}
