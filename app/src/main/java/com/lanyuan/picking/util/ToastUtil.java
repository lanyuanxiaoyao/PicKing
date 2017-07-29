package com.lanyuan.picking.util;

import android.widget.Toast;

import com.lanyuan.picking.application.MyApplication;

/**
 * Created by lanyuanxiaoyao on 2017/7/26.
 */

public class ToastUtil {
    /**
     * 显示toast
     *
     * @param content  内容
     * @param duration 持续时间
     */
    public static void toast(String content, int duration) {
        if (content == null) {
            return;
        } else {
            MyApplication.ToastInstance.builder.display(content, duration);
        }
    }

    /**
     * 显示默认持续时间为short的Toast
     *
     * @param content 内容
     */
    public static void toast(String content) {
        if (content == null) {
            return;
        } else {
            MyApplication.ToastInstance.builder.display(content, Toast.LENGTH_SHORT);
        }
    }

    /**
     * 停止Toast
     */
    public static void cancel() {
        MyApplication.ToastInstance.builder.cancel();
    }
}
