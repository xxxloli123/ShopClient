package com.zsh.shopclient.aView;

import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.zsh.shopclient.R;
import com.zsh.shopclient.aPresenter.MerchantParticularsActivity;
import com.zsh.shopclient.adapter.LeftImageRightTextAdapter;
import com.zsh.shopclient.tool.RecyclerLimit;

/**
 * Created by Administrator on 2017/12/5.
 */

public class MerchantParticularsV extends BaseView {
    @Override
    protected int getLayoutId() {
        return R.layout.activity_merchant_particulars;
    }

    @Override
    public void initView() {
        MerchantParticularsActivity activity = (MerchantParticularsActivity) getRootView().getContext();
        View.OnClickListener listener = activity.getClickListener();
        getRootView().findViewById(R.id.image_goBack).setOnClickListener(listener);
        ((TextView)getRootView().findViewById(R.id.text_title)).setText(R.string.MerchantParticulars);
        Glide.with(activity).load(activity.getString(R.string.ip)+activity.getString(R.string.shopImagePath)+activity.getInfo(0)).into(((ImageView)getRootView().findViewById(R.id.image_photo)));
        ((TextView)getRootView().findViewById(R.id.text_shopName)).setText(activity.getInfo(1));
        float distance = Float.valueOf((String) activity.getInfo(15));
        ((TextView)getRootView().findViewById(R.id.text_distance)).setText(activity.getString(R.string.distanceMerchant)+": "+ (distance >1000.0f?Math.round(distance/10)/100.0f:distance)+"km");
        ((RatingBar)getRootView().findViewById(R.id.rating)).setRating(Float.valueOf((String) activity.getInfo(12)));
        ((TextView)getRootView().findViewById(R.id.text_monthSalesVolume)).setText(activity.getString(R.string.monthSalesVolume)+ activity.getInfo(13));
        ((TextView)getRootView().findViewById(R.id.text_distributionDistance)).setText(activity.getString(R.string.merchantDistributionDistance)+": "+ 1000* Float.valueOf((String) activity.getInfo(9))+"m");
        ((TextView)getRootView().findViewById(R.id.text_price)).setText(activity.getString(R.string.price)+": "+activity.getString(R.string.sign)+ activity.getInfo(10));
        ((TextView)getRootView().findViewById(R.id.text_tel)).setText(activity.getString(R.string.phoneNumber)+": "+ activity.getInfo(2));
        ((TextView)getRootView().findViewById(R.id.text_site)).setText(activity.getString(R.string.site)+":"+activity.getInfo(3)+ activity.getInfo(4)+ activity.getInfo(5)+ activity.getInfo(6));
        ((TextView)getRootView().findViewById(R.id.text_announcement)).setText(activity.getString(R.string.announcement)+": "+ activity.getInfo(11));
        if(null != activity.getEvaluate(0)) {
            Glide.with(activity).load(activity.getString(R.string.ip) + activity.getString(R.string.userHead) + activity.getEvaluate(0)).into(((ImageView) getRootView().findViewById(R.id.image_head)));
            ((TextView) getRootView().findViewById(R.id.text_name)).setText(activity.getEvaluate(1));
            ((TextView) getRootView().findViewById(R.id.text_time)).setText(activity.getEvaluate(2));
            float star = null == activity.getEvaluate(3) ? 0.0f : Float.valueOf((String) activity.getEvaluate(3));
            ((RatingBar) getRootView().findViewById(R.id.rating_bar)).setRating(star);
            ((TextView) getRootView().findViewById(R.id.text_evaluate)).setText(activity.getEvaluate(4));
        }else {
            ((RelativeLayout) getRootView().findViewById(R.id.image_head).getParent()).setVisibility(View.GONE);
            getRootView().findViewById(R.id.text_reveal).setVisibility(View.GONE);
        }
        getRootView().findViewById(R.id.text_reveal).setOnClickListener(listener);
        RecyclerView recycler = getRootView().findViewById(R.id.recycler);
        recycler.setLayoutManager(new LinearLayoutManager(activity,LinearLayoutManager.VERTICAL,false));
        recycler.addItemDecoration(new RecyclerLimit(activity, (byte) LinearLayout.HORIZONTAL));
        recycler.setItemAnimator(new DefaultItemAnimator());
        LeftImageRightTextAdapter adapter = new LeftImageRightTextAdapter(activity,activity.getActivitys());
        adapter.setDisplayOne();
        recycler.setAdapter(adapter);
    }
}
