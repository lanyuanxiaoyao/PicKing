package com.lanyuan.picking.config;

import android.os.Environment;

import com.lanyuan.picking.pattern.BasePattern;
import com.lanyuan.picking.pattern.anime.AKabe;
import com.lanyuan.picking.pattern.anime.Acg12;
import com.lanyuan.picking.pattern.anime.AnimePic;
import com.lanyuan.picking.pattern.anime.AoJiao;
import com.lanyuan.picking.pattern.anime.Apic;
import com.lanyuan.picking.pattern.anime.E926;
import com.lanyuan.picking.pattern.anime.KonaChan;
import com.lanyuan.picking.pattern.anime.MiniTokyo;
import com.lanyuan.picking.pattern.anime.MoeimgNormal;
import com.lanyuan.picking.pattern.anime.SafeBooru;
import com.lanyuan.picking.pattern.anime.ZeroChan;
import com.lanyuan.picking.pattern.girls.GankIO;
import com.lanyuan.picking.pattern.girls.Meizi4493;
import com.lanyuan.picking.pattern.girls.Mntu92;
import com.lanyuan.picking.pattern.others.Aitaotu;
import com.lanyuan.picking.pattern.others.Nanrentu;
import com.lanyuan.picking.pattern.others.DuowanCos;
import com.lanyuan.picking.pattern.girls.JDlingyu;
import com.lanyuan.picking.pattern.girls.JianDan;
import com.lanyuan.picking.pattern.girls.MM131;
import com.lanyuan.picking.pattern.girls.RosiMM;
import com.lanyuan.picking.pattern.girls.XiuMM;
import com.lanyuan.picking.pattern.girls.Yesky;
import com.lanyuan.picking.pattern.sex.Yande;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AppConfig {

    public static final String DOWNLOAD_PATH = Environment.getExternalStorageDirectory().getAbsolutePath() + "/Pictures/Papaya/";

    public static final String DEFAULT_COLOR = "default";
    public static final String BLUE_COLOR = "blue";
    public static final String ORANGE_COLOR = "orange";
    public static final String BROWN_COLOR = "brown";
    public static final String PURPLE_COLOR = "purple";
    public static final String CYAN_COLOR = "cyan";
    public static final String GREEN_COLOR = "green";
    public static final String YELLOW_COLOR = "yellow";
    public static final String GREY_COLOR = "grey";

    public static final String version_code = "versionCode";

    public static final String show_tips = "showTips";
    public static final String choose_theme = "chooseTheme";
    public static final String load_pic_swipe = "loadPicSwipe";
    public static final String click_to_back = "clickToBack";
    public static final String download_path = "downloadPath";
    public static final String cache_size = "cacheSize";
    public static final String clean_cache = "cleanCache";
    public static final String share_model = "shareModel";
    public static final String hide_pic = "hidePic";
    public static final String auto_gif_play = "autoGifPlay";
    public static final String auto_load_more = "autoLoadMore";
    public static final String cols_detail = "colsDetail";

    public static final String ancdslknsdlnv = "anclsdncklsa";

    public static final String anime_patterns = "animePatterns";
    public static final String others_patterns = "boysPatterns";
    public static final String girls_patterns = "girlsPatterns";
    public static final String sex_patterns = "sexPatterns";

    public static Map categoryList;

    static {
        categoryList = new HashMap();
        List<BasePattern> animePatterns = new ArrayList<BasePattern>() {{
            add(new MoeimgNormal());
            add(new E926());
            add(new SafeBooru());
            add(new com.lanyuan.picking.pattern.anime.Aitaotu());
            add(new KonaChan());
            add(new Apic());
            // add(new Acg12());
            add(new AoJiao());
            add(new ZeroChan());
            add(new AKabe());
            add(new AnimePic());
            add(new MiniTokyo());
        }};
        List<BasePattern> girlsPatterns = new ArrayList<BasePattern>() {{
            add(new GankIO());
            add(new Meizi4493());
            add(new Mntu92());
            add(new com.lanyuan.picking.pattern.girls.Aitaotu());
            // add(new Mzitu());
            add(new JianDan());
            add(new JDlingyu());
            add(new MM131());
            add(new XiuMM());
            add(new RosiMM());
            add(new Yesky());
        }};
        List<BasePattern> othersPatterns = new ArrayList<BasePattern>() {{
            add(new Aitaotu());
            add(new Nanrentu());
            add(new DuowanCos());
        }};
        List<BasePattern> sexPatterns = new ArrayList<BasePattern>() {{
            add(new Yande());
        }};
        categoryList.put(anime_patterns, animePatterns);
        categoryList.put(girls_patterns, girlsPatterns);
        categoryList.put(others_patterns, othersPatterns);
    }

}
