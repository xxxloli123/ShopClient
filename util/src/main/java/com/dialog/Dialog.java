package com.dialog;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.StyleRes;
import android.support.v7.app.AppCompatDialog;
import android.view.Gravity;
import android.view.Window;
import android.view.WindowManager;

/**
 * Created by sgrape on 2017/4/29.
 */

public class Dialog extends AppCompatDialog {
    public Dialog(@NonNull Context context) {
        super(context);
    }

    public Dialog(@NonNull Context context, @StyleRes int themeResId) {
        super(context, themeResId);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Window win = getWindow();
        win.setBackgroundDrawable(new ColorDrawable(Color.parseColor("#00000000")));
        WindowManager.LayoutParams attrs = win.getAttributes();
        attrs.gravity = Gravity.CENTER;
        win.setAttributes(attrs);
    }

    @Override
    public void setContentView(@LayoutRes int layoutResID) {
        super.setContentView(layoutResID);
    }

    public void setLayout(int width, int height) {
        getWindow().setLayout(width, height);
        getWindow().getAttributes().gravity = Gravity.CENTER;
    }


    protected <T> T findView(@IdRes int id) {
        return (T) findViewById(id);
    }

    @Override
    public void dismiss() {
        if (isShowing())
            super.dismiss();
    }

    @Override
    public void show() {
        if (!isShowing())
            super.show();
    }
}
