package com.lanyuan.picking.config;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Environment;
import android.preference.PreferenceManager;

import java.util.Map;

public class AppConfig {

    public static final String DOWNLOAD_PATH = Environment.getExternalStorageDirectory().getAbsolutePath() + "/Papaya/";

    public static final String DEFAULT_COLOR = "default";
    public static final String BLUE_COLOR = "blue";
    public static final String ORANGE_COLOR = "orange";
    public static final String BROWN_COLOR = "brown";

    public static final String choose_theme = "chooseTheme";
    public static final String load_pic_swipe = "loadPicSwipe";
    public static final String click_to_back = "clickToBack";
    public static final String download_path = "downloadPath";
    public static final String cache_size = "cacheSize";
    public static final String share_model = "shareModel";

}
