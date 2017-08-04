package com.lanyuan.picking.ui.setting;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.Toolbar;

import com.lanyuan.picking.R;
import com.lanyuan.picking.ui.BaseActivity;
import com.lanyuan.picking.config.AppConfig;

import butterknife.BindView;
import butterknife.ButterKnife;

public class SettingActivity extends BaseActivity {

    @BindView(R.id.toolbar)
    Toolbar toolbar;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);

        ButterKnife.bind(this);

        getFragmentManager().beginTransaction()
                .replace(R.id.setting_container, new SettingFragment())
                .commit();

        toolbar.setTitle("设置");
        setSupportActionBar(toolbar);
        getSupportActionBar().setHomeButtonEnabled(true);
    }

    @Override
    public void onBackPressed() {
        AppConfig.init(this);
        super.onBackPressed();
    }
}
