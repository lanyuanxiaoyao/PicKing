package com.lanyuan.picking.util;

import android.content.Context;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.lanyuan.picking.common.bean.PicInfo;

import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static android.content.Context.MODE_PRIVATE;

public class FavoriteUtil {

    public static List<PicInfo> favorites = new ArrayList<>();

    public static void init(Context context) {
        favorites = loadFavorites(context);
    }

    public static void remove(Context context, PicInfo item) {
        favorites.remove(item);
        saveFavorites(context, favorites);
    }

    public static boolean add(Context context, PicInfo item) {
        if (favorites.contains(item))
            return false;
        favorites.add(item);
        saveFavorites(context, favorites);
        return true;
    }

    public static void saveFavorites(Context context, String jsonData) {
        try {
            FileOutputStream outputStream = context.openFileOutput("favoritePic.ini", MODE_PRIVATE);
            outputStream.write(jsonData.getBytes());
            outputStream.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void saveFavorites(Context context, List<PicInfo> favoriteList) {
        String json = JsonUtil.getInstance().toJson(favoriteList);
        saveFavorites(context, json);
    }

    public static List<PicInfo> loadFavorites(Context context) {
        String s = null;
        try {
            FileInputStream inputStream = context.openFileInput("favoritePic.ini");
            ByteArrayOutputStream stream = new ByteArrayOutputStream();
            byte[] buffer = new byte[1024];
            int length = -1;
            while ((length = inputStream.read(buffer)) != -1) {
                stream.write(buffer, 0, length);
            }
            stream.close();
            inputStream.close();
            s = stream.toString();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return new ArrayList<>();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return JsonUtil.getInstance().fromJson(s, new TypeToken<List<PicInfo>>() {
        }.getType());
    }
}
