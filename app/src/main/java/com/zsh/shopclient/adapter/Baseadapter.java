package com.zsh.shopclient.adapter;


import android.content.Context;
import android.support.annotation.LayoutRes;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

/**
 * Created by sgrape on 2017/5/16.
 * e-mail: sgrape1153@gmail.com
 */

public abstract class Baseadapter<T> extends android.widget.BaseAdapter {

    protected List<T> list;
    protected LayoutInflater inflater;

    public Baseadapter(Context context, List<T> list) {
        this.list = list;
        inflater = LayoutInflater.from(context);
    }

    public Baseadapter(Context context) {
        this(context, null);
    }

    @Override
    public int getCount() {
        return list == null ? 0 : list.size();
    }

    @Override
    public T getItem(int position) {
        return list == null ? null : list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    public void notifyDataSetChanged(List<T> list) {
        this.list = list;
        super.notifyDataSetChanged();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        if (convertView == null) {
            convertView = inflater.inflate(getLayoutId(position), null, false);
        }
        return getView(convertView, position);
    }

    protected abstract View getView(View view, int position);


    protected abstract
    @LayoutRes
    int getLayoutId(int position);
    public List<T> getData(){
        return list;
    }
}
