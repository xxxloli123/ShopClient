package com.zsh.shopclient.adapter;

import android.content.Intent;
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
import com.zsh.shopclient.aPresenter.CommodityParticularsActivity;
import com.zsh.shopclient.aPresenter.ShopActivity;

import java.util.List;

/**
 * Created by Administrator on 2017/11/17.
 */

public class CommodityAdapter extends RecyclerView.Adapter {
    private BaseActivity activity;
    private List<CharSequence[]> list;
    private boolean isItemClick;
    private ItemOnClickListener itemOnClickListener;

    public CommodityAdapter(BaseActivity activity, List<CharSequence[]> list,boolean isItemClick) {
        this.activity = activity;
        this.list = list;
        this.isItemClick = isItemClick;
    }
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new Vh(LayoutInflater.from(activity).inflate(R.layout.item_commodity,parent,false));
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        holder.itemView.setId(position);
        holder.itemView.setTag(list.get(position));
        Glide.with(activity).load(activity.getString(R.string.ip) + activity.getString(R.string.pictureLibraryPath)+list.get(position)[0]).into(((Vh)holder).image);
        ((Vh)holder).name.setText(list.get(position)[1]);
        SpannableString ss = new SpannableString(activity.getString(R.string.sign)+Math.round(Float.valueOf((String) list.get(position)[2])*100)/100.0f);
        ss.setSpan(new AbsoluteSizeSpan((int)activity.getResources().getDimension(R.dimen.size20)),1,ss.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        ((Vh)holder).currentPrice.setText(ss);
        ((Vh)holder).reduce.setVisibility("0".equals(list.get(position)[4])? View.GONE : View.VISIBLE);
        ((Vh)holder).quantity.setText("0".equals(list.get(position)[4])? "" : list.get(position)[4]);
    }

    @Override
    public int getItemCount() {
        return null == list ?0: list.size();
    }
    public void update(List<CharSequence[]>list, boolean isEmpty){
        if(isEmpty)
            this.list = null;
        else
            this.list = list;
        notifyDataSetChanged();
    }
    public void setItemOnClickListener(ItemOnClickListener itemOnClickListener) {
        this.itemOnClickListener = itemOnClickListener;
    }
    private class Vh extends RecyclerView.ViewHolder implements View.OnClickListener{
        ImageView image,reduce;
        TextView name,monthSalesVolume,currentPrice,originalCost,quantity;
        public Vh(View itemView) {
            super(itemView);
            image = itemView.findViewById(R.id.image);
            reduce = itemView.findViewById(R.id.image_reduce);
            View add = itemView.findViewById(R.id.image_add);
            name = itemView.findViewById(R.id.text_name);
            monthSalesVolume = itemView.findViewById(R.id.text_monthSalesVolume);
            currentPrice = itemView.findViewById(R.id.text_currentPrice);
            originalCost = itemView.findViewById(R.id.text_originalCost);
            quantity = itemView.findViewById(R.id.text_quantity);
            if(isItemClick)
                itemView.setOnClickListener(this);
            reduce.setOnClickListener(this);
            add.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            switch (v.getId()){
                case R.id.image_add:
                    if(null != itemOnClickListener)
                        itemOnClickListener.onClickAdd(itemView,list.get(itemView.getId()));//list.get(itemView.getId())[5] = "" + itemOnClickListener.onClickAdd(list.get(itemView.getId()));
                    notifyItemChanged(itemView.getId());
                    break;
                case R.id.image_reduce:
                    if(null != itemOnClickListener)
                        itemOnClickListener.onClickReduce(itemView,list.get(itemView.getId()));//list.get(itemView.getId())[5] = "" + itemOnClickListener.onClickReduce(list.get(itemView.getId()));
                    /*if("0".equals(list.get(itemView.getId())[5])) {
                        list.remove(itemView.getId());
                        notifyDataSetChanged();
                    }else
                        notifyItemChanged(itemView.getId());*/
                    break;
                default:
                    Intent intent =  activity.getIntent();
                    intent.setClass(activity,CommodityParticularsActivity.class);
                    intent.putExtra(activity.getString(R.string.position),itemView.getId());
                    intent.putExtra(activity.getString(R.string.title),list.get(itemView.getId()));
                    activity.startActivityForResult(intent, ShopActivity.SHOPPINGcART);
                    break;
            }
        }
    }
    public interface ItemOnClickListener {
        int onClickAdd(View itemView,CharSequence[] array);
        int onClickReduce(View itemView,CharSequence[] array);
    }
}
