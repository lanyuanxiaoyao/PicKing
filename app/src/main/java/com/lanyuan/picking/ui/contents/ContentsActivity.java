package com.lanyuan.picking.ui.contents;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.Log;
import android.view.View;

import com.aspsine.swipetoloadlayout.OnLoadMoreListener;
import com.aspsine.swipetoloadlayout.OnRefreshListener;
import com.aspsine.swipetoloadlayout.SwipeToLoadLayout;
import com.facebook.drawee.backends.pipeline.Fresco;
import com.lanyuan.picking.R;
import com.lanyuan.picking.common.AlbumInfo;
import com.lanyuan.picking.common.WebViewTask;
import com.lanyuan.picking.ui.BaseActivity;
import com.lanyuan.picking.ui.dialog.PicDialog;
import com.lanyuan.picking.ui.detail.DetailActivity;
import com.lanyuan.picking.ui.menu.Menu;
import com.lanyuan.picking.ui.menu.MenuAdapter;
import com.lanyuan.picking.config.AppConfig;
import com.lanyuan.picking.pattern.BasePattern;
import com.lanyuan.picking.util.OkHttpClientUtil;
import com.lanyuan.picking.util.SPUtils;
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
import okhttp3.Call;
import okhttp3.Request;
import okhttp3.Response;

public class ContentsActivity extends BaseActivity {

    @BindView(R.id.swipe_target)
    RecyclerView recyclerView;
    @BindView(R.id.menu_recycle_view)
    RecyclerView menuRecycleView;
    @BindView(R.id.swipe_layout)
    SwipeToLoadLayout refreshLayout;
    @BindView(R.id.content_drawer)
    DrawerLayout drawerLayout;

    private String baseUrl;
    private String currentUrl;
    private String firstUrl;

    private boolean isRunnable = true;
    private boolean hasMore = true;

    private ContentsAdapter adapter;

    private List<Menu> menuList;

    private PicDialog picDialog;

    private BasePattern pattern;

