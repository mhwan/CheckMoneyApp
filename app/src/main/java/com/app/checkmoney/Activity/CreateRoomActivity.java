package com.app.checkmoney.Activity;

import android.app.Activity;
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
        findViewById(R.id.button_create_room).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

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
        return getString(R.string.title_create_room);
    }

    @Override
    protected boolean getResult() {
        return false;
    }

    @Override
    protected Activity getActivity() {
        return CreateRoomActivity.this;
    }
}
