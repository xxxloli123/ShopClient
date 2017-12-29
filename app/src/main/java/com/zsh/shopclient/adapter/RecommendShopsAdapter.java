package com.zsh.shopclient.adapter;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.zsh.shopclient.R;
import com.zsh.shopclient.aPresenter.BaseActivity;
import com.zsh.shopclient.aPresenter.ShopActivity;
import com.zsh.shopclient.tool.Verification;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2017/11/7.
 */

public class RecommendShopsAdapter extends RecyclerView.Adapter {
    private Activity activity;
    private List<Map<Map<String,Object>,List<Map<String,Object>>>> list;
    private boolean isEnd;
    private BaseRecycleAdapter.FootLoadListener footLoadListener;

    public RecommendShopsAdapter(Activity activity, List<Map<Map<String,Object>,List<Map<String,Object>>>> list) {
        this.activity = activity;
        this.list = list;
    }
    @Override
    public int getItemViewType(int position) {
        if(null != footLoadListener &&  isEnd && 1+ position == getItemCount())
            footLoadListener.loadMoreListener();
        return super.getItemViewType(position);
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new Vh(LayoutInflater.from(activity).inflate(R.layout.item_recommend_shop,parent,false));
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        Map<Map<String,Object>,List<Map<String,Object>>> data = list.get(position);
        Map<String,Object> keyObj = data.keySet().iterator().next();
        List<Map<String,Object>> values = data.get(keyObj);
        CharSequence[] array = new CharSequence[11];
        array[0] = (String) keyObj.get("shopImage");
        array[1] = (String)keyObj.get("shopName");
        array[2] = (String)keyObj.get("lineConsume");
        array[3] = (String)keyObj.get("delivery");
        array[4] = (String)keyObj.get("deliveryFee");
        array[5] = (String)keyObj.get("grade");
        array[6] = (String)keyObj.get("ordercount");
        array[7] = (String)keyObj.get("lng");
        array[8] = (String)keyObj.get("lat");
        array[9] = (String)keyObj.get("id");
        array[10] = (String)keyObj.get("shopUserDistance");
        //holder.itemView.setTag(array);刷新报异常You must not call setTag() on a view Glide is targeting
        Glide.with(activity).load(activity.getString(R.string.ip) + activity.getString(R.string.shopImagePath) + array[0]).into(((Vh) holder).logo);
        List<CharSequence[]> list = getActivitys(values);
        //((Vh) holder).logo.setTag(list);
        ((Vh)holder).name.setText(array[1]);
        ((Vh)holder).name.setTag(array);
        ((Vh)holder).rating.setRating(Verification.isNumeric((String)keyObj.get("grade"))? Integer.valueOf((String)keyObj.get("grade")):0);
        ((Vh)holder).rating.setTag(list);
        ((Vh)holder).monthSalesVolume.setText(activity.getString(R.string.monthSalesVolume)+keyObj.get("ordercount")+activity.getString(R.string.one));
        ((Vh)holder).freight.setText(activity.getString(R.string.freight)+keyObj.get("deliveryFee"));
        float distance = Float.valueOf((String) keyObj.get("shopUserDistance"));
        ((Vh)holder).meter.setText(1< distance ? distance + activity.getString(R.string.meterKm): distance*1000+ activity.getString(R.string.meter));
        ((RelativeLayout)((Vh) holder).meter.getParent()).setTag(position);
        if(null != values && 0< values.size()) {
            ((RelativeLayout)((Vh) holder).recycler.getParent()).setVisibility(View.VISIBLE);
            (((Vh) holder).quantity).setText(values.size()+ activity.getString(R.string.quantityActivity));
            ((Vh)holder).recycler.setLayoutManager(new LinearLayoutManager(activity,LinearLayoutManager.VERTICAL,false));
            ((Vh)holder).recycler.setItemAnimator(new DefaultItemAnimator());
            ((Vh)holder).recycler.setAdapter(new LeftImageRightTextAdapter(activity,list));
        }else
            ((RelativeLayout)((Vh) holder).recycler.getParent()).setVisibility(View.GONE);
    }
    @Override
    public int getItemCount() {
        return null == list ? 0 : list.size();
    }
    public void update(List<Map<Map<String,Object>,List<Map<String,Object>>>> list,boolean isRefresh){
        isEnd = !isRefresh && 0 == list.size();
        if(null == this.list || isRefresh)
            this.list = list;
        else
            this.list.addAll(list);
        notifyDataSetChanged();
    }

    public void setFootLoadListener(BaseRecycleAdapter.FootLoadListener footLoadListener) {
        this.footLoadListener = footLoadListener;
    }

    private List<CharSequence[]>getActivitys(List<Map<String,Object>> maps){
        List<CharSequence[]> list = new ArrayList<>();
        for(Map<String,Object> map : maps)
            list.add(new CharSequence[]{(CharSequence) map.get("activityName")});
        return list;
    }
    private class Vh extends RecyclerView.ViewHolder implements View.OnClickListener{
        ImageView logo;
        RatingBar rating;
        TextView name,monthSalesVolume,freight,meter,quantity;
        RecyclerView recycler;
        public Vh(View itemView) {
            super(itemView);
            logo = itemView.findViewById(R.id.image_logo);
            name = itemView.findViewById(R.id.text_name);
            rating = itemView.findViewById(R.id.rating);
            monthSalesVolume = itemView.findViewById(R.id.text_monthSalesVolume);
            freight = itemView.findViewById(R.id.text_freight);
            meter = itemView.findViewById(R.id.text_meter);
            ((RelativeLayout)meter.getParent()).setOnClickListener(this);
            quantity = itemView.findViewById(R.id.text_activityQuantity);
            quantity.setOnClickListener(this);
            recycler = itemView.findViewById(R.id.recycler);
        }

        @Override
        public void onClick(View v) {
            switch (v.getId()){
                case R.id.text_activityQuantity:
                    ((LeftImageRightTextAdapter)recycler.getAdapter()).setDisplayOne();
                    break;
                default:
                    Intent intent = activity.getIntent();
                    intent.setClass(activity, ShopActivity.class);
                    intent.putExtra(activity.getString(R.string.requestParam),"GeneralGoods");
                    intent.putExtra(activity.getString(R.string.title),(CharSequence[]) name.getTag());
                    activity.startActivity(intent);
                    break;
            }
        }
    }
}
