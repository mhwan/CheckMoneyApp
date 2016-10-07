package com.app.checkmoney.CustomUi;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.app.checkmoney.Activity.CreateRoomActivity;
import com.app.checkmoney.Activity.RoomActivity;
import com.app.checkmoney.Items.RoomListItem;
import com.app.checkmoney.Items.RoomUserItem;
import com.app.checkmoney.Util.AppUtility;
import com.moneycheck.checkmoneyapp.R;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link RoomInfoFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class RoomInfoFragment extends Fragment implements View.OnClickListener, RoomActivity.RoomActivityResultListener{
    private View view;
    private RoomUserItem receiverInfo;
    private RoomListItem roomItem;
    private RoomActivity parent_activity;
    private RelativeLayout receiver_layout;
    private TextView receiver_name, receiver_phone, no_receiver_data, room_title, expire_date, money;
    private CircleImageView receiver_profile;
    public RoomInfoFragment() {
        // Required empty public constructor
    }

    // TODO: Rename and change types and number of parameters
    public static RoomInfoFragment newInstance() {
        RoomInfoFragment fragment = new RoomInfoFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        parent_activity = (RoomActivity) getActivity();
        parent_activity.setRoomResultlistener(this);
        receiverInfo = parent_activity.getReceiver();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view =  inflater.inflate(R.layout.layout_room_info, container, false);
        initView();
        return view;
    }

    private void initView(){
        receiver_layout = (RelativeLayout) view.findViewById(R.id.bg_receiver);
        receiver_name = (TextView) view.findViewById(R.id.receiver_name);
        receiver_phone = (TextView) view.findViewById(R.id.receiver_phone);
        no_receiver_data = (TextView) view.findViewById(R.id.text_no_receiver);
        receiver_profile = (CircleImageView) view.findViewById(R.id.image_profile);
        room_title = (TextView) view.findViewById(R.id.room_title);
        expire_date = (TextView) view.findViewById(R.id.text_date);
        money = (TextView) view.findViewById(R.id.text_money);
        TextView button_change = (TextView) view.findViewById(R.id.button_change);
        TextView button_setting_message = (TextView) view.findViewById(R.id.button_messgage_alarm);
        ImageButton button_setting_room = (ImageButton) view.findViewById(R.id.button_room_setting);

        button_change.setOnClickListener(this);
        button_setting_message.setOnClickListener(this);
        button_setting_room.setOnClickListener(this);
        view.findViewById(R.id.bg_receiver).setOnClickListener(this);
    }

    public void setContent(){
        if (receiverInfo == null) {
            no_receiver_data.setVisibility(View.VISIBLE);
            receiver_layout.setVisibility(View.GONE);
        } else {
            no_receiver_data.setVisibility(View.GONE);
            receiver_layout.setVisibility(View.VISIBLE);
            receiver_name.setText(receiverInfo.getName());
            receiver_phone.setText(receiverInfo.getPhoneNumber());

            //ImageLoaderUtility.getInstance().initImageLoader();
            //ImageLoader.getInstance().displayImage("", receiver_profile, ImageLoaderUtility.getInstance().getProfileImageOptions());
        }
        room_title.setText(roomItem.getRoom_name());
        expire_date.setText(roomItem.getExpire_date());
        money.setText(String.valueOf(roomItem.getPrice_money()));
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.button_change :
                break;
            case R.id.button_messgage_alarm :
                break;
            case R.id.button_room_setting :
                Intent intent = new Intent(parent_activity, CreateRoomActivity.class);
                intent.putExtra(AppUtility.BaseDataType.NAME_ROOM, "자바를 자바");
                intent.putExtra(AppUtility.BaseDataType.MONEY_ROOM, 6000);
                intent.putExtra(AppUtility.BaseDataType.DATE_ROOM, "2016/09/21");

                parent_activity.startActivityForResult(intent, AppUtility.BaseDataType.ROOMMANAGE_REQUEST);
                parent_activity.overridePendingTransition(R.anim.anim_slide_in, R.anim.anim_keep);
                break;
            case R.id.bg_receiver :
                final CustomProfileDialog dialog = new CustomProfileDialog(parent_activity);
                dialog.setName(receiverInfo.getName());
                dialog.setPhonenumber(receiverInfo.getPhoneNumber());
                dialog.setExchangeType(receiverInfo.getEtype());
                dialog.setButtonlistener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        //전화 걸기
                        dialog.dismiss();
                    }
                }, new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        //메시지 전송
                        dialog.dismiss();
                    }
                });
                dialog.show();
                break;
        }

    }

    @Override
    public void onActivityResults(int requestcode, int resultcode, Intent data) {

    }
}
