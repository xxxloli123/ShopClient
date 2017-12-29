package com.zsh.shopclient.receiver;

import android.content.Context;
import android.util.Log;

import com.tencent.android.tpush.XGPushBaseReceiver;
import com.tencent.android.tpush.XGPushClickedResult;
import com.tencent.android.tpush.XGPushRegisterResult;
import com.tencent.android.tpush.XGPushShowedResult;
import com.tencent.android.tpush.XGPushTextMessage;

/**
 * Created by Administrator on 2017/11/1.
 */

public class MessageReceiver extends XGPushBaseReceiver {
    @Override
    public void onRegisterResult(Context context, int i, XGPushRegisterResult xgPushRegisterResult) {
        Log.d("MessageReceiver","onRegisterResult-i->"+i+"-xgPushRegisterResult->"+xgPushRegisterResult);
    }

    @Override
    public void onUnregisterResult(Context context, int i) {
        Log.d("MessageReceiver","onUnregisterResult-i->"+i);
    }

    @Override
    public void onSetTagResult(Context context, int i, String s) {
        Log.d("MessageReceiver","onSetTagResult-i->"+i+"s->"+s);
    }

    @Override
    public void onDeleteTagResult(Context context, int i, String s) {
        Log.d("MessageReceiver","onDeleteTagResult-i->"+i+"s->"+s);
    }

    @Override
    public void onTextMessage(Context context, XGPushTextMessage xgPushTextMessage) {
        Log.d("MessageReceiver","onTextMessage->"+xgPushTextMessage);
    }

    @Override
    public void onNotifactionClickedResult(Context context, XGPushClickedResult xgPushClickedResult) {
        Log.d("MessageReceiver","onNotifactionClickedResult->"+xgPushClickedResult);
        //Intent intent = new Intent(context,MainActivity.class);
        //intent.putExtra("activity","MainActivity");
        //intent.putExtra("news",xgPushClickedResult.getContent());
        //context.startActivity(intent);
    }

    @Override
    public void onNotifactionShowedResult(Context context, XGPushShowedResult xgPushShowedResult) {
        Log.d("MessageReceiver","onNotifactionShowedResult->"+xgPushShowedResult);
    }
}
