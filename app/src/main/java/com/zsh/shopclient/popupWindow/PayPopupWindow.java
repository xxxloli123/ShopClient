package com.zsh.shopclient.popupWindow;

import android.graphics.drawable.ColorDrawable;
import android.os.Handler;
import android.os.Message;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.PopupWindow;
import android.widget.Toast;

import com.alipay.sdk.app.PayTask;
import com.tencent.mm.opensdk.modelpay.PayReq;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;
import com.xxx.skynet.sharedPreference.IOSharedPreferences;
import com.zsh.shopclient.R;
import com.zsh.shopclient.aPresenter.BaseActivity;
import com.zsh.shopclient.model.WXPay;
import com.zsh.shopclient.util.JsonHelp;
import com.zsh.shopclient.util.PayResult;
import com.zsh.shopclient.wxapi.WXEntryActivity;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Date;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.SortedMap;
import java.util.TreeMap;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by Administrator on 2017/12/9.
 */

public class PayPopupWindow {
    private static final byte ALIPAY = 0;
    public static final byte SUCCEED = 1;
    public static final byte DEFEATED = -1;
    private BaseActivity activity;
    private PopupWindow popupWindow;
    private Map map;
    private PayResultListener payResultListener;
    private Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what){
                case ALIPAY:
                    PayResult payResult = new PayResult((Map<String, String>) msg.obj);
                    String resultInfo = payResult.getResult();// 同步返回需要验证的信息
                    String resultStatus = payResult.getResultStatus();
                    if(null != payResultListener)
                        payResultListener.inform(TextUtils.equals(resultStatus, "9000")? SUCCEED : DEFEATED);
                    Toast.makeText(activity,activity.getString(TextUtils.equals(resultStatus, "9000") ? R.string.paymentSuccess :R.string.paymentFailure),Toast.LENGTH_SHORT).show();
                    break;
            }
        }
    };
    private View.OnClickListener listener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if(null != map)
            if(null != map)
                switch (v.getId()){
                    case R.id.text_alipayPay:
                        linkNet("alipay");
                        break;
                    case R.id.text_weChatPay:
                        linkNet("weChat");
                        break;
                    case R.id.text_balancePay:
                        Toast.makeText(activity,activity.getString(R.string.temporaryNotOpen),Toast.LENGTH_SHORT).show();
                        break;
                }
        }
    };
    private void linkNet(final String payWay){
        MultipartBody.Builder requestBody = new MultipartBody.Builder().setType(MultipartBody.FORM);
        Iterator iterator = map.keySet().iterator();
        while(iterator.hasNext()){
            String key = (String) iterator.next();
            requestBody.addFormDataPart(key,(String) map.get(key));
        }
        new OkHttpClient().newCall(new Request.Builder().url(activity.getString(R.string.ip)+ activity.getString(payWay.equals("alipay")? R.string.alipayPayUrl : R.string.weChatPayUrl)).post(requestBody.build()).build()).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                try {
                    JSONObject result = new JSONObject(response.body().string());
                    Log.i("CloseAccountActivity", "onCreate->" + result);
                    if (200 == result.getInt("statusCode"))
                        if ("alipay".equals(payWay))
                            alipay(result.getString("alipayBody"));
                        else {
                            JSONObject wxResult = result.getJSONObject("wxresult");
                            WXPay wxPay = new WXPay();
                            wxPay.setAppid(wxResult.getJSONArray("appid").getString(0));
                            wxPay.setMch_id(wxResult.getJSONArray("mch_id").getString(0));
                            wxPay.setPrepay_id(wxResult.getJSONArray("prepay_id").getString(0));
                            wxPay.setSign(wxResult.getJSONArray("sign").getString(0));
                            wxPay.setResult_code(wxResult.getJSONArray("result_code").getString(0));
                            wxPay.setReturn_msg(wxResult.getJSONArray("return_msg").getString(0));
                            wxPay.setNonce_str(wxResult.getJSONArray("nonce_str").getString(0));
                            wxPay.setReturn_code(wxResult.getJSONArray("return_code").getString(0));
                            wxPay.setTrade_type(wxResult.getJSONArray("trade_type").getString(0));
                            wxPay(wxPay);//JsonHelp.fromJson(result.getString("wxresult"), WXPay.class));
                        }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }
    private void alipay(final String parameter){
        new Thread(new Runnable() {
            @Override
            public void run() {
                PayTask alipay = new PayTask(activity);
                Message msg = new Message();
                msg.what = ALIPAY;
                msg.obj = alipay.payV2(parameter,true);;
                handler.sendMessage(msg);
            }
        }).start();
    }
    private void wxPay(WXPay wxPay){
        IWXAPI iwxapi = WXAPIFactory.createWXAPI(activity,wxPay.getAppid(),true);
        iwxapi.registerApp(WXEntryActivity.APPID);
        PayReq request = new PayReq();
        request.appId = wxPay.getAppid();
        request.partnerId = wxPay.getMch_id();
        request.prepayId = wxPay.getPrepay_id();
        request.packageValue = "Sign=WXPay";
        request.nonceStr = randomCode(32);
        request.timeStamp = (new Date().getTime()/1000)+"";
        SortedMap<Object, Object> params = new TreeMap<Object, Object>();
        params.put("appid", request.appId);
        params.put("partnerid", request.partnerId);
        params.put("prepayid", request.prepayId);
        params.put("package", request.packageValue);
        params.put("noncestr",  request.nonceStr);
        params.put("timestamp", request.timeStamp);
        request.sign = createSign(params);
        iwxapi.sendReq(request);
    }
    private void backgroundAlpha(final float alpha){
        WindowManager.LayoutParams lp = activity.getWindow().getAttributes();
        lp.alpha = alpha;
        activity.getWindow().setAttributes(lp);
    }
    public PayPopupWindow(BaseActivity activity,Map<String,String> map,PayResultListener payResultListener) {
        this.activity = activity;
        this.map = map;
        this.payResultListener = payResultListener;
        View layout = LayoutInflater.from(activity).inflate(R.layout.popupwindow_pay,null);
        layout.findViewById(R.id.text_alipayPay).setOnClickListener(listener);
        layout.findViewById(R.id.text_weChatPay).setOnClickListener(listener);
        layout.findViewById(R.id.text_balancePay).setOnClickListener(listener);
        popupWindow = new PopupWindow(layout, ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.WRAP_CONTENT,true);
        popupWindow.setOutsideTouchable(true);
        popupWindow.setTouchable(true);
        popupWindow.setBackgroundDrawable(new ColorDrawable(ContextCompat.getColor(activity,R.color.lucency)));
        popupWindow.setAnimationStyle(R.style.BottomAlphaAnimation);
        popupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                backgroundAlpha(1.0f);
            }
        });
    }
    public void displayPopupWindow(){
        backgroundAlpha(0.6f);
        popupWindow.showAtLocation(activity.findViewById(android.R.id.content), Gravity.BOTTOM,0,0);//Activity.getWindow.getDecorView()获得activity的rootView
    }
    public void dismiss(){
        popupWindow.dismiss();
    }
    public interface PayResultListener{
        void inform(byte result);
    }
    // 随机码
    public static String randomCode(int bit) {
        String code = "";
        int index = 0;
        String[] pond = new String[] { "0", "1", "2", "3", "4", "5", "6", "7", "8", "9","a","b","c" };
        while (code.length() < bit) {
            index = (int) (Math.random() * pond.length);
            code += pond[index];
        }
        return code;
    }
    // 创建md5 数字签证
    public static String createSign(SortedMap<Object, Object> parame) {
        StringBuffer buffer = new StringBuffer();
        Set set = parame.entrySet();
        Iterator iterator = set.iterator();
        while (iterator.hasNext()) {
            Map.Entry entry = (Map.Entry) iterator.next();
            String key = (String) entry.getKey();
            Object value = entry.getValue();
            if (null != value && !"".equals(value) && !"sign".equals(key) && !"key".equals(key))
                buffer.append(key + "=" + value + "&");
        }
        buffer.append("key=" + WXEntryActivity.PARTNER_KEY);
        System.out.println(">>>    buffer>>>>>>>"+buffer);
        return encode(buffer.toString(), "MD5");
    }
    private static String encode(String str, String method) {
        MessageDigest md = null;
        String dstr = null;
        try {
            md = MessageDigest.getInstance(method);
            md.update(str.getBytes());
            dstr = new BigInteger(1, md.digest()).toString(16);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return dstr;
    }
}
