package com.lanyuan.picking.pattern.others;

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

public class Nanrentu implements MultiPicturePattern {

    @Override
    public String getCategoryCoverUrl() {
        return "http://www.nanrentu.cc/images/logo.png";
    }

    @Override
    public int getBackgroundColor() {
        return Color.WHITE;
    }

    @Override
    public String getBaseUrl(List<Menu> menuList, int position) {
        return "http://www.nanrentu.cc";
    }

    @Override
    public List<Menu> getMenuList() {
        List<Menu> menuList = new ArrayList<>();
        menuList.add(new Menu("内地帅哥", "http://www.nanrentu.cc/nd/list_1_1.html"));
        menuList.add(new Menu("港台帅哥", "http://www.nanrentu.cc/gt/list_2_1.html"));
        menuList.add(new Menu("欧美帅哥", "http://www.nanrentu.cc/om/list_18_1.html"));
        menuList.add(new Menu("gay", "http://www.nanrentu.cc/gay/list_855_1.html"));
        return menuList;
    }

    @Override
    public Map<ContentsActivity.parameter, Object> getContent(String baseUrl, String currentUrl, byte[] result, Map<ContentsActivity.parameter, Object> resultMap) throws UnsupportedEncodingException {
        List<AlbumInfo> urls = new ArrayList<>();
        Document document = Jsoup.parse(new String(result, "gb2312"));
        Elements elements = document.select(".partacpic li a");
        for (Element element : elements) {
            AlbumInfo temp = new AlbumInfo();
            temp.setAlbumUrl(baseUrl + element.attr("href"));
            Elements elements1 = element.select("img");
            if (elements1.size() > 0) {
                temp.setCoverUrl(baseUrl + elements1.get(0).attr("src"));
            }
            urls.add(temp);
        }
        resultMap.put(ContentsActivity.parameter.CURRENT_URL, currentUrl);
        resultMap.put(ContentsActivity.parameter.RESULT, urls);
        return resultMap;
    }

    @Override
    public String getContentNext(String baseUrl, String currentUrl, byte[] result) throws UnsupportedEncodingException {
        Document document = Jsoup.parse(new String(result, "gb2312"));
        Elements elements = document.select("div.pagelist a:contains(下一页)");
        if (elements.size() > 0) {
            Pattern pattern = Pattern.compile("http.*/");
            Matcher matcher = pattern.matcher(currentUrl);
            if (matcher.find()) {
                return matcher.group() + elements.get(0).attr("href");
            }
        }
        return "";
    }

    @Override
    public Map<DetailActivity.parameter, Object> getDetailContent(String baseUrl, String currentUrl, byte[] result, Map<DetailActivity.parameter, Object> resultMap) throws UnsupportedEncodingException {
        List<PicInfo> urls = new ArrayList<>();
        Document document = Jsoup.parse(new String(result, "gb2312"));
        Elements elements = document.select("div.picshowtop img");
        if (elements.size() > 0)
            urls.add(new PicInfo(baseUrl + elements.get(0).attr("src")));
        resultMap.put(DetailActivity.parameter.CURRENT_URL, currentUrl);
        resultMap.put(DetailActivity.parameter.RESULT, urls);
        return resultMap;
    }

    @Override
    public String getDetailNext(String baseUrl, String currentUrl, byte[] result) throws UnsupportedEncodingException {
        Document document = Jsoup.parse(new String(result, "gb2312"));
        Elements elements = document.select("div.pagelist a:contains(下一页)");
        if (elements.size() > 0) {
            String tempUrl = elements.get(0).attr("href");
            if (tempUrl == null || "".equals(tempUrl))
                return "";
            Pattern pattern = Pattern.compile("http.*/");
            Matcher matcher = pattern.matcher(currentUrl);
            if (matcher.find()) {
                return matcher.group() + tempUrl;
            }
        }
        return "";
    }

}
