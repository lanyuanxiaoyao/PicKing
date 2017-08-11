package com.lanyuan.picking.pattern;

import java.io.UnsupportedEncodingException;

public interface SinglePicturePattern extends BasePattern {

    String getSinglePicContent(String baseUrl, String currentUrl, byte[] result) throws UnsupportedEncodingException;

}
