package com.xxx.skynet.sharedPreference;

import android.content.Context;
import android.content.SharedPreferences;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * Created by Administrator on 2017/4/28 0028.
 * 这玩意不支持多线程
 */

public class IOSharedPreferences {
    public static void outBool(Context context,String name,String key,boolean bool){
        SharedPreferences.Editor editor = context.getSharedPreferences(name, Context.MODE_PRIVATE).edit();
        editor.putBoolean(key, bool);
        editor.commit();
    }
    public static boolean inBool(Context context,String name,String key){
        SharedPreferences preferences = context.getSharedPreferences(name, Context.MODE_PRIVATE);
        return preferences.getBoolean(key, false);
    }
    public static void outString(Context context,String name,String key,String value){
        SharedPreferences.Editor editor = context.getSharedPreferences(name,Context.MODE_PRIVATE).edit();
        editor.putString(key,value);
        editor.commit();
    }
    public static String inString(Context context,String name,String key){
        return context.getSharedPreferences(name,Context.MODE_PRIVATE).getString(key,null);
        //return "2c9ad8435e997fec015ea357ecf20221";
    }
    public static void outInt(Context context,String name,String key,int digit){
        SharedPreferences.Editor editor = context.getSharedPreferences(name,Context.MODE_PRIVATE).edit();
        editor.putInt(key,digit);
        editor.commit();
    }
    public static int inInt(Context context,String name,String key){
        return context.getSharedPreferences(name,Context.MODE_PRIVATE).getInt(key,0);
    }
    public static void outFloat(Context context,String name,String key,float dicimals){
        SharedPreferences.Editor editor = context.getSharedPreferences(name,Context.MODE_PRIVATE).edit();
        editor.putFloat(key,dicimals);
        editor.commit();
    }
    public static float inFloat(Context context,String name,String key){
        return context.getSharedPreferences(name,Context.MODE_PRIVATE).getFloat(key,0.0f);
    }
    public static void outLong(Context context,String name,String key,long digit){
        SharedPreferences.Editor editor = context.getSharedPreferences(name,Context.MODE_PRIVATE).edit();
        editor.putLong(key,digit);
        editor.commit();
    }
    public static long inLong(Context context,String name,String key){
        return context.getSharedPreferences(name,Context.MODE_PRIVATE).getLong(key,0);
    }
    //Android的SharedPreference中putStringSet存取数据 http://blog.csdn.net/wave_1102/article/details/44992899
    public static void outStringSet(Context context,String name,String key,Set<String> set){
        set.addAll(inStringSet(context,name,key));
        SharedPreferences.Editor editor = context.getSharedPreferences(name,Context.MODE_PRIVATE).edit();
        editor.putStringSet(key,set);
        editor.commit();
    }
    public static Set<String> inStringSet(Context context,String name,String key){
        return context.getSharedPreferences(name,Context.MODE_PRIVATE).getStringSet(key,null);
    }
    public static String[] toArray(Set<String>set){
        return null == set ? null : set.toArray(new String[set.size()]);
    }
    //清空以name为名的xml
    public static void remove(Context context, String name){
        context.getSharedPreferences(name, Context.MODE_PRIVATE).edit().clear().commit();
    }
}
