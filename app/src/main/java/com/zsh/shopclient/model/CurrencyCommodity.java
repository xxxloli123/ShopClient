package com.zsh.shopclient.model;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2017/12/15.
 */

public class CurrencyCommodity extends Struct {
    private List<CharSequence[]>list;

    public CurrencyCommodity(String url,JSONArray jsonArray)throws JSONException {
        list = new ArrayList<>();
        for(int index = 0;index< jsonArray.length();index++){
            JSONObject jsonObject = jsonArray.getJSONObject(index);
            list.add(new CharSequence[]{url+jsonObject.getString("smallImg"),jsonObject.getString("productName"),jsonObject.getString("details"),
                    jsonObject.getString("singleIntegral"), jsonObject.getString("id"),});
        }
    }

    public List<CharSequence[]> getList() {
        return list;
    }
}
