package com.lanyuan.picking.pattern.RoseMM;

import android.util.Log;

import com.lanyuan.picking.common.BaseDetailActivity;
import com.lanyuan.picking.util.OkHttpClientUtil;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import okhttp3.Call;
import okhttp3.Request;
import okhttp3.Response;

public class RosiMMDetailActivity extends BaseDetailActivity {
    @Override
    public Map<parameter, Object> getContent(String baseUrl, String currentUrl) {
        Map<parameter, Object> resultMap = new HashMap<>();
        List<String> urls = new ArrayList<>();
        Request request = new Request.Builder()
                .url(currentUrl)
                .build();

        try {
            Call call = OkHttpClientUtil.getInstance().newCall(request);
            Response response = call.execute();
            byte[] result = response.body().bytes();
            Document document = Jsoup.parse(new String(result, "gbk"));
            Elements elements = document.select("#imgString img");
            for (Element element : elements) {
                urls.add(element.attr("src"));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        resultMap.put(parameter.CURRENT_URL, currentUrl);
        resultMap.put(parameter.RESULT, urls);
        return resultMap;
    }

    @Override
    public String getNext(String baseUrl, String currentUrl) {
        Request request = new Request.Builder()
                .url(currentUrl)
                .build();

        try {
            Call call = OkHttpClientUtil.getInstance().newCall(request);
            Response response = call.execute();
            byte[] result = response.body().bytes();
            Document document = Jsoup.parse(new String(result, "gbk"));
            Elements elements = document.select(".page_c a:containsOwn(下一页)");
            if (elements.size() > 0) {
                Pattern pattern = Pattern.compile("http://.*/");
                Matcher matcher = pattern.matcher(currentUrl);
                if (matcher.find()) {
                    String prefix = matcher.group();
                    Log.e("RosiMMDetailActivity", "getNext: " + prefix);
                    return prefix + elements.get(0).attr("href");
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return "";
    }
}
