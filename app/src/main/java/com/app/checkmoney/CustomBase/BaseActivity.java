package com.app.checkmoney.CustomBase;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.app.checkmoney.Util.AppUtility;
import com.app.checkmoney.Util.DevelopeLog;
import com.moneycheck.checkmoneyapp.R;

public abstract class BaseActivity extends AppCompatActivity {
    private Toolbar toolbar;
    private int navigationIcon, toolbarId;
    private LinearLayout root_layout;
    private View new_layout;
    private ToolbarType type;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_base);
        AppUtility.getInstance().addActivity(this);
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
        View toolberview = LayoutInflater.from(this).inflate(toolbarId, null);

        if (type.equals(ToolbarType.SUB_TYPE)) {
            TextView toolbar_title = (TextView) toolberview.findViewById(R.id.toolbar_title);
            toolbar_title.setText(getToolbarTitle());
            toolberview.findViewById(navigationIcon).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    finish();
                }
            });
        } else if (type.equals(ToolbarType.MAIN_TYPE)) {
            toolberview.findViewById(navigationIcon).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //설정 열기
                }
            });
        }

        Toolbar.LayoutParams layoutParams = new Toolbar.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        getSupportActionBar().setCustomView(toolberview, layoutParams);

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

    public enum ToolbarType{ MAIN_TYPE, SUB_TYPE }
    protected abstract String getToolbarTitle();
}
