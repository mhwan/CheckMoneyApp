package com.app.checkmoney.CustomUi;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import com.app.checkmoney.Activity.EditRoomActivity;
import com.app.checkmoney.Activity.RoomActivity;
import com.app.checkmoney.Items.UserItem;
import com.app.checkmoney.Util.AppUtility;
import com.moneycheck.checkmoneyapp.R;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link RoomInfoFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class RoomInfoFragment extends Fragment implements View.OnClickListener{
    private View view;
    private UserItem receiverInfo;
    private RoomActivity parent_activity;
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
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view =  inflater.inflate(R.layout.layout_room_info, container, false);
        parent_activity = (RoomActivity) getActivity();
        makeSample();
        initView();
        return view;
    }

    private void makeSample(){
        receiverInfo = new UserItem("김민수", "01012345678", UserItem.ManageType.MANAGER, UserItem.ExchangeType.RECEIVER);
    }
    private void initView(){
        TextView receiver_name = (TextView) view.findViewById(R.id.receiver_name);
        TextView receiver_phone = (TextView) view.findViewById(R.id.receiver_phone);
        CircleImageView receiver_profile = (CircleImageView) view.findViewById(R.id.image_profile);
        TextView room_title = (TextView) view.findViewById(R.id.room_title);
        TextView expire_date = (TextView) view.findViewById(R.id.text_date);
        TextView money = (TextView) view.findViewById(R.id.text_money);
        TextView button_change = (TextView) view.findViewById(R.id.button_change);
        TextView button_setting_message = (TextView) view.findViewById(R.id.button_messgage_alarm);
        ImageButton button_setting_room = (ImageButton) view.findViewById(R.id.button_room_setting);

        receiver_name.setText(receiverInfo.getName());
        receiver_phone.setText(receiverInfo.getPhoneNumber());
        button_change.setOnClickListener(this);
        button_setting_message.setOnClickListener(this);
        button_setting_room.setOnClickListener(this);
        view.findViewById(R.id.bg_receiver).setOnClickListener(this);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.button_change :
                break;
            case R.id.button_messgage_alarm :
                break;
            case R.id.button_room_setting :
                Intent intent = new Intent(parent_activity, EditRoomActivity.class);
                intent.putExtra(AppUtility.BaseDataType.NAME_ROOM, "자바를 자바");
                intent.putExtra(AppUtility.BaseDataType.MONEY_ROOM, 6000);
                intent.putExtra(AppUtility.BaseDataType.DATE_ROOM, "2016/09/21");

                startActivityForResult(intent, 0x445);
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
}
