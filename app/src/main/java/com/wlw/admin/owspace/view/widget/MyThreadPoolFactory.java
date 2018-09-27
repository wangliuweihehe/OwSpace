package com.wlw.admin.owspace.view.widget;

import android.support.annotation.NonNull;
import android.text.TextUtils;

import java.util.concurrent.ThreadFactory;

public class MyThreadPoolFactory implements ThreadFactory {
    String name;
    public MyThreadPoolFactory() {
    }
    public MyThreadPoolFactory(String name) {
        this.name = name;
    }
    @Override
    public Thread newThread(@NonNull Runnable r) {
        if (TextUtils.isEmpty(name)) {
            return new Thread();
        } else {
            return new Thread(name);
        }
    }
}
