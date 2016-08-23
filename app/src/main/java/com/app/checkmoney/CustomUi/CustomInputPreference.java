package com.app.checkmoney.CustomUi;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.preference.Preference;
import android.util.AttributeSet;
import android.view.View;
import android.widget.TextView;

import com.app.checkmoney.Util.DevelopeLog;
import com.moneycheck.checkmoneyapp.R;

/**
 * Created by Mhwan on 2016. 8. 23..
 */
public class CustomInputPreference extends Preference {
    private TextView newSignView;
    private Context context;
    public CustomInputPreference(Context context) {
        super(context);
        this.context = context;
        setLayoutResource(R.layout.layout_input_preference);
    }

    public CustomInputPreference(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
        setLayoutResource(R.layout.layout_input_preference);
    }

    public CustomInputPreference(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.context = context;
        setLayoutResource(R.layout.layout_input_preference);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public CustomInputPreference(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        this.context = context;
        setLayoutResource(R.layout.layout_input_preference);
    }

    public void setNewSignView(){
        if (newSignView!=null)
            newSignView.setVisibility(View.VISIBLE);
        else {
            DevelopeLog.e("textview " + "null!!!!!!!!!!!!!!");
        }
    }
}
