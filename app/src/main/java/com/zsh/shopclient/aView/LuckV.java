package com.zsh.shopclient.aView;

import android.content.Intent;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.zsh.shopclient.R;
import com.zsh.shopclient.aPresenter.CommodityDetailsActivity;
import com.zsh.shopclient.aPresenter.LuckActivity;
import com.zsh.shopclient.adapter.TopImageBelowAdapter;
import com.zsh.shopclient.adapter.UpImageBelowTextAdapter;
import com.zsh.shopclient.tool.RecyclerLimit;

import java.util.List;

import butterknife.BindView;

/**
 * Created by Administrator on 2017/12/13.
 */

public class LuckV extends BaseView {
    @BindView(R.id.swipe_update)SwipeRefreshLayout refresh;
    @BindView(R.id.recycler)RecyclerView recycler;
    @BindView(R.id.text_noData)TextView nodate;
    @Override
    protected int getLayoutId() {
        return R.layout.recycler;
    }

    @Override
    public void initView() {
        View.OnClickListener listener = ((LuckActivity)getRootView().getContext()).getClickListener();
        getRootView().findViewById(R.id.image_goBack).setOnClickListener(listener);
        ((TextView)getRootView().findViewById(R.id.text_title)).setText(R.string.spellLuck);
        refresh.setOnRefreshListener(((LuckActivity)getRootView().getContext()).getRefreshListener());
        recycler.setLayoutManager(new GridLayoutManager(getRootView().getContext(),2,GridLayoutManager.VERTICAL,false));
        //recycler.addItemDecoration(new RecyclerLimit(getRootView().getContext(),RecyclerLimit.LEVELvERTICAL));
        recycler.setItemAnimator(new DefaultItemAnimator());
        recycler.setAdapter(new TopImageBelowAdapter((LuckActivity)getRootView().getContext(),null,0));
    }
    public void update(final List<CharSequence[]> list){
        ((LuckActivity)getRootView().getContext()).runOnUiThread(new Runnable() {
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
