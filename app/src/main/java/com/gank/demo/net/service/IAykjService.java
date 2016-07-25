package com.gank.demo.net.service;

import com.gank.demo.net.bean.PrintStatus;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;
import rx.Observable;

/**
 * Created by GankLi on 2016/7/22.
 */

public interface IAykjService {

    public static final String BASE_URL = "http://api.aykj0577.com";

    @GET("WS/DealData.ashx")
    Observable<String> uploadPrintData(@Query("op") String op,
                                     @Query("content") String content,
                                     @Query("unm") String unm,
                                     @Query("dno") String dno,
                                     @Query("key") String key,
                                     @Query("mode") String mode,
                                     @Query("msgno") String msgno);

    @GET("WS/DealData.ashx")
    Observable<String> queryPrintStatus(@Query("op") String op,
                                             @Query("unm") String unm,
                                             @Query("dno") String dno,
                                             @Query("key") String key,
                                             @Query("mode") String mode);
}
