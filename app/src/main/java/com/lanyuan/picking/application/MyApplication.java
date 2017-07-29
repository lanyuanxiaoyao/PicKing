package com.lanyuan.picking.application;

import android.app.Application;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.drawee.backends.pipeline.Fresco;
import com.lanyuan.picking.R;

import butterknife.ButterKnife;

public class MyApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        Fresco.initialize(this);
        ToastInstance.builder.init(this);
    }

    public enum ToastInstance {
        builder;

        private Toast toast;
        private TextView textView;

        /**
         * 初始化Toast
         *
         * @param context
         */
        public void init(Context context) {
            View view = LayoutInflater.from(context).inflate(R.layout.toast_view, null);
            textView = (TextView) view.findViewById(R.id.toast_text);
            toast = Toast.makeText(context, "", Toast.LENGTH_SHORT);
            toast.setView(view);
        }

        /**
         * 显示Toast
         *
         * @param content
         * @param duration Toast持续时间
         */
        public void display(CharSequence content, int duration) {
            if (content.length() != 0) {
                textView.setText(content);
                toast.setDuration(duration);
                toast.show();
            }
        }

        /**
         * 停止显示
         */
        public void cancel() {
            toast.cancel();
        }
    }
}
