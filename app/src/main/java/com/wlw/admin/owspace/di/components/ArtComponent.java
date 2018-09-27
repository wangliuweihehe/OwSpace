package com.wlw.admin.owspace.di.components;

import com.wlw.admin.owspace.di.modules.ArtModule;
import com.wlw.admin.owspace.di.scopes.UserScope;
import com.wlw.admin.owspace.view.activity.ArtActivity;
import com.wlw.admin.owspace.view.activity.DailyActivity;


import dagger.Component;

@UserScope
@Component(modules = ArtModule.class, dependencies = NetComponent.class)
public interface ArtComponent {
    void inject(ArtActivity artActivity);
    void inject(DailyActivity artActivity);
}
