package com.zsh.shopclient.aPresenter;

import android.content.Intent;
import android.view.View;

import com.zsh.shopclient.R;
import com.zsh.shopclient.aView.BaseView;
import com.zsh.shopclient.aView.DeliveryAddressV;

/**
 * Created by Administrator on 2017/12/6.
 */

public class DeliveryAddressActivity extends BaseActivity {
    public static final int DELIVERYaDDRESS = 1;
    @Override
    protected BaseView createTypeView() {
        return new DeliveryAddressV();
    }
    public View.OnClickListener getClickListener(){
        return new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                switch (v.getId()){
                    case R.id.image_goBack:
                        finish();
                        break;
                    case R.id.text_addAddress:
                        startActivityForResult(new Intent(DeliveryAddressActivity.this,AddressActivity.class),DELIVERYaDDRESS);
                        break;
                }
            }
        };
    }
}
