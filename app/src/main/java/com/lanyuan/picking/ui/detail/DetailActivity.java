package com.lanyuan.picking.ui.detail;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.Snackbar;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.drawee.view.SimpleDraweeView;
import com.lanyuan.picking.R;
import com.lanyuan.picking.common.bean.AlbumInfo;
import com.lanyuan.picking.common.bean.PicInfo;
import com.lanyuan.picking.pattern.MultiPicturePattern;
import com.lanyuan.picking.ui.BaseActivity;
import com.lanyuan.picking.ui.dialog.PicDialog;
import com.lanyuan.picking.config.AppConfig;
import com.lanyuan.picking.pattern.BasePattern;
import com.lanyuan.picking.util.FrescoUtil;
import com.lanyuan.picking.util.OkHttpClientUtil;
import com.lanyuan.picking.util.PicUtil;
import com.lanyuan.picking.util.SPUtils;
import com.lanyuan.picking.util.ScreenUtil;
import com.lanyuan.picking.util.SnackbarUtils;
import com.lanyuan.picking.util.StatusBarUtil;

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

public class DetailActivity extends BaseActivity implements AppBarLayout.OnOffsetChangedListener {

    @BindView(R.id.detail_recycle_view)
    RecyclerView recyclerView;
    @BindView(R.id.detail_title_image)
    SimpleDraweeView titleImage;
    @BindView(R.id.toolbar_layout)
    CollapsingToolbarLayout toolbarLayout;
    @BindView(R.id.appbar_detail)
    AppBarLayout appBarLayout;
    @BindView(R.id.title_detaill)
    TextView title;
    @BindView(R.id.time_detail)
    TextView time;

    private DetailAdapter adapter;

    private String baseUrl;
    private String currentUrl;

    private int picNumber;

    private boolean isRunnable = true;
    private boolean hasMore = true;

    private BasePattern pattern;

    private AlbumInfo albumInfo;

    private PicDialog picDialog;

    private Snackbar snackbar;

    private int cols = 1;

    public enum parameter {
        RESULT, CURRENT_URL, GIF_THUMB
    }

    private CollapsingToolbarLayoutState appBarState;

    private enum CollapsingToolbarLayoutState {
        EXPANDED,
        COLLAPSED,
        MIDDLE
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        StatusBarUtil.MIUISetStatusBarLightMode(this, true);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().setStatusBarColor(Color.TRANSPARENT);
        }

        ButterKnife.bind(this);

        getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);

        picDialog = new PicDialog(this);
        snackbar = SnackbarUtils.Long(getWindow().getDecorView(), "").info().getSnackbar();

        Intent intent = getIntent();
        albumInfo = (AlbumInfo) intent.getSerializableExtra("albumInfo");
        pattern = (BasePattern) intent.getSerializableExtra("pattern");
        baseUrl = intent.getStringExtra("baseUrl");
        currentUrl = albumInfo.getAlbumUrl();
        FrescoUtil.setBlurFrescoController(titleImage, albumInfo.getCoverUrl(), 1, 1);
        toolbarLayout.setTitle("");
        title.setText(albumInfo.getTitle());
        time.setText(albumInfo.getTime());

        cols = (int) SPUtils.get(this, AppConfig.cols_detail, 1) + 1;
        recyclerView.setLayoutManager(new StaggeredGridLayoutManager(cols, StaggeredGridLayoutManager.VERTICAL));
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
        adapter = new DetailAdapter(this, new ArrayList<PicInfo>(), ScreenUtil.getScreenWidth(this) / cols);
        adapter.setOnLoveClickListener(new DetailAdapter.OnLoveClickListener() {
            @Override
            public void LoveClickListener(View view, int position, PicInfo picInfo) {
                PicUtil.doFromFresco(getWindow().getDecorView(), DetailActivity.this, picInfo.getPicUrl(), (String) SPUtils.get(DetailActivity.this, AppConfig.download_path, AppConfig.DOWNLOAD_PATH), PicUtil.SAVE_IMAGE);
            }
        });
        adapter.setOnClickListener(new DetailAdapter.OnItemClickListener() {
            @Override
            public void ItemClickListener(View view, int position, PicInfo picInfo) {
                picDialog.show(picInfo);
            }

            @Override
            public void ItemLongClickListener(View view, int position, PicInfo picInfo) {
            }
        });
        recyclerView.setAdapter(adapter);
    }

    @Override
    public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {

        if (verticalOffset == 0) {
            if (appBarState != CollapsingToolbarLayoutState.EXPANDED) {
                appBarState = CollapsingToolbarLayoutState.EXPANDED; // 展开
            }
        } else if (Math.abs(verticalOffset) >= appBarLayout.getTotalScrollRange()) {
            appBarState = CollapsingToolbarLayoutState.COLLAPSED; // 折叠
        } else {
            if (appBarState != CollapsingToolbarLayoutState.MIDDLE) {
                if (appBarState == CollapsingToolbarLayoutState.COLLAPSED) {
                }
                appBarState = CollapsingToolbarLayoutState.MIDDLE; // 中间
            }
        }

    }

    public Map<parameter, Object> getContent(String baseUrl, String currentUrl, byte[] result, Map<parameter, Object> resultMap) throws UnsupportedEncodingException {
        return ((MultiPicturePattern) pattern).getDetailContent(baseUrl, currentUrl, result, resultMap);
    }

    public String getNext(String baseUrl, String currentUrl, byte[] result) throws UnsupportedEncodingException {
        return ((MultiPicturePattern) pattern).getDetailNext(baseUrl, currentUrl, result);
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
                SnackbarUtils.Short(getWindow().getDecorView(), "获取内容失败，请检查网络连接").danger().show();
                return;
            }

            List<PicInfo> pics = (List<PicInfo>) resultMap.get(parameter.RESULT);
            if (pics.size() == 0) {
                SnackbarUtils.Short(getWindow().getDecorView(), "获取内容失败，请检查网络连接").danger().show();
                return;
            }
            adapter.addMore(pics);

            if (hasMore) {
                picNumber += pics.size();
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
                    Call call = OkHttpClientUtil.getInstance().newCall(request);
                    Response response = call.execute();
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
            if (!"".equals(url) && url != null) {
                currentUrl = url;
                new GetContent().execute(url);
            } else {
                hasMore = false;
                SnackbarUtils.Short(getWindow().getDecorView(), "(/◔ ◡ ◔)/ 加载完成 共" + picNumber + "张图片").confirm().show();
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    @Override
    protected void onStart() {
        super.onStart();
        if (hasMore)
            new GetContent().execute(currentUrl);
    }

    @Override
    protected void onDestroy() {
        isRunnable = false;
        snackbar.dismiss();
        super.onDestroy();
    }
}
