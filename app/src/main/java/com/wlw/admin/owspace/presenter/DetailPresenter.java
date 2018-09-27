package com.wlw.admin.owspace.presenter;

import com.wlw.admin.owspace.model.api.ApiService;
import com.wlw.admin.owspace.model.entity.DetailEntity;
import com.wlw.admin.owspace.model.entity.Result;

import javax.inject.Inject;

import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class DetailPresenter implements DetailContract.Presenter {
    private DetailContract.View view;
    private ApiService apiService;

    @Inject
    public DetailPresenter(DetailContract.View view, ApiService apiService) {
        this.view = view;
        this.apiService = apiService;
    }

    @Override
    public void getDetail(String itemId) {
        apiService.getDetail("api", "getPost", itemId, 1)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<Result.Data<DetailEntity>>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        view.showOnFailure();
                    }

                    @Override
                    public void onNext(Result.Data<DetailEntity> detailEntityData) {
                        view.updateListUI(detailEntityData.getDatas());
                    }
                });
    }
}
