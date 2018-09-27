package com.wlw.admin.owspace.presenter;

import com.wlw.admin.owspace.app.OwspaceApplication;
import com.wlw.admin.owspace.model.api.ApiService;
import com.wlw.admin.owspace.model.entity.SplashEntity;
import com.wlw.admin.owspace.utils.Log;
import com.wlw.admin.owspace.utils.NetUtil;
import com.wlw.admin.owspace.utils.OkHttpImageDownloader;
import com.wlw.admin.owspace.utils.TimeUtil;


import java.util.List;

import javax.inject.Inject;

import rx.Subscriber;
import rx.Subscription;
import rx.schedulers.Schedulers;

/**
 * @author admin
 */
public class SplashPresenter implements SplashContract.Presenter {
    private ApiService apiService;

    @Inject
    public SplashPresenter(SplashContract.View view, ApiService apiService) {
        this.apiService = apiService;
    }

    @Override
    public void getSplash(String deviceId) {
        String client = "android";
        String version = "1.3.0";
        long time = TimeUtil.getCurrentSeconds();
        apiService.getSplash(client, version, time, deviceId)
                .subscribeOn(Schedulers.io())
                .observeOn(Schedulers.io())
                .subscribe(new Subscriber<SplashEntity>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onNext(SplashEntity splashEntity) {
                        if (NetUtil.isWiFi(OwspaceApplication.getInstance())) {
                            if (splashEntity != null) {
                                List<String> imgs = splashEntity.getImages();
                                for (String url : imgs) {
                                    OkHttpImageDownloader.download(url);
                                }
                            } else {
                                Log.e(getClass().getSimpleName(), "不是WiFi连接");
                            }
                        }
                    }
                });
    }
}
