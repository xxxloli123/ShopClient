package com.zsh.shopclient.adapter;

import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.zsh.shopclient.R;
import com.zsh.shopclient.aPresenter.BaseActivity;

import java.util.List;

/**
 * Created by Administrator on 2017/11/5.
 * URL转Drawable之 Android中获取网络图片的三种方法 http://blog.csdn.net/xinlvmylife/article/details/49447689
 * 在线程中Drawable.createFromStream(new URL(imageUrl).openStream(), "image.jpg");
 */

public class UpImageBelowTextAdapter extends RecyclerView.Adapter {
    protected BaseActivity activity;
    protected List<CharSequence[]> list;
    private int layoutId;
    private Intent intent;
    private View.OnClickListener listener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if(null != intent) {
                intent.putExtra(activity.getString(R.string.title),list.get(v.getId()));
                activity.startActivity(intent);
            }
        }
    };

    public UpImageBelowTextAdapter(BaseActivity activity, List<CharSequence[]> list) {
        this(activity,list,0);
    }

    public UpImageBelowTextAdapter(BaseActivity activity, List<CharSequence[]> list, int layoutId) {
        this.activity = activity;
        this.list = list;
        this.layoutId = layoutId;
    }
    public void setIntent(Intent intent){
        this.intent = intent;
    }
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new Vh(LayoutInflater.from(activity).inflate(0== layoutId ? R.layout.item_image_text : layoutId,parent,false));
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        /*Drawable drawable = ContextCompat.getDrawable(activity,data.get(position)[0]);
        drawable.setBounds(0,0,drawable.getMinimumWidth(),drawable.getMinimumHeight());
        ((Vh)holder).text.setCompoundDrawables(null,drawable,null,null);
        ((Vh)holder).text.setText(data.get(position)[1]);*/
        Glide.with(activity).load(list.get(position)[0]).into(((Vh)holder).image);
        ((Vh)holder).text.setText(list.get(position)[1]);
        ((Vh)holder).itemView.setId(position);
    }

    @Override
    public int getItemCount() {
        return null == list ?0: list.size();
    }
    public void update(List<CharSequence[]>list){
        this.list = list;
        notifyDataSetChanged();
    }

    public void setListener(View.OnClickListener listener) {
        this.listener = listener;
    }

    protected class Vh extends RecyclerView.ViewHolder{
        ImageView image;
        TextView text;
        public Vh(View itemView) {
            super(itemView);
            if(0 != layoutId){
                RecyclerView.LayoutParams params = (RecyclerView.LayoutParams) itemView.getLayoutParams();
                params.width = RecyclerView.LayoutParams.WRAP_CONTENT;
                itemView.setLayoutParams(params);
            }
            image = itemView.findViewById(R.id.image);
            text = itemView.findViewById(R.id.text);
            itemView.setOnClickListener(listener);
        }
    }
}
