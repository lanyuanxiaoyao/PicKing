package com.lanyuan.picking.ui.dialog;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.support.design.widget.BottomSheetBehavior;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.AppCompatImageButton;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.facebook.drawee.drawable.ProgressBarDrawable;
import com.lanyuan.picking.R;
import com.lanyuan.picking.common.bean.PicInfo;
import com.lanyuan.picking.config.AppConfig;
import com.lanyuan.picking.ui.zoomableView.OnViewTapListener;
import com.lanyuan.picking.ui.zoomableView.PhotoDraweeView;
import com.lanyuan.picking.util.FavoriteUtil;
import com.lanyuan.picking.util.PicUtil;
import com.lanyuan.picking.util.SPUtils;
import com.lanyuan.picking.util.SnackbarUtils;
import com.lanyuan.picking.util.StatusBarUtil;

import java.util.List;

import butterknife.BindView;
import butterknife.BindViews;
import butterknife.ButterKnife;
import cn.lankton.flowlayout.FlowLayout;

public class PicDialog extends Dialog implements View.OnClickListener {

    private PicInfo picInfo;

    @BindView(R.id.pic_view)
    PhotoDraweeView photoDraweeView;
    @BindView(R.id.pic_title)
    TextView picTitle;
    @BindView(R.id.pic_time)
    TextView picTime;
    @BindView(R.id.tags)
    FlowLayout tags;

    private BottomSheetBehavior behavior;

    @BindViews({R.id.love_button, R.id.download_button, R.id.share_button, R.id.wallpaper_button})
    List<AppCompatImageButton> imageButtons;

    public PicDialog(Context context) {
        super(context, R.style.AppNoActionBarTheme);
        setOwnerActivity((Activity) context);
        setContentView(R.layout.dialog_pic);

        StatusBarUtil.MIUISetStatusBarLightMode(getOwnerActivity(), true);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            View decorView = getWindow().getDecorView();
            int option = View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                    | View.SYSTEM_UI_FLAG_LAYOUT_STABLE;
            decorView.setSystemUiVisibility(option);
            getWindow().setStatusBarColor(Color.TRANSPARENT);
        }

        ButterKnife.bind(this);

        if ((boolean) SPUtils.get(getContext(), AppConfig.click_to_back, false))
            photoDraweeView.setOnViewTapListener(new OnViewTapListener() {
                @Override
                public void onViewTap(View view, float x, float y) {
                    dismiss();
                }
            });

        photoDraweeView.setAllowParentInterceptOnEdge(false);
        photoDraweeView.setEnableDraweeMatrix(false);

        for (AppCompatImageButton imageButton : imageButtons)
            imageButton.setOnClickListener(this);

        getWindow().setWindowAnimations(R.style.dialogStyle);

        behavior = BottomSheetBehavior.from(findViewById(R.id.bottom_view));
    }


    public void show(final PicInfo picInfo) {
        if (picInfo != null && !"".equals(picInfo.getPicUrl())) {
            this.picInfo = picInfo;
            photoDraweeView.getHierarchy().setProgressBarImage(new ProgressBarDrawable());
            photoDraweeView.setPhotoUri(Uri.parse(picInfo.getPicUrl()));
        }
        if (picInfo.getTitle() != null)
            picTitle.setText(picInfo.getTitle());
        if (picInfo.getTime() != null)
            picTime.setText(picInfo.getTime());
        if (picInfo.getTags() != null) {
            tags.removeAllViews();
            for (String s : picInfo.getTags()) {
                TextView t = new TextView(getOwnerActivity());
                t.setText(s);
                ViewGroup.MarginLayoutParams layoutParams = new ViewGroup.MarginLayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                layoutParams.setMarginEnd(15);
                t.setLayoutParams(layoutParams);
                t.setPadding(10, 10, 10, 10);
                t.setBackgroundColor(Color.GRAY);
                t.setTextColor(Color.WHITE);
                tags.addView(t);
            }
        }
        this.show();
    }

    @Override
    public void dismiss() {
        behavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
        super.dismiss();
    }

    @Override
    public void onBackPressed() {
        if (behavior.getState() == BottomSheetBehavior.STATE_EXPANDED)
            behavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
        else
            super.onBackPressed();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.love_button:
                if (FavoriteUtil.add(getOwnerActivity(), picInfo))
                    SnackbarUtils.Short(getWindow().getDecorView(), "收藏成功").confirm().show();
                else
                    SnackbarUtils.Short(getWindow().getDecorView(), "已收藏过这张图片了").warning().show();
                break;
            case R.id.download_button:
                PicUtil.doFromFresco(getWindow().getDecorView(), getOwnerActivity(), picInfo.getPicUrl(), (String) SPUtils.get(getOwnerActivity(), AppConfig.download_path, AppConfig.DOWNLOAD_PATH), 0);
                break;
            case R.id.share_button:
                PicUtil.doFromFresco(getWindow().getDecorView(), getOwnerActivity(), picInfo.getPicUrl(), (String) SPUtils.get(getOwnerActivity(), AppConfig.download_path, AppConfig.DOWNLOAD_PATH), 1);
                break;
            case R.id.wallpaper_button:
                new AlertDialog.Builder(getOwnerActivity())
                        .setTitle("确定要把图片设为壁纸吗？")
                        .setMessage(getOwnerActivity().getResources().getString(R.string.wallpaper_tips))
                        .setNegativeButton("取消", null)
                        .setPositiveButton("设为壁纸", new OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                PicUtil.doFromFresco(getWindow().getDecorView(), getOwnerActivity(), picInfo.getPicUrl(), (String) SPUtils.get(getOwnerActivity(), AppConfig.download_path, AppConfig.DOWNLOAD_PATH), 2);
                            }
                        })
                        .show();
                break;
        }
    }
}
