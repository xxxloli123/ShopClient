package com.zsh.shopclient.aView;

import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.xxx.skynet.sharedPreference.IOSharedPreferences;
import com.zsh.shopclient.R;
import com.zsh.shopclient.aPresenter.DeliveryAddressActivity;
import com.zsh.shopclient.adapter.TargetAdapter;
import com.zsh.shopclient.tool.RecyclerLimit;

import butterknife.BindView;

/**
 * Created by Administrator on 2017/12/6.
 */

public class DeliveryAddressV extends BaseView {
    @BindView(R.id.recycler)RecyclerView recycler;
    @Override
    protected int getLayoutId() {
        return R.layout.recycler;
    }

    @Override
    public void initView() {
        DeliveryAddressActivity activity = (DeliveryAddressActivity)getRootView().getContext();
        View.OnClickListener listener = activity.getClickListener();
        getRootView().findViewById(R.id.image_goBack).setOnClickListener(listener);
        ((TextView)getRootView().findViewById(R.id.text_title)).setText(R.string.shipingAddress);
        recycler.setLayoutManager(new LinearLayoutManager(activity,LinearLayoutManager.VERTICAL,false));
        recycler.addItemDecoration(new RecyclerLimit(activity, (byte)LinearLayout.HORIZONTAL));
        recycler.setItemAnimator(new DefaultItemAnimator());
        recycler.setAdapter(new TargetAdapter(activity, IOSharedPreferences.toArray(IOSharedPreferences.inStringSet(activity,activity.getString(R.string.article),activity.getString(R.string.addressee)))));
        getRootView().findViewById(R.id.text_addAddress).setOnClickListener(listener);
    }
}
