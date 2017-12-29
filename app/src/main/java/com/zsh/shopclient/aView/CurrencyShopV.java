package com.zsh.shopclient.aView;

import android.content.Intent;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.AbsoluteSizeSpan;
import android.text.style.ForegroundColorSpan;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.zsh.shopclient.R;
import com.zsh.shopclient.aPresenter.CommodityDetailsActivity;
import com.zsh.shopclient.aPresenter.CurrencyShopActivity;
import com.zsh.shopclient.adapter.UpImageBelowTextAdapter;
import com.zsh.shopclient.tool.RecyclerLimit;

import java.util.List;

import butterknife.BindView;

/**
 * Created by Administrator on 2017/12/13.
 */

public class CurrencyShopV extends BaseView {
    @BindView(R.id.frame_currency)FrameLayout currency;
    @BindView(R.id.swipe_update)SwipeRefreshLayout refresh;
    @BindView(R.id.recycler)RecyclerView recycler;
    @BindView(R.id.text_noData)TextView nodate;
    @Override
    protected int getLayoutId() {
        return R.layout.activity_currency_shop;
    }

    @Override
    public void initView() {
        View.OnClickListener listener = ((CurrencyShopActivity)getRootView().getContext()).getClickListener();
        getRootView().findViewById(R.id.image_goBack).setOnClickListener(listener);
        ((TextView)getRootView().findViewById(R.id.text_title)).setText(R.string.currencyShop);
        currency.setOnClickListener(listener);
        getRootView().findViewById(R.id.frame_record).setOnClickListener(listener);
        refresh.setOnRefreshListener(((CurrencyShopActivity)getRootView().getContext()).getRefreshListener());
        recycler.setLayoutManager(new GridLayoutManager(getRootView().getContext(),2,GridLayoutManager.VERTICAL,false));
        recycler.addItemDecoration(new RecyclerLimit(getRootView().getContext(),RecyclerLimit.LEVELvERTICAL));
        recycler.setItemAnimator(new DefaultItemAnimator());
        recycler.setAdapter(new UpImageBelowTextAdapter((CurrencyShopActivity)getRootView().getContext(),null));
    }
    public void setCurrency(final String sum){
        ((CurrencyShopActivity)getRootView().getContext()).runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if(null == getRootView())return;
                String string = getRootView().getContext().getString(R.string.gold);
                SpannableString ss = new SpannableString(string + sum);
                ss.setSpan(new ForegroundColorSpan(ContextCompat.getColor(getRootView().getContext(),R.color.scarlet)),string.length(),ss.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                ss.setSpan(new AbsoluteSizeSpan((int)getRootView().getResources().getDimension(R.dimen.size16)),string.length(),ss.length(),Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                ((TextView)currency.getChildAt(0)).setText(ss);
            }
        });
    }
    public void update(final List<CharSequence[]> list){
        ((CurrencyShopActivity)getRootView().getContext()).runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if(null == getRootView())return;
                refresh.setRefreshing(false);
                ((UpImageBelowTextAdapter)recycler.getAdapter()).update(list);
                ((UpImageBelowTextAdapter)recycler.getAdapter()).setIntent(new Intent(getRootView().getContext(), CommodityDetailsActivity.class));
                nodate.setVisibility(0>= recycler.getAdapter().getItemCount()? View.VISIBLE : View.GONE);
            }
        });
    }
}
