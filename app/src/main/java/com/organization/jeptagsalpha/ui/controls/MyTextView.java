package com.organization.jeptagsalpha.ui.controls;

import android.content.Context;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.widget.TextView;

import com.organization.jeptagsalpha.MyConstants;


public class MyTextView extends TextView {
    public static final String ANDROID_SCHEMA = "http://schemas.android.com/apk/res/android";
    public MyTextView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(context,attrs);
    }

    public MyTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context,attrs);
    }

    public MyTextView(Context context) {
        super(context);
        init(context,null);
    }

    private void init(Context context, AttributeSet attrs) {
        if (!isInEditMode()) {
            Typeface tf = Typeface.createFromAsset(getContext().getAssets(), MyConstants.FONT_REG_PATH);
            if(attrs != null) {
                int textStyle = attrs.getAttributeIntValue(ANDROID_SCHEMA, "textStyle", Typeface.NORMAL);
                tf = selectTypeface(context, textStyle);
            }
            setTypeface(tf);
        }
    }
    private Typeface selectTypeface(Context context, int textStyle) {
        /*
        * information about the TextView textStyle:
        * http://developer.android.com/reference/android/R.styleable.html#TextView_textStyle
        */
        switch (textStyle) {
            case Typeface.BOLD: // bold
                return Typeface.createFromAsset(getContext().getAssets(), MyConstants.FONT_BOLD_PATH);


            case Typeface.NORMAL: // regular
            default:
                return Typeface.createFromAsset(getContext().getAssets(), MyConstants.FONT_REG_PATH);
        }
    }
}