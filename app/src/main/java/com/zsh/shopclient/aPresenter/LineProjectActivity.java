package com.zsh.shopclient.aPresenter;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.ListView;

import com.amap.api.services.core.AMapException;
import com.amap.api.services.core.LatLonPoint;
import com.amap.api.services.route.BusRouteResult;
import com.amap.api.services.route.DrivePath;
import com.amap.api.services.route.DriveRouteResult;
import com.amap.api.services.route.RidePath;
import com.amap.api.services.route.RideRouteResult;
import com.amap.api.services.route.RouteSearch;
import com.amap.api.services.route.WalkPath;
import com.amap.api.services.route.WalkRouteResult;
import com.slowlife.map.util.ToastUtil;
import com.zsh.shopclient.R;
import com.zsh.shopclient.aView.BaseView;
import com.zsh.shopclient.aView.LineProjectV;
import com.zsh.shopclient.adapter.BusResultListAdapter;

import overlay.DrivingRouteOverlay;
import overlay.RideRouteOverlay;
import overlay.WalkRouteOverlay;

import static com.zsh.shopclient.aView.LineProjectV.IDnAVI;

/**
 * Created by Administrator on 2017/11/30.
 */

public class LineProjectActivity extends BaseActivity {
    private RouteSearch routeSearch;
    private double[] line;

