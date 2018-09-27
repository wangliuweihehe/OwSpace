package com.wlw.admin.owspace.view.activity;

import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.github.ksoichiro.android.observablescrollview.ObservableScrollView;
import com.wlw.admin.owspace.R;
import com.wlw.admin.owspace.app.OwspaceApplication;
import com.wlw.admin.owspace.di.components.DaggerDetailComponent;
import com.wlw.admin.owspace.di.modules.DetailMoudle;
import com.wlw.admin.owspace.glide.GlideApp;
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
import fm.jiecao.jcvideoplayer_lib.JCVideoPlayer;
import fm.jiecao.jcvideoplayer_lib.JCVideoPlayerStandard;

/**
 * @author admin
 */
public class VideoDetailActivity extends BaseActivity implements DetailContract.View {
    @Inject
    DetailPresenter presenter;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.video)
    JCVideoPlayerStandard video;
    @BindView(R.id.news_top_img_under_line)
    View newsTopImgUnderLine;
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
    @BindView(R.id.news_top_lead_line)
    View newsTopLeadLine;
    @BindView(R.id.news_top)
    LinearLayout newsTop;
    @BindView(R.id.news_parse_web)
    LinearLayout newsParseWeb;
    @BindView(R.id.webView)
    WebView webView;
    @BindView(R.id.scrollView)
    ObservableScrollView scrollView;
    private Unbinder bind;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video_detail);
        bind = ButterKnife.bind(this);
        initView();
        initPresenter();
    }

    private void initView() {
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();

        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setTitle("");
        }
        toolbar.setNavigationOnClickListener(v -> onBackPressed());
    }

    private void initPresenter() {
        DaggerDetailComponent.builder()
                .netComponent(OwspaceApplication.get(this).getNetComponent())
                .detailMoudle(new DetailMoudle(this))
                .build()
                .inject(this);
    }

    @Override
    protected void onStart() {
        super.onStart();
        Bundle bundle = getIntent().getExtras();
        Item item = null;
        if (bundle != null) {
            item = bundle.getParcelable(Constant.KEY_ITEM);
        }
        if (item != null) {
            video.setUp(item.getVideo(), JCVideoPlayer.SCREEN_LAYOUT_LIST, "");
            GlideApp.with(this).load(item.getThumbnail()).centerCrop().into(video.thumbImageView);
            newsTopType.setText("视频");
            newsTopLeadLine.setVisibility(View.VISIBLE);
            newsTopDate.setText(item.getUpdate_time());
            newsTopAuthor.setText(item.getTitle());
            newsTopLead.setText(item.getLead());
            presenter.getDetail(item.getId());
        }
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
            newsTopLeadLine.setVisibility(View.VISIBLE);
            newsTopImgUnderLine.setVisibility(View.VISIBLE);
            AnalysisHTML analysisHTML = new AnalysisHTML();
            analysisHTML.loadHtml(this, detailEntity.getContent(), AnalysisHTML.HTML_STRING, newsParseWeb);
        } else {
            initWebViewSetting();
            newsParseWeb.setVisibility(View.GONE);
            video.setVisibility(View.GONE);
            webView.setVisibility(View.VISIBLE);
            newsTop.setVisibility(View.GONE);
            webView.loadUrl(addParam2WezeitUrl(detailEntity.getHtml5(), false));
        }
    }

    private void initWebViewSetting() {
        WebSettings webSettings = webView.getSettings();
        webSettings.setJavaScriptEnabled(true);
        webSettings.setRenderPriority(WebSettings.RenderPriority.HIGH);
        webSettings.setSupportZoom(true);
        webView.setScrollBarStyle(View.SCROLLBARS_OUTSIDE_OVERLAY);
        webSettings.setUseWideViewPort(true);
        webSettings.setLoadWithOverviewMode(true);
    }

    @Override
    public void showOnFailure() {

    }

    private String addParam2WezeitUrl(String url, boolean paramBoolean) {
        StringBuilder builder = new StringBuilder();
        builder.append(url)
                .append("?client=android")
                .append("&device_id=")
                .append(AppUtils.getDeviceId(this))
                .append("&version=" + "1.3.0")
                .append("&show_video=");
        if (paramBoolean) {
            builder.append("1");
        } else {
            builder.append(0);
        }
        return builder.toString();
    }

    @Override
    public void onBackPressed() {
        if (JCVideoPlayer.backPress()) {
            return;
        }
        super.onBackPressed();
    }

    @Override
    protected void onPause() {
        JCVideoPlayer.releaseAllVideos();
        super.onPause();
    }
}
