package com.app.checkmoney.Activity;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.app.checkmoney.CustomBase.BaseActivity;
import com.app.checkmoney.CustomBase.RequestPermission;
import com.app.checkmoney.Util.AppUtility;
import com.app.checkmoney.Util.ImageLoaderUtility;
import com.moneycheck.checkmoneyapp.R;
import com.nostra13.universalimageloader.core.ImageLoader;

import de.hdodenhof.circleimageview.CircleImageView;

public class RegisterActivity extends BaseActivity {
    private View root_view;
    private TextView button_register;
    private EditText input_name, input_phnum;
    private CircleImageView image_profile;
    private static final int REQUEST_PICK_IMAGE = 0x13;
    public static final String KEY_PHONE_NUMBER = "key_phnum";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setFromBaseView(R.layout.activity_register);
        setToolbar(R.layout.layout_register_toolbar, R.id.toolbar_close);
        initView(getIntent().getStringExtra(KEY_PHONE_NUMBER));
    }

    private void initView(String phnum) {
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
    }

    private void setPhoneNumber() {
        input_phnum.setText(AppUtility.getInstance().getMyPhoneNumber());
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
                    Toast.makeText(getApplicationContext(), "권한이 거부되어 사진을 선택할 수 없습니다.", Toast.LENGTH_SHORT).show();
                }
                break;
        }
    }

    private void openPhotoPicker() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "사진 선택"), REQUEST_PICK_IMAGE);
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
        return "회원가입";
    }
}
