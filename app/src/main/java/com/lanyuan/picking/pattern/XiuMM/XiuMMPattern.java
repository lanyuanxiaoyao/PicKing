package com.lanyuan.picking.pattern.XiuMM;

import com.lanyuan.picking.common.ContentsActivity;
import com.lanyuan.picking.common.DetailActivity;
import com.lanyuan.picking.common.AlbumInfo;
import com.lanyuan.picking.common.Menu;
import com.lanyuan.picking.pattern.BasePattern;
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

import okhttp3.Call;
import okhttp3.Request;
import okhttp3.Response;

public class XiuMMPattern implements BasePattern {
    @Override
    public String getBaseUrl(List<Menu> menuList, int position) {
        return "http://www.xiumm.org/";
    }

    @Override
    public List<Menu> getMenuList() {
        List<Menu> menuList = new ArrayList<>();
        menuList.add(new Menu("尤果", "http://www.xiumm.org/albums/UGirls.html"));
        menuList.add(new Menu("菠萝社", "http://www.xiumm.org/albums/BoLoli.html"));
        menuList.add(new Menu("萌缚", "http://www.xiumm.org/albums/MF.html"));
        menuList.add(new Menu("魅妍社", "http://www.xiumm.org/albums/MiStar.html"));
        menuList.add(new Menu("爱蜜社", "http://www.xiumm.org/albums/imiss.html"));
        menuList.add(new Menu("嗲囡囡", "http://www.xiumm.org/albums/FeiLin.html"));
        menuList.add(new Menu("优星馆", "http://www.xiumm.org/albums/UXING.html"));
        menuList.add(new Menu("模范学院", "http://www.xiumm.org/albums/MFStar.html"));
        menuList.add(new Menu("美媛馆", "http://www.xiumm.org/albums/MyGirl.html"));
        menuList.add(new Menu("秀人网", "http://www.xiumm.org/albums/XiuRen.html"));
        menuList.add(new Menu("V女郎", "http://www.xiumm.org/albums/vgirl.html"));
        menuList.add(new Menu("青豆客", "http://www.xiumm.org/albums/TGod.html"));
        menuList.add(new Menu("顽味生活", "http://www.xiumm.org/albums/Taste.html"));
        menuList.add(new Menu("DK御女郎", "http://www.xiumm.org/albums/DKGirl.html"));
        menuList.add(new Menu("薄荷叶", "http://www.xiumm.org/albums/MintYe.html"));
        menuList.add(new Menu("糖果画报", "http://www.xiumm.org/albums/CANDY.html"));
        menuList.add(new Menu("糖果画报", "http://www.xiumm.org/albums/CANDY.html"));
        menuList.add(new Menu("尤蜜荟", "http://www.xiumm.org/albums/YOUMI.html"));
        menuList.add(new Menu("猫萌榜", "http://www.xiumm.org/albums/MICAT.html"));
        return menuList;
    }

    @Override
    public Map<ContentsActivity.parameter, Object> getContent(String baseUrl, String currentUrl) {
        Map<ContentsActivity.parameter, Object> resultMap = new HashMap<>();
        List<AlbumInfo> urls = new ArrayList<>();

        Request request = new Request.Builder()
                .url(currentUrl)
                .build();
        try {
            Response response = OkHttpClientUtil.getInstance().newCall(request).execute();
            byte[] result = response.body().bytes();
            Document document = Jsoup.parse(new String(result, "utf-8"));
            Elements elements = document.select(".pic_box a");
            for (Element element : elements) {
                AlbumInfo temp = new AlbumInfo();
                temp.setAlbumUrl(element.attr("href"));
                Elements elements1 = element.select("img");
                if (elements1.size() > 0)
                    temp.setCoverUrl(elements1.get(0).attr("src"));
                urls.add(temp);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        resultMap.put(ContentsActivity.parameter.CURRENT_URL, currentUrl);
        resultMap.put(ContentsActivity.parameter.RESULT, urls);
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
            Elements elements = document.select(".paginator span.next a");
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
            Document document = Jsoup.parse(new String(result, "utf-8"));
            Elements elements = document.select(".gallary_item .pic_box img");
            for (Element element : elements) {
                urls.add(baseUrl + element.attr("src"));
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
            Call call = OkHttpClientUtil.getInstance().newCall(request);
            Response response = call.execute();
            byte[] result = response.body().bytes();
            Document document = Jsoup.parse(new String(result, "utf-8"));
            Elements elements = document.select(".paginator span.next a");
            if (elements.size() > 0)
                return baseUrl + elements.get(0).attr("href");
        } catch (IOException e) {
            e.printStackTrace();
        }
        return "";
    }
}
