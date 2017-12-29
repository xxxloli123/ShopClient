package com.zsh.shopclient.aView;

import android.graphics.PorterDuff;
import android.graphics.drawable.LayerDrawable;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.zsh.shopclient.R;
import com.zsh.shopclient.aPresenter.AppraiseActivity;
import com.zsh.shopclient.adapter.CommentEditAdapter;
import com.zsh.shopclient.tool.RecyclerLimit;
import com.zsh.shopclient.widget.SlideSwitchView;

import java.util.List;
import java.util.Map;

import butterknife.BindView;

/**
 * Created by Administrator on 2017/12/20.
 */

public class AppraiseV extends BaseView {
    @BindView(R.id.ratingBar_shop)RatingBar ratingBarShop;
    @BindView(R.id.edit_shopComment)EditText shopComment;
    @BindView(R.id.ratingBar_shippingService)RatingBar shippingService;
    @BindView(R.id.edit_shippingComment)EditText shippingComment;
    @BindView(R.id.slide_switch_anonymity)SlideSwitchView anonymity;
    @BindView(R.id.slide_switch_collect)SlideSwitchView collect;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_appraise;
    }

    @Override
    public void initView() {
        View.OnClickListener listener = ((AppraiseActivity)getRootView().getContext()).getClickListener();
        getRootView().findViewById(R.id.image_goBack).setOnClickListener(listener);
        ((TextView)getRootView().findViewById(R.id.text_title)).setText(R.string.evaluate);
        Map<CharSequence[],List<CharSequence[]>> map = (Map<CharSequence[], List<CharSequence[]>>) ((AppraiseActivity)getRootView().getContext()).getIntent().getExtras().get(getRootView().getContext().getString(R.string.title));
        Glide.with(getRootView().getContext()).load(getRootView().getContext().getString(R.string.ip)+getRootView().getContext().getString(R.string.shopImagePath)+map.keySet().iterator().next()[2]).
                into((ImageView)getRootView().findViewById(R.id.image_head));
        ((TextView)getRootView().findViewById(R.id.text_shopName)).setText(map.keySet().iterator().next()[3]);
        ((LayerDrawable)ratingBarShop.getProgressDrawable()).getDrawable(2).setColorFilter(ContextCompat.getColor(getRootView().getContext(), R.color.scarlet), PorterDuff.Mode.SRC_ATOP);//改变星星颜色，兼容安卓各个版本
        RecyclerView recycler = getRootView().findViewById(R.id.recycler);
        recycler.setLayoutManager(new LinearLayoutManager(getRootView().getContext(),LinearLayoutManager.VERTICAL,false));
        recycler.addItemDecoration(new RecyclerLimit(getRootView().getContext(),(byte) LinearLayout.HORIZONTAL));
        recycler.setItemAnimator(new DefaultItemAnimator());
        recycler.setAdapter(new CommentEditAdapter((AppraiseActivity)getRootView().getContext(),((AppraiseActivity)getRootView().getContext()).getList()));
        collect.togglr();
        ((TextView)getRootView().findViewById(R.id.text_integral)).setText("");
        getRootView().findViewById(R.id.text_commit).setOnClickListener(listener);
    }
    public float getRatingBarShop(){
        return ratingBarShop.getRating();
    }
    public float getShippingServiceStar(){
        return shippingService.getRating();
    }
    public String getShopCommentInput(){
        return shopComment.getText().toString().trim();
    }
    public String getShippingComment(){
        return shippingComment.getText().toString().trim();
    }
    public String getAnonymity(){
        return anonymity.getStatus()? "yes" : "no";
    }
    public String getCollect(){
        return  collect.getStatus()? "yes" : "no";
    }
}
