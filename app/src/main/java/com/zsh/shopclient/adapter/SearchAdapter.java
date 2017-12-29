package com.zsh.shopclient.adapter;

import android.content.Intent;
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
 * Created by Administrator on 2017/11/29.
 */

public class SearchAdapter extends RecyclerView.Adapter {
    private BaseActivity activity;
    private List<CharSequence[]> list;

    public SearchAdapter(BaseActivity activity, List<CharSequence[]> list) {
        this.activity = activity;
        this.list = list;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new Vh(LayoutInflater.from(activity).inflate(R.layout.item_text1,parent,false));
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        holder.itemView.setId(position);
        ((TextView)((FrameLayout)holder.itemView).getChildAt(0)).setText(list.get(position)[1]);
    }

    @Override
    public int getItemCount() {
        return null == list ?0: list.size();
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
            Intent intent = activity.getIntent();
            intent.putExtra(activity.getString(R.string.title),list.get(itemView.getId()));
            activity.startActivity(intent);
        }
    }
}
