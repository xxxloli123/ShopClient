package com.zsh.shopclient.model;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2017/12/18.
 */

public class Luck extends Struct {
    private List<CharSequence[]> list;

    public Luck(String imagePath,JSONArray jsonArray)throws JSONException{
        list = new ArrayList<>();
        for(int index = 0;index< jsonArray.length();index++){
            JSONObject jsonObject = jsonArray.getJSONObject(index);
            list.add(new CharSequence[]{imagePath + jsonObject.getString("productSmallImg"),jsonObject.getString("productName"),
                    jsonObject.getString("bargainPrice"), jsonObject.getString("id"),"0",jsonObject.getString("originalPrice")});
        }
    }
    public List<CharSequence[]> getList() {
        return list;
    }
}
