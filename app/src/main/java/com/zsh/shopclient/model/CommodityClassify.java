package com.zsh.shopclient.model;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2017/12/1.
 */

public class CommodityClassify extends Struct {
    private Map<CharSequence[],Map<CharSequence[],Map<CharSequence[],List<CharSequence[]>>>> map;
    private CharSequence[] classKey,libraryKey,kindKey;
    public CommodityClassify() {
        map = new LinkedHashMap<>();
    }
    public Iterator putClass(JSONArray jsonArray)throws JSONException{
        for(int index = 0;index < jsonArray.length();index++) {
            JSONObject jsonObject = jsonArray.getJSONObject(index);
            JSONArray array = jsonObject.getJSONArray("listclass");
            Map<CharSequence[],Map<CharSequence[],List<CharSequence[]>>>libraryMap = new LinkedHashMap<>();
            for (int i = 0; i < array.length(); i++) {
                JSONObject object = array.getJSONObject(i);
                JSONArray jsonArr = object.getJSONArray("listclass");
                Map<CharSequence[],List<CharSequence[]>> kindMap = new HashMap<>();
                for (int j = 0; j < jsonArr.length(); j++) {
                    JSONObject jsonObj = jsonArr.getJSONObject(j);
                    kindMap.put(new CharSequence[]{},null);
                }
                libraryMap.put(new CharSequence[]{object.getString("productClassName"), object.getString("id")}, kindMap);
            }
            map.put(new CharSequence[]{jsonObject.getString("productClassName"), jsonObject.getString("id")},libraryMap);
        }
        classKey = map.keySet().iterator().next();
        libraryKey = 0< map.get(classKey).keySet().size()? map.get(classKey).keySet().iterator().next() : null;
        return map.keySet().iterator();
    }
    public void putClass(CharSequence[] key,Map<CharSequence[],Map<CharSequence[],List<CharSequence[]>>>value){
        classKey = key;
        map.put(key,value);
    }
    public void putLibrary(final CharSequence[] classKey,CharSequence[] key,Map<CharSequence[],List<CharSequence[]>>value){
        libraryKey = key;
        map.get(classKey).put(key,value);
    }
    public Iterator putKind(JSONArray jsonArray)throws JSONException{
        for (int index = 0; index < jsonArray.length(); index++) {
            JSONObject object = jsonArray.getJSONObject(index);
            List<CharSequence[]> arrayList = new ArrayList<CharSequence[]>();
            JSONArray array = object.getJSONArray("listStandard");
            for(int i = 0;i< array.length();i++){
                JSONObject jsonObj = array.getJSONObject(i);
                arrayList.add(new CharSequence[]{jsonObj.getString("productcategory"),jsonObj.getString("standardName"),jsonObj.getString("id"),jsonObj.getString("price")});
            }
            map.get(classKey).get(libraryKey).put(new CharSequence[]{object.getString("smallImg"), object.getString("productName"),
                    object.getString("singlePrice"),object.getString("id"),"0"},arrayList);
        }
        kindKey = 0< map.get(classKey).get(libraryKey).keySet().size() ? map.get(classKey).get(libraryKey).keySet().iterator().next() :null;
        return map.get(classKey).get(libraryKey).keySet().iterator();
    }
    public List<CharSequence[]> getKindValue(){
        return null == libraryKey || null == kindKey ? null : map.get(classKey).get(libraryKey).get(kindKey);
    }
    public Map<CharSequence[],List<CharSequence[]>> getLibraryValue(){
        return map.get(classKey).get(libraryKey);
    }
    public List<CharSequence[]> toList(Iterator iterator){
        if(null == iterator)
            return null;
        List<CharSequence[]> list = new LinkedList<>();
        while(iterator.hasNext())
            list.add((CharSequence[]) iterator.next());
        return list;
    }
    public Iterator getClassKeys(){
        return map.keySet().iterator();
    }
    public Iterator getLibraryKeys(){
        return map.get(classKey).keySet().iterator();
    }
    public Iterator getKindKeys(){
        return null == libraryKey ? null : map.get(classKey).get(libraryKey).keySet().iterator();
    }

    public CharSequence[] getClassKey() {
        return classKey;
    }

    public CharSequence[] getLibraryKey() {
        return libraryKey;
    }

    public CharSequence[] getKindKey() {
        return kindKey;
    }

    public void setClassKey(CharSequence[] classKey) {
        this.classKey = classKey;
        libraryKey = 0< map.get(classKey).keySet().size()? map.get(classKey).keySet().iterator().next(): null;
    }

    public void setLibraryKey(CharSequence[] libraryKey) {
        this.libraryKey = libraryKey;
    }

    public void setKindKey(CharSequence[] kindKey) {
        this.kindKey = kindKey;
    }
}
