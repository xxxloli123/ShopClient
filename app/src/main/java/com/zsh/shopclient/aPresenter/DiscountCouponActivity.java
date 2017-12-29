package com.zsh.shopclient.aPresenter;

import android.content.Intent;
import android.view.KeyEvent;
import android.view.View;

import com.zsh.shopclient.R;
import com.zsh.shopclient.aView.BaseView;
import com.zsh.shopclient.aView.DiscountCouponV;
import com.zsh.shopclient.adapter.TextAdapter;

/**
 * Created by Administrator on 2017/12/9.
 */

public class DiscountCouponActivity extends BaseActivity {
    @Override
    protected BaseView createTypeView() {
        return new DiscountCouponV();
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
    public TextAdapter.ItemOnClickListener getItemOnClickListener(){
        return new TextAdapter.ItemOnClickListener() {
            @Override
            public void itemOnClick(View view) {
                Intent intent = new Intent();
                intent.putExtra(getString(R.string.send),(CharSequence[]) view.getTag());
                setResult(RESULT_OK,intent);
                finish();
            }
        };
    }
}
