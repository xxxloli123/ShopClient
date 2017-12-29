package com.zsh.shopclient.view;

import android.content.Context;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.widget.CheckBox;
import android.widget.Checkable;
import android.widget.LinearLayout;

import com.zsh.shopclient.R;

/**
 * Created by sgrape on 2017/6/7.
 * e-mail: sgrape1153@gmail.com
 */

public class CheckLinearLayout extends LinearLayout implements Checkable {

    private CheckBox checkBox;
    private boolean canCheck;

    public CheckLinearLayout(Context context) {
        this(context, null);
    }

    public CheckLinearLayout(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public CheckLinearLayout(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        canCheck = true;
    }

    @Override
    public void setChecked(boolean checked) {
        if (checkBox != null && canCheck)
            checkBox.setChecked(checked);
    }

    @Override
    public boolean isChecked() {
        return checkBox.isChecked();
    }

    @Override
    public void toggle() {
        if (checkBox != null && canCheck)
            checkBox.toggle();
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        checkBox = (CheckBox) findViewById(R.id.checkbox);
        checkBox.setClickable(false);
        checkBox.setFocusable(false);
        checkBox.setFocusableInTouchMode(false);
    }

    @Override
    public void setClickable(boolean clickable) {
        if (!clickable) super.setOnClickListener(null);
        super.setClickable(clickable);
    }

    public void setCanCheck(boolean canCheck) {
        this.canCheck = canCheck;
    }
}
