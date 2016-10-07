package com.app.checkmoney.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Html;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.app.checkmoney.CustomBase.DataValidation;
import com.app.checkmoney.CustomBase.DoublePressedKill;
import com.app.checkmoney.CustomUi.CustomAlertDialog;
import com.app.checkmoney.CustomUi.CustomLoginDialog;
import com.app.checkmoney.CustomUi.CustomProgressDialog;
import com.app.checkmoney.CustomUi.FadeInLinearlayout;
import com.app.checkmoney.Util.AppUtility;
import com.app.checkmoney.Util.DevelopeLog;
import com.app.checkmoney.NetworkUtil.RetrofitService;
import com.app.checkmoney.NetworkUtil.RetrofitUtil;
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

import java.io.IOException;
import java.util.HashMap;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener{
    private DoublePressedKill doublePressed;
    private SessionCallback sessionCallback;
    private boolean isLoginMode = false;


    /**
     * 로그인모드로 들어왔을때 백버튼을 누를때 어떻게 처리해줄것인가!?
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        //AppUtility.getInstance().addActivity(this);
        Bundle bundle = getIntent().getExtras();
        if (bundle!=null){
            isLoginMode = bundle.getBoolean("loginmode");
        }

        sessionCallback = new SessionCallback();
        Session.getCurrentSession().addCallback(sessionCallback);
        Session.getCurrentSession().checkAndImplicitOpen();
        doublePressed = new DoublePressedKill(LoginActivity.this);
        initView();
    }

    private void initView() {
        /*
        findViewById(R.id.login_for_kakao).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openKakaoLogin();
            }
        });*/
        ImageView imageView = (ImageView) findViewById(R.id.logo_login);
        FadeInLinearlayout button_layout = (FadeInLinearlayout) findViewById(R.id.button_layout);
        if (!isLoginMode) {
            findViewById(R.id.login_for_register).setOnClickListener(this);
            TextView button_go_register = (TextView) findViewById(R.id.button_go_register);
            button_go_register.setText(Html.fromHtml(getString(R.string.text_register_message)));
            button_go_register.setOnClickListener(this);
            imageView.startAnimation(AnimationUtils.loadAnimation(getApplicationContext(), R.anim.anim_logo));
            button_layout.show();
        } else {
            button_layout.setVisibility(View.GONE);
            final CustomLoginDialog loginDialog = new CustomLoginDialog(LoginActivity.this);
            loginDialog.setListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (isValidate(loginDialog.getInputId(), loginDialog.getInputPw())) {
                        if (AppUtility.getInstance().isNetworkConnection())
                            loginWork(loginDialog.getInputId(), loginDialog.getInputPw(), loginDialog);
                        else
                            Toast.makeText(getApplicationContext(), getString(R.string.text_unconnected_internet), Toast.LENGTH_SHORT).show();
                    }
                }
            }, new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    loginDialog.dismiss();
                }
            });
            loginDialog.setCancelable(false);
            loginDialog.setCanceledOnTouchOutside(false);
            loginDialog.show();
        }
    }

    private boolean isValidate(String email, String pw) {
        if (DataValidation.isEmptyString(email) || !DataValidation.isValidValue(email, DataValidation.Type.EMAIL)) {
            Toast.makeText(getApplicationContext(), getString(R.string.text_invalidate_email), Toast.LENGTH_SHORT).show();
            return false;
        } else if (DataValidation.isEmptyString(pw)) {
            Toast.makeText(getApplicationContext(), getString(R.string.text_invalidate_password), Toast.LENGTH_SHORT).show();
            return false;
        }

        return true;
    }

    private void loginWork(final String email, final String pw, final CustomLoginDialog dialog) {
        final CustomProgressDialog progressDialog = new CustomProgressDialog(LoginActivity.this);
        progressDialog.setMessage(getString(R.string.text_working_register));
        progressDialog.show();

        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("userEmail", email);
        hashMap.put("userPassWord", pw);

        RetrofitService.LoginService service = RetrofitUtil.getInstance().getRetrofit(RetrofitUtil.Mode.LOGIN).create(RetrofitService.LoginService.class);
        Call<ResponseBody> call = service.loginForApp(hashMap);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (progressDialog.isShowing())
                    progressDialog.dismiss();

                try {
                    String result = response.body().string();
                    DevelopeLog.d("result :: "+result);
                    if (result.contains(RetrofitService.GeneralResponse.SUCCESS)) {
                        dialog.dismiss();
                        Toast.makeText(LoginActivity.this, getString(R.string.text_succeed_login), Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                        startActivity(intent);
                        overridePendingTransition(R.anim.anim_slide_in, R.anim.anim_keep);
                    } else if (result.contains(RetrofitService.GeneralResponse.FAIL)) {
                        Toast.makeText(getApplicationContext(), getString(R.string.text_fail_login), Toast.LENGTH_SHORT).show();
                        DevelopeLog.d("fail to login!!");
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                    DevelopeLog.e("err to login!!");
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                if (progressDialog.isShowing())
                    progressDialog.dismiss();

                DevelopeLog.e(t.getMessage());
                final CustomAlertDialog dialog = new CustomAlertDialog(LoginActivity.this);
                dialog.setTitle(getString(R.string.text_error_title));
                dialog.setMessage(getString(R.string.text_error_unknwon));
                dialog.setPositiveButton(getString(R.string.okay), new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                    }
                });
                dialog.show();

            }
        });
        /*
        new AsyncTask<String, Void, NetworkUtility.BasicStatus>(){
            private CustomProgressDialog progressDialog;
            @Override
            protected void onPostExecute(NetworkUtility.BasicStatus basicStatus) {
                progressDialog.dismiss();
                switch (basicStatus){
                    case OK :
                        dialog.dismiss();
                        Toast.makeText(LoginActivity.this, getString(R.string.text_succeed_login), Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                        startActivity(intent);
                        overridePendingTransition(R.anim.anim_slide_in, R.anim.anim_keep);
                        break;
                    case FAIL:
                        Toast.makeText(getApplicationContext(), getString(R.string.text_fail_login), Toast.LENGTH_SHORT).show();
                        break;
                }
            }

            @Override
            protected void onPreExecute() {
                progressDialog = new CustomProgressDialog(LoginActivity.this, CustomProgressDialog.Type.TRANSPARENT);
                progressDialog.show();
            }

            @Override
            protected NetworkUtility.BasicStatus doInBackground(String... params) {
                NetworkUtility.BasicStatus result = NetworkUtility.BasicStatus.FAIL;
                try {
                    result = NetworkUtility.getInstance().loginForApp(email, pw);
                } catch (IOException | JSONException e) {
                    e.printStackTrace();
                }
                return result;
            }
        }.execute(email, pw);*/
    }

    private void openKakaoLogin() {
        com.kakao.auth.Session.getCurrentSession().open(AuthType.KAKAO_TALK_EXCLUDE_NATIVE_LOGIN, LoginActivity.this);
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch (id) {
            case R.id.login_for_register:
                final CustomLoginDialog dialog = new CustomLoginDialog(LoginActivity.this);
                dialog.getWindow().getAttributes().windowAnimations = R.style.LoginDialogAnimation;
                dialog.setListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (isValidate(dialog.getInputId(), dialog.getInputPw())) {
                            if (AppUtility.getInstance().isNetworkConnection())
                                loginWork(dialog.getInputId(), dialog.getInputPw(), dialog);
                            else
                                Toast.makeText(getApplicationContext(), getString(R.string.text_unconnected_internet), Toast.LENGTH_SHORT).show();
                        }
                    }
                }, new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                        //비밀번호 잊어버린경우
                    }
                });
                dialog.show();
                break;

            case R.id.button_go_register:
                Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
                startActivity(intent);
                overridePendingTransition(R.anim.anim_slide_in, R.anim.anim_keep);
                break;
        }
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
            if (exception != null) {
                DevelopeLog.d("TAG", exception.getMessage());
            }
        }
    }

    protected void requestMe() {
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
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Session.getCurrentSession().removeCallback(sessionCallback);
    }

    private void openRegisterForKakao(UserProfile userProfile) {
        Intent intent = new Intent(this, RegisterActivity.class);
        intent.putExtra(AppUtility.BaseDataType.KEY_KAKAO_NAME, userProfile.getNickname());
        intent.putExtra(AppUtility.BaseDataType.KEY_KAKAO_ID, userProfile.getId());
        intent.putExtra(AppUtility.BaseDataType.KEY_KAKAO_IMAGE_PATH, userProfile.getProfileImagePath());
        startActivity(intent);
        overridePendingTransition(R.anim.anim_slide_in, R.anim.anim_keep);
        finish();
    }

    private void openFailedDialog(String failmessage) {
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
