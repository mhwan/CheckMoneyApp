package com.app.checkmoney.Activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.app.checkmoney.CustomBase.BaseActivity;
import com.app.checkmoney.CustomBase.DoublePressedKill;
import com.app.checkmoney.CustomBase.RecyclerItemClickListener;
import com.app.checkmoney.CustomUi.FloatingActionButton;
import com.app.checkmoney.CustomUi.RoomListAdapter;
import com.app.checkmoney.Items.RoomListItem;
import com.moneycheck.checkmoneyapp.R;

import java.util.ArrayList;

public class MainActivity extends BaseActivity {
    private DoublePressedKill doublePressed;
    private RoomListAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setFromBaseView(R.layout.activity_main);
        setToolbar(R.layout.layout_main_toolbar, R.id.main_toolbar_setting, ToolbarType.MAIN_TYPE);
        doublePressed = new DoublePressedKill(MainActivity.this);
        initView();
    }

    private void initView(){
        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.recycler_room);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new RoomListAdapter(this);
        recyclerView.setAdapter(adapter);
        recyclerView.addOnItemTouchListener(new RecyclerItemClickListener(this, recyclerView, new RecyclerItemClickListener.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                Intent intent = new Intent(MainActivity.this, RoomActivity.class);
                startActivityWithAnim(intent);
            }

            @Override
            public void onItemLongClick(View view, int position) {

            }
        }));
        FloatingActionButton floatingActionButton = (FloatingActionButton) findViewById(R.id.button_create);
        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, CreateRoomActivity.class);
                startActivityWithAnim(intent);
            }
        });


        refreshView();
    }

    private void refreshView(){
        adapter.refreshAdapter(createSampleRoom());
    }

    //임시로 방 데이터 생성 (테스트
    private ArrayList<RoomListItem> createSampleRoom(){
        ArrayList<RoomListItem> list = new ArrayList<>();

        for (int i =0; i<4; i++){
            RoomListItem item = new RoomListItem("자바를 잡자", "2016/07/04 12:02.13", 3000);
            if (i == 1)
                item.setCount_new_alarm(3);

            if (i == 2)
                item.setState_type(RoomListItem.RoomState.ALL_RECEIVE);
            else if (i==0)
                item.setState_type(RoomListItem.RoomState.NOT_RECEIVE);

            list.add(item);
        }

        return list;
    }

    @Override
    public void onBackPressed() {
        if (doublePressed != null)
            doublePressed.onBackPressed();
        else
            super.onBackPressed();
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
        return MainActivity.this;
    }
}
