package com.lanyuan.picking.pattern.anime;

import android.graphics.Color;

import com.lanyuan.picking.common.bean.AlbumInfo;
import com.lanyuan.picking.common.bean.PicInfo;
import com.lanyuan.picking.pattern.Searchable;
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

public class AnimePic implements SinglePicturePattern, Searchable {
    @Override
    public String getWebsiteName() {
        return "Anime Picture";
    }

    @Override
    public String getCategoryCoverUrl() {
        return "https://raw.githubusercontent.com/lanyuanxiaoyao/GitGallery/master/anime-pic.png";
    }

    @Override
    public int getBackgroundColor() {
        return Color.rgb(238, 238, 238);
    }

    @Override
    public String getBaseUrl(List<Menu> menuList, int position) {
        return "https://anime-pictures.net";
    }

    @Override
    public List<Menu> getMenuList() {
        List<Menu> menuList = new ArrayList<>();
        menuList.add(new Menu("Pictures", "https://anime-pictures.net/pictures/view_posts/0?lang=en"));
        return menuList;
    }

    @Override
    public Map<ContentsActivity.parameter, Object> getContent(String baseUrl, String currentUrl, byte[] result, Map<ContentsActivity.parameter, Object> resultMap) throws UnsupportedEncodingException {
        List<AlbumInfo> data = new ArrayList<>();
        Document document = Jsoup.parse(new String(result, "utf-8"));
        Elements elements = document.select(".posts_block span.img_block_big>a");
        for (Element element : elements) {
            AlbumInfo temp = new AlbumInfo();
            temp.setAlbumUrl(baseUrl + element.attr("href"));
            Elements elements1 = element.select("img");
            if (elements1.size() > 0) {
                String coverUrl = elements1.get(0).attr("src");
                if (coverUrl != null && !"".equals(coverUrl)) {
                    if (coverUrl.startsWith("https:"))
                        temp.setPicUrl(coverUrl);
                    else
                        temp.setPicUrl("https:" + coverUrl);
                }
            }
            if (temp.getPicUrl() != null && !"".equals(temp.getPicUrl()))
                data.add(temp);
        }

        resultMap.put(ContentsActivity.parameter.CURRENT_URL, currentUrl);
        resultMap.put(ContentsActivity.parameter.RESULT, data);
        return resultMap;
    }

    @Override
    public String getContentNext(String baseUrl, String currentUrl, byte[] result) throws UnsupportedEncodingException {
        Document document = Jsoup.parse(new String(result, "utf-8"));
        Elements elements = document.select("p.numeric_pages a:containsOwn(>)");
        if (elements.size() > 0) {
            return baseUrl + elements.get(0).attr("href");
        }
        return "";
    }

    @Override
    public PicInfo getSinglePicContent(String baseUrl, String currentUrl, byte[] result) throws UnsupportedEncodingException {
        Document document = Jsoup.parse(new String(result, "utf-8"));

        String sTitle = "";
        Elements title = document.select(".post_content h1");
        if (title.size() > 0)
            sTitle = title.get(0).text();

        List<String> tagList = new ArrayList<>();
        Elements tags = document.select("ul.tags li a");
        if (tags.size() > 0)
            for (Element tag : tags)
                tagList.add(tag.text());

        Elements elements = document.select("#big_preview_cont a");
        if (elements.size() > 0) {
            return new PicInfo(baseUrl + elements.get(0).attr("href")).setTitle(sTitle).setTags(tagList);
        }
        return null;
    }

    @Override
    public String getSearch(String query) {
        return "https://anime-pictures.net/pictures/view_posts/0?search_tag=" + query + "&order_by=date&ldate=0&lang=en";
    }
}
