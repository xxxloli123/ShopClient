package com.zsh.shopclient.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.zsh.shopclient.R;
import com.zsh.shopclient.aPresenter.BaseActivity;

import java.util.List;

/**
 * Created by Administrator on 2017/11/15.
 */

public class LeftIconRightTextAdapter extends RecyclerView.Adapter {
    private BaseActivity activity;
    private List<CharSequence[]> list;
    private int index;
    private TextAdapter.ItemOnClickListener itemOnClickListener;

    public LeftIconRightTextAdapter(BaseActivity activity, List<CharSequence[]> list) {
        this.activity = activity;
        this.list = list;
        index = -1;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new Vh(LayoutInflater.from(activity).inflate(R.layout.item_left_icon_right_text,parent,false));
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        Glide.with(activity).load(list.get(position)[0]).into((ImageView)((RelativeLayout)holder.itemView).getChildAt(0));
        ((TextView)((RelativeLayout)holder.itemView).getChildAt(1)).setText(list.get(position)[1]);
        holder.itemView.setId(position);
        if(index == position)
            holder.itemView.setBackgroundResource(R.color.white);
        else
            holder.itemView.setBackgroundResource(R.color.backgroundGray);
    }

    @Override
    public int getItemCount() {
        return null == list ?0: list.size();
    }
    public void setItemOnClickListener(TextAdapter.ItemOnClickListener itemOnClickListener) {
        this.itemOnClickListener = itemOnClickListener;
    }
    public void update(List<CharSequence[]>list){
        this.list = list;
        notifyDataSetChanged();
    }
    private class Vh extends RecyclerView.ViewHolder implements View.OnClickListener{
        public Vh(View itemView) {
            super(itemView);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            if(null != itemOnClickListener && v.getId() != index) {
                index = v.getId();
                v.setTag(list.get(index));
                itemOnClickListener.itemOnClick(v);
                notifyDataSetChanged();
            }
        }
    }
}
