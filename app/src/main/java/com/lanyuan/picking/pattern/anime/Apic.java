package com.lanyuan.picking.pattern.anime;

import android.graphics.Color;
import android.util.Log;

import com.lanyuan.picking.common.bean.AlbumInfo;
import com.lanyuan.picking.common.bean.PicInfo;
import com.lanyuan.picking.pattern.MultiPicturePattern;
import com.lanyuan.picking.pattern.Searchable;
import com.lanyuan.picking.ui.contents.ContentsActivity;
import com.lanyuan.picking.ui.detail.DetailActivity;
import com.lanyuan.picking.ui.menu.Menu;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Apic implements MultiPicturePattern, Searchable {
    @Override
    public String getCategoryCoverUrl() {
        return "http://www.apic.in/wp-content/themes/AZone/big-logo.png";
    }

    @Override
    public int getBackgroundColor() {
        return Color.WHITE;
    }

    @Override
    public String getBaseUrl(List<Menu> menuList, int position) {
        return "http://www.apic.in/";
    }

    @Override
    public List<Menu> getMenuList() {
        List<Menu> menuList = new ArrayList<>();
        menuList.add(new Menu("动漫区", "http://www.apic.in/anime"));
        menuList.add(new Menu("制服控", "http://www.apic.in/zhifu"));
        menuList.add(new Menu("Hentai", "http://www.apic.in/hentai"));
        menuList.add(new Menu("御三家", "http://www.apic.in/yusanjia"));
        menuList.add(new Menu("二次元杂图", "http://www.apic.in/zatuji"));
        menuList.add(new Menu("福利包", "http://www.apic.in/fuli"));
        menuList.add(new Menu("足控", "http://www.apic.in/hentai/zukongfuli"));
        menuList.add(new Menu("绝对领域", "http://www.apic.in/hentai/jueduilingyu"));
        menuList.add(new Menu("胖次", "http://www.apic.in/hentai/pangci"));
        menuList.add(new Menu("萝莉控", "http://www.apic.in/zhifu/lolikong"));
        menuList.add(new Menu("百合", "http://www.apic.in/hentai/baihe"));
        menuList.add(new Menu("巨乳", "http://www.apic.in/hentai/juru"));
        return menuList;
    }

    @Override
    public Map<ContentsActivity.parameter, Object> getContent(String baseUrl, String currentUrl, byte[] result, Map<ContentsActivity.parameter, Object> resultMap) throws UnsupportedEncodingException {
        List<AlbumInfo> data = new ArrayList<>();
        Document document = Jsoup.parse(new String(result, "utf-8"));

        Elements elements = document.select("div.loop");
        for (Element element : elements) {
            AlbumInfo temp = new AlbumInfo();
            Elements album = element.select(".content a:has(img)");
            if (album.size() > 0)
                temp.setAlbumUrl(album.attr("href") + "/1");

            Elements pic = album.select("img");
            if (pic.size() > 0)
                temp.setCoverUrl(pic.get(0).attr("src").trim());

            Elements title = element.select("h2 a");
            if (title.size() > 0)
                temp.setTitle(title.get(0).attr("title"));

            Elements time = element.select(".date");
            if (time.size() > 0)
                temp.setTime(time.get(0).text());

            data.add(temp);
        }

        if (data.size() == 0) {
            Elements elements1 = document.select(".postlists a.block-image");
            for (Element element : elements1) {
                AlbumInfo temp = new AlbumInfo();
                temp.setAlbumUrl(element.attr("href").trim());
                String s = element.attr("style");
                Pattern pattern = Pattern.compile("http.*jpg");
                Matcher matcher = pattern.matcher(s);
                if (matcher.find())
                    temp.setCoverUrl(matcher.group());
                data.add(temp);
            }
        }

        resultMap.put(ContentsActivity.parameter.CURRENT_URL, currentUrl);
        resultMap.put(ContentsActivity.parameter.RESULT, data);
        return resultMap;
    }

    @Override
    public String getContentNext(String baseUrl, String currentUrl, byte[] result) throws UnsupportedEncodingException {
        Document document = Jsoup.parse(new String(result, "utf-8"));
        Elements elements = document.select("#page .next");
        if (elements.size() > 0)
            return elements.get(0).attr("href");
        return "";
    }

    @Override
    public Map<DetailActivity.parameter, Object> getDetailContent(String baseUrl, String currentUrl, byte[] result, Map<DetailActivity.parameter, Object> resultMap) throws UnsupportedEncodingException {
        List<PicInfo> urls = new ArrayList<>();
        Document document = Jsoup.parse(new String(result, "utf-8"));
        Elements title = document.select("#title h2");
        String sTitle = "";
        if (title.size() > 0)
            sTitle = title.get(0).text();

        Elements elements = document.select(".post img");
        for (Element element : elements) {
            urls.add(new PicInfo(element.attr("src").trim()).setTitle(sTitle));
        }

        if (urls.size() == 0) {
            Elements elements1 = document.select(".entry-content p a:has(img)");
            for (Element element : elements) {
                urls.add(new PicInfo(element.attr("href").trim()).setTitle(sTitle));
            }
        }

        resultMap.put(DetailActivity.parameter.CURRENT_URL, currentUrl);
        resultMap.put(DetailActivity.parameter.RESULT, urls);
        return resultMap;
    }

    @Override
    public String getDetailNext(String baseUrl, String currentUrl, byte[] result) throws UnsupportedEncodingException {
        Document document = Jsoup.parse(new String(result, "utf-8"));
        Elements elements = document.select("#page-links a");

        String url = "";

        Elements elements1 = document.select("nav a:containsOwn(下一页)");
        if (elements1.size() > 0) {
            url = elements1.get(0).attr("href");
        }


        if ("".equals(url)) {
            int pageCount = 0, index;
            if (elements.size() > 0)
                pageCount = elements.size() + 1;
            else
                url = "";
            int length = currentUrl.length();
            String prefix = currentUrl.substring(0, length - 1);
            index = Integer.parseInt(currentUrl.substring(length - 1, length));
            if (++index > pageCount)
                url = "";
            else
                url = prefix + index;
        }

        return url;
    }

    @Override
    public String getSearch(String query) {
        return "http://www.apic.in/?s=" + query;
    }
}
