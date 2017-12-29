package com.zsh.shopclient.fView;

import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.zsh.shopclient.R;
import com.zsh.shopclient.aPresenter.MainActivity;
import com.zsh.shopclient.aPresenter.MerchantActivity;
import com.zsh.shopclient.aView.BaseView;
import com.zsh.shopclient.aView.MainV;
import com.zsh.shopclient.adapter.RecommendShopsAdapter;
import com.zsh.shopclient.fPresenter.VipFragment;
import com.zsh.shopclient.tool.RecyclerLimit;

import java.util.List;
import java.util.Map;

import butterknife.BindView;

/**
 * Created by Administrator on 2017/11/9.
 */

public class VipV extends BaseView {
    @BindView(R.id.swipe_update)SwipeRefreshLayout refresh;
    @BindView(R.id.recycler)RecyclerView recycler;
    @BindView(R.id.text_noData)TextView nodate;
    @Override
    protected int getLayoutId() {
        return R.layout.recycler;
    }

    @Override
    public void initView() {
        getRootView().findViewById(R.id.image_goBack).setVisibility(View.GONE);
        ((TextView)getRootView().findViewById(R.id.text_title)).setText(R.string.vip);
        refresh.setOnRefreshListener(((VipFragment)((MainV)((MainActivity)getRootView().getContext()).getTypeView()).getIndexFragment(1)).getRefreshListener());
        recycler.setLayoutManager(new LinearLayoutManager(getRootView().getContext(),LinearLayoutManager.VERTICAL,false));
        recycler.setItemAnimator(new DefaultItemAnimator());
        recycler.addItemDecoration(new RecyclerLimit(getRootView().getContext(),(byte) LinearLayoutManager.HORIZONTAL));
        RecommendShopsAdapter adapter = new RecommendShopsAdapter((MainActivity)getRootView().getContext(),null);
        adapter.setFootLoadListener(((VipFragment)((MainV)(((MainActivity)getRootView().getContext()).getTypeView())).getIndexFragment(1)).getFootLoadListener());
        recycler.setAdapter(adapter);
    }
    public void update(final List<Map<Map<String,Object>,List<Map<String,Object>>>> list, final boolean isRefresh){
        ((MainActivity)getRootView().getContext()).runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if(null == getRootView())return;
                refresh.setRefreshing(false);
                ((RecommendShopsAdapter)recycler.getAdapter()).update(list,isRefresh);
                nodate.setVisibility(0>= recycler.getAdapter().getItemCount()? View.VISIBLE : View.GONE);
            }
        });
    }
}
