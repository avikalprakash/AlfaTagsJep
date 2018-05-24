package com.organization.jeptagsalpha.ui.controls;

import android.content.Context;
import android.util.AttributeSet;
import android.view.Gravity;
import android.widget.Button;

import com.organization.jeptagsalpha.R;

public class RedButton extends Button {

    public RedButton(Context context, AttributeSet attrs) {
        super(context, attrs);
        setProperty(context);
    }

    public RedButton(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        setProperty(context);
    }

    public void setProperty(Context context)
    {
       /* setNormalHeight(8);
        setPressedHeight(2);
        setCorners(12);
        setBackgroundColor(Color.parseColor("#F57F76"));
        setShadowColor(Color.parseColor("#b16262"));*/
        setBackground(getResources().getDrawable(R.drawable.rounded_button));
        setGravity(Gravity.CENTER);
        setTextColor(getResources().getColor(R.color.white));


    }

}
