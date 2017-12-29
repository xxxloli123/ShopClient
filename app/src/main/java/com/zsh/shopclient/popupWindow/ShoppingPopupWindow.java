package com.zsh.shopclient.popupWindow;

import android.graphics.drawable.ColorDrawable;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.Toast;

import com.xxx.skynet.sharedPreference.IOSharedPreferences;
import com.zsh.shopclient.R;
import com.zsh.shopclient.aPresenter.BaseActivity;
import com.zsh.shopclient.adapter.CommodityAdapter;
import com.zsh.shopclient.model.ShoppingCart;
import com.zsh.shopclient.tool.RecyclerLimit;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import butterknife.BindView;
import butterknife.ButterKnife;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by Administrator on 2017/12/5.
 */

public class ShoppingPopupWindow {
    @BindView(R.id.recycler)RecyclerView recycler;
    private BaseActivity activity;
    private PopupWindow popupWindow;
    private ShoppingCart shoppingCart;
    private Notification notification;
    private void submit(final int index,final CharSequence[]array,final CharSequence way) {
        MultipartBody.Builder requestBody = new MultipartBody.Builder().setType(MultipartBody.FORM);
        requestBody.addFormDataPart("shopCartId", array[5].toString()).addFormDataPart("modify",way.toString());
        new OkHttpClient().newCall(new Request.Builder().url(activity.getString(R.string.ip) + activity.getString(R.string.updateShoppingCartPath)).post(requestBody.build()).build()).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
                activity.getTypeView().hintHoast(R.string.getDataDefeated, null, Toast.LENGTH_SHORT);
            }
            @Override
            public void onResponse(Call call, Response response) throws IOException {
                try {
                    JSONObject result = new JSONObject(response.body().string());
                    Log.i("ShoppingCartActivity", "linkNet->" + result);
                    if (200 == result.getInt("statusCode")) {
                        shoppingCart.getList().get(index)[4] = Integer.valueOf((String) array[4]) + ("minus".equals(way) ? -1 : 1) + "";
                        if("0".equals(array[4]))
                            shoppingCart.getList().remove(index);
                        updateUi();
                    }else
                        activity.getTypeView().hintHoast(R.string.submitDataDefeated,null, Toast.LENGTH_SHORT);
                } catch (JSONException e) {
                    e.printStackTrace();
                    activity.getTypeView().hintHoast(R.string.submitDataDefeated,null, Toast.LENGTH_SHORT);
                }
            }
        });
    }
    private void updateUi(){
        final int total = shoppingCart.getTotal();
        activity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if(null == recycler)return;
                ((CommodityAdapter) recycler.getAdapter()).update(shoppingCart.getList(), null == shoppingCart.getList());
                if(shoppingCart.isEmpty())
                    dismiss();
                if(null != notification)
                    notification.updateShoppingCart(total,shoppingCart.getTotalPrice());
            }
        });
    }
    private void backgroundAlpha(final float alpha){
        WindowManager.LayoutParams lp = activity.getWindow().getAttributes();
        lp.alpha = alpha;
        activity.getWindow().setAttributes(lp);
    }
    public ShoppingPopupWindow(final BaseActivity activity, final String IdStr, ShoppingCart shoppingTrolley) {
        this.activity = activity;
        shoppingCart = shoppingTrolley;
        LinearLayout layout = (LinearLayout) LayoutInflater.from(activity).inflate(R.layout.popupwindow_shopping_cart,null);
        layout.findViewById(R.id.image_empty).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (v.getId()){
                    case R.id.image_empty:
                        MultipartBody.Builder body = new MultipartBody.Builder().setType(MultipartBody.FORM);
                        body.addFormDataPart("shopId",IdStr).addFormDataPart("userId", IOSharedPreferences.inString(activity,activity.getString(R.string.user),activity.getString(R.string.userId)));
                        new OkHttpClient().newCall(new Request.Builder().url(activity.getString(R.string.ip)+ activity.getString(R.string.emptyShopShoppingCart)).post(body.build()).build()).enqueue(new Callback() {
                            @Override
                            public void onFailure(Call call, IOException e) {
                                activity.getTypeView().hintHoast(R.string.getDataDefeated,null, Toast.LENGTH_SHORT);
                            }

                            @Override
                            public void onResponse(Call call, Response response) throws IOException {
                                try {
                                    JSONObject result = new JSONObject(response.body().string());
                                    Log.i("ShoppingCartActivity", "linkNet->" + result);
                                    if (200 == result.getInt("statusCode")) {
                                        shoppingCart.clear();
                                        updateUi();
                                    }else
                                        updateUi();
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                    updateUi();
                                }
                            }
                        });
                        break;
                }
            }
        });
        ButterKnife.bind(this,layout);
        recycler.setLayoutManager(new LinearLayoutManager(activity,LinearLayoutManager.VERTICAL,false));
        recycler.addItemDecoration(new RecyclerLimit(activity,(byte)LinearLayout.HORIZONTAL));
        recycler.setItemAnimator(new DefaultItemAnimator());
        CommodityAdapter adapter = new CommodityAdapter(activity,shoppingTrolley.getList(),true);
        adapter.setItemOnClickListener(new CommodityAdapter.ItemOnClickListener() {
            @Override
            public int onClickAdd(View itemView, CharSequence[] array) {
                submit(itemView.getId(),array,"add");
                return 0;
            }

            @Override
            public int onClickReduce(View itemView, CharSequence[] array) {
                submit(itemView.getId(),array,"minus");
                return 0;
            }
        });
        recycler.setAdapter(adapter);
        DisplayMetrics dm = new DisplayMetrics();
        activity.getWindowManager().getDefaultDisplay().getMetrics(dm);
        popupWindow = new PopupWindow(layout, ViewGroup.LayoutParams.MATCH_PARENT,dm.heightPixels/5*4,true);
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
    public void displayPopupWindow(){
        backgroundAlpha(0.6f);
        popupWindow.showAtLocation(activity.findViewById(android.R.id.content), Gravity.BOTTOM,0,0);//Activity.getWindow.getDecorView()获得activity的rootView
    }
    public void dismiss(){
        popupWindow.dismiss();
    }
    public void setNotification(Notification notification) {
        this.notification = notification;
    }
    public interface Notification{
        void updateShoppingCart(int total,float totalPrice);
    }
}
