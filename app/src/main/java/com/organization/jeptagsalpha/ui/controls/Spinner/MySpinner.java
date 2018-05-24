package com.organization.jeptagsalpha.ui.controls.Spinner;

import android.content.Context;
import android.util.AttributeSet;

import com.organization.jeptagsalpha.R;
import com.weiwangcn.betterspinner.library.material.MaterialBetterSpinner;


public class MySpinner extends MaterialBetterSpinner {
    public MySpinner(Context arg0, AttributeSet arg1) {
        super(arg0, arg1);
        setUpController(arg0,arg1);
    }

    private void setUpController(Context arg0, AttributeSet arg1) {
        setFocusFraction(1f);
        setFloatingLabel(FLOATING_LABEL_NORMAL);
        setPrimaryColor(getResources().getColor(R.color.accent));
        setErrorColor(getResources().getColor(R.color.accent));
        setBaseColor(getResources().getColor(R.color.textColorSecondry));
        setTextColor(getResources().getColor(R.color.textColorSecondry));
        setHelperTextColor(getResources().getColor(R.color.textColorSecondry));
        setFloatingLabelTextColor(getResources().getColor(R.color.textColorSecondry));
        setHintTextColor(getResources().getColor(R.color.textColorSecondry));
        setUnderlineColor(getResources().getColor(R.color.textColorSecondry));

       // setTextSize(MyConstants.convertDpToPixel(8));

    }

    public MySpinner(Context arg0, AttributeSet arg1, int arg2) {
        super(arg0, arg1, arg2);
        setUpController(arg0,arg1);
    }

    public MySpinner(Context context) {
        super(context);
    }

}
