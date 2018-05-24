package com.organization.jeptagsalpha.ui.controls.Spinner;

import android.content.Context;
import android.util.AttributeSet;

import com.organization.jeptagsalpha.R;
import com.weiwangcn.betterspinner.library.material.MaterialBetterSpinner;


public class MySpinnerLight2 extends MaterialBetterSpinner {
    public MySpinnerLight2(Context arg0, AttributeSet arg1) {
        super(arg0, arg1);
        setUpController(arg0,arg1);
    }

    private void setUpController(Context arg0, AttributeSet arg1) {
        setFocusFraction(1f);
        setFloatingLabel(FLOATING_LABEL_NORMAL);
        setFloatingLabelTextColor(getResources().getColor(R.color.spinner_text_color));
        setBaseColor(getResources().getColor(R.color.spinner_text_color));
        setTextColor(getResources().getColor(R.color.spinner_text_color));
        setHintTextColor(getResources().getColor(R.color.spinner_text_color));
        setPrimaryColor(getResources().getColor(R.color.colorLightAccent));
        setErrorColor(getResources().getColor(R.color.colorLightAccent));
        setHelperTextColor(getResources().getColor(R.color.textColorSecondry));
        setUnderlineColor(getResources().getColor(R.color.transparent));
   //     setTextSize(MyConstants.convertDpToPixel(8));

    }

    public MySpinnerLight2(Context arg0, AttributeSet arg1, int arg2) {
        super(arg0, arg1, arg2);
        setUpController(arg0,arg1);
    }

    public MySpinnerLight2(Context context) {
        super(context);
    }


}
