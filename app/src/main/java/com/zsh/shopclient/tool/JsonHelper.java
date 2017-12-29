package com.zsh.shopclient.tool;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2017/11/2.
 */

public class JsonHelper {
    public static HashMap<String,Object>jsonObject2Map(JSONObject jsonObj){
        HashMap map = null;
        try {
            map = new HashMap();
            Iterator<String> iterators = jsonObj.keys();
            while (iterators.hasNext()){
                String name = iterators.next();
                map.put(name,jsonObj.getString(name));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return map;
    }
    public static List<Map<String,Object>> jsonArray2List(JSONArray jsonArray) throws JSONException{
        List<Map<String,Object>> list = new ArrayList<>();
        for(int index = 0;index < jsonArray.length();index++)
            list.add(jsonObject2Map(jsonArray.getJSONObject(index)));
        return list;
    }
}
