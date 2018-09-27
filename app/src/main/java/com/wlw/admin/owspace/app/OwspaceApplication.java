package com.wlw.admin.owspace.app;

import android.app.Application;
import android.content.Context;

import com.squareup.leakcanary.LeakCanary;
import com.wlw.admin.owspace.R;
import com.wlw.admin.owspace.di.components.DaggerNetComponent;
import com.wlw.admin.owspace.di.components.NetComponent;
import com.wlw.admin.owspace.di.modules.NetModule;
import com.wlw.admin.owspace.utils.DensityUtils;

import uk.co.chrisjenx.calligraphy.CalligraphyConfig;

/**
 * @author admin
 */
public class OwspaceApplication extends Application {
    private static OwspaceApplication instance;

    private NetComponent netComponent;

    public static OwspaceApplication get(Context context) {
        return (OwspaceApplication) context.getApplicationContext();
    }

    @Override
    public void onCreate() {
        super.onCreate();
        LeakCanary.install(this);
        instance = this;
        DensityUtils.setDensity(this, 360);
        initNet();
        initTypeFace();
    }

    private void initNet() {
        netComponent = DaggerNetComponent.builder()
                .netModule(new NetModule())
                .build();
    }

    private void initTypeFace() {
        CalligraphyConfig calligraphyConfig = new CalligraphyConfig.Builder()
                .setDefaultFontPath("fonts/PMingLiU.ttf")
                .setFontAttrId(R.attr.fontPath)
                .build();
        CalligraphyConfig.initDefault(calligraphyConfig);
    }

    public NetComponent getNetComponent() {
        return netComponent;
    }

    public static OwspaceApplication getInstance() {
        return instance;
    }
}
