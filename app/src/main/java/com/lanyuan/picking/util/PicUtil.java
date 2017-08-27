package com.lanyuan.picking.util;

import android.app.WallpaperManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.view.View;

import com.facebook.common.executors.CallerThreadExecutor;
import com.facebook.common.internal.Closeables;
import com.facebook.common.memory.PooledByteBuffer;
import com.facebook.common.memory.PooledByteBufferInputStream;
import com.facebook.common.references.CloseableReference;
import com.facebook.datasource.BaseDataSubscriber;
import com.facebook.datasource.DataSource;
import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.imagepipeline.datasource.BaseBitmapDataSubscriber;
import com.facebook.imagepipeline.image.CloseableImage;
import com.facebook.imagepipeline.request.ImageRequest;
import com.lanyuan.picking.config.AppConfig;
import com.lanyuan.picking.util.common.common.utils.BitmapUtil;
import com.yanzhenjie.permission.AndPermission;
import com.yanzhenjie.permission.Permission;
import com.yanzhenjie.permission.PermissionListener;
import com.yanzhenjie.permission.Rationale;
import com.yanzhenjie.permission.RationaleListener;
import com.zhy.base.fileprovider.FileProvider7;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
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
                                saveFromFresco(view, context, url, path);
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

    private static void saveFromFresco(final View view, final Context context, final String url, final String path) {
        SnackbarUtils.Indefinite(view, "正在保存……").info().show();
        ImageRequest imageRequest = ImageRequest.fromUri(url);
        DataSource<CloseableReference<PooledByteBuffer>> dataSource = Fresco.getImagePipeline()
                .fetchEncodedImage(imageRequest, null);
        dataSource.subscribe(new BaseDataSubscriber<CloseableReference<PooledByteBuffer>>() {
            @Override
            protected void onNewResultImpl(DataSource<CloseableReference<PooledByteBuffer>> dataSource) {
                if (!dataSource.isFinished()) {
                    SnackbarUtils.Short(view, "保存失败").danger().show();
                }
                CloseableReference reference = dataSource.getResult();
                if (reference != null) {
                    try {
                        String filename = Md5Util.getMD5(url) + "." + getPictureType(url);
                        String filePath = path + filename;
                        ifPathNotExistsAndCreate(path);
                        PooledByteBuffer result = (PooledByteBuffer) reference.get();
                        InputStream inputStream = new PooledByteBufferInputStream(result);
                        try {
                            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream(1024);
                            byte[] bytes = new byte[1024];
                            int n;
                            while ((n = inputStream.read(bytes)) != -1) {
                                byteArrayOutputStream.write(bytes, 0, n);
                            }
                            inputStream.close();
                            byteArrayOutputStream.close();
                            File file = new File(filePath);
                            if (!file.exists())
                                file.createNewFile();
                            DataOutputStream dataOutputStream = new DataOutputStream(new FileOutputStream(file));
                            byteArrayOutputStream.writeTo(dataOutputStream);
                            SnackbarUtils.Short(view, "保存成功").confirm().show();
                        } catch (Exception e) {
                            SnackbarUtils.Short(view, "保存失败").danger().show();
                        } finally {
                            Closeables.closeQuietly(inputStream);
                        }
                    } finally {
                        CloseableReference.closeSafely(reference);
                        reference = null;
                    }
                }
            }

            @Override
            protected void onFailureImpl(DataSource<CloseableReference<PooledByteBuffer>> dataSource) {
                SnackbarUtils.Short(view, "保存失败").danger().show();
            }
        }, CallerThreadExecutor.getInstance());
    }

    private static void shareImageFromFresco(final View view, final Context context, final String url, final String path) {
        ImageRequest imageRequest = ImageRequest.fromUri(url);
        DataSource<CloseableReference<PooledByteBuffer>> dataSource = Fresco.getImagePipeline()
                .fetchEncodedImage(imageRequest, null);
        dataSource.subscribe(new BaseDataSubscriber<CloseableReference<PooledByteBuffer>>() {
            Snackbar snackbar = SnackbarUtils.Indefinite(view, "正在准备分享...").info().getSnackbar();

            @Override
            protected void onNewResultImpl(DataSource<CloseableReference<PooledByteBuffer>> dataSource) {
                if (!dataSource.isFinished()) {
                    SnackbarUtils.Short(view, "分享失败").danger().show();
                }
                CloseableReference reference = dataSource.getResult();
                if (reference != null) {
                    try {
                        String sharePath = path + "share" + File.separatorChar;
                        String filePath = sharePath + Md5Util.getMD5(url) + "." + getPictureType(url);
                        ifPathNotExistsAndCreate(sharePath);
                        if ((boolean) SPUtils.get(context, AppConfig.share_model, false) == false) {
                            snackbar.dismiss();
                            Intent share = new Intent(Intent.ACTION_SEND);
                            share.setType("text/plain");
                            share.putExtra(Intent.EXTRA_TEXT, "有人给你分享了一张图片:" + url);
                            context.startActivity(Intent.createChooser(share, "分享到"));
                        } else {
                            PooledByteBuffer result = (PooledByteBuffer) reference.get();
                            InputStream inputStream = new PooledByteBufferInputStream(result);
                            try {
                                ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream(1024);
                                byte[] bytes = new byte[1024];
                                int n;
                                while ((n = inputStream.read(bytes)) != -1) {
                                    byteArrayOutputStream.write(bytes, 0, n);
                                }
                                inputStream.close();
                                byteArrayOutputStream.close();
                                File file = new File(filePath);
                                if (!file.exists())
                                    file.createNewFile();
                                DataOutputStream dataOutputStream = new DataOutputStream(new FileOutputStream(file));
                                byteArrayOutputStream.writeTo(dataOutputStream);

                                snackbar.dismiss();
                                Intent share = new Intent(Intent.ACTION_SEND);
                                share.putExtra(Intent.EXTRA_SUBJECT, "分享");
                                share.putExtra(Intent.EXTRA_TEXT, "有人给你分享了一张图片");
                                share.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                share.setType("image/jpg");
                                share.putExtra(Intent.EXTRA_STREAM, Uri.fromFile(file));
                                context.startActivity(Intent.createChooser(share, "分享到"));
                                context.sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, FileProvider7.getUriForFile(context, new File(filePath))));
                            } catch (Exception e) {
                                SnackbarUtils.Short(view, "分享失败").danger().show();
                            } finally {
                                Closeables.closeQuietly(inputStream);
                            }
                        }
                    } finally {
                        CloseableReference.closeSafely(reference);
                        reference = null;
                    }
                }
            }

            @Override
            protected void onFailureImpl(DataSource<CloseableReference<PooledByteBuffer>> dataSource) {
                SnackbarUtils.Short(view, "分享失败").danger().show();
            }
        }, CallerThreadExecutor.getInstance());
    }

    private static void setWallPaperImageFromFresco(final View view, final Context context, final String url, final String path) {
        SnackbarUtils.Indefinite(view, "正在设置壁纸...").info().show();
        if (getPictureType(url).equals("gif"))
            SnackbarUtils.Long(view, "GIF图不支持设置为壁纸").danger().show();
        ImageRequest imageRequest = ImageRequest.fromUri(url);
        DataSource<CloseableReference<CloseableImage>> dataSource = Fresco.getImagePipeline()
                .fetchDecodedImage(imageRequest, null);
        dataSource.subscribe(new BaseBitmapDataSubscriber() {
            @Override
            public void onNewResultImpl(@Nullable Bitmap bitmap) {
                if (bitmap != null) {
                    String sharePath = path + "wallpaper" + File.separatorChar;
                    String filePath = sharePath + Md5Util.getMD5(url) + "." + getPictureType(url);
                    ifPathNotExistsAndCreate(sharePath);
                    if (BitmapUtil.saveBitmap(bitmap, filePath)) {
                        try {
                            WallpaperManager wallpaper = WallpaperManager.getInstance(context);
                            wallpaper.setBitmap(bitmap);
                            SnackbarUtils.Short(view, "设置成功").confirm().show();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        context.sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, FileProvider7.getUriForFile(context, new File(filePath))));
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

    public static String getPictureType(String pictureName) {
        if (pictureName.endsWith("png") || pictureName.endsWith("PNG"))
            return "png";
        else if (pictureName.endsWith("gif") || pictureName.endsWith("GIF"))
            return "gif";
        else
            return "jpg";
    }
}