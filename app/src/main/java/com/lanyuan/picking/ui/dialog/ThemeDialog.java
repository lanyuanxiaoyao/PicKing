package com.lanyuan.picking.ui.dialog;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.AppCompatButton;
import android.view.View;

import com.lanyuan.picking.R;
import com.lanyuan.picking.config.AppConfig;
import com.lanyuan.picking.ui.BaseActivity;

import java.util.List;

import butterknife.BindViews;
import butterknife.ButterKnife;

public class ThemeDialog extends Dialog implements View.OnClickListener {

    @BindViews({R.id.theme_default, R.id.theme_blue, R.id.theme_orange, R.id.theme_brown, R.id.theme_purple, R.id.theme_cyan, R.id.theme_green, R.id.theme_grey, R.id.theme_yellow})
    List<AppCompatButton> buttons;

    private BaseActivity activity;

    public ThemeDialog(@NonNull Context context, BaseActivity activity) {
        super(context, R.style.Theme_AppCompat_Dialog_MinWidth);
        setOwnerActivity((Activity) context);
        setContentView(R.layout.dialog_theme);

        ButterKnife.bind(this);

        this.activity = activity;

        for (AppCompatButton button : buttons)
            button.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.theme_default:
                changeTheme(AppConfig.DEFAULT_COLOR);
                break;
            case R.id.theme_blue:
                changeTheme(AppConfig.BLUE_COLOR);
                break;
            case R.id.theme_orange:
                changeTheme(AppConfig.ORANGE_COLOR);
                break;
            case R.id.theme_brown:
                changeTheme(AppConfig.BROWN_COLOR);
                break;
            case R.id.theme_purple:
                changeTheme(AppConfig.PURPLE_COLOR);
                break;
            case R.id.theme_cyan:
                changeTheme(AppConfig.CYAN_COLOR);
                break;
            case R.id.theme_green:
                changeTheme(AppConfig.GREEN_COLOR);
                break;
            case R.id.theme_yellow:
                changeTheme(AppConfig.YELLOW_COLOR);
                break;
            case R.id.theme_grey:
                changeTheme(AppConfig.GREY_COLOR);
                break;
        }
    }

    private void changeTheme(String theme) {
        dismiss();
        activity.changeTheme(theme);
        activity.finish();
        activity.startActivity(activity.getIntent());
    }
}
