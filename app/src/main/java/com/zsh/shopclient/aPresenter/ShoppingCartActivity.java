package com.zsh.shopclient.aPresenter;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Toast;

import com.xxx.skynet.sharedPreference.IOSharedPreferences;
import com.zsh.shopclient.R;
import com.zsh.shopclient.aView.BaseView;
import com.zsh.shopclient.aView.ShoppingCartV;
import com.zsh.shopclient.adapter.CommodityAdapter;
import com.zsh.shopclient.adapter.ShoppingCartAdapter;
import com.zsh.shopclient.model.ShoppingTrolley;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by Administrator on 2017/11/19.
 */

public class ShoppingCartActivity extends BaseActivity {
    private ShoppingTrolley shoppingTrolley;
    private void linkNet(){
        MultipartBody.Builder body = new MultipartBody.Builder().setType(MultipartBody.FORM);
        body.addFormDataPart("userId", IOSharedPreferences.inString(this,getString(R.string.user),getString(R.string.userId)));
        new OkHttpClient().newCall(new Request.Builder().url(getString(R.string.ip)+ getString(R.string.shoppingTrolley)).post(body.build()).build()).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                getTypeView().hintHoast(R.string.getDataDefeated,null, Toast.LENGTH_SHORT);
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                try {
                    JSONObject result = new JSONObject(response.body().string());
                    Log.i("ShoppingCartActivity", "linkNet->" + result);
                    if (200 == result.getInt("statusCode") && 0< result.getJSONArray("listsCart").length()) {
                        shoppingTrolley = new ShoppingTrolley(result.getJSONArray("listsCart"));
                        ((ShoppingCartV)getTypeView()).updateAdapter(shoppingTrolley.getList());
                    }else
                        ((ShoppingCartV)getTypeView()).updateAdapter(null);
                } catch (JSONException e) {
                    e.printStackTrace();
                    ((ShoppingCartV)getTypeView()).updateAdapter(null);
                }
            }
        });
    }
    private void submit(final int position,final CharSequence[]array,final CharSequence way) {
        MultipartBody.Builder requestBody = new MultipartBody.Builder().setType(MultipartBody.FORM);
        requestBody.addFormDataPart("shopCartId", array[7].toString()).addFormDataPart("modify",way.toString());
        new OkHttpClient().newCall(new Request.Builder().url(getString(R.string.ip) + getString(R.string.updateShoppingCartPath)).post(requestBody.build()).build()).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
                getTypeView().hintHoast(R.string.getDataDefeated, null, Toast.LENGTH_SHORT);
            }
            @Override
            public void onResponse(Call call, Response response) throws IOException {
                try {
                    JSONObject result = new JSONObject(response.body().string());
                    Log.i("ShoppingCartActivity", "linkNet->" + result);
                    if (200 == result.getInt("statusCode")) {
                        shoppingTrolley.addCount(position,"add".equals(way) ? 1 : -1);
                        ((ShoppingCartV)getTypeView()).updateAdapter(shoppingTrolley.getList());
                    }else
                        getTypeView().hintHoast(R.string.submitDataDefeated,null, Toast.LENGTH_SHORT);
                } catch (JSONException e) {
                    e.printStackTrace();
                    getTypeView().hintHoast(R.string.submitDataDefeated,null, Toast.LENGTH_SHORT);
                }
            }
        });
    }
    private void emptyShoppingCart(final String shopId){
        MultipartBody.Builder body = new MultipartBody.Builder().setType(MultipartBody.FORM);
        body.addFormDataPart("shopId",shopId).addFormDataPart("userId", IOSharedPreferences.inString(ShoppingCartActivity.this,getString(R.string.user),getString(R.string.userId)));
        new OkHttpClient().newCall(new Request.Builder().url(getString(R.string.ip)+ getString(R.string.emptyShopShoppingCart)).post(body.build()).build()).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                getTypeView().hintHoast(R.string.getDataDefeated,null, Toast.LENGTH_SHORT);
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                try {
                    JSONObject result = new JSONObject(response.body().string());
                    Log.i("ShoppingCartActivity", "linkNet->" + result);
                    if (200 == result.getInt("statusCode")) {
                        shoppingTrolley.romove(shopId);
                        ((ShoppingCartV) getTypeView()).updateAdapter(shoppingTrolley.getList());
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }
    private void goBack(){
        Intent resultIntent = new Intent();
        resultIntent.putExtra(getString(R.string.result),null == shoppingTrolley ?0: shoppingTrolley.getCount());
        resultIntent.putExtra(getString(R.string.attach),null == shoppingTrolley ?0: shoppingTrolley.getTotalPrice());
        resultIntent.putExtra(getString(R.string.send),true);
        setResult(RESULT_OK,resultIntent);
        finish();
    }
    @Override
    protected BaseView createTypeView() {
        return new ShoppingCartV();
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        linkNet();
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(null != data)
            switch (requestCode){
                case ShopActivity.SHOPPINGcART:
                    linkNet();
                    break;
            }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if(KeyEvent.KEYCODE_BACK == keyCode)
            goBack();
        return super.onKeyDown(keyCode, event);
    }
    public CommodityAdapter.ItemOnClickListener getOnClickListener(){
        return new CommodityAdapter.ItemOnClickListener() {
            @Override
            public int onClickAdd(View itemView,CharSequence[] array) {
                //array[5] = Integer.valueOf((String) array[5]) +1+ "";
                submit(itemView.getId(),array,"add");
                return 0;
            }

            @Override
            public int onClickReduce(View itemView,CharSequence[] array) {
                //array[5] = Integer.valueOf((String) array[5]) -1+ "";
                submit(itemView.getId(),array,"minus");
                return 0;
            }
        };
    }
    public ShoppingCartAdapter.EmptyListener getEmptyListener(){
        return new ShoppingCartAdapter.EmptyListener() {
            @Override
            public void emptyCart(CharSequence[] array) {
                emptyShoppingCart(array[2].toString());
            }
        };
    }
    public View.OnClickListener getClickListener(){
        return new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                switch (v.getId()){
                    case R.id.image_goBack:
                        goBack();
                        break;
                    case R.id.linear_navi:
                        for(int index = 0;index < shoppingTrolley.getList().size();index++)
                            if(3 == shoppingTrolley.getList().get(index).length)
                                emptyShoppingCart(shoppingTrolley.getList().get(index)[2].toString());
                        break;
                    case R.id.text_closeAccount:
                        break;
                }
            }
        };
    }
}
