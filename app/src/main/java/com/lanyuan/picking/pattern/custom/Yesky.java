package com.lanyuan.picking.pattern.custom;

import android.graphics.Color;
import android.util.Log;

import com.lanyuan.picking.common.AlbumInfo;
import com.lanyuan.picking.pattern.BasePattern;
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

public class Yesky implements BasePattern {
    @Override
    public String getCategoryCoverUrl() {
        return "http://www.yesky.com/TLimages2009/yesky/images/pic2015/pic_logo.jpg";
    }

    @Override
    public int getBackgroundColor() {
        return Color.rgb(162, 58, 137);
    }

    @Override
    public String getBaseUrl(List<Menu> menuList, int position) {
        return "http://pic.yesky.com";
    }

    @Override
    public List<Menu> getMenuList() {
        List<Menu> menuList = new ArrayList<>();
        menuList.add(new Menu("cosplay美女", "http://pic.yesky.com/c/6_18332.shtml"));
        menuList.add(new Menu("chinajoy", "http://pic.yesky.com/c/6_19691.shtml"));
        menuList.add(new Menu("中国女明星", "http://pic.yesky.com/c/6_3655.shtml"));
        menuList.add(new Menu("韩国女明星", "http://pic.yesky.com/c/6_3663.shtml"));
        menuList.add(new Menu("欧美女明星", "http://pic.yesky.com/c/6_3659.shtml"));
        menuList.add(new Menu("青豆客", "http://pic.yesky.com/c/6_22231.shtml"));
        menuList.add(new Menu("克拉女神", "http://pic.yesky.com/c/6_23031.shtml"));
        menuList.add(new Menu("写真摄影", "http://pic.yesky.com/c/6_22812.shtml"));
        menuList.add(new Menu("尤果网", "http://pic.yesky.com/c/6_22171.shtml"));
        menuList.add(new Menu("气质美女", "http://pic.yesky.com/c/6_20472.shtml"));
        menuList.add(new Menu("街拍美女", "http://pic.yesky.com/c/6_20477.shtml"));
        menuList.add(new Menu("非主流", "http://pic.yesky.com/c/6_20474.shtml"));
        menuList.add(new Menu("性感美腿", "http://pic.yesky.com/c/6_20498.shtml"));
        menuList.add(new Menu("清纯美女", "http://pic.yesky.com/c/6_20471.shtml"));
        menuList.add(new Menu("性感美女", "http://pic.yesky.com/c/6_20771.shtml"));
        menuList.add(new Menu("日韩美女", "http://pic.yesky.com/c/6_22151.shtml"));
        menuList.add(new Menu("性感模特", "http://pic.yesky.com/c/6_20671.shtml"));
        menuList.add(new Menu("女神", "http://pic.yesky.com/c/6_61091.shtml"));
        menuList.add(new Menu("美女魅惑", "http://pic.yesky.com/c/6_20491.shtml"));
        menuList.add(new Menu("PANS写真", "http://pic.yesky.com/c/6_22591.shtml"));
        menuList.add(new Menu("女色图片", "http://pic.yesky.com/c/6_61100.shtml"));
        menuList.add(new Menu("霓裳", "http://pic.yesky.com/c/6_61103.shtml"));
        menuList.add(new Menu("妆容", "http://pic.yesky.com/c/6_61107.shtml"));
        menuList.add(new Menu("妆容", "http://pic.yesky.com/c/6_61107.shtml"));
        return menuList;
    }

    @Override
    public boolean isSinglePic() {
        return false;
    }

    @Override
    public Map<ContentsActivity.parameter, Object> getContent(String baseUrl, String currentUrl, byte[] result, Map<ContentsActivity.parameter, Object> resultMap) throws UnsupportedEncodingException {
        List<AlbumInfo> data = new ArrayList<>();
        Document document = Jsoup.parse(new String(result, "gb2312"));
        Elements elements = document.select(".lb_box dt a");
        for (Element element : elements) {
            AlbumInfo temp = new AlbumInfo();
            temp.setAlbumUrl(element.attr("href"));
            Elements elements1 = element.select("img");
            if (elements1.size() > 0)
                temp.setCoverUrl(elements1.get(0).attr("src"));
            data.add(temp);
        }
        if (data.size() == 0) {
            Elements elements1 = document.select(".mode_box dt a");
            for (Element element : elements1) {
                AlbumInfo temp = new AlbumInfo();
                temp.setAlbumUrl(element.attr("href"));
                Elements elements2 = element.select("img");
                if (elements2.size() > 0)
                    temp.setCoverUrl(elements2.get(0).attr("src"));
                data.add(temp);
            }
        }

        resultMap.put(ContentsActivity.parameter.CURRENT_URL, currentUrl);
        resultMap.put(ContentsActivity.parameter.RESULT, data);
        return resultMap;
    }

    @Override
    public String getContentNext(String baseUrl, String currentUrl, byte[] result) throws UnsupportedEncodingException {
        Document document = Jsoup.parse(new String(result, "gb2312"));
        Elements elements = document.select(".flym font a:containsOwn(下一页)");
        if (elements.size() > 0) {
            Log.e("Yesky", "getContentNext: " + baseUrl + elements.get(0).attr("href"));
            return baseUrl + elements.get(0).attr("href");
        }
        return "";
    }

    @Override
    public String getSinglePicContent(String baseUrl, String currentUrl, byte[] result) {
        return null;
    }

    @Override
    public Map<DetailActivity.parameter, Object> getDetailContent(String baseUrl, String currentUrl, byte[] result, Map<DetailActivity.parameter, Object> resultMap) throws UnsupportedEncodingException {
        List<String> data = new ArrayList<>();
        Document document = Jsoup.parse(new String(result, "gb2312"));
        Elements elements = document.select(".l_effect_img_mid img");
        if (elements.size() > 0)
            data.add(elements.get(0).attr("src"));

        resultMap.put(DetailActivity.parameter.CURRENT_URL, currentUrl);
        resultMap.put(DetailActivity.parameter.RESULT, data);
        return resultMap;
    }

    @Override
    public String getDetailNext(String baseUrl, String currentUrl, byte[] result) throws UnsupportedEncodingException {
        Document document = Jsoup.parse(new String(result, "gb2312"));
        Elements elements = document.select(".l_effect_bottom li a");
        if (elements.size() > 0)
            if (elements.get(elements.size() - 1).attr("href").equals(currentUrl))
                return "";
        Elements elements1 = document.select(".l_effect_img_mid a");
        if (elements1.size() > 0)
            return elements1.get(0).attr("href");
        return "";
    }
}
