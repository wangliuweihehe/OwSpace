package com.wlw.admin.owspace.view.activity;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
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
import com.wlw.admin.owspace.utils.Constant;
import com.wlw.admin.owspace.view.adapter.ArtRecycleViewAdapter;
import com.wlw.admin.owspace.view.widget.CustomPtrHeader;


import java.util.List;
import java.util.Objects;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import in.srain.cube.views.ptr.PtrClassicFrameLayout;
import in.srain.cube.views.ptr.PtrDefaultHandler;
import in.srain.cube.views.ptr.PtrFrameLayout;

/**
 * @author admin
 */
public class ArtActivity extends BaseActivity implements ArtContract.View {
    @BindView(R.id.title)
    TextView title;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;
    @BindView(R.id.ptrFrameLayout)
    PtrClassicFrameLayout mPtrFrame;
    @Inject
    ArtPresenter presenter;
    private int mode = 1;
    private int page = 1;
    private String deviceId;
    private ArtRecycleViewAdapter recycleViewAdapter;
    boolean isRefresh = false, hasMore = true;
    private int lastVisibleItem;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_art);
        ButterKnife.bind(this);
        mode = getIntent().getIntExtra(Constant.KEY_MODE, 1);
        initPresenter();
        initView();
    }

    private void initView() {
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setTitle("");
            title.setText(getIntent().getStringExtra(Constant.KEY_TITLE));
        }
        deviceId = AppUtils.getDeviceId(this);
        toolbar.setNavigationOnClickListener(v -> onBackPressed());

        recycleViewAdapter = new ArtRecycleViewAdapter(this);
        recyclerView.setAdapter(recycleViewAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));

        mPtrFrame.setPtrHandler(new PtrDefaultHandler() {
            @Override
            public void onRefreshBegin(PtrFrameLayout frame) {
                page = 1;
                isRefresh = true;
                hasMore = true;
                loadData(page, mode, "0", deviceId, "0");
            }
        });
        mPtrFrame.setOffsetToRefresh(200);
        mPtrFrame.autoRefresh(true);
        CustomPtrHeader header = new CustomPtrHeader(this, mode);
        mPtrFrame.setHeaderView(header);
        mPtrFrame.addPtrUIHandler(header);
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {

                if (newState == RecyclerView.SCROLL_STATE_IDLE && !isRefresh && hasMore
                        && (lastVisibleItem + 1 == recycleViewAdapter.getItemCount())) {
                    loadData(page, mode, recycleViewAdapter.getLastItemId(), deviceId, recycleViewAdapter.getLastItemCreateTime());
                }
            }

            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                lastVisibleItem = ((LinearLayoutManager) Objects.requireNonNull(recyclerView.getLayoutManager())).findLastVisibleItemPosition();
            }
        });
    }

    private void loadData(int page, int mode, String pageId, String deviceId, String createTime) {
        presenter.getListByPage(page, mode, pageId, deviceId, createTime);
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

    }

    @Override
    public void showNoMore() {
        hasMore = false;
        if (!isRefresh) {
            recycleViewAdapter.setHasMore(false);
            recycleViewAdapter.notifyItemChanged(recycleViewAdapter.getItemCount() - 1);
        }
    }

    @Override
    public void updateListUI(List<Item> itemList) {
        mPtrFrame.refreshComplete();
        isRefresh = false;
        page++;
        if (isRefresh) {
            recycleViewAdapter.setHasMore(true);
            recycleViewAdapter.setError(false);
            recycleViewAdapter.replaceAllData(itemList);
        } else {
            recycleViewAdapter.setArtList(itemList);
        }
    }

    @Override
    public void showOnFailure() {
        isRefresh = false;
        if (!isRefresh) {
            recycleViewAdapter.setError(true);
            recycleViewAdapter.notifyItemChanged(recycleViewAdapter.getItemCount() - 1);
        } else {
            Toast.makeText(this, "刷新失败", Toast.LENGTH_SHORT).show();
        }
    }
}
