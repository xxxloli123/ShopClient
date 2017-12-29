package com.zsh.shopclient.aView;

import android.os.Bundle;
import android.support.annotation.StringRes;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.zsh.shopclient.R;
import com.zsh.shopclient.aPresenter.BaseActivity;

import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * Created by Administrator on 2017/10/29.
 */

public abstract class BaseView {
    public static final int X = -1;
    private View rootView;//获取activity view 方式1.activity.getWindow().getDecorView()方式2.activity.findViewById(android.R.id.content).getRootView()
    private Unbinder unbinder;
    protected abstract int getLayoutId();
    public abstract void initView();

    public void onCreate(BaseActivity activity, ViewGroup container, Bundle bundle) {
        if(null == rootView)
            rootView = LayoutInflater.from(activity).inflate(getLayoutId(),null,false);
        ViewGroup parent = (ViewGroup)rootView.getParent();
        if(null != parent)
            parent.removeView(rootView);
        unbinder = ButterKnife.bind(this,rootView);
    }
    public void hintHoast(@StringRes final int stringId, final String hint, final int duration){
        ((BaseActivity)getRootView().getContext()).runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if(null == getRootView())return;
                if(X != stringId)
                    Toast.makeText(getRootView().getContext(),getRootView().getContext().getString(stringId),duration).show();
                else if(null != hint)
                    Toast.makeText(getRootView().getContext(),hint,duration).show();
            }
        });
    }
    public void onDestroy(){
        unbinder.unbind();
    }
    public View getRootView(){return rootView;}
    public void initWidget(){}
}
