package com.lanyuan.picking.common;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.View;
import android.widget.Toast;

import com.facebook.drawee.backends.pipeline.Fresco;
import com.lanyuan.picking.R;
import com.lanyuan.picking.config.AppConfig;
import com.lanyuan.picking.pattern.custom.BasePattern;
import com.lanyuan.picking.util.ScreenUtil;
import com.lanyuan.picking.util.ToastUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;

public class DetailActivity extends BaseActivity {

    @BindView(R.id.detail_recycle_view)
    RecyclerView recyclerView;

    private DetailAdapter adapter;

    private String baseUrl;
    private String currentUrl;

    private int picNumber;

    private boolean isRunnable = true;
    private boolean hasMore = true;

    private BasePattern pattern;

    private PicDialog picDialog;

    public enum parameter {
        RESULT, CURRENT_URL
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        ButterKnife.bind(this);

        picDialog = new PicDialog(this);

        Intent intent = getIntent();
        pattern = (BasePattern) intent.getSerializableExtra("pattern");
        baseUrl = intent.getStringExtra("baseUrl");
        currentUrl = intent.getStringExtra("currentUrl");

        recyclerView.setLayoutManager(new StaggeredGridLayoutManager(1, StaggeredGridLayoutManager.VERTICAL));
        if (!(boolean) AppConfig.getByResourceId(this, R.string.load_pic_swipe, false))
            recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
                @Override
                public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                    super.onScrollStateChanged(recyclerView, newState);
                    if (newState > 0)
                        Fresco.getImagePipeline().pause();
                    else
                        Fresco.getImagePipeline().resume();
                }
            });
        adapter = new DetailAdapter(this, new ArrayList<String>(), ScreenUtil.getScreenWidth(this));
        adapter.setOnClickListener(new DetailAdapter.OnItemClickListener() {
            @Override
            public void ItemClickListener(View view, int position, String url) {
                picDialog.show(url);
            }

            @Override
            public void ItemLongClickListener(View view, int position, String url) {
                toast(url);
            }
        });
        recyclerView.setAdapter(adapter);

        new GetContent().execute(currentUrl);
    }

    public Map<parameter, Object> getContent(String baseUrl, String currentUrl) {
        return pattern.getDetailContent(baseUrl, currentUrl);
    }

    public String getNext(String baseUrl, String currentUrl) {
        return pattern.getDetailNext(baseUrl, currentUrl);
    }

    private class GetContent extends AsyncTask<String, Integer, Map<parameter, Object>> {

        @Override
        protected Map<parameter, Object> doInBackground(String... strings) {
            if (strings.length > 0)
                return getContent(baseUrl, strings[0]);
            else
                return null;
        }

        @Override
        protected void onPostExecute(Map<parameter, Object> resultMap) {
            if (!isRunnable) return;

            List<String> urls = (List<String>) resultMap.get(parameter.RESULT);
            adapter.addMore(urls);

            if (hasMore) {
                picNumber += urls.size();
                toast("正在加载第" + picNumber + "张图片", Toast.LENGTH_LONG);
                new GetNext().execute((String) resultMap.get(parameter.CURRENT_URL));
            }
        }
    }

    private class GetNext extends AsyncTask<String, Integer, String> {

        @Override
        protected String doInBackground(String... strings) {
            if (strings.length > 0)
                return getNext(baseUrl, strings[0]);
            else
                return "";
        }

        @Override
        protected void onPostExecute(String url) {
            if (!isRunnable) return;
            if (!"".equals(url)) {
                new GetContent().execute(url);
            } else {
                hasMore = false;
                toast("(/◔ ◡ ◔)/\n加载完成 共" + picNumber + "张图片", Toast.LENGTH_LONG);
            }
        }
    }

    @Override
    protected void onPause() {
        isRunnable = false;
        ToastUtil.cancel();
        super.onPause();
    }
}
