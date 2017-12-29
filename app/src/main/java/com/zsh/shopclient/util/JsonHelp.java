package com.zsh.shopclient.util;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import com.google.gson.JsonSyntaxException;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2017/5/2 0002.
 * Gson在java对象和json字符串之间转换 http://blog.csdn.net/jq_ak47/article/details/52685298
 * Gson 生成和解析带Date类型的json http://blog.csdn.net/onlyonecoder/article/details/16907857
 */

public class JsonHelp {
    /**
     * javabean to json
     *
     * @param obj
     * @return
     */
    public static String toJson(Object obj) {
        Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd HH:mm:ss").create();//避免对象中有时间影响转换
        String json = gson.toJson(obj);
        return json;
    }
    /**
     * list to json
     *
     * @param list
     * @return
     */
    public static String listToJson(List<?> list) {
       Gson gson = new Gson();
        String json = gson.toJson(list);
        return json;
    }

    /**
     * map to json
     *
     * @param map
     * @return
     */
    public static String mapToJson(Map<String, Object> map) {

        Gson gson = new Gson();
        String json = gson.toJson(map);
        return json;
    }

    public static <Type>Type fromJson(String json, Class<Type> type){
        try{
        Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd HH:mm:ss").create();//避免对象中有时间影响转换
        return gson.fromJson(json,type);
        }catch (JsonSyntaxException e){
            e.printStackTrace();
        }
        return null;
    }
    /**
     * json字符串转List集合
     *  new TypeToken<List<Type>>() {}.getType()这句Type使用实体类
     *  报错com.google.gson.internal.LinkedTreeMap cannot be cast to xxx 解决 http://blog.csdn.net/anddroid_lanyan/article/details/46561341
     */
    public static <Type>List<Type> jsonToList(String json,Class<Type> type) {
        Gson gson =  new GsonBuilder().setDateFormat("yyyy-MM-dd HH:mm:ss").create();//避免对象中有时间影响转换
        List<Type> list = gson.fromJson(json, new TypeToken<List<Type>>() {}.getType());//对于不是类的情况，用这个参数给出
        return list;
    }
    public static <Type>List<Type> jsonToArray(String json,Class<Type> type){
        ArrayList<Type> list = new ArrayList<Type>();
        JsonArray array = new JsonParser().parse(json).getAsJsonArray();
        for(final JsonElement elem : array)
            list.add(fromJson(elem.toString(), type));
        return list;
    }
    private static <Type> Map<String, Type> jsonToMap(String json) {
        // TODO Auto-generated method stub
        Gson gson = new Gson();
        Map<String, Type> maps = gson.fromJson(json, new TypeToken<Map<String, Type>>() {}.getType());
        return maps;
    }
}
