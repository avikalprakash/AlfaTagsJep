package com.organization.jeptagsalpha.ui.controls;

import android.content.Context;
import android.content.res.TypedArray;
import android.support.design.widget.TextInputLayout;
import android.util.AttributeSet;
import android.view.View;
import android.widget.RelativeLayout;

import com.organization.jeptagsalpha.R;



public class MyTextInputLayout extends TextInputLayout {
    private static final String ANDROID_RES_NAMESPACE = "http://schemas.android.com/apk/res/android";
    private static final int INVALID_VALUE = -1;
    private int editTextInputType;
    private float editTextTextSize;
    MyEditText mMyEditText;
    Context mContext;
    public MyTextInputLayout(Context context) {
        super(context);
        mContext = context;

    }

    public MyTextInputLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        mContext = context;
        if (!isInEditMode()) {
            setUpEditTextIcon(attrs);
        }
    }

    public MyTextInputLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mContext = context;
        if (!isInEditMode()) {
            setUpEditTextIcon(attrs);
        }
    }

    public void setUpEditTextIcon(AttributeSet attrs)
    {
        editTextInputType = attrs.getAttributeIntValue(ANDROID_RES_NAMESPACE, "inputType", -1);
        TypedArray typedArray = getContext().obtainStyledAttributes(attrs, R.styleable.MyTextInputLayout);
        if (typedArray != null) {

        }

    }
    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        mMyEditText = new MyEditText(mContext);
        mMyEditText.setId(View.generateViewId());
        if (editTextInputType != -1) {
            mMyEditText.setInputType(editTextInputType);
        }
        mMyEditText.setSingleLine(true);
        mMyEditText.setTextSize(8);
        RelativeLayout.LayoutParams editTextParams = new RelativeLayout.LayoutParams(
                RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
        mMyEditText.setLayoutParams(editTextParams);
        addView(mMyEditText);

    }
    public String getText()
    {
        if(mMyEditText!= null)
        {
            return mMyEditText.getText().toString();
        }
        return "";
    }
    public void setText(String text)
    {
        if(mMyEditText!= null)
        {
            mMyEditText.setText(text);
        }
    }
    public MyEditText getMyEditText() {
        return mMyEditText;
    }

    public void setMyEditText(MyEditText mMyEditText) {
        this.mMyEditText = mMyEditText;
    }
}
