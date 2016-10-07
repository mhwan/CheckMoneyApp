package com.app.checkmoney.CustomUi;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.text.InputType;
import android.text.Selection;
import android.util.TypedValue;
import android.view.ContextThemeWrapper;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.app.checkmoney.Util.DevelopeLog;
import com.moneycheck.checkmoneyapp.R;

/**
 * Created by Mhwan on 2016. 8. 19..
 */
public class CustomAlertDialog extends Dialog {
    private String title, message, hint, negative_text = getContext().getString(R.string.text_cancel), positive_text = getContext().getString(R.string.okay);
    private View.OnClickListener negative_listener = null, positive_listener = null;
    private inflateType type;
    private int layoutId =0;
    private Context context;
    private View rootview;
    private View customView;
    private EditText editText;
    private int inputType = InputType.TYPE_CLASS_TEXT;

    public CustomAlertDialog(Context context) {
        super(context);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        super.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        this.context = context;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ui_alert_dialog);

        TextView titleview = (TextView) findViewById(R.id.title_text);
        TextView positivebutton = (TextView) findViewById(R.id.positive_button);
        TextView negativebutton = (TextView) findViewById(R.id.negative_button);
        if (title.equals("") || title.isEmpty() || title == null)
            titleview.setVisibility(View.GONE);
        else {
            titleview.setVisibility(View.VISIBLE);
            titleview.setText(title);
        }

        rootview = findViewById(R.id.message_frame);
        inflateView((ViewGroup) rootview);

        //두개의 버튼을 모두 활성화 시킴
        if (positive_listener != null && negative_listener != null){
            positivebutton.setVisibility(View.VISIBLE);
            negativebutton.setVisibility(View.VISIBLE);
            positivebutton.setText(positive_text);
            negativebutton.setText(negative_text);
            positivebutton.setOnClickListener(positive_listener);
            negativebutton.setOnClickListener(negative_listener);
        } else if (positive_listener != null && negative_listener == null) {
            negativebutton.setVisibility(View.GONE);
            positivebutton.setVisibility(View.VISIBLE);
            positivebutton.setText(positive_text);
            positivebutton.setOnClickListener(positive_listener);
        } else if (negative_listener != null && positive_listener == null) {
            positivebutton.setVisibility(View.GONE);
            negativebutton.setVisibility(View.VISIBLE);
            negativebutton.setText(negative_text);
            negativebutton.setOnClickListener(negative_listener);
        } else {
            positivebutton.setVisibility(View.GONE);
            negativebutton.setVisibility(View.GONE);
        }
    }

    public View getDialogRootView(){
        return rootview;
    }
    public void setTitle(String title){
        this.title = title;
    }

    public void setMessage(String message) {
        type = inflateType.GENERAL_TEXT;
        this.message = message;
    }

    public void setEditMessage(String hint, String message){
        type = inflateType.EDIT_TEXT;
        this.message = message;
        this.hint = hint;
    }

    public void setInputType(int inputType){
        this.inputType = inputType;
    }
    public void setLayout(int layoutId){
        type = inflateType.LAYOUT;
        this.layoutId = layoutId;
    }

    public void setLayout(View view){
        type = inflateType.LAYOUT;
        this.customView = view;
    }
    public void setNegativeButton(String negative_text, final View.OnClickListener listener){
        this.negative_text = negative_text;
        this.negative_listener = listener;
    }
    public void setPositiveButton(String positive_text, final View.OnClickListener listener){
        this.positive_text = positive_text;
        this.positive_listener = listener;
    }

    public String getEdittextMessage(){
        return editText.getText().toString();
        //return getEdittextMessage(rootview);
    }

    public String getEdittextMessage(View view) {
        String message = "";
        DevelopeLog.d(view.getClass().getName().toString());
        if (view instanceof ViewGroup) {
            ViewGroup viewGroup = (ViewGroup) view;
            for (int i=0; i<viewGroup.getChildCount(); i++){
                getEdittextMessage(viewGroup.getChildAt(i));
            }
        } else if (view instanceof EditText) {
            DevelopeLog.d("edittext!");
            EditText editText = (EditText) view;
            return editText.getText().toString();
        }

        return message;
    }
    private void inflateView(ViewGroup rootView){
        if (type.equals(inflateType.LAYOUT)) {
            if (rootView instanceof RelativeLayout) {
                RelativeLayout relativeLayout = (RelativeLayout) rootView;
                relativeLayout.setGravity(Gravity.LEFT);
            }
            if (layoutId != 0 && customView == null) {
                LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                View view = inflater.inflate(layoutId, rootView, false);
            }
            else {
                rootView.addView(customView);
            }
        } else {
            RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams
                    (ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            params.addRule(RelativeLayout.CENTER_HORIZONTAL);

            if (type.equals(inflateType.GENERAL_TEXT)) {
                TextView textView = new TextView(context);
                textView.setText(message);
                textView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 15);
                textView.setTextColor(ContextCompat.getColor(context, R.color.colorAccentBlack));
                textView.setLayoutParams(params);

                rootView.addView(textView);
            } else if (type.equals(inflateType.EDIT_TEXT)) {
                ContextThemeWrapper newContext = new ContextThemeWrapper(context, R.style.EditTextTheme);
                editText = new EditText(newContext);
                editText.setText(message);
                Selection.setSelection(editText.getText(), message.length());
                editText.setTextSize(TypedValue.COMPLEX_UNIT_SP, 15);
                editText.setTextColor(ContextCompat.getColor(context, R.color.colorAccentBlack));
                editText.setHint(hint);
                editText.setInputType(inputType);
                editText.setHintTextColor(ContextCompat.getColor(context, R.color.colorAccentLight));
                editText.setLayoutParams(params);

                rootView.addView(editText);
            }

        }
    }

    private enum inflateType { GENERAL_TEXT, EDIT_TEXT, LAYOUT }
}
