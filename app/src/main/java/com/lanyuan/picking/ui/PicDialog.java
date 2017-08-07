package com.lanyuan.picking.ui;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.AppCompatImageButton;
import android.util.Log;
import android.view.View;
import android.view.Window;

import com.facebook.drawee.drawable.ProgressBarDrawable;
import com.lanyuan.picking.R;
import com.lanyuan.picking.config.AppConfig;
import com.lanyuan.picking.ui.detail.DetailActivity;
import com.lanyuan.picking.util.PicUtil;
import com.lanyuan.picking.util.SnackbarUtils;

import java.util.List;

import butterknife.BindView;
import butterknife.BindViews;
import butterknife.ButterKnife;
import me.relex.photodraweeview.OnViewTapListener;
import me.relex.photodraweeview.PhotoDraweeView;

public class PicDialog extends Dialog implements View.OnClickListener {

    private String url;

    @BindView(R.id.pic_view)
    PhotoDraweeView photoDraweeView;

    @BindViews({R.id.love_button, R.id.download_button, R.id.share_button, R.id.wallpaper_button})
    List<AppCompatImageButton> imageButtons;

    public PicDialog(Context context) {
        super(context, R.style.AppNoActionBarDarkTheme);
        setOwnerActivity((Activity) context);
        setContentView(R.layout.pic_dialog);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            View decorView = getWindow().getDecorView();
            int option = View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                    | View.SYSTEM_UI_FLAG_LAYOUT_STABLE;
            decorView.setSystemUiVisibility(option);
            getWindow().setStatusBarColor(Color.TRANSPARENT);
        }

        ButterKnife.bind(this);

        if ((boolean) AppConfig.getByResourceId(getContext(), R.string.click_to_back, false))
            photoDraweeView.setOnViewTapListener(new OnViewTapListener() {
                @Override
                public void onViewTap(View view, float x, float y) {
                    dismiss();
                }
            });

        for (AppCompatImageButton imageButton : imageButtons)
            imageButton.setOnClickListener(this);

        Window window = this.getWindow();
        window.setWindowAnimations(R.style.dialogStyle);
    }

    public void show(final String url) {
        if (url != null && !"".equals(url)) {
            this.url = url;
            photoDraweeView.getHierarchy().setProgressBarImage(new ProgressBarDrawable());
            photoDraweeView.setPhotoUri(Uri.parse(url));
        }
        this.show();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.love_button:
                SnackbarUtils.Short(getWindow().getDecorView(), "收藏功能还在开发当中……").info().show();
                break;
            case R.id.download_button:
                PicUtil.doFromFresco(getWindow().getDecorView(), getOwnerActivity(), url, (String) AppConfig.getByResourceId(getOwnerActivity(), R.string.download_path, AppConfig.DOWNLOAD_PATH), 0);
                break;
            case R.id.share_button:
                PicUtil.doFromFresco(getWindow().getDecorView(), getOwnerActivity(), url, (String) AppConfig.getByResourceId(getOwnerActivity(), R.string.download_path, AppConfig.DOWNLOAD_PATH), 1);
                break;
            case R.id.wallpaper_button:
                new AlertDialog.Builder(getOwnerActivity())
                        .setTitle("确定要把图片设为壁纸吗？")
                        .setNegativeButton("取消", null)
                        .setPositiveButton("设为壁纸", new OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                PicUtil.doFromFresco(getWindow().getDecorView(), getOwnerActivity(), url, (String) AppConfig.getByResourceId(getOwnerActivity(), R.string.download_path, AppConfig.DOWNLOAD_PATH), 2);
                            }
                        })
                        .show();
                break;
        }
    }
}
