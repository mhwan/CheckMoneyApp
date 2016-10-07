package com.app.checkmoney.CustomUi;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.Window;
import android.widget.TextView;

import com.moneycheck.checkmoneyapp.R;

/**
 * Created by Mhwan on 2016. 9. 1..
 */
public class CustomProgressDialog extends Dialog {
    private Context context;
    private Type type = Type.DEFAULT;
    private TextView message;
    public CustomProgressDialog(Context context) {
        super(context);
        this.context = context;
        initView();
    }

    public CustomProgressDialog(Context context, Type type){
        super(context);
        this.context = context;
        this.type = type;
        initView();
    }

    private void initView(){
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        setCancelable(false);
        setCanceledOnTouchOutside(false);

        if (type.equals(Type.TRANSPARENT))
            setContentView(R.layout.ui_progress_circle);
        else {
            requestWindowFeature(Window.FEATURE_NO_TITLE);
            getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            setCanceledOnTouchOutside(false);
            setCancelable(false);
            setContentView(R.layout.ui_progress_dialog);
            message = (TextView) findViewById(R.id.message);
        }
    }

    public boolean isShowing(){
        return super.isShowing();
    }

    public void setMessage(String message){
        if (type.equals(Type.DEFAULT) && this.message != null)
            this.message.setText(message);
    }

    public enum Type { DEFAULT, TRANSPARENT }
}
