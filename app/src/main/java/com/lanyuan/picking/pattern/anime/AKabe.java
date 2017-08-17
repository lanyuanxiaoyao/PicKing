package com.lanyuan.picking.pattern.anime;

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

public class AKabe implements MultiPicturePattern {
    @Override
    public String getCategoryCoverUrl() {
        return "http://www.a-kabe.com/common/img/hnav_logo.png";
    }

    @Override
    public int getBackgroundColor() {
        return Color.rgb(0, 128, 128);
    }

    @Override
    public String getBaseUrl(List<Menu> menuList, int position) {
        return "http://www.a-kabe.com";
    }

    @Override
    public List<Menu> getMenuList() {
        List<Menu> menuList = new ArrayList<>();
        menuList.add(new Menu("全部图片", "http://www.a-kabe.com/"));
        return menuList;
    }

    @Override
    public Map<ContentsActivity.parameter, Object> getContent(String baseUrl, String currentUrl, byte[] result, Map<ContentsActivity.parameter, Object> resultMap) throws UnsupportedEncodingException {
        List<AlbumInfo> data = new ArrayList<>();
        Document document = Jsoup.parse(new String(result, "utf-8"));
        Elements elements = document.select("#main article.entry");
        for (Element element : elements) {
            AlbumInfo temp = new AlbumInfo();

            Elements pic = element.select("figure a");
            if (pic.size() > 0) {
                temp.setAlbumUrl(pic.attr("href"));
                Elements cover = pic.select("img");
                if (cover.size() > 0)
                    temp.setCoverUrl(cover.get(0).attr("src"));
            }

            Elements title = element.select("h2");
            if (title.size() > 0)
                temp.setTitle(title.get(0).text());

            data.add(temp);
        }

        resultMap.put(ContentsActivity.parameter.CURRENT_URL, currentUrl);
        resultMap.put(ContentsActivity.parameter.RESULT, data);
        return resultMap;
    }

    @Override
    public String getContentNext(String baseUrl, String currentUrl, byte[] result) throws UnsupportedEncodingException {
        Document document = Jsoup.parse(new String(result, "utf-8"));
        Elements elements = document.select(".pageButeNav a.link_next");
        if (elements.size() > 0)
            return elements.get(0).attr("href");
        return "";
    }

    @Override
    public Map<DetailActivity.parameter, Object> getDetailContent(String baseUrl, String currentUrl, byte[] result, Map<DetailActivity.parameter, Object> resultMap) throws UnsupportedEncodingException {
        List<PicInfo> urls = new ArrayList<>();
        Document document = Jsoup.parse(new String(result, "utf-8"));

        String sTitle = "";
        Elements title = document.select("#header h1");
        if (title.size() > 0)
            sTitle = title.get(0).text();

        List<String> tagList = new ArrayList<>();
        Elements tags = document.select("ul.tagList a");
        if (tags.size() > 0)
            for (Element tag : tags)
                tagList.add(tag.text());

        Elements elements = document.select("ul.gallery li:has(img)");
        for (Element element : elements) {
            urls.add(new PicInfo(element.attr("data-src")).setTitle(sTitle).setTags(tagList));
        }

        resultMap.put(DetailActivity.parameter.CURRENT_URL, currentUrl);
        resultMap.put(DetailActivity.parameter.RESULT, urls);
        return resultMap;
    }

    @Override
    public String getDetailNext(String baseUrl, String currentUrl, byte[] result) throws UnsupportedEncodingException {
        return null;
    }
}
