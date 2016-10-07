package com.app.checkmoney.Activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.app.checkmoney.CustomBase.BaseActivity;
import com.app.checkmoney.CustomUi.CustomBottomButton;
import com.app.checkmoney.CustomUi.DividerItemDecoration;
import com.app.checkmoney.CustomUi.LeaveUserAdapter;
import com.app.checkmoney.Items.RoomUserItem;
import com.app.checkmoney.Util.AppUtility;
import com.moneycheck.checkmoneyapp.R;

import java.util.ArrayList;

public class UserManageActivity extends BaseActivity {
    private RecyclerView recyclerView;
    private boolean isEdited = false;
    private LeaveUserAdapter adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setFromBaseView(R.layout.activity_user_manage);
        setToolbar(R.layout.layout_base_toolbar, R.id.toolbar_close, ToolbarType.SUB_TYPE);
        initView();
    }

    private void initView(){
        findViewById(R.id.button_user_invite).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), UserInviteActivity.class);
                intent.putExtra(AppUtility.BaseDataType.ALREADY_ADDED_LIST, getAlreadyAddedUserList());
                startActivityForResultWithAnim(intent, AppUtility.BaseDataType.USERINVITE_REQUEST);
            }
        });
        CustomBottomButton bottomButton = (CustomBottomButton) findViewById(R.id.button_okay);
        bottomButton.getButtonBackground().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setResultForFinish();
            }
        });

        recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.addItemDecoration(new DividerItemDecoration(getContext(), DividerItemDecoration.VERTICAL_LIST, R.drawable.line_divider, 12));
        adapter = new LeaveUserAdapter(getContext());
        recyclerView.setAdapter(adapter);


        refreshView();
    }

    private ArrayList<String> getAlreadyAddedUserList(){
        ArrayList<String> list = new ArrayList<>();
        list.add("01027315790");
        list.add("01089597928");
        list.add("0312697707");
        list.add("01040099525");
        return list;
    }
    private ArrayList<RoomUserItem> getUserList(){
        Bundle bundle = getIntent().getExtras();
        ArrayList<RoomUserItem> list = new ArrayList<>();
        if (bundle!=null)
            list = (ArrayList<RoomUserItem>) bundle.getSerializable(AppUtility.BaseDataType.MANAGE_USER_LIST);

        return list;
    }

    //뷰를 새로고침해준다
    private void refreshView(){
        adapter.refreshAdapter(getUserList());
    }
    @Override
    protected void setResultForFinish() {
        if (isEdited) {
            setResult(RESULT_OK);
            finishActivity();
        } else
            super.setResultForFinish();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == AppUtility.BaseDataType.USERINVITE_REQUEST && resultCode == RESULT_OK){
            isEdited = true;
            adapter.addSelectedItem((ArrayList<RoomUserItem>) data.getSerializableExtra(AppUtility.BaseDataType.SELECTED_CONTACT_LIST));
        }
    }

    @Override
    protected String getToolbarTitle() {
        return getString(R.string.text_user_title_manage);
    }

    @Override
    protected boolean getResult() {
        return false;
    }

    @Override
    protected Activity getActivity() {
        return UserManageActivity.this;
    }
}
