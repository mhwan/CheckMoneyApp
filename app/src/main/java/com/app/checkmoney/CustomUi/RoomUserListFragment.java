package com.app.checkmoney.CustomUi;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.app.checkmoney.Activity.RoomActivity;
import com.app.checkmoney.Items.UserItem;
import com.moneycheck.checkmoneyapp.R;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link RoomUserListFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class RoomUserListFragment extends Fragment implements View.OnClickListener{
    private RoomActivity parent_activity;
    private View view;
    public RoomUserListFragment() {
        // Required empty public constructor
    }

    // TODO: Rename and change types and number of parameters
    public static RoomUserListFragment newInstance() {
        RoomUserListFragment fragment = new RoomUserListFragment();
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
        // Inflate the layout for this fragment
        view =  inflater.inflate(R.layout.layout_room_user_list, container, false);
        parent_activity = (RoomActivity) getActivity();
        initView();
        return view;
    }

    private void initView(){
        TextView button_manageuser = (TextView) view.findViewById(R.id.button_manage_user);
        String button_text = button_manageuser.getText().toString();
        button_manageuser.setText(Html.fromHtml("<u>" + button_text + "</u>"));
        button_manageuser.setOnClickListener(this);

        addUserListData(createSampleUser(), view.findViewById(R.id.userlist_frame));
    }

    private ArrayList<UserItem> createSampleUser(){
        ArrayList<UserItem> list = new ArrayList<>();
        for (int i=0; i<16; i++){
            UserItem userItem = new UserItem("배명환", "01050574876", UserItem.ManageType.NO_MANAGER, UserItem.ExchangeType.GIVER);
            if (i == 3 || i == 5 || i==8 || i==12)
                userItem.setNewNotify(true);
            if (i== 7 ||i==10 || i==14)
                userItem.setIspay(true);

            list.add(userItem);
        }

        return list;
    }
    private void addUserListData(ArrayList<UserItem> userItemArrayList, View rootview){
        if (rootview instanceof LinearLayout){
            int last_index = userItemArrayList.size()-1;
            int index = 0;
            for (UserItem item : userItemArrayList){
                View listview = LayoutInflater.from(parent_activity).inflate(R.layout.ui_item_user_list, (ViewGroup) rootview, false);
                View background = listview.findViewById(R.id.user_info);
                ((TextView)listview.findViewById(R.id.user_name)).setText(item.getName());
                ((TextView)listview.findViewById(R.id.user_phone)).setText(item.getPhoneNumber());
                CircleImageView profile_image = (CircleImageView) listview.findViewById(R.id.image_profile);

                if (item.ispay())
                    listview.findViewById(R.id.bg_user_list).setBackgroundColor(ContextCompat.getColor(parent_activity, R.color.colorDarkbg));
                if (item.isNewNotify()) {
                    listview.findViewById(R.id.button_layout).setVisibility(View.VISIBLE);
                    listview.findViewById(R.id.button_okay).setOnClickListener(this);
                    listview.findViewById(R.id.button_deny).setOnClickListener(this);
                } else
                    listview.findViewById(R.id.button_layout).setVisibility(View.GONE);

                listview.findViewById(R.id.list_divier).setVisibility((index == last_index)? View.GONE : View.VISIBLE);
                background.setOnClickListener(this);
                //뷰에 해당하는 아이템을 태그로 저장
                background.setTag(item);

                ((ViewGroup) rootview).addView(listview);
                index++;
            }
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.button_okay :

                break;
            case R.id.button_deny :

                break;
            case R.id.button_manage_user :

                break;
            case R.id.user_info :
                Object tag = v.getTag();
                if (tag instanceof UserItem){
                    UserItem userItem = (UserItem) tag;

                    Toast.makeText(parent_activity, userItem.getName(), Toast.LENGTH_SHORT).show();
                }
                break;

        }
    }
}
