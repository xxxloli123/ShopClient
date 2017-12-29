package com.zsh.shopclient.aPresenter;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.View;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationListener;
import com.zsh.shopclient.R;
import com.zsh.shopclient.aView.AddRegionV;
import com.zsh.shopclient.aView.BaseView;
import com.zsh.shopclient.aView.MainV;
import com.zsh.shopclient.fPresenter.HomeFragment;

/**
 * Created by Administrator on 2017/12/11.
 */

public class AddRegionActivity extends BaseActivity {
    private AMapLocation location;
    private AMapLocationClient locationClient;
    private AMapLocationClientOption locationOption;
    private Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if(null != location) {
                handler.removeCallbacks(runnable);
                Intent intent = new Intent();
                String[] destination = new String[]{((String[])msg.obj)[0],((String[])msg.obj)[1],location.getProvince(),location.getCity(), location.getDistrict()
                        , getIntent().getCharSequenceArrayExtra(getString(R.string.send))[0]+((String[]) msg.obj)[2],getString(R.string.cityProper),""+location.getLongitude(),""+location.getLatitude()};
                intent.putExtra(getString(R.string.send),destination);
                intent.putExtra(getString(R.string.attach),true);
                setResult(RESULT_OK,intent);
                ((AddRegionV)getTypeView()).loadingDismiss();
                finish();
            }else{
                handler.removeCallbacks(runnable);
                handler.postDelayed(runnable,2000);
            }
        }
    };
    private Runnable runnable = new Runnable() {
        @Override
        public void run() {
            handler.removeCallbacks(runnable);
            handler.postDelayed(runnable,2000);
        }
    };
    private void aMapLocatioin(){
        locationClient = new AMapLocationClient(this);
        locationOption = new AMapLocationClientOption();
        locationOption.setLocationMode(AMapLocationClientOption.AMapLocationMode.Hight_Accuracy);
        locationOption.setInterval(0);
        locationClient.setLocationOption(locationOption);
        locationClient.setLocationListener(new AMapLocationListener() {
            @Override
            public void onLocationChanged(AMapLocation aMapLocation) {
                if(null != aMapLocation)
                    if(0 == aMapLocation.getErrorCode()){
                        location = aMapLocation;
                        locationClient.stopLocation();
                        locationClient.onDestroy();
                    }else
                        Log.e("MainActivity", "location Error, ErrCode:" + aMapLocation.getErrorCode() + ", errInfo:" + aMapLocation.getErrorInfo());
            }
        });
        locationClient.startLocation();
    }
    @Override
    protected BaseView createTypeView() {
        return new AddRegionV();
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        aMapLocatioin();
    }

    public View.OnClickListener getClickListener(){
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (v.getId()){
                    case R.id.image_goBack:
                        finish();
                        break;
                    case R.id.text_sumbit:
                        String[] inputs = ((AddRegionV)getTypeView()).getEdits();
                        if(null != inputs) {
                            Message message = new Message();
                            message.obj = inputs;
                            handler.sendMessage(message);
                            ((AddRegionV)getTypeView()).loadingDisplay();
                        }
                        break;
                }
            }
        };
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        locationClient.stopLocation();
        locationClient.onDestroy();
        handler.removeCallbacks(runnable);
    }
}
