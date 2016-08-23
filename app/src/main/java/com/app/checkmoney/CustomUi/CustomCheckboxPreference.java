package com.app.checkmoney.CustomUi;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.preference.CheckBoxPreference;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;

import com.moneycheck.checkmoneyapp.R;

/**
 * Created by Mhwan on 2016. 8. 23..
 */
public class CustomCheckboxPreference extends CheckBoxPreference {
    private Context context;
    public CustomCheckboxPreference(Context context) {
        super(context);
        this.context = context;
        setLayoutResource(R.layout.layout_checkbox_preference);
    }

    public CustomCheckboxPreference(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
        setLayoutResource(R.layout.layout_checkbox_preference);
    }

    public CustomCheckboxPreference(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.context = context;
        setLayoutResource(R.layout.layout_checkbox_preference);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public CustomCheckboxPreference(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        this.context = context;
        setLayoutResource(R.layout.layout_checkbox_preference);
    }

    @Override
    protected View onCreateView(ViewGroup parent) {

        return super.onCreateView(parent);
    }
}
