package com.zsh.shopclient.service;

import android.app.Service;
import android.content.Intent;
import android.os.CountDownTimer;
import android.os.IBinder;
import android.support.annotation.Nullable;

import com.zsh.shopclient.R;
import com.zsh.shopclient.aView.LoginV;

/**
 * Created by Administrator on 2017/11/1.
 */

public class TimeService extends Service {
    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
    @Override
    public int onStartCommand(final Intent intent, int flags, int startId) {
        final Intent timeIntent = new Intent(LoginV.ACTION_TIME);
        new CountDownTimer(60000,1000){
            @Override
            public void onTick(long millisUntilFinished) {
                timeIntent.putExtra(getString(R.string.notification),millisUntilFinished/1000+"s");
                sendBroadcast(timeIntent);
            }

            @Override
            public void onFinish() {
                TimeService.this.stopSelf();
                timeIntent.putExtra(getString(R.string.notification),getString(R.string.getVerigyCode));
                sendBroadcast(timeIntent);
            }
        }.start();
        return super.onStartCommand(intent, flags, startId);
    }
}
