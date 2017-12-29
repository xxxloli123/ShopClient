package com.zsh.shopclient.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.zsh.shopclient.R;
import com.zsh.shopclient.aPresenter.BaseActivity;

import java.util.List;

/**
 * Created by Administrator on 2017/11/13.
 */

public class TextAdapter extends RecyclerView.Adapter {
    private BaseActivity activity;
    private List<CharSequence[]> list;
    private int index;
    private ItemOnClickListener itemOnClickListener;

    public TextAdapter(BaseActivity activity, List<CharSequence[]> list) {
        this.activity = activity;
        this.list = list;
        index = 0;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new Vh(LayoutInflater.from(activity).inflate(R.layout.item_text,parent,false));
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        switch (position %4){
            case 0:
                holder.itemView.setBackgroundResource(R.color.peachPink);
                break;
            case 1:
                holder.itemView.setBackgroundResource(R.color.textGray);
                break;
            case 2:
                holder.itemView.setBackgroundResource(R.color.cambridgeBlue);
                break;
            case 3:
                holder.itemView.setBackgroundResource(R.color.brown);
                break;
        }
        if(index == position){
            ((LinearLayout)(holder.itemView)).getChildAt(0).setBackgroundResource(R.color.black);
            ((LinearLayout)(holder.itemView)).getChildAt(1).setSelected(true);
            ((LinearLayout)holder.itemView).getChildAt(2).setBackgroundResource(R.color.black);
        }else{
            ((LinearLayout)holder.itemView).getChildAt(0).setBackgroundResource(R.color.white);
            ((LinearLayout)(holder.itemView)).getChildAt(1).setSelected(false);
            ((LinearLayout)holder.itemView).getChildAt(2).setBackgroundResource(R.color.white);
        }
        holder.itemView.setId(position);
        ((TextView)((LinearLayout)(holder.itemView)).getChildAt(1)).setText(list.get(position)[0]);
    }

    @Override
    public int getItemCount() {
        return null == list ?0 : list.size();
    }
    public CharSequence getItemCharSequence(int position){
        return list.get(index)[position];
    }

    public void setItemOnClickListener(ItemOnClickListener itemOnClickListener) {
        this.itemOnClickListener = itemOnClickListener;
    }
    private class Vh extends RecyclerView.ViewHolder implements View.OnClickListener{
        public Vh(View itemView) {
            super(itemView);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            if(null != itemOnClickListener && v.getId() != index) {//防止重复点中一个item而反复调用监听事件
                index = v.getId();
                itemOnClickListener.itemOnClick(v);
                notifyDataSetChanged();
            }
        }
    }
    public interface ItemOnClickListener{
        void itemOnClick(View view);
    }
}
