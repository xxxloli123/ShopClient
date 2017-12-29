package com.zsh.shopclient.aPresenter;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.xxx.skynet.sharedPreference.IOSharedPreferences;
import com.zsh.shopclient.R;
import com.zsh.shopclient.aView.BaseView;
import com.zsh.shopclient.aView.CurrencyShopV;
import com.zsh.shopclient.model.CurrencyCommodity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * Created by Administrator on 2017/12/13.
 */

public class CurrencyShopActivity extends BaseActivity {
    private void linkNet(){
        final String url = getString(R.string.ip)+getString(R.string.pictureLibraryPath);
        new OkHttpClient().newCall(new Request.Builder().url(getString(R.string.ip)+getString(R.string.currencyCommodity)).post(RequestBody.create(MediaType.parse("application/json"), "1")).build()).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
                getTypeView().hintHoast(R.string.getDataDefeated,null, Toast.LENGTH_SHORT);
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                try {
                    JSONObject result = new JSONObject(response.body().string());
                    Log.i("ShopFragment","onViewCreated->"+result);
                    if(200 == result.getInt("statusCode"))
                        ((CurrencyShopV)getTypeView()).update(new CurrencyCommodity(url,result.getJSONArray("listproduct")).getList());
                    else
                        getTypeView().hintHoast(BaseView.X,result.getString("message"), Toast.LENGTH_SHORT);
                } catch (JSONException e) {
                    e.printStackTrace();
                    getTypeView().hintHoast(R.string.parseFailure,null, Toast.LENGTH_SHORT);
                }
            }
        });
    }
    @Override
    protected BaseView createTypeView() {
        return new CurrencyShopV();
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        MultipartBody.Builder requestBody = new MultipartBody.Builder().setType(MultipartBody.FORM);
        requestBody.addFormDataPart("userId", IOSharedPreferences.inString(this,getString(R.string.user),getString(R.string.userId)));
        new OkHttpClient().newCall(new Request.Builder().url(getString(R.string.ip)+getString(R.string.currencySum)).post(requestBody.build()).build()).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
                getTypeView().hintHoast(R.string.getDataDefeated,null, Toast.LENGTH_SHORT);
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                try {
                    JSONObject result = new JSONObject(response.body().string());
                    Log.i("ShopFragment","onViewCreated->"+result);
                    if(200 == result.getInt("statusCode"))
                        ((CurrencyShopV)getTypeView()).setCurrency(result.getString("Integral"));
                    else
                        getTypeView().hintHoast(BaseView.X,result.getString("message"), Toast.LENGTH_SHORT);
                } catch (JSONException e) {
                    e.printStackTrace();
                    getTypeView().hintHoast(R.string.parseFailure,null, Toast.LENGTH_SHORT);
                }
            }
        });
        linkNet();
    }
    public SwipeRefreshLayout.OnRefreshListener getRefreshListener(){
        return new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                linkNet();
            }
        };
    }
    public View.OnClickListener getClickListener(){
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (v.getId()){
                    case R.id.image_goBack:
                        finish();
                        break;
                    case R.id.frame_currency:
                        break;
                    case R.id.frame_record:
                        startActivity(new Intent(CurrencyShopActivity.this,ConversionRecordActivity.class));
                        break;
                }
            }
        };
    }
}
