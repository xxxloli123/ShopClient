package com.zsh.shopclient.model;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.IdentityHashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2017/12/19.
 */

public class Order extends Struct {
    //private Map<CharSequence[],List<CharSequence[]>>map;
    private List<KeyValue<CharSequence[],List<CharSequence[]>>>list;

    public Order() {
        //map = new IdentityHashMap<>();
        list = new ArrayList<>();
    }

    public void toList(JSONArray jsonArray, boolean isReplace)throws JSONException{
        if(isReplace)
            list.clear();
        for(int index = 0;index < jsonArray.length();index++){
            JSONObject jsonObject = jsonArray.getJSONObject(index);
            List<CharSequence[]> list = new ArrayList();
            JSONArray goods = jsonObject.getJSONArray("goods");
            for(int i = 0;i < goods.length();i++) {
                JSONObject jsonObj = goods.getJSONObject(i);
                list.add(new CharSequence[]{jsonObj.getString("smallImg"),jsonObj.getString("goodsName"), jsonObj.getString("goodsPrice"),jsonObj.getString("goodsnum"),jsonObj.getString("id")});
            }
            this.list.add(new KeyValue<>(new CharSequence[]{jsonObject.getString("shopImage"), jsonObject.getString("shopName"),
                    jsonObject.getString("shopTelePhone"),jsonObject.getString("startPro"),jsonObject.getString("startCity"),
                    jsonObject.getString("startDistrict"),jsonObject.getString("startHouseNumber"),jsonObject.getString("packingprice"),
                    jsonObject.getString("lineOrder_value"), jsonObject.getString("delivery"),jsonObject.getString("deliveryFee"),
                    jsonObject.getString("comment"),jsonObject.getString("shopId"), jsonObject.getString("shopUserDistance"),
                    jsonObject.getString("receiverName"), jsonObject.getString("receiverPhone"), jsonObject.getString("endPro"),
                    jsonObject.getString("endCity"), jsonObject.getString("endDistrict"), jsonObject.getString("endStreet"),
                    jsonObject.getString("endHouseNumber"), jsonObject.getString("userActualFee"),jsonObject.getString("postmanName"),
                    jsonObject.getString("postmanPhone"),jsonObject.getString("postmanId"), jsonObject.getString("platformDelivery"),
                    jsonObject.getString("createDate"), jsonObject.getString("orderNumber"), jsonObject.getString("status_value"),
                    jsonObject.getString("payType_value"), jsonObject.getString("id")},list));
        }
    }
    /*public Map getMap(){29
        return map;
    }*/
    public List getList(String filtration,String filtrate){
        List list = new ArrayList<>();
        for (KeyValue keyValue : this.list)
            if (("".equals(filtration)|| filtration.equals(((CharSequence[]) keyValue.getKey())[28]))&& filtrate.equals(((CharSequence[]) keyValue.getKey())[8]))
                list.add(keyValue);
        return list;
    }
    public void setListStatus(int position,String key){
        list.get(position).getKey()[27] = key;
    }
}
