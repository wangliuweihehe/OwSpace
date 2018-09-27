package com.wlw.admin.owspace.presenter;


import com.wlw.admin.owspace.model.entity.Item;

import java.util.List;

public interface MainContract {
    interface Presenter {
        /**
         * 根据页数获取数据
         *
         * @param page       页数
         * @param model      不知
         * @param pageId     不知
         * @param deviceId   设备号
         * @param createTime 时间
         */
        void getListByPage(int page, int model, String pageId, String deviceId, String createTime);

        /**
         * 推荐数据
         *
         * @param deviceId 设备号
         */
        void getRecommend(String deviceId);
    }

    interface View {
        /**
         * 加载
         */
        void showLoading();

        /**
         * 隐藏加载框
         */
        void dismissLoading();

        /**
         * 无数据
         */
        void showNoData();

        /**
         * 更多
         */
        void showNoMore();

        /**
         * 跟新数据
         */
        void updateListUI(List<Item> itemList);

        /**
         * 数据请求失败
         */
        void showOnFailure();

        /**
         * 展示农历
         */
        void showLunar(String content);
    }
}
