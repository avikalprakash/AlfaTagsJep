package com.organization.jeptagsalpha.ui.controls;

import android.content.Context;



import android.util.AttributeSet;

import android.view.LayoutInflater;
import android.widget.LinearLayout;


import com.organization.jeptagsalpha.R;

public class MyFormEdit extends LinearLayout {
    LayoutInflater mInflater;
    Context mContext ;
    public MyFormEdit(Context context) {
        super(context);
        mContext = context;
        setUpController();
    }


    private void setUpController() {
        mInflater = LayoutInflater.from(mContext);
        setShowDividers(SHOW_DIVIDER_MIDDLE);
        setDividerDrawable(getResources().getDrawable(R.drawable.form_empty_space));
    }

    public MyFormEdit(Context context, AttributeSet attrs) {
        super(context, attrs);
        mContext = context;
        setUpController();

    }

    public MyFormEdit(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mContext = context;
        setUpController();

    }
}
