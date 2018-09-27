package com.wlw.admin.owspace.view.activity;

import android.support.annotation.NonNull;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.PagerSnapHelper;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.widget.TextView;
import android.widget.Toast;

import com.wlw.admin.owspace.R;
import com.wlw.admin.owspace.app.OwspaceApplication;
import com.wlw.admin.owspace.di.components.DaggerArtComponent;
import com.wlw.admin.owspace.di.modules.ArtModule;
import com.wlw.admin.owspace.model.entity.Item;
import com.wlw.admin.owspace.presenter.ArtContract;
import com.wlw.admin.owspace.presenter.ArtPresenter;
import com.wlw.admin.owspace.utils.AppUtils;
import com.wlw.admin.owspace.utils.Log;
import com.wlw.admin.owspace.view.adapter.DailyAdapter;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * @author admin
 */
public class DailyActivity extends AppCompatActivity implements ArtContract.View {
    @BindView(R.id.title)
    TextView title;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;

    @Inject
    ArtPresenter presenter;

    private String deviceId;
    private int page = 1;
    private static final int MODE = 4;
    private boolean isLoading = true;
    private DailyAdapter adapter;
    private int lastVisibleItemPosition;
    private List<Item> items;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_daily);
        ButterKnife.bind(this);
        initPresenter();
        initView();
        deviceId = AppUtils.getDeviceId(this);
        presenter.getListByPage(page, MODE, "0", deviceId, "0");
    }

    private void initView() {
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setTitle("");
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
        toolbar.setNavigationOnClickListener(v -> onBackPressed());
        items = new ArrayList<>();
        adapter = new DailyAdapter(items);
        recyclerView.setAdapter(adapter);
        PagerSnapHelper pagerSnapHelper = new PagerSnapHelper();
        pagerSnapHelper.attachToRecyclerView(recyclerView);
        recyclerView.setNestedScrollingEnabled(false);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                if (adapter.getItemCount() <= lastVisibleItemPosition + 2 && !isLoading && adapter.getItemCount() > 0) {
                    presenter.getListByPage(page, 0, adapter.getLastItemId(), deviceId, adapter.getLastItemCreateTime());
                }
            }

            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                lastVisibleItemPosition = ((LinearLayoutManager) Objects.requireNonNull(recyclerView.getLayoutManager())).findLastVisibleItemPosition();
                int posi1 = ((LinearLayoutManager) recyclerView.getLayoutManager()).findFirstCompletelyVisibleItemPosition();
                int posi2 = ((LinearLayoutManager) recyclerView.getLayoutManager()).findLastCompletelyVisibleItemPosition();

            }
        });
    }

    private void initPresenter() {
        DaggerArtComponent.builder()
                .netComponent(OwspaceApplication.get(this).getNetComponent())
                .artModule(new ArtModule(this))
                .build()
                .inject(this);
    }

    @Override
    public void showLoading() {

    }

    @Override
    public void dismissLoading() {

    }

    @Override
    public void showNoData() {
        Toast.makeText(this, "暂无数据", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void showNoMore() {
        Toast.makeText(this, "没有更多了", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void updateListUI(List<Item> itemList) {
        isLoading = false;
        adapter.setArtList(itemList);
        page++;
    }

    @Override
    public void showOnFailure() {
        if (adapter.getItemCount() == 0) {
            showNoData();
        } else {
            Toast.makeText(this, "加载数据失败，请检查您的网络", Toast.LENGTH_SHORT).show();
        }
    }
}
