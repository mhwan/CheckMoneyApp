package com.app.checkmoney.NetworkUtil;

import com.app.checkmoney.Interceptor.*;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by Mhwan on 2016. 9. 11..
 */
public class RetrofitUtil {
    private static RetrofitUtil instance;
    private RetrofitUtil(){
    }

    public static RetrofitUtil getInstance(){
        if (instance == null)
            instance = new RetrofitUtil();
        return instance;
    }

    public Retrofit getRetrofit(){
        return getRetrofit(Mode.DEFAULT);
    }
    public Retrofit getRetrofit(Mode mode){
        Retrofit.Builder retrofitbuilder = new Retrofit.Builder();
        if (mode.equals(Mode.DEFAULT)) {
            retrofitbuilder.baseUrl(UrlList.DEFAULT_URL);
            retrofitbuilder.addConverterFactory(GsonConverterFactory.create());
        } else if (mode.equals(Mode.LOGIN)) {
            retrofitbuilder.baseUrl(UrlList.DEFAULT_URL);
            retrofitbuilder.client(createLoginClient());
            retrofitbuilder.addConverterFactory(GsonConverterFactory.create());
        }

        return retrofitbuilder.build();
    }

    private OkHttpClient createLoginClient(){
        OkHttpClient client = new OkHttpClient.Builder()
                .addInterceptor(new ReceiveCookieInterceptor())
                .build();

        return client;
    }
    public enum Mode{ DEFAULT, LOGIN }
}
