package com.app.checkmoney.Activity;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.text.Selection;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.app.checkmoney.CustomBase.BaseActivity;
import com.app.checkmoney.CustomBase.RequestPermission;
import com.app.checkmoney.CustomUi.CustomProgressDialog;
import com.app.checkmoney.Util.AppUtility;
import com.app.checkmoney.Util.ImageLoaderUtility;
import com.app.checkmoney.Util.NetworkUtility;
import com.moneycheck.checkmoneyapp.R;
import com.nostra13.universalimageloader.core.ImageLoader;

import org.json.JSONException;

import java.io.IOException;

import de.hdodenhof.circleimageview.CircleImageView;

public class RegisterActivity extends BaseActivity {
    private View root_view;
    private TextView button_register;
    private EditText input_name, input_phnum, input_email, input_pw, input_repw;
    private CircleImageView image_profile;
    private static final int REQUEST_PICK_IMAGE = 0x13;
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

        button_register = (TextView) findViewById(R.id.button_register);
        input_email = (EditText) findViewById(R.id.input_email);
        input_pw = (EditText) findViewById(R.id.input_pw);
        input_repw = (EditText) findViewById(R.id.input_checkpw);
        input_name = (EditText) findViewById(R.id.input_name);
        input_phnum = (EditText) findViewById(R.id.input_phnum);
        input_phnum.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus == true) {
                    if (new RequestPermission(RegisterActivity.this, 1).isGranted())
                        setPhoneNumber();
                }
            }
        });
        image_profile = (CircleImageView) findViewById(R.id.image_profile);
        findViewById(R.id.button_edit_profile).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (new RequestPermission(RegisterActivity.this, 0).isGranted()) {
                    openPhotoPicker();
                }
            }
        });

        button_register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = input_email.getText().toString();
                String name = input_name.getText().toString();
                String phnum = input_phnum.getText().toString().replace("-", "");
                String pw = input_pw.getText().toString();
                String checkPw = input_repw.getText().toString();
                if (checkValidate(email, pw, checkPw, phnum) && button_register.isEnabled()) {
                    if (AppUtility.getInstance().isNetworkConnection()) {
                        registerWork(email, name, pw, phnum);
                    }
                    else
                        Toast.makeText(getContext(), getString(R.string.text_unconnected_internet), Toast.LENGTH_SHORT).show();
                }

            }
        });
    }

    private void registerWork(String email, String name, String pw, String phnum){
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
                        startActivity(intent);
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
        }.execute(email, name, pw, phnum);
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
        findViewById(R.id.inputlayout_email).setVisibility(View.GONE);
        findViewById(R.id.inputlayout_pw).setVisibility(View.GONE);
        findViewById(R.id.inputlayout_checkpw).setVisibility(View.GONE);

        input_name.setText(bundle.getString(AppUtility.BaseDataType.KEY_KAKAO_NAME));
        String image_path = bundle.getString(AppUtility.BaseDataType.KEY_KAKAO_IMAGE_PATH);
        if (!image_path.isEmpty() && !(image_path == null)) {
            ImageLoaderUtility.getInstance().initImageLoader();
            ImageLoader.getInstance().displayImage(image_path, image_profile, ImageLoaderUtility.getInstance().getProfileImageOptions());
        }
    }


    //유효성 검사 (임시로 항목이 비었는지만 체크
    private boolean checkValidate(String email, String pw, String checkPw, String phnum){
        if (email.equals("") || email.isEmpty())
            return false;
        if (phnum.equals("") || phnum.isEmpty())
            return false;

        if (!isKakaoRegister) {
            if (pw.equals("") || pw.isEmpty())
                return false;
            if (checkPw.equals("") || checkPw.isEmpty() || !checkPw.equals(pw))
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
        startActivityForResult(Intent.createChooser(intent, getString(R.string.text_pick_image)), REQUEST_PICK_IMAGE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_PICK_IMAGE && resultCode == RESULT_OK && data != null && data.getData() != null) {
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
