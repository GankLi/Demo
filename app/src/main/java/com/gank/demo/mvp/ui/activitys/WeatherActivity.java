package com.gank.demo.mvp.ui.activitys;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.gank.demo.R;
import com.gank.demo.mvp.model.entity.Weather;
import com.gank.demo.mvp.presenter.IWeatherPresenter;
import com.gank.demo.mvp.presenter.impl.WeatherPresenterImpl;
import com.gank.demo.mvp.ui.views.IWeatherView;
import com.gank.demo.mvp.utils.VolleyRequest;

public class WeatherActivity extends Activity implements IWeatherView {

    private TextView mSummary;
    private Button mBtn;
    private EditText mEdit;
    IWeatherPresenter mWeatherPresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_weather);
        mSummary = (TextView) findViewById(R.id.summary);
        mBtn = (Button) findViewById(R.id.btn);
        mBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mWeatherPresenter.getWeather(mEdit.getText().toString());
            }
        });
        mEdit = (EditText) findViewById(R.id.edit);
        mWeatherPresenter = new WeatherPresenterImpl(this);
        VolleyRequest.buildRequestQueue(this);
    }

    @Override
    public void showLoading() {
        mSummary.setText("正在更新...");
    }

    @Override
    public void hideLoading() {

    }

    @Override
    public void showError() {
        mSummary.setText("获取天气失败");
    }

    @Override
    public void setWeatherInfo(Weather weather) {
        String str = weather.toString();
        mSummary.setText(str);
    }
}
