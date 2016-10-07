package com.app.checkmoney.Activity;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.app.checkmoney.CustomBase.BaseActivity;
import com.app.checkmoney.CustomUi.CustomAlertDialog;
import com.app.checkmoney.CustomUi.CustomInputLayout;
import com.app.checkmoney.CustomUi.CustomProgressDialog;
import com.app.checkmoney.Util.AppUtility;
import com.app.checkmoney.NetworkUtil.NetworkUtility;
import com.moneycheck.checkmoneyapp.R;
import com.wdullaer.materialdatetimepicker.date.DatePickerDialog;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class CreateRoomActivity extends BaseActivity implements View.OnClickListener{
    private CustomInputLayout title_layout, money_layout, date_layout, receiver_layout;
    private Calendar calendar;
    private boolean isEdit = false;
    private boolean isUserManage = false;
    private String roomId;
    private int checked_receiver_id = -1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle bundle = getIntent().getExtras();
        if (bundle != null){
            isEdit = true;
            roomId= "ss";
        }
        setFromBaseView(R.layout.activity_create_room);
        setToolbar(R.layout.layout_base_toolbar, R.id.toolbar_close, ToolbarType.SUB_TYPE);
        initView();
        writeContent(bundle);
    }

    private void initView(){
        title_layout = (CustomInputLayout) findViewById(R.id.input_title);
        money_layout = (CustomInputLayout) findViewById(R.id.input_money);
        date_layout = (CustomInputLayout) findViewById(R.id.input_expire);
        receiver_layout = (CustomInputLayout) findViewById(R.id.input_receiver);
        TextView button_manage_user = (TextView) findViewById(R.id.button_manage_user);

        button_manage_user.setText(Html.fromHtml("<u>" + button_manage_user.getText().toString() + "</u>"));
        button_manage_user.setOnClickListener(this);
        date_layout.getInputBox().setOnClickListener(this);
        receiver_layout.getInputBox().setOnClickListener(this);
        findViewById(R.id.button_create_room).setOnClickListener(this);

        if (isEdit) {
            button_manage_user.setVisibility(View.VISIBLE);
            receiver_layout.setVisibility(View.GONE);
        }
    }

    private void showSelectReceiverDialog(){
        ColorStateList colorStateList = new ColorStateList(
                new int[][]{
                        new int[]{-android.R.attr.state_checked},
                        new int[]{android.R.attr.state_checked}
                },
                new int[]{
                        ContextCompat.getColor(getContext(), R.color.colorAccentLight)
                        , ContextCompat.getColor(getContext(), R.color.colorPrimaryDark),
                }
        );
        final String [] textlist = new String[]{"본인", "다른 사람"};
        View view = LayoutInflater.from(getContext()).inflate(R.layout.ui_dialog_selectuser, null);
        final RadioGroup radioGroup = (RadioGroup)view.findViewById(R.id.radio_group);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            ((RadioButton)view.findViewById(R.id.radio_1)).setButtonTintList(colorStateList);
            ((RadioButton)view.findViewById(R.id.radio_2)).setButtonTintList(colorStateList);
        }

        //다이얼로그를 열때 체크된 리스트를 초기화함 (처음열 경우 첫번째 아이템으로 초기화한다.
        if (checked_receiver_id == 1) {
            ((RadioButton) view.findViewById(R.id.radio_2)).setChecked(true);
            checked_receiver_id = 1;
        }
        else if (checked_receiver_id == 0) {
            ((RadioButton) view.findViewById(R.id.radio_1)).setChecked(true);
            checked_receiver_id = 0;
        }
        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                View radioButton = radioGroup.findViewById(checkedId);
                int index = radioGroup.indexOfChild(radioButton);
                checked_receiver_id = index;
            }
        });

        final CustomAlertDialog dialog = new CustomAlertDialog(getContext());
        dialog.setTitle(getString(R.string.title_receiver));
        dialog.setLayout(view);
        dialog.setPositiveButton(getString(R.string.okay), new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                if (checked_receiver_id != -1) {
                    receiver_layout.setInputText(textlist[checked_receiver_id]);
                    if (checked_receiver_id == 1)
                        Toast.makeText(getContext(), getString(R.string.text_receiver_other_person), Toast.LENGTH_SHORT).show();
                }
            }
        });
        dialog.show();
    }

    public void writeContent(Bundle bundle){
        if (bundle == null)
            return;

        String title = bundle.getString(AppUtility.BaseDataType.NAME_ROOM);
        int money = bundle.getInt(AppUtility.BaseDataType.MONEY_ROOM);
        String sDate = bundle.getString(AppUtility.BaseDataType.DATE_ROOM);
        calendar = Calendar.getInstance();
        calendar.setTime(AppUtility.getInstance().getDate(sDate, AppUtility.BaseDataType.DB_DATE_FORMAT));

        title_layout.setInputText(title);
        money_layout.setInputText(String.valueOf(money));
        setTimeText(calendar.getTime());
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == AppUtility.BaseDataType.USERMANAGE_REQUEST) {
            isUserManage = true;
        }
    }

    @Override
    protected void setResultForFinish() {
        if (isEdit) {
            if (isUserManage)
                setResult(AppUtility.BaseDataType.ROOM_USER_MANAGE_RESULT);
            else
                setResult(AppUtility.BaseDataType.ROOM_MANAGE_RESULT);

            finishActivity();
        } else
            super.setResultForFinish();
    }

    private void createRoomWork(String title, String money){
        new AsyncTask<String, Void, NetworkUtility.BasicStatus>() {
            private CustomProgressDialog dialog;
            @Override
            protected void onPreExecute() {
                dialog = new CustomProgressDialog(getContext());
                dialog.setMessage("방 생성중..");
                dialog.show();
            }

            @Override
            protected void onPostExecute(NetworkUtility.BasicStatus basicStatus) {
                dialog.cancel();
                switch (basicStatus) {
                    case OK:
                        if (isEdit) {
                            Toast.makeText(getContext(), getString(R.string.text_success_edit_room), Toast.LENGTH_SHORT).show();
                            finishActivity();
                        }
                        else {
                            Toast.makeText(getContext(), getString(R.string.text_success_create_room), Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(getContext(), RoomActivity.class);
                            startActivityWithAnim(intent);
                            CreateRoomActivity.this.finish();
                        }
                        break;
                    case FAIL:
                        Toast.makeText(getContext(), getString(R.string.text_fail_create_room), Toast.LENGTH_SHORT).show();
                        break;
                }
            }

            @Override
            protected NetworkUtility.BasicStatus doInBackground(String... params) {
                NetworkUtility.BasicStatus status = NetworkUtility.BasicStatus.FAIL;
                status = NetworkUtility.getInstance().writeRoomData(params[0], params[1], params[2], roomId);
                return status;
            }
        }.execute(title, money, new SimpleDateFormat(AppUtility.BaseDataType.DB_DATE_FORMAT).format(calendar.getTime()));
    }

    private boolean isValidate(String title, String money) {
        if (title == null || title.equals("") || title.isEmpty()) {
            Toast.makeText(getContext(), getString(R.string.text_invalidate_title), Toast.LENGTH_SHORT).show();
            return false;
        } else if (money == null || !checkMoneyData(money))
            return false;
        else if (!AppUtility.getInstance().isAfterDay(calendar)) {
            Toast.makeText(getContext(), getString(R.string.text_invalidate_time), Toast.LENGTH_SHORT).show();
            return false;
        } else if (!isEdit && checked_receiver_id == -1){
            Toast.makeText(getContext(), getString(R.string.text_invalidate_receiver), Toast.LENGTH_SHORT).show();
            return false;
        }

        return true;
    }

    private boolean checkMoneyData(String money){
        int value = Integer.parseInt(money);
        if (value < 1000) {
            Toast.makeText(getContext(), getString(R.string.text_invalidate_money), Toast.LENGTH_SHORT).show();
            return false;
        }

        return true;
    }

    private void openDatePicker(){
        if (calendar == null)
            calendar = Calendar.getInstance();

        DatePickerDialog datePickerDialog = DatePickerDialog.newInstance(new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePickerDialog view, int year, int monthOfYear, int dayOfMonth) {
                calendar.set(year, monthOfYear, dayOfMonth);
                setTimeText(calendar.getTime());
            }
        }, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));
        datePickerDialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialog) {
                dialog.dismiss();
            }
        });
        datePickerDialog.setAccentColor(ContextCompat.getColor(getApplicationContext(), R.color.colorPrimaryDark));
        datePickerDialog.setTitle(getString(R.string.text_select_date));
        datePickerDialog.vibrate(true);
        datePickerDialog.show(getFragmentManager(), "datepicker");
    }

    private void setTimeText(Date date) {
        SimpleDateFormat format = new SimpleDateFormat(getString(R.string.text_format_expire_date));
        date_layout.setInputText(format.format(date));
    }
    @Override
    protected String getToolbarTitle() {
        return (isEdit) ? getString(R.string.title_edit_room) : getString(R.string.title_create_room);
    }

    @Override
    protected boolean getResult() {
        return false;
    }

    @Override
    protected Activity getActivity() {
        return CreateRoomActivity.this;
    }

    @Override
    public void onClick(View v) {
        int id  = v.getId();
        switch (id) {
            case R.id.button_manage_user :
                Intent intent = new Intent(getContext(), UserManageActivity.class);
                startActivityForResultWithAnim(intent, AppUtility.BaseDataType.USERMANAGE_REQUEST);
                break;
            case R.id.input_receiver :
                showSelectReceiverDialog();
                break;
            case R.id.input_expire :
                openDatePicker();
                break;
            case R.id.button_create_room :
                String title = title_layout.getInputText();
                String money = money_layout.getInputText();
                if (isValidate(title, money) && AppUtility.getInstance().isNetworkConnection())
                    createRoomWork(title, money);
                break;
        }
    }
}
