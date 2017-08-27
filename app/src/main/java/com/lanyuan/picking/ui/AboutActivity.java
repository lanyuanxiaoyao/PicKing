package com.lanyuan.picking.ui;

import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;

import com.lanyuan.picking.R;
import com.lanyuan.picking.util.AliPayUtil;
import com.lanyuan.picking.util.SnackbarUtils;
import com.lanyuan.picking.util.common.common.utils.PackageUtil;

import butterknife.BindView;
import butterknife.ButterKnife;

public class AboutActivity extends BaseActivity {

    @BindView(R.id.toolbar)
    Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);

        ButterKnife.bind(this);

        toolbar.setTitle("关于  v " + PackageUtil.getAppPackageInfo(this).versionName);
        setSupportActionBar(toolbar);

        TextView alipay = (TextView) findViewById(R.id.alipay);
        alipay.setTextIsSelectable(true);
        alipay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!AliPayUtil.goAliPay(AboutActivity.this))
                    SnackbarUtils.Short(getWindow().getDecorView(), "设备上没有安装支付宝").danger().show();
            }
        });
    }
}
