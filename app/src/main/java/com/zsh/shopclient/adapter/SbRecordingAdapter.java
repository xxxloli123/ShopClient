package com.zsh.shopclient.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.zsh.shopclient.R;
import com.zsh.shopclient.model.SbRecording;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Administrator on 2017/9/15.
 */

public class SbRecordingAdapter extends BaseAdapter implements View.OnClickListener {

    private ArrayList<SbRecording> sbRecordings;
    private Context mContext;
    private Callback mCallback;

    //响应按钮点击事件,调用子定义接口，并传入View
    @Override
    public void onClick(View v) {
        mCallback.click(v);
    }

    /**
     * 自定义接口，用于回调按钮点击事件到Activity
     *
     * @author Ivan Xu
     *         2014-11-26
     */
    public interface Callback {
        public void click(View v);
    }

    public SbRecordingAdapter(Context mContext, ArrayList<SbRecording> sbRecordings) {
        this.sbRecordings = sbRecordings;
        this.mContext = mContext;
//        mCallback = callback;
    }

    @Override
    public int getCount() {
        return sbRecordings == null ? 0 : sbRecordings.size();
    }

    @Override
    public Object getItem(int i) {
        return sbRecordings.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(final int i, View view, ViewGroup viewGroup) {
        ViewHolder holder = null;
        if (view == null) {
            view = LayoutInflater.from(mContext).inflate(R.layout.item_sb_recording, null);
            holder = new ViewHolder(view);
            view.setTag(holder);
        } else {
            holder = (ViewHolder) view.getTag();
        }

        holder.quantityTv.setText(sbRecordings.get(i).getNumber()+" 掌币");
        holder.infoTv.setText(sbRecordings.get(i).getRecordsdeail());
        return view;
    }

    static class ViewHolder {
        @BindView(R.id.quantity_tv)
        TextView quantityTv;
        @BindView(R.id.info_tv)
        TextView infoTv;

        ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }
}
