package com.app.checkmoney.Interceptor;

import com.app.checkmoney.Util.DevelopeLog;

import java.io.IOException;
import java.util.HashSet;

import okhttp3.Interceptor;
import okhttp3.Response;

/**
 * Created by Mhwan on 2016. 10. 3..
 */
public class ReceiveCookieInterceptor implements Interceptor {

    @Override
    public Response intercept(Chain chain) throws IOException {
        Response origResponce = chain.proceed(chain.request());

        if (!origResponce.headers("Set-Cookie").isEmpty()){
            HashSet<String> cookies = new HashSet<>();

            for (String header : origResponce.headers("Set-Cookie")) {
                cookies.add(header);
                DevelopeLog.d(header);
            }


        }

        return origResponce;
    }
}
