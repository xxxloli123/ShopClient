package com.xxx.skynet.tool;

/**
 * Created by Administrator on 2017/11/1.
 */

public class ClassInfoHelper {
    public static String getClassShortName(final Class<?> type){
        String fullName = type.getName();
        String showName = fullName.substring(fullName.lastIndexOf(".")+1,fullName.length());
        return showName.replace("[A-Z]","$0").toLowerCase();
    }
}
