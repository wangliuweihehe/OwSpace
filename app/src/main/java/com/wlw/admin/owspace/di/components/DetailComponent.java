package com.wlw.admin.owspace.di.components;

import com.wlw.admin.owspace.di.modules.DetailMoudle;
import com.wlw.admin.owspace.di.scopes.UserScope;
import com.wlw.admin.owspace.view.activity.AudioDetailActivity;
import com.wlw.admin.owspace.view.activity.DetailActivity;
import com.wlw.admin.owspace.view.activity.VideoDetailActivity;

import dagger.Component;

@UserScope
@Component(modules = DetailMoudle.class, dependencies = NetComponent.class)
public interface DetailComponent {
    void inject(DetailActivity activity);

    void inject(VideoDetailActivity activity);

    void inject(AudioDetailActivity activity);
}
