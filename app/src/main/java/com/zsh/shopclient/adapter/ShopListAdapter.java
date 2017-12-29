package com.zsh.shopclient.adapter;

import android.content.Intent;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.zsh.shopclient.R;
import com.zsh.shopclient.aPresenter.BaseActivity;
import com.zsh.shopclient.aPresenter.ShopActivity;
import java.util.LinkedHashMap;
import java.util.List;

/**
 * Created by Administrator on 2017/11/15.
 */

public class ShopListAdapter extends RecyclerView.Adapter {
    private BaseActivity activity;
    private List<CharSequence[]> list;
    private LinkedHashMap<String,List<CharSequence[]>> listLinkedHashMap;

    public ShopListAdapter(BaseActivity activity, List<CharSequence[]> list, LinkedHashMap<String, List<CharSequence[]>> listLinkedHashMap) {
        this.activity = activity;
        this.list = list;
        this.listLinkedHashMap = listLinkedHashMap;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new Vh(LayoutInflater.from(activity).inflate(R.layout.item_shop,parent,false));
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        holder.itemView.setId(position);
        Glide.with(activity).load(activity.getString(R.string.ip) + activity.getString(R.string.shopImagePath)+list.get(position)[0]).into(((Vh) holder).logo);
        ((Vh) holder).name.setText(list.get(position)[1]);
        String homeDelivery = activity.getString(R.string.homeDelivery);
        String toShop = activity.getString(R.string.toShop);
        String way = "";
        if("yes".equals(list.get(position)[2]) && list.get(position)[2].equals(list.get(position)[3]))
            way = toShop +" | "+ homeDelivery;
        else if("yes".equals(list.get(position)[2]))
            way = toShop;
        else if("yes".equals(list.get(position)[3]))
            way = homeDelivery;
        ((Vh) holder).way.setText(way);
        ((Vh) holder).rating.setRating(Integer.valueOf((String) list.get(position)[5]));
        ((Vh) holder).monthSalesVolume.setText(activity.getString(R.string.monthSalesVolume) + list.get(position)[6] + activity.getString(R.string.one));
        ((Vh) holder).freight.setText(activity.getString(R.string.freight) + list.get(position)[4]);
        ((Vh) holder).meter.setText(1< Float.valueOf((String)list.get(position)[10])? list.get(position)[10] + activity.getString(R.string.meterKm): Float.valueOf((String)list.get(position)[10])*1000+ activity.getString(R.string.meter));
        List<CharSequence[]> values = listLinkedHashMap.get(list.get(position)[0]);
        if (null != values && 0 < values.size()) {
            ((RelativeLayout) ((Vh) holder).recycler.getParent()).setVisibility(View.VISIBLE);
            (((Vh) holder).quantity).setText(values.size() + activity.getString(R.string.quantityActivity));
            ((Vh) holder).recycler.setLayoutManager(new LinearLayoutManager(activity, LinearLayoutManager.VERTICAL, false));
            ((Vh) holder).recycler.setItemAnimator(new DefaultItemAnimator());
            ((Vh) holder).recycler.setAdapter(new LeftImageRightTextAdapter(activity, values));
        } else
            ((RelativeLayout) ((Vh) holder).recycler.getParent()).setVisibility(View.GONE);
        //注意无法隐藏分割线
        /*if(null == filtration || list.get(position)[2].toString().contains(filtration))//str.indexOf("string")!=-1
            ((Vh)holder).setVisibility(true);
        else
            ((Vh)holder).setVisibility(false);*/
    }
    public void update(List<CharSequence[]>list,LinkedHashMap<String,List<CharSequence[]>>listLinkedHashMap){
        this.list = list;
        this.listLinkedHashMap = listLinkedHashMap;
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return null == list ?0: list.size();
    }
    private class Vh extends RecyclerView.ViewHolder implements View.OnClickListener{
        ImageView logo;
        RatingBar rating;
        TextView name,way,monthSalesVolume,freight,meter,quantity;
        RecyclerView recycler;
        public Vh(View itemView) {
            super(itemView);
            logo = itemView.findViewById(R.id.image_logo);
            name = itemView.findViewById(R.id.text_name);
            way = itemView.findViewById(R.id.text_way);
            rating = itemView.findViewById(R.id.rating);
            monthSalesVolume = itemView.findViewById(R.id.text_monthSalesVolume);
            freight = itemView.findViewById(R.id.text_freight);
            meter = itemView.findViewById(R.id.text_meter);
            ((RelativeLayout)meter.getParent()).setOnClickListener(this);
            quantity = itemView.findViewById(R.id.text_activityQuantity);
            quantity.setOnClickListener(this);
            recycler = itemView.findViewById(R.id.recycler);
        }
        public void setVisibility(boolean isVisible){//ecyclerview隐藏单个item代码（经测试有效 http://blog.csdn.net/rgen_xiao/article/details/55509722
            RecyclerView.LayoutParams param = (RecyclerView.LayoutParams)itemView.getLayoutParams();
            if(isVisible){
                param.width = RelativeLayout.LayoutParams.MATCH_PARENT;
                param.height = RelativeLayout.LayoutParams.WRAP_CONTENT;
                itemView.setVisibility(View.VISIBLE);
            }else{
                itemView.setVisibility(View.GONE);
                param.width = 0;
                param.height = 0;
            }
            itemView.setLayoutParams(param);
        }
        @Override
        public void onClick(View v) {
            switch (v.getId()){
                case R.id.text_activityQuantity:
                    ((LeftImageRightTextAdapter)recycler.getAdapter()).setDisplayOne();
                    break;
                default:
                    Intent intent = activity.getIntent();
                    intent.setClass(activity, ShopActivity.class);
                    intent.putExtra(activity.getString(R.string.title),list.get(itemView.getId()));
                    activity.startActivity(intent);
                    break;
            }
        }
    }
}
