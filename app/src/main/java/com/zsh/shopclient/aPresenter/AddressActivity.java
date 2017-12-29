package com.zsh.shopclient.aPresenter;

import com.zsh.shopclient.aView.BaseView;
import com.zsh.shopclient.aView.AddressV;

/**
 * Created by Administrator on 2017/12/6.
 */

public class AddressActivity extends BaseActivity {
    @Override
    protected BaseView createTypeView() {
        return new AddressV();
    }
}
