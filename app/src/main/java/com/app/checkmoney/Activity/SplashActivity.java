package com.app.checkmoney.Activity;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.WindowManager;

import com.app.checkmoney.Util.AppUtility;
import com.moneycheck.checkmoneyapp.R;

/**
 * Created by Mhwan on 2016. 8. 19..
 */
public class SplashActivity extends Activity {
    private final Handler handler = new Handler();
    private final int SPLASH_DELAY_TIME = 1400;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        hideEverything();

        // 이미지 리소스 주소 넣어줄것
        setBackgroundImage(findViewById(R.id.logo_splash), R.mipmap.logo_login);
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent intent = new Intent(SplashActivity.this, LoginActivity.class);
                startActivity(intent);
                overridePendingTransition(0, R.anim.fade_out);
                finish();

            }
        }, SPLASH_DELAY_TIME);
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    private void setBackgroundImage(View view, int resourceId){
        Bitmap bitmap = AppUtility.getInstance().decodeBitmapFromResource(getResources(), resourceId, 1);
        if (Build.VERSION.SDK_INT >= 16) {
            view.setBackground(new BitmapDrawable(getResources(), bitmap));

        } else {
            view.setBackgroundDrawable(new BitmapDrawable(getResources(), bitmap));
        }
    }

    private void hideEverything() {
        if (Build.VERSION.SDK_INT < 16) {
            getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                    WindowManager.LayoutParams.FLAG_FULLSCREEN);
        } else {
            View decorView = getWindow().getDecorView();
            // Hide the status bar.
            int uiOptions = View.SYSTEM_UI_FLAG_FULLSCREEN;
            decorView.setSystemUiVisibility(uiOptions);
        }
    }
}
