package com.organization.jeptagsalpha.ui.controls;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.organization.jeptagsalpha.R;


public class MenuItem extends RelativeLayout{
    Context mContext;
    ImageView imageView;
    MyTextView myTextView;

    public MenuItem(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, R.style.MenuLayout);
        this.mContext = context;
        setUpController();
    }

    public MenuItem(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.mContext = context;
        setUpController();
    }

    public MenuItem(Context context) {
        super(context);
        this.mContext = context;
    }

    private void setUpController() {
        imageView = new ImageView(mContext,null,R.style.MenuLayoutImage);

    }




}
