package com.zsh.shopclient.adapter;

import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

/**
 * Created by Administrator on 2017/10/29.
 */

public class ViewPagerAdapter extends PagerAdapter {
    private List<View> datas;

    public ViewPagerAdapter(List<View> datas) {
        this.datas = datas;
    }

    @Override
    public int getCount() {
        return null == datas ? 0 : datas.size();
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        container.addView(datas.get(position),0);
        return datas.get(position);
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        //super.destroyItem(container, position, object);
        container.removeView(datas.get(position));
    }

}
