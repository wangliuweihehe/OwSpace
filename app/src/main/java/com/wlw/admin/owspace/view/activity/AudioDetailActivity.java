package com.wlw.admin.owspace.view.activity;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Build;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.AppCompatImageView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.TextView;

import com.github.ksoichiro.android.observablescrollview.ObservableScrollView;
import com.github.ksoichiro.android.observablescrollview.ObservableScrollViewCallbacks;
import com.github.ksoichiro.android.observablescrollview.ScrollState;
import com.github.ksoichiro.android.observablescrollview.ScrollUtils;
import com.wlw.admin.owspace.R;
import com.wlw.admin.owspace.app.OwspaceApplication;
import com.wlw.admin.owspace.di.components.DaggerDetailComponent;
import com.wlw.admin.owspace.di.modules.DetailMoudle;
import com.wlw.admin.owspace.glide.GlideApp;
import com.wlw.admin.owspace.model.entity.DetailEntity;
import com.wlw.admin.owspace.model.entity.Item;
import com.wlw.admin.owspace.player.IPlayback;
import com.wlw.admin.owspace.player.PlayState;
import com.wlw.admin.owspace.player.PlaybackService;
import com.wlw.admin.owspace.presenter.DetailContract;
import com.wlw.admin.owspace.presenter.DetailPresenter;
import com.wlw.admin.owspace.utils.AnalysisHTML;
import com.wlw.admin.owspace.utils.AppUtils;
import com.wlw.admin.owspace.utils.Constant;
import com.wlw.admin.owspace.utils.Timeutils;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * @author admin
 */
public class AudioDetailActivity extends AppCompatActivity implements ObservableScrollViewCallbacks, DetailContract.View, IPlayback.Callback, Handler.Callback {
    @BindView(R.id.image)
    ImageView image;
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

    @BindView(R.id.toolbar)
    Toolbar toolbar;

    @Inject
    DetailPresenter presenter;

    @BindView(R.id.button_play_toggle)
    AppCompatImageView buttonPlayToggle;
    @BindView(R.id.text_view_progress)
    TextView textViewProgress;
    @BindView(R.id.seek_bar)
    SeekBar seekBar;
    @BindView(R.id.text_view_duration)
    TextView textViewDuration;

