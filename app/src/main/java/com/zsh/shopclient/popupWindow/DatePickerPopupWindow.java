package com.zsh.shopclient.popupWindow;

import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.support.v4.content.ContextCompat;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.DatePicker;
import android.widget.PopupWindow;
import android.widget.TimePicker;
import android.widget.Toast;

import com.zsh.shopclient.R;
import com.zsh.shopclient.aPresenter.BaseActivity;

import java.util.Date;
import java.util.GregorianCalendar;

/**
 * Created by Administrator on 2017/12/7.
 */

public class DatePickerPopupWindow {
    private BaseActivity activity;
    private PopupWindow popupWindow;
    private Sumbit sumbit;

    public DatePickerPopupWindow(final BaseActivity activity) {
        this.activity = activity;
        View view = LayoutInflater.from(activity).inflate(R.layout.popupwindow_date_picker,null);
        final DatePicker datePicker = view.findViewById(R.id.datePicker);
        //Calendar.getInstance().after(new GregorianCalendar(year, monthOfYear, dayOfMonth));
        final TimePicker timePicker = view.findViewById(R.id.timePicker);
        timePicker.setIs24HourView(true);
        view.findViewById(R.id.text_confirm).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (v.getId()){
                    case R.id.text_confirm:
                        if(null != sumbit) {
                            Date date = new Date(System.currentTimeMillis());
                            if(date.after(new GregorianCalendar(datePicker.getYear(),datePicker.getMonth(),datePicker.getDayOfMonth(),timePicker.getCurrentHour(),timePicker.getCurrentMinute()).getTime()))
                                Toast.makeText(activity,activity.getString(R.string.timeServiceError),Toast.LENGTH_SHORT).show();
                            else if (Build.VERSION.SDK_INT > Build.VERSION_CODES.LOLLIPOP_MR1)
                                sumbit.commit(datePicker.getYear(), datePicker.getMonth(), datePicker.getDayOfMonth(), timePicker.getHour(), timePicker.getMinute(), 0);
                            else
                                sumbit.commit(datePicker.getYear(), datePicker.getMonth(), datePicker.getDayOfMonth(), timePicker.getCurrentHour(), timePicker.getCurrentMinute(), 0);
                        }
                        break;
                }
            }
        });
        popupWindow = new PopupWindow(view, ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.WRAP_CONTENT,true);
        popupWindow.setOutsideTouchable(true);
        popupWindow.setTouchable(true);
        popupWindow.setBackgroundDrawable(new ColorDrawable(ContextCompat.getColor(activity, R.color.lucency)));
        popupWindow.setAnimationStyle(R.style.BottomAlphaAnimation);
        popupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                backgroundAlpha(1.0f);
            }
        });
    }
    private void backgroundAlpha(final float alpha){
        WindowManager.LayoutParams lp = activity.getWindow().getAttributes();
        lp.alpha = alpha;
        activity.getWindow().setAttributes(lp);
    }

    public void setSumbit(Sumbit sumbit) {
        this.sumbit = sumbit;
    }

    public void displayPopupWindow(){
        //Glide.with(activity).load(activity.getString(R.string.ip)+imagePaths.get(0)).into(image);
        //currentPrice.setText(info[]);
        backgroundAlpha(0.6f);
        popupWindow.showAtLocation(activity.findViewById(android.R.id.content), Gravity.BOTTOM,0,0);//Activity.getWindow.getDecorView()获得activity的rootView
    }
    public void dismiss(){
        popupWindow.dismiss();
    }
    public interface Sumbit{
        void commit(int year,int month,int day,int hour,int minute,int second);
    }
}
