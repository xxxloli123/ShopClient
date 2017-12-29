package com.zsh.shopclient.aView;

import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.zsh.shopclient.R;
import com.zsh.shopclient.aPresenter.EvaluateActivity;
import com.zsh.shopclient.adapter.EvaluateAdapter;
import com.zsh.shopclient.tool.RecyclerLimit;

import java.util.List;

import butterknife.BindView;

/**
 * Created by Administrator on 2017/12/6.
 */

public class EvaluateV extends BaseView {
    @BindView(R.id.swipe_update)SwipeRefreshLayout refresh;
    @BindView(R.id.recycler)RecyclerView recycler;
    @BindView(R.id.text_noData)TextView noData;
    @Override
    protected int getLayoutId() {
        return R.layout.recycler;
    }

    @Override
    public void initView() {
        EvaluateActivity activity = (EvaluateActivity)getRootView().getContext();
        getRootView().findViewById(R.id.image_goBack).setOnClickListener(activity.getClickListener());
        ((TextView)getRootView().findViewById(R.id.text_title)).setText(R.string.evaluate);
        refresh.setOnRefreshListener(activity.getRefreshListener());
        recycler.setLayoutManager(new LinearLayoutManager(activity,LinearLayoutManager.VERTICAL,false));
        recycler.addItemDecoration(new RecyclerLimit(activity,(byte) LinearLayout.HORIZONTAL));
        recycler.setItemAnimator(new DefaultItemAnimator());
        EvaluateAdapter adapter = new EvaluateAdapter(activity,null);
        adapter.setDisplayOne();
        adapter.setLoadMoreListener(activity.getLoadMoreListener());
        recycler.setAdapter(adapter);
    }
    public void update(final List<CharSequence[]> list, final boolean isReplace){
        ((EvaluateActivity)getRootView().getContext()).runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if(null == recycler)
                    return;
                ((EvaluateAdapter)recycler.getAdapter()).update(list,isReplace);
                if(isReplace)
                    refresh.setRefreshing(false);
                noData.setVisibility(0< recycler.getAdapter().getItemCount()? View.GONE : View.VISIBLE);
            }
        });
    }
}
