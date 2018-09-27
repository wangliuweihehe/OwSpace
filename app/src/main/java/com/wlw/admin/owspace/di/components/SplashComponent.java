package com.wlw.admin.owspace.di.components;

import com.wlw.admin.owspace.di.modules.SplashModule;
import com.wlw.admin.owspace.di.scopes.UserScope;
import com.wlw.admin.owspace.view.activity.SplashActivity;

import dagger.Component;

@UserScope
@Component(modules = SplashModule.class, dependencies = NetComponent.class)

public interface SplashComponent {
    void inject(SplashActivity splashActivity);
}
