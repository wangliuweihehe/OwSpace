package com.wlw.admin.owspace.di.modules;


import com.wlw.admin.owspace.presenter.MainContract;

import dagger.Module;
import dagger.Provides;

@Module
public class MainModule {
    private MainContract.View mView;

    public MainModule(MainContract.View mView) {
        this.mView = mView;
    }

    @Provides
    public MainContract.View provideMainView() {
        return mView;
    }
}
