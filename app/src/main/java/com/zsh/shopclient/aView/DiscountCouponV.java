package com.zsh.shopclient.aView;

import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.TextView;

import com.zsh.shopclient.R;
import com.zsh.shopclient.aPresenter.DiscountCouponActivity;
import com.zsh.shopclient.adapter.DiscountCouponAdapter;
import com.zsh.shopclient.tool.RecyclerLimit;

import java.util.HashMap;
import java.util.List;

/**
 * Created by Administrator on 2017/12/9.
 */

public class DiscountCouponV extends BaseView {
    @Override
    protected int getLayoutId() {
        return R.layout.recycler;
    }

    @Override
    public void initView() {
        DiscountCouponActivity activity = (DiscountCouponActivity)getRootView().getContext();
        getRootView().findViewById(R.id.image_goBack).setOnClickListener(activity.getClickListener());
        ((TextView)getRootView().findViewById(R.id.text_title)).setText(R.string.merchantDiscountCoupon);
        getRootView().findViewById(R.id.swipe_update).setEnabled(false);
        RecyclerView recycler = getRootView().findViewById(R.id.recycler);
        recycler.setLayoutManager(new LinearLayoutManager(activity,LinearLayoutManager.VERTICAL,false));
        //recycler.addItemDecoration(new RecyclerLimit(activity,(byte) LinearLayout.HORIZONTAL));
        recycler.setItemAnimator(new DefaultItemAnimator());
        DiscountCouponAdapter adapter = new DiscountCouponAdapter(activity,((HashMap<List<CharSequence[]>,List<CharSequence[]>>)activity.getIntent().getExtras().
                get(activity.getString(R.string.send))).keySet().iterator().next());
        adapter.setOnItemClickListener(activity.getItemOnClickListener());
        recycler.setAdapter(adapter);
    }
}
