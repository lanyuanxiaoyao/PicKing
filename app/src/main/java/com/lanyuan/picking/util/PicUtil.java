package com.lanyuan.picking.util;

import android.app.WallpaperManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.support.annotation.NonNull;
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
import com.lanyuan.picking.R;
import com.lanyuan.picking.config.AppConfig;
import com.lanyuan.picking.ui.detail.DetailActivity;
import com.litesuits.common.utils.BitmapUtil;
import com.litesuits.common.utils.FileUtil;
import com.yanzhenjie.permission.AndPermission;
import com.yanzhenjie.permission.Permission;
import com.yanzhenjie.permission.PermissionListener;
import com.yanzhenjie.permission.Rationale;
import com.yanzhenjie.permission.RationaleListener;

import java.io.File;
import java.util.List;

public class PicUtil {
    public static final int SAVE_IMAGE = 0;
    public static final int SHARE_IMAGE = 1;
    public static final int SET_WALLPAPER = 2;

    public static void doFromFresco(final View view, final Context context, final String url, final String path, final Integer pattern) {
        AndPermission.with(context)
                .requestCode(100)
                .permission(Permission.STORAGE)
                .callback(new PermissionListener() {
                    @Override
                    public void onSucceed(int requestCode, @NonNull List<String> grantedPermissions) {
                        switch (pattern) {
                            case SAVE_IMAGE:
                                saveImageFromFresco(view, url, path);
                                break;
                            case SHARE_IMAGE:
                                shareImageFromFresco(view, context, url, path);
                                break;
                            case SET_WALLPAPER:
                                setWallPaperImageFromFresco(view, context, url, path);
                                break;
                        }
                    }

                    @Override
                    public void onFailed(int requestCode, @NonNull List<String> deniedPermissions) {
                        if (AndPermission.hasAlwaysDeniedPermission(context, deniedPermissions))
                            SnackbarUtils.Custom(view, "您已禁止木瓜读取外置存储权限\n请在安全软件中开启", 5000).danger().show();
                        else
                            SnackbarUtils.Long(view, "禁用此权限将无法保存和分享图片和设置壁纸").danger().show();
                    }
                })
                .rationale(new RationaleListener() {
                    @Override
                    public void showRequestPermissionRationale(int requestCode, Rationale rationale) {
                        AndPermission.rationaleDialog(context, rationale).show();
                    }
                })
                .start();
    }

    private static void saveImageFromFresco(final View view, final String url, final String path) {
        SnackbarUtils.Short(view, "正在保存...").info().show();
        ImageRequest imageRequest = ImageRequest.fromUri(url);
        DataSource<CloseableReference<CloseableImage>> dataSource = Fresco.getImagePipeline()
                .fetchDecodedImage(imageRequest, null);
        dataSource.subscribe(new BaseBitmapDataSubscriber() {
            @Override
            public void onNewResultImpl(@Nullable Bitmap bitmap) {
                if (bitmap != null) {
                    String filePath = path + Md5Util.getMD5(url) + ".jpg";
                    ifPathNotExistsAndCreate(path);
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

    private static void shareImageFromFresco(final View view, final Context context, final String url, final String path) {
        ImageRequest imageRequest = ImageRequest.fromUri(url);
        DataSource<CloseableReference<CloseableImage>> dataSource = Fresco.getImagePipeline()
                .fetchDecodedImage(imageRequest, null);
        dataSource.subscribe(new BaseBitmapDataSubscriber() {
            @Override
            public void onNewResultImpl(@Nullable Bitmap bitmap) {
                SnackbarUtils.Short(view, "正在准备分享...").info().show();
                if (bitmap != null) {
                    String filePath = path + Md5Util.getMD5(url) + ".jpg";
                    ifPathNotExistsAndCreate(path);
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

    private static void setWallPaperImageFromFresco(final View view, final Context context, final String url, final String path) {
        SnackbarUtils.Short(view, "正在设置壁纸...").info().show();
        ImageRequest imageRequest = ImageRequest.fromUri(url);
        DataSource<CloseableReference<CloseableImage>> dataSource = Fresco.getImagePipeline()
                .fetchDecodedImage(imageRequest, null);
        dataSource.subscribe(new BaseBitmapDataSubscriber() {
            @Override
            public void onNewResultImpl(@Nullable Bitmap bitmap) {
                if (bitmap != null) {
                    String filePath = path + Md5Util.getMD5(url) + ".jpg";
                    File tempFile = new File(filePath);
                    ifPathNotExistsAndCreate(path);
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

    private static void ifPathNotExistsAndCreate(String path) {
        File file = new File(path);
        if (!file.exists())
            file.mkdirs();
    }
}