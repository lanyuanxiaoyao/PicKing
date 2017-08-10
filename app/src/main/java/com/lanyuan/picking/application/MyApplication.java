package com.lanyuan.picking.application;

import android.app.Application;

import com.aitangba.swipeback.ActivityLifecycleHelper;
import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.imagepipeline.backends.okhttp3.OkHttpImagePipelineConfigFactory;
import com.facebook.imagepipeline.core.ImagePipelineConfig;

import okhttp3.OkHttpClient;

public class MyApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        OkHttpClient httpClient = new OkHttpClient();
        ImagePipelineConfig config = OkHttpImagePipelineConfigFactory.newBuilder(this, httpClient)
                .setDownsampleEnabled(true)
                .build();
        Fresco.initialize(this, config);
        registerActivityLifecycleCallbacks(ActivityLifecycleHelper.build());
    }
}
