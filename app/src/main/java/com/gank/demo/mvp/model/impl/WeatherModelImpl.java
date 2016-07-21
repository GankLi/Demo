package com.gank.demo.mvp.model.impl;

import com.android.volley.Response;
import com.gank.demo.mvp.model.IWeatherModel;
import com.gank.demo.mvp.model.entity.Weather;
import com.gank.demo.mvp.utils.VolleyRequest;
import com.gank.demo.mvp.presenter.IOnWeatherListener;

import com.android.volley.VolleyError;


/**
 * Created by GankLi on 2016/6/23.
 */

public class WeatherModelImpl implements IWeatherModel {
    @Override
    public void loadWeather(String cityNO, final IOnWeatherListener listener) {
        VolleyRequest.newInstance().newGsonRequest("http://www.weather.com.cn/data/sk/" + cityNO + ".html",
                Weather.class, new Response.Listener<Weather>() {
                    @Override
                    public void onResponse(Weather weather) {
                        if (weather != null) {
                            listener.onSuccess(weather);
                        } else {
                            listener.onError();
                        }
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        listener.onError();
                    }
                });
    }
}
