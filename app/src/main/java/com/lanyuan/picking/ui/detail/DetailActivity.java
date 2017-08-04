package com.lanyuan.picking.ui.detail;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.Gravity;
import android.view.View;

import com.facebook.drawee.backends.pipeline.Fresco;
import com.lanyuan.picking.R;
import com.lanyuan.picking.ui.BaseActivity;
import com.lanyuan.picking.ui.PicDialog;
import com.lanyuan.picking.config.AppConfig;
import com.lanyuan.picking.pattern.BasePattern;
import com.lanyuan.picking.util.OkHttpClientUtil;
import com.lanyuan.picking.util.PicUtil;
import com.lanyuan.picking.util.ScreenUtil;
import com.lanyuan.picking.util.SnackbarUtils;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import okhttp3.Request;
import okhttp3.Response;

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

    private Snackbar snackbar;

    public enum parameter {
        RESULT, CURRENT_URL
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().setStatusBarColor(Color.TRANSPARENT);
        }

        ButterKnife.bind(this);

        picDialog = new PicDialog(this);
        snackbar = SnackbarUtils.Long(getWindow().getDecorView(), "").info().getSnackbar();

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
        adapter.setOnLoveClickListener(new DetailAdapter.OnLoveClickListener() {
            @Override
            public void LoveClickListener(View view, int position, String url) {
                PicUtil.saveImageFromFresco(getWindow().getDecorView(), url, (String) AppConfig.getByResourceId(getBaseContext(), R.string.download_path, AppConfig.DOWNLOAD_PATH));
            }
        });
        adapter.setOnClickListener(new DetailAdapter.OnItemClickListener() {
            @Override
            public void ItemClickListener(View view, int position, String url) {
                picDialog.show(url);
            }

            @Override
            public void ItemLongClickListener(View view, int position, final String url) {
                String[] items = {"保存", "分享", "设为壁纸"};
                AlertDialog dialog = new AlertDialog.Builder(DetailActivity.this)
                        .setItems(items, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int index) {
                                switch (index) {
                                    case 0:
                                        PicUtil.saveImageFromFresco(getWindow().getDecorView(), url, (String) AppConfig.getByResourceId(getBaseContext(), R.string.download_path, AppConfig.DOWNLOAD_PATH));
                                        break;
                                    case 1:
                                        PicUtil.shareImageFromFresco(getWindow().getDecorView(), DetailActivity.this, url, (String) AppConfig.getByResourceId(getBaseContext(), R.string.download_path, AppConfig.DOWNLOAD_PATH));
                                        break;
                                    case 2:
                                        PicUtil.setWallPaperImageFromFresco(getWindow().getDecorView(), DetailActivity.this, url, (String) AppConfig.getByResourceId(getBaseContext(), R.string.download_path, AppConfig.DOWNLOAD_PATH));
                                        break;
                                }
                            }
                        })
                        .create();
                dialog.show();
            }
        });
        recyclerView.setAdapter(adapter);

        new GetContent().execute(currentUrl);
    }

    public Map<parameter, Object> getContent(String baseUrl, String currentUrl, byte[] result, Map<parameter, Object> resultMap) throws UnsupportedEncodingException {
        return pattern.getDetailContent(baseUrl, currentUrl, result, resultMap);
    }

    public String getNext(String baseUrl, String currentUrl, byte[] result) throws UnsupportedEncodingException {
        return pattern.getDetailNext(baseUrl, currentUrl, result);
    }

    private class GetContent extends AsyncTask<String, Integer, Map<parameter, Object>> {

        @Override
        protected Map<parameter, Object> doInBackground(String... strings) {
            if (strings.length > 0) {
                try {
                    Map<parameter, Object> resultMap = new HashMap<>();
                    Request request = new Request.Builder()
                            .url(strings[0])
                            .build();
                    Response response = OkHttpClientUtil.getInstance().newCall(request).execute();
                    byte[] result = response.body().bytes();
                    return getContent(baseUrl, strings[0], result, resultMap);
                } catch (IOException e) {
                    e.printStackTrace();
                    return null;
                }
            } else
                return null;
        }

        @Override
        protected void onPostExecute(Map<parameter, Object> resultMap) {
            if (!isRunnable)
                return;
            else if (resultMap == null) {
                SnackbarUtils.Short(getWindow().getDecorView(), "获取内容失败，请检查网络连接").danger().show();
                return;
            }

            List<String> urls = (List<String>) resultMap.get(parameter.RESULT);
            if (urls.size() == 0) {
                SnackbarUtils.Short(getWindow().getDecorView(), "获取内容失败，请检查网络连接").danger().show();
                return;
            }
            adapter.addMore(urls);

            if (hasMore) {
                picNumber += urls.size();
                snackbar.setText("正在加载第" + picNumber + "张图片").show();
                new GetNext().execute((String) resultMap.get(parameter.CURRENT_URL));
            }
        }
    }

    private class GetNext extends AsyncTask<String, Integer, String> {

        @Override
        protected String doInBackground(String... strings) {
            if (strings.length > 0) {
                try {
                    Request request = new Request.Builder()
                            .url(strings[0])
                            .build();
                    Response response = OkHttpClientUtil.getInstance().newCall(request).execute();
                    byte[] result = response.body().bytes();
                    return getNext(baseUrl, strings[0], result);
                } catch (IOException e) {
                    e.printStackTrace();
                    return "";
                }
            } else
                return "";
        }

        @Override
        protected void onPostExecute(String url) {
            if (!isRunnable) return;
            if (!"".equals(url)) {
                new GetContent().execute(url);
            } else {
                hasMore = false;
                SnackbarUtils.Short(getWindow().getDecorView(), "(/◔ ◡ ◔)/ 加载完成 共" + picNumber + "张图片").confirm().show();
            }
        }
    }

    @Override
    protected void onPause() {
        isRunnable = false;
        snackbar.dismiss();
        super.onPause();
    }
}
