package com.lanyuan.picking.pattern.others;

import android.graphics.Color;

import com.lanyuan.picking.common.bean.AlbumInfo;
import com.lanyuan.picking.common.bean.PicInfo;
import com.lanyuan.picking.pattern.MultiPicturePattern;
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

public class Aitaotu implements MultiPicturePattern {
    @Override
    public String getWebsiteName() {
        return "爱套图";
    }

    @Override
    public String getCategoryCoverUrl() {
        return "https://www.aitaotu.com/Style/img/newlogo.png";
    }

    @Override
    public int getBackgroundColor() {
        return Color.rgb(158, 195, 255);
    }

    @Override
    public String getBaseUrl(List<Menu> menuList, int position) {
        return "https://www.aitaotu.com";
    }

    @Override
    public List<Menu> getMenuList() {
        List<Menu> menuList = new ArrayList<>();
        menuList.add(new Menu("欧美明星", "https://www.aitaotu.com/mxtp/ommx/"));
        menuList.add(new Menu("日韩明星", "https://www.aitaotu.com/mxtp/rhmx/"));
        menuList.add(new Menu("港台明星", "https://www.aitaotu.com/mxtp/gtmx/"));
        menuList.add(new Menu("大陆明星", "https://www.aitaotu.com/mxtp/dlmx/"));
        menuList.add(new Menu("帅哥图片", "https://www.aitaotu.com/shuaige/"));
        menuList.add(new Menu("唯美图片", "https://www.aitaotu.com/weimei/"));
        menuList.add(new Menu("伤感图片", "https://www.aitaotu.com/shanggan/"));
        menuList.add(new Menu("狗狗图片", "https://www.aitaotu.com/dwtp/ggtp/"));
        menuList.add(new Menu("萌猫图片", "https://www.aitaotu.com/dwtp/xmtp/"));
        menuList.add(new Menu("小鸟图片", "https://www.aitaotu.com/dwtp/xntp/"));
        menuList.add(new Menu("创意设计", "https://www.aitaotu.com/cysj/cytp/"));
        menuList.add(new Menu("QQ头像", "https://www.aitaotu.com/touxiang/"));
        menuList.add(new Menu("表情包", "https://www.aitaotu.com/cysj/bqb/"));
        menuList.add(new Menu("福彩3d走势图", "https://www.aitaotu.com/cysj/zst/"));
        menuList.add(new Menu("电影海报", "https://www.aitaotu.com/cysj/dyhb/"));
        menuList.add(new Menu("素描图片", "https://www.aitaotu.com/cysj/smtp/"));
        menuList.add(new Menu("唯美手机壁纸", "https://www.aitaotu.com/sjbz/wmsjbz/"));
        menuList.add(new Menu("苹果手机壁纸", "https://www.aitaotu.com/sjbz/pgsjbz/"));
        menuList.add(new Menu("手机动态壁纸", "https://www.aitaotu.com/sjbz/sjdtbz/"));
        menuList.add(new Menu("安卓手机壁纸", "https://www.aitaotu.com/sjbz/azsjbz/"));
        return menuList;
    }

    @Override
    public Map<ContentsActivity.parameter, Object> getContent(String baseUrl, String currentUrl, byte[] result, Map<ContentsActivity.parameter, Object> resultMap) throws UnsupportedEncodingException {
        List<AlbumInfo> urls = new ArrayList<>();
        Document document = Jsoup.parse(new String(result, "utf-8"));

        // 方案1
        Elements elements = document.select("div.img a:has(img)");
        for (Element element : elements) {
            AlbumInfo temp = new AlbumInfo();
            temp.setAlbumUrl(baseUrl + element.attr("href"));
            Elements elements1 = element.select("img");
            if (elements1.size() > 0) {
                temp.setPicUrl(elements1.get(0).attr("data-original"));
                if (temp.getPicUrl() == null || "".equals(temp.getPicUrl()))
                    temp.setPicUrl(elements1.get(0).attr("src"));
            }
            urls.add(temp);
        }

        // 方案2
        if (urls.size() == 0) {
            Elements elements2 = document.select("#mainbody a:has(img)");
            for (Element element : elements2) {
                AlbumInfo temp = new AlbumInfo();
                temp.setAlbumUrl(baseUrl + element.attr("href"));
                Elements elements3 = element.select("img");
                if (elements3.size() > 0) {
                    temp.setPicUrl(elements3.get(0).attr("data-original"));
                }
                urls.add(temp);
            }
        }

        resultMap.put(ContentsActivity.parameter.CURRENT_URL, currentUrl);
        resultMap.put(ContentsActivity.parameter.RESULT, urls);
        return resultMap;
    }

    @Override
    public String getContentNext(String baseUrl, String currentUrl, byte[] result) throws UnsupportedEncodingException {
        Document document = Jsoup.parse(new String(result, "utf-8"));
        Elements elements = document.select("#pageNum a:containsOwn(下一页)");
        if (elements.size() > 0)
            return baseUrl + elements.get(0).attr("href");
        return "";
    }

    @Override
    public Map<DetailActivity.parameter, Object> getDetailContent(String baseUrl, String currentUrl, byte[] result, Map<DetailActivity.parameter, Object> resultMap) throws UnsupportedEncodingException {
        List<PicInfo> urls = new ArrayList<>();
        Document document = Jsoup.parse(new String(result, "utf-8"));
        Elements title = document.select("#photos h1");
        String sTitle = "";
        if (title.size() > 0)
            sTitle = title.get(0).text();

        Elements time = document.select(".tsmaincont-desc span");
        String sTime = "";
        if (time.size() > 0)
            sTime = time.get(0).text();

        Elements elements = document.select("#big-pic img");
        for (Element element : elements) {
            urls.add(new PicInfo(element.attr("src")).setTitle(sTitle).setTime(sTime));
        }

        resultMap.put(DetailActivity.parameter.CURRENT_URL, currentUrl);
        resultMap.put(DetailActivity.parameter.RESULT, urls);
        return resultMap;
    }

    @Override
    public String getDetailNext(String baseUrl, String currentUrl, byte[] result) throws UnsupportedEncodingException {
        Document document = Jsoup.parse(new String(result, "utf-8"));
        Elements elements = document.select("#nl a");
        if (elements.size() > 0)
            return baseUrl + elements.get(0).attr("href");
        return "";
    }
}
