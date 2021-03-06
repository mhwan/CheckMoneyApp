package com.app.checkmoney.CustomUi;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.preference.Preference;
import android.util.AttributeSet;

import com.moneycheck.checkmoneyapp.R;

/**
 * Created by Mhwan on 2016. 8. 23..
 */
public class CustomPreference extends Preference {
    private Context context;
    public CustomPreference(Context context) {
        super(context);
        this.context = context;
        setLayoutResource(R.layout.layout_preference);
    }

    public CustomPreference(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
        setLayoutResource(R.layout.layout_preference);
    }

    public CustomPreference(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.context = context;
        setLayoutResource(R.layout.layout_preference);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public CustomPreference(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        this.context = context;
        setLayoutResource(R.layout.layout_preference);
    }
}
