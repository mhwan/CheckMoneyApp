package com.app.checkmoney.CustomBase;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.app.checkmoney.Activity.UserSettingActivity;
import com.app.checkmoney.CustomUi.CustomAlertDialog;
import com.app.checkmoney.Util.DevelopeLog;
import com.moneycheck.checkmoneyapp.R;

public abstract class BaseActivity extends AppCompatActivity {
    private Toolbar toolbar;
    private int navigationIcon, toolbarId;
    private LinearLayout root_layout;
    private View new_layout;
    private ToolbarType type;
    protected View toolbarview;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_base);
        //AppUtility.getInstance().addActivity(getActivity());
        root_layout = (LinearLayout) findViewById(R.id.rootview);
        toolbar = (Toolbar) findViewById(R.id.base_toolbar);
        DevelopeLog.d("base oncreate");
    }

    @Override
    protected void onStart() {
        super.onStart();
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setDisplayShowCustomEnabled(true);
        toolbarview = LayoutInflater.from(this).inflate(toolbarId, null);

        if (type.equals(ToolbarType.SUB_TYPE)) {
            TextView toolbar_title = (TextView) toolbarview.findViewById(R.id.toolbar_title);
            toolbar_title.setText(getToolbarTitle());
            toolbarview.findViewById(navigationIcon).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    setResultForFinish();
                }
            });
        } else if (type.equals(ToolbarType.MAIN_TYPE)) {
            toolbarview.findViewById(navigationIcon).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //설정 열기
                    Intent intent = new Intent(getActivity(), UserSettingActivity.class);
                    startActivity(intent);
                }
            });
        } else if (type.equals(ToolbarType.BACK_TYPE)) {
            TextView toolbar_title = (TextView) toolbarview.findViewById(R.id.toolbar_title);
            toolbar_title.setText(getToolbarTitle());
            toolbarview.findViewById(navigationIcon).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    finishActivity();
                }
            });
            toolbarview.findViewById(R.id.toolbar_okay).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    setResultForFinish();
                }
            });
        }

        Toolbar.LayoutParams layoutParams = new Toolbar.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        getSupportActionBar().setCustomView(toolbarview, layoutParams);

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finishActivity();
    }


    protected void startActivityWithAnim(Intent intent){
        startActivity(intent);
        overridePendingTransition(R.anim.anim_slide_in, R.anim.anim_keep);
    }

    protected void startActivityForResultWithAnim(Intent intent, int requestCode){
        startActivityForResult(intent, requestCode);
        overridePendingTransition(R.anim.anim_slide_in, R.anim.anim_keep);
    }
    protected void finishActivity(){
        finish();
        overridePendingTransition(0, R.anim.anim_slide_out);
    }
    protected void setToolbar(int toolbarId, int navigationIconId, ToolbarType type) {
        this.toolbarId = toolbarId;
        this.navigationIcon = navigationIconId;
        this.type = type;
    }

    protected void setFromBaseView(int viewId) {
        new_layout = LayoutInflater.from(this).inflate(viewId, root_layout, false);
        root_layout.addView(new_layout);
    }

    public enum ToolbarType{ MAIN_TYPE, SUB_TYPE, BACK_TYPE }
    protected abstract String getToolbarTitle();
    protected abstract boolean getResult();
    protected abstract Activity getActivity();

    protected void setResultForFinish(){
        if (getResult())
            setResult(RESULT_OK);
        finishActivity();
    }

    protected void showUnknwonErrorDialog(){
        final CustomAlertDialog dialog = new CustomAlertDialog(getContext());
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
    public Context getContext(){
        return getActivity();
    }
}
