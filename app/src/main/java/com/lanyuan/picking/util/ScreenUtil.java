package com.lanyuan.picking.util;

import android.app.Activity;
import android.util.DisplayMetrics;
import android.view.WindowManager;

public class ScreenUtil {

    public static int getScreenWidth(Activity activity){
        WindowManager manager = activity.getWindowManager();
        DisplayMetrics outMetrics = new DisplayMetrics();
        manager.getDefaultDisplay().getMetrics(outMetrics);
        return outMetrics.widthPixels;
    }

}
