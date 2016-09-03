package com.app.checkmoney.Util;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;

import com.kakao.auth.KakaoSDK;

/**
 * Created by Mhwan on 2016. 8. 19..
 */
public class AppContext extends Application{
    private static Context context;
    private static volatile AppContext instance = null;
    private static volatile Activity currentActivity = null;
    private static boolean DEBUG_MODE = false;
    public static Context getContext() {
        return context;
    }
    public static boolean getDEBUG_MODE(){
        return DEBUG_MODE;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        context = getApplicationContext();
        this.DEBUG_MODE = isDebuggableMode();
        initKakao();
    }

    private void initKakao(){
        instance = this;
        KakaoSDK.init(new KakaoSdkAdapter());
    }

    public static void setCurrentActivity(Activity currentActivity) {
        AppContext.currentActivity = currentActivity;
    }

    public static Activity getCurrentActivity() {
        return currentActivity;
    }

    public static AppContext getGlobalApplicationContext() {
        if(instance == null)
            throw new IllegalStateException("this application does not inherit com.kakao.GlobalApplication");
        return instance;
    }



    /**
     * 애플리케이션 종료시 singleton 어플리케이션 객체 초기화한다.
     */
    @Override
    public void onTerminate() {
        super.onTerminate();
        instance = null;
    }

    private boolean isDebuggableMode() {
        boolean debuggable = false;

        PackageManager pm = context.getPackageManager();
        try {
            ApplicationInfo appinfo = pm.getApplicationInfo(context.getPackageName(), 0);
            debuggable = (0 != (appinfo.flags & ApplicationInfo.FLAG_DEBUGGABLE));
        } catch (PackageManager.NameNotFoundException e) {
            /* debuggable variable will remain false */
        }

        return debuggable;
    }
}
