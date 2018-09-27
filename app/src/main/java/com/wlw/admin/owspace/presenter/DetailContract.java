package com.wlw.admin.owspace.presenter;

import com.wlw.admin.owspace.model.entity.DetailEntity;

public interface DetailContract {
    interface View {
        void showLoading();

        void dismissLoading();

        void updateListUI(DetailEntity detailEntity);

        void showOnFailure();
    }

    interface Presenter {
        void getDetail(String itemId);
    }
}
