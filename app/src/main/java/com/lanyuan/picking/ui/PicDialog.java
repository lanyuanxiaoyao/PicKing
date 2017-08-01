package com.lanyuan.picking.ui;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.util.Log;
import android.view.View;
import android.view.Window;

import com.lanyuan.picking.R;
import com.lanyuan.picking.config.AppConfig;

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

    public void show(String url) {
        if (url != null && !"".equals(url))
            photoDraweeView.setPhotoUri(Uri.parse(url));

        this.show();
    }
}
