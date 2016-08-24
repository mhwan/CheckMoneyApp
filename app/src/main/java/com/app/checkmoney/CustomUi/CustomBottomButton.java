package com.app.checkmoney.CustomUi;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.res.TypedArray;
import android.os.Build;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.moneycheck.checkmoneyapp.R;

/**
 * Created by Mhwan on 2016. 8. 25..
 */
public class CustomBottomButton extends RelativeLayout {
    private TextView button_text;
    private ImageView button_icon;
    private RelativeLayout background;
    private Context context;

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public CustomBottomButton(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        this.context = context;
        initView();
        setTypedArray(context, attrs);
    }

    public CustomBottomButton(Context context) {
        super(context);
        this.context = context;
        initView();
    }

    public CustomBottomButton(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
        initView();
        setTypedArray(context, attrs);
    }

    public CustomBottomButton(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.context = context;
        initView();
        setTypedArray(context, attrs);
    }

    private void initView(){
        LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.ui_bottom_button, this, false);
        addView(view);

        button_text = (TextView) view.findViewById(R.id.button_text);
        button_icon = (ImageView) view.findViewById(R.id.button_image);
        background = (RelativeLayout) view.findViewById(R.id.button_root);
    }

    private void setTypedArray(Context context, AttributeSet attrs){
        TypedArray attrArray = context.obtainStyledAttributes(attrs, R.styleable.CustomBottomButton, 0, 0);
        String text = attrArray.getString(R.styleable.CustomBottomButton_button_text);
        int resourceId = attrArray.getResourceId(R.styleable.CustomBottomButton_button_icon, 0);
        int bgcolor = attrArray.getColor(R.styleable.CustomBottomButton_button_color, ContextCompat.getColor(context, R.color.colorPrimary));
        setButton_text(text);
        setButton_icon(resourceId);
        //setButtonBackground(bgcolor);

        attrArray.recycle();
    }

    public void setButton_text(String text){
        button_text.setText(text);
    }

    public void setButton_icon(int resourceid){
        if (resourceid!=0) {
            button_icon.setVisibility(VISIBLE);
            button_icon.setImageResource(resourceid);
        }
        else
            button_icon.setVisibility(GONE);
    }

    /*
    public void setButtonBackground(int colorid){
        background.setBackgroundColor(ContextCompat.getColor(context, colorid));
    }*/
}
