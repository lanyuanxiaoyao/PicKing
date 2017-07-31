package com.lanyuan.picking.pattern.custom;

import android.graphics.Color;

import com.lanyuan.picking.R;
import com.lanyuan.picking.common.ContentsActivity;
import com.lanyuan.picking.common.DetailActivity;
import com.lanyuan.picking.common.AlbumInfo;
import com.lanyuan.picking.common.Menu;
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

import okhttp3.Call;
import okhttp3.Request;
import okhttp3.Response;

public class MM131Pattern implements BasePattern {
    @Override
    public int getResourceId() {
        return R.mipmap.mm131;
    }

    @Override
    public int getBackgroundColor() {
        return Color.WHITE;
    }

    @Override
    public String getBaseUrl(List<Menu> menuList, int position) {
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
    public Map<ContentsActivity.parameter, Object> getContent(String baseUrl, String currentUrl) {
        Map<ContentsActivity.parameter, Object> resultMap = new HashMap<>();
        List<AlbumInfo> data = new ArrayList<>();

        Request request = new Request.Builder()
                .url(currentUrl)
                .build();
        try {
            Response response = OkHttpClientUtil.getInstance().newCall(request).execute();
            byte[] result = response.body().bytes();
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
            Document document = Jsoup.parse(new String(result, "gbk"));
            Elements elements = document.select("dd.page a:containsOwn(下一页)");
            if (elements.size() > 0)
                return baseUrl + elements.get(0).attr("href");
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
            Call call = OkHttpClientUtil.getInstance().newCall(request);
            Response response = call.execute();
            byte[] result = response.body().bytes();
            Document document = Jsoup.parse(new String(result, "gbk"));
            Elements elements = document.select("div.content-pic img");
            if (elements.size() > 0)
                urls.add(elements.get(0).attr("src"));
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
            Call call = OkHttpClientUtil.getInstance().newCall(request);
            Response response = call.execute();
            byte[] result = response.body().bytes();
            Document document = Jsoup.parse(new String(result, "gbk"));
            Elements elements = document.select("div.content-page a:containsOwn(下一页)");
            if (elements.size() > 0)
                return baseUrl + elements.get(0).attr("href");
        } catch (IOException e) {
            e.printStackTrace();
        }
        return "";
    }
}
