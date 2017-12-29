package com.zsh.shopclient;

import android.support.v4.app.FragmentActivity;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2017/11/7.
 */

public class Test {
    public static List<String[]> getVip(FragmentActivity activity) {
        List<String[]> list = new ArrayList<>();
        String icon = "android.resource://" + activity.getPackageName() + "/" + R.mipmap.ic_launcher;
        list.add(new String[]{icon, "xxx"});
        list.add(new String[]{icon, "xxx"});
        list.add(new String[]{icon, "xxx"});
        list.add(new String[]{icon, "xxx"});
        list.add(new String[]{icon, "xxx"});
        list.add(new String[]{icon, "xxx"});
        list.add(new String[]{icon, "xxx"});
        list.add(new String[]{icon, "xxx"});
        list.add(new String[]{icon, "xxx"});
        list.add(new String[]{icon, "xxx"});
        list.add(new String[]{icon, "xxx"});
        list.add(new String[]{icon, "xxx"});
        list.add(new String[]{icon, "xxx"});
        list.add(new String[]{icon, "xxx"});
        list.add(new String[]{icon, "xxx"});
        list.add(new String[]{icon, "xxx"});
        return list;
    }
}
