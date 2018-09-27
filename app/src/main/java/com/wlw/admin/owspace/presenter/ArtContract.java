package com.wlw.admin.owspace.presenter;

import com.wlw.admin.owspace.model.entity.Item;

import java.util.List;

public interface ArtContract {
    interface Presenter {
        void getListByPage(int page, int model, String pageId, String deviceId, String createTime);
    }

    interface View {
        void showLoading();

        void dismissLoading();

        void showNoData();

        void showNoMore();

        void updateListUI(List<Item> itemList);

        void showOnFailure();
    }
}
