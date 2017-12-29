package com.zsh.shopclient.aView;

import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationListener;
import com.amap.api.maps.AMap;
import com.amap.api.maps.CameraUpdateFactory;
import com.amap.api.maps.LocationSource;
import com.amap.api.maps.MapView;
import com.amap.api.maps.model.MyLocationStyle;
import com.amap.api.services.route.BusPath;
import com.amap.api.services.route.BusRouteResult;
import com.zsh.shopclient.R;
import com.zsh.shopclient.aPresenter.LineProjectActivity;
import com.zsh.shopclient.adapter.BusResultListAdapter;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import butterknife.BindView;

/**
 * Created by Administrator on 2017/11/30.
 */

public class LineProjectV extends BaseView {
    public static final int IDnAVI = 0;
    @BindView(R.id.mapView) MapView mapView;
    @BindView(R.id.bus_result_list)ListView listView;
    private AMap aMap;
    private AMapLocation mapLocation;
    private AMapLocationClientOption mLocationOption;
    private AMapLocationClient mlocationClient;
    private LocationSource.OnLocationChangedListener mListener;
    @Override
    protected int getLayoutId() {
        return R.layout.activity_map_line_project;
    }

    @Override
    public void initView() {
        LineProjectActivity activity = (LineProjectActivity)getRootView().getContext();
        View.OnClickListener listener = activity.getClickListener();
        getRootView().findViewById(R.id.image_goBack).setOnClickListener(listener);
        ((TextView)getRootView().findViewById(R.id.text_title)).setText("路线规划");
        LinearLayout linear = getRootView().findViewById(R.id.linear_navi);
        ImageView imageView = new ImageView(activity);
        imageView.setImageResource(R.mipmap.icon_navi);
        imageView.setId(IDnAVI);
        imageView.setOnClickListener(listener);
        linear.addView(imageView);
        View view = getRootView().findViewById(R.id.image_drive);
        view.setSelected(true);
        view.setOnClickListener(listener);
        getRootView().findViewById(R.id.image_walk).setOnClickListener(listener);
        getRootView().findViewById(R.id.image_bus).setOnClickListener(listener);
        getRootView().findViewById(R.id.image_bicycling).setOnClickListener(listener);
        listView.setAdapter(new BusResultListAdapter(activity,null));
    }
    public void setListView(BusRouteResult list){
        if(null != list) {
            listView.setVisibility(View.VISIBLE);
            ((BusResultListAdapter) listView.getAdapter()).update(list);
        }
    }
    public void setSelected(View view){
        LinearLayout linear = ((LinearLayout)view.getParent());
        for(int index = 0;index < linear.getChildCount();index++)
            linear.getChildAt(index).setSelected(view.getId() == linear.getChildAt(index).getId());
    }
    public void initMap(Bundle savedInstanceState){
        mapView.onCreate(savedInstanceState);
        if(null == aMap)
            aMap = mapView.getMap();
        aMap.getUiSettings().setZoomControlsEnabled(true);
        aMap.getUiSettings().setMyLocationButtonEnabled(true);
        aMap.getUiSettings().setScaleControlsEnabled(true);
        aMap.getUiSettings().setCompassEnabled(true);
        aMap.setTrafficEnabled(true);
        aMap.moveCamera(CameraUpdateFactory.zoomTo(16));
        aMap.setLocationSource(new LocationSource(){
            @Override
            public void activate(OnLocationChangedListener onLocationChangedListener) {
                mListener = onLocationChangedListener;
                if(null != mlocationClient) {
                    //初始化定位参数
                    mLocationOption = new AMapLocationClientOption();
                    //设置定位回调监听
                    locationListener();
                    //设置定位模式为高精度模式，Battery_Saving为低功耗模式，Device_Sensors是仅设备模式
                    mLocationOption.setLocationMode(AMapLocationClientOption.AMapLocationMode.Hight_Accuracy);
                    //设置定位间隔,单位毫秒,默认为2000ms
                    mLocationOption.setInterval(0);
                    //设置定位参数
                    mlocationClient.setLocationOption(mLocationOption);
                }
            }

            @Override
            public void deactivate() {
                mListener = null;
                if (mlocationClient != null) {
                    mlocationClient.stopLocation();
                    mlocationClient.onDestroy();
                }
                mlocationClient = null;
            }
        });
        //初始化定位
        mlocationClient = new AMapLocationClient(getRootView().getContext());
        MyLocationStyle myLocationStyle = new MyLocationStyle();
        //myLocationStyle.anchor(0.5f,0.5f);
        myLocationStyle.showMyLocation(true);
        myLocationStyle.strokeColor(Color.argb(0,0,0,0));
        myLocationStyle.radiusFillColor(Color.argb(0,0,0,0));
        aMap.setMyLocationEnabled(true);
        myLocationStyle.myLocationType(MyLocationStyle.LOCATION_TYPE_LOCATE);
        aMap.setMyLocationStyle(myLocationStyle);
        mlocationClient.startLocation();
    }
    public AMap getaMap(){
        return aMap;
    }
    private void locationListener(){
        mlocationClient.setLocationListener(new AMapLocationListener() {
            @Override
            public void onLocationChanged(AMapLocation aMapLocation) {
                if (aMapLocation != null) {
                    if (aMapLocation.getErrorCode() == 0) {
                        //定位成功回调信息，设置相关消息
                        aMapLocation.getLocationType();//获取当前定位结果来源，如网络定位结果，详见定位类型表
                        aMapLocation.getAccuracy();//获取精度信息
                        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                        Date date = new Date(aMapLocation.getTime());
                        df.format(date);//定位时间
                        mListener.onLocationChanged(aMapLocation);//显示系统小蓝点
                        mapLocation = aMapLocation;
                    } else {
                        Toast.makeText(getRootView().getContext(),"<v_v>定位失败", Toast.LENGTH_SHORT).show();
                        mLocationOption.setLocationMode(AMapLocationClientOption.AMapLocationMode.Device_Sensors);
                        //显示错误信息ErrCode是错误码，errInfo是错误信息，详见错误码表。
                        Log.e("AmapError", "location Error, ErrCode:" + aMapLocation.getErrorCode() + ", errInfo:" + aMapLocation.getErrorInfo());
                    }
                }
            }
        });
    }
    public MapView getMapView(){
        return mapView;
    }
}
