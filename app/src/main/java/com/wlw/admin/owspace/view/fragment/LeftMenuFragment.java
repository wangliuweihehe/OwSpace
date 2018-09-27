package com.wlw.admin.owspace.view.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.AppCompatImageView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.BounceInterpolator;
import android.view.animation.OvershootInterpolator;
import android.view.animation.ScaleAnimation;
import android.view.animation.TranslateAnimation;
import android.widget.TextView;

import com.wlw.admin.owspace.R;
import com.wlw.admin.owspace.model.entity.Event;
import com.wlw.admin.owspace.utils.RxBus;
import com.wlw.admin.owspace.view.activity.ArtActivity;
import com.wlw.admin.owspace.view.activity.DailyActivity;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

public class LeftMenuFragment extends Fragment {
    private Unbinder bind;

    @BindView(R.id.right_slide_close)
    AppCompatImageView rightSlideClose;
    @BindView(R.id.search)
    AppCompatImageView search;
    @BindView(R.id.home_page_tv)
    TextView homePageTv;
    @BindView(R.id.words_tv)
    TextView wordsTv;
    @BindView(R.id.voice_tv)
    TextView voiceTv;
    @BindView(R.id.video_tv)
    TextView videoTv;
    @BindView(R.id.calendar_tv)
    TextView calendarTv;

    private List<View> mViewList;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_left_menu, container, false);
        bind = ButterKnife.bind(this, root);
        return root;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mViewList = new ArrayList<>();
        LoadView();
        setViewOnClick();
    }

    private void setViewOnClick() {
        rightSlideClose.setOnClickListener(v -> RxBus.getInstance().postEvent(new Event(1000, "closeMenu")));
        homePageTv.setOnClickListener(v -> RxBus.getInstance().postEvent(new Event(1000, "closeMenu")));

        wordsTv.setOnClickListener(v -> startArtActivity(1, "文字"));
        voiceTv.setOnClickListener(v -> startArtActivity(3, "声音"));
        videoTv.setOnClickListener(v -> startArtActivity(2, "影像"));
        calendarTv.setOnClickListener(v -> startActivity(new Intent(getContext(), DailyActivity.class)));
    }

    private void startArtActivity(int mode, String title) {
        Intent intent = new Intent(getContext(), ArtActivity.class);
        intent.putExtra("mode", mode);
        intent.putExtra("title", title);
        startActivity(intent);
    }

    private void LoadView() {
        mViewList.add(homePageTv);
        mViewList.add(wordsTv);
        mViewList.add(voiceTv);
        mViewList.add(videoTv);
        mViewList.add(calendarTv);
    }

    public void startAnim() {
        startIconAnim(search);
        startIconAnim(rightSlideClose);
        startColumnAnim();
    }

    private void startColumnAnim() {

        for (int i = 1; i <= mViewList.size(); i++) {
            View view = mViewList.get(i - 1);
            TranslateAnimation translateAnimation = new TranslateAnimation(i * -35, 0, 0, 0);
            translateAnimation.setDuration(700);
            translateAnimation.setInterpolator(new OvershootInterpolator());
            view.startAnimation(translateAnimation);
        }
    }

    private void startIconAnim(View view) {
        float f1 = view.getWidth() / 2;
        float f2 = view.getHeight() / 2;
        ScaleAnimation scaleAnimation = new ScaleAnimation(0.2f, 1.0f, 0.2f, 1.0f, f1, f2);
        scaleAnimation.setDuration(1000);
        scaleAnimation.setInterpolator(new BounceInterpolator());
        view.startAnimation(scaleAnimation);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        bind.unbind();
    }
}
