package com.lanyuan.picking.ui.favorite;

import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.View;

import com.facebook.drawee.backends.pipeline.Fresco;
import com.lanyuan.picking.R;
import com.lanyuan.picking.common.bean.PicInfo;
import com.lanyuan.picking.config.AppConfig;
import com.lanyuan.picking.ui.BaseActivity;
import com.lanyuan.picking.ui.dialog.PicDialog;
import com.lanyuan.picking.util.FavoriteUtil;
import com.lanyuan.picking.util.PicUtil;
import com.lanyuan.picking.util.SPUtils;
import com.lanyuan.picking.util.ScreenUtil;
import com.lanyuan.picking.util.SnackbarUtils;
import com.lanyuan.picking.util.StatusBarUtil;

import butterknife.BindView;
import butterknife.ButterKnife;

public class FavoriteActivity extends BaseActivity {

    @BindView(R.id.favorite_recycle_view)
    RecyclerView recyclerView;

    private FavoriteAdapter adapter;

    private PicDialog picDialog;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favorite);

        StatusBarUtil.MIUISetStatusBarLightMode(this, true);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().setStatusBarColor(Color.TRANSPARENT);
        }

        ButterKnife.bind(this);

        picDialog = new PicDialog(this);

        recyclerView.setLayoutManager(new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL));
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
        adapter = new FavoriteAdapter(this, FavoriteUtil.favorites, ScreenUtil.getScreenWidth(this) / 2);
        adapter.setOnLoveClickListener(new FavoriteAdapter.OnLoveClickListener() {
            @Override
            public void LoveClickListener(View view, int position, PicInfo picInfo) {
                PicUtil.doFromFresco(getWindow().getDecorView(), FavoriteActivity.this, picInfo.getPicUrl(), (String) SPUtils.get(FavoriteActivity.this, AppConfig.download_path, AppConfig.DOWNLOAD_PATH), PicUtil.SAVE_IMAGE);
            }
        });
        adapter.setOnClickListener(new FavoriteAdapter.OnItemClickListener() {
            @Override
            public void ItemClickListener(View view, int position, PicInfo picInfo) {
                picDialog.show(picInfo);
            }

            @Override
            public void ItemLongClickListener(final View view, final int position, final PicInfo picInfo) {
                new AlertDialog.Builder(FavoriteActivity.this)
                        .setTitle("注意")
                        .setMessage("确定要将这张图片从收藏中移除吗？")
                        .setPositiveButton("移除", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                adapter.remove(picInfo);
                                FavoriteUtil.remove(FavoriteActivity.this, picInfo);
                                SnackbarUtils.Short(getWindow().getDecorView(), "移除成功").confirm().show();
                            }
                        })
                        .setNegativeButton("取消", null)
                        .show();
            }
        });
        recyclerView.setAdapter(adapter);

        if (adapter.getItemCount() == 0) {
            SnackbarUtils.Indefinite(getWindow().getDecorView(), "收藏夹空空如也呢！").info().show();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}
