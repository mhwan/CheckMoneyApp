package com.app.checkmoney.CustomUi;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.preference.PreferenceCategory;
import android.util.AttributeSet;

import com.moneycheck.checkmoneyapp.R;

/**
 * Created by Mhwan on 2016. 8. 23..
 */
public class CustomPreferenceCategory extends PreferenceCategory {
    public CustomPreferenceCategory(Context context) {
        super(context);
        setLayoutResource(R.layout.layout_preference_category);
    }

    public CustomPreferenceCategory(Context context, AttributeSet attrs) {
        super(context, attrs);
        setLayoutResource(R.layout.layout_preference_category);
    }

    public CustomPreferenceCategory(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        setLayoutResource(R.layout.layout_preference_category);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public CustomPreferenceCategory(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        setLayoutResource(R.layout.layout_preference_category);
    }
}
