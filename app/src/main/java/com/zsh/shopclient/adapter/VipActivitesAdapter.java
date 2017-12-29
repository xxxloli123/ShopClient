package com.zsh.shopclient.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.zsh.shopclient.R;
import com.zsh.shopclient.model.Activites;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Administrator on 2017/9/15.
 */

public class VipActivitesAdapter extends BaseAdapter {

    private ArrayList<Activites> codes;
    private Context mContext;

    public VipActivitesAdapter(Context mContext, ArrayList<Activites> codes) {
        this.codes = codes;
        this.mContext = mContext;
    }

    //刷新Adapter
    public void refresh(ArrayList<Activites> codes) {
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
            view = LayoutInflater.from(mContext).inflate(R.layout.item_vip_activites, null);
            holder = new ViewHolder(view);
            view.setTag(holder);
        } else {
            holder = (ViewHolder) view.getTag();
        }
        holder.nameTv.setText(codes.get(i).getActivityName() + "");
        return view;
    }

    static class ViewHolder {
        @BindView(R.id.name_tv)
        TextView nameTv;

        ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }
}
