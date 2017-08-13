package com.lanyuan.picking.pattern.anime;

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
        menuList.add(new Menu("卡通动漫", "https://www.aitaotu.com/dongman/"));
        menuList.add(new Menu("火影忍者", "https://www.aitaotu.com/dmtp/hyrz/"));
        menuList.add(new Menu("妖精的尾巴", "https://www.aitaotu.com/dmtp/yjdwb/"));
        menuList.add(new Menu("海贼王", "https://www.aitaotu.com/dmtp/hzw/"));
        menuList.add(new Menu("东京食尸鬼", "https://www.aitaotu.com/dmtp/djssg/"));
        menuList.add(new Menu("死神", "https://www.aitaotu.com/dmtp/sishen/"));
        menuList.add(new Menu("秦时明月", "https://www.aitaotu.com/dmtp/qsmy/"));
        menuList.add(new Menu("刀剑神域", "https://www.aitaotu.com/dmtp/djsy/"));
        menuList.add(new Menu("名侦探柯南", "https://www.aitaotu.com/dmtp/mztkn/"));
        menuList.add(new Menu("英雄联盟", "https://www.aitaotu.com/yxtp/yxlm/"));
        menuList.add(new Menu("穿越火线", "https://www.aitaotu.com/yxtp/cyhx/"));
        menuList.add(new Menu("动漫手机壁纸", "https://www.aitaotu.com/sjbz/dmsjbz/"));
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
            if (elements1.size() > 0)
                temp.setCoverUrl(elements1.get(0).attr("data-original"));
            urls.add(temp);
        }

        // 方案2
        if (urls.size() == 0) {
            Elements elements2 = document.select("#mainbody a:has(img)");
            for (Element element : elements2) {
                AlbumInfo temp = new AlbumInfo();
                temp.setAlbumUrl(baseUrl + element.attr("href"));
                Elements elements3 = element.select("img");
                if (elements3.size() > 0)
                    temp.setCoverUrl(elements3.get(0).attr("data-original"));
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
        Elements elements = document.select("#big-pic img");
        for (Element element : elements) {
            urls.add(new PicInfo(element.attr("src")));
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
