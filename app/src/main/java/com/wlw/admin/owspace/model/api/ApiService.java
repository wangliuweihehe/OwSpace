package com.wlw.admin.owspace.model.api;

import com.wlw.admin.owspace.model.entity.DetailEntity;
import com.wlw.admin.owspace.model.entity.Item;
import com.wlw.admin.owspace.model.entity.Result;
import com.wlw.admin.owspace.model.entity.SplashEntity;

import java.util.List;

import retrofit2.http.GET;
import retrofit2.http.Query;
import rx.Observable;
import rx.Observer;

public interface ApiService {
    /**
     * 获取闪屏页图片
     *  http://static.owspace.com/static/picture_list.txt?client=android&version=1.3.0&time=1467864021&device_id=866963027059338
     *
     * @param client   Android 端
     * @param version  版本
     * @param time     时间
     * @param deviceId 设备号
     * @return observable
     */
    @GET("static/picture_list.txt")
    Observable<SplashEntity> getSplash(@Query("client") String client, @Query("version") String version, @Query("time") long time, @Query("device_id") String deviceId);

    /**
     * http://static.owspace.com/?c=api&a=getPost&post_id=292296&show_sdv=1
     *
     * @param c c
     * @param a a
     * @param post_id post_id
     * @param show_sdv show_sdv
     * @return data
     */
    @GET("/")
    Observable<Result.Data<DetailEntity>>getDetail(@Query("c")String c,@Query("a")String a,@Query("post_id")String post_id,@Query("show_sdv")int show_sdv);

    @GET("/")
    Observable<Result.Data<List<Item>>> getList(@Query("c") String c, @Query("a") String a, @Query("p") int page, @Query("model") int model, @Query("page_id") String pageId, @Query("create_time") String createTime, @Query("client") String client, @Query("version") String version, @Query("time") long time, @Query("device_id") String deviceId, @Query("show_sdv") int show_sdv);

    /**
     * http://static.owspace.com/index.php?m=Home&c=Api&a=getLunar&client=android&device_id=866963027059338&version=866963027059338
     * @return
     */
    @GET("index.php")
    Observable<String> getRecommend(@Query("m") String m,@Query("c") String api,@Query("a") String a,@Query("client") String client,@Query("version") String version, @Query("device_id") String deviceId);
}
