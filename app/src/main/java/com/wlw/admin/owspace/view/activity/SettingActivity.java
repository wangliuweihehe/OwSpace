package com.wlw.admin.owspace.view.activity;

import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.wlw.admin.owspace.BuildConfig;
import com.wlw.admin.owspace.R;

import java.io.File;
import java.text.DecimalFormat;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * @author admin
 */
public class SettingActivity extends AppCompatActivity {
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.version_tv)
    TextView versionTv;
    @BindView(R.id.cacheSize)
    TextView cacheSize;
    @BindView(R.id.cacheLayout)
    RelativeLayout cacheLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);
        ButterKnife.bind(this);
        initView();
    }

    private void initView() {
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setTitle("");
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
        toolbar.setNavigationOnClickListener(v -> onBackPressed());
        versionTv.setText(String.valueOf(BuildConfig.VERSION_CODE));
        File file = Glide.getPhotoCacheDir(this);
        DecimalFormat format = new DecimalFormat("##0.00");
        String dd = format.format(getDirSize(file));
        cacheSize.setText(dd + "M");
        cacheLayout.setOnClickListener(v ->
                new Thread() {
                    @Override
                    public void run() {
                        Glide.get(getApplication()).clearDiskCache();
                    }
                }.start());
    }

    private float getDirSize(File file) {
        if (file.exists()) {
            if (file.isDirectory()) {
                File[] files = file.listFiles();
                float size = 0;
                for (File f : files) {
                    size = getDirSize(f);
                }
                return size;
            } else {
                return file.length() / 1024 / 1024;
            }
        }
        return 0;
    }

}
