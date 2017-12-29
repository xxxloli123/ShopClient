package com.zsh.shopclient.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.zsh.shopclient.R;
import com.zsh.shopclient.interfaceconfig.Config;
import com.zsh.shopclient.model.VipShop;
import com.zsh.shopclient.view.MyListView;
import com.zsh.shopclient.view.RatingBar;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Administrator on 2017/9/15.
 */

public class VipShopAdapter extends BaseAdapter {

    private ArrayList<VipShop> codes;
    private Context mContext;

    public VipShopAdapter(Context mContext, ArrayList<VipShop> codes) {
        this.codes = codes;
        this.mContext = mContext;
    }

    //刷新Adapter
    public void refresh(ArrayList<VipShop> codes) {
        this.codes = codes;//传入list，然后调用notifyDataSetChanged方法
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return codes == null ? 0 : codes.size();
    }

    @Override
    public Object getItem(int i) {
        return codes.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        ViewHolder holder = null;
        if (view == null) {
            view = LayoutInflater.from(mContext).inflate(R.layout.item_vip_shop, null);
            holder = new ViewHolder(view);
            view.setTag(holder);
        } else {
            holder = (ViewHolder) view.getTag();
        }
        holder.priceTv.setText("价格 : ￥" + codes.get(i).getShop().getDeliveryFee());
        holder.nameTv.setText(codes.get(i).getShop().getShopName() + "");
        holder.activitesTv.setText(codes.get(i).getActivites().size() + "个活动");
        final ViewHolder finalHolder = holder;
        holder.activitesTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (finalHolder.activitesList.getVisibility()==View.VISIBLE)
                    finalHolder.activitesList.setVisibility(View.GONE);
                else finalHolder.activitesList.setVisibility(View.VISIBLE);
            }
        });
        Glide.with(mContext).load(Config.Url.getUrl(Config.IMG_ShopHear) +
                codes.get(i).getShop().getShopImage()).into(holder.img);
        holder.distanceTv.setText( codes.get(i).getShop().getShopUserDistance() + " km以内");
        holder.evaluateRb.setCountSelected(Integer.parseInt(codes.get(i).getShop().getGrade()));
        holder.salesTv.setText("月销 : " + codes.get(i).getShop().getOrdercount());
        holder.dispatchingTv.setText("商家配送距离 : " + codes.get(i).getShop().getDistance());
        VipActivitesAdapter vipActivitesAdapter = new VipActivitesAdapter(mContext, codes.get(i).getActivites());
        holder.activitesList.setAdapter(vipActivitesAdapter);
        return view;
    }

    static class ViewHolder {
        @BindView(R.id.img)
        ImageView img;
        @BindView(R.id.name_tv)
        TextView nameTv;
        @BindView(R.id.distance_tv)
        TextView distanceTv;
        @BindView(R.id.evaluate_rb)
        RatingBar evaluateRb;
        @BindView(R.id.sales_tv)
        TextView salesTv;
        @BindView(R.id.dispatching_tv)
        TextView dispatchingTv;
        @BindView(R.id.price_tv)
        TextView priceTv;
        @BindView(R.id.activites_tv)
        TextView activitesTv;
        @BindView(R.id.activites_list)
        MyListView activitesList;

        ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }
}
