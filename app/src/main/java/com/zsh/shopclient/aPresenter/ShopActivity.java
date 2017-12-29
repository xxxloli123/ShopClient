package com.zsh.shopclient.aPresenter;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.xxx.skynet.sharedPreference.IOSharedPreferences;
import com.zsh.shopclient.R;
import com.zsh.shopclient.aView.BaseView;
import com.zsh.shopclient.aView.ShopV;
import com.zsh.shopclient.model.MerchantParticulars;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;

import cn.sharesdk.onekeyshare.OnekeyShare;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by Administrator on 2017/11/6.
 */

public class ShopActivity extends BaseActivity {
    public static final int SHOPPINGcART = 1;
    private CharSequence[] shopInfo;
    private MerchantParticulars shopInfos;

    @Override
    protected BaseView createTypeView() {
        return new ShopV();
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        shopInfo = getIntent().getCharSequenceArrayExtra(getString(R.string.title));
        new OkHttpClient().newCall(new Request.Builder().url(getString(R.string.ip)+getString(R.string.shopInfo)).post(new MultipartBody.Builder()
                .setType(MultipartBody.FORM).addFormDataPart("shopId",shopInfo[9].toString()).build()).build()).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {}

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                try {
                    JSONObject result = new JSONObject(response.body().string());
                    Log.i("ShopActivity","linkShopInfo->"+result);
                    if(200 == result.getInt("statusCode")){
                        shopInfos = new MerchantParticulars(null,result.getJSONObject("shop"));
                        shopInfos.getInfo()[15] = shopInfo[10];
                        shopInfos.initEvaluate(result.getJSONArray("listEvaluate"));
                        shopInfos.initActivitys(result.getJSONArray("listShopActivity"));
                        ((ShopV)getTypeView()).updateSupport(shopInfos.getInfo());
                    }else
                        getTypeView().hintHoast(BaseView.X,result.getString("message"), Toast.LENGTH_SHORT);
                } catch (JSONException e) {
                    e.printStackTrace();
                    getTypeView().hintHoast(R.string.parseFailure,null, Toast.LENGTH_SHORT);
                }
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(null != data)
            switch (requestCode){
                case SHOPPINGcART:
                    if(data.getBooleanExtra(getString(R.string.send),false))
                        ((com.zsh.shopclient.fView.ShopV)(((ShopV)getTypeView()).getFragment(0)).getTypeView()).updateShoppingCart(data.getIntExtra(getString(R.string.result),0),data.getFloatExtra(getString(R.string.attach),0.0f));
                    else
                        ((com.zsh.shopclient.fView.ShopV)(((ShopV)getTypeView()).getFragment(0)).getTypeView()).addShoppingCart(data.getIntExtra(getString(R.string.result),0),data.getFloatExtra(getString(R.string.attach),0.0f));
                    break;
            }
    }
    public View.OnClickListener getClickListener(){
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (v.getId()){
                    case R.id.image_goBack:
                        finish();
                        break;
                    case R.id.image_search:
                        Intent searchIntent = new Intent(ShopActivity.this,SearchActivity.class);
                        searchIntent.putExtra(getString(R.string.requestParam),getIntent().getStringExtra(getString(R.string.requestParam)));
                        searchIntent.putExtra(getString(R.string.path),R.string.searchCommodity);
                        searchIntent.putExtra(getString(R.string.attach),"productName");
                        LinkedHashMap<String,String> map = new LinkedHashMap<>();
                        map.put("shopId",shopInfo[9].toString());
                        Bundle bundle = new Bundle();
                        bundle.putSerializable(getString(R.string.sendBody),map);
                        searchIntent.putExtras(bundle);
                        startActivity(searchIntent);
                        break;
                    case R.id.image_share:
                        OnekeyShare oks = new OnekeyShare();
                        oks.disableSSOWhenAuthorize();//关闭sso授权
                        oks.setTitle("标题");
                        oks.setTitleUrl("http://sharesdk.cn");// titleUrl是标题的网络链接，QQ和QQ空间等使用
                        oks.setText("我是分享文本");// text是分享文本，所有平台都需要这个字段
                        // imagePath是图片的本地路径，Linked-In以外的平台都支持此参数
                        // oks.setImagePath("/sdcard/test.jpg");//确保SDcard下面存在此张图片
                        oks.setUrl("http://sharesdk.cn");// url仅在微信（包括好友和朋友圈）中使用
                        oks.setComment("我是测试评论文本");// comment是我对这条分享的评论，仅在人人网和QQ空间使用
                        oks.setSite(getString(R.string.app_name));// site是分享此内容的网站名称，仅在QQ空间使用
                        oks.setSiteUrl("http://sharesdk.cn");// siteUrl是分享此内容的网站地址，仅在QQ空间使用
                        oks.show(ShopActivity.this);
                        break;
                    case R.id.image_becomeMember:
                        final String userId = IOSharedPreferences.inString(ShopActivity.this,getString(R.string.user),getString(R.string.userId));
                        if(null != userId) {
                            MultipartBody.Builder body = new MultipartBody.Builder().setType(MultipartBody.FORM);
                            body.addFormDataPart("shopId", shopInfo[9].toString()).addFormDataPart("userId", userId);
                            new OkHttpClient().newCall(new Request.Builder().url(getString(R.string.ip) + getString(R.string.shopVip)).post(body.build()).build()).enqueue(new Callback() {
                                @Override
                                public void onFailure(Call call, IOException e) {
                                    e.printStackTrace();
                                    getTypeView().hintHoast(R.string.getDataDefeated, null, Toast.LENGTH_SHORT);
                                }

                                @Override
                                public void onResponse(Call call, Response response) throws IOException {
                                    try {
                                        JSONObject result = new JSONObject(response.body().string());
                                        Log.i("ShopActivity", "getClickListener--->" + result);
                                        if (200 == result.getInt("statusCode"))
                                            ((ShopV) getTypeView()).setBecomeMember();
                                        else
                                            getTypeView().hintHoast(BaseView.X, result.getString("message"), Toast.LENGTH_SHORT);
                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                        getTypeView().hintHoast(R.string.submitDataDefeated, null, Toast.LENGTH_SHORT);
                                    }
                                }
                            });
                        }else
                            startActivityForResult(new Intent(ShopActivity.this,LoginActivity.class),MainActivity.RETURN_USER);
                        break;
                    case R.id.image_lineProject:
                        Intent  lineProjectIntent = new Intent(ShopActivity.this,IntelligentBroadcastActivity.class);
                        lineProjectIntent.putExtra(getString(R.string.city),getIntent().getStringExtra(getString(R.string.city)));
                        lineProjectIntent.putExtra(getString(R.string.send),new double[]{getIntent().getDoubleExtra(getString(R.string.longitude),0.0),getIntent().getDoubleExtra(getString(R.string.latitude),0.0),
                                Double.valueOf(shopInfo[7].toString()),Double.valueOf(shopInfo[8].toString())});
                        startActivity(lineProjectIntent);
                        break;
                    case R.id.text_support:
                        Intent intent = new Intent(ShopActivity.this,MerchantParticularsActivity.class);
                        HashMap<List<CharSequence[]>,List<CharSequence[]>> hashMap = new HashMap<>();
                        hashMap.put(shopInfos.getEvaluate(),shopInfos.getActivitys());
                        Bundle particulars = new Bundle();
                        particulars.putCharSequenceArray(getString(R.string.send),shopInfos.getInfo());
                        particulars.putSerializable(getString(R.string.sendBody),hashMap);
                        intent.putExtras(particulars);
                        startActivity(intent);
                        break;
                }
            }
        };
    }
    public CharSequence[] getShopInfo(){return shopInfo;}
    public CharSequence[] getShopInfos(){
        return shopInfos.getInfo();
    }
    public HashMap<List<CharSequence[]>, List<CharSequence[]>> getHashMap() {
        HashMap<List<CharSequence[]>, List<CharSequence[]>> hashMap = new HashMap<>();
        hashMap.put(shopInfos.getEvaluate(),shopInfos.getActivitys());
        return hashMap;
    }
    public HashMap<String,List<CharSequence[]>> getFreeShipping(){
        HashMap<String,List<CharSequence[]>>hashMap = new HashMap<>();
        hashMap.put(getString(R.string.address),shopInfos.getFreeShipping());
        return hashMap;
    }
}
