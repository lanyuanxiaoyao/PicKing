package com.lanyuan.picking;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.preference.PreferenceManager;
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
import android.util.Log;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.Window;
import android.view.WindowManager;

import com.lanyuan.picking.pattern.anime.AoJiao;
import com.lanyuan.picking.pattern.anime.Yande;
import com.lanyuan.picking.pattern.anime.ZeroChan;
import com.lanyuan.picking.pattern.custom.DuowanCos;
import com.lanyuan.picking.pattern.custom.Yesky;
import com.lanyuan.picking.ui.AboutActivity;
import com.lanyuan.picking.ui.BaseActivity;
import com.lanyuan.picking.config.AppConfig;
import com.lanyuan.picking.pattern.anime.Acg12;
import com.lanyuan.picking.pattern.anime.Apic;
import com.lanyuan.picking.pattern.BasePattern;
import com.lanyuan.picking.pattern.custom.MM131;
import com.lanyuan.picking.pattern.custom.RosiMM;
import com.lanyuan.picking.pattern.custom.XiuMM;
import com.lanyuan.picking.ui.dialog.ThemeDialog;
import com.lanyuan.picking.ui.favorite.FavoriteActivity;
import com.lanyuan.picking.ui.setting.SettingActivity;
import com.lanyuan.picking.ui.category.CategoryFragment;
import com.lanyuan.picking.ui.category.CategoryPagerAdapter;
import com.lanyuan.picking.util.AliPayUtil;
import com.lanyuan.picking.util.FavoriteUtil;
import com.lanyuan.picking.util.SPUtils;
import com.lanyuan.picking.util.SnackbarUtils;
import com.litesuits.common.assist.Network;

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

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            WindowManager.LayoutParams localLayoutParams = getWindow().getAttributes();
            localLayoutParams.flags = (WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS | localLayoutParams.flags);
            if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
                drawerLayout.setFitsSystemWindows(true);
                drawerLayout.setClipToPadding(false);
            }
        }

        ButterKnife.bind(this);

        setSupportActionBar(toolbar);
        getSupportActionBar().setHomeButtonEnabled(true);

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawerLayout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();

        navigationView.setItemIconTintList(null);
        navigationView.setNavigationItemSelectedListener(this);

        CategoryPagerAdapter categoryPagerAdapter = new CategoryPagerAdapter(getSupportFragmentManager(), getFragmentList(), getTitleList());
        viewPager.setAdapter(categoryPagerAdapter);
        viewPager.setOffscreenPageLimit(5);
        tabLayout.setupWithViewPager(viewPager);

        if (!Network.isConnected(this))
            SnackbarUtils.Long(getWindow().getDecorView(), "当前没有网络连接！！").danger().show();
        else if (!Network.isWifiConnected(this))
            SnackbarUtils.Custom(getWindow().getDecorView(), "当前不在WiFi连接下，请注意流量使用！！", 5000).warning().show();

        showTips();

        FavoriteUtil.init(this);
        // startActivity(new Intent(this, TestActivity.class));
    }

    private void showTips() {
        if ((boolean) SPUtils.get(this, AppConfig.show_tips, true)) {
            AlertDialog dialog = new AlertDialog.Builder(this)
                    .setTitle("提示")
                    .setMessage(getResources().getString(R.string.tips))
                    .setPositiveButton("知道了", null)
                    .setNegativeButton("不在提示", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            SPUtils.put(MainActivity.this, AppConfig.show_tips, false);
                        }
                    })
                    .create();
            // dialog.getWindow().setWindowAnimations(R.style.dialogStyle);
            dialog.show();
        }
    }

    private List<Fragment> getFragmentList() {
        List<Fragment> fragmentList = new ArrayList<>();
        fragmentList.add(new CategoryFragment().init(new ArrayList<BasePattern>() {{
            add(new Apic());
            add(new Acg12());
            add(new AoJiao());
            add(new ZeroChan());
            add(new Yande());
        }}));
        fragmentList.add(new CategoryFragment().init(new ArrayList<BasePattern>() {{
            add(new MM131());
            add(new XiuMM());
            add(new RosiMM());
            add(new Yesky());
            add(new DuowanCos());
        }}));
        return fragmentList;
    }

    private List<String> getTitleList() {
        List<String> titleList = new ArrayList<>();
        titleList.add("二次元区");
        titleList.add("三次元区");
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
                Log.e("MainActivity", "onNavigationItemSelected: " + FavoriteUtil.favorites.size());
                startActivity(new Intent(this, FavoriteActivity.class));
                break;
            case R.id.nav_setting:
                startActivity(new Intent(this, SettingActivity.class));
                break;
            case R.id.nav_theme:
                new ThemeDialog(this, this).show();
                break;
            /*case R.id.nav_update:
                break;*/
            case R.id.nav_donate:
                if (!AliPayUtil.goAliPay(this))
                    SnackbarUtils.Short(getWindow().getDecorView(), "设备上没有安装支付宝").danger().show();
                break;
            /*case R.id.nav_share:
                Intent share = new Intent(Intent.ACTION_SEND);
                share.putExtra(Intent.EXTRA_TEXT, "有人给你分享了一张图片");
                share.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                share.setType("text/plain");
                startActivity(Intent.createChooser(share, "分享到"));
                break;*/
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
