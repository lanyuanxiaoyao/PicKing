package com.lanyuan.picking.pattern.RoseMM;

import com.lanyuan.picking.common.BaseContentsActivity;
import com.lanyuan.picking.common.BaseInfo;
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

import okhttp3.Request;
import okhttp3.Response;

public class RosiMMActivity extends BaseContentsActivity {
    @Override
    public String getBaseUrl(List<Menu> menuList, int position) {
        return "http://www.rosmm.com/";
    }

    @Override
    public Class getTargetDetailActivity() {
        return RosiMMDetailActivity.class;
    }

    @Override
    public List<Menu> getMenuList() {
        List<Menu> menuList = new ArrayList<>();
        menuList.add(new Menu("ROSI写真", "http://www.rosmm.com/rosimm"));
        return menuList;
    }

    @Override
    public Map<parameter, Object> getContent(String baseUrl, String currentUrl) {
        Map<parameter, Object> resultMap = new HashMap<>();
        List<BaseInfo> urls = new ArrayList<>();

        try {
            Request request = new Request.Builder()
                    .url(currentUrl)
                    .build();
            Response response = OkHttpClientUtil.getInstance().newCall(request).execute();
            byte[] result = response.body().bytes();
            Document document = Jsoup.parse(new String(result, "gbk"));
            Elements elements = document.select("#sliding li a:has(img)");
            for (Element element : elements) {
                BaseInfo temp = new BaseInfo();
                temp.setAlbumUrl(baseUrl + element.attr("href"));
                Elements elements1 = element.select("img");
                if (elements1.size() > 0)
                    temp.setCoverUrl(elements1.get(0).attr("src"));
                urls.add(temp);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        resultMap.put(parameter.CURRENT_URL, currentUrl);
        resultMap.put(parameter.RESULT, urls);
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
            Elements elements = document.select("script");
            for (Element element : elements) {
                String code = element.html();
                if (!element.html().equals("")) {
                    Pattern pattern = Pattern.compile("index_\\d*.htm\">下一页");
                    Matcher matcher = pattern.matcher(code);
                    if (matcher.find()) {
                        String temp = matcher.group();
                        return baseUrl + "rosimm/" + temp.substring(0, temp.length() - 5);
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return "";
    }
}
