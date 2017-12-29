package com.zsh.shopclient.model;

import com.amap.api.location.AMapLocation;
import com.amap.api.maps.AMapUtils;
import com.amap.api.maps.model.LatLng;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2017/12/5.
 */

public class MerchantParticulars extends Struct{
    private CharSequence[] array;
    private List<CharSequence[]> freeShipping;
    private List<CharSequence[]>evaluate,activitys;
    private List<CharSequence[]> resolve(String string){
        List freeShipping = new ArrayList<CharSequence[]>();
        int start = 0;
        while(start < string.length()) {
            int end = string.substring(start).indexOf(",");
            String district = string.substring(start,-1 != end ? start + end: string.length());
            if (!"".equals(district))
                freeShipping.add(new CharSequence[]{district});
            start += district.length()+1;
        }
        return freeShipping;
    }

    public MerchantParticulars(AMapLocation location,JSONObject jsonObject)throws JSONException{
        array = new CharSequence[]{jsonObject.getString("shopImage"), jsonObject.getString("shopName"),jsonObject.getString("telePhone"),
                jsonObject.getString("pro"),jsonObject.getString("city"), jsonObject.getString("district"),jsonObject.getString("houseNumber"),
                jsonObject.getString("lineConsume"),jsonObject.getString("delivery"),jsonObject.getString("distance"), jsonObject.getString("deliveryFee")
                ,jsonObject.getString("shopNotices"), jsonObject.getString("grade"),jsonObject.getString("ordercount"), jsonObject.getString("id"),
                null == location ? jsonObject.getString("shopUserDistance"): ""+AMapUtils.calculateLineDistance(new LatLng(location.getLatitude(),location.getLongitude()), new LatLng(jsonObject.getDouble("lat"),jsonObject.getDouble("lng"))),};
        freeShipping = resolve(jsonObject.getString("shopTransportArea"));
    }
    public void initEvaluate(JSONArray jsonArray)throws JSONException{
        evaluate = new ArrayList<>();
        for(int index = 0;index < jsonArray.length();index++){
            JSONObject jsonObject = jsonArray.getJSONObject(index);
            evaluate.add(new CharSequence[]{jsonObject.getString("headerImg"),jsonObject.getString("name"),jsonObject.getString("createDate"),jsonObject.getString("grade"),jsonObject.getString("comment")});
        }
    }
    public void initActivitys(JSONArray jsonArray)throws JSONException{
        activitys = new ArrayList<>();
        for(int index = 0;index < jsonArray.length();index++){
            JSONObject jsonObject = jsonArray.getJSONObject(index);
            activitys.add(new CharSequence[]{jsonObject.getString("activityName")});
        }
    }
    public CharSequence[] getInfo(){
        return array;
    }

    public List<CharSequence[]> getEvaluate() {
        return evaluate;
    }

    public List<CharSequence[]> getActivitys() {
        return activitys;
    }

    public List<CharSequence[]> getFreeShipping() {
        return freeShipping;
    }
}
