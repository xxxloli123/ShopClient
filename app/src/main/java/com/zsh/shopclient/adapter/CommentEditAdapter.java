package com.zsh.shopclient.adapter;

import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.zsh.shopclient.R;
import com.zsh.shopclient.aPresenter.BaseActivity;

import java.util.List;

/**
 * Created by Administrator on 2017/12/22.
 */

public class CommentEditAdapter extends RecyclerView.Adapter {
    private BaseActivity activity;
    private List<CharSequence[]>list;

    public CommentEditAdapter(BaseActivity activity, List<CharSequence[]> list) {
        this.activity = activity;
        this.list = list;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new Vh(LayoutInflater.from(activity).inflate(R.layout.item_appraise,parent,false));
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        ((Vh)holder).itemView.setId(position);
        ((Vh)holder).starLevel.setRating(Float.valueOf(list.get(position)[2].toString()));
        ((Vh)holder).input.setId(position);
        ((Vh)holder).input.setText(list.get(position)[3].toString());
        Glide.with(activity).load(activity.getString(R.string.ip)+activity.getString(R.string.shopImagePath)+list.get(position)[0]).into(((Vh)holder).image);
        ((Vh)holder).name.setText(list.get(position)[1]);
    }

    @Override
    public int getItemCount() {
        return null == list ?0: list.size();
    }
    private class Vh extends RecyclerView.ViewHolder{
        RatingBar starLevel;
        ImageView image;
        TextView name;
        EditText input;
        public Vh(final View itemView) {
            super(itemView);
            starLevel = itemView.findViewById(R.id.rating_bar_product);
            image = itemView.findViewById(R.id.image_product);
            name = itemView.findViewById(R.id.text_propertyName);
            input = itemView.findViewById(R.id.edit_shopComment);
            starLevel.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener(){
                @Override
                public void onRatingChanged(RatingBar ratingBar, float rating, boolean fromUser) {
                    list.get(itemView.getId())[2] = ""+rating;
                }
            });
            input.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {}

                @Override
                public void afterTextChanged(Editable s) {
                    list.get(input.getId())[3] = input.getText().toString().trim();
                }
            });
        }
    }
}
