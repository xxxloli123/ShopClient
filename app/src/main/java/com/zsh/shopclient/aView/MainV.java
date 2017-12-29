package com.zsh.shopclient.aView;

import android.support.v4.app.FragmentTransaction;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.zsh.shopclient.R;
import com.zsh.shopclient.aPresenter.MainActivity;
import com.zsh.shopclient.fPresenter.BaseFragment;
import com.zsh.shopclient.fPresenter.DiscoverFragment;
import com.zsh.shopclient.fPresenter.HomeFragment;
import com.zsh.shopclient.fPresenter.MyFragment;
import com.zsh.shopclient.fPresenter.OrderFragment;
import com.zsh.shopclient.fPresenter.VipFragment;

import butterknife.BindView;

/**
 * Created by Administrator on 2017/10/31.
 * android （MD）v7.widget.SearchView的使用解析 https://zhuanlan.zhihu.com/p/22388833?refer=qyddai
 */

public class MainV extends BaseView {
    @BindView(R.id.group)RadioGroup group;
    private BaseFragment[] fragments;
    private BaseFragment displayFragment;
    @Override
    public void initView() {
        fragments = new BaseFragment[5];
        fragments[0] = new HomeFragment();
        fragments[1] = new VipFragment();
        fragments[2] = new DiscoverFragment();
        fragments[3] = new OrderFragment();
        fragments[4] = new MyFragment();
        group.setOnCheckedChangeListener(((MainActivity)getRootView().getContext()).getOnCheckedChangeListener());
        ((RadioButton)group.getChildAt(0)).setChecked(true);
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_main;
    }
    public void setFragment(int index){
        FragmentTransaction transaction = ((MainActivity)getRootView().getContext()).getSupportFragmentManager().beginTransaction();
        if(null != displayFragment)
            transaction.hide(displayFragment);
        if(!fragments[index].isAdded())
            transaction.add(R.id.frame,fragments[index]);
        displayFragment = fragments[index];
        transaction.show(fragments[index]).commit();
    }
    public BaseFragment getIndexFragment(int index){
        return fragments[index];
    }
}
