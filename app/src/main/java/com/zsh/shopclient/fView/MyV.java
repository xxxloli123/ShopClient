package com.zsh.shopclient.fView;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.view.View;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.slowlife.map.util.ToastUtil;
import com.xxx.skynet.sharedPreference.IOSharedPreferences;
import com.xxx.skynet.sqlSave.TypeSqliteHelper;
import com.zsh.shopclient.R;
import com.zsh.shopclient.aPresenter.ComplainActivity;
import com.zsh.shopclient.aPresenter.DiscountCouponActivity;
import com.zsh.shopclient.aPresenter.HelpActivity;
import com.zsh.shopclient.aPresenter.LoginActivity;
import com.zsh.shopclient.aPresenter.MainActivity;
import com.zsh.shopclient.aPresenter.ReceiptAddressActivity;
import com.zsh.shopclient.aPresenter.RecommentdAwardActivity;
import com.zsh.shopclient.aPresenter.SbShopActivity;
import com.zsh.shopclient.aPresenter.SetActivity;
import com.zsh.shopclient.aPresenter.VIPActivity;
import com.zsh.shopclient.aView.BaseView;
import com.zsh.shopclient.http.OkHttp;
import com.zsh.shopclient.http.OkHttpCallback;
import com.zsh.shopclient.interfaceconfig.Config;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.OnClick;
import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by xxxloli on 2017/11/21.
 */

public class MyV extends BaseView implements OkHttpCallback.Impl {
    public static CircleImageView headImage;
    @BindView(R.id.name_text)
    TextView nameText;
    @BindView(R.id.balance_text)
    TextView balanceText;
    @BindView(R.id.discount_coupon_tv)
    TextView discountCouponTv;
    @BindView(R.id.sb_tv)
    TextView sbTv;
    private MainActivity activity;

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_my;
    }

    @Override
    public void initView() {
        headImage = getRootView().findViewById(R.id.head_image);
        activity = (MainActivity) getRootView().getContext();
        headImage.setOnClickListener(activity.getClickListener());
        if (IOSharedPreferences.inString(activity, activity.getString(R.string.user),
                activity.getString(R.string.userId))!=null)initData();
    }

    public void initData() {
        List<List<String>> lists1 = new TypeSqliteHelper(activity,activity.
                getString(R.string.sqlLibrary),null,1,null).query("user",new String[]{"name","phone","headerImg","id"},null);
        Map<String, Object> map = new HashMap<>();
        map.put("userId", IOSharedPreferences.inString(activity,
                activity.getString(R.string.user), activity.getString(R.string.userId)));
        OkHttp.Call(Config.Url.getUrl(Config.GET_UserInfo), map, this);
        Glide.with(activity).load(Config.Url.getUrl(Config.IMG_Hear) +
                lists1.get(0).get(2)).into(headImage);
        String userName = "null".equals(lists1.get(0).get(1))? activity.getString(R.string.zshUser): lists1.get(0).get(1);
        nameText.setText(userName);
        headImage.setOnClickListener(activity.getClickListener());
    }

    @OnClick({R.id.set_rl, R.id.name_text, R.id.balance_ll, R.id.discount_coupon_ll, R.id.shop_rl,
            R.id.sb_ll, R.id.address_rl, R.id.vip_rl, R.id.recommended_rl, R.id.service_center_rl, R.id.join_in_rl})
    public void onViewClicked(View view) {
        Intent intent = null;
        switch (view.getId()) {
            case R.id.service_center_rl:
                intent = new Intent(getContext(), HelpActivity.class);
                intent.putExtra(HelpActivity.URI, "slowlife/app/invoking/userHelpInfo.html");
                activity.startActivity(intent);
                break;
            case R.id.join_in_rl:
                //用intent启动拨打电话
                intent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:72721515"));
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                activity.startActivity(intent);
                break;
        };

        switch (view.getId()) {
            case R.id.shop_rl:
                if (isLogin())activity.startActivity(new Intent(activity, LoginActivity.class));
                else activity.startActivity(new Intent(activity, SbShopActivity.class));
                break;
            case R.id.set_rl:
                if (isLogin())activity.startActivity(new Intent(activity, LoginActivity.class));
                else activity.startActivity(new Intent(activity, SetActivity.class));
                break;
            case R.id.name_text:
                if (isLogin())activity.startActivity(new Intent(activity, LoginActivity.class));
                break;
            case R.id.balance_ll:
                break;
            case R.id.discount_coupon_ll:
                if (isLogin())activity.startActivity(new Intent(activity, LoginActivity.class));
                else activity.startActivity(new Intent(activity, DiscountCouponActivity.class));
                break;
            case R.id.sb_ll:
                if (isLogin())activity.startActivity(new Intent(activity, LoginActivity.class));
                else activity.startActivity(new Intent(activity, SbShopActivity.class));
                break;
            case R.id.address_rl:
                if (isLogin())activity.startActivity(new Intent(activity, LoginActivity.class));
                else activity.startActivity(new Intent(activity, ReceiptAddressActivity.class));
                break;
            case R.id.vip_rl:
                if (isLogin())activity.startActivity(new Intent(activity, LoginActivity.class));
                else activity.startActivity(new Intent(activity, VIPActivity.class));
                break;
            case R.id.recommended_rl:
                if (isLogin())activity.startActivity(new Intent(activity, LoginActivity.class));
                else activity.startActivity(new Intent(activity, RecommentdAwardActivity.class));
                break;

        }
    }

    private boolean isLogin() {
        return  (IOSharedPreferences.inString(activity, activity.getString(R.string.user),
                activity.getString(R.string.userId))==null);
    }

    @Override
    public void onSuccess(Object tag, JSONObject json) throws JSONException {
        balanceText.setText(json.getString("balance"));
        discountCouponTv.setText(json.getString("CouponNumber"));
        sbTv.setText(json.getString("integral"));
    }

    @Override
    public void fail(Object tag, int code, JSONObject json) throws JSONException {
        ToastUtil.show(activity,"网络连接错误");
    }

    @NonNull
    @Override
    public Context getContext() {
        return activity;
    }

    @OnClick(R.id.feedback_rl)
    public void onViewClicked() {
        activity.startActivity(new Intent(activity, ComplainActivity.class));
    }
}
