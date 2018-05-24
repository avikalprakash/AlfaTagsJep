package com.organization.jeptagsalpha.ui.controls;



import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Typeface;
import android.support.annotation.NonNull;
import android.util.AttributeSet;

import com.rengwuxian.materialedittext.MaterialAutoCompleteTextView;
import com.organization.jeptagsalpha.MyConstants;
import com.organization.jeptagsalpha.R;

public class MyAutoCompleteTextView extends MaterialAutoCompleteTextView {

    private static final int INVALID_VALUE = -1;
    public MyAutoCompleteTextView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        setUpController(context, attrs);
    }

    public MyAutoCompleteTextView(Context context, AttributeSet attrs)
    {
        super(context, attrs);
        setUpController(context, attrs);
    }
    public MyAutoCompleteTextView(Context context) {
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
        setFloatingLabelTextColor(getResources().getColor(R.color.textColorLightPrimary));
        setBaseColor(getResources().getColor(R.color.textColorLightPrimary));
        setTextColor(getResources().getColor(R.color.textColorLightPrimary));
        setHintTextColor(getResources().getColor(R.color.textColorLightPrimary));
        setPrimaryColor(getResources().getColor(R.color.colorLightAccent));
        setErrorColor(getResources().getColor(R.color.colorLightAccent));
        setHelperTextColor(getResources().getColor(R.color.textColorLightPrimary));
        setUnderlineColor(getResources().getColor(R.color.textColorLightPrimary));
        //setTextSize(MyConstants.convertDpToPixel(4));

    }

    @Override
    protected void onDraw(@NonNull Canvas canvas) {
        super.onDraw(canvas);


    }
}
