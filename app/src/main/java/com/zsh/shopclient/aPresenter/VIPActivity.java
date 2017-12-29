package com.zsh.shopclient.aPresenter;

import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationListener;
import com.amap.api.maps2d.LocationSource;
import com.google.gson.Gson;
import com.slowlife.map.util.ToastUtil;
import com.xxx.skynet.sharedPreference.IOSharedPreferences;
import com.zsh.shopclient.R;
import com.zsh.shopclient.aView.BaseView;
import com.zsh.shopclient.aView.MainV;
import com.zsh.shopclient.adapter.BaseRecycleAdapter;
import com.zsh.shopclient.adapter.RecommendShopsAdapter;
import com.zsh.shopclient.adapter.VipShopAdapter;
import com.zsh.shopclient.fPresenter.VipFragment;
import com.zsh.shopclient.fView.VipV;
import com.zsh.shopclient.interfaceconfig.Config;
import com.zsh.shopclient.model.VipShop;
import com.zsh.shopclient.tool.JsonHelper;
import com.zsh.shopclient.tool.RecyclerLimit;
import com.zsh.shopclient.view.MyListView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import in.srain.cube.views.ptr.PtrClassicDefaultFooter;
import in.srain.cube.views.ptr.PtrClassicDefaultHeader;
import in.srain.cube.views.ptr.PtrDefaultHandler;
import in.srain.cube.views.ptr.PtrDefaultHandler2;
import in.srain.cube.views.ptr.PtrFrameLayout;
import in.srain.cube.views.ptr.PtrHandler2;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class VIPActivity extends BaseActivity1 implements AMapLocationListener, LocationSource {

    @BindView(R.id.ptr_frame_layout)
    PtrFrameLayout ptrFrameLayout;
    @BindView(R.id.shop_list)
    MyListView shopList;
    @BindView(R.id.recycler)
    RecyclerView recycler;
    @BindView(R.id.text_noData)
    TextView nodate;
    @BindView(R.id.swipe_update)
    SwipeRefreshLayout refresh;
    private OnLocationChangedListener mListener;

    private AMapLocationClient mlocationClient;
    // /声明mLocationOption对象
    private AMapLocationClientOption mLocationOption;
    private AMapLocation aMapLocation;
    private int page = 0;
    private ArrayList<VipShop> vipShops;
    private VipShopAdapter sbCommodityAdapter;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_vip;
    }

    @Override
    protected void init() {
        mlocationClient = new AMapLocationClient(this);
        //初始化定位参数
        mLocationOption = new AMapLocationClientOption();
        //设置定位监听
        mlocationClient.setLocationListener(this);
        //设置定位模式为高精度模式，Battery_Saving为低功耗模式，Device_Sensors是仅设备模式
        mLocationOption.setLocationMode(AMapLocationClientOption.AMapLocationMode.Battery_Saving);
        //设置定位间隔,单位毫秒,默认为2000ms
        mLocationOption.setInterval(2 * 60 * 1000);
        //设置定位参数
        mlocationClient.setLocationOption(mLocationOption);
        // 此方法为每隔固定时间会发起一次定位请求，为了减少电量消耗或网络流量消耗，
        // 注意设置合适的定位时间的间隔（最小间隔支持为1000ms），并且在合适时间调用stopLocation()方法来取消定位请求
        // 在定位结束后，在合适的生命周期调用onDestroy()方法
        // 在单次定位情况下，定位无论成功与否，都无需调用stopLocation()方法移除请求，定位sdk内部会移除
//启动定位
        mlocationClient.startLocation();

        PtrClassicDefaultHeader ptrClassicDefaultHeader = new PtrClassicDefaultHeader(this);

        ptrFrameLayout.setHeaderView(ptrClassicDefaultHeader);
        ptrFrameLayout.addPtrUIHandler(ptrClassicDefaultHeader);

        PtrClassicDefaultFooter ptrClassicDefaultFooter = new PtrClassicDefaultFooter(this);
        ptrFrameLayout.setFooterView(ptrClassicDefaultFooter);
        ptrFrameLayout.addPtrUIHandler(ptrClassicDefaultFooter);
    }

    private void vip() {
        refresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                page = 1;
                linkNet(true);
            }
        });
        recycler.setLayoutManager(new LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false));
        recycler.setItemAnimator(new DefaultItemAnimator());
        recycler.addItemDecoration(new RecyclerLimit(this,(byte) LinearLayoutManager.HORIZONTAL));
        RecommendShopsAdapter adapter = new RecommendShopsAdapter(this,null);
        adapter.setFootLoadListener(new BaseRecycleAdapter.FootLoadListener() {
            @Override
            public void loadMoreListener() {
                page++;
                linkNet(false);
            }
        });
        recycler.setAdapter(adapter);
    }

    private void linkNet(final boolean isRefresh) {
        final String userId = IOSharedPreferences.inString(this, getString(R.string.user), getString(R.string.userId));
        if ( null != userId) {
            MultipartBody.Builder requestBody = new MultipartBody.Builder();
            requestBody.addFormDataPart("pro", aMapLocation.getProvince()).
                    addFormDataPart("city", aMapLocation.getCity()).
                    addFormDataPart("district", aMapLocation.getDistrict())
                    .addFormDataPart("lng", aMapLocation.getLongitude()+"").
                    addFormDataPart("lat", aMapLocation.getLatitude()+"")
                    .addFormDataPart("startPage", "" + page)
                    .addFormDataPart("pageSize", "" + 8).
                    addFormDataPart("userId", userId);
            new OkHttpClient().newCall(new Request.Builder().url(getString(R.string.ip) + getString(R.string.vipShop)).post(requestBody.build()).build()).enqueue(new Callback() {
                @Override
                public void onFailure(Call call, IOException e) {
                    e.printStackTrace();
                    ToastUtil.show(VIPActivity.this,R.string.getDataDefeated);
                }

                @Override
                public void onResponse(Call call, Response response) throws IOException {
                    try {
                        JSONObject result = new JSONObject(response.body().string());
                        Log.i("VipFragment", "linkNet->" + result);
                        if (200 == result.getInt("statusCode")) {
                            List<Map<String, Object>> listMap = JsonHelper.jsonArray2List(result.getJSONArray("list"));
                            List<Map<Map<String, Object>, List<Map<String, Object>>>> lists = new ArrayList<Map<Map<String, Object>, List<Map<String, Object>>>>();
                            for (Map<String, Object> map : listMap) {
                                Map<Map<String, Object>, List<Map<String, Object>>> maps = new HashMap<Map<String, Object>, List<Map<String, Object>>>();
                                maps.put(JsonHelper.jsonObject2Map(new JSONObject(map.get("shop").toString())), JsonHelper.jsonArray2List(new JSONArray(map.get("activity").toString())));
                                lists.add(maps);
                            }
                            update(lists, isRefresh);
                        } else
                            ToastUtil.show(VIPActivity.this,result.getString("message"));
                    } catch (JSONException e) {
                        e.printStackTrace();
                        ToastUtil.show(VIPActivity.this,R.string.parseFailure);
                    }
                }
            });
        }else
            update(null, isRefresh);
    }

    private void update(final List<Map<Map<String, Object>, List<Map<String, Object>>>> list, final boolean isRefresh) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (refresh!=null){
                    refresh.setRefreshing(false);
                    ((RecommendShopsAdapter)recycler.getAdapter()).update(list,isRefresh);
                }
                if (nodate!=null)nodate.setVisibility(0>= recycler.getAdapter().getItemCount()? View.VISIBLE : View.GONE);

            }
        });
    }


    private void inData() {
//        参数：[userId, pro, city, district, lng, lat, startPage, pageSize]
        Map<String, Object> map = new HashMap<>();
        map.put("userId", IOSharedPreferences.inString(this, getString(R.string.user), getString(R.string.userId)));
        map.put("pro", aMapLocation.getProvince());
        map.put("city", aMapLocation.getCity());
        map.put("district", aMapLocation.getDistrict());
        map.put("lng", aMapLocation.getLongitude());
        map.put("lat", aMapLocation.getLatitude());
        map.put("startPage", ++page);
        map.put("pageSize", 20);
        newCall(Config.Url.getUrl(Config.GET_VIP), map);
    }

    @Override
    protected void initListener() {
        ptrFrameLayout.setPtrHandler(new PtrHandler2() {
            @Override
            public boolean checkCanDoLoadMore(PtrFrameLayout frame, View content, View footer) {
                return PtrDefaultHandler2.checkContentCanBePulledUp(frame, content, footer);
            }

            @Override
            public void onLoadMoreBegin(PtrFrameLayout frame) {
                ptrFrameLayout.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        inData();
                    }
                }, 1500);
            }

            @Override
            public boolean checkCanDoRefresh(PtrFrameLayout frame, View content, View header) {
                return PtrDefaultHandler.checkContentCanBePulledDown(frame, content, header);
            }

            @Override
            public void onRefreshBegin(PtrFrameLayout frame) {
                ptrFrameLayout.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        page = 0;
                        inData();
                    }
                }, 1500);
            }
        });
    }

    @Override
    public void onSuccess(Object tag, JSONObject json) throws JSONException {
        //if (ptrFrameLayout!=null) ptrFrameLayout.refreshComplete();
        JSONArray arr = json.getJSONArray("list");
        if (arr.length() == 0) return;
        if (vipShops == null) vipShops = new ArrayList<>();
        else if (page == 1 && !vipShops.isEmpty()) vipShops.clear();
        Gson gson = new Gson();
        for (int i = 0; i < arr.length(); i++) {
            vipShops.add(gson.fromJson(arr.getString(i), VipShop.class));
        }
        if (sbCommodityAdapter != null) {
            sbCommodityAdapter.refresh(vipShops);
            return;
        }
        sbCommodityAdapter = new VipShopAdapter(this, vipShops);
        shopList.setAdapter(sbCommodityAdapter);
    }

    @OnClick(R.id.back_rl)
    public void onViewClicked() {
        finish();
    }

    @Override
    public void onLocationChanged(AMapLocation aMapLocation) {
        if (aMapLocation != null) {
            if (aMapLocation.getErrorCode() == 0) {
                this.aMapLocation = aMapLocation;
                if (mListener != null) mListener.onLocationChanged(aMapLocation);
                vip();
                deactivate();
            } else {
                //定位失败时，可通过ErrCode（错误码）信息来确定失败的原因，errInfo是错误信息，详见错误码表。
                Log.e("AmapError", "location Error, ErrCode:"
                        + aMapLocation.getErrorCode() + ", errInfo:"
                        + aMapLocation.getErrorInfo());
            }
        }
    }

    @Override
    public void activate(OnLocationChangedListener onLocationChangedListener) {
        mListener = onLocationChangedListener;
        if (mlocationClient == null) {
            mlocationClient = new AMapLocationClient(this);
            mLocationOption = new AMapLocationClientOption();
            //设置定位监听
            mlocationClient.setLocationListener(this);
            //设置为高精度定位模式
            mLocationOption.setLocationMode(AMapLocationClientOption.AMapLocationMode.Battery_Saving);
            //设置定位参数
            mlocationClient.setLocationOption(mLocationOption);
            // 此方法为每隔固定时间会发起一次定位请求，为了减少电量消耗或网络流量消耗，
            // 注意设置合适的定位时间的间隔（最小间隔支持为2000ms），并且在合适时间调用stopLocation()方法来取消定位请求
            // 在定位结束后，在合适的生命周期调用onDestroy()方法
            // 在单次定位情况下，定位无论成功与否，都无需调用stopLocation()方法移除请求，定位sdk内部会移除
            mlocationClient.startLocation();
        }
    }

    /**
     * 停止定位  调了这个activate（） 必须要在onLocationChanged（）调下面这个
     */
    @Override
    public void deactivate() {
        mListener = null;
        if (mlocationClient != null) {
            mlocationClient.stopLocation();
            mlocationClient.onDestroy();
        }
        mlocationClient = null;
    }
}
