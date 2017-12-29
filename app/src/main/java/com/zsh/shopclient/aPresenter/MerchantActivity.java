package com.zsh.shopclient.aPresenter;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.amap.api.location.AMapLocation;
import com.zsh.shopclient.R;
import com.zsh.shopclient.aView.BaseView;
import com.zsh.shopclient.aView.MerchantActivityV;
import com.zsh.shopclient.adapter.BaseRecycleAdapter;
import com.zsh.shopclient.tool.JsonHelper;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
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
 * Created by Administrator on 2017/12/13.
 */

public class MerchantActivity extends BaseActivity {
    private int page;

    private void linkNet(final boolean isRefresh){
        AMapLocation location = (AMapLocation) getIntent().getExtras().get(getString(R.string.location));
        MultipartBody.Builder requestBody = new MultipartBody.Builder();
        requestBody.addFormDataPart("pro",location.getProvince()).addFormDataPart("city",location.getCity()).addFormDataPart("district",location.getDistrict()).
                addFormDataPart("lng",location.getLongitude()+"").addFormDataPart("lat",location.getLatitude()+"").addFormDataPart("startPage",""+page).addFormDataPart("pageSixe",""+8);
        new OkHttpClient().newCall(new Request.Builder().url(getString(R.string.ip)+getString(R.string.activityShopUrl)).post(requestBody.build()).build()).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
                getTypeView().hintHoast(R.string.getDataDefeated,null, Toast.LENGTH_SHORT);
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                try {
                    JSONObject result = new JSONObject(response.body().string());
                    Log.i("MerchantActivity","linkNet->"+result);
                    if(200 == result.getInt("statusCode")) {
                        List<Map<String,Object>> listMap = JsonHelper.jsonArray2List(result.getJSONArray("applistshop"));
                        List<Map<Map<String,Object>,List<Map<String,Object>>>> lists = new ArrayList<Map<Map<String, Object>, List<Map<String, Object>>>>();
                        for(Map<String,Object> map : listMap) {
                            Map<Map<String,Object>,List<Map<String,Object>>> maps = new HashMap<Map<String, Object>, List<Map<String, Object>>>();
                            maps.put(JsonHelper.jsonObject2Map(new JSONObject(map.get("shop").toString())), JsonHelper.jsonArray2List(new JSONArray(map.get("activity").toString())));
                            lists.add(maps);
                        }
                        ((MerchantActivityV) getTypeView()).update(lists,isRefresh);
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
    protected BaseView createTypeView() {
        return new MerchantActivityV();
    }
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        page = 1;
        linkNet(true);
    }
    public SwipeRefreshLayout.OnRefreshListener getRefreshListener(){
        return new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                page = 1;
                linkNet(true);
            }
        };
    }
    public BaseRecycleAdapter.FootLoadListener getFootLoadListener(){
        return new BaseRecycleAdapter.FootLoadListener() {
            @Override
            public void loadMoreListener() {
                page++;
                linkNet(false);
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
                }
            }
        };
    }
}
