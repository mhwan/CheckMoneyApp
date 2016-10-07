package com.app.checkmoney.Activity;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.TextInputLayout;
import android.text.Selection;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.app.checkmoney.CustomBase.BaseActivity;
import com.app.checkmoney.CustomBase.DataValidation;
import com.app.checkmoney.CustomBase.RequestPermission;
import com.app.checkmoney.CustomUi.CustomProgressDialog;
import com.app.checkmoney.Util.AppUtility;
import com.app.checkmoney.Util.DevelopeLog;
import com.app.checkmoney.Util.ImageLoaderUtility;
import com.app.checkmoney.NetworkUtil.RetrofitService;
import com.app.checkmoney.NetworkUtil.RetrofitUtil;
import com.moneycheck.checkmoneyapp.R;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.io.IOException;
import java.util.HashMap;

import de.hdodenhof.circleimageview.CircleImageView;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RegisterActivity extends BaseActivity implements View.OnFocusChangeListener, View.OnClickListener{
    private View root_view;
    private TextView button_register;
    private EditText input_name, input_phnum, input_email, input_pw, input_repw;
    private TextInputLayout name_layout, phnum_layout, email_layout, pw_layout, checkPw_layout;
    private CircleImageView image_profile;
    private boolean isKakaoRegister = false;
    private long kakaoId = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setFromBaseView(R.layout.activity_register);
        setToolbar(R.layout.layout_register_toolbar, R.id.toolbar_close, ToolbarType.SUB_TYPE);
        Bundle bundle = getIntent().getExtras();
        initView();
        setContent(bundle);
    }

    private void initView() {
        root_view = findViewById(R.id.root_register);
        root_view.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                    root_view.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                }
                setBackgroundImage(root_view.getHeight());
            }
        });
        email_layout = (TextInputLayout) findViewById(R.id.inputlayout_email);
        checkPw_layout = (TextInputLayout) findViewById(R.id.inputlayout_checkpw);
        pw_layout = (TextInputLayout) findViewById(R.id.inputlayout_pw);
        phnum_layout = (TextInputLayout) findViewById(R.id.inputlayout_phnum);
        name_layout = (TextInputLayout) findViewById(R.id.inputlayout_name);
        button_register = (TextView) findViewById(R.id.button_register);
        input_email = (EditText) findViewById(R.id.input_email);
        input_email.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {

            }
        });
        input_pw = (EditText) findViewById(R.id.input_pw);
        input_repw = (EditText) findViewById(R.id.input_checkpw);
        input_repw.setOnFocusChangeListener(this);
        input_name = (EditText) findViewById(R.id.input_name);
        input_name.setOnFocusChangeListener(this);
        input_phnum = (EditText) findViewById(R.id.input_phnum);
        input_phnum.setOnFocusChangeListener(this);
        image_profile = (CircleImageView) findViewById(R.id.image_profile);
        findViewById(R.id.button_edit_profile).setOnClickListener(this);
        button_register.setOnClickListener(this);
    }

    private void registerforKakao(String name, String phnum) {
        final CustomProgressDialog progressDialog = new CustomProgressDialog(getContext(), CustomProgressDialog.Type.TRANSPARENT);
        progressDialog.show();

        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("kakaoId", String.valueOf(kakaoId));
        hashMap.put("userName", name);
        hashMap.put("userPhoneNumber", phnum);
    }
    private void registerWork(String email, String name, String pw, String phnum){
        final CustomProgressDialog progressDialog = new CustomProgressDialog(getContext(), CustomProgressDialog.Type.TRANSPARENT);
        //progressDialog.setMessage(getString(R.string.text_working_register));
        progressDialog.show();

        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("userEmail", email);
        hashMap.put("kakaoId", String.valueOf(kakaoId));
        hashMap.put("userName", name);
        hashMap.put("userPassWord", pw);


        RetrofitService.RegisterService service = RetrofitUtil.getInstance().getRetrofit().create(RetrofitService.RegisterService.class);
        Call<okhttp3.ResponseBody> call = service.registerForApp(hashMap);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (progressDialog.isShowing())
                    progressDialog.dismiss();
                try {
                    String result = response.body().string();
                    DevelopeLog.d("result :: "+result);
                    if (result.equals(RetrofitService.RegisterResponse.SUCCESS)) {
                        Toast.makeText(getContext(), getString(R.string.text_succeed_register), Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
                        intent.putExtra("loginmode", true);
                        startActivityWithAnim(intent);
                        RegisterActivity.this.finish();
                    } else if (result.equals(RetrofitService.RegisterResponse.EXIST)) {
                        Toast.makeText(getContext(), getString(R.string.text_exist_account_register), Toast.LENGTH_SHORT).show();
                    } else if (result.equals(RetrofitService.RegisterResponse.FAIL)) {
                        Toast.makeText(getContext(), getString(R.string.text_failed_register), Toast.LENGTH_SHORT).show();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                    DevelopeLog.e("err");
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                if (progressDialog.isShowing())
                    progressDialog.dismiss();
                DevelopeLog.e(t.getMessage());
                showUnknwonErrorDialog();
            }
        });
        /*
        new AsyncTask<String, Void, NetworkUtility.RegisterStatus>() {
            private CustomProgressDialog progressDialog;

            @Override
            protected void onPreExecute() {
                progressDialog = new CustomProgressDialog(getContext());
                progressDialog.setMessage(getString(R.string.text_working_register));
                progressDialog.show();
            }

            @Override
            protected void onPostExecute(NetworkUtility.RegisterStatus registerStatus) {
                switch (registerStatus){
                    case OK:
                        Toast.makeText(getContext(), getString(R.string.text_succeed_register), Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(RegisterActivity.this, MainActivity.class);
                        startActivityWithAnim(intent);
                        RegisterActivity.this.finish();
                        break;
                    case FAIL:
                        Toast.makeText(getContext(), getString(R.string.text_failed_register), Toast.LENGTH_SHORT).show();
                        break;
                    case EXIST:
                        Toast.makeText(getContext(), getString(R.string.text_exist_account_register), Toast.LENGTH_SHORT).show();
                        break;
                }
                progressDialog.dismiss();
            }

            @Override
            protected NetworkUtility.RegisterStatus doInBackground(String... params) {
                NetworkUtility.RegisterStatus result = NetworkUtility.RegisterStatus.FAIL;
                try {
                    result = NetworkUtility.getInstance().registerForApp(params[0], kakaoId, params[1], params[2], params[3]);
                } catch (IOException | JSONException e) {
                    e.printStackTrace();
                }
                return result;
            }
        }.execute(email, name, pw, phnum);*/
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch (id) {
            case R.id.button_register:
                String email = input_email.getText().toString();
                String name = input_name.getText().toString();
                String phnum = input_phnum.getText().toString().replace("-", "");
                String pw = input_pw.getText().toString();
                String checkPw = input_repw.getText().toString();
                if (checkValidate(name, email, pw, checkPw, phnum) && button_register.isEnabled()) {
                    if (AppUtility.getInstance().isNetworkConnection()) {
                        if (isKakaoRegister)
                            registerforKakao(name, phnum);
                        else
                            registerWork(email, name, pw, phnum);
                        /*
                        Toast.makeText(getContext(), getString(R.string.text_succeed_register), Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(RegisterActivity.this, MainActivity.class);
                        startActivityWithAnim(intent);
                        RegisterActivity.this.finish();*/
                    }
                    else
                        Toast.makeText(getContext(), getString(R.string.text_unconnected_internet), Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.button_edit_profile:
                if (new RequestPermission(getContext(), RequestPermission.WRITE_EXTERNAL_STORAGE_TYPE).isGranted()) {
                    openPhotoPicker();
                }
                break;
        }
    }

    @Override
    public void onFocusChange(View v, boolean hasFocus) {
        int id = v.getId();

        switch (id){
            case R.id.input_name:
                String input = input_name.getText().toString();
                if (hasFocus == false && !DataValidation.isEmptyString(input) && !DataValidation.isValidLength(input, 2, 10))
                    showCancelErrorMessage(INPUT_TYPE.NAME, true);
                else
                    showCancelErrorMessage(INPUT_TYPE.NAME, false);
                break;

            case R.id.input_checkpw:
                String pw = input_repw.getText().toString();
                String original = input_pw.getText().toString();

                if (hasFocus == false && !DataValidation.isEmptyString(pw) && !original.equals(pw)){
                    showCancelErrorMessage(INPUT_TYPE.CHECK_PW, true);
                } else
                    showCancelErrorMessage(INPUT_TYPE.CHECK_PW, false);
                break;

            case R.id.input_email:
                String email = input_email.getText().toString();
                if (hasFocus == false && !DataValidation.isEmptyString(email) && !DataValidation.isValidValue(email, DataValidation.Type.EMAIL)){
                    showCancelErrorMessage(INPUT_TYPE.EMAIL, true);
                } else {
                    showCancelErrorMessage(INPUT_TYPE.EMAIL, false);
                }
                break;

            case R.id.input_phnum:
                if (hasFocus == true) {
                    showCancelErrorMessage(INPUT_TYPE.PH_NUM, false);
                    if (new RequestPermission(getContext(), RequestPermission.READ_PHONE_STATE_TYPE).isGranted())
                        setPhoneNumber();
                }
                break;
        }
    }

    private enum INPUT_TYPE { NAME, EMAIL, PW, CHECK_PW, PH_NUM }

    private void showCancelErrorMessage(INPUT_TYPE type, boolean show){
        if (show) {
            if (type.equals(INPUT_TYPE.NAME)) {
                name_layout.setErrorEnabled(true);
                name_layout.setError(getString(R.string.text_invalidate_name));
            } else if (type.equals(INPUT_TYPE.EMAIL)) {
                email_layout.setErrorEnabled(true);
                email_layout.setError(getString(R.string.text_invalidate_email));
            } else if (type.equals(INPUT_TYPE.PW)) {
                pw_layout.setErrorEnabled(true);
                pw_layout.setError(getString(R.string.text_invalidate_password));
            } else if (type.equals(INPUT_TYPE.CHECK_PW)) {
                checkPw_layout.setErrorEnabled(true);
                checkPw_layout.setError(getString(R.string.text_not_same_password));
            } else if (type.equals(INPUT_TYPE.PH_NUM)) {
                phnum_layout.setErrorEnabled(true);
                phnum_layout.setError(getString(R.string.text_invalidate_phnum));
            }
        } else {
            if (type.equals(INPUT_TYPE.NAME)) {
                name_layout.setErrorEnabled(false);
                name_layout.setError("");
            } else if (type.equals(INPUT_TYPE.EMAIL)) {
                email_layout.setErrorEnabled(false);
                email_layout.setError("");
            } else if (type.equals(INPUT_TYPE.PW)) {
                pw_layout.setErrorEnabled(false);
                pw_layout.setError("");
            } else if (type.equals(INPUT_TYPE.CHECK_PW)) {
                checkPw_layout.setErrorEnabled(false);
                checkPw_layout.setError("");
            } else if (type.equals(INPUT_TYPE.PH_NUM)) {
                phnum_layout.setErrorEnabled(false);
                phnum_layout.setError("");
            }
        }
    }
    /**
     * 카카오 계정으로 가입할때 값 세팅
     */
    private void setContent(Bundle bundle){
        if (bundle == null) {
            isKakaoRegister = false;
            return;
        }

        isKakaoRegister = true;
        kakaoId = bundle.getLong(AppUtility.BaseDataType.KEY_KAKAO_ID);
        email_layout.setVisibility(View.GONE);
        pw_layout.setVisibility(View.GONE);
        checkPw_layout.setVisibility(View.GONE);

        input_name.setText(bundle.getString(AppUtility.BaseDataType.KEY_KAKAO_NAME));
        String image_path = bundle.getString(AppUtility.BaseDataType.KEY_KAKAO_IMAGE_PATH);
        if (!image_path.isEmpty() && !(image_path == null)) {
            ImageLoaderUtility.getInstance().initImageLoader();
            ImageLoader.getInstance().displayImage(image_path, image_profile, ImageLoaderUtility.getInstance().getProfileImageOptions());
        }
    }


    /**
     *
     * 카카오계정으로 가입 : 이름, 핸드폰번호
     * 일반계정으로 가입 : 이름, 이메일, 비밀번호, 비밀번호 확인
     **/
    private boolean checkValidate(String name, String email, String pw, String checkPw, String phnum){
        if (!isKakaoRegister) {
            if (DataValidation.isEmptyString(email) || !DataValidation.isValidValue(email, DataValidation.Type.EMAIL)) {
                showCancelErrorMessage(INPUT_TYPE.EMAIL, true);
                return false;
            }
            if (DataValidation.isEmptyString(pw)) {
                showCancelErrorMessage(INPUT_TYPE.PW, true);
                return false;
            }
            if (DataValidation.isEmptyString(checkPw) || !checkPw.equals(pw)) {
                showCancelErrorMessage(INPUT_TYPE.CHECK_PW, true);
                return false;
            }
        }

        if (DataValidation.isEmptyString(name) || !DataValidation.isValidLength(name, 2, 10)) {
            showCancelErrorMessage(INPUT_TYPE.NAME, true);
            return false;
        }
        if (DataValidation.isEmptyString(phnum) || !DataValidation.isValidValue(phnum, DataValidation.Type.PHONE_NUMBER)) {
            showCancelErrorMessage(INPUT_TYPE.PH_NUM, true);
            return false;
        }
        return true;
    }

    private void setPhoneNumber() {
        String number = AppUtility.getInstance().getMyPhoneNumber();
        input_phnum.setText(number);
        Selection.setSelection(input_phnum.getText(), number.length());
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case RequestPermission.Code.READ_PHONE_STATE:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    setPhoneNumber();
                }
                break;
            case RequestPermission.Code.WRITE_EXTERNAL_STORAGE:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    openPhotoPicker();
                } else {
                    Toast.makeText(getApplicationContext(), getString(R.string.text_deny_select_photo), Toast.LENGTH_SHORT).show();
                }
                break;
        }
    }

    private void openPhotoPicker() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, getString(R.string.text_pick_image)), AppUtility.BaseDataType.PICK_IMAGE_REQUEST);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == AppUtility.BaseDataType.PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
            //로더가init되지 않았을 수 있으므로 init

            ImageLoaderUtility.getInstance().initImageLoader();
            ImageLoader.getInstance().displayImage(data.getData().toString(), image_profile, ImageLoaderUtility.getInstance().getProfileImageOptions());
        }
    }

    private void setBackgroundImage(int height) {
        Bitmap bitmap = AppUtility.getInstance().decodeSampledBitmapFromResource(getResources(), R.drawable.bg_register, AppUtility.getInstance().getScreenWidth(), height);
        if (Build.VERSION.SDK_INT >= 16) {
            root_view.setBackground(new BitmapDrawable(getResources(), bitmap));
        } else {
            root_view.setBackgroundDrawable(new BitmapDrawable(getResources(), bitmap));
        }
    }

    @Override
    protected String getToolbarTitle() {
        return getString(R.string.title_register);
    }

    @Override
    protected boolean getResult() {
        return false;
    }

    @Override
    protected Activity getActivity() {
        return RegisterActivity.this;
    }
}
