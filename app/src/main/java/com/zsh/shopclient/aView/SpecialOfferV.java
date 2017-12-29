package com.zsh.shopclient.aView;

import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.zsh.shopclient.R;
import com.zsh.shopclient.aPresenter.SpecialOfferActivity;
import com.zsh.shopclient.adapter.CommodityAdapter;
import com.zsh.shopclient.adapter.TxtAdapter;
import com.zsh.shopclient.tool.RecyclerLimit;

import java.util.List;

import butterknife.BindView;

/**
 * Created by Administrator on 2017/12/13.
 */

public class SpecialOfferV extends BaseView {
    public static final int SHOPPINGcART = 0;
    @BindView(R.id.recycler_class)RecyclerView recyclerClass;
    @BindView(R.id.recycler)RecyclerView recycler;
    @BindView(R.id.text_noData)TextView noData;
    @Override
    protected int getLayoutId() {
        return R.layout.activity_special_offer;
    }

    @Override
    public void initView() {
        View.OnClickListener listener = ((SpecialOfferActivity)getRootView().getContext()).getClickListener();
        getRootView().findViewById(R.id.image_goBack).setOnClickListener(listener);
        ((TextView)getRootView().findViewById(R.id.text_title)).setText(R.string.everyDaySspecial);
        ImageView rightView = new ImageView(getRootView().getContext());
        rightView.setOnClickListener(listener);
        rightView.setId(SHOPPINGcART);
        rightView.setImageResource(R.mipmap.icon_shopping_cart_white);
        ((LinearLayout)getRootView().findViewById(R.id.linear_navi)).addView(rightView);
        recyclerClass.setLayoutManager(new LinearLayoutManager(getRootView().getContext(),LinearLayoutManager.VERTICAL,false));
        recyclerClass.addItemDecoration(new RecyclerLimit(getRootView().getContext(),(byte) LinearLayout.HORIZONTAL));
        recyclerClass.setItemAnimator(new DefaultItemAnimator());
        recycler.setLayoutManager(new LinearLayoutManager(getRootView().getContext(),LinearLayoutManager.VERTICAL,false));
        recycler.addItemDecoration(new RecyclerLimit(getRootView().getContext(),(byte) LinearLayout.HORIZONTAL));
        recycler.setItemAnimator(new DefaultItemAnimator());
        CommodityAdapter adapter = new CommodityAdapter((SpecialOfferActivity)getRootView().getContext(),null,true);
        adapter.setItemOnClickListener(((SpecialOfferActivity)getRootView().getContext()).getInfoListener());
        recycler.setAdapter(adapter);
    }
    public void updateRecyclerClass(final List<CharSequence[]>list){
        ((SpecialOfferActivity)getRootView().getContext()).runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if(null == getRootView())return;
                TxtAdapter adapter = new TxtAdapter((SpecialOfferActivity)getRootView().getContext(),list);
                recyclerClass.setAdapter(adapter);
                adapter.setItemOnClickListener(((SpecialOfferActivity)getRootView().getContext()).getItemClickListener());
                noData.setVisibility(0 == recyclerClass.getAdapter().getItemCount()? View.VISIBLE : View.GONE);
            }
        });
    }
    public void updateRecycler(final List<CharSequence[]>list){
        ((SpecialOfferActivity)getRootView().getContext()).runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if(null == getRootView())return;
                ((CommodityAdapter)recycler.getAdapter()).update(list,false);
                noData.setVisibility(0 == recycler.getAdapter().getItemCount()? View.VISIBLE : View.GONE);
            }
        });
    }
}
