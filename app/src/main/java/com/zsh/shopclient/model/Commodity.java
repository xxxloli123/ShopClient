package com.zsh.shopclient.model;

import org.json.JSONArray;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2017/12/3.
 */

public class Commodity extends Struct {
    private List<CharSequence[]> list;

    public Commodity(JSONArray jsonArray) {
        list = new ArrayList<>();
    }
}
