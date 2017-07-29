package com.lanyuan.picking.pattern.MM131;

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

public class MM131Activity extends BaseContentsActivity {

    @Override
    public String getBaseUrl(List<Menu> menuList, int position) {
        return menuList.get(position).getUrl();
    }

    @Override
    public Class getTargetDetailActivity() {
        return MM131DetailActivity.class;
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
    public Map<parameter, Object> getContent(String baseUrl, String currentUrl) {
        Map<parameter, Object> resultMap = new HashMap<>();
        List<BaseInfo> data = new ArrayList<>();

        Request request = new Request.Builder()
                .url(currentUrl)
                .build();
        try {
            Response response = OkHttpClientUtil.getInstance().newCall(request).execute();
            byte[] result = response.body().bytes();
            Document document = Jsoup.parse(new String(result, "gbk"));
            Elements elements = document.select("dd a img:not([border])");
            for (Element element : elements) {
                BaseInfo temp = new BaseInfo();
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

        resultMap.put(parameter.CURRENT_URL, currentUrl);
        resultMap.put(parameter.RESULT, data);
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
}
