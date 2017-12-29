package com.zsh.shopclient.adapter;

import android.app.Activity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.zsh.shopclient.R;
import com.zsh.shopclient.aPresenter.BaseActivity;
import com.zsh.shopclient.tool.Verification;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2017/11/22.
 */

public class EvaluateAdapter extends BaseRecycleAdapter<CharSequence[]> {
    private boolean displayOne;

    public EvaluateAdapter(Activity activity,List<CharSequence[]> list) {
        super(activity, list);
        displayOne = true;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if(TYPE_FOOTER == viewType)
            return super.onCreateViewHolder(parent,viewType);
        return new Vh(LayoutInflater.from(getActivity()).inflate(R.layout.item_evaluate,parent,false));
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if(getItemCount() -1 > position) {
            holder.itemView.setId(position);
            Glide.with(getActivity()).load(getActivity().getString(R.string.ip)+getActivity().getString(R.string.userHead)+getPosition(position)[0]).into(((Vh) holder).image);
            ((Vh) holder).name.setText(getPosition(position)[1]);
            ((Vh) holder).time.setText(getPosition(position)[2]);
            ((Vh) holder).ratingBar.setRating(Verification.isNumeric((String) getPosition(position)[3]) ? Integer.valueOf((String) getPosition(position)[3]) : 0);
            ((Vh) holder).evaluate.setText(getPosition(position)[4]);
        }else if(isEnd())
            setVisibility((FootViewHolder)holder,View.GONE);
        else
            setVisibility((FootViewHolder)holder,View.VISIBLE);
    }

    @Override
    public int getItemCount() {
        if(displayOne)
            return 0== size() ?0:1;
        return super.getItemCount();
    }
    @Override
    public void update(List list, boolean isReplace) {
        if(null == list || 0== list.size())
            setEnd(true);
        else if(isNull() || isReplace)
            setDatas(list);
        else
            addAll(list);
        notifyDataSetChanged();
    }
    public void setDisplayOne(){
        displayOne = !displayOne;
        notifyDataSetChanged();
    }
    private class Vh extends RecyclerView.ViewHolder{
        ImageView image;
        RatingBar ratingBar;
        TextView name,time,evaluate;
        public Vh(View itemView) {
            super(itemView);
            image = itemView.findViewById(R.id.image_head);
            name = itemView.findViewById(R.id.text_name);
            time = itemView.findViewById(R.id.text_time);
            ratingBar = itemView.findViewById(R.id.rating_bar);
            evaluate = itemView.findViewById(R.id.text_evaluate);
        }
    }
}
