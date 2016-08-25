package com.app.checkmoney.Activity;

import android.app.Activity;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.text.Html;
import android.view.View;
import android.widget.TextView;

import com.app.checkmoney.CustomBase.BaseActivity;
import com.app.checkmoney.CustomUi.CustomInputLayout;
import com.app.checkmoney.Util.AppUtility;
import com.moneycheck.checkmoneyapp.R;
import com.wdullaer.materialdatetimepicker.date.DatePickerDialog;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class EditRoomActivity extends BaseActivity {
    private Calendar calendar;
    private CustomInputLayout title_layout, money_layout, date_layout;
    private TextView button_manage_user;
    private String title;
    private int money;
    public static final String NAME_ROOM = "key_name_room";
    public static final String MONEY_ROOM = "key_money_room";
    public static final String DATE_ROOM = "key_date_room";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setFromBaseView(R.layout.activity_edit_room);
        setToolbar(R.layout.layout_base_toolbar, R.id.toolbar_close, ToolbarType.SUB_TYPE);
        Bundle bundle = getIntent().getExtras();
        if (bundle != null){
            title = bundle.getString(NAME_ROOM);
            money = bundle.getInt(MONEY_ROOM);
            String sDate = bundle.getString(DATE_ROOM);
            calendar = Calendar.getInstance();
            calendar.setTime(AppUtility.getInstance().getDate(sDate, AppUtility.BaseDataType.DB_DATE_FORMAT));
        }
        initView();
    }

    private void initView(){
        title_layout = (CustomInputLayout) findViewById(R.id.input_title);
        money_layout = (CustomInputLayout) findViewById(R.id.input_money);
        date_layout = (CustomInputLayout) findViewById(R.id.input_expire);
        button_manage_user = (TextView) findViewById(R.id.button_manage_user);
        button_manage_user.setText(Html.fromHtml("<u>" + button_manage_user.getText().toString() + "</u>"));
        button_manage_user.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        date_layout.getInputBox().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openDatePicker();
            }
        });
        findViewById(R.id.button_create_room).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        title_layout.setInputText(title);
        money_layout.setInputText(money+"");
        if (calendar!=null)
            setTimeText(calendar.getTime());

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
        return "방 설정";
    }

    @Override
    protected boolean getResult() {
        return false;
    }

    @Override
    protected Activity getActivity() {
        return EditRoomActivity.this;
    }
}
