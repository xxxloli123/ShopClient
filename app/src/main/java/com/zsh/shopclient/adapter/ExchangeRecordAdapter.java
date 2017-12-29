package com.zsh.shopclient.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.zsh.shopclient.R;
import com.zsh.shopclient.model.Record;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Administrator on 2017/9/15.
 */

public class ExchangeRecordAdapter extends BaseAdapter {

    private List<Record> accounts;
    private Context context;

    public ExchangeRecordAdapter(Context context, List<Record> accounts) {
        this.accounts = accounts;
        this.context = context;
    }


    @Override
    public int getCount() {
        return accounts == null ? 0 : accounts.size();
    }

    @Override
    public Object getItem(int i) {
        return accounts.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        ViewHolder holder = null;
        if (view == null) {
            view = LayoutInflater.from(context).inflate(R.layout.item_exchange_record, null);
            holder = new ViewHolder(view);
            view.setTag(holder);
        } else {
            holder = (ViewHolder) view.getTag();
        }

        holder.nameTv.setText(accounts.get(i).getProductName());
        holder.priceTv.setText(accounts.get(i).getSingleIntegral()+"");
        holder.timeTv.setText(accounts.get(i).getCreateDate());
        return view;
    }

    static class ViewHolder {
        @BindView(R.id.name_tv)
        TextView nameTv;
        @BindView(R.id.price_tv)
        TextView priceTv;
        @BindView(R.id.time_tv)
        TextView timeTv;

        ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }
}
