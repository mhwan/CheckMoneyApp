package com.app.checkmoney.CustomUi;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.LinearLayout;

import com.moneycheck.checkmoneyapp.R;

/**
 * Created by Mhwan on 2016. 8. 20..
 */
public class FadeInLinearlayout extends LinearLayout {
    private Context context;
    private Animation startanimation;

    public FadeInLinearlayout(Context context) {
        super(context);
        this.context = context;
        initAnimations();
    }

    public FadeInLinearlayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
        initAnimations();
    }

    public FadeInLinearlayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.context = context;
        initAnimations();
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public FadeInLinearlayout(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        this.context = context;
        initAnimations();
    }

    private void initAnimations() {
        startanimation = AnimationUtils.loadAnimation(context, R.anim.anim_button);
    }

    public void show() {
        if (isVisible())
            return;
        show(true);
    }

    public void show(boolean withAnimation) {
        if (withAnimation)
            this.startAnimation(startanimation);
        this.setVisibility(View.VISIBLE);
    }

    public boolean isVisible()
    {
        return (this.getVisibility() == View.VISIBLE);
    }
}
