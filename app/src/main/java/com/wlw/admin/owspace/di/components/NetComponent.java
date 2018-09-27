package com.wlw.admin.owspace.di.components;


import com.wlw.admin.owspace.di.modules.NetModule;
import com.wlw.admin.owspace.model.api.ApiService;

import javax.inject.Singleton;

import dagger.Component;
import okhttp3.OkHttpClient;
import retrofit2.Retrofit;

@Singleton
@Component(modules = NetModule.class)
public interface NetComponent {
    ApiService getApiService();

    OkHttpClient getOkhttp();

    Retrofit getRetrofit();
}
