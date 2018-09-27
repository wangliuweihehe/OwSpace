package com.wlw.admin.owspace.view.activity;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.github.ksoichiro.android.observablescrollview.ObservableScrollView;
import com.github.ksoichiro.android.observablescrollview.ObservableScrollViewCallbacks;
import com.github.ksoichiro.android.observablescrollview.ScrollState;
import com.github.ksoichiro.android.observablescrollview.ScrollUtils;
import com.wlw.admin.owspace.R;
import com.wlw.admin.owspace.app.OwspaceApplication;
import com.wlw.admin.owspace.di.components.DaggerDetailComponent;
import com.wlw.admin.owspace.di.modules.DetailMoudle;
import com.wlw.admin.owspace.model.entity.DetailEntity;
import com.wlw.admin.owspace.model.entity.Item;
import com.wlw.admin.owspace.presenter.DetailContract;
import com.wlw.admin.owspace.presenter.DetailPresenter;
import com.wlw.admin.owspace.utils.AnalysisHTML;
import com.wlw.admin.owspace.utils.AppUtils;
import com.wlw.admin.owspace.utils.Constant;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

public class DetailActivity extends BaseActivity implements ObservableScrollViewCallbacks, DetailContract.View {
    @BindView(R.id.favorite)
    ImageView favorite;
    @BindView(R.id.write)
    ImageView write;
    @BindView(R.id.share)
    ImageView share;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.webView)
    WebView webView;
    @BindView(R.id.scrollView)
    ObservableScrollView scrollView;
    @BindView(R.id.image)
    ImageView image;
    @BindView(R.id.news_parse_web)
    LinearLayout newsParseWeb;
    @BindView(R.id.news_top_type)
    TextView newsTopType;
    @BindView(R.id.news_top_date)
    TextView newsTopDate;
    @BindView(R.id.news_top_title)
    TextView newsTopTitle;
    @BindView(R.id.news_top_author)
    TextView newsTopAuthor;
    @BindView(R.id.news_top_lead)
    TextView newsTopLead;
    @BindView(R.id.news_top)
    LinearLayout newsTop;
    @BindView(R.id.news_top_img_under_line)
    View newsTopImgUnderLine;
    @BindView(R.id.news_top_lead_line)
    View newsTopLeadLine;

    @Inject
    DetailPresenter presenter;

    private int mParallaxImageHeight;
    private Unbinder bind;
    private Item item;
    private int color;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        bind = ButterKnife.bind(this);
        initView();
        initPresenter();
    }

    @Override
    protected void onStart() {
        super.onStart();
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            item = bundle.getParcelable(Constant.KEY_ITEM);
        }
        if (item != null) {
            Glide.with(this).load(item.getThumbnail()).apply(new RequestOptions().centerCrop()).into(image);
            newsTopLeadLine.setVisibility(View.VISIBLE);
            newsTopImgUnderLine.setVisibility(View.VISIBLE);
            newsTopType.setText("文字");
            newsTopDate.setText(item.getUpdate_time());
            newsTopTitle.setText(item.getTitle());
            newsTopAuthor.setText(item.getAuthor());
            newsTopLead.setText(item.getLead());
            newsTopLead.setLineSpacing(1.5f, 1.8f);
            presenter.getDetail(item.getId());
        }
    }

    private void initView() {
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setTitle("");
        }
        toolbar.setNavigationOnClickListener(v -> onBackPressed());
        toolbar.setBackgroundColor(ScrollUtils.getColorWithAlpha(0, color));

        scrollView.setScrollViewCallbacks(this);
        mParallaxImageHeight = getResources().getDimensionPixelSize(R.dimen.parallax_image_height);
    }

    private void initPresenter() {
        DaggerDetailComponent.builder()
                .netComponent(OwspaceApplication.get(this).getNetComponent())
                .detailMoudle(new DetailMoudle(this))
                .build()
                .inject(this);
    }
     @Override
    protected void onDestroy() {
        super.onDestroy();
        bind.unbind();
    }


    @Override
    public void onScrollChanged(int i, boolean b, boolean b1) {
        color = getResources().getColor(R.color.primary);
        float alpha = Math.min(1, (float) i / mParallaxImageHeight);
        toolbar.setBackgroundColor(ScrollUtils.getColorWithAlpha(alpha, color));
    }

    @Override
    public void onDownMotionEvent() {

    }

    @Override
    public void onUpOrCancelMotionEvent(ScrollState scrollState) {

    }

    @Override
    public void showLoading() {

    }

    @Override
    public void dismissLoading() {

    }

    @Override
    public void updateListUI(DetailEntity detailEntity) {
        if (detailEntity.getParseXML() == 1) {
            AnalysisHTML analysisHTML = new AnalysisHTML();
            analysisHTML.loadHtml(this, detailEntity.getContent(), AnalysisHTML.HTML_STRING, newsParseWeb);
            newsTopType.setText("文字");
        } else {
            initWebSetting();
            newsParseWeb.setVisibility(View.GONE);
            image.setVisibility(View.GONE);
            webView.setVisibility(View.VISIBLE);
            newsTop.setVisibility(View.GONE);
            webView.loadUrl(addParams2WezeitUrl(detailEntity.getLink_url(), false));
        }
    }

    @SuppressLint("SetJavaScriptEnabled")
    private void initWebSetting() {
        WebSettings settings = webView.getSettings();
        settings.setJavaScriptEnabled(true);
        settings.setSupportZoom(true);
        settings.setUseWideViewPort(true);
        settings.setLoadWithOverviewMode(true);
        webView.setScrollBarStyle(View.SCROLLBARS_OUTSIDE_OVERLAY);
    }

    @Override
    public void showOnFailure() {

    }

    public String addParams2WezeitUrl(String url, boolean paramsBoolean) {
        StringBuilder builder = new StringBuilder();
        builder.append(url)
                .append("?client=android")
                .append("&device_id=")
                .append(AppUtils.getDeviceId(this))
                .append("&version=1.3.0")
                .append("&show_video=");
        if (paramsBoolean) {
            builder.append(0);
        } else {
            builder.append(1);
        }
        return builder.toString();
    }
}
