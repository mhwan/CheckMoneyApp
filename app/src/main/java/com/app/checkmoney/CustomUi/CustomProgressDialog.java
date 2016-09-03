package com.app.checkmoney.CustomUi;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.support.v4.content.ContextCompat;
import android.widget.ProgressBar;

import com.moneycheck.checkmoneyapp.R;

/**
 * Created by Mhwan on 2016. 9. 1..
 */
public class CustomProgressDialog extends ProgressDialog {
    private Context context;
    public CustomProgressDialog(Context context) {
        super(context);
        this.context = context;
        initView();
    }

    private void initView(){
        setOnShowListener(new DialogInterface.OnShowListener() {
            @Override
            public void onShow(DialogInterface dialog) {
                ProgressBar v = (ProgressBar) findViewById(android.R.id.progress);
                v.getIndeterminateDrawable().setColorFilter(ContextCompat.getColor(context, R.color.colorPrimaryDark),
                        android.graphics.PorterDuff.Mode.MULTIPLY);
            }
        });
        setCanceledOnTouchOutside(false);
        setCancelable(false);
    }
}
