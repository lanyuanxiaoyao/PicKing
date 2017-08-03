package com.lanyuan.picking.util;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.util.Log;
import android.view.View;

import com.facebook.common.executors.CallerThreadExecutor;
import com.facebook.common.references.CloseableReference;
import com.facebook.datasource.DataSource;
import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.imagepipeline.datasource.BaseBitmapDataSubscriber;
import com.facebook.imagepipeline.image.CloseableImage;
import com.facebook.imagepipeline.request.ImageRequest;
import com.litesuits.common.utils.BitmapUtil;

import java.io.File;

public class PicUtil {

    public static void saveImageFromFresco(final View view, final String url, final String path) {
        ImageRequest imageRequest = ImageRequest.fromUri(url);
        DataSource<CloseableReference<CloseableImage>> dataSource = Fresco.getImagePipeline()
                .fetchDecodedImage(imageRequest, null);
        dataSource.subscribe(new BaseBitmapDataSubscriber() {
            @Override
            public void onNewResultImpl(@Nullable Bitmap bitmap) {
                Log.e("PicUtil", "onNewResultImpl: 开始");
                if (bitmap != null) {
                    String filePath = path + MD5.getMD5(url) + ".jpg";
                    if (BitmapUtil.saveBitmap(bitmap, filePath)) {
                        Snackbar.make(view, "保存成功", Snackbar.LENGTH_SHORT).show();
                        Log.e("PicUtil", "onNewResultImpl: 保存成功");
                    } else {
                        Log.e("PicUtil", "onNewResultImpl: 保存失败");
                    }
                } else {
                    Log.e("PicUtil", "onNewResultImpl: bitmap is null");
                }
            }

            @Override
            public void onFailureImpl(DataSource dataSource) {
                Log.e("PicUtil", "onFailureImpl: 失败");
            }
        }, CallerThreadExecutor.getInstance());
    }

    public static void shareImageFromFresco(final Context context, final String url, final String path) {
        ImageRequest imageRequest = ImageRequest.fromUri(url);
        DataSource<CloseableReference<CloseableImage>> dataSource = Fresco.getImagePipeline()
                .fetchDecodedImage(imageRequest, null);
        dataSource.subscribe(new BaseBitmapDataSubscriber() {
            @Override
            public void onNewResultImpl(@Nullable Bitmap bitmap) {
                Log.e("PicUtil", "onNewResultImpl: 开始");
                if (bitmap != null) {
                    String filePath = path + MD5.getMD5(url) + ".jpg";
                    if (BitmapUtil.saveBitmap(bitmap, filePath)) {
                        Log.e("PicUtil", "onNewResultImpl: 保存成功");
                        Intent share = new Intent(Intent.ACTION_SEND);
                        share.putExtra(Intent.EXTRA_SUBJECT, "分享");
                        share.putExtra(Intent.EXTRA_TEXT, "有人给你分享了一张图片");
                        share.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        share.setType("image/jpg");
                        File file = new File(filePath);
                        share.putExtra(Intent.EXTRA_STREAM, Uri.fromFile(file));
                        context.startActivity(Intent.createChooser(share, "分享到"));
                    } else {
                        Log.e("PicUtil", "onNewResultImpl: 保存失败");
                    }
                } else {
                    Log.e("PicUtil", "onNewResultImpl: bitmap is null");
                }
            }

            @Override
            public void onFailureImpl(DataSource dataSource) {
                Log.e("PicUtil", "onFailureImpl: 失败");
            }
        }, CallerThreadExecutor.getInstance());
    }
}