package com.wlw.admin.owspace.di.components;

import com.wlw.admin.owspace.di.modules.MainModule;
import com.wlw.admin.owspace.di.scopes.UserScope;
import com.wlw.admin.owspace.view.activity.MainActivity;

import dagger.Component;

/**
 * @author admin
 */
@UserScope
@Component(modules = MainModule.class, dependencies = NetComponent.class)
public interface MainComponent {

    void inject(MainActivity activity);
}
