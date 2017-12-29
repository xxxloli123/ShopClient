package com.zsh.shopclient.model;

import java.util.ArrayList;

/**
 * Created by xxxloli on 2017/11/24.
 */

public class VipShop {
    private Shop shop;
    private ArrayList<Activites> activity;

    public Shop getShop() {
        return shop;
    }

    public void setShop(Shop shop) {
        this.shop = shop;
    }

    public ArrayList<Activites> getActivites() {
        return activity;
    }

    public void setActivites(ArrayList<Activites> activites) {
        this.activity = activites;
    }
}
