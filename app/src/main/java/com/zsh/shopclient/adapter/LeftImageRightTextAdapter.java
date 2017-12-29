package com.zsh.shopclient.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.zsh.shopclient.R;
import com.zsh.shopclient.aPresenter.BaseActivity;

import java.util.List;

/**
 * Created by Administrator on 2017/11/9.
 * 隐藏显示item的Adapter
 */

public class LeftImageRightTextAdapter extends RecyclerView.Adapter {
    private Context activity;
    private List<CharSequence[]> list;
    private boolean displayOne;

    public LeftImageRightTextAdapter(Context activity, List<CharSequence[]> list) {
        this.activity = activity;
        this.list = list;
        displayOne = true;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new Vh(LayoutInflater.from(activity).inflate(R.layout.item_left_image_right_text,parent,false));
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        /*if(0 == position)
            ((Vh)holder).setVisibility(true);
        else
            ((Vh)holder).setVisibility(displayOne);*/
        ((Vh)holder).text.setText(list.get(position)[0]);
    }
    public void setDisplayOne(){
        displayOne = !displayOne;
        notifyDataSetChanged();
    }
    @Override
    public int getItemCount() {
        if(displayOne)
            return null == list || 0== list.size() ?0:1;
        else
            return null == list ?0:list.size();
    }
    private class Vh extends RecyclerView.ViewHolder{
        TextView text;
        public Vh(View itemView) {
            super(itemView);
            text = itemView.findViewById(R.id.text);
        }
        public void setVisibility(boolean isVisible){//ecyclerview隐藏单个item代码（经测试有效 http://blog.csdn.net/rgen_xiao/article/details/55509722
            RecyclerView.LayoutParams param = (RecyclerView.LayoutParams)itemView.getLayoutParams();
            if(isVisible){
                param.width = LinearLayout.LayoutParams.WRAP_CONTENT;
                param.height = LinearLayout.LayoutParams.WRAP_CONTENT;
                //itemView.setVisibility(View.VISIBLE);
            }else{
                //itemView.setVisibility(View.GONE);
                param.width = 0;
                param.height = 0;
            }
            itemView.setLayoutParams(param);
        }
    }
}
