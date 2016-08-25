package com.app.checkmoney.Activity;

import android.app.Activity;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.app.checkmoney.CustomBase.BaseActivity;
import com.app.checkmoney.CustomUi.CustomBottomButton;
import com.app.checkmoney.CustomUi.RoomInfoFragment;
import com.app.checkmoney.CustomUi.RoomUserListFragment;
import com.moneycheck.checkmoneyapp.R;

public class RoomActivity extends BaseActivity {
    private LinearLayout room_container;
    private CustomBottomButton bottomButton;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setFromBaseView(R.layout.activity_room);
        setToolbar(R.layout.layout_main_toolbar, R.id.main_toolbar_setting, ToolbarType.MAIN_TYPE);
        initView();
    }

    private void initView(){
        room_container = (LinearLayout) findViewById(R.id.room_frame);
        bottomButton = (CustomBottomButton) findViewById(R.id.button_message_send);
        bottomButton.getButtonBackground().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(RoomActivity.this, "버튼 버튼 클릭되엇", Toast.LENGTH_SHORT).show();
            }
        });
        addFragment();
    }

    private void addFragment() {
        LinearLayout linearLayout = new LinearLayout(this);
        linearLayout.setOrientation(LinearLayout.VERTICAL);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
            linearLayout.setId(View.generateViewId());
        } else {
            linearLayout.setId(R.id.linear_container);
        }

        getSupportFragmentManager().beginTransaction().add(linearLayout.getId(), RoomInfoFragment.newInstance()).commit();
        getSupportFragmentManager().beginTransaction().add(linearLayout.getId(), RoomUserListFragment.newInstance()).commit();

        room_container.addView(linearLayout);
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
