package com.zsh.shopclient.aPresenter;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.amap.api.location.AMapLocation;
import com.xxx.skynet.sharedPreference.IOSharedPreferences;
import com.zsh.shopclient.R;
import com.zsh.shopclient.aView.BaseView;
import com.zsh.shopclient.aView.SpecialOfferV;
import com.zsh.shopclient.adapter.CommodityAdapter;
import com.zsh.shopclient.adapter.TextAdapter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by Administrator on 2017/12/13.
 */

public class SpecialOfferActivity extends BaseActivity {
    private void linkNet(String shopId){
        MultipartBody.Builder requestBody = new MultipartBody.Builder().setType(MultipartBody.FORM);
        requestBody.addFormDataPart("shopId",shopId).addFormDataPart("type",getIntent().getStringExtra(getString(R.string.paramValue)));
        new OkHttpClient().newCall(new Request.Builder().url(getString(R.string.ip)+getString(R.string.everyDaySspecialCommodity)).post(requestBody.build()).build()).enqueue(new Callback() {
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
                    if(200 == result.getInt("statusCode")){
                        List<CharSequence[]>list = new ArrayList<CharSequence[]>();
                        JSONArray jsonArray = result.getJSONArray("listbargaingoods");
                        for(int index = 0;index < jsonArray.length();index++) {
                            JSONObject jsonObject = jsonArray.getJSONObject(index);
                            list.add(new CharSequence[]{jsonObject.getString("productSmallImg"), jsonObject.getString("productName"),
                                    jsonObject.getString("bargainPrice"), jsonObject.getString("id"),"0"});
                        }
                        ((SpecialOfferV)getTypeView()).updateRecycler(list);
                    }else
                        getTypeView().hintHoast(BaseView.X,result.getString("message"), Toast.LENGTH_SHORT);
                } catch (JSONException e) {
                    e.printStackTrace();
                    getTypeView().hintHoast(R.string.parseFailure,null, Toast.LENGTH_SHORT);
                }
            }
        });
    }
    private void addShoppingCart(final CharSequence[] array,final CharSequence[] screen,final int total,String userId){
        if(null != screen[0] && null != screen[1]) {
            MultipartBody.Builder body = new MultipartBody.Builder().setType(MultipartBody.FORM);
            body.addFormDataPart("userId", userId).
                    addFormDataPart("productId", (String) array[3]).
                    addFormDataPart("productcategory", (String) screen[0]).addFormDataPart("standardName", (String) screen[1]).
                    addFormDataPart("goodsnum",total+"").
                    addFormDataPart("type", "ActivityGoods");
            new OkHttpClient().newCall(new Request.Builder().url(getString(R.string.ip) + getString(R.string.addShoppingCartPath)).post(body.build()).build()).enqueue(new Callback() {
                @Override
                public void onFailure(Call call, IOException e) {
                    getTypeView().hintHoast(R.string.submitDataDefeated, null, Toast.LENGTH_SHORT);
                }

                @Override
                public void onResponse(Call call, Response response) throws IOException {
                    try {
                        JSONObject result = new JSONObject(response.body().string());
                        Log.i("PropertyPopupWindow", "clickListener--->" + result);
                        getTypeView().hintHoast(BaseView.X, result.getString("message"), Toast.LENGTH_SHORT);
                    } catch (JSONException e) {
                        e.printStackTrace();
                        getTypeView().hintHoast(R.string.submitDataDefeated, null, Toast.LENGTH_SHORT);
                    }
                }
            });
        }else
            Toast.makeText(this,getString(R.string.selectCommodityAttribute),Toast.LENGTH_SHORT).show();
    }
    @Override
    protected BaseView createTypeView() {
        return new SpecialOfferV();
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        MultipartBody.Builder requestBody = new MultipartBody.Builder().setType(MultipartBody.FORM);
        AMapLocation location = (AMapLocation) getIntent().getExtras().get(getString(R.string.location));
        getIntent().putExtra(getString(R.string.requestParam),"ActivityGoods");
        requestBody.addFormDataPart("pro",location.getProvince()).addFormDataPart("city",location.getCity()).addFormDataPart("district",location.getDistrict()).addFormDataPart("type",getIntent().getStringExtra(getString(R.string.paramValue)));
        new OkHttpClient().newCall(new Request.Builder().url(getString(R.string.ip)+getString(R.string.everyDaySspecialUrl)).post(requestBody.build()).build()).enqueue(new Callback() {
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
                    if(200 == result.getInt("statusCode")){
                        List<CharSequence[]>list = new ArrayList<CharSequence[]>();
                        JSONArray jsonArray = result.getJSONArray("listshop");
                        for(int index = 0;index< jsonArray.length();index++){
                            JSONObject jsonObject = jsonArray.getJSONObject(index);
                            list.add(new CharSequence[]{jsonObject.getString("shopName"),jsonObject.getString("deliveryFee"),jsonObject.getString("id"),
                                    jsonObject.getString("lng"),jsonObject.getString("lat")});
                        }
                        ((SpecialOfferV)getTypeView()).updateRecyclerClass(list);
                        if(0 < list.size())
                            linkNet(list.get(0)[2].toString());
                    }else
                        getTypeView().hintHoast(BaseView.X,result.getString("message"), Toast.LENGTH_SHORT);
                } catch (JSONException e) {
                    e.printStackTrace();
                    getTypeView().hintHoast(R.string.parseFailure,null, Toast.LENGTH_SHORT);
                }
            }
        });
    }
    public View.OnClickListener getClickListener(){
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (v.getId()){
                    case R.id.image_goBack:
                        finish();
                        break;
                    case SpecialOfferV.SHOPPINGcART:
                        final String userId = IOSharedPreferences.inString(SpecialOfferActivity.this, getString(R.string.user), getString(R.string.userId));
                        if (null != userId) {
                            Intent intent = new Intent(SpecialOfferActivity.this, ShoppingCartActivity.class);
                            intent.putExtra(getString(R.string.requestParam), "ActivityGoods");
                            startActivity(intent);
                        }else
                            startActivityForResult(new Intent(SpecialOfferActivity.this,LoginActivity.class),MainActivity.RETURN_USER);
                        break;
                }
            }
        };
    }
    public TextAdapter.ItemOnClickListener getItemClickListener(){
        return new TextAdapter.ItemOnClickListener() {
            @Override
            public void itemOnClick(View view) {
                linkNet(((CharSequence[])view.getTag())[2].toString());
            }
        };
    }
    public CommodityAdapter.ItemOnClickListener getInfoListener(){
        return new CommodityAdapter.ItemOnClickListener(){
            @Override
            public int onClickAdd(View itemView,final CharSequence[] array) {
                int count = 1;
                final String userId = IOSharedPreferences.inString(SpecialOfferActivity.this,getString(R.string.user),getString(R.string.userId));
                if(null != userId)
                    addShoppingCart(array,new String[]{"",""},1,userId);
                else
                    startActivityForResult(new Intent(SpecialOfferActivity.this,LoginActivity.class),MainActivity.RETURN_USER);
                return count;
            }

            @Override
            public int onClickReduce(View itemView,CharSequence[] array) {
                return 0;
            }
        };
    }
}
