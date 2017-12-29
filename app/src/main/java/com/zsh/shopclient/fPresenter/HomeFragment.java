package com.zsh.shopclient.fPresenter;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.SearchView;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.amap.api.location.AMapLocation;
import com.amap.api.services.core.LatLonPoint;
import com.xxx.skynet.sharedPreference.IOSharedPreferences;
import com.zsh.shopclient.R;
import com.zsh.shopclient.aPresenter.EditAddressActivity;
import com.zsh.shopclient.aPresenter.LoginActivity;
import com.zsh.shopclient.aPresenter.LuckActivity;
import com.zsh.shopclient.aPresenter.MainActivity;
import com.zsh.shopclient.aPresenter.MerchantActivity;
import com.zsh.shopclient.aPresenter.SbShopActivity;
import com.zsh.shopclient.aPresenter.SpecialOfferActivity;
import com.zsh.shopclient.aView.BaseView;
import com.zsh.shopclient.fView.HomeV;
import com.zsh.shopclient.fView.ShotcutV;
import com.zsh.shopclient.tool.JsonHelper;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cn.com.inlee.merchant.zbar.CaptureActivity;
import cn.sharesdk.onekeyshare.OnekeyShare;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * Created by Administrator on 2017/11/5.
 */

public class HomeFragment extends BaseFragment {
    @Override
    protected BaseView createTypeView() {
        return new HomeV();
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        /*((HomeV)getTypeView()).getWeather().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivityForResult(new Intent(getActivity(),LoginActivity.class),RETURN_USER);
            }
        });*/
        new OkHttpClient().newCall(new Request.Builder().url(getString(R.string.ip)+ getString(R.string.getRecyclerImage)).
                post(RequestBody.create(MediaType.parse("application/json"), "1")).build()).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                try {
                    JSONObject result = new JSONObject(response.body().string());
                    Log.i("HomeFragment","onViewCreated-recyclerImage->"+result);
                    if(200 == result.getInt("statusCode")){
                        List<String[]> lists = new ArrayList<String[]>();
                        JSONArray jsonArray = result.getJSONArray("listadv");
                        for(int index = 0;index < jsonArray.length();index++) {
                            JSONObject jsonObj = jsonArray.getJSONObject(index);
                            lists.add(new String[]{jsonObj.getString("advImg"),jsonObj.getString("id")});
                        }
                        ((HomeV)getTypeView()).initRecyclerImage(lists);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }
    public void linkNet(AMapLocation location,String userId){
        final String shopUrl = getString(R.string.ip)+ getString(R.string.shopImagePath);
        final String  commodityUrl = getString(R.string.ip)+ getString(R.string.pictureLibraryPath);
        ((HomeV)getTypeView()).setLocation(location.getDistrict());
        MultipartBody.Builder requestVip = new MultipartBody.Builder().setType(MultipartBody.FORM);
        requestVip.addFormDataPart("userId",userId);
        new OkHttpClient().newCall(new Request.Builder().url(getString(R.string.ip)+ getString(R.string.vipShops)).method("POST",requestVip.build()).build()).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                try {
                    JSONObject result = new JSONObject(response.body().string());
                    Log.i("HomeFragment","linkNet-vip->"+result);
                    List<CharSequence[]> list = new ArrayList<CharSequence[]>();
                    if(200 == result.getInt("statusCode")) {
                        JSONArray jsonArray = result.getJSONObject("collectionsInfo").getJSONArray("aaData");
                        for(int index = 0;index < jsonArray.length();index++){
                            JSONObject jsonObj = jsonArray.getJSONObject(index);
                            list.add(new CharSequence[]{shopUrl +jsonObj.getString("typeSmallImg"),jsonObj.getString("typeName"),"no","no","0","0","0","0","0",
                                    jsonObj.getString("typeId"), "0"});
                        }
                    }
                    ((HomeV)getTypeView()).initVipMerchant((MainActivity) getActivity(), list);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
        MultipartBody.Builder requestFlashSale = new MultipartBody.Builder().setType(MultipartBody.FORM);
        requestFlashSale.addFormDataPart("userId",userId).addFormDataPart("pro",location.getProvince()).addFormDataPart("city",location.getCity()).addFormDataPart("district",location.getDistrict());
        new OkHttpClient().newCall(new Request.Builder().url(getString(R.string.ip)+ getString(R.string.flashSaleUrl)).post(requestFlashSale.build()).build()).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                try {
                    JSONObject result = new JSONObject(response.body().string());
                    Log.i("HomeFragment", "linkNet-flashSale->" + result);
                    List<CharSequence[]> list = new ArrayList<CharSequence[]>();
                    if (200 == result.getInt("statusCode")) {
                        JSONArray jsonArray = result.getJSONObject("BargainGoodsInfo").getJSONArray("aaData");
                        for (int index = 0; index < jsonArray.length(); index++) {
                            JSONObject jsonObj = jsonArray.getJSONObject(index);
                            list.add(new CharSequence[]{commodityUrl + jsonObj.getString("productSmallImg"),jsonObj.getString("productName"),
                                    jsonObj.getString("bargainPrice"),jsonObj.getString("id"),"0",jsonObj.getString("originalPrice")});
                        }
                    }
                    ((HomeV) getTypeView()).initFlashSale((MainActivity) getActivity(), list);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
        MultipartBody.Builder requestBody = new MultipartBody.Builder().setType(MultipartBody.FORM);
        requestBody.addFormDataPart("pro",location.getProvince()).addFormDataPart("city",location.getCity()).addFormDataPart("district",location.getDistrict())
        .addFormDataPart("lng",""+location.getLongitude()).addFormDataPart("lat",""+location.getLatitude());
        new OkHttpClient().newCall(new Request.Builder().url(getString(R.string.ip)+ getString(R.string.recommendShops)).post(requestBody.build()).build()).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                try {
                    JSONObject result = new JSONObject(response.body().string());
                    Log.i("HomeFragment","linkNet-RecommendMerchant->"+result);
                    if (200 == result.getInt("statusCode")) {
                        List<Map<String,Object>> listMap = JsonHelper.jsonArray2List(result.getJSONArray("list"));
                        List<Map<Map<String,Object>,List<Map<String,Object>>>> lists = new ArrayList<Map<Map<String, Object>, List<Map<String, Object>>>>();
                        for(Map<String,Object> map : listMap) {
                            Map<Map<String,Object>,List<Map<String,Object>>> maps = new HashMap<Map<String, Object>, List<Map<String, Object>>>();
                            maps.put(JsonHelper.jsonObject2Map(new JSONObject(map.get("shop").toString())), JsonHelper.jsonArray2List(new JSONArray(map.get("activity").toString())));
                            lists.add(maps);
                        }
                        ((HomeV) getTypeView()).initRecommendMerchant((MainActivity) getActivity(), lists);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }
    public SearchView.OnQueryTextListener getOnQueryTextViewListener(){//v7.widget.SearchView的使用 http://blog.csdn.net/jxxfzgy/article/details/46055857
        return new SearchView.OnQueryTextListener(){
            @Override
            public boolean onQueryTextSubmit(String query) {
                ((HomeV)getTypeView()).hindKeyboard();
                AMapLocation location = ((MainActivity)getActivity()).getLocation();
                if(null != location) {
                    MultipartBody.Builder requestBody = new MultipartBody.Builder().setType(MultipartBody.FORM);
                    requestBody.addFormDataPart("keyword", query).addFormDataPart("pro", location.getProvince()).addFormDataPart("city", location.getCity()).
                            addFormDataPart("district", location.getDistrict()).addFormDataPart("lng", ""+location.getLongitude()).
                            addFormDataPart("lat",""+location.getLatitude() ).addFormDataPart("startPage", "0").addFormDataPart("pageSize", "8");
                    new OkHttpClient().newCall(new Request.Builder().url(getString(R.string.ip) + getString(R.string.search)).post(requestBody.build()).build()).enqueue(new Callback() {
                        @Override
                        public void onFailure(Call call, IOException e) {
                            e.printStackTrace();
                            getTypeView().hintHoast(R.string.getDataDefeated,null,Toast.LENGTH_SHORT);
                        }

                        @Override
                        public void onResponse(Call call, Response response) throws IOException {
                            try {
                                JSONObject result = new JSONObject(response.body().string());
                                Log.i("HomeFragment", "getOnQueryTextViewListener-onQueryTextChange->" + result);
                                if (200 == result.getInt("statusCode")) {
                                    List<Map<String, Object>> listMap = JsonHelper.jsonArray2List(result.getJSONArray("applistshop"));
                                    List<Map<Map<String, Object>, List<Map<String, Object>>>> lists = new ArrayList<Map<Map<String, Object>, List<Map<String, Object>>>>();
                                    for (Map<String, Object> map : listMap) {
                                        Map<Map<String, Object>, List<Map<String, Object>>> maps = new HashMap<Map<String, Object>, List<Map<String, Object>>>();
                                        maps.put(JsonHelper.jsonObject2Map(new JSONObject(map.get("shop").toString())), JsonHelper.jsonArray2List(new JSONArray(map.get("activity").toString())));
                                        lists.add(maps);
                                    }
                                    if(0< lists.size())
                                        ((HomeV)getTypeView()).displayPopupWindow(lists);
                                    else
                                        getTypeView().hintHoast(R.string.noData,null,Toast.LENGTH_SHORT);
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    });
                }else
                    Toast.makeText(getActivity(),getString(R.string.getDataDefeated),Toast.LENGTH_SHORT).show();
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                if(0 < newText.length())
                    ;
                return false;
            }
        };
    }
    public View.OnClickListener getClickListener(){
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((HomeV)getTypeView()).clearFocus();
                final String userId = IOSharedPreferences.inString(getActivity(),getString(R.string.user),getString(R.string.userId));
                switch (v.getId()){
                    case R.id.text_location:
                        AMapLocation location = ((MainActivity)getActivity()).getLocation();
                        Intent intent = new Intent(getActivity(), EditAddressActivity.class);
                        intent.putExtra("latlonPoint", null == location ? null : new LatLonPoint(location.getLatitude(),location.getLongitude()));
                        startActivityForResult(intent, MainActivity.ADDRESS);
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
                        oks.show(getActivity());
                        break;
                    case R.id.text_scanQr:
                        if(null != userId) {
                            Intent scan = new Intent(getActivity(), CaptureActivity.class);
                            scan.putExtra("url", getString(R.string.ip));
                            scan.putExtra("parameter", "scanerId=用户id" + "&scannederId=扫描结果");
                            startActivityForResult(scan, MainActivity.SCAN_QR);
                        }else
                            startActivityForResult(new Intent(getActivity(),LoginActivity.class),MainActivity.RETURN_USER);
                        break;
                    case R.id.image_signIn:
                        if(null != userId)
                            new OkHttpClient().newCall(new Request.Builder().url(getString(R.string.ip)+getString(R.string.usersign)).
                                    post(new MultipartBody.Builder().setType(MultipartBody.FORM).addFormDataPart("userId",userId).build()).build()).enqueue(new Callback() {
                                @Override
                                public void onFailure(Call call, IOException e) {
                                    e.printStackTrace();
                                    getTypeView().hintHoast(R.string.getDataDefeated,null,Toast.LENGTH_SHORT);
                                }

                                @Override
                                public void onResponse(Call call, Response response) throws IOException {
                                    try {
                                        JSONObject result = new JSONObject(response.body().string());
                                        Log.i("HomeFragment", "getOnQueryTextViewListener-onQueryTextChange->" + result);
                                        if (200 == result.getInt("statusCode"))
                                            ((HomeV)getTypeView()).signIn(result.getInt("integralNumber"));
                                        getTypeView().hintHoast(BaseView.X,result.getString("message"),Toast.LENGTH_SHORT);
                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                        getTypeView().hintHoast(R.string.parseFailure,null,Toast.LENGTH_SHORT);
                                    }
                                }
                            });
                        else
                            startActivityForResult(new Intent(getActivity(),LoginActivity.class),MainActivity.RETURN_USER);
                        break;
                    case R.id.image_specialOffer:
                        if(null != ((MainActivity)getActivity()).getLocation()) {
                            Intent specialOffer = new Intent(getActivity(), SpecialOfferActivity.class);
                            specialOffer.putExtra(getString(R.string.paramValue), "Bargain");
                            specialOffer.putExtra(getString(R.string.location), ((MainActivity)getActivity()).getLocation());
                            startActivity(specialOffer);
                        }
                        break;
                    case R.id.image_currencyShop:
                        if(null != userId)
                            startActivity(new Intent(getActivity(), SbShopActivity.class));
                        else
                            startActivityForResult(new Intent(getActivity(),LoginActivity.class),MainActivity.RETURN_USER);
                        break;
                    case R.id.image_activity:
                        if(null != ((MainActivity)getActivity()).getLocation()) {
                            Intent intentActivity = new Intent(getActivity(),MerchantActivity.class);
                            intentActivity.putExtra(getString(R.string.location),((MainActivity)getActivity()).getLocation());
                            startActivity(intentActivity);
                        }
                        break;
                    case R.id.image_luck:
                        if(null != ((MainActivity)getActivity()).getLocation()) {
                            Intent luck = new Intent(getActivity(),LuckActivity.class);
                            luck.putExtra(getString(R.string.location),((MainActivity)getActivity()).getLocation());
                            startActivity(luck);
                        }
                        break;
                }
            }
        };
    }
}
