package com.zsh.shopclient.model;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by Administrator on 2017/12/6.
 */

public class CloseAccount extends Struct {
    private List<CharSequence[]> targets;
    private int position;
    public CloseAccount(JSONArray jsonArray)throws JSONException{
        targets = new LinkedList<>();
        for(int index = 0;index < jsonArray.length();index++){
            JSONObject jsonObject = jsonArray.getJSONObject(index);
            targets.add(new CharSequence[]{jsonObject.getString("personname"),jsonObject.getString("personphone"),jsonObject.getString("pro"),jsonObject.getString("city"),
                    jsonObject.getString("district"),jsonObject.getString("houseNumber"),jsonObject.getString("street"),jsonObject.getString("lng"),
                    jsonObject.getString("lat")});
            if("yes".equals(jsonObject.getString("defaultAddress")))
                position = index;
        }
    }
    public CharSequence[] mergetAddress(){
        StringBuilder sb[] = new StringBuilder[3];
        sb[0] = new StringBuilder(targets.get(position)[0]);
        sb[1] = new StringBuilder(targets.get(position)[1]);
        sb[2] = new StringBuilder();
        for(int i = 2;i<targets.get(position).length -2;i++)
            sb[2].append(targets.get(0)[i]);
        return sb;
    }
    public String toJsonString(CharSequence[] array){
        try {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("personname",array[0]).put("personphone",array[1]).put("pro",array[2]).put("city",array[3]).put("district",array[4]).
                    put("houseNumber",array[5]).put("street",array[6]).put("lng",array[7]).put("lat",array[8]);
            return jsonObject.toString();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }

    public int getPosition() {
        return position;
    }
    public CharSequence[] getDefaultTarget(){
        return targets.get(position);
    }
    public void setPosition(int position) {
        this.position = position;
    }
    public List<CharSequence[]> getTarget() {
        return targets;
    }
}
