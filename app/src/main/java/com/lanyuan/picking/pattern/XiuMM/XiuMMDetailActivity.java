package com.lanyuan.picking.pattern.XiuMM;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.View;
import android.widget.Toast;

import com.facebook.drawee.backends.pipeline.Fresco;
import com.lanyuan.picking.R;
import com.lanyuan.picking.common.BaseActivity;
import com.lanyuan.picking.common.BaseDetailActivity;
import com.lanyuan.picking.util.OkHttpClientUtil;
import com.lanyuan.picking.util.ScreenUtil;
import com.lanyuan.picking.util.ToastUtil;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import okhttp3.Call;
import okhttp3.Request;
import okhttp3.Response;

public class XiuMMDetailActivity extends BaseDetailActivity {

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
            Document document = Jsoup.parse(new String(result, "utf-8"));
            Elements elements = document.select(".gallary_item .pic_box img");
            for (Element element : elements) {
                urls.add(baseUrl + element.attr("src"));
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
            Document document = Jsoup.parse(new String(result, "utf-8"));
            Elements elements = document.select(".paginator span.next a");
            if (elements.size() > 0)
                return baseUrl + elements.get(0).attr("href");
        } catch (IOException e) {
            e.printStackTrace();
        }
        return "";
    }

}
