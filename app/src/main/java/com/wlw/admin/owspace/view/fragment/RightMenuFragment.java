package com.wlw.admin.owspace.view.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.BounceInterpolator;
import android.view.animation.OvershootInterpolator;
import android.view.animation.ScaleAnimation;
import android.view.animation.TranslateAnimation;
import android.widget.ImageView;
import android.widget.TextView;

import com.wlw.admin.owspace.R;
import com.wlw.admin.owspace.model.entity.Event;
import com.wlw.admin.owspace.utils.RxBus;
import com.wlw.admin.owspace.view.activity.SettingActivity;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

public class RightMenuFragment extends Fragment {
    private Unbinder bind;

    @BindView(R.id.right_slide_close)
    ImageView rightSlideClose;
    @BindView(R.id.setting)
    ImageView setting;
    @BindView(R.id.avatar_iv)
    ImageView avatarIv;
    @BindView(R.id.name_tv)
    TextView nameTv;
    @BindView(R.id.notification_tv)
    TextView notificationTv;
    @BindView(R.id.favorites_tv)
    TextView favoritesTv;
    @BindView(R.id.download_tv)
    TextView downLoadTv;
    @BindView(R.id.note_tv)
    TextView noteTv;
    private List<View> mViewList;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_right_nemu, container, false);
        bind = ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        loadView();
    }

    private void loadView() {
        mViewList = new ArrayList<>();
        mViewList.add(notificationTv);
        mViewList.add(favoritesTv);
        mViewList.add(downLoadTv);
        mViewList.add(noteTv);
        rightSlideClose.setOnClickListener(v -> RxBus.getInstance().postEvent(new Event(1000, "closeMenu")));
        setting.setOnClickListener(v -> startActivity(new Intent(getContext(), SettingActivity.class)));
    }

    public void startAnim() {
        startIconAnim(rightSlideClose);
        startIconAnim(setting);
        startColumnAnim();
    }

    private void startColumnAnim() {
        for (int i = 1; i <= mViewList.size(); i++) {
            View view = mViewList.get(i - 1);
            TranslateAnimation translateAnimation = new TranslateAnimation(i * 35, 0, 0, 0);
            translateAnimation.setDuration(700);
            translateAnimation.setInterpolator(new OvershootInterpolator());
            view.startAnimation(translateAnimation);
        }

    }

    private void startIconAnim(View view) {
        ScaleAnimation scaleAnimation = new ScaleAnimation(1.0f, 0.5f, 1.0f, 0.5f, view.getWidth() * 0.5f, view.getHeight() * 0.5f);
        scaleAnimation.setDuration(1000);
        scaleAnimation.setInterpolator(new BounceInterpolator());
        view.startAnimation(scaleAnimation);
    }

    @Override
    public void onDestroy() {
        bind.unbind();
        super.onDestroy();
    }
}
