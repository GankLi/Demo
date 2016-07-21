package com.gank.demo.mvp.presenter.impl;

import com.gank.demo.mvp.model.IWeatherModel;
import com.gank.demo.mvp.model.entity.Weather;
import com.gank.demo.mvp.model.impl.WeatherModelImpl;
import com.gank.demo.mvp.presenter.IOnWeatherListener;
import com.gank.demo.mvp.presenter.IWeatherPresenter;
import com.gank.demo.mvp.ui.views.IWeatherView;

/**
 * Created by GankLi on 2016/6/23.
 */

public class WeatherPresenterImpl implements IOnWeatherListener, IWeatherPresenter {

    private IWeatherModel mWeatherModel;
    private IWeatherView mWeatherView;

    public WeatherPresenterImpl(IWeatherView weatherView){
        mWeatherView = weatherView;
        mWeatherModel = new WeatherModelImpl();
    }


    @Override
    public void onSuccess(Weather weather) {
        mWeatherView.hideLoading();
        mWeatherView.setWeatherInfo(weather);
    }

    @Override
    public void onError() {
        mWeatherView.hideLoading();
        mWeatherView.showError();
    }

    @Override
    public void getWeather(String cityNO) {
        mWeatherView.showLoading();
        mWeatherModel.loadWeather(cityNO, this);
    }
}
