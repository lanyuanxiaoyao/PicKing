package com.lanyuan.picking.ui;

import android.content.Context;
import android.support.v7.widget.AppCompatTextView;
import android.util.AttributeSet;

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
        setText("正在拼命加载数据...");
    }

    @Override
    public void onPrepare() {
        setText("");
    }

    @Override
    public void onMove(int y, boolean isComplete, boolean automatic) {
        if (!isComplete) {
            if (y > getHeight()) {
                setText("释放刷新");
            } else if (y < getHeight()) {
                setText("滑动即可刷新");
            }
        } else {
            setText("刷新返回");
        }
    }

    @Override
    public void onRelease() {
        setText("");
    }

    @Override
    public void onComplete() {
        setText("刷新成功");
    }

    @Override
    public void onReset() {
        setText("");
    }
}
