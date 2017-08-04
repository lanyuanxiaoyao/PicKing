package com.lanyuan.picking.application;

import android.app.Application;
import android.content.Context;
import android.support.design.widget.Snackbar;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.aitangba.swipeback.ActivityLifecycleHelper;
import com.facebook.drawee.backends.pipeline.Fresco;
import com.lanyuan.picking.R;

public class MyApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        Fresco.initialize(this);
        registerActivityLifecycleCallbacks(ActivityLifecycleHelper.build());
    }
}
