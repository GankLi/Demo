package com.gank.demo.mvp.model;

import com.gank.demo.mvp.presenter.IOnWeatherListener;

/**
 * Created by GankLi on 2016/6/23.
 */

public interface IWeatherModel {
    void loadWeather(String cityNO, IOnWeatherListener listener);
}
