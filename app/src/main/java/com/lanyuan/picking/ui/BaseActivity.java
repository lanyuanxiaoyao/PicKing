package com.lanyuan.picking.ui;

import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.View;

import com.aitangba.swipeback.SwipeBackActivity;
import com.facebook.common.util.ByteConstants;
import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.imagepipeline.core.ImagePipelineFactory;
import com.lanyuan.picking.R;
import com.lanyuan.picking.application.MyApplication;
import com.lanyuan.picking.config.AppConfig;
import com.lanyuan.picking.util.SPUtils;

public class BaseActivity extends SwipeBackActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        changeTheme((String) SPUtils.get(this, AppConfig.choose_theme, AppConfig.DEFAULT_COLOR));
        super.onCreate(savedInstanceState);
        if (Build.VERSION.SDK_INT >= 21) {
            View decorView = getWindow().getDecorView();
            int option = View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                    | View.SYSTEM_UI_FLAG_LAYOUT_STABLE;
            decorView.setSystemUiVisibility(option);
        }
    }

    public void changeTheme(String theme) {
        if (theme == null) return;
        switch (theme) {
            case AppConfig.DEFAULT_COLOR:
                setTheme(R.style.AppNoActionBarTheme);
                SPUtils.put(this, AppConfig.choose_theme, AppConfig.DEFAULT_COLOR);
                break;
            case AppConfig.BLUE_COLOR:
                setTheme(R.style.AppNoActionBarThemeBlue);
                SPUtils.put(this, AppConfig.choose_theme, AppConfig.BLUE_COLOR);
                break;
            case AppConfig.ORANGE_COLOR:
                setTheme(R.style.AppNoActionBarThemeOrange);
                SPUtils.put(this, AppConfig.choose_theme, AppConfig.ORANGE_COLOR);
                break;
            case AppConfig.BROWN_COLOR:
                setTheme(R.style.AppNoActionBarThemeBrown);
                SPUtils.put(this, AppConfig.choose_theme, AppConfig.BROWN_COLOR);
                break;
            case AppConfig.PURPLE_COLOR:
                setTheme(R.style.AppNoActionBarThemePurple);
                SPUtils.put(this, AppConfig.choose_theme, AppConfig.PURPLE_COLOR);
                break;
            case AppConfig.CYAN_COLOR:
                setTheme(R.style.AppNoActionBarThemeCyan);
                SPUtils.put(this, AppConfig.choose_theme, AppConfig.CYAN_COLOR);
                break;
            case AppConfig.GREEN_COLOR:
                setTheme(R.style.AppNoActionBarThemeGreen);
                SPUtils.put(this, AppConfig.choose_theme, AppConfig.GREEN_COLOR);
                break;
            case AppConfig.YELLOW_COLOR:
                setTheme(R.style.AppNoActionBarThemeYellow);
                SPUtils.put(this, AppConfig.choose_theme, AppConfig.YELLOW_COLOR);
                break;
            case AppConfig.GREY_COLOR:
                setTheme(R.style.AppNoActionBarThemeGrey);
                SPUtils.put(this, AppConfig.choose_theme, AppConfig.GREY_COLOR);
                break;
        }
    }
}
