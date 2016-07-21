package com.gank.demo.mvp.ui.views;

import com.gank.demo.mvp.model.entity.Weather;

/**
 * Created by GankLi on 2016/6/23.
 */

public interface IWeatherView {
    void showLoading();

    void hideLoading();

    void showError();

    void setWeatherInfo(Weather weather);
}
