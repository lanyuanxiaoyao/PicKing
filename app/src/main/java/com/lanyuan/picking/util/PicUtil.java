package com.lanyuan.picking.util;

import android.app.WallpaperManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.view.View;

import com.facebook.common.executors.CallerThreadExecutor;
import com.facebook.common.references.CloseableReference;
import com.facebook.datasource.DataSource;
import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.imagepipeline.datasource.BaseBitmapDataSubscriber;
import com.facebook.imagepipeline.image.CloseableImage;
import com.facebook.imagepipeline.request.ImageRequest;
import com.lanyuan.picking.R;
import com.lanyuan.picking.config.AppConfig;
import com.litesuits.common.utils.BitmapUtil;

import java.io.File;

public class PicUtil {

    public static void saveImageFromFresco(final View view, final String url, final String path) {
        SnackbarUtils.Short(view, "正在保存...").info().show();
        ImageRequest imageRequest = ImageRequest.fromUri(url);
        DataSource<CloseableReference<CloseableImage>> dataSource = Fresco.getImagePipeline()
                .fetchDecodedImage(imageRequest, null);
        dataSource.subscribe(new BaseBitmapDataSubscriber() {
            @Override
            public void onNewResultImpl(@Nullable Bitmap bitmap) {
                if (bitmap != null) {
                    String filePath = path + Md5Util.getMD5(url) + ".jpg";
                    if (BitmapUtil.saveBitmap(bitmap, filePath)) {
                        SnackbarUtils.Short(view, "保存成功").confirm().show();
                    } else {
                        SnackbarUtils.Short(view, "保存失败").danger().show();
                    }
                } else {
                    SnackbarUtils.Short(view, "保存失败").danger().show();
                }
            }

            @Override
            public void onFailureImpl(DataSource dataSource) {
                SnackbarUtils.Short(view, "保存失败").danger().show();
            }
        }, CallerThreadExecutor.getInstance());
    }

    public static void shareImageFromFresco(final View view, final Context context, final String url, final String path) {
        ImageRequest imageRequest = ImageRequest.fromUri(url);
        DataSource<CloseableReference<CloseableImage>> dataSource = Fresco.getImagePipeline()
                .fetchDecodedImage(imageRequest, null);
        dataSource.subscribe(new BaseBitmapDataSubscriber() {
            @Override
            public void onNewResultImpl(@Nullable Bitmap bitmap) {
                SnackbarUtils.Short(view, "正在准备分享...").info().show();
                if (bitmap != null) {
                    String filePath = path + Md5Util.getMD5(url) + ".jpg";
                    if ((boolean) AppConfig.getByResourceId(context, R.string.share_model, false) == false) {
                        Intent share = new Intent(Intent.ACTION_SEND);
                        share.setType("text/plain");
                        share.putExtra(Intent.EXTRA_TEXT, "有人给你分享了一张图片:" + url);
                        context.startActivity(Intent.createChooser(share, "分享到"));
                    } else {
                        if (BitmapUtil.saveBitmap(bitmap, filePath)) {
                            Intent share = new Intent(Intent.ACTION_SEND);
                            share.putExtra(Intent.EXTRA_SUBJECT, "分享");
                            share.putExtra(Intent.EXTRA_TEXT, "有人给你分享了一张图片");
                            share.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            share.setType("image/jpg");
                            File file = new File(filePath);
                            share.putExtra(Intent.EXTRA_STREAM, Uri.fromFile(file));
                            context.startActivity(Intent.createChooser(share, "分享到"));

                        } else {
                            SnackbarUtils.Short(view, "分享失败").danger().show();
                        }
                    }
                } else {
                    SnackbarUtils.Short(view, "分享失败").danger().show();
                }
            }

            @Override
            public void onFailureImpl(DataSource dataSource) {
                SnackbarUtils.Short(view, "分享失败").danger().show();
            }
        }, CallerThreadExecutor.getInstance());
    }

    public static void setWallPaperImageFromFresco(final View view, final Context context, final String url, final String path) {
        SnackbarUtils.Short(view, "正在设置壁纸...").info().show();
        ImageRequest imageRequest = ImageRequest.fromUri(url);
        DataSource<CloseableReference<CloseableImage>> dataSource = Fresco.getImagePipeline()
                .fetchDecodedImage(imageRequest, null);
        dataSource.subscribe(new BaseBitmapDataSubscriber() {
            @Override
            public void onNewResultImpl(@Nullable Bitmap bitmap) {
                if (bitmap != null) {
                    String filePath = path + Md5Util.getMD5(url) + ".jpg";
                    if (BitmapUtil.saveBitmap(bitmap, filePath)) {
                        try {
                            WallpaperManager wallpaper = WallpaperManager.getInstance(context);
                            wallpaper.setBitmap(bitmap);
                            SnackbarUtils.Short(view, "设置成功").confirm().show();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    } else {
                        SnackbarUtils.Short(view, "设置失败").danger().show();
                    }
                } else {
                    SnackbarUtils.Short(view, "设置失败").danger().show();
                }
            }

            @Override
            public void onFailureImpl(DataSource dataSource) {
                SnackbarUtils.Short(view, "设置失败").danger().show();
            }
        }, CallerThreadExecutor.getInstance());
    }
}