package com.zsh.shopclient.aPresenter;

import android.os.Bundle;
import android.support.annotation.Nullable;

import com.zsh.shopclient.R;
import com.zsh.shopclient.aView.BaseView;
import com.zsh.shopclient.aView.ListV;

import org.json.JSONObject;

/**
 * Created by Administrator on 2017/11/9.
 */

public class ShopListActivity extends BaseActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        String jsonStr = getIntent().getStringExtra(getString(R.string.send));
    }

    @Override
    protected BaseView createTypeView() {
        return new ListV();
    }
}
