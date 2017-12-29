package com.zsh.shopclient.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.zsh.shopclient.aPresenter.BaseActivity;
import com.zsh.shopclient.aView.LoginV;

/**
 * Created by Administrator on 2017/11/1.
 */

public class TimeBr extends BroadcastReceiver {
    private BaseActivity activity;

    public TimeBr(BaseActivity loginActivity) {
        this.activity = loginActivity;
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        if(LoginV.ACTION_TIME == intent.getAction())
            ((LoginV)activity.getTypeView()).setVerifyCode();
    }
}
