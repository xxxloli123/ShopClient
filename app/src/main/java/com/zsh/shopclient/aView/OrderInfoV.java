package com.zsh.shopclient.aView;

import android.support.annotation.StringRes;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.zsh.shopclient.R;
import com.zsh.shopclient.aPresenter.OrderInfoActivity;
import com.zsh.shopclient.adapter.GoodsAdapter;
import com.zsh.shopclient.tool.RecyclerLimit;

import java.util.List;
import java.util.Map;

import butterknife.BindView;

/**
 * Created by Administrator on 2017/12/20.
 */

public class OrderInfoV extends BaseView {
    @BindView(R.id.text_status)TextView status;
    @BindView(R.id.text)TextView text;
    @Override
    protected int getLayoutId() {
        return R.layout.activity_order_info;
    }

    @Override
    public void initView() {
        View.OnClickListener listener = ((OrderInfoActivity)getRootView().getContext()).getClickListener();
        getRootView().findViewById(R.id.image_goBack).setOnClickListener(listener);
       Map<CharSequence[],List<CharSequence[]>>map = (Map<CharSequence[], List<CharSequence[]>>) ((OrderInfoActivity)getRootView().getContext()).getIntent().getExtras().get(getRootView().getContext().getString(R.string.title));
        status.setText(map.keySet().iterator().next()[28].toString());
        Map<String,CharSequence[]> statusValue = (Map<String, CharSequence[]>) ((OrderInfoActivity)getRootView().getContext()).getIntent().getExtras().get(getRootView().getContext().getString(R.string.send));
        if(null != statusValue.get(map.keySet().iterator().next()[28]))
            text.setText(statusValue.get(map.keySet().iterator().next()[28])[0]);
        else
            text.setVisibility(View.GONE);
        text.setOnClickListener(listener);
        TextView shopName = getRootView().findViewById(R.id.text_shopName);
        shopName.setText(map.keySet().iterator().next()[1]);
        shopName.setOnClickListener(listener);
        RecyclerView recycler = getRootView().findViewById(R.id.recycler);
        recycler.setLayoutManager(new LinearLayoutManager(getRootView().getContext(),LinearLayoutManager.VERTICAL,false));
        recycler.addItemDecoration(new RecyclerLimit(getRootView().getContext(),(byte) LinearLayout.HORIZONTAL));
        recycler.setItemAnimator(new DefaultItemAnimator());
        recycler.setAdapter(new GoodsAdapter((OrderInfoActivity)getRootView().getContext(),map.get(map.keySet().iterator().next())));
        ((TextView)getRootView().findViewById(R.id.text_packingExpense)).setText(map.keySet().iterator().next()[7]);
        ((TextView)getRootView().findViewById(R.id.text_freight)).setText(map.keySet().iterator().next()[10]);
        ((TextView)getRootView().findViewById(R.id.text_sum)).setText(getRootView().getContext().getString(R.string.sign)+map.keySet().iterator().next()[21]);
        TextView shopPhone = getRootView().findViewById(R.id.text_contactPhone);
        shopPhone.setText(map.keySet().iterator().next()[2]);
        shopPhone.setOnClickListener(listener);
        ((TextView)getRootView().findViewById(R.id.text_address)).setText(change(getRootView().getContext().getString(R.string.shipingAddress), map.keySet().iterator().next()[16].toString()
                + map.keySet().iterator().next()[17]+ map.keySet().iterator().next()[18]+ map.keySet().iterator().next()[19]+ map.keySet().iterator().next()[20]));
        ((TextView)getRootView().findViewById(R.id.text_freightWay)).setText(change(getRootView().getContext().getString(R.string.freightWay),getRootView().getContext().getString("yes".equals(map.keySet().iterator().next()[25])?R.string.huiDiDeliver:R.string.merchantDeliver)));
        ((TextView)getRootView().findViewById(R.id.text_freightMember)).setText(change(getRootView().getContext().getString(R.string.freightMember),map.keySet().iterator().next()[22].toString()+"("+ map.keySet().iterator().next()[23]+")"));
        ((TextView)getRootView().findViewById(R.id.text_remark)).setText(change(getRootView().getContext().getString(R.string.remark),map.keySet().iterator().next()[11].toString()));
        ((TextView)getRootView().findViewById(R.id.text_orderCode)).setText(change(getRootView().getContext().getString(R.string.orderCode),map.keySet().iterator().next()[27].toString()));
        ((TextView)getRootView().findViewById(R.id.text_payWay)).setText(change(getRootView().getContext().getString(R.string.payWay),map.keySet().iterator().next()[29].toString()));
        ((TextView)getRootView().findViewById(R.id.text_orderTime)).setText(change(getRootView().getContext().getString(R.string.orderTime),map.keySet().iterator().next()[26].toString()));
    }
    private SpannableString change(String hint,String string){
        SpannableString ss = new SpannableString(hint+": " + string);
        ss.setSpan(new ForegroundColorSpan(ContextCompat.getColor(getRootView().getContext(),R.color.textGray)),0,hint.length()+2,SpannableString.SPAN_EXCLUSIVE_EXCLUSIVE);
        return ss;
    }
    public void setStatus(@StringRes final int stringId, final CharSequence statucValue){
        ((OrderInfoActivity)getRootView().getContext()).runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if(null == getRootView())return;
                if(-1 != stringId) {
                    status.setText(stringId);
                    text.setText(statucValue);
                }
                status.setVisibility(-1 == stringId ? View.VISIBLE : View.GONE);
                text.setVisibility(-1 == stringId ? View.VISIBLE : View.GONE);
            }
        });
    }
}
