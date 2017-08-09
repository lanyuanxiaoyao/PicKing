package com.lanyuan.picking.util;

import android.content.Context;

import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static android.content.Context.MODE_PRIVATE;

public class FavoriteUtil {

    public static List<String> favorites = new ArrayList<>();

    public static void init(Context context) {
        favorites = loadFavorites(context);
    }

    public static void remove(Context context, String item) {
        favorites.remove(item);
        saveFavorites(context, favorites);
    }

    public static boolean add(Context context, String item) {
        if (favorites.contains(item))
            return false;
        favorites.add(item);
        saveFavorites(context, favorites);
        return true;
    }

    public static void saveFavorites(Context context, String s) {
        try {
            FileOutputStream outputStream = context.openFileOutput("favorite.ini", MODE_PRIVATE);
            outputStream.write(s.getBytes());
            outputStream.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void saveFavorites(Context context, List<String> favoriteList) {
        String json = JsonUtil.getInstance().toJson(favoriteList);
        saveFavorites(context, json);
    }

    public static List<String> loadFavorites(Context context) {
        String s = null;
        try {
            FileInputStream inputStream = context.openFileInput("favorite.ini");
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
        return JsonUtil.getInstance().fromJson(s, ArrayList.class);
    }

}
