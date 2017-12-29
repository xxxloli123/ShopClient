package com.slowlife.map.interfaces;

import android.app.Activity;
import android.util.Log;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationListener;
import com.amap.api.maps2d.LocationSource;
import com.amap.api.services.core.LatLonPoint;
import com.amap.api.services.geocoder.GeocodeSearch;
import com.amap.api.services.geocoder.RegeocodeAddress;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;

/**
 * Created by sgrape on 2017/5/13.
 * e-mail: sgrape1153@gmail.com
 */

public class APSImpl implements APSInterface, LocationSource,
        AMapLocationListener {
    private Activity act;
    private LocationSource.OnLocationChangedListener mListener;
    private AMapLocationClient mlocationClient;
    // /声明mLocationOption对象
    private AMapLocationClientOption mLocationOption;
    private AMapLocation mapLocation;
    private RegeocodeAddress result;
    private LatLonPoint point;
    private GeocodeSearch geocoderSearch;
    private ExecutorService mExecutorService;
    private List<OnApsChanged> listeners;

    public APSImpl(Activity activity) {
        act = activity;
        listeners = new ArrayList<>();
    }


    public RegeocodeAddress getResult() {
        return result;
    }

    public LatLonPoint getPoint() {
        return point;
    }

    @Override
    public void onCreate() {
        mlocationClient = new AMapLocationClient(act.getApplicationContext());
        //初始化定位参数
        mLocationOption = new AMapLocationClientOption();
        //设置定位监听
        mlocationClient.setLocationListener(this);
        //设置定位模式为高精度模式，Battery_Saving为低功耗模式，Device_Sensors是仅设备模式
        mLocationOption.setLocationMode(AMapLocationClientOption.AMapLocationMode.Hight_Accuracy);
        //设置定位间隔,单位毫秒,默认为2000ms
//        mLocationOption.setInterval(2000);
        mLocationOption.setOnceLocation(true);
//单位是毫秒，默认30000毫秒，建议超时时间不要低于8000毫秒。
        mLocationOption.setHttpTimeOut(10000);
        mLocationOption.setWifiScan(true);
//获取最近3s内精度最高的一次定位结果：
//设置setOnceLocationLatest(boolean b)接口为true，启动定位时SDK会返回最近3s内精度最高的一次定位结果。如果设置其为true，setOnceLocation(boolean b)接口也会被设置为true，反之不会，默认为false。
        mLocationOption.setOnceLocationLatest(true);
        //设置定位参数
        mlocationClient.setLocationOption(mLocationOption);
        // 此方法为每隔固定时间会发起一次定位请求，为了减少电量消耗或网络流量消耗，
        // 注意设置合适的定位时间的间隔（最小间隔支持为1000ms），并且在合适时间调用stopLocation()方法来取消定位请求
        // 在定位结束后，在合适的生命周期调用onDestroy()方法
        // 在单次定位情况下，定位无论成功与否，都无需调用stopLocation()方法移除请求，定位sdk内部会移除
        //启动定位
        mlocationClient.startLocation();
        geocoderSearch = new GeocodeSearch(act);
    }

    /**
     * 方法必须重写
     */
    @Override
    public void onPause() {
        deactivate();
        if (mExecutorService != null)
            mExecutorService.shutdown();
        if (mlocationClient != null)
            mlocationClient.onDestroy();
    }

    /**
     * 激活定位
     */
    @Override
    public void activate(LocationSource.OnLocationChangedListener listener) {
        mListener = listener;
        if (mlocationClient == null) {
            mlocationClient = new AMapLocationClient(act);
            mLocationOption = new AMapLocationClientOption();
            //设置定位监听
            mlocationClient.setLocationListener(this);
            //设置为高精度定位模式
            mLocationOption.setLocationMode(AMapLocationClientOption.AMapLocationMode.Hight_Accuracy);
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
     * 停止定位
     */
    @Override
    public void deactivate() {
        mListener = null;
        if (mlocationClient != null) {
            mlocationClient.stopLocation();
            mlocationClient.onDestroy();
        }
        mlocationClient = null;
        if (mExecutorService != null)
            mExecutorService.shutdown();
    }


    /**
     * 定位成功后回调函数
     */
    @Override
    public void onLocationChanged(AMapLocation amapLocation) {
        if (amapLocation != null && amapLocation.getErrorCode() == 0) {
            if (this.mapLocation == null) this.mapLocation = amapLocation;
            if (mListener != null) mListener.onLocationChanged(amapLocation);
            if (point == null)
                point = new LatLonPoint(amapLocation.getLatitude(), amapLocation.getLongitude());
            else {
                point.setLatitude(amapLocation.getLatitude());
                point.setLongitude(amapLocation.getLongitude());
            }
            for (OnApsChanged oac: listeners)oac.onChanged(amapLocation);
        } else {
            String errText = "定位失败," + amapLocation.getErrorCode() + ": " + amapLocation.getErrorInfo();
            Log.w("APSImpl", errText);
            for (OnApsChanged oap : listeners) oap.Fail();
        }
    }


    public void addListener(OnApsChanged l) {
        if (!listeners.contains(l))
            listeners.add(l);
    }

}
