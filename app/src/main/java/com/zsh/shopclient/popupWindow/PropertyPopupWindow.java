package com.zsh.shopclient.popupWindow;

import android.graphics.drawable.ColorDrawable;
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
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.xxx.skynet.sharedPreference.IOSharedPreferences;
import com.zsh.shopclient.R;
import com.zsh.shopclient.aPresenter.BaseActivity;
import com.zsh.shopclient.aView.BaseView;
import com.zsh.shopclient.adapter.LinkageTextAdapter;
import com.zsh.shopclient.adapter.TextAdapter;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by Administrator on 2017/12/1.
 */

public class PropertyPopupWindow {
    @BindView(R.id.image)ImageView image;
    @BindView(R.id.text_currentPrice)TextView currentPrice;
    @BindView(R.id.text_type)TextView type;
    @BindView(R.id.recycler_type)RecyclerView typeRecycler;
    @BindView(R.id.text_style)TextView style;
    @BindView(R.id.recycler_style)RecyclerView styleRecycler;
    @BindView(R.id.text_quantity)TextView total;
    @BindView(R.id.image_reduce)ImageView reduce;
    private BaseActivity activity;
    private PopupWindow popupWindow;
    private final CharSequence[] info;
    private final List<CharSequence[]>list;
    private CharSequence[] charSequences;
    private Submit stubmit;
    private View.OnClickListener clickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()){
                case R.id.image_reduce:
                    setTotal(-1);
                    break;
                case R.id.image_add:
                    setTotal(1);
                    break;
                case R.id.text_confirm:
                    if(null != stubmit)
                        stubmit.commit(charSequences,Integer.valueOf(total.getText().toString().trim()));
                        break;
            }
        }
    };
    public PropertyPopupWindow(final BaseActivity activity,CharSequence[] info ,List<CharSequence[]> attribute) {
        this.activity = activity;
        this.info = info;
        list = attribute;
        charSequences = new CharSequence[2];
        Map<CharSequence,List<CharSequence>>[] arrayMap = new Map[2];
        arrayMap[0] = new LinkedHashMap<CharSequence, List<CharSequence>>();
        arrayMap[1] = new LinkedHashMap<CharSequence, List<CharSequence>>();
        if(null != attribute)
            for(CharSequence[] array : attribute){
                getValue(arrayMap[0],array[0],array[1]);
                getValue(arrayMap[1],array[1],array[0]);
            }
        initView(arrayMap);
        float[] prices = priceRange();
        currentPrice.setText(activity.getString(R.string.sign)+(prices[0] != prices[1] ? Math.round(prices[0]*100)/100.0f +" - "+ Math.round(prices[1]*100)/100.0f : "" + Math.round(prices[0]*100)/100.0f));
    }

    public void displayPopupWindow(){
        //Glide.with(activity).load(activity.getString(R.string.ip)+imagePaths.get(0)).into(image);
        //currentPrice.setText(info[]);
        backgroundAlpha(0.6f);
        popupWindow.showAtLocation(activity.findViewById(android.R.id.content), Gravity.BOTTOM,0,0);//Activity.getWindow.getDecorView()获得activity的rootView
    }
    public void dismiss(){
        popupWindow.dismiss();
    }
    public void setStubmit(Submit stubmit){
        this.stubmit = stubmit;
    }
    private void setTotal(int count){
        int quantity = Integer.valueOf(total.getText().toString().trim());
        int add = 1 == quantity && 0> count ? quantity: quantity + count;
        total.setText(""+add);
        reduce.setVisibility(1< add ? View.VISIBLE : View.GONE);
        String sign = activity.getString(R.string.sign);
        final CharSequence type = ((LinkageTextAdapter)typeRecycler.getAdapter()).getDriveKey();
        final CharSequence style = ((LinkageTextAdapter)styleRecycler.getAdapter()).getDriveKey();
        float price[] = priceRange();
        currentPrice.setText(sign +(null == type || null == style ? price[0] +" - "+price[1] : price(type,style)));
    }
    private float[] priceRange(){
        float price[] = new float[]{0.0f,0.0f};
        price[0] = price[1] = Float.valueOf((String) list.get(0)[3]);
        if(0 == list.size())
            return price;
        //originalCostMin = originalCostMax = Float.valueOf(list.get(0)[1]);
        for(int index = 1;index< list.size();index++) {
            float currentPrice = Float.valueOf((String) list.get(index)[3]);
            if (price[0] > currentPrice)
                price[0] = currentPrice;
            else if(price[1] < currentPrice)
                price[1] = currentPrice;
            /*float originalCostPrice = Float.valueOf(list.get(index)[1]);
            if (originalCostMin > originalCostPrice)
                originalCostMin = originalCostPrice;
            else if(originalCostMax < originalCostPrice)
                originalCostMax = currentPrice;*/
        }
        return price;
    }
    private void initView(Map<CharSequence,List<CharSequence>>[] arrayMap){
        View view = LayoutInflater.from(activity).inflate(R.layout.popupwindow_specification,null);
        ButterKnife.bind(this,view);
        Glide.with(activity).load(activity.getString(R.string.ip)+activity.getString(R.string.pictureLibraryPath)+info[0]);
        currentPrice.setText(activity.getString(R.string.sign)+info[3]);
        typeRecycler.setLayoutManager(new GridLayoutManager(activity,4,LinearLayoutManager.VERTICAL,false));
        typeRecycler.setItemAnimator(new DefaultItemAnimator());
        LinkageTextAdapter tyepAdapter = new LinkageTextAdapter(activity,arrayMap[0]);
        typeRecycler.setAdapter(tyepAdapter);
        tyepAdapter.setItemOnClickListener(new TextAdapter.ItemOnClickListener() {
            @Override
            public void itemOnClick(View view) {
                charSequences[0] = ((LinkageTextAdapter)typeRecycler.getAdapter()).getDriveKey();
                ((LinkageTextAdapter)styleRecycler.getAdapter()).setKey(charSequences[0]);
                String sign = activity.getString(R.string.sign);
                final CharSequence type = ((LinkageTextAdapter)typeRecycler.getAdapter()).getDriveKey();
                final CharSequence style = ((LinkageTextAdapter)styleRecycler.getAdapter()).getDriveKey();
                float price[] = priceRange();
                currentPrice.setText(sign +((null == type || null == style)&& price[0] != price[1] ? price[0] +" - "+price[1] : price(type,style)));
            }
        });
        styleRecycler.setLayoutManager(new GridLayoutManager(activity,4, LinearLayoutManager.VERTICAL,false));
        styleRecycler.setItemAnimator(new DefaultItemAnimator());
        LinkageTextAdapter styleAdapter = new LinkageTextAdapter(activity,arrayMap[1]);
        styleRecycler.setAdapter(styleAdapter);
        styleAdapter.setItemOnClickListener(new TextAdapter.ItemOnClickListener() {
            @Override
            public void itemOnClick(View view) {
                charSequences[1] = ((LinkageTextAdapter)styleRecycler.getAdapter()).getDriveKey();
                ((LinkageTextAdapter)typeRecycler.getAdapter()).setKey(charSequences[1]);
                String sign = activity.getString(R.string.sign);
                final CharSequence type = ((LinkageTextAdapter)typeRecycler.getAdapter()).getDriveKey();
                final CharSequence style = ((LinkageTextAdapter)styleRecycler.getAdapter()).getDriveKey();
                float price[] = priceRange();
                currentPrice.setText(sign +((null == type || null == style)&& price[0] != price[1] ? price[0]+" - "+price[1]: price(type,style)));
            }
        });
        reduce.setOnClickListener(clickListener);
        total.setText("1");
        view.findViewById(R.id.image_add).setOnClickListener(clickListener);
        view.findViewById(R.id.text_confirm).setOnClickListener(clickListener);
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
    private void backgroundAlpha(final float alpha){
        WindowManager.LayoutParams lp = activity.getWindow().getAttributes();
        lp.alpha = alpha;
        activity.getWindow().setAttributes(lp);
    }
    private CharSequence price(CharSequence type,CharSequence style){
        for(CharSequence[] charSequences : list)
            if(charSequences[0].equals(type) && charSequences[1].equals(style))
                return Math.round(Float.valueOf((String)charSequences[3])*Integer.valueOf(total.getText().toString().trim())*100)/100.00f+"";
        return ""+ Math.round(Float.valueOf((String) list.get(0)[3])*100)/100.00f;
    }
    private void getValue(Map<CharSequence,List<CharSequence>> map,final CharSequence key,final CharSequence charSequence){
        List<CharSequence> value = null;
        if(null == map.get(key))
            value =  new ArrayList<CharSequence>();
        else
            value =  map.get(key);
        value.add(charSequence);
        map.put(key,value);
    }
    public interface Submit{
        void commit(CharSequence[] array,int total);
    }
}
