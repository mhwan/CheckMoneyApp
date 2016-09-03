package com.app.checkmoney.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.Toast;

import com.app.checkmoney.CustomBase.DoublePressedKill;
import com.app.checkmoney.CustomUi.CustomAlertDialog;
import com.app.checkmoney.CustomUi.CustomLoginDialog;
import com.app.checkmoney.CustomUi.FadeInLinearlayout;
import com.app.checkmoney.Util.AppUtility;
import com.app.checkmoney.Util.DevelopeLog;
import com.kakao.auth.AuthType;
import com.kakao.auth.ErrorCode;
import com.kakao.auth.ISessionCallback;
import com.kakao.auth.Session;
import com.kakao.network.ErrorResult;
import com.kakao.usermgmt.UserManagement;
import com.kakao.usermgmt.callback.MeResponseCallback;
import com.kakao.usermgmt.response.model.UserProfile;
import com.kakao.util.exception.KakaoException;
import com.moneycheck.checkmoneyapp.R;

public class LoginActivity extends AppCompatActivity {
    private DoublePressedKill doublePressed;
    private SessionCallback sessionCallback;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        //AppUtility.getInstance().addActivity(this);
        sessionCallback = new SessionCallback();
        Session.getCurrentSession().addCallback(sessionCallback);
        Session.getCurrentSession().checkAndImplicitOpen();
        doublePressed = new DoublePressedKill(LoginActivity.this);
        initView();
    }

    private void initView(){
        /*
        findViewById(R.id.login_for_kakao).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openKakaoLogin();
            }
        });*/
        findViewById(R.id.login_for_register).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final CustomLoginDialog dialog = new CustomLoginDialog(LoginActivity.this);
                dialog.getWindow().getAttributes().windowAnimations = R.style.LoginDialogAnimation;
                dialog.setListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                        Toast.makeText(LoginActivity.this, getString(R.string.text_succeed_login), Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                        startActivity(intent);
                    }
                }, new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                        Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
                        startActivity(intent);
                    }
                });
                dialog.show();
            }
        });

        ImageView imageView = (ImageView) findViewById(R.id.logo_login);
        FadeInLinearlayout button_layout = (FadeInLinearlayout) findViewById(R.id.button_layout);
        imageView.startAnimation(AnimationUtils.loadAnimation(getApplicationContext(), R.anim.anim_logo));
        button_layout.show();
    }

    private void openKakaoLogin(){
        com.kakao.auth.Session.getCurrentSession().open(AuthType.KAKAO_TALK_EXCLUDE_NATIVE_LOGIN, LoginActivity.this);
    }

    private class SessionCallback implements ISessionCallback {
        //세션 오픈성공 카카오 로그인
        @Override
        public void onSessionOpened() {
            requestMe();
            DevelopeLog.d("session open");
        }

        @Override
        public void onSessionOpenFailed(KakaoException exception) {
            if(exception != null) {
                DevelopeLog.d("TAG" , exception.getMessage());
            }
        }
    }

    protected void requestMe(){
        UserManagement.requestMe(new MeResponseCallback() {
            @Override
            public void onFailure(ErrorResult errorResult) {
                String message = "failed to get user info. msg=" + errorResult;
                DevelopeLog.e(message);

                ErrorCode result = ErrorCode.valueOf(errorResult.getErrorCode());
                if (result == ErrorCode.CLIENT_ERROR_CODE) {
                    openFailedDialog(getString(R.string.text_fail_login_kakao_weak_server));
                } else {
                    openFailedDialog(getString(R.string.text_fail_login_kakao_others));
                }
            }

            @Override
            public void onSessionClosed(ErrorResult errorResult) {
                DevelopeLog.d("session closed");
                openFailedDialog(getString(R.string.text_fail_login_kakao_others));
            }

            @Override
            public void onSuccess(UserProfile userProfile) {
                openRegisterForKakao(userProfile);
            }

            @Override
            public void onNotSignedUp() {
                DevelopeLog.d("on not signed up");
                //showSignup();
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Session.getCurrentSession().removeCallback(sessionCallback);
    }

    private void openRegisterForKakao(UserProfile userProfile){
        Intent intent = new Intent(this, RegisterActivity.class);
        intent.putExtra(AppUtility.BaseDataType.KEY_KAKAO_NAME, userProfile.getNickname());
        intent.putExtra(AppUtility.BaseDataType.KEY_KAKAO_ID, userProfile.getId());
        intent.putExtra(AppUtility.BaseDataType.KEY_KAKAO_IMAGE_PATH, userProfile.getProfileImagePath());
        startActivity(intent);
        finish();
    }

    private void openFailedDialog(String failmessage){
        final CustomAlertDialog dialog = new CustomAlertDialog(this);
        dialog.setTitle(getString(R.string.text_error_title));
        dialog.setMessage(failmessage);
        dialog.setPositiveButton(getString(R.string.okay), new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.cancel();
                AppUtility.getInstance().finishApplication(LoginActivity.this);
            }
        });
        dialog.show();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (Session.getCurrentSession().handleActivityResult(requestCode, resultCode, data)) {
            return;
        }

        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void onBackPressed() {
        if (doublePressed != null)
            doublePressed.onBackPressed();
        else
            super.onBackPressed();
    }
}
