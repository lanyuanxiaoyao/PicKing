package com.lanyuan.picking;

import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Build;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;

import com.lanyuan.picking.common.BaseActivity;
import com.lanyuan.picking.common.ContentsActivity;
import com.lanyuan.picking.common.PicDialog;
import com.lanyuan.picking.config.AppConfig;
import com.lanyuan.picking.pattern.BasePattern;
import com.lanyuan.picking.pattern.MM131.MM131Pattern;
import com.lanyuan.picking.pattern.RoseMM.RosiMMPattern;
import com.lanyuan.picking.pattern.XiuMM.XiuMMPattern;
import com.lanyuan.picking.setting.SettingActivity;

import java.util.List;

import butterknife.BindView;
import butterknife.BindViews;
import butterknife.ButterKnife;
import me.relex.photodraweeview.OnPhotoTapListener;
import me.relex.photodraweeview.OnViewTapListener;
import me.relex.photodraweeview.PhotoDraweeView;

public class MainActivity extends BaseActivity implements View.OnClickListener, NavigationView.OnNavigationItemSelectedListener {

    @BindViews({R.id.mm131, R.id.xiumm, R.id.rosimm})
    List<ImageView> imageViews;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.drawer_layout)
    DrawerLayout drawerLayout;
    @BindView(R.id.nav_view)
    NavigationView navigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ButterKnife.bind(this);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            WindowManager.LayoutParams localLayoutParams = getWindow().getAttributes();
            localLayoutParams.flags = (WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS | localLayoutParams.flags);
            if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
                drawerLayout.setFitsSystemWindows(true);
                drawerLayout.setClipToPadding(false);
            }
        }

        AppConfig.init(this);

        setSupportActionBar(toolbar);
        getSupportActionBar().setHomeButtonEnabled(true);

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawerLayout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();

        navigationView.setNavigationItemSelectedListener(this);

        for (ImageView imageView : imageViews)
            imageView.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.mm131:
                startActivity(new MM131Pattern());
                break;
            case R.id.xiumm:
                startActivity(new XiuMMPattern());
                break;
            case R.id.rosimm:
                startActivity(new RosiMMPattern());
                break;
        }
    }

    private void startActivity(BasePattern pattern) {
        Intent intent = new Intent(MainActivity.this, ContentsActivity.class);
        Bundle bundle = new Bundle();
        bundle.putSerializable("pattern", pattern);
        intent.putExtras(bundle);
        startActivity(intent);
    }

    @Override
    public void onBackPressed() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();

        switch (id) {
            case R.id.nav_setting:
                startActivity(new Intent(MainActivity.this, SettingActivity.class));
                break;
            case R.id.nav_donate:
                Intent intent = new Intent();
                intent.setAction("android.intent.action.VIEW");
                Uri qrcode_url = Uri.parse("alipayqr://platformapi/startapp?saId=10000007&clientVersion=3.7.0.0718&qrcode=https%3A%2F%2Fqr.alipay.com%2FFKX06852EOBWRJPACOMCF5%3F_s%3Dweb-other");
                intent.setData(qrcode_url);
                intent.setClassName("com.eg.android.AlipayGphone", "com.alipay.mobile.quinox.LauncherActivity");
                startActivity(intent);
                break;
            case R.id.nav_about:
                startActivity(new Intent(MainActivity.this, AboutActivity.class));
                break;
        }
        drawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }
}
