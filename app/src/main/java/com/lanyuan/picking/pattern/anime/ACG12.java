package com.lanyuan.picking.pattern.anime;

import android.graphics.Color;

import com.lanyuan.picking.common.AlbumInfo;
import com.lanyuan.picking.ui.contents.ContentsActivity;
import com.lanyuan.picking.ui.detail.DetailActivity;
import com.lanyuan.picking.ui.menu.Menu;
import com.lanyuan.picking.pattern.BasePattern;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

public class ACG12 implements BasePattern {
    @Override
    public String getCategoryCoverUrl() {
        return "https://static.acg12.com/uploads/2017/06/7cc936d8c0258f17b7ca86309d919490.png";
    }

    @Override
    public int getBackgroundColor() {
        return Color.rgb(236, 227, 191);
    }

    @Override
    public String getBaseUrl(List<Menu> menuList, int position) {
        return null;
    }

    @Override
    public List<Menu> getMenuList() {
        List<Menu> menuList = new ArrayList<>();
        menuList.add(new Menu("动漫杂志", "https://acg12.com/category/acg-gallery/acg-magazine/"));
        return menuList;
    }

    @Override
    public Map<ContentsActivity.parameter, Object> getContent(String baseUrl, String currentUrl, byte[] result, Map<ContentsActivity.parameter, Object> resultMap) throws UnsupportedEncodingException {
        List<AlbumInfo> data = new ArrayList<>();
        Document document = Jsoup.parse(new String(result, "utf-8"));
        Elements elements = document.select("section .card-bg > a");
        for (Element element : elements) {
            AlbumInfo temp = new AlbumInfo();
            temp.setAlbumUrl(element.attr("href"));
            Elements elements1 = element.select("img");
            if (elements1.size() > 0)
                temp.setCoverUrl(elements1.get(0).attr("data-src"));
            data.add(temp);
        }

        resultMap.put(ContentsActivity.parameter.CURRENT_URL, currentUrl);
        resultMap.put(ContentsActivity.parameter.RESULT, data);
        return resultMap;
    }

    @Override
    public String getNext(String baseUrl, String currentUrl, byte[] result) throws UnsupportedEncodingException {
        Document document = Jsoup.parse(new String(result, "utf-8"));
        Elements elements = document.select(".pager a.next");
        if (elements.size() > 0)
            return elements.get(0).attr("href");
        return "";
    }

    @Override
    public Map<DetailActivity.parameter, Object> getDetailContent(String baseUrl, String currentUrl, byte[] result, Map<DetailActivity.parameter, Object> resultMap) throws UnsupportedEncodingException {
        List<String> urls = new ArrayList<>();
        Document document = Jsoup.parse(new String(result, "utf-8"));
        Elements elements = document.select(".entry-content a:has(img)");
        for (Element element : elements) {
            String url = element.attr("href");
            if (url == null || url.equals("") || !Pattern.matches("https:.*.jpg", url)) {
                Elements elements1 = element.select("img");
                if (elements1.size() > 0)
                    url = elements1.get(0).attr("src");
            }
            if (!Pattern.matches("https:.*.jpg", url))
                url = "https:" + url;
            urls.add(url);
        }
        resultMap.put(DetailActivity.parameter.CURRENT_URL, currentUrl);
        resultMap.put(DetailActivity.parameter.RESULT, urls);
        return resultMap;
    }

    @Override
    public String getDetailNext(String baseUrl, String currentUrl, byte[] result) {
        return "";
    }
}
