package com.app.checkmoney.CustomUi;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.Html;
import android.view.View;
import android.view.Window;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import com.moneycheck.checkmoneyapp.R;

/**
 * Created by Mhwan on 2016. 8. 28..
 */
public class CustomLoginDialog extends Dialog {
    private Context context;
    private EditText input_id, input_pw;
    private View.OnClickListener login_listener, register_listener;

    public CustomLoginDialog(Context context) {
        super(context);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        super.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        this.context = context;
    }

    public CustomLoginDialog(Context context, int themeResId) {
        super(context, themeResId);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        super.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        this.context = context;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ui_login_dialog);

        input_id = (EditText) findViewById(R.id.input_email);
        input_pw = (EditText) findViewById(R.id.input_pw);
        ImageButton button_login = (ImageButton) findViewById(R.id.button_login);
        button_login.setOnClickListener(login_listener);
        TextView forget_password = (TextView) findViewById(R.id.button_go_register);
        forget_password.setText(Html.fromHtml(context.getString(R.string.text_forget_password)));
        forget_password.setOnClickListener(register_listener);
    }

    public void setListener(View.OnClickListener login_listener, View.OnClickListener register_listener){
        this.login_listener = login_listener;
        this.register_listener = register_listener;
    }

    public String getInputId(){
        return input_id.getText().toString();
    }

    public String getInputPw(){
        return input_pw.getText().toString();
    }
}
