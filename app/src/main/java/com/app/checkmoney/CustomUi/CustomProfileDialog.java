package com.app.checkmoney.CustomUi;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.app.checkmoney.Items.UserItem;
import com.moneycheck.checkmoneyapp.R;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by Mhwan on 2016. 8. 27..
 */
public class CustomProfileDialog extends Dialog {
    private Context context;
    private String name, phonenumber;
    private UserItem.ExchangeType exchangeType;
    private boolean isPay = false;
    private View.OnClickListener call_listener, message_listener;

    public CustomProfileDialog(Context context) {
        super(context);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        super.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        this.context = context;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ui_profile_dialog);

        TextView text_name = (TextView) findViewById(R.id.dialog_name);
        TextView text_phone = (TextView) findViewById(R.id.dialog_phone_number);
        ImageButton button_call = (ImageButton) findViewById(R.id.button_call);
        ImageButton button_message = (ImageButton) findViewById(R.id.button_message);
        ImageView sign_view = (ImageView) findViewById(R.id.image_sign);
        CircleImageView image_profile = (CircleImageView) findViewById(R.id.image_profile);

        text_name.setText(name);
        text_phone.setText(phonenumber);
        button_call.setOnClickListener(call_listener);
        button_message.setOnClickListener(message_listener);

        if (exchangeType.equals(UserItem.ExchangeType.RECEIVER))
            sign_view.setImageResource(R.mipmap.image_sign_receiver);
        else {
            if (isPay)
                sign_view.setImageResource(R.mipmap.image_sign_giver);
            else
                sign_view.setImageResource(R.mipmap.image_sign_not_give);
        }
    }

    public void setExchangeType(UserItem.ExchangeType exchangeType) {
        this.exchangeType = exchangeType;
    }

    public void setPay(boolean pay) {
        isPay = pay;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setButtonlistener(View.OnClickListener call_listener, View.OnClickListener message_listener) {
        this.call_listener = call_listener;
        this.message_listener = message_listener;
    }

    public void setPhonenumber(String phonenumber) {
        this.phonenumber = phonenumber;
    }
}
