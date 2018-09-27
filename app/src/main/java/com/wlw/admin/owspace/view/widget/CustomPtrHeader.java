package com.wlw.admin.owspace.view.widget;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;
;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.wlw.admin.owspace.R;
import com.wlw.admin.owspace.utils.Log;

import java.text.SimpleDateFormat;
import java.util.Date;

import in.srain.cube.views.ptr.PtrFrameLayout;
import in.srain.cube.views.ptr.PtrUIHandler;
import in.srain.cube.views.ptr.indicator.PtrIndicator;

public class CustomPtrHeader extends FrameLayout implements PtrUIHandler {
    private final static String TAG = CustomPtrHeader.class.getSimpleName();
    private final static String KEY_SHAREDPREFERENCES = "CustomPtrHeader_last_update";
    private long mLastUpdateTime = -1;
    private String mLastUpdateTimeKey;
    private ImageView refreshImage;
    private TextView mLastUpdateTextView;
    private boolean mShouldShowLastUpdate;
    private SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    private LastUpdateTimeUpdater mLastUpdateTimeUpdater = new LastUpdateTimeUpdater();

    public CustomPtrHeader(@NonNull Context context, int mode) {
        super(context);
        mLastUpdateTimeKey = "CustomPtrHeader_last_update_mode" + mode;
        init();
    }

    private void init() {
        View view = LayoutInflater.from(getContext()).inflate(R.layout.refresh_header, this);
        refreshImage = view.findViewById(R.id.refresh_loading);
        mLastUpdateTextView = view.findViewById(R.id.latest_fresh_time);
        Glide.with(getContext()).load(R.mipmap.refresh_loading).into(refreshImage);
        mShouldShowLastUpdate = true;
        tryUpdateLastUpdateTime();
    }

    private void tryUpdateLastUpdateTime() {
        if (TextUtils.isEmpty(mLastUpdateTimeKey) || !mShouldShowLastUpdate) {
            mLastUpdateTextView.setVisibility(GONE);
        } else {
            String time = getLastUpdateTime();
            if (TextUtils.isEmpty(time)) {
                mLastUpdateTextView.setVisibility(GONE);
            } else {
                mLastUpdateTextView.setVisibility(VISIBLE);
                mLastUpdateTextView.setText(time);
            }
        }
    }

    private String getLastUpdateTime() {
        if (mLastUpdateTime == -1 && !TextUtils.isEmpty(mLastUpdateTimeKey)) {
            mLastUpdateTime = getContext().getSharedPreferences(KEY_SHAREDPREFERENCES, 0).getLong(mLastUpdateTimeKey, -1);
        }
        if (mLastUpdateTime == -1) {
            return null;
        }
        long diffTime = System.currentTimeMillis() - mLastUpdateTime;
        int seconds = (int) (diffTime / 1000);
        if (diffTime < 0) {
            return null;
        }
        if (seconds <= 0) {
            return null;
        }
        StringBuilder sb = new StringBuilder();
        if (seconds < 60) {
            sb.append(seconds).append("秒前");
        } else {
            int minutes = (seconds / 60);
            if (minutes > 60) {
                int hours = minutes / 60;
                if (hours > 24) {
                    Date date = new Date(mLastUpdateTime);
                    sb.append(dateFormat.format(date));
                } else {
                    sb.append(hours).append("小时前");
                }
            } else {
                sb.append(minutes).append("分钟前");
            }
        }
        return sb.toString();
    }

    public void setLastUpdateTimeKey(String key) {
        if (TextUtils.isEmpty(key)) {
            return;
        }
        mLastUpdateTimeKey = key;
    }

    @Override
    public void onUIReset(PtrFrameLayout frame) {
        Log.e(TAG, "onUIReset");
        refreshImage.setVisibility(GONE);
    }

    @Override
    public void onUIRefreshPrepare(PtrFrameLayout frame) {
        Log.e(TAG, "onUIRefreshPrepare");
        mShouldShowLastUpdate = true;
        tryUpdateLastUpdateTime();
        mLastUpdateTimeUpdater.start();
        refreshImage.setVisibility(VISIBLE);
    }

    @Override
    public void onUIRefreshBegin(PtrFrameLayout frame) {
        Log.e(TAG, "onUIRefreshBegin");
        tryUpdateLastUpdateTime();
        mLastUpdateTimeUpdater.stop();
    }

    @Override
    public void onUIRefreshComplete(PtrFrameLayout frame) {
        Log.e(TAG, "onUIRefreshComplete");
        SharedPreferences sharedPreferences = getContext().getSharedPreferences(KEY_SHAREDPREFERENCES, 0);
        if (!TextUtils.isEmpty(mLastUpdateTimeKey)) {
            sharedPreferences.edit().putLong(mLastUpdateTimeKey, System.currentTimeMillis()).apply();
        }
    }


    @Override
    public void onUIPositionChange(PtrFrameLayout frame, boolean isUnderTouch, byte status, PtrIndicator ptrIndicator) {
        Log.e(TAG, "onUIPositionChange");

    }

    private class LastUpdateTimeUpdater implements Runnable {
        private boolean mRunning = false;

        private void start() {
            if (TextUtils.isEmpty(mLastUpdateTimeKey)) {
                return;
            }
            mRunning = true;
            run();
        }

        private void stop() {
            mRunning = false;
            removeCallbacks(this);
        }

        @Override
        public void run() {
            tryUpdateLastUpdateTime();
            if (mRunning) {
                postDelayed(this, 1500);
            }
        }
    }
}
