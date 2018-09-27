package com.wlw.admin.owspace.presenter;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.wlw.admin.owspace.model.api.ApiService;
import com.wlw.admin.owspace.model.entity.Item;
import com.wlw.admin.owspace.model.entity.Result;
import com.wlw.admin.owspace.utils.Log;
import com.wlw.admin.owspace.utils.TimeUtil;

import java.util.List;

import javax.inject.Inject;

import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class MainPresenter implements MainContract.Presenter {
    private static final String TAG = "MainPresenter";
    private MainContract.View view;
    private ApiService apiService;

    @Inject
    public MainPresenter(MainContract.View view, ApiService apiService) {
        this.view = view;
        this.apiService = apiService;
        Log.e(TAG, "app:" + apiService);
    }

    @Override
    public void getListByPage(int page, int model, String pageId, String deviceId, String createTime) {
        apiService.getList("api", "getList", page, model, pageId, createTime, "android", "1.3.0", TimeUtil.getCurrentSeconds(), deviceId, 1)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<Result.Data<List<Item>>>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        e.printStackTrace();
                        view.showOnFailure();
                    }

                    @Override
                    public void onNext(Result.Data<List<Item>> listData) {
                        if (listData.getDatas().size() > 0) {
                            view.updateListUI(listData.getDatas());
                        } else {
                            view.showNoMore();
                        }
                    }
                });
    }

    @Override
    public void getRecommend(String deviceId) {
        String key = TimeUtil.getDate("yyyyMMdd");
        Log.e(TAG, key);
        apiService.getRecommend("home", "Api", "getLunar", "android", deviceId, deviceId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<String>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onNext(String s) {
                        String key = TimeUtil.getDate("yyyyMMdd");
                        try {
                            JsonParser jsonParser = new JsonParser();
                            JsonElement je = jsonParser.parse(s);
                            JsonObject jsonObject = je.getAsJsonObject();
                            jsonObject = jsonObject.getAsJsonObject("datas");
                            jsonObject = jsonObject.getAsJsonObject(key);
                            view.showLunar(jsonObject.get("thumbnail").getAsString());
                        } catch (Exception e) {
                            e.printStackTrace();
                        }

                    }
                })
        ;
    }
}
