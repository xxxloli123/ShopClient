package com.zsh.shopclient.fView;

import android.content.Intent;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.zsh.shopclient.R;
import com.zsh.shopclient.aPresenter.CommodityParticularsActivity;
import com.zsh.shopclient.aPresenter.MainActivity;
import com.zsh.shopclient.aView.BaseView;
import com.zsh.shopclient.aView.MainV;
import com.zsh.shopclient.adapter.TopImageBelowAdapter;
import com.zsh.shopclient.adapter.UpImageBelowTextAdapter;
import com.zsh.shopclient.fPresenter.DiscoverFragment;

import java.util.List;

import butterknife.BindView;

/**
 * Created by Administrator on 2017/12/17.
 */

public class DiscoverV extends BaseView {
    @BindView(R.id.recycler_specialOffer)RecyclerView specialOffer;
    @BindView(R.id.recycler_flashSale)RecyclerView flashSale;
    @Override
    protected int getLayoutId() {
        return R.layout.fragment_discover;
    }

    @Override
    public void initView() {
        View.OnClickListener listener = ((DiscoverFragment)((MainV)((MainActivity)getRootView().getContext()).getTypeView()).getIndexFragment(2)).getClickListener();
        getRootView().findViewById(R.id.image_goBack).setVisibility(View.GONE);
        ((TextView)getRootView().findViewById(R.id.text_title)).setText(R.string.discover);
        getRootView().findViewById(R.id.image_currencyShop).setOnClickListener(listener);
        getRootView().findViewById(R.id.image_referralBonuses).setOnClickListener(listener);
        getRootView().findViewById(R.id.text_everyDaySspecial).setOnClickListener(listener);
        getRootView().findViewById(R.id.text_flashSale).setOnClickListener(listener);
    }
    public void initSpecialOffer(final MainActivity activity, final List<CharSequence[]> list){
        activity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if(null == getRootView())return;
                UpImageBelowTextAdapter flashSaleAdapter = new TopImageBelowAdapter(activity,list,R.layout.item_image_text1);
                flashSaleAdapter.setIntent(new Intent(getRootView().getContext(), CommodityParticularsActivity.class));
                specialOffer.setLayoutManager(new LinearLayoutManager(activity,LinearLayoutManager.HORIZONTAL,false));
                specialOffer.setItemAnimator(new DefaultItemAnimator());
                specialOffer.setAdapter(flashSaleAdapter);
            }
        });
    }
    public void initFlashSale(final MainActivity activity, final List<CharSequence[]> list){
        activity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if(null == getRootView())return;
                UpImageBelowTextAdapter flashSaleAdapter = new TopImageBelowAdapter(activity,list,R.layout.item_image_text1);
                flashSaleAdapter.setIntent(new Intent(getRootView().getContext(), CommodityParticularsActivity.class));
                flashSale.setLayoutManager(new LinearLayoutManager(activity,LinearLayoutManager.HORIZONTAL,false));
                flashSale.setItemAnimator(new DefaultItemAnimator());
                flashSale.setAdapter(flashSaleAdapter);
            }
        });
    }
}
