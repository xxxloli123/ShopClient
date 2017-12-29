package com.zsh.shopclient.aPresenter;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.tencent.android.tpush.XGIOperateCallback;
import com.tencent.android.tpush.XGPushConfig;
import com.tencent.android.tpush.XGPushManager;
import com.xxx.skynet.sharedPreference.IOSharedPreferences;
import com.xxx.skynet.sqlSave.TypeSqliteHelper;
import com.zsh.shopclient.R;
import com.zsh.shopclient.tool.JsonHelper;
import com.zsh.shopclient.aView.BaseView;
import com.zsh.shopclient.aView.LoginV;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by Administrator on 2017/10/30.
 */

public class LoginActivity extends BaseActivity {
    private JSONObject sms;
    private HashMap<String,Object> userMap;

    @Override
    protected BaseView createTypeView() {
        return new LoginV();
    }
    public TextWatcher getInListener(final TextView text){
        return new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {}

            @Override
            public void afterTextChanged(Editable s) {
                if(11 == s.length()) {
                    text.setClickable(true);
                    text.setSelected(true);
                }else {
                    text.setClickable(false);
                    text.setSelected(false);
                }
            }
        };
    }
    public View.OnClickListener getClickListener() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (v.getId()){
                    case R.id.image_goBack:
                        finish();
                        break;
                    case R.id.text_getVerigyCode:
                        //startService(new Intent(LoginActivity.this, TimeService.class));
                        ((LoginV) getTypeView()).setVerifyCode();
                        linkNet(getString(R.string.getMessage),null);
                        break;
                    case R.id.text_signIn:
                        String code = ((LoginV)getTypeView()).getVerifyCode();
                        if(!"".equals(code) && null != sms)
                            try {
                                ((LoginV)getTypeView()).loadingDisplay();
                                sms.put("code",code);
                                if(((LoginV) getTypeView()).getIAgree())
                                    registerXinGe();
                                else
                                    Toast.makeText(LoginActivity.this,getString(R.string.agreementDisagee),Toast.LENGTH_SHORT).show();
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        else
                            Toast.makeText(LoginActivity.this,getString(R.string.noCode),Toast.LENGTH_SHORT).show();
                        break;
                    case R.id.text_agreement:
                        Intent intent = new Intent(LoginActivity.this,HelpActivity.class);
                        intent.putExtra(HelpActivity.URI,getString(R.string.userAgreement));
                        startActivity(intent);
                        break;
                }
            }
        };
    }
    private void registerXinGe(){
        XGPushConfig.enableDebug(this, true);//开启信鸽日志输出
        XGPushManager.registerPush(this, new XGIOperateCallback() {
            @Override
            public void onSuccess(Object data, int flag) {
                Log.d("TPush", "注册成功，设备token为：" + data);
                linkNet(getString(R.string.login),data.toString().trim());
            }
            @Override
            public void onFail(Object data, int errCode, String msg) {
                Log.d("TPush", "注册失败，错误码：" + errCode + ",错误信息：" + msg);
                Toast.makeText(LoginActivity.this,getString(R.string.signInDefeated),Toast.LENGTH_SHORT).show();
                ((LoginV) getTypeView()).loadingDismiss();
            }
        });
    }
    private void linkNet(final String path,final String token){
        MultipartBody.Builder requestBody = new MultipartBody.Builder().setType(MultipartBody.FORM);
        requestBody.addFormDataPart("phone",((LoginV)getTypeView()).getPhone());
        if(null != token)
            requestBody.addFormDataPart("phoneType", "Android").addFormDataPart("token", token).addFormDataPart("smsStr", sms.toString());
        else
            requestBody.addFormDataPart("codeType","0");
        Request.Builder request = new Request.Builder().url(getString(R.string.ip)+path).method("POST",requestBody.build());
        new OkHttpClient().newCall(request.build()).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
                getTypeView().hintHoast(R.string.getDataDefeated,null,Toast.LENGTH_SHORT);
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String result = response.body().string();
                Log.i("LoginActivity",result);
                if(200 == response.code())
                    try {
                        JSONObject jsonObj = new JSONObject(result);
                        if(null != token) {
                            userMap = JsonHelper.jsonObject2Map(jsonObj.getJSONObject("user"));
                            save();
                        }else
                            sms = jsonObj.getJSONObject("sms");
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            if(null != userMap){
                                Intent intent = new Intent();
                                Bundle bundle = new Bundle();
                                bundle.putSerializable(getString(R.string.returnData),userMap);
                                intent.putExtras(bundle);
                                setResult(RESULT_OK,intent);
                                finish();
                            } else if(null != token && null == userMap) {
                                Toast.makeText(LoginActivity.this,getString(R.string.signInDefeated),Toast.LENGTH_SHORT).show();
                                ((LoginV) getTypeView()).loadingDismiss();
                            }
                        }
                    });
            }
        });
    }
    private void save(){
        List<String> fields = new ArrayList<>();
        fields.add("user");
        for(String name :userMap.keySet())
            fields.add(name);
        TypeSqliteHelper save = new TypeSqliteHelper(this,getString(R.string.sqlLibrary),null,1,fields);
        save.insert("user",userMap);
        save.close();
        IOSharedPreferences.outString(this,getString(R.string.user),getString(R.string.userId),(String) userMap.get("id"));
    }
}