    private int mParallaxImageHeight;
    private PlaybackService mPlaybackService;
    private Unbinder bind;
    private ServiceConnection mConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            mPlaybackService = ((PlaybackService.LocalBinder) service).getService();
            register();
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            unRegister();
            mPlaybackService = null;
        }
    };
    private String song;
    private Handler handler;
    private ScheduledExecutorService pool;

    private void unRegister() {
        if (mPlaybackService != null) {
            mPlaybackService.unRegisterCallback(this);
        }
    }

    private void register() {
        mPlaybackService.registerCallback(this);
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_audio_detail);
        bind = ButterKnife.bind(this);
        handler = new Handler(this);
        initView();
        initPresenter();
        bindPlaybackService();
    }

    @Override
    protected void onStart() {
        super.onStart();
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            Item item = bundle.getParcelable(Constant.KEY_ITEM);
            if (item != null) {
                GlideApp.with(this).load(item.getThumbnail()).centerCrop().into(image);
                newsTopLeadLine.setVisibility(View.VISIBLE);
                newsTopImgUnderLine.setVisibility(View.VISIBLE);
                newsTopType.setText("音频");
                newsTopDate.setText(item.getUpdate_time());
                newsTopTitle.setText(item.getTitle());
                newsTopAuthor.setText(item.getAuthor());
                newsTopLead.setText(item.getLead());
                newsTopLead.setLineSpacing(1.5f, 1.5f);
                presenter.getDetail(item.getId());

            }
        }
    }

    private void initView() {
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setTitle("");
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
        toolbar.setNavigationOnClickListener(v -> onBackPressed());
        toolbar.setBackgroundColor(ScrollUtils.getColorWithAlpha(0, getResources().getColor(R.color.primary)));
        scrollView.setScrollViewCallbacks(this);
        mParallaxImageHeight = getResources().getDimensionPixelSize(R.dimen.parallax_image_height);

        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {

            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                mPlaybackService.seekTo(getSeekDuration(seekBar.getProgress()));
            }
        });
        buttonPlayToggle.setOnClickListener(v -> {
            if (mPlaybackService == null || song == null) {
                return;
            }
            if (mPlaybackService.isPlaying()) {
                if (TextUtils.equals(song, mPlaybackService.getSong())) {
                    mPlaybackService.pause();
                    buttonPlayToggle.setImageResource(R.drawable.ic_play);
                } else {
                    mPlaybackService.play(song);
                    buttonPlayToggle.setImageResource(R.drawable.ic_pause);
                }

            } else {
                if (TextUtils.equals(song, mPlaybackService.getSong())) {
                    mPlaybackService.play();
                } else {
                    mPlaybackService.play(song);
                }
                buttonPlayToggle.setImageResource(R.drawable.ic_pause);
            }
        });
    }

    private int getSeekDuration(int progress) {
        return (int) (getCurrentSongDuration() * (float) (progress / seekBar.getMax()));
    }

    private int getCurrentSongDuration() {
        int duration = 0;
        if (mPlaybackService != null) {
            duration = mPlaybackService.getDuration();
        }
        return duration;
    }

    private void initPresenter() {
        DaggerDetailComponent.builder().detailMoudle(new DetailMoudle(this))
                .netComponent(OwspaceApplication.get(this).getNetComponent())
                .build()
                .inject(this);
    }

    private void bindPlaybackService() {
        bindService(new Intent(this, PlaybackService.class), mConnection, Context.BIND_AUTO_CREATE);
    }


    @Override
    protected void onDestroy() {
        unRegister();
        handler.removeMessages(1);
        super.onDestroy();
        bind.unbind();
    }

    @Override
    public void onScrollChanged(int i, boolean b, boolean b1) {
        int color = getResources().getColor(R.color.primary);
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
        song = detailEntity.getFm();
        if (detailEntity.getParseXML() == 1) {
            AnalysisHTML analysisHTML = new AnalysisHTML();
            analysisHTML.loadHtml(this, detailEntity.getContent(), AnalysisHTML.HTML_STRING, newsParseWeb);
            newsTopType.setText("音频");
        } else {
            initWebViewSetting();
            newsParseWeb.setVisibility(View.GONE);
            image.setVisibility(View.GONE);
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
    public void showOnFailure() {

    }

    @Override
    public void onComplete(PlayState state) {

    }

    @Override
    public void onPlayStatusChanged(PlayState status) {
        switch (status) {
            case INIT:
                break;
            case PREPARE:
                break;
            case PLAYING:
                updateDuration();
                playTimer();
                buttonPlayToggle.setImageResource(R.drawable.ic_pause);
                break;
            case PAUSE:
                cancelTimer();
                buttonPlayToggle.setImageResource(R.drawable.ic_play);
                break;
            case ERROR:

                break;
            case COMPLETE:
                cancelTimer();
                buttonPlayToggle.setImageResource(R.drawable.ic_play);
                seekBar.setProgress(0);
                break;
            default:
                break;
        }
    }

    private void cancelTimer() {
        if (pool != null) {
            pool.shutdown();
        }
        pool = null;
    }

    private void playTimer() {
        pool = Executors.newScheduledThreadPool(1);
        pool.scheduleWithFixedDelay(() -> {
            if (mPlaybackService == null) {
                return;
            }
            if (mPlaybackService.isPlaying()) {
                handler.sendEmptyMessage(1);
            }
        }, 0, 1, TimeUnit.SECONDS);
    }

    private void updateDuration() {
        textViewDuration.setText(Timeutils.formatDuration(mPlaybackService.getDuration()));
    }

    @Override
    public void onPosition(int position) {

    }

    @Override
    public boolean handleMessage(Message msg) {
        if (msg.what == 1 && mPlaybackService.isPlaying()) {
            if (isFinishing()) {
                return true;
            }
            int progress = seekBar.getMax() * mPlaybackService.getProgress() / mPlaybackService.getDuration();
            updateProgressTextWithProgress(mPlaybackService.getProgress());
            if (progress >= 0 && progress <= seekBar.getMax()) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                    seekBar.setProgress(progress, true);
                } else {
                    seekBar.setProgress(progress);
                }
            }
        }
        return true;
    }

    private void updateProgressTextWithProgress(int progress) {
        textViewProgress.setText(Timeutils.formatDuration(progress));
    }
}
