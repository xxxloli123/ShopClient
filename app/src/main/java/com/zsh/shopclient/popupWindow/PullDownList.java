package com.zsh.shopclient.popupWindow;

import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.PopupWindow;

import com.zsh.shopclient.R;
import com.zsh.shopclient.aPresenter.BaseActivity;
import com.zsh.shopclient.adapter.LeftIconRightTextAdapter;
import com.zsh.shopclient.adapter.TextAdapter;
import com.zsh.shopclient.tool.RecyclerLimit;

import java.util.List;

/**
 * Created by Administrator on 2017/12/19.
 */

public class PullDownList {
    private BaseActivity activity;
    private PopupWindow popupWindow;

    private void backgroundAlpha(final float alpha){
        WindowManager.LayoutParams lp = activity.getWindow().getAttributes();
        lp.alpha = alpha;
        activity.getWindow().setAttributes(lp);
    }
    public PullDownList(BaseActivity activity, List<CharSequence[]> list,TextAdapter.ItemOnClickListener itemOnClickListener) {
        this.activity = activity;
        View view = LayoutInflater.from(activity).inflate(R.layout.popupwindow_recycler,null);
        ((RecyclerView)view).setLayoutManager(new LinearLayoutManager(activity,LinearLayoutManager.VERTICAL,false));
        ((RecyclerView)view).setItemAnimator(new DefaultItemAnimator());
        ((RecyclerView)view).addItemDecoration(new RecyclerLimit(activity,(byte) LinearLayoutManager.HORIZONTAL));
        popupWindow = new PopupWindow(view, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT,true);
        popupWindow.setOutsideTouchable(true);//设置popwindow如果点击外面区域，便关闭。
        popupWindow.setTouchable(true);
        popupWindow.setBackgroundDrawable(new ColorDrawable(ContextCompat.getColor(activity,R.color.white)));//不设置背景点击外面没办法关闭
        popupWindow.setAnimationStyle(R.style.DrawerAlphaAnimation);
        popupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                dismissPopupWindow();
            }
        });
        ((RecyclerView)popupWindow.getContentView()).setAdapter(new LeftIconRightTextAdapter(activity,list));
        ((LeftIconRightTextAdapter)((RecyclerView)popupWindow.getContentView()).getAdapter()).setItemOnClickListener(itemOnClickListener);
    }
    public void setList(List<CharSequence[]>list){
        ((LeftIconRightTextAdapter)((RecyclerView)popupWindow.getContentView()).getAdapter()).update(list);
    }
    public void displayPopupWindow(View view){
        popupWindow.showAsDropDown(view,0, 0);
        //backgroundAlpha(0.6f);
    }
    public void dismissPopupWindow(){
        popupWindow.dismiss();
        //backgroundAlpha(1f);
    }
}
