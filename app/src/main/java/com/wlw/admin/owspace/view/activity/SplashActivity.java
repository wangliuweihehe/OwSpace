package com.wlw.admin.owspace.view.activity;

import android.Manifest;
import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.content.Intent;
import android.content.res.AssetManager;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.view.View;

import com.wlw.admin.owspace.R;
import com.wlw.admin.owspace.app.OwspaceApplication;
import com.wlw.admin.owspace.di.components.DaggerSplashComponent;
import com.wlw.admin.owspace.di.modules.SplashModule;
import com.wlw.admin.owspace.presenter.SplashContract;
import com.wlw.admin.owspace.presenter.SplashPresenter;
import com.wlw.admin.owspace.utils.AppUtils;
import com.wlw.admin.owspace.utils.FileUtil;
import com.wlw.admin.owspace.utils.Log;
import com.wlw.admin.owspace.utils.PreferenceUtils;
import com.wlw.admin.owspace.view.widget.FixedImageView;


import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Random;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import pub.devrel.easypermissions.AfterPermissionGranted;
import pub.devrel.easypermissions.EasyPermissions;

/**
 * @author admin
 */
public class SplashActivity extends BaseActivity implements SplashContract.View, EasyPermissions.PermissionCallbacks {
    @BindView(R.id.splash_img)
    FixedImageView splashImg;
    @Inject
    SplashPresenter presenter;
    public String tag;
    private static final int PERMISSION_REQUEST_CODE = 1;

    private String[] neeDPermissions = {
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.READ_PHONE_STATE
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        tag = getClass().getSimpleName();
        DaggerSplashComponent
                .builder()
                .netComponent(OwspaceApplication.get(this).getNetComponent())
                .splashModule(new SplashModule(this))
                .build().inject(this);
        initStatus();
    }

    private void initStatus() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            View decorView = getWindow().getDecorView();
            int option = View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LAYOUT_STABLE;
            decorView.setSystemUiVisibility(option);
            getWindow().setStatusBarColor(Color.TRANSPARENT);
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        requestCodePermissions();
    }

    @AfterPermissionGranted(PERMISSION_REQUEST_CODE)
    private void requestCodePermissions() {
        if (!EasyPermissions.hasPermissions(this, neeDPermissions)) {
            EasyPermissions.requestPermissions(this, "应用需要这些权限", PERMISSION_REQUEST_CODE, neeDPermissions);
        } else {
            setContentView(R.layout.activity_splash);
            ButterKnife.bind(SplashActivity.this);
            delaySplash();
            String deviceId = AppUtils.getDeviceId(this);
            Log.e(tag, "deviceId" + deviceId);
            presenter.getSplash(deviceId);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, this);
    }

    @Override
    public void onPermissionsGranted(int requestCode, @NonNull List<String> perms) {
        recreate();
    }

    @Override
    public void onPermissionsDenied(int requestCode, @NonNull List<String> perms) {
        showMissingPermissionDialog();
    }

    private void showMissingPermissionDialog() {
        new AlertDialog.Builder(this)
                .setTitle("提示")
                .setMessage("当前应用缺少必要权限。请点击\"设置\"-\"权限\"-打开所需权限。")
                .setNegativeButton("取消", (dialog, which) -> finish())
                .setPositiveButton("设置", (dialog, which) -> startAppSetting())
                .setCancelable(false)
                .show();

    }

    private void startAppSetting() {
        Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
        intent.setData(Uri.parse("package:" + getPackageName()));
        startActivity(intent);
    }

    private void delaySplash() {
        List<String> picList = FileUtil.getAllAD();
        if (picList.size() > 0) {
            Random random = new Random();
            int index = random.nextInt(picList.size());
            String splashImgIndex = "splash_img_index";
            int imgIndex = PreferenceUtils.getPrefInt(this, splashImgIndex, 0);
            Log.e(tag, "当前的imageIndex=" + imgIndex);
            if (index == imgIndex) {
                if (index >= picList.size()) {
                    index--;
                } else if (imgIndex == 0) {
                    if (index + 1 < picList.size()) {
                        index++;
                    }
                }
            }
            PreferenceUtils.setPrefInt(this, splashImgIndex, index);
            Log.e(tag, "当前的imageIndex=" + imgIndex);
            File file = new File(picList.get(index));
            try {
                InputStream is = new FileInputStream(file);
                splashImg.setImageDrawable(inputStream2Drawable(is));
                animWelcomeImage();
                is.close();

            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            AssetManager assetManager = getAssets();
            try {
                InputStream is = assetManager.open("welcome_default.jpg");
                splashImg.setImageDrawable(inputStream2Drawable(is));
                animWelcomeImage();
                is.close();
            } catch (IOException e) {
                e.printStackTrace();
            }

        }
    }

    private void animWelcomeImage() {
        ObjectAnimator animator = ObjectAnimator.ofFloat(splashImg, "translationX", -100f);
        animator.setDuration(1500L).start();
        animator.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {

            }

            @Override
            public void onAnimationEnd(Animator animation) {
                startActivity(new Intent(SplashActivity.this, MainActivity.class));
                finish();
            }

            @Override
            public void onAnimationCancel(Animator animation) {

            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }
        });
    }

    private Drawable inputStream2Drawable(InputStream is) {
        return BitmapDrawable.createFromStream(is, "splashImg");
    }


}
