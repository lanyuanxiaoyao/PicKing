package com.lanyuan.picking.pattern.Anime;

import android.graphics.Color;

import com.lanyuan.picking.R;
import com.lanyuan.picking.common.AlbumInfo;
import com.lanyuan.picking.common.ContentsActivity;
import com.lanyuan.picking.common.DetailActivity;
import com.lanyuan.picking.common.Menu;
import com.lanyuan.picking.pattern.custom.BasePattern;
import com.lanyuan.picking.util.OkHttpClientUtil;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import okhttp3.Request;
import okhttp3.Response;

public class ACG12 implements BasePattern {
    @Override
    public int getResourceId() {
        return R.mipmap.acg12;
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
    public Map<ContentsActivity.parameter, Object> getContent(String baseUrl, String currentUrl) {
        Map<ContentsActivity.parameter, Object> resultMap = new HashMap<>();
        List<AlbumInfo> data = new ArrayList<>();

        Request request = new Request.Builder()
                .url(currentUrl)
                .build();
        try {
            Response response = OkHttpClientUtil.getInstance().newCall(request).execute();
            byte[] result = response.body().bytes();
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
        } catch (IOException e) {
            e.printStackTrace();
        }

        resultMap.put(ContentsActivity.parameter.CURRENT_URL, currentUrl);
        resultMap.put(ContentsActivity.parameter.RESULT, data);
        return resultMap;
    }

    @Override
    public String getNext(String baseUrl, String currentUrl) {
        Request request = new Request.Builder()
                .url(currentUrl)
                .build();

        try {
            Response response = OkHttpClientUtil.getInstance().newCall(request).execute();
            byte[] result = response.body().bytes();
            Document document = Jsoup.parse(new String(result, "utf-8"));
            Elements elements = document.select(".pager a.next");
            if (elements.size() > 0)
                return elements.get(0).attr("href");
        } catch (IOException e) {
            e.printStackTrace();
        }
        return "";
    }

    @Override
    public Map<DetailActivity.parameter, Object> getDetailContent(String baseUrl, String currentUrl) {
        Map<DetailActivity.parameter, Object> resultMap = new HashMap<>();
        List<String> urls = new ArrayList<>();
        Request request = new Request.Builder()
                .url(currentUrl)
                .build();

        try {
            Response response = OkHttpClientUtil.getInstance().newCall(request).execute();
            byte[] result = response.body().bytes();
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
        } catch (IOException e) {
            e.printStackTrace();
        }

        resultMap.put(DetailActivity.parameter.CURRENT_URL, currentUrl);
        resultMap.put(DetailActivity.parameter.RESULT, urls);
        return resultMap;
    }

    @Override
    public String getDetailNext(String baseUrl, String currentUrl) {
        return "";
    }
}
