package com.lanyuan.picking.config;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import java.util.Map;

public class AppConfig {

    private static Map map;

    public static void init(Context context) {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        setMap(sharedPreferences.getAll());
    }

    public static Object getByResourceId(Context context, int id, Object defaultValue) {
        Object o = getMap().get(context.getResources().getString(id));
        return o == null ? defaultValue : o;
    }

    public static Map getMap() {
        return map;
    }

    public static void setMap(Map map) {
        AppConfig.map = map;
    }

}
