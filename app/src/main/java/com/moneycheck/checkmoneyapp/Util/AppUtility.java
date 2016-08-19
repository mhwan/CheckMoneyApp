package com.moneycheck.checkmoneyapp.Util;

import android.app.Activity;

/**
 * Created by Mhwan on 2016. 8. 19..
 */
public class AppUtility {
    private static AppUtility instance;

    private AppUtility(){
    }

    public synchronized static AppUtility getInstance(){
        if (instance == null) {
            instance = new AppUtility();
        }
        return instance;
    }

    public void finishApplication(Activity activity) {
        activity.moveTaskToBack(true);
        activity.finish();
    }

}
