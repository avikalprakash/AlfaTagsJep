package com.organization.jeptagsalpha.ui.controls;

import android.content.Context;
import android.support.design.widget.TextInputLayout;
import android.util.AttributeSet;

/**
 * Created by Lord Ganesha on 15/08/2016.
 */

public class EditTextIcon extends TextInputLayout {
    public EditTextIcon(Context context) {
        super(context);
        setUpEditTextIcon();
    }

    public EditTextIcon(Context context, AttributeSet attrs) {
        super(context, attrs);
        setUpEditTextIcon();
    }

    public EditTextIcon(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        setUpEditTextIcon();
    }

    public void setUpEditTextIcon()
    {
    }

  /*  @Override
    protected void onFinishInflate() {

        super.onFinishInflate();
        Typeface tf = Typeface.createFromAsset(getContext().getAssets(), MyConstants.FONT_BOLD_PATH);
        label.setTypeface(tf);
        label.setPadding(0,10,0,10);
        label.setTextColor(Color.parseColor("#FFFFFF"));

        expand();
        this.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
            }
        });

    }*/
}
