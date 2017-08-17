package com.lanyuan.picking.pattern.anime;

import android.graphics.Color;
import android.util.Log;

import com.lanyuan.picking.common.bean.AlbumInfo;
import com.lanyuan.picking.common.bean.PicInfo;
import com.lanyuan.picking.pattern.SinglePicturePattern;
import com.lanyuan.picking.ui.contents.ContentsActivity;
import com.lanyuan.picking.ui.menu.Menu;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

public class ZeroChan implements SinglePicturePattern {
    @Override
    public String getCategoryCoverUrl() {
        return "https://raw.githubusercontent.com/lanyuanxiaoyao/GitGallery/master/header-1.png";
    }

    @Override
    public int getBackgroundColor() {
        return Color.rgb(109, 81, 103);
    }

    @Override
    public String getBaseUrl(List<Menu> menuList, int position) {
        if (menuList == null)
            return "http://www.zerochan.net";
        return menuList.get(position).getUrl();
    }

    @Override
    public List<Menu> getMenuList() {
        List<Menu> menuList = new ArrayList<>();
        menuList.add(new Menu("All", "http://www.zerochan.net/?p=1"));
        menuList.add(new Menu("ACCA: 13-ku Kansatsu-ka", "http://www.zerochan.net/ACCA%3A+13-ku+Kansatsu-ka"));
        menuList.add(new Menu("Akatsuki no Yona", "http://www.zerochan.net/Akatsuki+no+Yona"));
        menuList.add(new Menu("Ardyn Izunia", "http://www.zerochan.net/Ardyn+Izunia"));
        menuList.add(new Menu("Bakugou Katsuki", "http://www.zerochan.net/Bakugou+Katsuki"));
        menuList.add(new Menu("Boku no Hero Academia", "http://www.zerochan.net/Boku+no+Hero+Academia"));
        menuList.add(new Menu("Caster (Nero Claudius)", "http://www.zerochan.net/Caster+%28Nero+Claudius%29"));
        menuList.add(new Menu("Fate/Apocrypha", "http://www.zerochan.net/Fate%2FApocrypha"));
        menuList.add(new Menu("Fate/Grand Order", "http://www.zerochan.net/Fate%2FGrand+Order"));
        menuList.add(new Menu("Final Fantasy XV", "http://www.zerochan.net/Final+Fantasy+XV"));
        menuList.add(new Menu("Gin Tama", "http://www.zerochan.net/Gin+Tama"));
        menuList.add(new Menu("Hatsune Miku", "http://www.zerochan.net/Hatsune+Miku"));
        menuList.add(new Menu("Ignis Scientia", "http://www.zerochan.net/Ignis+Scientia"));
        menuList.add(new Menu("Jean Otus", "http://www.zerochan.net/Jean+Otus"));
        menuList.add(new Menu("Joan of Arc (Fate/Apocrypha)", "http://www.zerochan.net/Joan+of+Arc+%28Fate%2FApocrypha%29"));
        menuList.add(new Menu("JoJo no Kimyou na Bouken", "http://www.zerochan.net/JoJo+no+Kimyou+na+Bouken"));
        menuList.add(new Menu("Kakyoin Noriaki", "http://www.zerochan.net/Kakyoin+Noriaki"));
        menuList.add(new Menu("Kantai Collection", "http://www.zerochan.net/Kantai+Collection"));
        menuList.add(new Menu("KR0NPR1NZ", "http://www.zerochan.net/KR0NPR1NZ"));
        menuList.add(new Menu("Kuujou Joutarou", "http://www.zerochan.net/Kuujou+Joutarou"));
        menuList.add(new Menu("Little Witch Academia", "http://www.zerochan.net/Little+Witch+Academia"));
        menuList.add(new Menu("NARUTO", "http://www.zerochan.net/NARUTO"));
        menuList.add(new Menu("NieR: Automata", "http://www.zerochan.net/NieR%3A+Automata"));
        menuList.add(new Menu("Noctis Lucis Caelum", "http://www.zerochan.net/Noctis+Lucis+Caelum"));
        menuList.add(new Menu("Pixiv Id 736058", "http://www.zerochan.net/Pixiv+Id+736058"));
        menuList.add(new Menu("Pokémon", "http://www.zerochan.net/Pok%C3%A9mon"));
        menuList.add(new Menu("Pokémon SPECIAL", "http://www.zerochan.net/Pok%C3%A9mon+SPECIAL"));
        menuList.add(new Menu("Ravus Nox Fleuret", "http://www.zerochan.net/Ravus+Nox+Fleuret"));
        menuList.add(new Menu("Re:Zero Kara Hajimeru Isekai Seikatsu", "http://www.zerochan.net/Re%3AZero+Kara+Hajimeru+Isekai+Seikatsu"));
        menuList.add(new Menu("Re:Zero kara Hajimeru Isekai Seikatsu Ex", "http://www.zerochan.net/Re%3AZero+kara+Hajimeru+Isekai+Seikatsu+Ex"));
        menuList.add(new Menu("Rem (Re:Zero)", "http://www.zerochan.net/Rem+%28Re%3AZero%29"));
        menuList.add(new Menu("Rokudenashi Majutsu Koushi to Kinki Kyouten", "http://www.zerochan.net/Rokudenashi+Majutsu+Koushi+to+Kinki+Kyouten"));
        menuList.add(new Menu("Saber Alter", "http://www.zerochan.net/Saber+Alter"));
        menuList.add(new Menu("Sakata Gintoki", "http://www.zerochan.net/Sakata+Gintoki"));
        menuList.add(new Menu("Senju Hashirama", "http://www.zerochan.net/Senju+Hashirama"));
        menuList.add(new Menu("Touhou", "http://www.zerochan.net/Touhou"));
        menuList.add(new Menu("Touken Ranbu", "http://www.zerochan.net/Touken+Ranbu"));
        menuList.add(new Menu("Vatican Kiseki Chousakan", "http://www.zerochan.net/Vatican+Kiseki+Chousakan"));
        menuList.add(new Menu("Watanabe You", "http://www.zerochan.net/Watanabe+You"));
        menuList.add(new Menu("Wlop", "http://www.zerochan.net/Wlop"));
        menuList.add(new Menu("Yuri!!! On Ice", "http://www.zerochan.net/Yuri%21%21%21+On+Ice"));
        return menuList;
    }

