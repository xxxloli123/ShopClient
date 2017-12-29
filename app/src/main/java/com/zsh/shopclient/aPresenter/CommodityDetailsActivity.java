package com.zsh.shopclient.aPresenter;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Toast;

import com.xxx.skynet.sharedPreference.IOSharedPreferences;
import com.zsh.shopclient.R;
import com.zsh.shopclient.aView.BaseView;
import com.zsh.shopclient.aView.CommodityDetailsV;

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
 * Created by Administrator on 2017/12/15.
 * 511322199311209642身份证号码信息
 身份证地区：中国 > 四川省 > 南充市 > 营山县
 地区行政区号： 511322   511322身份证
 出身年月日：1993年11月20日
 姓名：     性别：女     年龄：24周岁
 18位完整号码： 511322199311209642
 15位完整号码： 5113229331120964
 511322199311209642身份信息解读：1-6位区位代码：511322；7-14位出生年月日：19931120; 15-17位顺序码：964；第18位效验码：2;
 511322199311209642身份号码真实信息核查比对服务
 */

public class CommodityDetailsActivity extends BaseActivity {
    private CharSequence[] shopInfo;
    private void linkNet(final String url,final String orderId,final String send,final String attach,final String sendTail){
        new OkHttpClient().newCall(new Request.Builder().url(url).post(new MultipartBody.Builder().setType(MultipartBody.FORM).addFormDataPart("orderId",orderId).build()).build()).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {}

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                try {
                    JSONObject result = new JSONObject(response.body().string());
                    Log.i("CommodityDetailsActivity","onViewCreated--->"+result);
                    if(200 == result.getInt("statusCode")){
                        HashMap<CharSequence[],List<CharSequence[]>> hashMap = new HashMap<>();
                        hashMap.put(shopInfo,null);
                        Intent intent = new Intent(CommodityDetailsActivity.this,CloseAccountActivity.class);
                        Bundle bundle = new Bundle();
                        bundle.putSerializable(send,hashMap);
                        intent.putExtras(bundle);
                        intent.putExtra(attach,new CharSequence[]{result.getJSONObject("order").getJSONArray("goods").getJSONObject(0).getString("smallImg"),
                                        result.getJSONObject("order").getJSONArray("goods").getJSONObject(0).getString("goodsName"), "",
                                        result.getJSONObject("order").getJSONArray("goods").getJSONObject(0).getString("goodsPrice"), "",
                                result.getJSONObject("order").getJSONArray("goods").getJSONObject(0).getString("goodsnum"),
                                result.getJSONObject("order").getJSONArray("goods").getJSONObject(0).getString("goodsId"),
                                result.getJSONObject("order").getJSONArray("goods").getJSONObject(0).getString("id")});
                        intent.putExtra(sendTail,new HashMap<>());
                        startActivity(intent);
                        finish();
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
    protected BaseView createTypeView() {
        return new CommodityDetailsV();
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        MultipartBody.Builder body = new MultipartBody.Builder().setType(MultipartBody.FORM);
        body.addFormDataPart("bargainGoodsId",(String) getIntent().getCharSequenceArrayExtra(getString(R.string.title))[3]);
        new OkHttpClient().newCall(new Request.Builder().url(getString(R.string.ip)+getString(R.string.commodityParticulars)).post(body.build()).build()).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
                getTypeView().hintHoast(R.string.getDataDefeated,null, Toast.LENGTH_SHORT);
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                try {
                    JSONObject result = new JSONObject(response.body().string());
                    Log.i("CommodityDetailsActivity","onViewCreated--->"+result);
                    if(200 == result.getInt("statusCode")){
                        List<String> paths = new ArrayList<String>();
                        JSONObject jsonObject = result.getJSONObject("img");
                        paths.add(jsonObject.getString("oneBigImg"));
                        paths.add(jsonObject.getString("twoBigImg"));
                        paths.add(jsonObject.getString("threeBigImg"));
                        paths.add(jsonObject.getString("fourBigImg"));
                        paths.add(jsonObject.getString("fiveBigImg"));
                        paths.add(jsonObject.getString("sixBigImg"));
                        ((CommodityDetailsV)getTypeView()).initImage(paths,jsonObject.getString("detailsImg"));
                        JSONObject productJson = result.getJSONObject("bargaingoods");
                        CharSequence[] products = new String[]{productJson.getString("shopSmallImg"),productJson.getString("productSmallImg"), productJson.getString("productName"),
                                productJson.getString("bargainPrice"),productJson.getString("originalPrice"),  productJson.getString("details"), productJson.getString("id"),};
                        shopInfo = new CharSequence[]{"yes","no","shopImage",productJson.getString("shopName"),"0","0","0","0","0","phone","city","district", "houseNumber","shopNotices",productJson.getString("shopId")};
                        ((CommodityDetailsV)getTypeView()).updateRests(products);
                    }else
                        getTypeView().hintHoast(BaseView.X,result.getString("message"), Toast.LENGTH_SHORT);
                } catch (JSONException e) {
                    e.printStackTrace();
                    getTypeView().hintHoast(R.string.parseFailure,null, Toast.LENGTH_SHORT);
                }
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
                    case R.id.text_snapUp:
                        String userId = IOSharedPreferences.inString(CommodityDetailsActivity.this, getString(R.string.user), getString(R.string.userId));
                        if(null != userId) {
                            final String url = getString(R.string.ip) + getString(R.string.orderInfo);
                            final String send = getString(R.string.send);
                            final String attach = getString(R.string.attach);
                            final String sendTail = getString(R.string.sendTail);
                            MultipartBody.Builder body = new MultipartBody.Builder().setType(MultipartBody.FORM);
                            body.addFormDataPart("userId", userId).addFormDataPart("productId", (String) getIntent().getCharSequenceArrayExtra(getString(R.string.title))[3]);
                            new OkHttpClient().newCall(new Request.Builder().url(getString(R.string.ip) + getString(R.string.snapUpUrl)).post(body.build()).build()).enqueue(new Callback() {
                                @Override
                                public void onFailure(Call call, IOException e) {
                                    getTypeView().hintHoast(R.string.submitDataDefeated, null, Toast.LENGTH_SHORT);
                                }

                                @Override
                                public void onResponse(Call call, Response response) throws IOException {
                                    try {
                                        JSONObject result = new JSONObject(response.body().string());
                                        Log.i("CommodityDetailsActivity", "clickListener--->" + result);
                                        if (200 == result.getInt("statusCode"))
                                            linkNet(url, result.getString("orderId"), send, attach, sendTail);
                                        else
                                            getTypeView().hintHoast(BaseView.X, result.getString("message"), Toast.LENGTH_LONG);
                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                        getTypeView().hintHoast(R.string.submitDataDefeated, null, Toast.LENGTH_SHORT);
                                    }
                                }
                            });
                        }else
                            startActivityForResult(new Intent(CommodityDetailsActivity.this,LoginActivity.class),MainActivity.RETURN_USER);
                        break;
                }
            }
        };
    }
    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        switch (ev.getAction()){
            case MotionEvent.ACTION_DOWN:
                break;
            case MotionEvent.ACTION_UP:
                break;
            case MotionEvent.ACTION_MOVE:
                ((CommodityDetailsV)getTypeView()).setTitleBar();
                break;
        }
        return super.dispatchTouchEvent(ev);
    }
}
