package com.zsh.shopclient.model;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by Administrator on 2017/12/14.
 */

public class ShoppingTrolley extends Struct {
    private List<CharSequence[]> list;
    private float totalPrice;
    public ShoppingTrolley(JSONArray jsonArray)throws JSONException {
        list = new LinkedList<>();
        for (int index = 0; index < jsonArray.length(); index++) {
            JSONArray jsonArray1 = jsonArray.getJSONArray(index);
            list.add(new CharSequence[]{jsonArray1.getJSONObject(0).getString("shopImage"),jsonArray1.getJSONObject(0).getString("shopName"),
                    jsonArray1.getJSONObject(0).getString("lineConsume"),jsonArray1.getJSONObject(0).getString("delivery"), "0","0","0","0","0",
                    jsonArray1.getJSONObject(0).getString("shopId"),""});
            for (int i = 0; i < jsonArray1.length(); i++) {
                JSONObject jsonObject = jsonArray1.getJSONObject(i);
                list.add(new CharSequence[]{jsonObject.getString("smallImg"), jsonObject.getString("goodsName"), jsonObject.getString("goodsPrice"),
                        jsonObject.getString("goodsId"),jsonObject.getString("goodsnum"),jsonObject.getString("productcategory"),
                        jsonObject.getString("standardName"),jsonObject.getString("id")});
            }
        }
    }
    public void addCount(int position,int count){
        int total = Integer.valueOf((String) list.get(position)[4])+ count;
        if(0== total) {
            list.remove(position);
            if((position == list.size() ||11== list.get(position).length) && 11== list.get(position -1).length)
                list.remove(position -1);
        }else
            list.get(position)[4] = total + "";
    }
    public int getCount() {
        int count = 0;
        totalPrice = 0.0f;
        for (CharSequence[] array : list) {
            if(8== array.length) {
                totalPrice += Float.valueOf((String) array[2]) * Integer.valueOf((String) array[4]);
                count += Integer.valueOf((String) array[4]);
            }
        }
        return count;
    }
    public float getTotalPrice() {
        return totalPrice;
    }
    public List<CharSequence[]> getList() {
        return list;
    }
    public void romove(String shopId){//删除区间数据
        boolean isStart = false;
        for(int index = 0;index< list.size();) {
            if (11 == list.get(index).length)
                if (shopId.equals(list.get(index)[9]))
                    isStart = true;
                else if (isStart)
                    break;
            if(isStart)
                list.remove(index);
            else
                index++;
        }
    }
    public List<String> getShopIds(){
        List<String> positions = new ArrayList<>();
        for(int index = 0;index < list.size();index++)
            if(11 == list.get(index).length)
                positions.add((String)list.get(index)[9]);
        return positions;
    }
}
