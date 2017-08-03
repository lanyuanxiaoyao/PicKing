package com.lanyuan.picking.ui;

import android.content.Context;
import android.support.v7.widget.AppCompatTextView;
import android.util.AttributeSet;
import android.util.Log;

import com.aspsine.swipetoloadlayout.SwipeRefreshTrigger;
import com.aspsine.swipetoloadlayout.SwipeTrigger;

public class RefreshView extends AppCompatTextView implements SwipeRefreshTrigger, SwipeTrigger {

    public RefreshView(Context context) {
        super(context);
    }

    public RefreshView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public RefreshView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    public void onRefresh() {
        setText("onRefresh");
    }

    @Override
    public void onPrepare() {
        setText("onPrepare");
    }

    @Override
    public void onMove(int y, boolean isComplete, boolean automatic) {
        if (!isComplete) {
            y = Math.abs(y);
            if (y > getHeight()) {
                setText("松手刷新");
            } else if (y < getHeight()) {
                setText("下拉刷新");
            }
        } else {
            setText("加载完成");
        }
    }

    @Override
    public void onRelease() {
        setText("onRelease");
    }

    @Override
    public void onComplete() {
        setText("加载完成");
    }

    @Override
    public void onReset() {
        setText("onReset");
    }
}
