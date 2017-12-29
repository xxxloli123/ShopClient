package com.zsh.shopclient.aPresenter;

import android.content.Intent;
import android.view.View;

import com.zsh.shopclient.R;
import com.zsh.shopclient.aView.BaseView;
import com.zsh.shopclient.aView.MerchantParticularsV;

import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2017/12/5.
 */

public class MerchantParticularsActivity extends BaseActivity {
    @Override
    protected BaseView createTypeView() {
        return new MerchantParticularsV();
    }
    public CharSequence getInfo(int index){
        return getIntent().getCharSequenceArrayExtra(getString(R.string.send))[index];
    }
    public CharSequence getEvaluate(int index){
        Map<List<CharSequence[]>,List<CharSequence[]>> map = (Map<List<CharSequence[]>,List<CharSequence[]>>)getIntent().getExtras().get(getString(R.string.sendBody));
        if(null != map) {
            Iterator iterator = map.keySet().iterator();
            List<CharSequence[]> list = (List<CharSequence[]>) iterator.next();
            return 0 == list.size() ? null : list.get(0)[index];
        }else
            return null;
    }
    public List<CharSequence[]> getActivitys(){
        Map<List<CharSequence[]>,List<CharSequence[]>> map = (Map<List<CharSequence[]>,List<CharSequence[]>>)getIntent().getExtras().get(getString(R.string.sendBody));
        if(null != map) {
            Iterator iterator = map.keySet().iterator();
            List<CharSequence[]> list = (List<CharSequence[]>) iterator.next();
            return 0 == list.size()  ? null: map.get(list);
        }else
            return null;
    }
    public View.OnClickListener getClickListener(){
        return new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                switch (v.getId()){
                    case R.id.image_goBack:
                        finish();
                        break;
                    case R.id.text_reveal:
                        Intent intent = getIntent();
                        intent.setClass(MerchantParticularsActivity.this,EvaluateActivity.class);
                        startActivity(intent);
                        break;
                }
            }
        };
    }
}
