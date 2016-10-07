package com.app.checkmoney.Activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.app.checkmoney.CustomBase.BaseActivity;
import com.app.checkmoney.CustomUi.CustomBottomButton;
import com.app.checkmoney.CustomUi.RoomInfoFragment;
import com.app.checkmoney.CustomUi.RoomUserListFragment;
import com.app.checkmoney.Items.RoomUserItem;
import com.app.checkmoney.Util.AppUtility;
import com.moneycheck.checkmoneyapp.R;

import java.util.ArrayList;

public class RoomActivity extends BaseActivity {
    private LinearLayout room_container;
    private CustomBottomButton bottomButton;
    private RoomActivityResultListener roomlistener;
    private UserActivityResultListener userlistener;
    private ArrayList<RoomUserItem> giver_list;
    private RoomUserItem receiverItem;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setFromBaseView(R.layout.activity_room);
        setToolbar(R.layout.layout_main_toolbar, R.id.main_toolbar_setting, ToolbarType.MAIN_TYPE);
        initView();
    }

    private void initView() {
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

    public void setRoomResultlistener (RoomActivityResultListener listener){
        this.roomlistener = listener;
    }
    public void setUserResultlistener(UserActivityResultListener listener){
        this.userlistener = listener;
    }

    //각각의 프래그먼트와의 연결을 위한 리스너
    public interface RoomActivityResultListener {
        void onActivityResults(int requestcode, int resultcode, Intent data);
    }

    public interface UserActivityResultListener {
        void onActivityResults(int requestcode, int resultcode, Intent data);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == AppUtility.BaseDataType.ROOMMANAGE_REQUEST){
            if (resultCode == AppUtility.BaseDataType.ROOM_MANAGE_RESULT)
                roomlistener.onActivityResults(requestCode, resultCode, data);
            else if (resultCode == AppUtility.BaseDataType.ROOM_USER_MANAGE_RESULT) {
                reloadGiverList();
                roomlistener.onActivityResults(requestCode, resultCode, data);
                userlistener.onActivityResults(requestCode, resultCode, data);
            }
        } else if (requestCode == AppUtility.BaseDataType.USERMANAGE_REQUEST && resultCode == RESULT_OK){
            reloadGiverList();
            userlistener.onActivityResults(requestCode, resultCode, data);
        }
    }

    public RoomUserItem getReceiver(){
        if (receiverItem == null)
            receiverItem = createSampleReceiver();

        return receiverItem;
    }

    public RoomUserItem createSampleReceiver(){
        return new RoomUserItem("김민수", "01012345678", RoomUserItem.ManageType.MANAGER, RoomUserItem.ExchangeType.RECEIVER);
    }

    public ArrayList<RoomUserItem> getGiverList(){
        if (giver_list == null)
            giver_list = createSampleUser();

        return giver_list;
    }

    public void reloadGiverList(){
        giver_list.clear();
        giver_list.addAll(createSampleUser());
    }

    private ArrayList<RoomUserItem> createSampleUser(){
        ArrayList<RoomUserItem> list = new ArrayList<>();
        for (int i=0; i<16; i++){
            RoomUserItem roomUserItem = new RoomUserItem("배명환", "01050574876", RoomUserItem.ManageType.NO_MANAGER, RoomUserItem.ExchangeType.GIVER);
            if (i == 3 || i == 5 || i==8 || i==12)
                roomUserItem.setNewNotify(true);
            if (i== 7 ||i==10 || i==14)
                roomUserItem.setIspay(true);

            list.add(roomUserItem);
        }

        return list;
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
