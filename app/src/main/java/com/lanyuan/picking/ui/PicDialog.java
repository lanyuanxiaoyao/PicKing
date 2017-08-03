package com.lanyuan.picking.ui;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.View;
import android.view.Window;

import com.lanyuan.picking.R;
import com.lanyuan.picking.config.AppConfig;
import com.lanyuan.picking.ui.detail.DetailActivity;
import com.lanyuan.picking.util.PicUtil;

import butterknife.BindView;
import butterknife.ButterKnife;
import me.relex.photodraweeview.OnViewTapListener;
import me.relex.photodraweeview.PhotoDraweeView;

public class PicDialog extends Dialog {

    @BindView(R.id.pic_view)
    PhotoDraweeView photoDraweeView;

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

        Window window = this.getWindow();
        window.setWindowAnimations(R.style.dialogStyle);
    }

    public void show(final String url, int position) {
        if (url != null && !"".equals(url)) {
            photoDraweeView.setPhotoUri(Uri.parse(url));
            photoDraweeView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View view) {
                    String[] items = {"保存", "分享"};
                    AlertDialog dialog = new AlertDialog.Builder(getOwnerActivity())
                            .setItems(items, new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int index) {
                                    Log.e("DetailActivity", "onClick: " + index);
                                    switch (index) {
                                        case 0:
                                            PicUtil.saveImageFromFresco(getWindow().getDecorView(), url, (String) AppConfig.getByResourceId(getOwnerActivity(), R.string.download_path, AppConfig.DOWNLOAD_PATH));
                                            break;
                                        case 1:
                                            PicUtil.shareImageFromFresco(getOwnerActivity(), url, (String) AppConfig.getByResourceId(getOwnerActivity(), R.string.download_path, AppConfig.DOWNLOAD_PATH));
                                            break;

                                    }
                                }
                            })
                            .create();
                    dialog.show();
                    return false;
                }
            });
        }
        this.show();
    }
}
