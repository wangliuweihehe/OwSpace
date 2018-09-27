package com.wlw.admin.owspace.di.modules;

import com.wlw.admin.owspace.presenter.ArtContract;

import dagger.Module;
import dagger.Provides;

@Module
public class ArtModule {
    private ArtContract.View view;

    public ArtModule(ArtContract.View view) {
        this.view = view;
    }

    @Provides
    ArtContract.View provideView() {
        return view;
    }
}
