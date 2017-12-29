package com.zsh.shopclient.aPresenter;

import com.zsh.shopclient.aView.BaseView;
import com.zsh.shopclient.aView.RecommendV;

/**
 * Created by Administrator on 2017/11/13.
 */

public class RecommendActivity extends BaseActivity {
    @Override
    protected BaseView createTypeView() {
        return new RecommendV();
    }
}
