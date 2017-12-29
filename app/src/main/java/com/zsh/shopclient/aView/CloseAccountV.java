package com.zsh.shopclient.aView;

import android.support.annotation.StringRes;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.AbsoluteSizeSpan;
import android.text.style.ForegroundColorSpan;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.zsh.shopclient.R;
import com.zsh.shopclient.aPresenter.CloseAccountActivity;
import com.zsh.shopclient.adapter.CommodityAdapter;
import com.zsh.shopclient.popupWindow.PayPopupWindow;
import com.zsh.shopclient.tool.RecyclerLimit;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;

/**
 * Created by Administrator on 2017/12/3.
 */

public class CloseAccountV extends BaseView {
    @BindView(R.id.text_homeDelivery)TextView homeDelivery;
    @BindView(R.id.text_toShop)TextView toShop;
    @BindView(R.id.text_linkmanContacts)TextView linkmanContacts;
    @BindView(R.id.text_deliveryTime)TextView deliveryTime;
    @BindView(R.id.recycler)RecyclerView recycler;
    @BindView(R.id.text_discountCoupon)TextView discountCoupon;
    @BindView(R.id.text_enjoyActivitys)TextView enjoyActivitys;
    @BindView(R.id.text_packingExpense)TextView packingExpense;
    @BindView(R.id.text_freight)TextView freight;
    @BindView(R.id.text_freeShipping)TextView freeShipping;
    @BindView(R.id.text_remark)EditText remark;
    private TextView pay;
    private SpannableString mergeText(@StringRes int key,@StringRes int hint){
        String deliveryTimeStr = getRootView().getContext().getString(key)+"\t";
        String asSoonAsPossible = -1 == hint ? "":getRootView().getContext().getString(hint);
        SpannableString ss = new SpannableString(deliveryTimeStr+asSoonAsPossible);
        ss.setSpan(new AbsoluteSizeSpan((int)getRootView().getContext().getResources().getDimension(R.dimen.size16)),deliveryTimeStr.length(),ss.length(),Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        ss.setSpan(new ForegroundColorSpan(ContextCompat.getColor(getRootView().getContext(),R.color.scarlet)),deliveryTimeStr.length(),ss.length(),Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        return ss;
    }
    @Override
    protected int getLayoutId() {
        return R.layout.activity_close_account;
    }

    @Override
    public void initView() {
        CloseAccountActivity activity = (CloseAccountActivity) getRootView().getContext();
        View.OnClickListener listener = activity.getClickListener();
        activity.findViewById(R.id.image_goBack).setOnClickListener(listener);
        ((TextView)getRootView().findViewById(R.id.text_title)).setText(R.string.closeAccount);
        CharSequence[] array = ((Map<CharSequence[],List<CharSequence[]>>)activity.getIntent().getExtras().get(activity.getString(R.string.send))).keySet().iterator().next();
        if(!"yes".equals(array[7]))
            toShop.setVisibility(View.GONE);
        if(!"yes".equals(array[8]))
            homeDelivery.setVisibility(View.GONE);
        homeDelivery.setOnClickListener(listener);
        homeDelivery.setClickable(false);
        toShop.setOnClickListener(listener);
        linkmanContacts.setOnClickListener(listener);
        deliveryTime.setOnClickListener(listener);
        deliveryTime.setText(mergeText(R.string.deliveryTime,R.string.asSoonAsPossible));
        TextView shopName = getRootView().findViewById(R.id.text_shopName);
        shopName.setOnClickListener(listener);
        shopName.setText(array[1]);
        recycler.setLayoutManager(new LinearLayoutManager(activity,LinearLayoutManager.VERTICAL,false));
        recycler.addItemDecoration(new RecyclerLimit(getRootView().getContext(),(byte)LinearLayout.HORIZONTAL));
        recycler.setItemAnimator(new DefaultItemAnimator());
        CommodityAdapter adapter = new CommodityAdapter(activity,null,false);
        adapter.setItemOnClickListener(activity.getInfoListener());
        recycler.setAdapter(adapter);
        discountCoupon.setOnClickListener(listener);
        freeShipping.setVisibility(0== ((HashMap<String,List<CharSequence[]>>)activity.getIntent().getExtras().get(activity.getString(R.string.sendTail))).size() ? View.GONE :View.VISIBLE);
        pay = (TextView) ((LinearLayout)getRootView().findViewById(R.id.linear)).getChildAt(0);
        getRootView().findViewById(R.id.text_freeShipping).setOnClickListener(listener);
        getRootView().findViewById(R.id.text_pay).setOnClickListener(listener);
    }
    public void selected(boolean isHomeDelivery){
        linkmanContacts.setText(R.string.linkmanContacts);
        if(isHomeDelivery){
            homeDelivery.setTextColor(ContextCompat.getColor(getRootView().getContext(),R.color.scarlet));
            homeDelivery.setTextSize(getRootView().getResources().getDimension(R.dimen.size20));
            homeDelivery.setClickable(false);
            toShop.setClickable(true);
            toShop.setTextColor(ContextCompat.getColor(getRootView().getContext(),R.color.textDarkgray));
            toShop.setTextSize(getRootView().getResources().getDimension(R.dimen.size16));
            deliveryTime.setText(mergeText(R.string.deliveryTime,R.string.asSoonAsPossible));
            packingExpense.setVisibility(View.VISIBLE);
            freight.setVisibility(View.VISIBLE);
            freeShipping.setVisibility(View.VISIBLE);
        }else{
            homeDelivery.setTextColor(ContextCompat.getColor(getRootView().getContext(),R.color.textDarkgray));
            homeDelivery.setTextSize(getRootView().getResources().getDimension(R.dimen.size16));
            homeDelivery.setClickable(true);
            toShop.setClickable(false);
            toShop.setTextColor(ContextCompat.getColor(getRootView().getContext(),R.color.scarlet));
            toShop.setTextSize(getRootView().getResources().getDimension(R.dimen.size20));
            deliveryTime.setText(mergeText(R.string.arrivalTime,-1));
            packingExpense.setVisibility(View.GONE);
            freight.setVisibility(View.GONE);
            freeShipping.setVisibility(View.GONE);
        }
    }
    public boolean isClickable(){
        return homeDelivery.isClickable();
    }
    public void updateConsignee(final CharSequence[] target){
        ((CloseAccountActivity)getRootView().getContext()).runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if(null == getRootView())return;
                String hint = getRootView().getContext().getString(R.string.linkmanContacts) + "\t";
                if(!homeDelivery.isClickable()) {
                    int firstStart = hint.length();
                    String addres = hint + "\t" + target[0] + "(" + target[1] + ")\n" + getRootView().getContext().getString(R.string.site) + "\t\t";
                    int addressBegin = addres.length();
                    SpannableString ss = new SpannableString(addres + target[2]);
                    ss.setSpan(new AbsoluteSizeSpan((int) getRootView().getContext().getResources().getDimension(R.dimen.size16)), firstStart, firstStart + target[0].length() + target[1].length() + 3, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                    ss.setSpan(new ForegroundColorSpan(ContextCompat.getColor(getRootView().getContext(), R.color.scarlet)), firstStart, firstStart + target[0].length() + target[1].length() + 3, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                    ss.setSpan(new AbsoluteSizeSpan((int) getRootView().getContext().getResources().getDimension(R.dimen.size16)), addressBegin, ss.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                    ss.setSpan(new ForegroundColorSpan(ContextCompat.getColor(getRootView().getContext(), R.color.scarlet)), addressBegin, ss.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                    linkmanContacts.setText(ss);
                }else if(!"".equals(target[0])){
                    int firstStart = hint.length();
                    String contactPhone = hint + "\t" + target[0] + "(" + target[1] + ")"+target[2];
                    SpannableString ss = new SpannableString(contactPhone);
                    ss.setSpan(new AbsoluteSizeSpan((int) getRootView().getContext().getResources().getDimension(R.dimen.size16)), firstStart, contactPhone.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                    ss.setSpan(new ForegroundColorSpan(ContextCompat.getColor(getRootView().getContext(), R.color.scarlet)), firstStart, contactPhone.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                    linkmanContacts.setText(ss);
                }
            }
        });
    }
    public void updateDeliveryTime(String timeStr){
        String deliveryTimeStr = getRootView().getContext().getString(homeDelivery.isSelected() ? R.string.deliveryTime :R.string.arrivalTime)+"\t";
        SpannableString ss = new SpannableString(deliveryTimeStr+"\t"+ timeStr);
        ss.setSpan(new AbsoluteSizeSpan((int)getRootView().getContext().getResources().getDimension(R.dimen.size16)),deliveryTimeStr.length(),ss.length(),Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        ss.setSpan(new ForegroundColorSpan(ContextCompat.getColor(getRootView().getContext(),R.color.scarlet)),deliveryTimeStr.length(),ss.length(),Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        deliveryTime.setText(ss);
    }
    public void updateAdapter(final List<CharSequence[]> list,final float price){
        ((CloseAccountActivity)getRootView().getContext()).runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if(null == getRootView())return;
                ((CommodityAdapter) recycler.getAdapter()).update(list,false);
                pay.setText(getRootView().getContext().getString(R.string.sign)+ price);
            }
        });
    }
    public void updateDiscountCoupon(final int total){
        ((CloseAccountActivity)getRootView().getContext()).runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if(null == getRootView())return;
                discountCoupon.setText(discountCoupon.getText().toString().trim()+": "+ total + getRootView().getContext().getString(R.string.sheet));
            }
        });
    }
    public void updateRest(final CharSequence... rest){
        ((CloseAccountActivity)getRootView().getContext()).runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if(null == getRootView())return;
                float packFree = Math.round(Float.valueOf((String)rest[1])*100)/100.0f;
                float postage = Math.round(Float.valueOf((String)rest[2])*100)/100.0f;
                enjoyActivitys.setText(enjoyActivitys.getText().toString().trim()+"\t" + rest[0]);
                packingExpense.setText(packingExpense.getText().toString().trim()+"\t" +  rest[1]);
                //freight.setText(freight.getText().toString().trim()+"\t" +  rest[2]);
                //pay.setText(getRootView().getContext().getString(R.string.sign)+(Float.valueOf(pay.getText().toString().substring(1).trim())+packFree + postage));
            }
        });
    }
    public String getRemark(){
        return remark.getText().toString().trim();
    }
    public void addFree(float restCost){
        pay.setText(getRootView().getContext().getString(R.string.sign)+ (Float.valueOf(pay.getText().toString().substring(1).trim()) + restCost));
    }
    public void setFreight(final float... free){
        if(0 == free.length)return;
        final String carriage = Math.round(free[0]*100)/100.00f + "";
        final String distance = free[1]< 1.0f ? Math.round(free[1]*1000) +"m" : free[1]+"km";
        ((CloseAccountActivity)getRootView().getContext()).runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if(null == getRootView())return;
                String hint = getRootView().getContext().getString(R.string.freight)+"\t";
                String price = hint + getRootView().getContext().getString(R.string.sign)+ carriage;
                SpannableString ss = new SpannableString(price +"\t\t"+ getRootView().getContext().getString(R.string.distance)+ distance);
                ss.setSpan(new AbsoluteSizeSpan((int)getRootView().getResources().getDimension(R.dimen.size16)),hint.length(),price.length(),Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                ss.setSpan(new ForegroundColorSpan(ContextCompat.getColor(getRootView().getContext(),R.color.scarlet)),hint.length(),price.length(),Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                freight.setText(ss);
                pay.setText(getRootView().getContext().getString(R.string.sign)+(Math.round(free[2]*100)/100.00f));
            }
        });
    }
    public void payPopupWindow(final Map<String,String> map){
        ((CloseAccountActivity)getRootView().getContext()).runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if(null == getRootView())return;
                new PayPopupWindow((CloseAccountActivity)getRootView().getContext(),map,null).displayPopupWindow();
            }
        });
    }
}
