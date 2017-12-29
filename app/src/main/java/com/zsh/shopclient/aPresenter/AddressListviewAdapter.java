package com.zsh.shopclient.aPresenter;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.TextView;


import com.zsh.shopclient.R;
import com.zsh.shopclient.model.AddressEntity;
import com.zsh.shopclient.view.CheckLinearLayout;

import java.util.List;

/**
 * Created by Administrator on 2017/1/23 0023.
 */

public class AddressListviewAdapter extends BaseAdapter {
    private List<AddressEntity> list;
    private ReceiptAddressActivity mContext;
    private LayoutInflater inflater;
    private boolean signChioce;

    public AddressListviewAdapter(ReceiptAddressActivity mContext, List<AddressEntity> list) {
        this(mContext, list, false);
    }

    public AddressListviewAdapter(ReceiptAddressActivity act, List<AddressEntity> list, boolean item) {
        this.list = list;
        this.mContext = act;
        inflater = LayoutInflater.from(mContext);
        signChioce = item;
    }

    @Override
    public int getCount() {
        return list == null || list.isEmpty() ? 0 : list.size();
    }

    @Override
    public AddressEntity getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.item_receipt_address_listview, parent, false);
            holder = new ViewHolder();
            holder.name = (TextView) convertView.findViewById(R.id.item_name);
            holder.phone = (TextView) convertView.findViewById(R.id.item_phone);
            holder.address = (TextView) convertView.findViewById(R.id.item_address);
            holder.checkBox = (CheckBox) convertView.findViewById(R.id.checkbox);
            if (signChioce) {
                convertView.setFocusableInTouchMode(false);
                convertView.setFocusable(false);
                convertView.setClickable(false);
            } else ((CheckLinearLayout) convertView).setCanCheck(false);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        AddressEntity address = list.get(position);
        holder.name.setText(address.getPersonname());
        holder.phone.setText(address.getPersonphone());
        holder.address.setText(String.format("%s%s%s%s", address.getPro(), address.getCity(),
                address.getDistrict(), address.getHouseNumber()));
        convertView.findViewById(R.id.item_edit).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, NewAddressActivity.class);
                AddressEntity addr = list.get(position);
                intent.putExtra(NewAddressActivity.ADDRESS, addr);
                mContext.startActivity(intent);
            }
        });
        holder.checkBox.setChecked(address.isDefault());
        convertView.findViewById(R.id.item_del).setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        mContext.delAddr(list.get(position));
                    }
                });
        return convertView;
    }

    public void notifyDataSetChanged(List<AddressEntity> addrs) {
        this.list = addrs;
        notifyDataSetChanged();
    }

    public class ViewHolder {
        private TextView name;
        private TextView phone;
        private TextView address;
        CheckBox checkBox;
    }
}
