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
import com.zsh.shopclient.aPresenter.DeliveryAddressActivity;
import com.zsh.shopclient.aPresenter.AddressActivity;

/**
 * Created by Administrator on 2017/12/6.
 */

public class TargetAdapter extends RecyclerView.Adapter {
    private BaseActivity activity;
    private CharSequence[] targets;

    public TargetAdapter(BaseActivity activity, CharSequence[] targets) {
        this.activity = activity;
        this.targets = targets;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new Vh(LayoutInflater.from(activity).inflate(R.layout.item_text1,parent,false));
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        ((TextView)((FrameLayout)holder.itemView).getChildAt(0)).setText(targets[position]);
    }

    @Override
    public int getItemCount() {
        return null == targets ?0: targets.length;
    }
    private class Vh extends RecyclerView.ViewHolder implements View.OnClickListener{
        public Vh(View itemView) {
            super(itemView);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            Intent intent = new Intent(activity,AddressActivity.class);
            intent.putExtra(activity.getString(R.string.send),targets);
            activity.startActivityForResult(intent,DeliveryAddressActivity.DELIVERYaDDRESS);
        }
    }
}
