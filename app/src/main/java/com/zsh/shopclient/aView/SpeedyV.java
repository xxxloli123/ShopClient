package com.zsh.shopclient.aView;

import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.zsh.shopclient.R;
import com.zsh.shopclient.aPresenter.BaseActivity;
import com.zsh.shopclient.aPresenter.SpeedyActivity;
import com.zsh.shopclient.adapter.LeftIconRightTextAdapter;
import com.zsh.shopclient.adapter.ShopListAdapter;
import com.zsh.shopclient.adapter.TextAdapter;
import com.zsh.shopclient.tool.RecyclerLimit;

import java.util.LinkedHashMap;
import java.util.List;

import butterknife.BindView;

/**
 * Created by Administrator on 2017/11/13.
 */

public class SpeedyV extends BaseView {
    @BindView(R.id.recycler_class)RecyclerView rclass;
    @BindView(R.id.text_consumptionWay)TextView consumptionWay;
    @BindView(R.id.swipe_update)SwipeRefreshLayout update;
    @BindView(R.id.recycler)RecyclerView recycler;
    @BindView(R.id.text_noData)TextView noData;
    private PopupWindow popupWindow;
    private void backgroundAlpha(final float alpha){
        WindowManager.LayoutParams lp = ((BaseActivity)getRootView().getContext()).getWindow().getAttributes();
        lp.alpha = alpha;
        ((BaseActivity)getRootView().getContext()).getWindow().setAttributes(lp);
    }
    @Override
    protected int getLayoutId() {
        return R.layout.activity_speedy;
    }

    @Override
    public void initView() {
        SpeedyActivity activity = (SpeedyActivity)getRootView().getContext();
        View.OnClickListener listener = activity.getListener();
        getRootView().findViewById(R.id.image_goBack).setOnClickListener(listener);
        consumptionWay.setOnClickListener(listener);
        CharSequence[] charSequence = activity.getIntent().getCharSequenceArrayExtra(activity.getString(R.string.title));
        ((TextView)getRootView().findViewById(R.id.text_title)).setText(charSequence[1]);
        /*ImageView rightView = new ImageView(activity);
        rightView.setImageResource(R.mipmap.icon_search);
        ((LinearLayout)activity.findViewById(R.id.linear_navi)).addView(rightView);*/
        recycler.setLayoutManager(new LinearLayoutManager(getRootView().getContext(),LinearLayoutManager.VERTICAL,false));
        recycler.addItemDecoration(new RecyclerLimit(activity,(byte) LinearLayoutManager.HORIZONTAL));
        recycler.setItemAnimator(new DefaultItemAnimator());
        update.setOnRefreshListener(activity.getUpdateListener());
    }
    public void updateFinish(){
        update.setRefreshing(false);
    }
    @Override
    public void initWidget() {
        super.initWidget();
        SpeedyActivity activity = (SpeedyActivity) getRootView().getContext();
        View view = LayoutInflater.from(activity).inflate(R.layout.popupwindow_recycler,null);
        ((RecyclerView)view).setLayoutManager(new LinearLayoutManager(activity,LinearLayoutManager.VERTICAL,false));
        ((RecyclerView)view).setItemAnimator(new DefaultItemAnimator());
        ((RecyclerView)view).addItemDecoration(new RecyclerLimit(activity,(byte) LinearLayoutManager.HORIZONTAL));
        popupWindow = new PopupWindow(view,ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT,true);
        popupWindow.setOutsideTouchable(true);//设置popwindow如果点击外面区域，便关闭。
        popupWindow.setTouchable(true);
        popupWindow.setBackgroundDrawable(new ColorDrawable(ContextCompat.getColor(activity,R.color.white)));//不设置背景点击外面没办法关闭
        popupWindow.setAnimationStyle(R.style.DrawerAlphaAnimation);
        popupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                backgroundAlpha(1f);
            }
        });
        ((RecyclerView)popupWindow.getContentView()).setAdapter(new LeftIconRightTextAdapter(activity,activity.initRecyclerWay()));
        ((LeftIconRightTextAdapter)((RecyclerView)popupWindow.getContentView()).getAdapter()).setItemOnClickListener(activity.getWayItemListener());
    }
    public void initClassList(final List<CharSequence[]> list){
        ((SpeedyActivity)getRootView().getContext()).runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if(null == rclass)
                    return;
                rclass.setLayoutManager(new LinearLayoutManager(getRootView().getContext(),LinearLayoutManager.HORIZONTAL,false));
                rclass.setItemAnimator(new DefaultItemAnimator());
                rclass.setAdapter(new TextAdapter((SpeedyActivity) getRootView().getContext(),list));
                ((TextAdapter)rclass.getAdapter()).setItemOnClickListener(((SpeedyActivity)getRootView().getContext()).getClassItemListener());
            }
        });
    }
    public void pitchOn(int index){
        consumptionWay.setTextColor(ContextCompat.getColor(getRootView().getContext(),R.color.scarlet));
        Drawable drawable = ContextCompat.getDrawable(getRootView().getContext(),R.mipmap.icon_triangle_up_red);
        drawable.setBounds(0,0,drawable.getMinimumWidth(),drawable.getMinimumHeight());
        consumptionWay.setCompoundDrawables(null,null,drawable,null);
        displayPopupWindow();
    }
    public void displayPopupWindow(){
        popupWindow.showAsDropDown(consumptionWay,0, (int)getRootView().getContext().getResources().getDimension(R.dimen.margin8));
        backgroundAlpha(0.6f);
    }
    public void dismissPopupWindow(List<CharSequence[]>list,LinkedHashMap<String,List<CharSequence[]>>listLinkedHashMap){
        consumptionWay.setTextColor(ContextCompat.getColor(getRootView().getContext(),R.color.black));
        Drawable drawable = ContextCompat.getDrawable(getRootView().getContext(),R.mipmap.icon_triangle_down_black);
        drawable.setBounds(0,0,drawable.getMinimumWidth(),drawable.getMinimumHeight());
        consumptionWay.setCompoundDrawables(null,null,drawable,null);
        popupWindow.dismiss();
        backgroundAlpha(1f);
        ((ShopListAdapter)recycler.getAdapter()).update(list,listLinkedHashMap);
        recycler.setVisibility(0 == recycler.getAdapter().getItemCount()? View.GONE : View.VISIBLE);
        noData.setVisibility(0 == recycler.getAdapter().getItemCount() ? View.VISIBLE : View.GONE);
    }
    public void update(final List<CharSequence[]>list, final LinkedHashMap<String, List<CharSequence[]>> listLinkedHashMap){
        ((SpeedyActivity)getRootView().getContext()).runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if(null == update) return;
                update.setRefreshing(false);
                if (null == recycler.getAdapter())
                    recycler.setAdapter(new ShopListAdapter((BaseActivity) getRootView().getContext(), list, listLinkedHashMap));
                else
                    ((ShopListAdapter)recycler.getAdapter()).update(list, listLinkedHashMap);
                recycler.setVisibility(0 == recycler.getAdapter().getItemCount()? View.GONE : View.VISIBLE);
                noData.setVisibility(0 == recycler.getAdapter().getItemCount() ? View.VISIBLE : View.GONE);
            }
        });
    }
    public TextAdapter getRclassAdapter(){
        return (TextAdapter)rclass.getAdapter();
    }
}
