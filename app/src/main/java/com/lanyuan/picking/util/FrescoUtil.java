package com.lanyuan.picking.util;

import android.graphics.drawable.Animatable;
import android.net.Uri;
import android.support.annotation.Nullable;
import android.view.ViewGroup;

import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.drawee.controller.BaseControllerListener;
import com.facebook.drawee.controller.ControllerListener;
import com.facebook.drawee.interfaces.DraweeController;
import com.facebook.drawee.view.SimpleDraweeView;
import com.facebook.imagepipeline.image.ImageInfo;
import com.facebook.imagepipeline.postprocessors.IterativeBoxBlurPostProcessor;
import com.facebook.imagepipeline.request.ImageRequest;
import com.facebook.imagepipeline.request.ImageRequestBuilder;
import com.lanyuan.picking.common.bean.BaseInfo;
import com.lanyuan.picking.common.bean.PicInfo;

public class FrescoUtil {

    /**
     * 按照图片宽度适应高度
     *
     * @param simpleDraweeView
     * @param baseInfo
     * @param imageWidth
     */
    public static void setControllerListener(final SimpleDraweeView simpleDraweeView, final BaseInfo baseInfo, final int imageWidth, boolean isGif) {
        final ViewGroup.LayoutParams layoutParams = simpleDraweeView.getLayoutParams();
        ControllerListener controllerListener = new BaseControllerListener<ImageInfo>() {
            @Override
            public void onFinalImageSet(String id, @Nullable ImageInfo imageInfo, @Nullable Animatable anim) {
                if (imageInfo == null) {
                    return;
                }
                int height = imageInfo.getHeight();
                int width = imageInfo.getWidth();
                layoutParams.width = imageWidth;
                layoutParams.height = (int) ((float) (imageWidth * height) / (float) width);
                baseInfo.setWidth(layoutParams.width);
                baseInfo.setHeight(layoutParams.height);
                simpleDraweeView.setLayoutParams(layoutParams);
                simpleDraweeView.setTag(baseInfo.getPicUrl());
            }

            @Override
            public void onIntermediateImageSet(String id, @Nullable ImageInfo imageInfo) {
            }

            @Override
            public void onFailure(String id, Throwable throwable) {
                throwable.printStackTrace();
            }
        };
        DraweeController controller = Fresco.newDraweeControllerBuilder()
                .setOldController(simpleDraweeView.getController())
                .setControllerListener(controllerListener)
                .setUri(Uri.parse(isGif ? baseInfo.getPicUrl() : baseInfo.getGifThumbUrl()))
                .setAutoPlayAnimations(true)
                .build();
        simpleDraweeView.setController(controller);
    }

    public static void setBlurFrescoController(SimpleDraweeView simpleDraweeView, String url, int iterations, int blurRadius) {
        try {
            Uri uri = Uri.parse(url);
            ImageRequest request = ImageRequestBuilder.newBuilderWithSource(uri)
                    .setPostprocessor(new IterativeBoxBlurPostProcessor(iterations, blurRadius))
                    .build();
            DraweeController controller = Fresco.newDraweeControllerBuilder()
                    .setOldController(simpleDraweeView.getController())
                    .setImageRequest(request)
                    .build();
            simpleDraweeView.setController(controller);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}