package com.zsh.shopclient.aPresenter;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Toast;

import com.xxx.skynet.sharedPreference.IOSharedPreferences;
import com.zsh.shopclient.R;
import com.zsh.shopclient.aView.BaseView;
import com.zsh.shopclient.aView.CommodityParticularsV;
import com.zsh.shopclient.adapter.TextAdapter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by Administrator on 2017/11/18.
 */

public class CommodityParticularsActivity extends BaseActivity {
    private List<CharSequence[]> list;
    private CharSequence[] charSequences;
    private int total;
    private float price;
    private View.OnClickListener clickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            final String userId = IOSharedPreferences.inString(CommodityParticularsActivity.this, getString(R.string.user), getString(R.string.userId));
            switch (v.getId()){
                case R.id.image_goBack:
                    goBack();
                    break;
                case CommodityParticularsV.IDsHOPPINGcART://Android Activity任务和返回栈全面解析 http://blog.csdn.net/huaxun66/article/details/52841855
                    if (null != userId) {//怎样打开已经打开过的Activity http://blog.sina.com.cn/s/blog_8db55cf60101q7m7.html
                        Intent intent = new Intent(CommodityParticularsActivity.this, ShoppingCartActivity.class);
                        intent.putExtra(getString(R.string.requestParam), getIntent().getStringExtra(getString(R.string.requestParam)));
                        startActivity(intent);//startActivityForResult(intent, ShopActivity.SHOPPINGcART);
                    } else
                        startActivityForResult(new Intent(CommodityParticularsActivity.this,LoginActivity.class),MainActivity.RETURN_USER);
                    break;
                case R.id.text_select:
                    ((CommodityParticularsV)getTypeView()).displayPopupWindow();
                    break;
                case R.id.text_reveal:
                    ((CommodityParticularsV)getTypeView()).setReveal();
                    break;
                case R.id.text_addShoppingCart:
                    ((CommodityParticularsV)getTypeView()).displayPopupWindow();
                    break;
                case R.id.text_buyImmediately:
                    break;
                case R.id.image_reduce:
                    ((CommodityParticularsV)getTypeView()).setTotal(-1);
                    break;
                case R.id.image_add:
                    ((CommodityParticularsV)getTypeView()).setTotal(1);
                    break;
                case R.id.text_confirm:
                    if(null != userId) {
                        final int count = ((CommodityParticularsV) getTypeView()).getTotal();
                        final float sum = ((CommodityParticularsV) getTypeView()).getCurrentPrice();
                        MultipartBody.Builder body = new MultipartBody.Builder().setType(MultipartBody.FORM);
                        body.addFormDataPart("userId",userId ).addFormDataPart("productId", (String) getIntent().getCharSequenceArrayExtra(getString(R.string.title))[3]).
                                addFormDataPart("productcategory", null == charSequences[0] ? "" : (String) charSequences[0]).
                                addFormDataPart("standardName", null == charSequences[1] ? "" : (String) charSequences[1]).addFormDataPart("goodsnum", "" + count).
                                addFormDataPart("type", getIntent().getStringExtra(getString(R.string.requestParam)));
                        new OkHttpClient().newCall(new Request.Builder().url(getString(R.string.ip) + getString(R.string.addShoppingCartPath)).post(body.build()).build()).enqueue(new Callback() {
                            @Override
                            public void onFailure(Call call, IOException e) {
                                getTypeView().hintHoast(R.string.submitDataDefeated, null, Toast.LENGTH_SHORT);
                            }

                            @Override
                            public void onResponse(Call call, Response response) throws IOException {
                                try {
                                    JSONObject result = new JSONObject(response.body().string());
                                    Log.i("CommodityParticularsActivity", "clickListener--->" + result);
                                    if (200 == result.getInt("statusCode")) {
                                        getTypeView().hintHoast(R.string.addSucceed, null, Toast.LENGTH_SHORT);
                                        charSequences[0] = charSequences[1] = null;
                                        ((CommodityParticularsV) getTypeView()).dismiss();
                                        total = count;
                                        price = sum;
                                    } else
                                        getTypeView().hintHoast(BaseView.X, result.getString("message"), Toast.LENGTH_SHORT);
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                    getTypeView().hintHoast(R.string.submitDataDefeated, null, Toast.LENGTH_SHORT);
                                }
                            }
                        });
                    }else
                        startActivityForResult(new Intent(CommodityParticularsActivity.this,LoginActivity.class),MainActivity.RETURN_USER);
                    break;
            }
        }
    };
    private void goBack(){
        Intent resultIntent = getIntent();
        resultIntent.putExtra(getString(R.string.result),total);
        resultIntent.putExtra(getString(R.string.attach),price);
        setResult(RESULT_OK,resultIntent);
        finish();
    }
    @Override
    protected BaseView createTypeView() {
        return new CommodityParticularsV();
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        charSequences = new CharSequence[2];
        total = 0;
        price = 0.0f;
        MultipartBody.Builder body = new MultipartBody.Builder().setType(MultipartBody.FORM);
        body.addFormDataPart("productId",(String) getIntent().getCharSequenceArrayExtra(getString(R.string.title))[3]);
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
                    Log.i("CommodityParticularsActivity","onViewCreated--->"+result);
                    if(200 == result.getInt("statusCode")){
                        List<String> paths = new ArrayList<String>();
                        JSONObject jsonObject = result.getJSONObject("img");
                        paths.add(jsonObject.getString("oneBigImg"));
                        paths.add(jsonObject.getString("twoBigImg"));
                        paths.add(jsonObject.getString("threeBigImg"));
                        paths.add(jsonObject.getString("fourBigImg"));
                        paths.add(jsonObject.getString("fiveBigImg"));
                        paths.add(jsonObject.getString("sixBigImg"));
                        ((CommodityParticularsV)getTypeView()).initImage(paths,jsonObject.getString("detailsImg"));
                        JSONObject productJson = result.getJSONObject("product");
                        CharSequence[] products = new String[]{productJson.getString("shopsort"),productJson.getString("shopSmallImg"),productJson.getString("smallImg"),
                                productJson.getString("id"),productJson.getString("genericClassName"),productJson.getString("details"),productJson.getString("details"),
                                productJson.getString("sales"),productJson.getString("shopId"),productJson.getString("platformsort"),productJson.getString("shopClassId"),
                                productJson.getString("singlePrice"),productJson.getString("keywords"),productJson.getString("genericClassId"),productJson.getString("commentCount"),
                                productJson.getString("effect"),productJson.getString("trademark"),productJson.getString("shopName"),productJson.getString("modifyId"),
                                productJson.getString("shopClassName"),productJson.getString("shopkeeperName"),productJson.getString("shopkeeperId"),productJson.getString("productName")};
                        list = new ArrayList<CharSequence[]>();
                        Map<CharSequence,List<CharSequence>>[] arrayMap = new Map[2];
                        arrayMap[0] = new LinkedHashMap<CharSequence, List<CharSequence>>();
                        arrayMap[1] = new LinkedHashMap<CharSequence, List<CharSequence>>();
                        JSONArray jsonArray = result.getJSONArray("standard");
                        for(int index = 0;index < jsonArray.length();index++){
                            JSONObject jsonObj = jsonArray.getJSONObject(index);
                            String key = jsonObj.getString("productcategory");
                            String value = jsonObj.getString("standardName");
                            getValue(arrayMap[0],key,value);
                            getValue(arrayMap[1],value,key);
                            list.add(new String[]{key,value,jsonObj.getString("id"),jsonObj.getString("price")});
                        }
                        ((CommodityParticularsV)getTypeView()).setPrice(arrayMap,productJson.getString("sales"),0< list.size() ? priceRange():new float[]{Float.valueOf((String)products[11]),Float.valueOf((String) products[11])});
                    }else
                        getTypeView().hintHoast(BaseView.X,result.getString("message"), Toast.LENGTH_SHORT);
                } catch (JSONException e) {
                    e.printStackTrace();
                    getTypeView().hintHoast(R.string.parseFailure,null, Toast.LENGTH_SHORT);
                }
            }
        });
        new OkHttpClient().newCall(new Request.Builder().url(getString(R.string.ip)+getString(R.string.commodityComment)).post(body.build()).build()).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
                getTypeView().hintHoast(R.string.getDataDefeated,null,Toast.LENGTH_SHORT);
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                try {
                    JSONObject result = new JSONObject(response.body().string());
                    Log.i("CommodityParticularsActivity","onViewCreated-comment->"+result);
                    if(200 == result.getInt("statusCode")){
                        List<CharSequence[]> list = new ArrayList<CharSequence[]>();
                        JSONArray jsonArray = result.getJSONArray("list");
                        for(int index = 0;index < jsonArray.length();index++) {
                            JSONObject jsonObject = jsonArray.getJSONObject(index);
                            list.add(new CharSequence[]{jsonObject.getString("headerImg"),jsonObject.getString("userNickName"),jsonObject.getString("createDate"),
                                    jsonObject.getString("grade"), jsonObject.getString("comment")});
                        }
                        ((CommodityParticularsV)getTypeView()).initEvaluate(list);
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
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if(KeyEvent.KEYCODE_BACK == keyCode)
            goBack();
        return super.onKeyDown(keyCode, event);
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(null != data)
            switch (requestCode){
                case ShopActivity.SHOPPINGcART:
                    total = data.getIntExtra(getString(R.string.result),0);
                    price = data.getFloatExtra(getString(R.string.attach),0.0f);
                    getIntent().putExtra(getString(R.string.send),true);
                    break;
            }
    }

    private int getIndex(List<CharSequence> list, CharSequence charSequence){
        int position = -1;
        for(int index = 0;index < list.size();index++)
            if(list.get(index).equals(charSequence)) {
                position = index;
                break;
            }
            if(-1 == position) {
                list.add(charSequence);
                position = list.size()-1;
            }
        return position;
    }
    private void getValue(Map<CharSequence,List<CharSequence>> map,final CharSequence key,final CharSequence charSequence){
        List<CharSequence> value = null;
        if(null == map.get(key))
            value =  new ArrayList<CharSequence>();
        else
            value =  map.get(key);
        value.add(charSequence);
        map.put(key,value);
    }
    public float[] priceRange(){
        float price[] = new float[2];
        price[0] = price[1] = ((CommodityParticularsV) getTypeView()).getCurrentPrice();
        //originalCostMin = originalCostMax = Float.valueOf(list.get(0)[1]);
        for(int index = 1;index< list.size();index++) {
            float currentPrice = Float.valueOf((String) list.get(index)[3]);
            if (price[0] > currentPrice)
                price[0] = currentPrice;
            else if(price[1] < currentPrice)
                price[1] = currentPrice;
            /*float originalCostPrice = Float.valueOf(list.get(index)[1]);
            if (originalCostMin > originalCostPrice)
                originalCostMin = originalCostPrice;
            else if(originalCostMax < originalCostPrice)
                originalCostMax = currentPrice;*/
        }
        return price;
    }
    public float price(CharSequence type,CharSequence style){
        for(CharSequence[] charSequences : list)
            if(charSequences[0].equals(type) && charSequences[1].equals(style))
                return Math.round(Float.valueOf((String)charSequences[3])*100)/100.0f;
        return Math.round(Float.valueOf((String)list.get(0)[3])*100)/100.0f;
    }
    public TextAdapter.ItemOnClickListener getTypeItemClick(){
        return new TextAdapter.ItemOnClickListener() {
            @Override
            public void itemOnClick(View view) {
                charSequences[0] = ((CommodityParticularsV)getTypeView()).setStyleAdapterKey();
            }
        };
    }
    public TextAdapter.ItemOnClickListener getStyleItemClick(){
        return new TextAdapter.ItemOnClickListener() {
            @Override
            public void itemOnClick(View view) {
                charSequences[1] = ((CommodityParticularsV)getTypeView()).setTypeAdapterKey();
            }
        };
    }
    public View.OnClickListener getClickListener(){
        return clickListener;
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        switch (ev.getAction()){
            case MotionEvent.ACTION_DOWN:
                break;
            case MotionEvent.ACTION_UP:
                break;
            case MotionEvent.ACTION_MOVE:
                ((CommodityParticularsV)getTypeView()).setTitleBar();
                break;
        }
        return super.dispatchTouchEvent(ev);
    }
}
