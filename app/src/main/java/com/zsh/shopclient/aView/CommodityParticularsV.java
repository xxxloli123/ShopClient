package com.zsh.shopclient.aView;

import android.graphics.Paint;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.transition.Transition;
import com.zsh.shopclient.R;
import com.zsh.shopclient.aPresenter.BaseActivity;
import com.zsh.shopclient.aPresenter.CommodityParticularsActivity;
import com.zsh.shopclient.adapter.EvaluateAdapter;
import com.zsh.shopclient.adapter.LinkageTextAdapter;
import com.zsh.shopclient.tool.RecyclerLimit;
import com.zsh.shopclient.widget.RecyclerImage;

import java.util.List;
import java.util.Map;

import butterknife.BindView;

/**
 * Created by Administrator on 2017/11/18.
 */

public class CommodityParticularsV extends BaseView {
    public static final int IDsHOPPINGcART = 0;
    @BindView(R.id.image_goBack)ImageView goBack;
    @BindView(R.id.text_title)TextView title;
    @BindView(R.id.recyclerImage)RecyclerImage recyclerImage;
    @BindView(R.id.text_monthSalesVolume)TextView monthSalesVolume;
    @BindView(R.id.text_currentPrice)TextView currentPrice;
    @BindView(R.id.text_originalCost)TextView originalCost;
    @BindView(R.id.recycler_evaluate)RecyclerView recyclerEvaluate;
    @BindView(R.id.text_reveal)TextView reveal;
    @BindView(R.id.image_particulars)ImageView particularsImage;
    private PopupWindow popupWindow;
    private TextView price,type,style,total;
    private ImageView image,shoppingCart,reduce;
    private RecyclerView typeRecycler,styleRecycler;
    private void setRecyclerImageHeight(){
        WindowManager wm = ((CommodityParticularsActivity)getRootView().getContext()).getWindowManager();
        DisplayMetrics dm = new DisplayMetrics();
        wm.getDefaultDisplay().getMetrics(dm);
        RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams)recyclerImage.getLayoutParams();
        params.height = dm.widthPixels;
        recyclerImage.setLayoutParams(params);
    }
    private void setPrice(){
        String sign = getRootView().getContext().getString(R.string.sign);
        final CharSequence type = ((LinkageTextAdapter)typeRecycler.getAdapter()).getDriveKey();
        final CharSequence style = ((LinkageTextAdapter)styleRecycler.getAdapter()).getDriveKey();
        float price[] = ((CommodityParticularsActivity)getRootView().getContext()).priceRange();
        currentPrice.setText(sign +((null == type || null == style)&& price[0] != price[1] ? Math.round(price[0]*100)/100.0f+" - "+Math.round(price[1]*100)/100.0f:Float.valueOf(total.getText().toString().trim())*((CommodityParticularsActivity)getRootView().getContext()).price(type,style)));
        this.price.setText(currentPrice.getText());
    }
    public void backgroundAlpha(final float alpha){
        WindowManager.LayoutParams lp = ((BaseActivity)getRootView().getContext()).getWindow().getAttributes();
        lp.alpha = alpha;
        ((BaseActivity)getRootView().getContext()).getWindow().setAttributes(lp);
    }
    @Override
    protected int getLayoutId() {
        return R.layout.activity_commodity_particulars;
    }

    @Override
    public void initView() {
        CommodityParticularsActivity activity = (CommodityParticularsActivity) getRootView().getContext();
        CharSequence[] charSequences = activity.getIntent().getCharSequenceArrayExtra(activity.getString(R.string.title));
        View.OnClickListener listener = activity.getClickListener();
        title.setText(charSequences[1]);
        title.setTextColor(0x00000000);
        RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams)goBack.getLayoutParams();
        params.height = params.width = (int)activity.getResources().getDimension(R.dimen.margin40);
        params.setMargins((int)activity.getResources().getDimension(R.dimen.margin8),0,0,0);
        goBack.setLayoutParams(params);
        ((View)goBack.getParent()).setBackgroundColor(0x00ffffff);
        int padding = (int)activity.getResources().getDimension(R.dimen.margin8);
        goBack.setPadding(padding,padding,padding,padding);
        Drawable goBackDrawable = ContextCompat.getDrawable(activity,R.drawable.shape_annulus_gray);
        goBackDrawable.setAlpha(0xaf);
        goBack.setBackground(goBackDrawable);
        goBack.setOnClickListener(listener);
        reveal.setOnClickListener(listener);
        //((RelativeLayout)getRootView()).getChildAt(0).setOnTouchListener(activity.getSlideListener());
        shoppingCart = new ImageView(activity);
        shoppingCart.setId(IDsHOPPINGcART);
        shoppingCart.setPadding(padding,padding,padding,padding);
        shoppingCart.setBackgroundResource(R.drawable.shape_annulus_gray);
        shoppingCart.getBackground().setAlpha(0xaf);
        shoppingCart.setImageResource(R.mipmap.icon_shopping_cart_white);
        shoppingCart.setOnClickListener(listener);
        LinearLayout linearNavi = activity.findViewById(R.id.linear_navi);
        linearNavi.addView(shoppingCart);
        setRecyclerImageHeight();
        ((TextView)activity.findViewById(R.id.text_name)).setText(charSequences[1]);
        currentPrice.setText(activity.getString(R.string.sign)+charSequences[2]);
        originalCost.getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG);
        recyclerEvaluate.setLayoutManager(new LinearLayoutManager(activity,LinearLayoutManager.VERTICAL,false));
        recyclerEvaluate.addItemDecoration(new RecyclerLimit(activity,(byte)LinearLayout.HORIZONTAL));
        recyclerEvaluate.setAdapter(new EvaluateAdapter(activity,null));
        activity.findViewById(R.id.text_select).setOnClickListener(listener);
        activity.findViewById(R.id.text_addShoppingCart).setOnClickListener(listener);
        activity.findViewById(R.id.text_buyImmediately).setOnClickListener(listener);
    }

    @Override
    public void initWidget() {
        super.initWidget();
        CommodityParticularsActivity activity = (CommodityParticularsActivity)getRootView().getContext();
        View view = LayoutInflater.from(activity).inflate(R.layout.popupwindow_specification,null);
        image = view.findViewById(R.id.image);
        price = view.findViewById(R.id.text_currentPrice);
        price.setText(currentPrice.getText());
        type = view.findViewById(R.id.text_type);
        typeRecycler = view.findViewById(R.id.recycler_type);
        //typeRecycler.setLayoutManager(new GridLayoutManager(activity,4,LinearLayoutManager.VERTICAL,false));
        //typeRecycler.addItemDecoration(new RecyclerLimit(activity,RecyclerLimit.LEVELvERTICAL));
        typeRecycler.setItemAnimator(new DefaultItemAnimator());
        LinkageTextAdapter tyepAdapter = new LinkageTextAdapter(activity,null);
        typeRecycler.setAdapter(tyepAdapter);
        tyepAdapter.setItemOnClickListener(activity.getTypeItemClick());
        style = view.findViewById(R.id.text_style);
        styleRecycler = view.findViewById(R.id.recycler_style);
        styleRecycler.setLayoutManager(new GridLayoutManager(activity,4,LinearLayoutManager.VERTICAL,false));
        //styleRecycler.addItemDecoration(new RecyclerLimit(activity,RecyclerLimit.LEVELvERTICAL));
        styleRecycler.setItemAnimator(new DefaultItemAnimator());
        LinkageTextAdapter styleAdapter = new LinkageTextAdapter(activity,null);
        styleAdapter.setItemOnClickListener(activity.getStyleItemClick());
        styleRecycler.setAdapter(styleAdapter);
        View.OnClickListener listener = activity.getClickListener();
        reduce = view.findViewById(R.id.image_reduce);
        reduce.setOnClickListener(listener);
        total = view.findViewById(R.id.text_quantity);
        total.setText("1");
        view.findViewById(R.id.image_add).setOnClickListener(listener);
        view.findViewById(R.id.text_confirm).setOnClickListener(listener);
        DisplayMetrics dm = new DisplayMetrics();
        activity.getWindowManager().getDefaultDisplay().getMetrics(dm);
        popupWindow = new PopupWindow(view, ViewGroup.LayoutParams.MATCH_PARENT,dm.heightPixels/5*4,true);
        popupWindow.setOutsideTouchable(true);
        popupWindow.setTouchable(true);
        popupWindow.setBackgroundDrawable(new ColorDrawable(ContextCompat.getColor(activity,R.color.lucency)));
        popupWindow.setAnimationStyle(R.style.BottomAlphaAnimation);
        popupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                backgroundAlpha(1.0f);
            }
        });
    }
    public void setTitleBar(){
        Log.d("CommodityParticularsV","y->"+((ScrollView)recyclerImage.getParent().getParent()).getScrollY());
        int y = ((ScrollView)recyclerImage.getParent().getParent()).getScrollY();
        if(y <0xff) {
            ((RelativeLayout) title.getParent()).setBackgroundColor(y <<24| ContextCompat.getColor(getRootView().getContext(), R.color.lucencyScarlet));
            if(y < 0xaf) {
                goBack.getBackground().setAlpha(0xaf -y);
                shoppingCart.getBackground().setAlpha(0xaf -y);
            }
            title.setTextColor(y <<24| 0x00ffffff);
        }
    }
    public void setReveal(){
        ((EvaluateAdapter)recyclerEvaluate.getAdapter()).setDisplayOne();
        String text = getRootView().getContext().getString(R.string.allReveal);
        Drawable drawable = null;
        if(text.equals(reveal.getText().toString().trim())) {
            text = getRootView().getContext().getString(R.string.conceal);
            drawable = ContextCompat.getDrawable(getRootView().getContext(), R.mipmap.icon_triangle_up_red);
        }else
            drawable = ContextCompat.getDrawable(getRootView().getContext(), R.mipmap.icon_triangle_down_black);
        drawable.setBounds(0,0,drawable.getMinimumWidth(),drawable.getMinimumHeight());
        reveal.setCompoundDrawables(null, null, drawable, null);
        reveal.setText(text);
    }
    public void setTotal(int count){
        int quantity = getTotal();
        int add = 1 == quantity && 0> count ? quantity: quantity + count;
        total.setText(""+add);
        reduce.setVisibility(1< add ? View.VISIBLE : View.GONE);
        String sign = getRootView().getContext().getString(R.string.sign);
        final CharSequence type = ((LinkageTextAdapter)typeRecycler.getAdapter()).getDriveKey();
        final CharSequence style = ((LinkageTextAdapter)styleRecycler.getAdapter()).getDriveKey();
        float price[] = ((CommodityParticularsActivity)getRootView().getContext()).priceRange();
        this.price.setText(sign +(null == type || null == style ? price[0] +" - "+price[1] :Math.round(add * ((CommodityParticularsActivity)getRootView().getContext()).price(type,style) *100)/100.0f));
    }
    public int getTotal(){
        return Integer.valueOf(total.getText().toString().trim());
    }
    public float getCurrentPrice(){
        String string = currentPrice.getText().toString().substring(1).trim();
        return Float.valueOf(-1 != string.indexOf('-')? string.substring(0,string.indexOf('-')):string);
    }
    public CharSequence setTypeAdapterKey(){
        CharSequence charSequence = ((LinkageTextAdapter)styleRecycler.getAdapter()).getDriveKey();
        ((LinkageTextAdapter)typeRecycler.getAdapter()).setKey(charSequence);
        setPrice();
        return charSequence;
    }
    public CharSequence setStyleAdapterKey(){
        CharSequence charSequence = ((LinkageTextAdapter)typeRecycler.getAdapter()).getDriveKey();
        ((LinkageTextAdapter)styleRecycler.getAdapter()).setKey(charSequence);
        setPrice();
        return charSequence;
    }
    public void initImage(final List<String> list, final String particulars){
        ((CommodityParticularsActivity)getRootView().getContext()).runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if(null == recyclerImage)
                    return;
                final String url = getRootView().getContext().getString(R.string.ip) + getRootView().getContext().getString(R.string.pictureLibraryPath);
                for(String path : list)
                    recyclerImage.addImageUrl(url + path);
                recyclerImage.commit();
                Glide.with(getRootView().getContext()).load(url + particulars).apply(new RequestOptions().centerCrop().placeholder(R.mipmap.ic_launcher)).into(new SimpleTarget<Drawable>() {
                    @Override
                    public void onResourceReady(Drawable resource, Transition<? super Drawable> transition) {
                        ViewGroup.LayoutParams params = particularsImage.getLayoutParams();
                        Log.i("CommodityParticularsV","initImage-width->"+resource.getIntrinsicWidth()+"-"+resource.getIntrinsicHeight());
                        int width = particularsImage.getWidth();
                        float scale = (float) width / (float) resource.getIntrinsicWidth();
                        int height = Math.round(resource.getIntrinsicHeight() * scale);
                        params.height = height;
                        particularsImage.setLayoutParams(params);
                        particularsImage.setImageDrawable(resource);
                    }
                });
                Glide.with(getRootView().getContext()).load(url+list.get(0)).into(image);
            }
        });
    }
    public void setPrice(final Map<CharSequence,List<CharSequence>>[] arrayMap, final String monthSalesVolume,final float... prices){
        final String current = prices[0] != prices[1] ? Math.round(prices[0]*100)/100.0f +" - "+ Math.round(prices[1]*100)/100.0f : "" + Math.round(prices[0]*100)/100.0f;
        //final String original = originalCostMin != originalCostMax ? originalCostMin + " - "+ originalCostMax : "" + originalCostMin;
        ((CommodityParticularsActivity)getRootView().getContext()).runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if(null == currentPrice)
                    return;
                String sign = getRootView().getContext().getString(R.string.sign);
                currentPrice.setText(sign + current);
                price.setText(sign + current);
                //originalCost.setVisibility(View.VISIBLE);
                //originalCost.setText(sign + original);
                CommodityParticularsV.this.monthSalesVolume.setText(getRootView().getContext().getString(R.string.monthSalesVolume)+monthSalesVolume);
                typeRecycler.setLayoutManager(new GridLayoutManager(getRootView().getContext(),0< arrayMap[0].size()&& arrayMap[0].size()<4 ? arrayMap[0].size():4,LinearLayoutManager.VERTICAL,false));
                styleRecycler.setLayoutManager(new GridLayoutManager(getRootView().getContext(),0< arrayMap[1].size()&& arrayMap[1].size()<4 ? arrayMap[1].size():4,LinearLayoutManager.VERTICAL,false));
                ((LinkageTextAdapter)typeRecycler.getAdapter()).update(arrayMap[0]);
                ((LinkageTextAdapter)styleRecycler.getAdapter()).update(arrayMap[1]);
            }
        });
    }
    public void initEvaluate(final List<CharSequence[]>list){
        ((CommodityParticularsActivity)getRootView().getContext()).runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if(null == reduce)
                    return;
                if(0== list.size()) {
                    reveal.setText(R.string.noComments);
                    reveal.setClickable(false);
                }
                ((EvaluateAdapter)recyclerEvaluate.getAdapter()).update(list,false);
            }
        });
    }
    public void displayPopupWindow(){
        backgroundAlpha(0.6f);
        popupWindow.showAtLocation(getRootView(), Gravity.BOTTOM,0,0);
    }
    public void dismiss(){
        ((CommodityParticularsActivity)getRootView().getContext()).runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if(null == popupWindow)return;
                popupWindow.dismiss();
            }
        });
    }

    @Override
    public void onDestroy() {
        recyclerImage.releaseResource();
        super.onDestroy();
    }
}
