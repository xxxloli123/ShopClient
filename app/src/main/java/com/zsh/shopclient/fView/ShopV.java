package com.zsh.shopclient.fView;

import android.support.v4.content.ContextCompat;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.AbsoluteSizeSpan;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.zsh.shopclient.R;
import com.zsh.shopclient.aPresenter.ShopActivity;
import com.zsh.shopclient.aView.BaseView;
import com.zsh.shopclient.adapter.CommodityAdapter;
import com.zsh.shopclient.adapter.TxtAdapter;
import com.zsh.shopclient.fPresenter.ShopFragment;
import com.zsh.shopclient.tool.RecyclerLimit;

import java.util.List;

import butterknife.BindView;

/**
 * Created by Administrator on 2017/11/17.
 */

public class ShopV extends BaseView {
    @BindView(R.id.recycler_class)RecyclerView rclass;
    @BindView(R.id.recycler_library)RecyclerView library;
    @BindView(R.id.recycler_info)RecyclerView info;
    @BindView(R.id.text_noData)TextView noData;
    @BindView(R.id.frame_shoppingTrolley)FrameLayout shoppingTrolley;
    @BindView(R.id.text_pay)TextView pay;
    @BindView(R.id.text_closeAccount)TextView closeAccount;
    @Override
    protected int getLayoutId() {
        return R.layout.fragment_shop;
    }

    @Override
    public void initView() {
        ShopActivity activity = (ShopActivity) getRootView().getContext();
        ShopFragment fragment = (ShopFragment)((com.zsh.shopclient.aView.ShopV)activity.getTypeView()).getFragment(0);
        rclass.setLayoutManager(new LinearLayoutManager(activity,LinearLayoutManager.VERTICAL,false));
        rclass.addItemDecoration(new RecyclerLimit(activity,(byte) LinearLayoutManager.HORIZONTAL));
        rclass.setItemAnimator(new DefaultItemAnimator());
        rclass.setAdapter(new TxtAdapter(activity, null));
        ((TxtAdapter) rclass.getAdapter()).setItemOnClickListener(fragment.getRclassListener());
        library.setLayoutManager(new LinearLayoutManager(getRootView().getContext(),LinearLayoutManager.HORIZONTAL,false));
        library.addItemDecoration(new RecyclerLimit(activity,(byte) LinearLayoutManager.VERTICAL));
        library.setItemAnimator(new DefaultItemAnimator());
        library.setAdapter(new TxtAdapter(activity, null,true));
        ((TxtAdapter) library.getAdapter()).setItemOnClickListener(fragment.getLibraryListener());
        info.setLayoutManager(new LinearLayoutManager(activity,LinearLayoutManager.VERTICAL,false));
        info.addItemDecoration(new RecyclerLimit(activity,(byte) LinearLayoutManager.HORIZONTAL));
        info.setItemAnimator(new DefaultItemAnimator());
        info.setAdapter(new CommodityAdapter(activity, null,true));
        ((CommodityAdapter) info.getAdapter()).setItemOnClickListener(fragment.getInfoListener());
        View.OnClickListener listener = fragment.getClickListener();
        shoppingTrolley.getChildAt(0).setOnClickListener(listener);
        closeAccount.setOnClickListener(listener);
        shoppingTrolley.getChildAt(0).setClickable(false);
        closeAccount.setClickable(false);
    }
    public void updateRclass(final List<CharSequence[]> list){
        ((ShopActivity) getRootView().getContext()).runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if(null == rclass)return;
                ((TxtAdapter) rclass.getAdapter()).update(list);
                if(0< rclass.getAdapter().getItemCount()) {
                    rclass.setVisibility(View.VISIBLE);
                    noData.setVisibility(View.GONE);
                }else
                    noData.setVisibility(View.VISIBLE);
            }
        });
    }
    public void updateLibrary(final List<CharSequence[]>list){
        ((ShopActivity) getRootView().getContext()).runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if(null == library)return;
                ((TxtAdapter) library.getAdapter()).update(list);
                if (0 < library.getAdapter().getItemCount()) {
                    library.setVisibility(View.VISIBLE);
                    noData.setVisibility(View.GONE);
                } else
                    noData.setVisibility(View.VISIBLE);
            }
        });
    }
    public void updateInfo(final List<CharSequence[]>list){
        ((ShopActivity) getRootView().getContext()).runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if(null == info)return;
                ((CommodityAdapter) info.getAdapter()).update(list,false);
                if (0 < info.getAdapter().getItemCount()) {
                    info.setVisibility(View.VISIBLE);
                    noData.setVisibility(View.GONE);
                } else
                    noData.setVisibility(View.VISIBLE);
            }
        });
    }
    public void updateShoppingCart(final int count,final float totalPrice){
        if (0 < count) {
            shoppingTrolley.getChildAt(0).setBackground(ContextCompat.getDrawable(getRootView().getContext(), R.drawable.shape_annulus_red));
            shoppingTrolley.getChildAt(1).setVisibility(View.VISIBLE);
            ((TextView) shoppingTrolley.getChildAt(1)).setText(count + "");
            String payStr = pay.getText().toString().trim();
            String price = getRootView().getContext().getString(R.string.sign) + totalPrice;//(count * totalPrice+("".equals(payStr)?0:Float.valueOf(payStr.substring(1))));
            SpannableString ss = new SpannableString(price);
            ss.setSpan(new AbsoluteSizeSpan((int) getRootView().getContext().getResources().getDimension(R.dimen.size20)), 0, price.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            closeAccount.setBackgroundResource(R.color.scarlet);
            pay.setText(ss);
            shoppingTrolley.getChildAt(0).setClickable(true);
            closeAccount.setClickable(true);
        } else {
            shoppingTrolley.getChildAt(0).setBackground(ContextCompat.getDrawable(getRootView().getContext(), R.drawable.shape_annulus_gray));
            shoppingTrolley.getChildAt(1).setVisibility(View.GONE);
            closeAccount.setBackgroundResource(R.color.lucencyBlack6f);
            pay.setText("");
            shoppingTrolley.getChildAt(0).setClickable(false);
            closeAccount.setClickable(false);
        }
    }
    public void addShoppingCart(final int count,final float totalPrice){
        if(1< pay.getText().toString().trim().length())
            updateShoppingCart(Integer.valueOf(((TextView)shoppingTrolley.getChildAt(1)).getText().toString().trim())+ count,totalPrice + Float.valueOf(pay.getText().toString().substring(1).trim()));
    }
    public float getPaySum(){
        String payStr = pay.getText().toString().trim();
        return "".equals(payStr) ?0.0f: Float.valueOf(payStr.substring(1));
    }
}
