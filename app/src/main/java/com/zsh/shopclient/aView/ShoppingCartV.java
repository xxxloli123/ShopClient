package com.zsh.shopclient.aView;

import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.zsh.shopclient.R;
import com.zsh.shopclient.aPresenter.ShoppingCartActivity;
import com.zsh.shopclient.adapter.ShoppingCartAdapter;
import com.zsh.shopclient.tool.RecyclerLimit;

import java.util.List;

import butterknife.BindView;

/**
 * Created by Administrator on 2017/11/19.
 */

public class ShoppingCartV extends BaseView {
    public static final int EMPTYsHOPPINGcART = 1;
    @BindView(R.id.linear_navi)LinearLayout navi;
    @BindView(R.id.recycler)RecyclerView recycler;
    @BindView(R.id.text_noData)TextView noData;
    @Override
    protected int getLayoutId() {
        return R.layout.activity_shopping_cart;
    }

    @Override
    public void initView() {
        ShoppingCartActivity activity = (ShoppingCartActivity) getRootView().getContext();
        View.OnClickListener listener = activity.getClickListener();
        activity.findViewById(R.id.image_goBack).setOnClickListener(listener);
        ((TextView)getRootView().findViewById(R.id.text_title)).setText(R.string.shoppingCart);
        ImageView empty = new ImageView(activity);
        empty.setImageResource(R.mipmap.icon_empty);
        navi.addView(empty);
        navi.setOnClickListener(listener);
        getRootView().findViewById(R.id.swipe_update).setEnabled(false);
        recycler.setLayoutManager(new LinearLayoutManager(activity,LinearLayoutManager.VERTICAL,false));
        recycler.addItemDecoration(new RecyclerLimit(getRootView().getContext(),(byte)LinearLayout.HORIZONTAL));
        recycler.setItemAnimator(new DefaultItemAnimator());
        ShoppingCartAdapter adapter = new ShoppingCartAdapter(activity,null);
        adapter.setItemOnClickListener(activity.getOnClickListener(),activity.getEmptyListener());
        recycler.setAdapter(adapter);
        activity.findViewById(R.id.text_closeAccount).setOnClickListener(listener);
    }
    public void updateAdapter(final List<CharSequence[]> list){
        ((ShoppingCartActivity)getRootView().getContext()).runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if(null == recycler)return;
                ((ShoppingCartAdapter)recycler.getAdapter()).update(list);
                navi.setClickable(0< recycler.getAdapter().getItemCount());
                noData.setVisibility(0< recycler.getAdapter().getItemCount() ? View.GONE : View.VISIBLE);
            }
        });
    }
}
