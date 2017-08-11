package com.lanyuan.picking.pattern;

import com.lanyuan.picking.ui.detail.DetailActivity;

import java.io.UnsupportedEncodingException;
import java.util.Map;

public interface MultiPicturePattern extends BasePattern {
    Map<DetailActivity.parameter, Object> getDetailContent(String baseUrl, String currentUrl, byte[] result, Map<DetailActivity.parameter, Object> resultMap) throws UnsupportedEncodingException;

    String getDetailNext(String baseUrl, String currentUrl, byte[] result) throws UnsupportedEncodingException;

}
