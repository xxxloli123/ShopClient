package com.zsh.shopclient.fPresenter;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.zsh.shopclient.aPresenter.BaseActivity;
import com.zsh.shopclient.aView.BaseView;

/**
 * Created by Administrator on 2017/11/5.
 */

public abstract class BaseFragment<TypeView extends BaseView> extends Fragment {
    private TypeView typeView;
    protected abstract TypeView createTypeView();

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        typeView = createTypeView();
        typeView.onCreate((BaseActivity) getActivity(),container,savedInstanceState);
        typeView.initView();
        typeView.initWidget();
        return typeView.getRootView();
    }

    @Override
    public void onViewStateRestored(@Nullable Bundle savedInstanceState) {
        super.onViewStateRestored(savedInstanceState);
        if(null == typeView)
            typeView = createTypeView();
    }

    public TypeView getTypeView() {
        return typeView;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        typeView.onDestroy();
    }
}
