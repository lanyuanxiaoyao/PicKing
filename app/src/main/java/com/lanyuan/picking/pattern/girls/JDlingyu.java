package com.lanyuan.picking.pattern.girls;

import android.graphics.Color;

import com.lanyuan.picking.common.bean.AlbumInfo;
import com.lanyuan.picking.common.bean.PicInfo;
import com.lanyuan.picking.pattern.MultiPicturePattern;
import com.lanyuan.picking.pattern.Searchable;
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

public class JDlingyu implements MultiPicturePattern, Searchable {
    @Override
    public String getWebsiteName() {
        return "绝对领域";
    }

    @Override
    public String getCategoryCoverUrl() {
        return "http://www.jdlingyu.wang/wp-content/uploads/2017/01/2017-01-07_20-57-14.png";
    }

    @Override
    public int getBackgroundColor() {
        return Color.LTGRAY;
    }

    @Override
    public String getBaseUrl(List<Menu> menuList, int position) {
        return "http://www.jdlingyu.wang/";
    }

    @Override
    public List<Menu> getMenuList() {
        List<Menu> menuList = new ArrayList<>();
        menuList.add(new Menu("主页", "http://www.jdlingyu.wang/"));
        menuList.add(new Menu("自拍", "http://www.jdlingyu.wang/%E8%87%AA%E6%8B%8D/"));
        menuList.add(new Menu("独家", "http://www.jdlingyu.wang/%E4%B8%93%E9%A2%98/%E7%8B%AC%E5%AE%B6//"));
        menuList.add(new Menu("胖次", "http://www.jdlingyu.wang/%E8%83%96%E6%AC%A1/"));
        menuList.add(new Menu("丝袜", "http://www.jdlingyu.wang/%E7%89%B9%E7%82%B9/%E4%B8%9D%E8%A2%9C/"));
        menuList.add(new Menu("汉服", "http://www.jdlingyu.wang/%E7%89%B9%E7%82%B9/%E6%B1%89%E6%9C%8D/"));
        menuList.add(new Menu("死库水", "http://www.jdlingyu.wang/%E7%89%B9%E7%82%B9/%E6%AD%BB%E5%BA%93%E6%B0%B4/"));
        menuList.add(new Menu("体操服", "http://www.jdlingyu.wang/%E7%89%B9%E7%82%B9/%E4%BD%93%E6%93%8D%E6%9C%8D/"));
        menuList.add(new Menu("女仆装", "http://www.jdlingyu.wang/%E7%89%B9%E7%82%B9/%E5%A5%B3%E4%BB%86%E8%A3%85/"));
        menuList.add(new Menu("水手服", "http://www.jdlingyu.wang/%E7%89%B9%E7%82%B9/%E6%B0%B4%E6%89%8B%E6%9C%8D/"));
        menuList.add(new Menu("和服浴衣", "http://www.jdlingyu.wang/%E7%89%B9%E7%82%B9/%E5%92%8C%E6%9C%8D%E6%B5%B4%E8%A1%A3/"));
        menuList.add(new Menu("束缚", "http://www.jdlingyu.wang/%E5%BC%84%E6%BD%AE/%E6%9D%9F%E7%BC%9A/"));
        menuList.add(new Menu("妹子图", "http://www.jdlingyu.wang/mzitu/"));
        menuList.add(new Menu("Hentai好物", "http://www.jdlingyu.wang/hentai/"));
        menuList.add(new Menu("二次元", "http://www.jdlingyu.wang/acg/"));
        return menuList;
    }

    @Override
    public Map<ContentsActivity.parameter, Object> getContent(String baseUrl, String currentUrl, byte[] result, Map<ContentsActivity.parameter, Object> resultMap) throws UnsupportedEncodingException {
        List<AlbumInfo> urls = new ArrayList<>();
        Document document = Jsoup.parse(new String(result, "utf-8"));
        Elements elements = document.select("#postlist .pin");
        for (Element element : elements) {
            AlbumInfo temp = new AlbumInfo();

            Elements album = element.select(".pin-coat>a");
            temp.setAlbumUrl(album.attr("href"));
            Elements pic = album.select("img");
            if (pic.size() > 0) {
                temp.setPicUrl(pic.get(0).attr("original"));
            }

            Elements title = element.select("span.bg");
            if (title.size() > 0)
                temp.setTitle(title.get(0).text());

            Elements time = element.select("span.timer");
            if (time.size() > 0)
                temp.setTime(time.get(0).text());

            urls.add(temp);
        }
        resultMap.put(ContentsActivity.parameter.CURRENT_URL, currentUrl);
        resultMap.put(ContentsActivity.parameter.RESULT, urls);
        return resultMap;
    }

    @Override
    public String getContentNext(String baseUrl, String currentUrl, byte[] result) throws UnsupportedEncodingException {
        Document document = Jsoup.parse(new String(result, "utf-8"));
        Elements elements = document.select("#pagenavi a:containsOwn(下一页)");
        if (elements.size() > 0)
            return elements.get(0).attr("href");
        return "";
    }

    @Override
    public Map<DetailActivity.parameter, Object> getDetailContent(String baseUrl, String currentUrl, byte[] result, Map<DetailActivity.parameter, Object> resultMap) throws UnsupportedEncodingException {
        List<PicInfo> urls = new ArrayList<>();
        Document document = Jsoup.parse(new String(result, "utf-8"));

        Elements title = document.select("h2.main-title");
        String sTitle = "";
        if (title.size() > 0)
            sTitle = title.get(0).text();

        Elements time = document.select("div.main-header span.post-span");
        String sTime = "";
        if (time.size() > 0)
            sTime = time.get(0).text();

        Elements elements = document.select(".main-body a:has(img)");
        for (Element element : elements) {
            urls.add(new PicInfo(element.attr("href")).setTitle(sTitle).setTime(sTime));
        }
        resultMap.put(DetailActivity.parameter.CURRENT_URL, currentUrl);
        resultMap.put(DetailActivity.parameter.RESULT, urls);
        return resultMap;
    }

    @Override
    public String getDetailNext(String baseUrl, String currentUrl, byte[] result) throws UnsupportedEncodingException {
        return null;
    }

    @Override
    public String getSearch(String query) {
        return "http://www.jdlingyu.wang/?s=" + query;
    }
}
