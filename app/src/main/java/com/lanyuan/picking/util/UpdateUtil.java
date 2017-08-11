package com.lanyuan.picking.util;

import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.View;
import android.widget.Toast;

import com.litesuits.common.utils.PackageUtil;

public class UpdateUtil {

    public static void goToAppMarket(Context context, View view) {
        if (PackageUtil.isInsatalled(context, "com.coolapk.market")) {
            Toast.makeText(context, "本应用目前仅发布在酷安市场，请前往酷安市场检查更新", Toast.LENGTH_SHORT).show();
            try {
                Uri uri = Uri.parse("market://details?id=" + context.getPackageName());
                Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(intent);
            } catch (ActivityNotFoundException e) {
                SnackbarUtils.Short(view, "跳转出错，请向作者反馈").danger().show();
            }
        } else {
            Toast.makeText(context, "未检测到酷安市场客户端，正在跳转到网页", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent();
            intent.setAction(Intent.ACTION_VIEW);
            intent.setData(Uri.parse("https://www.coolapk.com/apk/154571"));
            context.startActivity(intent);
        }
    }

}
