package com.app.checkmoney.CustomUi;


import android.animation.Animator;
import android.animation.ArgbEvaluator;
import android.animation.ValueAnimator;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.telephony.SmsManager;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.app.checkmoney.Activity.RoomActivity;
import com.app.checkmoney.Activity.UserManageActivity;
import com.app.checkmoney.CustomBase.RequestPermission;
import com.app.checkmoney.Items.RoomUserItem;
import com.app.checkmoney.Util.AppUtility;
import com.app.checkmoney.Util.DevelopeLog;
import com.moneycheck.checkmoneyapp.R;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link RoomUserListFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class RoomUserListFragment extends Fragment implements View.OnClickListener, RoomActivity.UserActivityResultListener{
    private RoomActivity parent_activity;
    private View view;
    private ArrayList<View> bg_list;
    private ArrayList<RoomUserItem> user_list;
    private String phoneNumber, messageContent;

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
        parent_activity = (RoomActivity) getActivity();
        parent_activity.setUserResultlistener(this);
        user_list = parent_activity.getGiverList();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view =  inflater.inflate(R.layout.layout_room_user_list, container, false);
        initView();
        return view;
    }

    private void initView(){
        TextView button_manageuser = (TextView) view.findViewById(R.id.button_manage_user);
        button_manageuser.setText(Html.fromHtml(getString(R.string.text_user_management)));
        button_manageuser.setOnClickListener(this);

        addUserListData(user_list);
    }


    private void addUserListData(ArrayList<RoomUserItem> roomUserItemArrayList){
        View rootview = view.findViewById(R.id.userlist_frame);
        if (rootview instanceof LinearLayout){
            int last_index = roomUserItemArrayList.size()-1;
            int index = 0;
            bg_list = new ArrayList<>();
            for (RoomUserItem item : roomUserItemArrayList){
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
                Toast.makeText(parent_activity, String.format(getString(R.string.text_confirm_deposit_money), user_list.get(position).getName()), Toast.LENGTH_SHORT).show();
                break;
            case R.id.button_deny :

                break;
            case R.id.button_manage_user :
                Intent intent = new Intent(parent_activity, UserManageActivity.class);
                intent.putExtra(AppUtility.BaseDataType.MANAGE_USER_LIST, user_list);
                parent_activity.startActivityForResult(intent, AppUtility.BaseDataType.USERMANAGE_REQUEST);
                parent_activity.overridePendingTransition(R.anim.anim_slide_in, R.anim.anim_keep);
                break;
            case R.id.user_info :
                final RoomUserItem item = user_list.get(position);
                final CustomProfileDialog dialog = new CustomProfileDialog(parent_activity);
                dialog.setName(item.getName());
                dialog.setPhonenumber(item.getPhoneNumber());
                dialog.setExchangeType(item.getEtype());
                dialog.setPay(item.ispay());
                dialog.setButtonlistener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        //전화 걸기
                        phoneNumber = item.getPhoneNumber();
                        if (RequestPermission.isGranted(getContext(), RequestPermission.CALL_PHONE_TYPE)) {
                            showCallDial(phoneNumber);
                        }

                        dialog.dismiss();
                    }
                }, new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        //메시지 전송
                        phoneNumber = item.getPhoneNumber();
                        messageContent = "aaaa";
                        if (RequestPermission.isGranted(getContext(), RequestPermission.SEND_SMS_TYPE))
                            sendMessage(item.getPhoneNumber(), messageContent);
                        dialog.dismiss();
                    }
                });
                dialog.show();
                break;

        }
    }

    private void showCallDial(String phone_number){
        if (phone_number== null || phone_number.isEmpty())
            return;
        Uri number_uri = Uri.parse("tel:"+phoneNumber);
        Intent callIntent = new Intent(Intent.ACTION_DIAL, number_uri);
        startActivity(callIntent);
    }

    private void sendMessage(String phone_number, String messageContent){
        if (phone_number == null || phone_number.isEmpty() || messageContent == null || messageContent.isEmpty())
            return;
        SmsManager smsManager = SmsManager.getDefault();
        smsManager.sendTextMessage(phone_number, null, messageContent, null, null);
    }
    private void reloadUserListData(){
        LinearLayout rootlayout = (LinearLayout) view.findViewById(R.id.userlist_frame);
        rootlayout.removeAllViews();

        addUserListData(user_list);
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case RequestPermission.Code.SEND_SMS:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    sendMessage(phoneNumber, messageContent);
                } else {
                    phoneNumber = null;
                    messageContent = null;
                    Toast.makeText(getContext(), getString(R.string.text_deny_send_message), Toast.LENGTH_SHORT).show();
                }
                break;
            case RequestPermission.Code.CALL_PHONE:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    showCallDial(phoneNumber);
                } else {
                    phoneNumber = null;
                    Toast.makeText(getContext(), getString(R.string.text_deny_call), Toast.LENGTH_SHORT).show();
                }
                break;
        }
    }

    @Override
    public void onActivityResults(int requestcode, int resultcode, Intent data) {
        reloadUserListData();
    }
}
