package com.organization.jeptagsalpha.ui.controls;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Typeface;
import android.support.annotation.NonNull;
import android.util.AttributeSet;

import com.rengwuxian.materialedittext.MaterialEditText;
import com.organization.jeptagsalpha.MyConstants;
import com.organization.jeptagsalpha.R;

public class MyEditText extends MaterialEditText {

    private static final int INVALID_VALUE = -1;
    public MyEditText(Context context, AttributeSet attrs, int defStyle) {


        super(context, attrs, defStyle);
        setUpController(context, attrs);
    }

    public MyEditText(Context context, AttributeSet attrs) {
        super(context, attrs);
        setUpController(context, attrs);
    }

    public MyEditText(Context context) {
        super(context);
    }



    private void setUpController(Context context,AttributeSet attrs) {
        if (!isInEditMode()) {
            Typeface tf = Typeface.createFromAsset(getContext().getAssets(), MyConstants.FONT_REG_PATH);
            setTypeface(tf);
        }
        //setSingleLine(true);
        setFocusFraction(1f);
        setFloatingLabel(FLOATING_LABEL_NORMAL);
        setFloatingLabelTextColor(getResources().getColor(R.color.textColorSecondry));
        setBaseColor(getResources().getColor(R.color.textColorSecondry));
        setTextColor(getResources().getColor(R.color.textColorSecondry));
        setHintTextColor(getResources().getColor(R.color.textColorSecondry));
        setPrimaryColor(getResources().getColor(R.color.accent));
        setErrorColor(getResources().getColor(R.color.accent));
        setHelperTextColor(getResources().getColor(R.color.textColorSecondry));
        setUnderlineColor(getResources().getColor(R.color.textColorSecondry));
       // setTextSize(MyConstants.convertDpToPixel(8));

    }

    @Override
    protected void onDraw(@NonNull Canvas canvas) {
        super.onDraw(canvas);


    }
}