package com.lanyuan.picking.common;

import android.graphics.Bitmap;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.Log;

import com.facebook.common.executors.CallerThreadExecutor;
import com.facebook.common.references.CloseableReference;
import com.facebook.datasource.DataSource;
import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.imagepipeline.datasource.BaseBitmapDataSubscriber;
import com.facebook.imagepipeline.image.CloseableImage;
import com.facebook.imagepipeline.request.ImageRequest;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

public class PicUtil {

    public static void saveImageFromFresco(String url, final String path) {
        ImageRequest imageRequest = ImageRequest.fromUri(url);
        DataSource<CloseableReference<CloseableImage>> dataSource = Fresco.getImagePipeline()
                .fetchDecodedImage(imageRequest, null);

        dataSource.subscribe(new BaseBitmapDataSubscriber() {
            @Override
            public void onNewResultImpl(@Nullable Bitmap bitmap) {
                if (bitmap != null) {
                    Boolean result = saveBitmap(bitmap, path);
                    if (result) {

                    } else {

                    }

                } else {

                }
            }

            @Override
            public void onFailureImpl(DataSource dataSource) {
                Log.e("PicUtil", "onFailureImpl: 失败");
            }
        }, CallerThreadExecutor.getInstance());
    }

    public static Boolean saveBitmap(Bitmap bitmap, String Path) {

        if (TextUtils.isEmpty(Path)) {
            throw new NullPointerException("保存的路径不能为空");
        }

        File file = new File(Path);
        if (file.exists()) {
            file.delete();
        }
        try {
            file.createNewFile();
        } catch (IOException e) {
            e.printStackTrace();
        }

        try {
            FileOutputStream out = new FileOutputStream(file);
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, out);
            out.flush();
            out.close();

        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return false;

        } catch (IOException e) {

            e.printStackTrace();
            return false;
        }

        return true;

    }

}
