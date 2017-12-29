package com.zsh.shopclient.adapter;

import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.zsh.shopclient.R;
import com.zsh.shopclient.aPresenter.BaseActivity;

import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2017/11/23.
 */

public class LinkageTextAdapter extends RecyclerView.Adapter {
    private BaseActivity activity;
    private Map<CharSequence,List<CharSequence>>map;
    private Iterator iterator;
    private int index;
    private CharSequence key,driveKey;
    private TextAdapter.ItemOnClickListener itemClickListener;

    public LinkageTextAdapter(BaseActivity activity, Map<CharSequence,List<CharSequence>>map) {
        this.activity = activity;
        this.map = map;
        iterator = null != map && 0< map.keySet().size()? map.keySet().iterator() : null;
        index = -1;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new Vh(LayoutInflater.from(activity).inflate(R.layout.item_linkage_text,parent,false));
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        CharSequence key = (CharSequence) iterator.next();
        holder.itemView.setId(position);
        holder.itemView.setTag(key);
        ((TextView)((LinearLayout)holder.itemView).getChildAt(0)).setText(key);
        if (position == index) {
            ((TextView) ((LinearLayout) holder.itemView).getChildAt(0)).setTextColor(ContextCompat.getColor(activity, R.color.scarlet));
            ((LinearLayout)holder.itemView).getChildAt(0).setClickable(true);
        }else if(null == this.key || map.containsKey(this.key) || map.get(key).contains(this.key)) {
            ((TextView) ((LinearLayout) holder.itemView).getChildAt(0)).setTextColor(ContextCompat.getColor(activity, R.color.textDarkgray));
            ((LinearLayout)holder.itemView).getChildAt(0).setClickable(true);
        }else {
            ((TextView) ((LinearLayout) holder.itemView).getChildAt(0)).setTextColor(ContextCompat.getColor(activity, R.color.grayPink));
            ((LinearLayout)holder.itemView).getChildAt(0).setClickable(false);
        }
    }

    @Override
    public int getItemCount() {
        return null == map ?0: map.size();
    }
    public void update(final Map<CharSequence,List<CharSequence>> map){
        this.map = map;
        iterator = map.keySet().iterator();
        notifyDataSetChanged();
    }
    public void setKey(CharSequence key) {
        this.key = key;
        iterator = map.keySet().iterator();
        notifyDataSetChanged();
    }
    public CharSequence getDriveKey(){
        return driveKey;
    }

    public void setItemOnClickListener(TextAdapter.ItemOnClickListener itemClickListener) {
        this.itemClickListener = itemClickListener;
    }

    private class Vh extends RecyclerView.ViewHolder implements View.OnClickListener{
        public Vh(View itemView) {
            super(itemView);
            ((LinearLayout)itemView).getChildAt(0).setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            index = itemView.getId() != index ? itemView.getId() :-1;
            driveKey = -1 != index ? (CharSequence)itemView.getTag():null;//itemView.getTag() != key ? (CharSequence) itemView.getTag():null;
            itemClickListener.itemOnClick(v);
            iterator = map.keySet().iterator();
            notifyDataSetChanged();
        }
    }
}
