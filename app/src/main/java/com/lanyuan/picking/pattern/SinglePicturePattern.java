package com.lanyuan.picking.pattern;

import com.lanyuan.picking.common.bean.PicInfo;

import java.io.UnsupportedEncodingException;

public interface SinglePicturePattern extends BasePattern {

    PicInfo getSinglePicContent(String baseUrl, String currentUrl, byte[] result) throws UnsupportedEncodingException;

}
