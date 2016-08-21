package com.app.checkmoney.CustomUi;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.res.TypedArray;
import android.os.Build;
import android.text.InputType;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.moneycheck.checkmoneyapp.R;

/**
 * Created by Mhwan on 2016. 8. 22..
 */
public class CustomInputLayout extends LinearLayout {
    private TextView title_textview;
    private EditText input_text;
    public static final int TYPE_STRING = 0;
    public static final int TYPE_NUMBER = 1;

    public CustomInputLayout(Context context) {
        super(context);
        initView();
    }

    public CustomInputLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView();
        setTypedArray(context, attrs);
    }

    public CustomInputLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView();
        setTypedArray(context, attrs);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public CustomInputLayout(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        initView();
        setTypedArray(context, attrs);
    }

    private void initView(){
        LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.ui_custom_input, this, false);
        addView(view);

        title_textview = (TextView) findViewById(R.id.title_view);
        input_text = (EditText) findViewById(R.id.input_view);
    }

    private void setTypedArray(Context context, AttributeSet attrs){
        TypedArray attrArray = context.obtainStyledAttributes(attrs, R.styleable.CustomInputLayout, 0, 0);
        String title = attrArray.getString(R.styleable.CustomInputLayout_view_title);
        String hint = attrArray.getString(R.styleable.CustomInputLayout_input_hint);
        String input_text = attrArray.getString(R.styleable.CustomInputLayout_input_text);
        int type = attrArray.getInt(R.styleable.CustomInputLayout_input_type, 0);
        boolean isblock = attrArray.getBoolean(R.styleable.CustomInputLayout_blockTouch, false);

        setTitleText(title);
        setInputHint(hint);
        setInputText(input_text);
        setInputType(type);
        blockTouchInputbox(isblock);

        attrArray.recycle();
    }

    public void setInputType(int type) {
        if (type == TYPE_NUMBER)
            input_text.setInputType(InputType.TYPE_CLASS_NUMBER);
    }

    public void setTitleText(String title) {
        title_textview.setText(title);
    }

    public void setInputHint(String hint) {
        input_text.setHint(hint);
    }

    public void setInputText(String text) {
        input_text.setText(text);
    }

    public EditText getInputBox(){
        return input_text;
    }

    public String getInputText(){
        return input_text.getText().toString();
    }

    public void blockTouchInputbox (boolean isblock){
        if (isblock) {
            input_text.setFocusable(false);
            input_text.setClickable(false);
        }
    }
}
