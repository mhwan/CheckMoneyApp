package com.app.checkmoney.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.Toast;

import com.app.checkmoney.CustomBase.DoublePressedKill;
import com.app.checkmoney.CustomUi.FadeInLinearlayout;
import com.app.checkmoney.Util.AppUtility;
import com.moneycheck.checkmoneyapp.R;

public class LoginActivity extends AppCompatActivity {
    private DoublePressedKill doublePressed;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        AppUtility.getInstance().addActivity(this);
        doublePressed = new DoublePressedKill(LoginActivity.this);
        initView();
    }

    private void initView(){
        findViewById(R.id.login_for_kakao).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getApplicationContext(), "카카카ㅏ카카카카카오ㅗ오오오오오오오ㅗ오어", Toast.LENGTH_SHORT).show();
            }
        });
        findViewById(R.id.login_for_register).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
                startActivity(intent);
            }
        });

        ImageView imageView = (ImageView) findViewById(R.id.logo_login);
        FadeInLinearlayout button_layout = (FadeInLinearlayout) findViewById(R.id.button_layout);
        imageView.startAnimation(AnimationUtils.loadAnimation(getApplicationContext(), R.anim.anim_logo));
        button_layout.show();
    }

    @Override
    public void onBackPressed() {
        if (doublePressed != null)
            doublePressed.onBackPressed();
        else
            super.onBackPressed();
    }
}
