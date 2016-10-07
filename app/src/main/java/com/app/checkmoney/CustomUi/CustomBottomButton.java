package com.app.checkmoney.CustomUi;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.GradientDrawable;
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
    private View rippleView;
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
        rippleView = view.findViewById(R.id.button_ripple);
    }

    private void setTypedArray(Context context, AttributeSet attrs){
        TypedArray attrArray = context.obtainStyledAttributes(attrs, R.styleable.CustomBottomButton, 0, 0);
        String text = attrArray.getString(R.styleable.CustomBottomButton_button_text);
        int resourceId = attrArray.getResourceId(R.styleable.CustomBottomButton_button_icon, 0);
        int bgcolor = attrArray.getColor(R.styleable.CustomBottomButton_button_color, ContextCompat.getColor(context, R.color.colorPrimary));
        int rippleId = attrArray.getResourceId(R.styleable.CustomBottomButton_button_ripple, 0);
        boolean isRipple = attrArray.getBoolean(R.styleable.CustomBottomButton_ripple_visible, true);
        int textsize = (int) (attrArray.getDimension(R.styleable.CustomBottomButton_text_size, 0)/context.getResources().getDisplayMetrics().density);
        setButton_text(text);
        setButtonTextSize(textsize);
        setButton_icon(resourceId);
        setButtonBackground(bgcolor);
        setButtonRipple(rippleId, isRipple);

        attrArray.recycle();
    }

    public void setButtonTextSize(int size){
        if (size<=0)
            return;
        button_text.setTextSize(size);
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

    public RelativeLayout getButtonBackground() {
        return background;
    }

    public void setButtonRipple(int rippleId, boolean isRipple){
        if (isRipple)
            setButtonRipple(rippleId);
        else
            rippleView.setVisibility(GONE);
    }

    /**
     * 버튼에 상단 그림자 효과를 준다
     * (resourceId == 0) 버튼색에서 알파값 30%에서 투명한 그라디언트를 생성하여 지정함
     *
     * @param resourceId 그라디언트 리소스 아이디
     */
    public void setButtonRipple(int resourceId){
        rippleView.setVisibility(VISIBLE);
        if (resourceId == 0){
            ColorDrawable colorDrawable = (ColorDrawable) background.getBackground();
            int color = colorDrawable.getColor();
            int red = (color >> 16) & 0xFF;
            int green = (color >> 8) & 0xFF;
            int blue = (color >> 0) & 0xFF;
            int alpha = (int) 2.55*30;

            int newColor = Color.argb(alpha, red, green, blue);

            GradientDrawable gd = new GradientDrawable(
                    GradientDrawable.Orientation.TOP_BOTTOM,
                    new int[] {0x00000000 , newColor});
            gd.setCornerRadius(0f);

            if (android.os.Build.VERSION.SDK_INT < android.os.Build.VERSION_CODES.JELLY_BEAN) {
                rippleView.setBackgroundDrawable(gd);
            } else {
                rippleView.setBackground(gd);
            }

        } else {
            rippleView.setBackgroundResource(resourceId);
        }
    }
    public void setButtonBackground(int colorid){
        background.setBackgroundColor(colorid);
    }
}
