package com.zsh.shopclient.fView;

import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.zsh.shopclient.R;
import com.zsh.shopclient.aPresenter.ShopActivity;
import com.zsh.shopclient.aView.*;
import com.zsh.shopclient.adapter.EvaluateAdapter;
import com.zsh.shopclient.fPresenter.CommentFragment;
import com.zsh.shopclient.tool.RecyclerLimit;

import java.util.List;

import butterknife.BindView;

/**
 * Created by Administrator on 2017/11/17.
 */

public class CommentV extends BaseView {
    @BindView(R.id.swipe_update)SwipeRefreshLayout refresh;
    @BindView(R.id.recycler)RecyclerView recycler;
    @BindView(R.id.text_noData)TextView noData;
    @Override
    protected int getLayoutId() {
        return R.layout.fragment_comment;
    }

    @Override
    public void initView() {
        ShopActivity activity = (ShopActivity)getRootView().getContext();
        refresh.setOnRefreshListener(((CommentFragment)((com.zsh.shopclient.aView.ShopV)activity.getTypeView()).getFragment(1)).getRefreshListener());
        recycler.setLayoutManager(new LinearLayoutManager(activity,LinearLayoutManager.VERTICAL,false));
        recycler.addItemDecoration(new RecyclerLimit(activity,(byte) LinearLayout.HORIZONTAL));
        recycler.setItemAnimator(new DefaultItemAnimator());
        EvaluateAdapter adapter = new EvaluateAdapter(activity,null);
        adapter.setDisplayOne();
        adapter.setLoadMoreListener(((CommentFragment)((com.zsh.shopclient.aView.ShopV)activity.getTypeView()).getFragment(1)).getLoadMoreListener());
        recycler.setAdapter(adapter);
    }
    public void update(final List<CharSequence[]>list,final boolean isReplace){
        ((ShopActivity)getRootView().getContext()).runOnUiThread(new Runnable() {
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
