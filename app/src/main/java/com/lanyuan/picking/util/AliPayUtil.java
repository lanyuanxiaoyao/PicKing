package com.lanyuan.picking.util;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;

import com.lanyuan.picking.config.AppConfig;
import com.litesuits.common.utils.PackageUtil;

public class AliPayUtil {

    public static boolean goAliPay(Context context) {
        if (PackageUtil.isInsatalled(context, "com.eg.android.AlipayGphone")) {
            int count = (int) SPUtils.get(context, AppConfig.ancdslknsdlnv, 0);
            SPUtils.put(context, AppConfig.ancdslknsdlnv, ++count);

            Intent intent = new Intent();
            intent.setAction("android.intent.action.VIEW");
            Uri qrcode_url = Uri.parse("alipayqr://platformapi/startapp?saId=10000007&clientVersion=3.7.0.0718&qrcode=https%3A%2F%2Fqr.alipay.com%2FFKX06852EOBWRJPACOMCF5%3F_s%3Dweb-other");
            intent.setData(qrcode_url);
            intent.setClassName("com.eg.android.AlipayGphone", "com.alipay.mobile.quinox.LauncherActivity");
            context.startActivity(intent);
            return true;
        } else
            return false;
    }

}
