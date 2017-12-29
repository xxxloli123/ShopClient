package com.zsh.shopclient.aView;

import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.zsh.shopclient.R;
import com.zsh.shopclient.aPresenter.ShopActivity;
import com.zsh.shopclient.adapter.PageAdapter;
import com.zsh.shopclient.fPresenter.BaseFragment;
import com.zsh.shopclient.fPresenter.CommentFragment;
import com.zsh.shopclient.fPresenter.ShopFragment;
import com.zsh.shopclient.widget.PagerSlidingTabStrip;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

/**
 * Created by Administrator on 2017/11/6.
 */

public class ShopV extends BaseView {
    @BindView(R.id.image_becomeMember)ImageView becomeMember;
    @BindView(R.id.text_support)TextView support;
    @BindView(R.id.pager_tabs) PagerSlidingTabStrip tabs;
    @BindView(R.id.addr_pager)ViewPager pager;
    private PageAdapter pagerAdapter;
    private List<BaseFragment> list;
    @Override
    protected int getLayoutId() {
        return R.layout.activity_shop;
    }

    @Override
    public void initView() {
        ShopActivity activity = (ShopActivity) getRootView().getContext();
        CharSequence[] charSequences =  activity.getIntent().getCharSequenceArrayExtra(activity.getString(R.string.title));
        TextView title = getRootView().findViewById(R.id.text_title);
        title.setText(charSequences[1]);
        View.OnClickListener listener = activity.getClickListener();
        getRootView().findViewById(R.id.image_goBack).setOnClickListener(listener);
        title.setOnClickListener(listener);
        getRootView().findViewById(R.id.image_search).setOnClickListener(listener);
        getRootView().findViewById(R.id.image_share).setOnClickListener(listener);
        getRootView().findViewById(R.id.image_becomeMember).setOnClickListener(listener);
        getRootView().findViewById(R.id.image_lineProject).setOnClickListener(listener);
        support.setText(("yes".equals(charSequences[2])? activity.getString(R.string.toShop) +"\t":"")+("yes".equals(charSequences[3])? activity.getString(R.string.homeDelivery):""));
        support.setOnClickListener(listener);
        list = new ArrayList<>();
        list.add(new ShopFragment());
        list.add(new CommentFragment());
        String minute = (3< charSequences.length ? "("+charSequences[5]+")":"") +activity.getString(R.string.minute);
        pagerAdapter = new PageAdapter(activity.getSupportFragmentManager(),list,activity.getString(R.string.shop),activity.getString(R.string.evaluate)+ minute);
        pager.setAdapter(pagerAdapter);
        tabs.setViewPager(pager);
        pager.setCurrentItem(0);
    }
    public void setBecomeMember(){
        ((ShopActivity)getRootView().getContext()).runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if(null == becomeMember)return;
            }
        });
    }
    public void updateSupport(final CharSequence[] charSequence){
        final ShopActivity activity = (ShopActivity)getRootView().getContext();
        activity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if(null != getRootView())
                    support.setText(("yes".equals(charSequence[7])? activity.getString(R.string.toShop)+"\t":"")+("yes".equals(charSequence[8])? activity.getString(R.string.homeDelivery)+"\t":""));
            }
        });
    }
    public BaseFragment getFragment(int index){
        return list.get(index);
    }
}
