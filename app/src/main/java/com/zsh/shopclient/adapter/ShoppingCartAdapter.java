package com.zsh.shopclient.adapter;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.zsh.shopclient.R;
import com.zsh.shopclient.aPresenter.BaseActivity;
import com.zsh.shopclient.aPresenter.CommodityParticularsActivity;
import com.zsh.shopclient.aPresenter.ShopActivity;

import java.util.List;

/**
 * Created by Administrator on 2017/12/14.
 */

public class ShoppingCartAdapter extends RecyclerView.Adapter {
    private static final int HEADLINE = 1;
    private BaseActivity activity;
    private List<CharSequence[]> list;
    private EmptyListener emptyListener;
    private CommodityAdapter.ItemOnClickListener itemOnClickListener;

    public ShoppingCartAdapter(BaseActivity activity, List<CharSequence[]> list) {
        this.activity = activity;
        this.list = list;
    }

    @Override
    public int getItemViewType(int position) {
        return 8== list.get(position).length ? super.getItemViewType(position): HEADLINE;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return HEADLINE == viewType ? new HeadlineVh(LayoutInflater.from(activity).inflate(R.layout.item_left_icon_right_text,parent,false))
        :new Vh(LayoutInflater.from(activity).inflate(R.layout.item_shopping_cart,parent,false));
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        holder.itemView.setId(position);
        if(8== list.get(position).length){
            Glide.with(activity).load(activity.getString(R.string.ip)+activity.getString(R.string.path)+list.get(position)[0]).into(((Vh) holder).image);
            ((Vh)holder).name.setText(list.get(position)[1]);
            ((Vh)holder).property.setText(list.get(position)[5]+"/"+list.get(position)[6]);
            ((Vh)holder).price.setText(activity.getString(R.string.sign)+ list.get(position)[2]);
            ((Vh)holder).count.setText(list.get(position)[4]);
        }else
            ((TextView)((RelativeLayout)holder.itemView).getChildAt(1)).setText(list.get(position)[1]);
    }

    @Override
    public int getItemCount() {
        return null == list ?0: list.size();
    }
    public void setItemOnClickListener(CommodityAdapter.ItemOnClickListener listener,EmptyListener emptyListener){
        itemOnClickListener = listener;
        this.emptyListener = emptyListener;
    }
    public void update(List<CharSequence[]>list){
        this.list = list;
        notifyDataSetChanged();
    }
    private class HeadlineVh extends RecyclerView.ViewHolder implements View.OnClickListener{
        public HeadlineVh(View itemView) {
            super(itemView);
            ((ImageView)((RelativeLayout)itemView).getChildAt(0)).setImageResource(R.mipmap.icon_shop);
            Drawable drawable = ContextCompat.getDrawable(activity,R.mipmap.icon_right);
            drawable.setBounds(0,0,drawable.getMinimumWidth(),drawable.getMinimumHeight());
            ((TextView)((RelativeLayout)itemView).getChildAt(1)).setCompoundDrawables(null,null,drawable,null);
            ((TextView)((RelativeLayout)itemView).getChildAt(1)).setCompoundDrawablePadding((int)activity.getResources().getDimension(R.dimen.margin8));
            ImageView empty = new ImageView(activity);
            empty.setId(R.id.emptyShoppingCart);
            empty.setOnClickListener(this);
            empty.setImageResource(R.mipmap.icon_empty);
            ((RelativeLayout)itemView).addView(empty);
            RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) empty.getLayoutParams();
            params.width = RelativeLayout.LayoutParams.WRAP_CONTENT;
            params.height = (int)activity.getResources().getDimension(R.dimen.margin32);
            params.rightMargin = (int)activity.getResources().getDimension(R.dimen.margin16);
            params.alignWithParent = true;
            params.addRule(RelativeLayout.CENTER_VERTICAL);
            params.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
            empty.setLayoutParams(params);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            switch (v.getId()){
                case R.id.emptyShoppingCart:
                    if(null != emptyListener)
                        emptyListener.emptyCart(list.get(itemView.getId()));
                    break;
                default:
                    Intent intent = new Intent(activity,ShopActivity.class);
                    intent.putExtra(activity.getString(R.string.title),list.get(itemView.getId()));
                    activity.startActivity(intent);
            }
        }
    }
    private class Vh extends RecyclerView.ViewHolder implements View.OnClickListener{
        ImageView image,reduce,add;
        TextView name,property,price,count;
        public Vh(View itemView) {
            super(itemView);
            itemView.setOnClickListener(this);
            image = itemView.findViewById(R.id.image_commodity);
            name = itemView.findViewById(R.id.text_name);
            property = itemView.findViewById(R.id.text_property);
            price = itemView.findViewById(R.id.text_price);
            reduce = itemView.findViewById(R.id.image_reduce);
            reduce.setOnClickListener(this);
            count = itemView.findViewById(R.id.text_quantity);
            add = itemView.findViewById(R.id.image_add);
            add.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            switch (v.getId()){
                case R.id.image_reduce:
                    if(null != itemOnClickListener)
                        itemOnClickListener.onClickReduce(itemView,list.get(itemView.getId()));
                    break;
                case R.id.image_add:
                    if(null != itemOnClickListener)
                        itemOnClickListener.onClickAdd(itemView,list.get(itemView.getId()));
                    break;
                default:
                    Intent intent = new Intent(activity, CommodityParticularsActivity.class);
                    intent.putExtra(activity.getString(R.string.title),list.get(itemView.getId()));
                    activity.startActivity(intent);
            }
        }
    }
    public interface EmptyListener{
        void emptyCart(CharSequence[] array);
    }
}
