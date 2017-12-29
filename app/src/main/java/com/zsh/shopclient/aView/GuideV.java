package com.zsh.shopclient.aView;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.ImageView;

import com.zsh.shopclient.R;
import com.zsh.shopclient.adapter.ViewPagerAdapter;
import com.zsh.shopclient.aPresenter.BaseActivity;
import com.zsh.shopclient.aPresenter.GuideActivity;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2017/10/29.
 */

public class GuideV extends BaseView {
    @Override
    public void initView() {
        BaseActivity activity = (BaseActivity) getRootView().getContext();
        ViewPager pager = activity.findViewById(R.id.page_guide);
        pager.setAdapter(new ViewPagerAdapter(getData()));
        //((GuideActivity)activity).getPageListener(pager);//1.设置滑动监听.
        //((GuideActivity)activity).getOnTouchListener(pager);//2.滑倒最后滑进主页
    }
    public List<View> getData() {
        List<View>data = new ArrayList<>();
        Context context = getRootView().getContext();
        String resource = "android resource://"+context.getPackageName()+"/";
        int r[] = new int[3];
        r[0] = R.mipmap.guide0;
        r[1] = R.mipmap.guide1;
        r[2] = R.mipmap.guide2;
        for(int i = 0;i<r.length;i++) {
            ImageView image = new ImageView(context);
            image.setId(i);
            image.setBackgroundResource(r[i]);
            data.add(image);
        }
        data.get(data.size() -1).setOnClickListener(((GuideActivity)context).getClickListener(data.size() -1));
        return data;
    }
    @Override
    public int getLayoutId() {
        return R.layout.activity_guide;
    }
}
