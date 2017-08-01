package com.lanyuan.picking.ui;

import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;

import com.aitangba.swipeback.SwipeBackActivity;
import com.lanyuan.picking.util.ToastUtil;

public class BaseActivity extends SwipeBackActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (Build.VERSION.SDK_INT >= 21) {
            View decorView = getWindow().getDecorView();
            int option = View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                    | View.SYSTEM_UI_FLAG_LAYOUT_STABLE;
            decorView.setSystemUiVisibility(option);
        }
    }

    public void toast(String content) {
        ToastUtil.toast(content);
    }

    public void toast(String content, int duration) {
        ToastUtil.toast(content, duration);
    }

}
