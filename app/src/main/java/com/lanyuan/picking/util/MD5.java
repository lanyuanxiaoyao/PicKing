package com.lanyuan.picking.util;

import com.litesuits.common.utils.MD5Util;

public class MD5 {
    public static String getMD5(String val) {
        return getString(MD5Util.md5(val));
    }

    private static String getString(byte[] b) {
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < b.length; i++) {
            sb.append(b[i]);
        }
        return sb.toString();
    }
}
