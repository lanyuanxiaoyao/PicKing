package com.lanyuan.picking.ui;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.Toolbar;
import android.view.KeyEvent;
import android.view.MenuItem;

import com.lanyuan.picking.R;
import com.lanyuan.picking.config.AppConfig;
import com.lanyuan.picking.ui.dialog.ThemeDialog;
import com.lanyuan.picking.ui.favorite.FavoriteActivity;
import com.lanyuan.picking.ui.setting.SettingActivity;
import com.lanyuan.picking.ui.category.CategoryFragment;
import com.lanyuan.picking.ui.category.CategoryPagerAdapter;
import com.lanyuan.picking.util.AliPayUtil;
import com.lanyuan.picking.util.FavoriteUtil;
import com.lanyuan.picking.util.SPUtils;
import com.lanyuan.picking.util.SnackbarUtils;
import com.lanyuan.picking.util.UpdateUtil;
import com.litesuits.common.assist.Network;
import com.litesuits.common.utils.PackageUtil;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends BaseActivity implements NavigationView.OnNavigationItemSelectedListener {

    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.drawer_layout)
    DrawerLayout drawerLayout;
    @BindView(R.id.nav_view)
    NavigationView navigationView;
    @BindView(R.id.view_pager)
    ViewPager viewPager;
    @BindView(R.id.tab_layout)
    TabLayout tabLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ButterKnife.bind(this);

        setSupportActionBar(toolbar);
        getSupportActionBar().setHomeButtonEnabled(true);

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawerLayout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();

        CategoryPagerAdapter categoryPagerAdapter = new CategoryPagerAdapter(getSupportFragmentManager(), getFragmentList(), getTitleList());
        viewPager.setAdapter(categoryPagerAdapter);
        viewPager.setOffscreenPageLimit(5);
        tabLayout.setupWithViewPager(viewPager);

        navigationView.setItemIconTintList(null);
        navigationView.setNavigationItemSelectedListener(this);

        if (!Network.isConnected(this))
            SnackbarUtils.Long(getWindow().getDecorView(), "当前没有网络连接！！").danger().show();
        else if (!Network.isWifiConnected(this))
            SnackbarUtils.Custom(getWindow().getDecorView(), "当前不在WiFi连接下，请注意流量使用！！", 5000).warning().show();

        firstLaunchAfterUpdate();

        FavoriteUtil.init(this);
    }

    private void firstLaunchAfterUpdate() {
        Integer versionCode = (Integer) SPUtils.get(this, AppConfig.version_code, 0);
        Integer versionCodeNow = PackageUtil.getAppVersionCode(this);
        if (versionCode.compareTo(versionCodeNow) < 0) {
            showTips();
        }
        SPUtils.put(this, AppConfig.version_code, versionCodeNow);
    }

    private void showTips() {
        AlertDialog dialog = new AlertDialog.Builder(this)
                .setTitle("关于木瓜")
                .setMessage(getResources().getString(R.string.tips))
                .setPositiveButton("知道了", null)
                .create();
        dialog.show();
    }

    private List<Fragment> getFragmentList() {
        List<Fragment> fragmentList = new ArrayList<>();
        fragmentList.add(new CategoryFragment().init(AppConfig.anime_patterns));
        fragmentList.add(new CategoryFragment().init(AppConfig.girls_patterns));
        fragmentList.add(new CategoryFragment().init(AppConfig.others_patterns));
        return fragmentList;
    }

    private List<String> getTitleList() {
        List<String> titleList = new ArrayList<>();
        titleList.add("二次元区");
        titleList.add("三次元区");
        titleList.add("四次元区");
        return titleList;
    }

    @Override
    public boolean supportSlideBack() {
        return false;
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
            case R.id.nav_favorite:
                startActivity(new Intent(this, FavoriteActivity.class));
                break;
            case R.id.nav_setting:
                startActivity(new Intent(this, SettingActivity.class));
                break;
            case R.id.nav_theme:
                new ThemeDialog(this, this).show();
                break;
            case R.id.nav_update:
                UpdateUtil.goToAppMarket(this, getWindow().getDecorView());
                break;
            case R.id.nav_donate:
                if (!AliPayUtil.goAliPay(this))
                    SnackbarUtils.Short(getWindow().getDecorView(), "设备上没有安装支付宝").danger().show();
                break;
            case R.id.nav_about:
                startActivity(new Intent(this, AboutActivity.class));
                break;
        }
        drawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }

    long LastTime = 0, NowTime = 0;

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            NowTime = System.currentTimeMillis();
            if (NowTime - LastTime < 1000) {
                finish();
            } else {
                SnackbarUtils.Short(getWindow().getDecorView(), "再点一次返回键退出").show();
            }
            LastTime = NowTime;
        }

        return false;
    }
}
