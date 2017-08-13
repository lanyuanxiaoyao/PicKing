package com.lanyuan.picking.pattern.girls;

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
        menuList.add(new Menu("国内套图", "https://www.aitaotu.com/guonei/"));
        menuList.add(new Menu("日韩套图", "https://www.aitaotu.com/rihan/"));
        menuList.add(new Menu("港台套图", "https://www.aitaotu.com/gangtai/"));
        menuList.add(new Menu("美女大全", "https://www.aitaotu.com/meinv/"));
        menuList.add(new Menu("AISS爱丝", "https://www.aitaotu.com/tag/aiss.html"));
        menuList.add(new Menu("尤果网", "https://www.aitaotu.com/tag/youguowang.html"));
        menuList.add(new Menu("推女郎", "https://www.aitaotu.com/tag/tuinvlang.html"));
        menuList.add(new Menu("ROSI", "https://www.aitaotu.com/tag/rosi.html"));
        menuList.add(new Menu("推女神", "https://www.aitaotu.com/tag/tuinvshen.html"));
        menuList.add(new Menu("西西人体", "https://www.aitaotu.com/tag/xixiwang.html"));
        menuList.add(new Menu("头条女神", "https://www.aitaotu.com/tag/ttns.html"));
        menuList.add(new Menu("秀人网", "https://www.aitaotu.com/tag/xiurenwang.html"));
        menuList.add(new Menu("PANS", "https://www.aitaotu.com/tag/pansidong.html"));
        menuList.add(new Menu("DDY Pantyhose", "https://www.aitaotu.com/tag/ddy.html"));
        menuList.add(new Menu("美媛馆", "https://www.aitaotu.com/tag/meiyuanguan.html"));
        menuList.add(new Menu("丽柜", "https://www.aitaotu.com/tag/ligui.html"));
        menuList.add(new Menu("美腿宝贝", "https://www.aitaotu.com/tag/meituibaobei.html"));
        menuList.add(new Menu("beautyleg", "https://www.aitaotu.com/tag/beautyleg.html"));
        menuList.add(new Menu("假面女皇", "https://www.aitaotu.com/tag/jiamiannvhuang.html"));
        menuList.add(new Menu("魅妍社", "https://www.aitaotu.com/tag/meiyanshe.html"));
        menuList.add(new Menu("东莞V女郎", "https://www.aitaotu.com/tag/vnvlang.html"));
        menuList.add(new Menu("爱蜜社", "https://www.aitaotu.com/tag/aimishe.html"));
        menuList.add(new Menu("3agirl", "https://www.aitaotu.com/tag/3agirl.html"));
        menuList.add(new Menu("RU1MM", "https://www.aitaotu.com/tag/ruyixiezhen.html"));
        menuList.add(new Menu("丝宝", "https://www.aitaotu.com/tag/sibao.html"));
        menuList.add(new Menu("DISI", "https://www.aitaotu.com/tag/disi.html"));
        menuList.add(new Menu("丝间舞", "https://www.aitaotu.com/tag/sijianwu.html"));
        menuList.add(new Menu("HeiSiAi写真", "https://www.aitaotu.com/tag/HeiSiAi.html"));
        menuList.add(new Menu("波萝社", "https://www.aitaotu.com/tag/boluoshe.html"));
        menuList.add(new Menu("蜜桃社", "https://www.aitaotu.com/tag/mitaoshe.html"));
        menuList.add(new Menu("ISHOW爱秀", "https://www.aitaotu.com/tag/aixiu.html"));
        menuList.add(new Menu("Leghacker", "https://www.aitaotu.com/tag/Leghacker.html"));
        menuList.add(new Menu("动感之星", "https://www.aitaotu.com/tag/dongganzhixing.html"));
        menuList.add(new Menu("MFStar", "https://www.aitaotu.com/tag/MFStar.html"));
        menuList.add(new Menu("赤足者", "https://www.aitaotu.com/tag/chizuzhe.html"));
        menuList.add(new Menu("丝魅VIP", "https://www.aitaotu.com/tag/simeivip.html"));
        menuList.add(new Menu("拍美VIP", "https://www.aitaotu.com/tag/paimei.html"));
        menuList.add(new Menu("尤物馆", "https://www.aitaotu.com/tag/youwuguan.html"));
        menuList.add(new Menu("FEILIN", "https://www.aitaotu.com/tag/feilin.html"));
        menuList.add(new Menu("唐韵", "https://www.aitaotu.com/tag/tangyun.html"));
        menuList.add(new Menu("优星馆", "https://www.aitaotu.com/tag/youxingguan.html"));
        menuList.add(new Menu("NICE-LEG", "https://www.aitaotu.com/tag/niceleg.html"));
        menuList.add(new Menu("上海炫彩摄影", "https://www.aitaotu.com/tag/shanghaixuancai.html"));
        menuList.add(new Menu("颜女神", "https://www.aitaotu.com/tag/yannvshen.html"));
        menuList.add(new Menu("美秀", "https://www.aitaotu.com/tag/meixiu.html"));
        menuList.add(new Menu("飞图网", "https://www.aitaotu.com/tag/feituwang.html"));
        menuList.add(new Menu("Tyingart", "https://www.aitaotu.com/tag/Tyingart.html"));
        menuList.add(new Menu("克拉女神", "https://www.aitaotu.com/tag/kelanvshen.html"));
        menuList.add(new Menu("糖丝Tangs", "https://www.aitaotu.com/tag/tangsi.html"));
        menuList.add(new Menu("影私荟", "https://www.aitaotu.com/tag/yingsihui.html"));
        menuList.add(new Menu("希威社", "https://www.aitaotu.com/tag/xiweisha.html"));
        menuList.add(new Menu("星乐园", "https://www.aitaotu.com/tag/xingleyuan.html"));
        menuList.add(new Menu("51MODO杂志", "https://www.aitaotu.com/tag/51modozazhi.html"));
        menuList.add(new Menu("MoKi筱嘤", "https://www.aitaotu.com/tag/moki.html"));
        menuList.add(new Menu("TASTE", "https://www.aitaotu.com/tag/taste.html"));
        menuList.add(new Menu("天使攝影", "https://www.aitaotu.com/tag/tianshisheying.html"));
        menuList.add(new Menu("中国腿模", "https://www.aitaotu.com/tag/zhongguotuimo.html"));
        menuList.add(new Menu("大名模网", "https://www.aitaotu.com/tag/damingmowang.html"));
        menuList.add(new Menu("花の颜", "https://www.aitaotu.com/tag/huayan.html"));
        menuList.add(new Menu("青豆客", "https://www.aitaotu.com/tag/qingdouke.html"));
        menuList.add(new Menu("尤蜜荟", "https://www.aitaotu.com/tag/youmihui.html"));
        menuList.add(new Menu("御女郎", "https://www.aitaotu.com/tag/yunvlang.html"));
        menuList.add(new Menu("丝尚写真", "https://www.aitaotu.com/tag/sishangxiezhen.html"));
        menuList.add(new Menu("糖果画报", "https://www.aitaotu.com/tag/tangguohuabao.html"));
        menuList.add(new Menu("girlt果团网", "https://www.aitaotu.com/tag/girlt.html"));
        menuList.add(new Menu("美女手机壁纸", "https://www.aitaotu.com/sjbz/mnsjbz/"));
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
