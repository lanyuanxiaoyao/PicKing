package com.lanyuan.picking.common;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.View;

import com.aspsine.swipetoloadlayout.OnLoadMoreListener;
import com.aspsine.swipetoloadlayout.OnRefreshListener;
import com.aspsine.swipetoloadlayout.SwipeToLoadLayout;
import com.lanyuan.picking.R;
import com.lanyuan.picking.util.ScreenUtil;
import com.lanyuan.picking.util.ToastUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;

public abstract class BaseContentsActivity extends BaseActivity {

    @BindView(R.id.swipe_target)
    RecyclerView recyclerView;
    @BindView(R.id.menu_recycle_view)
    RecyclerView menuRecycleView;
    @BindView(R.id.swipe_layout)
    SwipeToLoadLayout refreshLayout;

    private String baseUrl;
    private String currentUrl;

    private boolean isRunnable = true;
    private boolean hasMore = true;

    private BaseContentsAdapter adapter;

    private List<Menu> menuList;

    public enum parameter {
        RESULT, CURRENT_URL
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contents);

        ButterKnife.bind(this);

        menuList = getMenuList() == null ? new ArrayList<Menu>() : getMenuList();
        MenuAdapter menuAdapter = new MenuAdapter(this, menuList);
        menuAdapter.setOnClickListener(new MenuAdapter.OnItemClickListener() {
            @Override
            public void ItemClickListener(View view, int position) {
                ToastUtil.toast(menuList.get(position).getUrl());
                adapter.removeAll();
                baseUrl = getBaseUrl(menuList, position);
                currentUrl = menuList.get(position).getUrl();
                new GetContent().execute(currentUrl);
            }

            @Override
            public void ItemLongClickListener(View view, int position) {

            }
        });
        menuRecycleView.setLayoutManager(new LinearLayoutManager(this));
        menuRecycleView.setAdapter(menuAdapter);

        baseUrl = getBaseUrl(menuList, 0);
        currentUrl = menuList.get(0).getUrl();

        adapter = new BaseContentsAdapter(this, new ArrayList<BaseInfo>(), ScreenUtil.getScreenWidth(this) / 2);
        adapter.setOnClickListener(new BaseContentsAdapter.OnItemClickListener() {
            @Override
            public void ItemClickListener(View view, int position, BaseInfo baseInfo) {
                Intent intent = new Intent(BaseContentsActivity.this, getTargetDetailActivity());
                intent.putExtra("currentUrl", baseInfo.getAlbumUrl());
                intent.putExtra("baseUrl", baseUrl);
                startActivity(intent);
            }

            @Override
            public void ItemLongClickListener(View view, int position) {

            }
        });

        StaggeredGridLayoutManager staggeredGridLayoutManager = new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(staggeredGridLayoutManager);
        recyclerView.setAdapter(adapter);

        refreshLayout.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh() {
                toast("refresh");
                refreshLayout.setRefreshing(false);
            }
        });
        refreshLayout.setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void onLoadMore() {
                new GetNextPage().execute(currentUrl);
            }
        });

        new GetContent().execute(currentUrl);
    }

    public abstract String getBaseUrl(List<Menu> menuList, int position);

    public abstract Class getTargetDetailActivity();

    public abstract List<Menu> getMenuList();

    public abstract Map<parameter, Object> getContent(String baseUrl, String currentUrl);

    public abstract String getNext(String baseUrl, String currentUrl);

    private class GetContent extends AsyncTask<String, Integer, Map<parameter, Object>> {

        @Override
        protected Map<parameter, Object> doInBackground(String... strings) {
            if (strings.length > 0)
                return getContent(baseUrl, strings[0]);
            else
                return null;
        }

        @Override
        protected void onPostExecute(Map<parameter, Object> resultMap) {
            if (!isRunnable) return;

            currentUrl = (String) resultMap.get(parameter.CURRENT_URL);

            List<BaseInfo> urls = (List<BaseInfo>) resultMap.get(parameter.RESULT);
            adapter.addMore(urls);
        }
    }

    private class GetNextPage extends AsyncTask<String, Integer, String> {

        @Override
        protected String doInBackground(String... strings) {
            if (strings.length > 0)
                return getNext(baseUrl, strings[0]);
            else
                return "";
        }

        @Override
        protected void onPostExecute(String url) {
            if (!isRunnable) return;
            if (!"".equals(url)) {
                new GetContent().execute(url);
                refreshLayout.setLoadingMore(false);
            } else {
                hasMore = false;
                toast("下面已经没有更多了！");
                refreshLayout.setLoadingMore(false);
            }
        }
    }
}