    @Override
    protected BaseView createTypeView() {
        return new LineProjectV();
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        line = getIntent().getDoubleArrayExtra(getString(R.string.send));
        ((LineProjectV)getTypeView()).initMap(savedInstanceState);
        setRouteSearch();
    }
    @Override
    protected void onDestroy() {
        ((LineProjectV)getTypeView()).getMapView().onDestroy();
        super.onDestroy();
    }
    @Override
    protected void onResume() {
        ((LineProjectV)getTypeView()).getMapView().onResume();
        super.onResume();
    }
    @Override
    protected void onPause() {
        ((LineProjectV)getTypeView()).getMapView().onPause();
        super.onPause();
    }
    @Override
    protected void onSaveInstanceState(Bundle outState) {
        ((LineProjectV)getTypeView()).getMapView().onSaveInstanceState(outState);
        super.onSaveInstanceState(outState);
    }
    public void setRouteSearch(){
        routeSearch = new RouteSearch(this);
        routeSearch.calculateDriveRouteAsyn(new RouteSearch.DriveRouteQuery(new RouteSearch.FromAndTo(new LatLonPoint(line[1],line[0]),
                new LatLonPoint(line[3],line[2])),RouteSearch.DrivingDefault,null,null,""));
        routeSearch.setRouteSearchListener(new RouteSearch.OnRouteSearchListener() {
            @Override
            public void onBusRouteSearched(BusRouteResult busRouteResult, int errorCode) {
                if (errorCode == AMapException.CODE_AMAP_SUCCESS) {
                    if (busRouteResult != null && busRouteResult.getPaths() != null) {
                        if (busRouteResult.getPaths().size() > 0) {
                            ((LineProjectV)getTypeView()).setListView(busRouteResult);
                        } else if (busRouteResult != null && busRouteResult.getPaths() == null) {
                            ToastUtil.show(LineProjectActivity.this, R.string.failedLinePlanning);
                        }
                    } else {
                        ToastUtil.show(LineProjectActivity.this, R.string.failedLinePlanning);
                    }
                } else {
                    ToastUtil.showerror(LineProjectActivity.this, errorCode);
                }
            }

            @Override
            public void onDriveRouteSearched(DriveRouteResult driveRouteResult, int errorCode) {
                if (errorCode == AMapException.CODE_AMAP_SUCCESS)
                    if (driveRouteResult != null && driveRouteResult.getPaths() != null)
                        if (driveRouteResult.getPaths().size() > 0) {
                            final DrivePath drivePath = driveRouteResult.getPaths().get(0);
                            DrivingRouteOverlay drivingRouteOverlay = new DrivingRouteOverlay(LineProjectActivity.this, ((LineProjectV)getTypeView()).getaMap(), drivePath,
                                    driveRouteResult.getStartPos(), driveRouteResult.getTargetPos(), null);
                            drivingRouteOverlay.setNodeIconVisibility(false);//设置节点marker是否显示
                            drivingRouteOverlay.setIsColorfulline(true);//是否用颜色展示交通拥堵情况，默认true
                            drivingRouteOverlay.removeFromMap();
                            drivingRouteOverlay.addToMap();
                            drivingRouteOverlay.zoomToSpan();
                        } else if (driveRouteResult != null && driveRouteResult.getPaths() == null)
                            ToastUtil.show(LineProjectActivity.this, R.string.failedLinePlanning);
                    else
                        ToastUtil.show(LineProjectActivity.this, R.string.failedLinePlanning);
                else
                    ToastUtil.showerror(LineProjectActivity.this, errorCode);
            }

            @Override
            public void onWalkRouteSearched(WalkRouteResult walkRouteResult, int errorCode) {
                if (errorCode == AMapException.CODE_AMAP_SUCCESS)
                    if (walkRouteResult != null && walkRouteResult.getPaths() != null)
                        if (walkRouteResult.getPaths().size() > 0) {
                            final WalkPath walkPath = walkRouteResult.getPaths().get(0);
                            WalkRouteOverlay walkRouteOverlay = new WalkRouteOverlay(LineProjectActivity.this, ((LineProjectV)getTypeView()).getaMap(), walkPath,
                                    walkRouteResult.getStartPos(), walkRouteResult.getTargetPos());
                            walkRouteOverlay.removeFromMap();
                            walkRouteOverlay.addToMap();
                            walkRouteOverlay.zoomToSpan();
                        } else if (walkRouteResult != null && walkRouteResult.getPaths() == null)
                            ToastUtil.show(LineProjectActivity.this, R.string.failedLinePlanning);
                    else
                        ToastUtil.show(LineProjectActivity.this, R.string.failedLinePlanning);
                else
                    ToastUtil.showerror(LineProjectActivity.this, errorCode);
            }

            @Override
            public void onRideRouteSearched(RideRouteResult rideRouteResult, int errorCode) {
                if(AMapException.CODE_AMAP_SUCCESS == errorCode)
                    if(null != rideRouteResult && null != rideRouteResult.getPaths())
                        if(0< rideRouteResult.getPaths().size()){
                            final RidePath ridePath = rideRouteResult.getPaths().get(0);
                            RideRouteOverlay rideRouteOverlay = new RideRouteOverlay(LineProjectActivity.this,((LineProjectV)getTypeView()).getaMap(), ridePath,rideRouteResult.getStartPos(),rideRouteResult.getTargetPos());
                            rideRouteOverlay.removeFromMap();
                            rideRouteOverlay.addToMap();
                            rideRouteOverlay.zoomToSpan();
                        }else if (null != rideRouteResult && null == rideRouteResult.getPaths())
                            ToastUtil.show(LineProjectActivity.this, R.string.failedLinePlanning);
                    else
                        ToastUtil.show(LineProjectActivity.this, R.string.failedLinePlanning);
                else
                    ToastUtil.show(LineProjectActivity.this, errorCode);
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
                    case IDnAVI:
                        Intent intent = new Intent(LineProjectActivity.this,IntelligentBroadcastActivity.class);
                        intent.putExtra(getString(R.string.line),line);
                        startActivity(intent);
                        break;
                    case R.id.image_drive:
                        ((LineProjectV)getTypeView()).setSelected(v);
                        routeSearch.calculateDriveRouteAsyn(new RouteSearch.DriveRouteQuery(new RouteSearch.FromAndTo(new LatLonPoint(line[1],line[0]),
                                new LatLonPoint(line[3],line[2])),RouteSearch.DrivingDefault,null,null,""));
                        break;
                    case R.id.image_walk:
                        ((LineProjectV)getTypeView()).setSelected(v);
                        LatLonPoint startPoint = new LatLonPoint(line[1],line[0]);
                        LatLonPoint endPoint = new LatLonPoint(line[3],line[2]);
                        routeSearch.calculateWalkRouteAsyn(new RouteSearch.WalkRouteQuery(new RouteSearch.FromAndTo(startPoint,endPoint),RouteSearch.WalkDefault));
                        break;
                    case R.id.image_bus:
                        ((LineProjectV)getTypeView()).setSelected(v);
                        routeSearch.calculateBusRouteAsyn(new RouteSearch.BusRouteQuery(new RouteSearch.FromAndTo(new LatLonPoint(line[1],line[0]),
                                new LatLonPoint(line[3],line[2])),RouteSearch.BusDefault,getIntent().getStringExtra(getString(R.string.city)),0));
                        break;
                    case R.id.image_bicycling:
                        ((LineProjectV)getTypeView()).setSelected(v);
                        routeSearch.calculateRideRouteAsyn(new RouteSearch.RideRouteQuery(new RouteSearch.FromAndTo(new LatLonPoint(line[1],line[0]),
                                new LatLonPoint(line[3],line[2])),RouteSearch.RidingDefault));
                        break;
                }
            }
        };
    }
}
