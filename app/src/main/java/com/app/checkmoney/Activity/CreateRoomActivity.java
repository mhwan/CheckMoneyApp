package com.app.checkmoney.Activity;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.view.View;

import com.app.checkmoney.CustomBase.BaseActivity;
import com.app.checkmoney.CustomUi.CustomInputLayout;
import com.moneycheck.checkmoneyapp.R;
import com.wdullaer.materialdatetimepicker.date.DatePickerDialog;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class CreateRoomActivity extends BaseActivity {
    private CustomInputLayout title_layout, money_layout, date_layout;
    private Calendar calendar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setFromBaseView(R.layout.activity_create_room);
        setToolbar(R.layout.layout_base_toolbar, R.id.toolbar_close, ToolbarType.SUB_TYPE);
        initView();
    }

    private void initView(){
        title_layout = (CustomInputLayout) findViewById(R.id.input_title);
        money_layout = (CustomInputLayout) findViewById(R.id.input_money);
        date_layout = (CustomInputLayout) findViewById(R.id.input_expire);
        date_layout.getInputBox().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openDatePicker();
            }
        });
    }

    private void openDatePicker(){
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
        datePickerDialog.setTitle("기한 선택");
        datePickerDialog.vibrate(true);
        datePickerDialog.show(getFragmentManager(), "datepicker");
    }

    private void setTimeText(Date date) {
        SimpleDateFormat format = new SimpleDateFormat("yyyy년 MM월 dd일 까지", Locale.US);
        date_layout.setInputText(format.format(date));
    }
    @Override
    protected String getToolbarTitle() {
        return "방 만들기";
    }
}
