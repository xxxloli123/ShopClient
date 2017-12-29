package com.zsh.shopclient.aPresenter;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;

import com.zsh.shopclient.R;
import com.zsh.shopclient.aView.AgreementV;
import com.zsh.shopclient.aView.BaseView;

/**
 * Created by Administrator on 2017/10/31.
 */

public class AgreementActivity extends BaseActivity {
    @Override
    protected BaseView createTypeView() {
        return new AgreementV();
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }
    public View.OnClickListener getClickListener(){
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (v.getId()){
                    case R.id.image_goBack:
                        finish();
                        break;
                }
            }
        };
    }
}
