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

import okhttp3.Request;
import okhttp3.Response;

public class ApicPattern implements BasePattern {
    @Override
    public int getResourceId() {
        return R.mipmap.apic;
    }

    @Override
    public int getBackgroundColor() {
        return Color.rgb(142, 241, 218);
    }

    @Override
    public String getBaseUrl(List<Menu> menuList, int position) {
        return null;
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
            Elements elements = document.select(".content a");
            for (Element element : elements) {
                AlbumInfo temp = new AlbumInfo();
                temp.setAlbumUrl(element.attr("href") + "/1");
                Elements elements1 = element.select("img");
                if (elements1.size() > 0)
                    temp.setCoverUrl(elements1.get(0).attr("src"));
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
            Elements elements = document.select("#page .next");
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
            Elements elements = document.select(".post img");
            for (Element element : elements) {
                urls.add(element.attr("src"));
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
        Request request = new Request.Builder()
                .url(currentUrl)
                .build();

        try {
            Response response = OkHttpClientUtil.getInstance().newCall(request).execute();
            byte[] result = response.body().bytes();
            Document document = Jsoup.parse(new String(result, "utf-8"));
            Elements elements = document.select("#page-links a");

            int pageCount, index;
            if (elements.size() > 0)
                pageCount = elements.size() + 1;
            else
                return "";
            int length = currentUrl.length();
            String prefix = currentUrl.substring(0, length - 1);
            index = Integer.parseInt(currentUrl.substring(length - 1, length));
            if (++index > pageCount)
                return "";
            else
                return prefix + index;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return "";
    }
}
