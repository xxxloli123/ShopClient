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
import com.zsh.shopclient.model.SbCommodity;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Administrator on 2017/9/15.
 */

public class SbCommodityAdapter extends BaseAdapter {

    private ArrayList<SbCommodity> codes;
    private Context mContext;

    public SbCommodityAdapter(Context mContext, ArrayList<SbCommodity> codes) {
        this.codes = codes;
        this.mContext = mContext;
    }

    //刷新Adapter
    public void refresh(ArrayList<SbCommodity> codes) {
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
            view = LayoutInflater.from(mContext).inflate(R.layout.item_sb_commodity, null);
            holder = new ViewHolder(view);
            view.setTag(holder);
        } else {
            holder = (ViewHolder) view.getTag();
        }
        holder.priceTv.setText( codes.get(i).getSingleIntegral()+" 掌币");
        holder.nameTv.setText( codes.get(i).getProductName()+"");
        Glide.with(mContext).load(Config.Url.getUrl(Config.IMG_Commodity) + codes.get(i).getSmallImg())
                .into(holder.img);
        return view;
    }


    static class ViewHolder {
        @BindView(R.id.name_tv)
        TextView nameTv;
        @BindView(R.id.price_tv)
        TextView priceTv;
        @BindView(R.id.img)
        ImageView img;

        ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }
}
