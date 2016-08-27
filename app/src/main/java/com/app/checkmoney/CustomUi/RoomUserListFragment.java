package com.app.checkmoney.CustomUi;


import android.animation.Animator;
import android.animation.ArgbEvaluator;
import android.animation.ValueAnimator;
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
import com.app.checkmoney.Util.DevelopeLog;
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
    private ArrayList<View> bg_list;
    private ArrayList<UserItem> user_list;

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
        user_list = createSampleUser();

        addUserListData(user_list, view.findViewById(R.id.userlist_frame));
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
            bg_list = new ArrayList<>();
            for (UserItem item : userItemArrayList){
                View listview = LayoutInflater.from(parent_activity).inflate(R.layout.ui_item_user_list, (ViewGroup) rootview, false);
                View background = listview.findViewById(R.id.user_info);
                ((TextView)listview.findViewById(R.id.user_name)).setText(item.getName());
                ((TextView)listview.findViewById(R.id.user_phone)).setText(item.getPhoneNumber());
                CircleImageView profile_image = (CircleImageView) listview.findViewById(R.id.image_profile);
                View bg_user_list = listview.findViewById(R.id.bg_user_list);

                if (item.ispay())
                    bg_user_list.setBackgroundColor(ContextCompat.getColor(parent_activity, R.color.colorDarkbg));
                if (item.isNewNotify()) {
                    listview.findViewById(R.id.button_layout).setVisibility(View.VISIBLE);
                    View button_okay = listview.findViewById(R.id.button_okay);
                    View button_deny = listview.findViewById(R.id.button_deny);
                    button_okay.setOnClickListener(this);
                    button_deny.setOnClickListener(this);
                    button_okay.setTag(index);
                    button_deny.setTag(index);
                } else
                    listview.findViewById(R.id.button_layout).setVisibility(View.GONE);

                listview.findViewById(R.id.list_divier).setVisibility((index == last_index)? View.GONE : View.VISIBLE);
                background.setOnClickListener(this);
                //뷰에 해당하는 아이템을 태그로 저장 -> 포지션값 저장
                background.setTag(index);
                bg_list.add(index, listview);
                ((ViewGroup) rootview).addView(listview);
                index++;
            }
        }
    }

    /**
     * 입금 처리했을때 애니메이션
     *
     * 배경색은 회색처리, 버튼은 알파값과 x값 이동
     * @param position 리스트 포지션 값
     */
    private void changeStateAnimation(int position){
        if (position < 0 || position >= user_list.size())
            return;
        View listview = bg_list.get(position);
        final View bg = listview.findViewById(R.id.bg_user_list);
        final View button_layout = listview.findViewById(R.id.button_layout);
        ValueAnimator anim = new ValueAnimator();
        anim.setIntValues(ContextCompat.getColor(parent_activity, R.color.colorWhite), ContextCompat.getColor(parent_activity, R.color.colorDarkbg));
        anim.setEvaluator(new ArgbEvaluator());
        anim.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                bg.setBackgroundColor((Integer)valueAnimator.getAnimatedValue());
            }
        });
        anim.setDuration(280);
        anim.start();

        button_layout.animate()
                .alpha(0.0f)
                .translationX(button_layout.getWidth())
                .setDuration(260)
                .setListener(new Animator.AnimatorListener() {
                    @Override
                    public void onAnimationStart(Animator animation) {

                    }

                    @Override
                    public void onAnimationEnd(Animator animation) {
                        button_layout.setVisibility(View.GONE);
                    }

                    @Override
                    public void onAnimationCancel(Animator animation) {

                    }

                    @Override
                    public void onAnimationRepeat(Animator animation) {

                    }
                });
    }


    @Override
    public void onClick(View v) {
        Object tag = v.getTag();
        int position = -1;
        if (tag instanceof Integer)
            position = ((Integer) tag).intValue();

        DevelopeLog.d("position!! " + position);
        switch (v.getId()){
            case R.id.button_okay :
                changeStateAnimation(position);
                user_list.get(position).setIspay(true);
                Toast.makeText(parent_activity, user_list.get(position).getName()+"님 입금확인 되었습니다.", Toast.LENGTH_SHORT).show();
                break;
            case R.id.button_deny :

                break;
            case R.id.button_manage_user :

                break;
            case R.id.user_info :
                UserItem item = user_list.get(position);
                final CustomProfileDialog dialog = new CustomProfileDialog(parent_activity);
                dialog.setName(item.getName());
                dialog.setPhonenumber(item.getPhoneNumber());
                dialog.setExchangeType(item.getEtype());
                dialog.setPay(item.ispay());
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
