package com.app.checkmoney.Interceptor;

import java.io.IOException;
import java.util.Set;

import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by Mhwan on 2016. 10. 6..
 */

public class AddCookieInterceptor implements Interceptor {
    @Override
    public Response intercept(Chain chain) throws IOException {
        Request.Builder builder = chain.request().newBuilder();
        Set<String> preferences;

        /*
        for (String cookie : preferences) {
            builder.addHeader("Cookie", cookie);
        }*/
        //builder.removeHeader("User-Agent").addHeader("User-Agent", "Android");

        return chain.proceed(builder.build());
    }
}
