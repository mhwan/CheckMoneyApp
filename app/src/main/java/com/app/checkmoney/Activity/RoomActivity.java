package com.app.checkmoney.Activity;

import android.app.Activity;
import android.os.Bundle;

import com.app.checkmoney.CustomBase.BaseActivity;
import com.moneycheck.checkmoneyapp.R;

public class RoomActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setFromBaseView(R.layout.activity_room);
        setToolbar(R.layout.layout_main_toolbar, R.id.main_toolbar_setting, ToolbarType.MAIN_TYPE);
    }

    @Override
    protected String getToolbarTitle() {
        return "";
    }

    @Override
    protected boolean getResult() {
        return false;
    }

    @Override
    protected Activity getActivity() {
        return RoomActivity.this;
    }
}
