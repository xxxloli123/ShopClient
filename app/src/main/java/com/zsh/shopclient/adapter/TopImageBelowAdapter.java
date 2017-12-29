package com.zsh.shopclient.adapter;

import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.AbsoluteSizeSpan;
import android.text.style.ForegroundColorSpan;

import com.bumptech.glide.Glide;
import com.zsh.shopclient.R;
import com.zsh.shopclient.aPresenter.BaseActivity;

import java.util.List;

/**
 * Created by Administrator on 2017/12/17.
 */

public class TopImageBelowAdapter extends UpImageBelowTextAdapter {
    public TopImageBelowAdapter(BaseActivity activity, List<CharSequence[]> list, int layoutId) {
        super(activity, list,layoutId);
    }
    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        Glide.with(activity).load(list.get(position)[0]).into(((Vh)holder).image);
        String original = activity.getString(R.string.sign)+ list.get(position)[2];
        String str = activity.getString(R.string.economize) + activity.getString(R.string.sign) + (Float.valueOf((String) list.get(position)[5])- Float.valueOf((String) list.get(position)[2]));
        SpannableString ss = new SpannableString(list.get(position)[1]+"\n"+original+str);
        ss.setSpan(new AbsoluteSizeSpan((int)activity.getResources().getDimension(R.dimen.size20)),list.get(position)[1].length(),list.get(position)[1].length()+ original.length()+1, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        ss.setSpan(new ForegroundColorSpan(ContextCompat.getColor(activity,R.color.scarlet)),list.get(position)[1].length(),list.get(position)[1].length()+ original.length()+1,Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        ((Vh)holder).text.setText(ss);
        ((Vh)holder).itemView.setId(position);
    }
}
