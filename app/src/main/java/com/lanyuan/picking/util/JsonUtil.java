package com.lanyuan.picking.util;

import com.google.gson.Gson;

public class JsonUtil {

    private static Gson instance = new Gson();

    public static Gson getInstance() {
        return instance;
    }

}