    public enum parameter {
        RESULT, CURRENT_URL
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contents);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().setStatusBarColor(Color.TRANSPARENT);
        }

        ButterKnife.bind(this);

        picDialog = new PicDialog(this);

        Intent intent = getIntent();
        pattern = (BasePattern) intent.getSerializableExtra("pattern");

        menuList = getMenuList() == null ? new ArrayList<Menu>() : getMenuList();
        MenuAdapter menuAdapter = new MenuAdapter(this, menuList);
        menuAdapter.setOnClickListener(new MenuAdapter.OnItemClickListener() {
            @Override
            public void ItemClickListener(View view, int position) {
                adapter.removeAll();
                baseUrl = getBaseUrl(menuList, position);
                currentUrl = menuList.get(position).getUrl();
                firstUrl = currentUrl;
                refreshLayout.setRefreshing(true);
                drawerLayout.closeDrawer(GravityCompat.END);
            }

            @Override
            public void ItemLongClickListener(View view, int position) {

            }
        });
        menuRecycleView.setLayoutManager(new LinearLayoutManager(this));
        menuRecycleView.setAdapter(menuAdapter);

        baseUrl = getBaseUrl(menuList, 0);
        currentUrl = menuList.get(0).getUrl();
        firstUrl = currentUrl;

        if (!(boolean) SPUtils.get(this, AppConfig.load_pic_swipe, false))
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

        adapter = new ContentsAdapter(this, new ArrayList<AlbumInfo>(), ScreenUtil.getScreenWidth(this) / 2);
        adapter.setOnClickListener(new ContentsAdapter.OnItemClickListener() {
            @Override
            public void ItemClickListener(View view, int position, AlbumInfo albumInfo) {
                if (pattern.isSinglePic()) {
                    new GetSinglePicContent().execute(albumInfo.getAlbumUrl());
                    SnackbarUtils.Long(getWindow().getDecorView(), "正在加载，请稍候……").info().show();
                } else {
                    Intent intent = new Intent(ContentsActivity.this, DetailActivity.class);
                    Bundle bundle = new Bundle();
                    bundle.putString("currentUrl", albumInfo.getAlbumUrl());
                    bundle.putString("baseUrl", baseUrl);
                    bundle.putSerializable("pattern", pattern);
                    intent.putExtras(bundle);
                    startActivity(intent);
                }
            }

            @Override
            public void ItemLongClickListener(View view, int position, AlbumInfo albumInfo) {
            }
        });

        StaggeredGridLayoutManager staggeredGridLayoutManager = new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(staggeredGridLayoutManager);
        recyclerView.setAdapter(adapter);

        refreshLayout.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh() {
                adapter.removeAll();
                Log.e("ContentsActivity", "onRefresh: " + "WebView-start" + firstUrl);
                // new GetContentByWebView(ContentsActivity.this).execute(firstUrl);
                new GetContent().execute(firstUrl);
            }
        });
        refreshLayout.setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void onLoadMore() {
                new GetContentNext().execute(currentUrl);
            }
        });
        refreshLayout.setRefreshing(true);
    }

    public String getBaseUrl(List<Menu> menuList, int position) {
        return pattern.getBaseUrl(menuList, position);
    }

    public List<Menu> getMenuList() {
        return pattern.getMenuList();
    }

    public Map<parameter, Object> getContent(String baseUrl, String currentUrl, byte[] result, Map<parameter, Object> resultMap) throws UnsupportedEncodingException {
        return pattern.getContent(baseUrl, currentUrl, result, resultMap);
    }

    public String getContentNext(String baseUrl, String currentUrl, byte[] result) throws UnsupportedEncodingException {
        return pattern.getContentNext(baseUrl, currentUrl, result);
    }

    public String getSinglePicContent(String baseUrl, String currentUrl, byte[] result) throws UnsupportedEncodingException {
        return pattern.getSinglePicContent(baseUrl, currentUrl, result);
    }

    @Override
    public void onBackPressed() {
        if (drawerLayout.isDrawerOpen(GravityCompat.END)) {
            drawerLayout.closeDrawer(GravityCompat.END);
        } else {
            super.onBackPressed();
        }
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
                    Call call = OkHttpClientUtil.getInstance().newCall(request);
                    Response response = call.execute();
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
                Snackbar.make(getWindow().getDecorView(), "获取内容失败，请检查网络连接", Snackbar.LENGTH_LONG).show();
                refreshLayout.setRefreshing(false);
                return;
            }

            currentUrl = (String) resultMap.get(parameter.CURRENT_URL);
            if (firstUrl == null) firstUrl = currentUrl;

            List<AlbumInfo> urls = (List<AlbumInfo>) resultMap.get(parameter.RESULT);
            if (urls.size() == 0) {
                Snackbar.make(getWindow().getDecorView(), "获取内容失败，请检查网络连接", Snackbar.LENGTH_LONG).show();
                refreshLayout.setRefreshing(false);
                return;
            }
            adapter.addMore(urls);
            refreshLayout.setRefreshing(false);
            refreshLayout.setLoadingMore(false);
        }
    }

    private class GetContentNext extends AsyncTask<String, Integer, String> {

        @Override
        protected String doInBackground(String... strings) {
            if (strings.length > 0) {
                try {
                    Request request = new Request.Builder()
                            .url(strings[0])
                            .build();
                    Call call = OkHttpClientUtil.getInstance().newCall(request);
                    Response response = call.execute();
                    byte[] result = response.body().bytes();
                    return getContentNext(baseUrl, strings[0], result);
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
                SnackbarUtils.Short(getWindow().getDecorView(), "下面已经没有更多了！").danger().show();
                refreshLayout.setLoadingMore(false);
            }
        }
    }

    private class GetSinglePicContent extends AsyncTask<String, Integer, String> {

        @Override
        protected String doInBackground(String... strings) {
            if (strings.length > 0) {
                try {
                    Request request = new Request.Builder()
                            .url(strings[0])
                            .build();
                    Response response = OkHttpClientUtil.getInstance().newCall(request).execute();
                    byte[] result = response.body().bytes();
                    return getSinglePicContent(baseUrl, currentUrl, result);
                } catch (IOException e) {
                    e.printStackTrace();
                    return null;
                }
            } else
                return null;
        }

        @Override
        protected void onPostExecute(String result) {
            if (!isRunnable)
                return;
            else if (result == null || "".equals(result)) {
                SnackbarUtils.Long(getWindow().getDecorView(), "获取内容失败，请检查网络连接").danger().show();
                return;
            }

            picDialog.show(result);
        }
    }

    private class GetContentByWebView extends WebViewTask {

        public GetContentByWebView(Context context) {
            super(context);
        }

        @Override
        protected void onPostExecute(String result) {
            try {
                Map<parameter, Object> resultMap = new HashMap<>();
                resultMap = getContent(baseUrl, currentUrl, result.getBytes(), resultMap);

                if (!isRunnable) {
                    cancel();
                    return;
                } else if (resultMap == null) {
                    return;
                }

                currentUrl = (String) resultMap.get(parameter.CURRENT_URL);
                if (firstUrl == null) firstUrl = currentUrl;

                Log.e("GetContentByWebView", "onPostExecute: " + currentUrl);

                List<AlbumInfo> urls = (List<AlbumInfo>) resultMap.get(parameter.RESULT);
                if (urls.size() == 0) {
                    return;
                }

                Log.e("GetContentByWebView", "onPostExecute: " + urls.size());

                adapter.addMore(urls);
                refreshLayout.setRefreshing(false);
                refreshLayout.setLoadingMore(false);
                cancel();
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
        }

        @Override
        protected void onFailureExecute() {
            Snackbar.make(getWindow().getDecorView(), "获取内容失败，请检查网络连接", Snackbar.LENGTH_LONG).show();
            refreshLayout.setRefreshing(false);
        }
    }

    private class getContentNextByWebView extends WebViewTask {

        public getContentNextByWebView(Context context) {
            super(context);
        }

        @Override
        protected void onPostExecute(String result) {
            try {
                String url = getContentNext(baseUrl, currentUrl, result.getBytes());

                Log.e("getContentNextByWebView", "onPostExecute: " + url);

                if (!isRunnable) {
                    cancel();
                    return;
                }
                if (!"".equals(url)) {
                    new GetContentByWebView(ContentsActivity.this).execute(url);
                } else {
                    hasMore = false;
                    SnackbarUtils.Short(getWindow().getDecorView(), "下面已经没有更多了！").danger().show();
                    refreshLayout.setLoadingMore(false);
                    cancel();
                }
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
        }

        @Override
        protected void onFailureExecute() {
            Snackbar.make(getWindow().getDecorView(), "获取内容失败，请检查网络连接", Snackbar.LENGTH_LONG).show();
            refreshLayout.setLoadingMore(false);
        }
    }
}
