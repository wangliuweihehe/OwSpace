package com.wlw.admin.owspace.di.modules;

import com.wlw.admin.owspace.presenter.DetailContract;

import dagger.Module;
import dagger.Provides;

@Module
public class DetailMoudle {
    private DetailContract.View mView;

    public DetailMoudle(DetailContract.View mView) {
        this.mView = mView;
    }

    @Provides
    DetailContract.View provideView() {
        return mView;
    }
}
