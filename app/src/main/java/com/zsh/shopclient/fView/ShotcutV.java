package com.zsh.shopclient.fView;

import android.content.Intent;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.zsh.shopclient.R;
import com.zsh.shopclient.aPresenter.MainActivity;
import com.zsh.shopclient.aPresenter.SpeedyActivity;
import com.zsh.shopclient.aView.BaseView;
import com.zsh.shopclient.aView.MainV;
import com.zsh.shopclient.adapter.UpImageBelowTextAdapter;
import com.zsh.shopclient.fPresenter.ShotcutFragment;

import butterknife.BindView;

/**
 * Created by Administrator on 2017/11/5.
 */

public class ShotcutV extends BaseView {
    @BindView(R.id.recycler)RecyclerView recycler;
    @Override
    protected int getLayoutId() {
        return R.layout.fragment_shotcut;
    }

    @Override
    public void initView() {
        MainActivity activity = (MainActivity) getRootView().getContext();
        recycler.setLayoutManager(new GridLayoutManager(getRootView().getContext(),4,GridLayoutManager.VERTICAL,false));
        recycler.setItemAnimator(new DefaultItemAnimator());
        recycler.setAdapter(new UpImageBelowTextAdapter(activity,((ShotcutFragment)((HomeV)((MainV)activity.getTypeView()).getIndexFragment(0).
                getTypeView()).getFragment(0)).getData()));
        ((UpImageBelowTextAdapter)recycler.getAdapter()).setListener(((ShotcutFragment)((HomeV)((MainV)activity.getTypeView()).getIndexFragment(0).
                getTypeView()).getFragment(0)).getClickListener());
    }
}
