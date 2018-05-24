package com.organization.jeptagsalpha.ui.controls;

import android.content.Context;
import android.util.AttributeSet;
import android.view.Gravity;
import android.widget.Button;

import com.organization.jeptagsalpha.R;



public class BlueButton extends Button {

    public BlueButton(Context context, AttributeSet attrs) {
        super(context, attrs);
        setProperty(context);
    }

    public BlueButton(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        setProperty(context);
    }

    public void setProperty(Context context)
    {
       /* setNormalHeight(8);
        setPressedHeight(2);
        setCorners(12);
        setBackgroundColor(Color.parseColor("#5474b8"));
        setShadowColor(Color.parseColor("#3b5998"));*/
        setBackground(getResources().getDrawable(R.drawable.rounded_button));
        setGravity(Gravity.CENTER);
        setTextColor(getResources().getColor(R.color.white));

    }


}
