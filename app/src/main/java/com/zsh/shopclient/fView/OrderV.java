package com.zsh.shopclient.fView;

import android.graphics.drawable.Drawable;
import android.support.annotation.ColorRes;
import android.support.annotation.DrawableRes;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.zsh.shopclient.R;
import com.zsh.shopclient.aPresenter.BaseActivity;
import com.zsh.shopclient.aPresenter.MainActivity;
import com.zsh.shopclient.aView.BaseView;
import com.zsh.shopclient.aView.MainV;
import com.zsh.shopclient.adapter.OrderAdapter;
import com.zsh.shopclient.fPresenter.OrderFragment;
import com.zsh.shopclient.tool.RecyclerLimit;

import java.util.List;

import butterknife.BindView;

/**
 * Created by Administrator on 2017/12/13.
 */

public class OrderV extends BaseView {
    @BindView(R.id.text_all)TextView all;
    @BindView(R.id.text_commodityOrder)TextView commodityOrder;
    @BindView(R.id.swipe_update)SwipeRefreshLayout refresh;
    @BindView(R.id.recycler)RecyclerView recycler;
    @BindView(R.id.text_noData)TextView noData;
    @Override
    protected int getLayoutId() {
        return R.layout.fragment_order;
    }

    @Override
    public void initView() {
        View.OnClickListener listener = ((OrderFragment)((MainV)((MainActivity)getRootView().getContext()).getTypeView()).getIndexFragment(3)).getClickListener();
        getRootView().findViewById(R.id.image_goBack).setVisibility(View.GONE);
        ((TextView)getRootView().findViewById(R.id.text_title)).setText(R.string.order);
        getRootView().findViewById(R.id.text_all).setOnClickListener(listener);
        getRootView().findViewById(R.id.text_commodityOrder).setOnClickListener(listener);
        refresh.setOnRefreshListener(((OrderFragment)((MainV)((MainActivity)getRootView().getContext()).getTypeView()).getIndexFragment(3)).getUpdateListener());
        recycler.setLayoutManager(new LinearLayoutManager(getRootView().getContext(),LinearLayoutManager.VERTICAL,false));
        recycler.addItemDecoration(new RecyclerLimit(getRootView().getContext(),(byte) LinearLayout.HORIZONTAL));
        recycler.setItemAnimator(new DefaultItemAnimator());
        OrderAdapter adapter = new OrderAdapter((BaseActivity)getRootView().getContext(),null,listener);
        adapter.setFootLoadListener(((OrderFragment)((MainV)((MainActivity)getRootView().getContext()).getTypeView()).getIndexFragment(3)).getFootLoadListener());
        recycler.setAdapter(adapter);
    }
    public void setAllText(String text,List list){
        all.setText(text);
        viewChangeColor(all,1,R.color.textDarkgray,R.mipmap.icon_triangle_down_black);
        ((OrderAdapter)recycler.getAdapter()).update(list,false);
        noData.setVisibility(0>= recycler.getAdapter().getItemCount()? View.VISIBLE:View.GONE);
    }
    public void setCommodityOrderText(String text,List list){
        commodityOrder.setText(text);
        viewChangeColor(commodityOrder,1,R.color.textDarkgray,R.mipmap.icon_triangle_down_black);
        ((OrderAdapter)recycler.getAdapter()).update(list,false);
        noData.setVisibility(0>= recycler.getAdapter().getItemCount()? View.VISIBLE:View.GONE);
    }
    public void viewChangeColor(View view,float alpha, @ColorRes int colorId, @DrawableRes int mipmapId){
        ((TextView)view).setTextColor(ContextCompat.getColor(getRootView().getContext(),colorId));
        Drawable drawable = ContextCompat.getDrawable(getRootView().getContext(),mipmapId);
        drawable.setBounds(0,0,drawable.getMinimumWidth(),drawable.getMinimumHeight());
        ((TextView)view).setCompoundDrawables(null,null,drawable,null);
        refresh.setAlpha(alpha);
    }
    public void updateFinish(){
        refresh.setRefreshing(false);
    }
    public void updateRecycler(final List list,final boolean end){
        ((MainActivity)getRootView().getContext()).runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if(null == getRootView())return;
                ((OrderAdapter)recycler.getAdapter()).update(list,end);
                noData.setVisibility(0>= recycler.getAdapter().getItemCount()? View.VISIBLE:View.GONE);
            }
        });
    }
    public String getAllText(){
        String allStr = getRootView().getContext().getString(R.string.all);
        return allStr.equals(all.getText().toString().trim()) ? "" : all.getText().toString().trim();
    }
    public String getCommodityOrderText(){
        return commodityOrder.getText().toString().trim();
    }
}
