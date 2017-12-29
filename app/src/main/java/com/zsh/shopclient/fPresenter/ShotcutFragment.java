package com.zsh.shopclient.fPresenter;

import android.content.Intent;
import android.content.res.TypedArray;
import android.view.View;

import com.zsh.shopclient.R;
import com.zsh.shopclient.aPresenter.HelpActivity;
import com.zsh.shopclient.aPresenter.MainActivity;
import com.zsh.shopclient.aPresenter.SpeedyActivity;
import com.zsh.shopclient.aView.BaseView;
import com.zsh.shopclient.adapter.UpImageBelowTextAdapter;
import com.zsh.shopclient.fView.ShotcutV;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2017/11/5.
 */

public class ShotcutFragment extends BaseFragment {

    @Override
    protected BaseView createTypeView() {
        return new ShotcutV();
    }
    public List<CharSequence[]> getData(){
        String resource = "android.resource://"+getActivity().getPackageName()+"/";
        List<CharSequence[]> list = new ArrayList<>();
        TypedArray icons = getResources().obtainTypedArray(R.array.shotcut);
        TypedArray titles = getResources().obtainTypedArray(R.array.titles);
        String[] types = getResources().getStringArray(R.array.types);
        if(null != icons && null != titles && null != types) {
            int iconsLen = icons.length();
            int titlesLen = titles.length();
            int lenght =  iconsLen <= titlesLen ? iconsLen : titlesLen;
            for (int index = 0; index < lenght; index++)
                list.add(new String[]{resource +icons.getResourceId(index, 0), getString(titles.getResourceId(index, 0)),types[index]});
        }
        return list;
    }
    public View.OnClickListener getClickListener(){
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = null;
                if(0< v.getId()) {
                    intent = new Intent(getActivity(), SpeedyActivity.class);
                    intent.putExtra(getString(R.string.location), ((MainActivity)getActivity()).getLocation());
                }else{
                    intent = new Intent(getActivity(), HelpActivity.class);
                    intent.putExtra(HelpActivity.URI,getString(R.string.huiDi));
                }
                startActivity(intent);
            }
        };
    }
}
