package com.gank.demo.mvp.presenter;

import com.gank.demo.mvp.model.entity.Weather;

/**
 * Created by GankLi on 2016/6/23.
 */

public interface IOnWeatherListener {
    void onSuccess(Weather weather);
    void onError();
}
