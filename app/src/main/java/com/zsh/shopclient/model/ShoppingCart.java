package com.zsh.shopclient.model;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.LinkedList;
import java.util.List;

/**
 * Created by Administrator on 2017/12/5.
 */

public class ShoppingCart extends Struct {
    private List<CharSequence[]> list;
    private float totalPrice;
    public ShoppingCart(){
        list = new LinkedList<>();
    }
    public void toList(JSONArray jsonArray)throws JSONException{
        totalPrice = 0.0f;
        for (int index = 0; index < jsonArray.length(); index++) {
            JSONObject jsonObj = jsonArray.getJSONObject(index);
            list.add(new CharSequence[]{jsonObj.getString("smallImg"), jsonObj.getString("goodsName"),jsonObj.getString("goodsPrice"),
                    jsonObj.getString("goodsId"),jsonObj.getString("goodsnum"),jsonObj.getString("id")});
        }
    }
    public int getTotal(){
        int total = 0;
        totalPrice = 0.0f;
        if(null != list)
            for(CharSequence[] array : list) {
                total += Integer.valueOf((String) array[4]);
                totalPrice += Float.valueOf((String)array[2])*Integer.valueOf((String) array[4]);
            }
        return total;
    }
    public int reduce(CharSequence commodityId){
        int count = 0,position = -1;
        totalPrice = 0.0f;
        for(int index = 0;index < list.size();index++) {
            int total = Integer.valueOf((String) list.get(index)[5]);
            if (commodityId.equals(list.get(index)[6])) {
                count = --total;
                list.get(index)[5] = "" + total;
                if (0 >= total)
                    position = index;
            }
            totalPrice += Float.valueOf((String) list.get(index)[3])* total;
        }
        if(-1 != position)
            list.remove(position);
        return count;
    }
    public float getTotalPrice() {
        return Math.round(totalPrice*100)/100.0f;
    }
    public void add(CharSequence[] array,int increment){
        int point = -1;
        for(int index = 0;index < list.size();index++)
            if(list.get(index)[3] == array[3]) {
                point = index;
                break;
            }
        if(-1 < point){
            list.get(point)[4] = Integer.valueOf((String) list.get(point)[4]) + increment + "";
            if ("0".equals(list.get(point)[4]))
                list.remove(point);
        }else{
            CharSequence[] cs = new CharSequence[array.length];
            for(int index = 0;index< array.length;index++)
                cs[index] = array[index];
            cs[4] = ""+increment;
            list.add(cs);
        }
    }
    public void clear(){
        list.clear();
    }
    public boolean isEmpty(){
        return null == list || list.size() ==0;
    }

    public List<CharSequence[]> getList() {
        return list;
    }
}
