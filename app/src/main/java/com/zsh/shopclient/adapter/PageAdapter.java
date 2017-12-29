package com.zsh.shopclient.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.zsh.shopclient.fPresenter.BaseFragment;

import java.util.List;

/**
 * Created by Administrator on 2017/11/5.
 */

public class PageAdapter extends FragmentPagerAdapter {
    private List<BaseFragment> fragments;
    private CharSequence[] titles;
    public PageAdapter(FragmentManager fm, List<BaseFragment> fragments) {
        super(fm);
        this.fragments = fragments;
    }

    public PageAdapter(FragmentManager fm, List<BaseFragment> fragments, CharSequence... titles) {
        super(fm);
        this.fragments = fragments;
        this.titles = titles;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return null == titles ? "":titles[position];
    }

    @Override
    public Fragment getItem(int position) {
        return null == fragments ? null : fragments.get(position);
    }

    @Override
    public int getCount() {
        return null == fragments ?0: fragments.size();
    }
}
