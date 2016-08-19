package com.app.checkmoney.CustomUi;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.TextView;

import com.moneycheck.checkmoneyapp.R;

/**
 * Created by Mhwan on 2016. 8. 19..
 */
public class CustomAlertDialog extends Dialog {
    private String title, message, negative_text = "취소", positive_text = "확인";
    private View.OnClickListener negative_listener = null, positive_listener = null;

    public CustomAlertDialog(Context context) {
        super(context);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        super.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ui_alert_dialog);

        TextView titleview = (TextView) findViewById(R.id.title_text);
        TextView messageview = (TextView) findViewById(R.id.message_text);
        TextView positivebutton = (TextView) findViewById(R.id.positive_button);
        TextView negativebutton = (TextView) findViewById(R.id.negative_button);

        titleview.setText(title);
        messageview.setText(message);

        //두개의 버튼을 모두 활성화 시킴
        if (positive_listener != null && negative_listener != null){
            positivebutton.setVisibility(View.VISIBLE);
            negativebutton.setVisibility(View.VISIBLE);
            positivebutton.setText(positive_text);
            negativebutton.setText(negative_text);
            positivebutton.setOnClickListener(positive_listener);
            negativebutton.setOnClickListener(negative_listener);
        } else if (positive_listener != null) {
            negativebutton.setVisibility(View.GONE);
            positivebutton.setVisibility(View.VISIBLE);
            positivebutton.setText(positive_text);
            positivebutton.setOnClickListener(positive_listener);
        } else if (negative_listener != null) {
            positivebutton.setVisibility(View.GONE);
            negativebutton.setVisibility(View.VISIBLE);
            negativebutton.setText(negative_text);
            negativebutton.setOnClickListener(negative_listener);
        } else {
            positivebutton.setVisibility(View.GONE);
            negativebutton.setVisibility(View.GONE);
        }
    }

    public void setTitle(String title){
        this.title = title;
    }

    public void setMessage(String message) {
        this.message = message;
    }
    public void setNegativeButton(String negative_text, final View.OnClickListener listener){
        this.negative_text = negative_text;
        this.negative_listener = listener;
    }
    public void setPositiveButton(String positive_text, final View.OnClickListener listener){
        this.positive_text = positive_text;
        this.positive_listener = listener;
    }
}
