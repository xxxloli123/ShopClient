package com.zsh.shopclient.adapter;

import android.support.v7.widget.RecyclerView;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.AbsoluteSizeSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.zsh.shopclient.R;
import com.zsh.shopclient.aPresenter.BaseActivity;

import java.util.List;

/**
 * Created by Administrator on 2017/12/21.
 */

public class GoodsAdapter extends RecyclerView.Adapter {
    private BaseActivity activity;
    private List<CharSequence[]> list;

    public GoodsAdapter(BaseActivity activity, List<CharSequence[]> list) {
        this.activity = activity;
        this.list = list;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new Vh(LayoutInflater.from(activity).inflate(R.layout.item_goods,parent,false));
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        Glide.with(activity).load(activity.getString(R.string.ip)+activity.getString(R.string.path)+list.get(position)[0]).into(((Vh)holder).image);
        ((Vh)holder).name.setText(list.get(position)[1]);
        ((Vh)holder).price.setText(activity.getString(R.string.sign)+ list.get(position)[2]);
        ((Vh)holder).quantity.setText("*"+ list.get(position)[3]);
    }

    @Override
    public int getItemCount() {
        return null == list ? 0 : list.size();
    }
    private class Vh extends RecyclerView.ViewHolder{
        ImageView image;
        TextView name,price,quantity;
        public Vh(View itemView) {
            super(itemView);
            image = itemView.findViewById(R.id.image);
            name = itemView.findViewById(R.id.text_name);
            price = itemView.findViewById(R.id.text_currentPrice);
            quantity = itemView.findViewById(R.id.text_quantity);
        }
    }
}
