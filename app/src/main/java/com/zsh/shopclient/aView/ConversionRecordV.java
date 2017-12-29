package com.zsh.shopclient.aView;

import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.zsh.shopclient.R;
import com.zsh.shopclient.aPresenter.ConversionRecordActivity;
import com.zsh.shopclient.adapter.UpImageBelowTextAdapter;
import com.zsh.shopclient.tool.RecyclerLimit;

import java.util.List;

import butterknife.BindView;

/**
 * Created by Administrator on 2017/12/15.
 */

public class ConversionRecordV extends BaseView {
    @BindView(R.id.swipe_update)SwipeRefreshLayout refresh;
    @BindView(R.id.recycler)RecyclerView recycler;
    @BindView(R.id.text_noData)TextView nodate;
    @Override
    protected int getLayoutId() {
        return R.layout.recycler;
    }

    @Override
    public void initView() {
        View.OnClickListener listener = ((ConversionRecordActivity)getRootView().getContext()).getClickListener();
        getRootView().findViewById(R.id.image_goBack).setOnClickListener(listener);
        ((TextView)getRootView().findViewById(R.id.text_title)).setText(R.string.conversionRecord);
        refresh.setOnRefreshListener(((ConversionRecordActivity)getRootView().getContext()).getRefreshListener());
        recycler.setLayoutManager(new GridLayoutManager(getRootView().getContext(),2,GridLayoutManager.VERTICAL,false));
        recycler.addItemDecoration(new RecyclerLimit(getRootView().getContext(),RecyclerLimit.LEVELvERTICAL));
        recycler.setItemAnimator(new DefaultItemAnimator());
        recycler.setAdapter(new UpImageBelowTextAdapter((ConversionRecordActivity)getRootView().getContext(),null));
    }
    public void update(final List<CharSequence[]> list){
        ((ConversionRecordActivity)getRootView().getContext()).runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if(null == getRootView())return;
                refresh.setRefreshing(false);
                ((UpImageBelowTextAdapter)recycler.getAdapter()).update(list);
                nodate.setVisibility(0>= recycler.getAdapter().getItemCount()? View.VISIBLE : View.GONE);
            }
        });
    }
}
