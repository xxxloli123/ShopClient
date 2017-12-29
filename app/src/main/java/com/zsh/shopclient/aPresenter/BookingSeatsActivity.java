package com.zsh.shopclient.aPresenter;

import android.content.Intent;
import android.view.View;

import com.zsh.shopclient.R;
import com.zsh.shopclient.aView.BaseView;
import com.zsh.shopclient.aView.BookingSeatsV;

/**
 * Created by Administrator on 2017/12/12.
 */

public class BookingSeatsActivity extends BaseActivity {
    @Override
    protected BaseView createTypeView() {
        return new BookingSeatsV();
    }
    public View.OnClickListener getClickListener(){
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (v.getId()){
                    case R.id.image_goBack:
                        finish();
                        break;
                    case R.id.text_sumbit:
                        CharSequence[] relation = ((BookingSeatsV)getTypeView()).getRelation();
                        if(null != relation) {
                            Intent intent = new Intent();
                            intent.putExtra(getString(R.string.send), relation);
                            setResult(RESULT_OK, intent);
                            finish();
                        }
                }
            }
        };
    }
}
