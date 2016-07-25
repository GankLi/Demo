package com.gank.demo.net;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.gank.demo.R;
import com.gank.demo.net.service.IAykjService;
import com.gank.demo.net.utils.MockData;

import java.util.List;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.scalars.ScalarsConverterFactory;
import rx.Observable;
import rx.Observer;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class NetDemoActivity extends Activity {

    Button mButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.w("Gank", "onCreate: ");
        setContentView(R.layout.activity_net_demo);
        mButton = (Button) findViewById(R.id.button);
        mButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                OkHttpClient.Builder builder = new OkHttpClient.Builder();
                List<Interceptor> inter =  builder.interceptors();
                if(inter != null){
                    inter.add(new HttpLoggingInterceptor(new HttpLoggingInterceptor.Logger() {
                        @Override
                        public void log(String message) {
                            Log.w("Gank", message);
                        }
                    }).setLevel(HttpLoggingInterceptor.Level.BODY));
                }
                OkHttpClient client = builder.build();
                Retrofit retrofit = new Retrofit.Builder()
                        .client(client)
                        .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                        .addConverterFactory(ScalarsConverterFactory.create())
                        .baseUrl(IAykjService.BASE_URL)
                        .build();

                IAykjService service = retrofit.create(IAykjService.class);
                Log.w("Gank", "Key: " + MockData.KEY);
                Observable<String> observable = service.uploadPrintData(MockData.OP, MockData.CONTENT, MockData.UMN, MockData.DNO, MockData.KEY, MockData.MODE, MockData.MSGNO);
                observable.observeOn(AndroidSchedulers.mainThread()).subscribeOn(Schedulers.io())
                        .subscribe(new Observer<String>() {
                            @Override
                            public void onCompleted() {
                                Log.w("Gank", "onCompleted");
                            }

                            @Override
                            public void onError(Throwable e) {
                                Log.w("Gank", "onError");
                            }

                            @Override
                            public void onNext(String res) {
                                Log.w("Gank", "Upload -- Res: " + res);
                            }
                        });
                Observable<String> observable1 =  service.queryPrintStatus(MockData.OP_QUERY_DEVICE, MockData.UMN, MockData.DNO, MockData.KEY, MockData.MODE_QUERY_DEVICE);
                observable1.observeOn(AndroidSchedulers.mainThread()).subscribeOn(Schedulers.io())
                        .subscribe(new Observer<String>() {
                            @Override
                            public void onCompleted() {
                                Log.w("Gank", "onCompleted");
                            }

                            @Override
                            public void onError(Throwable e) {
                                Log.w("Gank", "onError", e);
                            }

                            @Override
                            public void onNext(String res) {
                                Log.w("Gank", "Query Device -- Res: " + res);
                            }
                        });

                Observable<String> observable2 =  service.queryOrderStatus(MockData.OP_QUERY_ORDER, MockData.UMN, MockData.MSGNO,  MockData.DNO, MockData.KEY, MockData.MODE_QUERY_ORDER);
                observable2.observeOn(AndroidSchedulers.mainThread()).subscribeOn(Schedulers.io())
                        .subscribe(new Observer<String>() {
                            @Override
                            public void onCompleted() {
                                Log.w("Gank", "onCompleted");
                            }

                            @Override
                            public void onError(Throwable e) {
                                Log.w("Gank", "onError", e);
                            }

                            @Override
                            public void onNext(String res) {
                                Log.w("Gank", "Query Order -- Res: " + res);
                            }
                        });
            }
        });
    }

}
