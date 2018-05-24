package com.organization.jeptagsalpha.ui.controls;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Typeface;
import android.support.annotation.NonNull;
import android.util.AttributeSet;

import com.organization.jeptagsalpha.MyConstants;
import com.organization.jeptagsalpha.R;
import com.rengwuxian.materialedittext.MaterialEditText;

public class MyEditTextLight2 extends MaterialEditText {

    private static final int INVALID_VALUE = -1;
    public MyEditTextLight2(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        setUpController(context, attrs);
    }

    public MyEditTextLight2(Context context, AttributeSet attrs)
    {
        super(context, attrs);
        setUpController(context, attrs);
    }
    public MyEditTextLight2(Context context) {
        super(context);
    }



    private void setUpController(Context context,AttributeSet attrs) {
        if (!isInEditMode()) {
            Typeface tf = Typeface.createFromAsset(getContext().getAssets(), MyConstants.FONT_REG_PATH);
            setTypeface(tf);
        }

       // TypedArray typedArray = context.obtainStyledAttributes(attrs,attrsArray);
        //setInputType(typedArray.getIndex(0));
      //  setSingleLine(true);
        setFocusFraction(1f);
        setFloatingLabel(FLOATING_LABEL_NORMAL);
        setFloatingLabelTextColor(getResources().getColor(R.color.edit_text_color));
        setBaseColor(getResources().getColor(R.color.edit_text_color));
        setTextColor(getResources().getColor(R.color.edit_text_color));
        setHintTextColor(getResources().getColor(R.color.edit_text_color));
        setPrimaryColor(getResources().getColor(R.color.colorLightAccent));
        setErrorColor(getResources().getColor(R.color.transparent));
        setHelperTextColor(getResources().getColor(R.color.textColorSecondry));
        setUnderlineColor(getResources().getColor(R.color.transparent));
        //setTextSize(MyConstants.convertDpToPixel(4));

    }

    @Override
    protected void onDraw(@NonNull Canvas canvas) {
        super.onDraw(canvas);


    }
}