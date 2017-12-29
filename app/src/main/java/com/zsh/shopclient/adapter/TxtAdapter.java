package com.zsh.shopclient.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.zsh.shopclient.R;
import com.zsh.shopclient.aPresenter.BaseActivity;

import java.util.List;

/**
 * Created by Administrator on 2017/11/17.
 */

public class TxtAdapter extends RecyclerView.Adapter {
    private BaseActivity activity;
    private List<CharSequence[]> list;
    private int index;
    private boolean orientation;
    private TextAdapter.ItemOnClickListener itemOnClickListener;

    public TxtAdapter(BaseActivity activity, List<CharSequence[]> list) {
        this(activity,list, false);
    }

    public TxtAdapter(BaseActivity activity, List<CharSequence[]> list, boolean orientation) {
        this.activity = activity;
        this.list = list;
        this.orientation = orientation;
        index = 0;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new Vh(LayoutInflater.from(activity).inflate(R.layout.item_text1,parent,false));
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        holder.itemView.setId(position);
        holder.itemView.setTag(list.get(position));
        ((TextView)((FrameLayout)holder.itemView).getChildAt(0)).setText(list.get(position)[0]);
        if(index == position) {
            //holder.itemView.setBackgroundResource(R.color.backgroundGray);//worning 设置holder.itemView背景色后分割线就看不显示了
            ((FrameLayout) holder.itemView).getChildAt(0).setSelected(true);
        }else{
            //holder.itemView.setBackgroundResource(R.color.white);
            ((FrameLayout) holder.itemView).getChildAt(0).setSelected(false);
        }
    }

    @Override
    public int getItemCount() {
        return null == list ?0: list.size();
    }

    public void setIndex(int index) {
        this.index = index;
    }

    public void setItemOnClickListener(TextAdapter.ItemOnClickListener itemOnClickListener) {
        this.itemOnClickListener = itemOnClickListener;
    }
    public void update(List<CharSequence[]>list){
        index = 0;
        this.list = list;
        notifyDataSetChanged();
    }
    private class Vh extends RecyclerView.ViewHolder implements View.OnClickListener{
        public Vh(View itemView) {
            super(itemView);
            itemView.setOnClickListener(this);
            if(orientation){
                RecyclerView.LayoutParams params = (RecyclerView.LayoutParams) itemView.getLayoutParams();
                params.width = RecyclerView.LayoutParams.WRAP_CONTENT;
                itemView.setLayoutParams(params);
            }
        }

        @Override
        public void onClick(View v) {
            if(index != itemView.getId() && null != itemOnClickListener) {
                itemOnClickListener.itemOnClick(v);
                index = itemView.getId();
                notifyDataSetChanged();
            }
        }
    }
}
