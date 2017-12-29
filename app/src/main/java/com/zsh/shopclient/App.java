package com.zsh.shopclient;

import android.app.Application;
import android.app.Service;
import android.content.Context;
import android.support.multidex.MultiDex;

import com.zsh.shopclient.aPresenter.BaseActivity;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2017/10/29.
 */

public class App extends Application {
    private static App app;
    private List<BaseActivity> activitys;
    private List<Service> services;
    public static synchronized App getInstance(){
        return app;
    }

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        MultiDex.install(this);
    }

    @Override
    public void onCreate() {
        super.onCreate();
        app = this;
        activitys = new ArrayList<>();
        services = new ArrayList<>();
    }
    public BaseActivity isExistActivity(Class<?> cls){
        for(BaseActivity activity : activitys)
            if(activity.getClass() == cls)
                return activity;
        return null;
    }
    public void addActivity(BaseActivity activity){
        activitys.add(activity);
    }
    public void removeActivity(BaseActivity activity){
        if(null != activity) {
            activitys.remove(activity);
            activity.finish();
            activity = null;
        }
    }
    public BaseActivity getActivity(int index){
        return activitys.get(index);
    }
    public void addService(Service service){
        services.add(service);
    }
    public void removeService(Service service){
        if(null != service) {
            services.remove(service);
            service.stopSelf();
            service = null;
        }
    }
    public Service getService(int index){
        return services.get(index);
    }
    public void exit(){
        for(BaseActivity activity : activitys)
            if(null != activity) {
                activity.finish();
                activity = null;
            }
        for(Service service : services)
            if(null != service) {
                service.stopSelf();
                service = null;
            }
        activitys.clear();
        services.clear();
        System.exit(0);
        //android.os.Process.killProcess(android.os.Process.myPid());
    }
}
