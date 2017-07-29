package com.lanyuan.picking.common;

import android.support.v7.app.AppCompatActivity;

import com.lanyuan.picking.util.ToastUtil;

public class BaseActivity extends AppCompatActivity {

    public void toast(String content) {
        ToastUtil.toast(content);
    }

    public void toast(String content, int duration) {
        ToastUtil.toast(content, duration);
    }

}
