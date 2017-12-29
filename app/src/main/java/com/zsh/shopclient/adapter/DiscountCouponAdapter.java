package com.zsh.shopclient.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.zsh.shopclient.R;
import com.zsh.shopclient.aPresenter.BaseActivity;

import java.util.List;

/**
 * Created by Administrator on 2017/12/9.
 */

public class DiscountCouponAdapter extends RecyclerView.Adapter {
    private List<CharSequence[]>list;
    private BaseActivity activity;
    private TextAdapter.ItemOnClickListener itemOnClickListener;

    public DiscountCouponAdapter(BaseActivity activity,List<CharSequence[]> list) {
        this.list = list;
        this.activity = activity;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new Vh(LayoutInflater.from(activity).inflate(R.layout.item_discount_coupon,parent,false));
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        ((TextView)((RelativeLayout)holder.itemView).getChildAt(0)).setText(activity.getString(R.string.sign)+ list.get(position)[0]);
        ((TextView)((RelativeLayout)holder.itemView).getChildAt(1)).setText(activity.getString(R.string.full)+activity.getString(R.string.sign)+
                list.get(position)[1]+activity.getString(R.string.minus)+activity.getString(R.string.sign)+ list.get(position)[0]);
        ((TextView)((RelativeLayout)holder.itemView).getChildAt(2)).setText(activity.getString(R.string.expirationDate)+ list.get(position)[2]);
        ((RelativeLayout)holder.itemView).getChildAt(3).setTag(list.get(position));
    }

    @Override
    public int getItemCount() {
        return null == list ?0: list.size();
    }
    public void setOnItemClickListener(TextAdapter.ItemOnClickListener itemOnClickListener){
        this.itemOnClickListener = itemOnClickListener;
    }
    private class Vh extends RecyclerView.ViewHolder implements View.OnClickListener{
        public Vh(View itemView) {
            super(itemView);
            itemView.findViewById(R.id.text_use).setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            switch (v.getId()){
                case R.id.text_use:
                    if(null != itemOnClickListener)
                        itemOnClickListener.itemOnClick(v);
                    break;
            }
        }
    }
}
