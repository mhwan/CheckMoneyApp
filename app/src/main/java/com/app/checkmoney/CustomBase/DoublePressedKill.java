package com.app.checkmoney.CustomBase;

import android.app.Activity;
import android.widget.Toast;

import com.app.checkmoney.Util.AppUtility;
import com.moneycheck.checkmoneyapp.R;

/**
 * Created by Mhwan on 2016. 8. 19..
 */
public class DoublePressedKill {
    private long backKeyPressTime = 0;
    private Activity activity;

    public DoublePressedKill(Activity activity){
        this.activity = activity;
    }

    public void onBackPressed(){
        //2초이상 지났으면 마지막 시간을 현재 시간으로 갱신하고 토스트 메시지 실행
        if (System.currentTimeMillis() > backKeyPressTime + 2000) {
            backKeyPressTime = System.currentTimeMillis();
            Toast.makeText(activity, activity.getString(R.string.text_finish_application), Toast.LENGTH_SHORT).show();
            return;
        }
        //2초이상 안지났으면 앱 종료
        else
            AppUtility.getInstance().finishApplication();
    }
}
