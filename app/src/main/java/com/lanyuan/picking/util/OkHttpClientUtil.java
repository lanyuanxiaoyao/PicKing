package com.lanyuan.picking.util;

import android.util.Log;

import okhttp3.OkHttpClient;

public class OkHttpClientUtil {

    private static OkHttpClient instance;

    public static OkHttpClient getInstance() {
        if (instance == null) {
            instance = new OkHttpClient();
        }
        return instance;
    }

}
