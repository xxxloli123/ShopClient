package com.zsh.shopclient.popupWindow;

import android.graphics.drawable.ColorDrawable;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.PopupWindow;

import com.zsh.shopclient.R;
import com.zsh.shopclient.aPresenter.BaseActivity;
import com.zsh.shopclient.adapter.TextAdapter;
import com.zsh.shopclient.adapter.TxtAdapter;
import com.zsh.shopclient.tool.RecyclerLimit;

import java.util.List;

/**
 * Created by Administrator on 2017/12/10.
 */

public class AddressPopupWindow {
    private BaseActivity activity;
    private PopupWindow popupWindow;
    private void backgroundAlpha(final float alpha){
        WindowManager.LayoutParams lp = activity.getWindow().getAttributes();
        lp.alpha = alpha;
        activity.getWindow().setAttributes(lp);
    }
    public AddressPopupWindow(BaseActivity activity, final List<CharSequence[]>list,final AddressListener listener) {
        this.activity = activity;
        RecyclerView recycler = new RecyclerView(activity);
        recycler.setBackgroundResource(R.color.white);
        recycler.setLayoutManager(new LinearLayoutManager(activity,LinearLayoutManager.VERTICAL,false));
        recycler.addItemDecoration(new RecyclerLimit(activity,(byte) LinearLayout.HORIZONTAL));
        recycler.setItemAnimator(new DefaultItemAnimator());
        TxtAdapter adapter = new TxtAdapter(activity,list);
        adapter.setIndex(-1);
        adapter.setItemOnClickListener(new TextAdapter.ItemOnClickListener() {
            @Override
            public void itemOnClick(View view) {
                if(null != list)
                    listener.setAddress((CharSequence[]) view.getTag());
                popupWindow.dismiss();
            }
        });
        recycler.setAdapter(adapter);
        popupWindow = new PopupWindow(recycler, ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.WRAP_CONTENT,true);
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
    private int getMaxHeight(){//防止list过多设置popupWindow最大高度
        return 0;
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
    public interface AddressListener{
        void setAddress(CharSequence[] array);
    }
}
