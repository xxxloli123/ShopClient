package com.zsh.shopclient.aPresenter;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewPager;
import android.view.MotionEvent;
import android.view.View;

import com.xxx.skynet.sharedPreference.IOSharedPreferences;
import com.zsh.shopclient.R;
import com.zsh.shopclient.aView.BaseView;
import com.zsh.shopclient.aView.GuideV;

import static android.support.v4.view.ViewPager.SCROLL_STATE_SETTLING;

/**
 * Created by Administrator on 2017/10/29.
 */

public class GuideActivity extends BaseActivity {
    private float x;
    private int page;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if(IOSharedPreferences.inBool(this,getString(R.string.config),getString(R.string.first))){
            startActivity(new Intent(GuideActivity.this, LaunchActivity.class));
            finish();
        }
    }
    public View.OnClickListener getClickListener(final int id){
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int i = v.getId();
                if (i == id) {
                    startActivity(new Intent(GuideActivity.this, MainActivity.class));
                    finish();
                    IOSharedPreferences.outBool(GuideActivity.this,getString(R.string.config),getString(R.string.first),true);
                }
            }
        };
    }
    public void getPageListener(final ViewPager pager){
        pager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {}

            @Override
            public void onPageSelected(int position) {
                page = position;
            }

            @Override
            public void onPageScrollStateChanged(int state) {
                if(SCROLL_STATE_SETTLING == state)
                    page = pager.getChildCount();
            }
        });
    }
    public void getOnTouchListener(final ViewPager pager){
        pager.setOnTouchListener( new View.OnTouchListener(){
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()){
                    case MotionEvent.ACTION_DOWN:
                        x = event.getRawX();
                        break;
                    case MotionEvent.ACTION_UP:
                        if(page == pager.getChildCount() && x -32>event.getRawX()) {
                            startActivity(new Intent(GuideActivity.this, MainActivity.class));
                            finish();
                            IOSharedPreferences.outBool(GuideActivity.this,getString(R.string.config),getString(R.string.first),true);
                        }
                        break;
                }
                return false;
            }
        });
    }

    @Override
    protected BaseView createTypeView() {
        return new GuideV();
    }
}