    @Override
    public Map<ContentsActivity.parameter, Object> getContent(String baseUrl, String currentUrl, byte[] result, Map<ContentsActivity.parameter, Object> resultMap) throws UnsupportedEncodingException {
        if (Pattern.matches("http://www\\.zerochan\\.net/\\?p.*", baseUrl))
            baseUrl = "http://www.zerochan.net";
        List<AlbumInfo> data = new ArrayList<>();
        Document document = Jsoup.parse(new String(result, "utf-8"));
        Elements elements = document.select("ul#thumbs2 li > a:has(img)");
        for (Element element : elements) {
            AlbumInfo temp = new AlbumInfo();
            Log.e("ZeroChan", "getContent: " + baseUrl + "/full" + element.attr("href"));
            temp.setAlbumUrl(baseUrl + "/full" + element.attr("href"));
            Elements elements1 = element.select("img");
            if (elements1.size() > 0)
                temp.setCoverUrl(elements1.get(0).attr("src"));
            data.add(temp);
        }

        resultMap.put(ContentsActivity.parameter.CURRENT_URL, currentUrl);
        resultMap.put(ContentsActivity.parameter.RESULT, data);
        return resultMap;
    }

    @Override
    public String getContentNext(String baseUrl, String currentUrl, byte[] result) throws UnsupportedEncodingException {
        Document document = Jsoup.parse(new String(result, "utf-8"));
        Elements elements = document.select("p.pagination a[rel=next]");
        if (elements.size() > 0) {
            return baseUrl + elements.get(0).attr("href");
        }
        return "";
    }

    @Override
    public PicInfo getSinglePicContent(String baseUrl, String currentUrl, byte[] result) throws UnsupportedEncodingException {
        Document document = Jsoup.parse(new String(result, "utf-8"));
        PicInfo info = new PicInfo();
        Elements elements = document.select("div#fullsize img");
        if (elements.size() > 0) {
            info.setPicUrl(elements.get(0).attr("src"));
        }
        return info;
    }

}
