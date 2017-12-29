package com.zsh.shopclient.aPresenter;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.amap.api.location.AMapLocation;
import com.xxx.skynet.sharedPreference.IOSharedPreferences;
import com.zsh.shopclient.R;
import com.zsh.shopclient.aView.BaseView;
import com.zsh.shopclient.aView.OrderInfoV;
import com.zsh.shopclient.model.MerchantParticulars;
import com.zsh.shopclient.popupWindow.PayPopupWindow;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by Administrator on 2017/12/20.
 */

public class OrderInfoActivity extends BaseActivity {
    public static final int EVALUATED = 0;
    private boolean bool;
    private MerchantParticulars shopInfos;
    private Map<String,CharSequence[]> statusValue;
    @Override
    protected BaseView createTypeView() {
        return new OrderInfoV();
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        final AMapLocation location = (AMapLocation) getIntent().getExtras().get(getString(R.string.location));
        statusValue = (Map<String, CharSequence[]>)getIntent().getExtras().get(getString(R.string.send));
        new OkHttpClient().newCall(new Request.Builder().url(getString(R.string.ip)+getString(R.string.shopInfo)).post(new MultipartBody.Builder()
                .setType(MultipartBody.FORM).addFormDataPart("shopId",((Map<CharSequence[], List<CharSequence[]>>)getIntent().getExtras().
                        get(getString(R.string.title))).keySet().iterator().next()[12].toString()).build()).build()).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {}

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                try {
                    JSONObject result = new JSONObject(response.body().string());
                    Log.i("OrderInfoActivity","onCreate->"+result);
                    if(200 == result.getInt("statusCode")){
                        shopInfos = new MerchantParticulars(location,result.getJSONObject("shop"));
                        shopInfos.initEvaluate(result.getJSONArray("listEvaluate"));
                        shopInfos.initActivitys(result.getJSONArray("listShopActivity"));
                    }else
                        getTypeView().hintHoast(BaseView.X,result.getString("message"), Toast.LENGTH_SHORT);
                } catch (JSONException e) {
                    e.printStackTrace();
                    getTypeView().hintHoast(R.string.parseFailure,null, Toast.LENGTH_SHORT);
                }
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(null != data){
            switch (requestCode){
                case EVALUATED:
                    bool = data.getBooleanExtra(getString(R.string.result),false);
                    if(bool)
                        ((OrderInfoV) getTypeView()).setStatus(R.string.offTheStocks, null);
                    break;
            }
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if(KeyEvent.KEYCODE_BACK == keyCode)
            goBack();
        return super.onKeyDown(keyCode, event);
    }
    private void goBack(){
        Intent intent = new Intent();
        intent.putExtra(getString(R.string.result),bool);
        setResult(RESULT_OK,intent);
        finish();
    }
    public View.OnClickListener getClickListener(){
        return new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                switch (v.getId()){
                    case R.id.image_goBack:
                        goBack();
                        break;
                    case R.id.text:
                        if(getString(R.string.pay).equals(((TextView)v).getText().toString().trim())) {
                            Map<String, String> map = new HashMap<>();
                            map.put("orderId",((CharSequence[])v.getTag())[30].toString());
                            new PayPopupWindow(OrderInfoActivity.this, map, new PayPopupWindow.PayResultListener() {
                                @Override
                                public void inform(byte result) {
                                    if(PayPopupWindow.SUCCEED == result)
                                        ((OrderInfoV)getTypeView()).setStatus(R.string.inYheDistribution,statusValue.get(getString(R.string.inYheDistribution))[0]);
                                }
                            }).displayPopupWindow();
                        }else if(getString(R.string.cancelation).equals(((TextView)v).getText().toString().trim())){
                            MultipartBody.Builder requestBody = new MultipartBody.Builder().setType(MultipartBody.FORM);
                            requestBody.addFormDataPart("userId", IOSharedPreferences.inString(OrderInfoActivity.this,getString(R.string.user),getString(R.string.userId))).
                                    addFormDataPart("orderId",((CharSequence[])v.getTag())[30].toString());
                            new OkHttpClient().newCall(new Request.Builder().url(getString(R.string.ip)+getString(R.string.cancelOrder)).post(requestBody.build()).build()).enqueue(new Callback() {
                                @Override
                                public void onFailure(Call call, IOException e) {
                                    getTypeView().hintHoast(R.string.getDataDefeated,null, Toast.LENGTH_SHORT);
                                }

                                @Override
                                public void onResponse(Call call, Response response) throws IOException {
                                    try {
                                        JSONObject result = new JSONObject(response.body().string());
                                        Log.i("OrderInfoActivity", "getClickListener-取消订单->" + result);
                                        if (200 == result.getInt("statusCode"))
                                            ((OrderInfoV)getTypeView()).setStatus(-1,null);
                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }
                                }
                            });
                        }else if(getString(R.string.makeSurtTheGoods).equals(((TextView)v).getText().toString().trim())){
                            MultipartBody.Builder requestBody = new MultipartBody.Builder().setType(MultipartBody.FORM);
                            requestBody.addFormDataPart("userId",IOSharedPreferences.inString(OrderInfoActivity.this,getString(R.string.user),getString(R.string.userId))).
                                    addFormDataPart("orderId",((CharSequence[])v.getTag())[30].toString());
                            new OkHttpClient().newCall(new Request.Builder().url(getString(R.string.ip)+getString(R.string.receiving)).post(requestBody.build()).build()).enqueue(new Callback() {
                                @Override
                                public void onFailure(Call call, IOException e) {
                                    getTypeView().hintHoast(R.string.getDataDefeated,null, Toast.LENGTH_SHORT);
                                }

                                @Override
                                public void onResponse(Call call, Response response) throws IOException {
                                    try {
                                        JSONObject result = new JSONObject(response.body().string());
                                        Log.i("OrderFragment", "getClickListener-确定收货->" + result);
                                        if (200 == result.getInt("statusCode"))
                                            ((OrderInfoV)getTypeView()).setStatus(R.string.pendingReviews,statusValue.get(getString(R.string.inYheDistribution))[0]);
                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }
                                }
                            });
                        }else if(getString(R.string.pendingReviews).equals(((TextView)v).getText().toString().trim())){
                            Intent test = new Intent(OrderInfoActivity.this, AppraiseActivity.class);
                            test.putExtra(getString(R.string.title),(HashMap)getIntent().getExtras().get(getString(R.string.title)));
                            startActivityForResult(test,EVALUATED);
                        }else if(getString(R.string.contactWithTheRider).equals(((TextView)v).getText().toString().trim()))
                            startActivity(new Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + ((CharSequence[])v.getTag())[22])));
                        break;
                    case R.id.text_shopName:
                        if(null == shopInfos)return;
                        HashMap hashMap = new HashMap();
                        hashMap.put(shopInfos.getEvaluate(),shopInfos.getActivitys());
                        Intent shopInfo = new Intent(OrderInfoActivity.this,MerchantParticularsActivity.class);
                        Bundle bundle = new Bundle();
                        bundle.putCharSequenceArray(getString(R.string.send),shopInfos.getInfo());
                        bundle.putSerializable(getString(R.string.sendBody),hashMap);
                        shopInfo.putExtras(bundle);
                        startActivity(shopInfo);
                        break;
                    case R.id.text_contactPhone://Android跳转到拨打电话界面 http://blog.csdn.net/sososohotsummer/article/details/42486799
                        startActivity(new Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + ((Map<CharSequence[], List<CharSequence[]>>) getIntent().getExtras().get(getString(R.string.title))).keySet().iterator().next()[9])));
                        break;
                }
            }
        };
    }
}
