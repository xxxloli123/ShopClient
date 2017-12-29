package com.zsh.shopclient.aView;

import android.graphics.Paint;
import android.graphics.drawable.Drawable;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.transition.Transition;
import com.zsh.shopclient.R;
import com.zsh.shopclient.aPresenter.CommodityDetailsActivity;
import com.zsh.shopclient.widget.RecyclerImage;

import java.util.List;

import butterknife.BindView;

/**
 * Created by Administrator on 2017/12/15.
 */

public class CommodityDetailsV extends BaseView {
    @BindView(R.id.image_goBack)ImageView goBack;
    @BindView(R.id.text_title)TextView title;
    @BindView(R.id.recyclerImage)RecyclerImage recyclerImage;
    @BindView(R.id.text_name)TextView name;
    @BindView(R.id.text_currentPrice)TextView currentPrice;
    @BindView(R.id.text_originalCost)TextView originalCost;
    @BindView(R.id.image_particulars)ImageView particularsImage;
    @BindView(R.id.text_snapUp)TextView snapUp;
    @Override
    protected int getLayoutId() {
        return R.layout.activity_commedity_details;
    }

    @Override
    public void initView() {
        CommodityDetailsActivity activity = (CommodityDetailsActivity) getRootView().getContext();
        CharSequence[] charSequences = activity.getIntent().getCharSequenceArrayExtra(activity.getString(R.string.title));
        View.OnClickListener listener = activity.getClickListener();
        title.setText(charSequences[1]);
        title.setTextColor(0x00000000);
        RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams)goBack.getLayoutParams();
        params.height = params.width = (int)activity.getResources().getDimension(R.dimen.margin40);
        params.setMargins((int)activity.getResources().getDimension(R.dimen.margin8),0,0,0);
        goBack.setLayoutParams(params);
        ((View)goBack.getParent()).setBackgroundColor(0x00ffffff);
        int padding = (int)activity.getResources().getDimension(R.dimen.margin8);
        goBack.setPadding(padding,padding,padding,padding);
        Drawable goBackDrawable = ContextCompat.getDrawable(activity,R.drawable.shape_annulus_gray);
        goBackDrawable.setAlpha(0xaf);
        goBack.setBackground(goBackDrawable);
        goBack.setOnClickListener(listener);
        name.setText(title.getText());
        currentPrice.setText(activity.getString(R.string.sign)+Math.round(Float.valueOf((String) charSequences[2])*100)/100.00f);
        originalCost.setText(activity.getString(R.string.sign)+charSequences[5]);
        originalCost.getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG);
        snapUp.setOnClickListener(listener);
    }
    public void setTitleBar(){
        int y = ((ScrollView)recyclerImage.getParent().getParent()).getScrollY();
        if(y <0xff) {
            ((RelativeLayout) title.getParent()).setBackgroundColor(y <<24| ContextCompat.getColor(getRootView().getContext(), R.color.lucencyScarlet));
            if(y < 0xaf)
                goBack.getBackground().setAlpha(0xaf -y);
            title.setTextColor(y <<24| 0x00ffffff);
        }
    }
    public void initImage(final List<String> list, final String particulars){
        ((CommodityDetailsActivity)getRootView().getContext()).runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if(null == recyclerImage) return;
                final String url = getRootView().getContext().getString(R.string.ip) + getRootView().getContext().getString(R.string.pictureLibraryPath);
                for(String path : list)
                    recyclerImage.addImageUrl(url + path);
                recyclerImage.commit();
                Glide.with(getRootView().getContext()).load(url + particulars).apply(new RequestOptions().centerCrop().placeholder(R.mipmap.ic_launcher)).into(new SimpleTarget<Drawable>() {
                    @Override
                    public void onResourceReady(Drawable resource, Transition<? super Drawable> transition) {
                        ViewGroup.LayoutParams params = particularsImage.getLayoutParams();
                        Log.i("CommodityDetailsV","initImage-width->"+resource.getIntrinsicWidth()+"-"+resource.getIntrinsicHeight());
                        int width = particularsImage.getWidth();
                        float scale = (float) width / (float) resource.getIntrinsicWidth();
                        int height = Math.round(resource.getIntrinsicHeight() * scale);
                        params.height = height;
                        particularsImage.setLayoutParams(params);
                        particularsImage.setImageDrawable(resource);
                    }
                });
            }
        });
    }
    public void updateRests(final CharSequence[]array){

    }
}
