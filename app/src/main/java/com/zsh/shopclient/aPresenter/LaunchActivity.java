package com.zsh.shopclient.aPresenter;

import com.zsh.shopclient.aView.LaunchV;
import com.zsh.shopclient.aView.BaseView;

/**
 * Created by Administrator on 2017/11/7.
 */

public class LaunchActivity extends BaseActivity {
    @Override
    protected BaseView createTypeView() {
        return new LaunchV();
    }
}
