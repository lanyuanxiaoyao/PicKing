package com.lanyuan.picking;

import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.WindowManager;

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
import com.lanyuan.picking.ui.setting.SettingActivity;
import com.lanyuan.picking.ui.category.CategoryFragment;
import com.lanyuan.picking.ui.category.CategoryPagerAdapter;

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

        AppConfig.init(this);

        setSupportActionBar(toolbar);
        getSupportActionBar().setHomeButtonEnabled(true);

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawerLayout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();

        navigationView.setNavigationItemSelectedListener(this);

        CategoryPagerAdapter categoryPagerAdapter = new CategoryPagerAdapter(getSupportFragmentManager(), getFragmentList(), getTitleList());
        viewPager.setAdapter(categoryPagerAdapter);
        viewPager.setOffscreenPageLimit(1);
        tabLayout.setupWithViewPager(viewPager);
    }

    private List<Fragment> getFragmentList() {
        List<Fragment> fragmentList = new ArrayList<>();
        fragmentList.add(new CategoryFragment().init(new ArrayList<BasePattern>() {{
            add(new Apic());
            add(new Acg12());
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
        titleList.add("风俗区");
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
            case R.id.nav_setting:
                startActivity(new Intent(MainActivity.this, SettingActivity.class));
                break;
            /*case R.id.nav_update:
                break;*/
            case R.id.nav_donate:
                Intent intent = new Intent();
                intent.setAction("android.intent.action.VIEW");
                Uri qrcode_url = Uri.parse("alipayqr://platformapi/startapp?saId=10000007&clientVersion=3.7.0.0718&qrcode=https%3A%2F%2Fqr.alipay.com%2FFKX06852EOBWRJPACOMCF5%3F_s%3Dweb-other");
                intent.setData(qrcode_url);
                intent.setClassName("com.eg.android.AlipayGphone", "com.alipay.mobile.quinox.LauncherActivity");
                startActivity(intent);
                break;
            /*case R.id.nav_share:
                Intent share = new Intent(Intent.ACTION_SEND);
                share.putExtra(Intent.EXTRA_TEXT, "有人给你分享了一张图片");
                share.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                share.setType("text/plain");
                startActivity(Intent.createChooser(share, "分享到"));
                break;*/
            case R.id.nav_about:
                startActivity(new Intent(MainActivity.this, AboutActivity.class));
                break;
        }
        drawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }
}
