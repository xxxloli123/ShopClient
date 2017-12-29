package com.zsh.shopclient.adapter;

import android.content.Intent;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.zsh.shopclient.R;
import com.zsh.shopclient.aPresenter.BaseActivity;
import com.zsh.shopclient.aPresenter.MainActivity;
import com.zsh.shopclient.aPresenter.OrderInfoActivity;
import com.zsh.shopclient.model.KeyValue;

import java.util.HashMap;
import java.util.List;

/**
 * Created by Administrator on 2017/12/19.
 */

public class OrderAdapter extends RecyclerView.Adapter{
    private BaseActivity activity;
    private boolean end;
    private View.OnClickListener listener;
    private HashMap<String,CharSequence[]> statusValue;
    private List<KeyValue<CharSequence[],List<CharSequence[]>>> list;
    private BaseRecycleAdapter.FootLoadListener footLoadListener;
    public OrderAdapter(BaseActivity activity, List list,View.OnClickListener listener) {
        this.activity = activity;
        this.list = list;
        this.listener = listener;
        statusValue = new HashMap<>();
        statusValue.put(activity.getString(R.string.pendingPay),new CharSequence[]{activity.getString(R.string.pay)});
        statusValue.put(activity.getString(R.string.waitingBills),new CharSequence[]{activity.getString(R.string.cancelation)});
        statusValue.put(activity.getString(R.string.inYheDistribution),new CharSequence[]{activity.getString(R.string.contactWithTheRider),activity.getString(R.string.makeSurtTheGoods)});
        statusValue.put(activity.getString(R.string.offTheStocks),new CharSequence[]{activity.getString(R.string.pendingReviews)});
        statusValue.put(activity.getString(R.string.alreadyCancelation),null);
        statusValue.put(activity.getString(R.string.alreadyClose),null);
    }

    public void setFootLoadListener(BaseRecycleAdapter.FootLoadListener footLoadListener) {
        this.footLoadListener = footLoadListener;
    }

    @Override
    public int getItemCount() {
        return null == list ?0:list.size();
    }
    @Override
    public int getItemViewType(int position) {
        if(null != footLoadListener && 1+ position == getItemCount() && !end)
            footLoadListener.loadMoreListener();
        return super.getItemViewType(position);
    }
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new Vh(LayoutInflater.from(activity).inflate(R.layout.item_order,parent,false));
    }
    public void update(List list,boolean end) {
        this.end = end;
        this.list = list;
        notifyDataSetChanged();
    }
    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        holder.itemView.setId(position);
        Glide.with(activity).load(activity.getString(R.string.ip) + activity.getString(R.string.shopImagePath) + list.get(position).getKey()[0]).into(((Vh) holder).image);
        ((Vh) holder).shopName.setText(list.get(position).getKey()[1]);
        ((Vh) holder).status.setText(list.get(position).getKey()[28]);
        ((Vh) holder).time.setText(list.get(position).getKey()[26]);
        if(0< list.get(position).getValue().size()) {
            ((Vh) holder).commodityName.setText(list.get(position).getValue().get(0)[1]);
            ((Vh) holder).price.setText(activity.getString(R.string.sign) + list.get(position).getValue().get(0)[2]);
        }
        ((Vh) holder).attribute.removeAllViews();
        if (null != statusValue.get(list.get(position).getKey()[28]))
            for (int index = 0; index < statusValue.get(list.get(position).getKey()[28]).length; index++) {
                TextView textView = new TextView(activity);
                textView.setId(position);
                textView.setTag(list.get(position));
                textView.setBackgroundResource(R.drawable.shape_rectagle_contour_scarlet);
                int padding = (int) activity.getResources().getDimension(R.dimen.margin4);
                textView.setPadding(padding, padding, padding, padding);
                textView.setTextSize(activity.getResources().getDimension(R.dimen.size16));
                textView.setText(statusValue.get(list.get(position).getKey()[28])[index]);
                textView.setTextColor(ContextCompat.getColor(activity, R.color.scarlet));
                textView.setOnClickListener(listener);
                ((Vh) holder).attribute.addView(textView);
                LinearLayout.LayoutParams layoutParams = (LinearLayout.LayoutParams)textView.getLayoutParams();
                layoutParams.rightMargin = padding;
                textView.setLayoutParams(layoutParams);
            }
    }
    private class Vh extends RecyclerView.ViewHolder implements View.OnClickListener{
        ImageView image;
        TextView shopName, status, time, commodityName, price;
        LinearLayout attribute;

        public Vh(View itemView) {
            super(itemView);
            image = itemView.findViewById(R.id.image);
            shopName = itemView.findViewById(R.id.text_shopName);
            status = itemView.findViewById(R.id.text_status);
            time = itemView.findViewById(R.id.text_time);
            commodityName = itemView.findViewById(R.id.text_commodityName);
            price = itemView.findViewById(R.id.text_price);
            attribute = itemView.findViewById(R.id.attributeTag);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            HashMap hashMap = new HashMap();
            hashMap.put(list.get(itemView.getId()).getKey(),list.get(itemView.getId()).getValue());
            Intent intent = new Intent(activity, OrderInfoActivity.class);
            intent.putExtra(activity.getString(R.string.title),hashMap);
            intent.putExtra(activity.getString(R.string.location),((MainActivity)activity).getLocation());
            intent.putExtra(activity.getString(R.string.send),statusValue);
            activity.startActivity(intent);
        }
    }
}
