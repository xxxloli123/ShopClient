package com.zsh.shopclient.fPresenter;

import android.content.Intent;
import android.content.res.TypedArray;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.ArrayRes;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.xxx.skynet.sharedPreference.IOSharedPreferences;
import com.zsh.shopclient.R;
import com.zsh.shopclient.aPresenter.AppraiseActivity;
import com.zsh.shopclient.aPresenter.BaseActivity;
import com.zsh.shopclient.aPresenter.OrderInfoActivity;
import com.zsh.shopclient.aView.BaseView;
import com.zsh.shopclient.adapter.BaseRecycleAdapter;
import com.zsh.shopclient.adapter.TextAdapter;
import com.zsh.shopclient.fView.OrderV;
import com.zsh.shopclient.model.KeyValue;
import com.zsh.shopclient.model.Order;
import com.zsh.shopclient.popupWindow.PayPopupWindow;
import com.zsh.shopclient.popupWindow.PullDownList;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by Administrator on 2017/11/5.
 */

public class OrderFragment extends BaseFragment {
    private PullDownList pullDownList;
    private boolean isAll;
    private int page,position;
    private Order order;
    private int allIndex = 0,wayIndex = 0;
    private static final String[] alls = new String[]{"all","paymented","Complete","CancelOrder"," Pending"};
    private static final String[] ways = new String[]{"NormalOrder","ShopConsumption"};
    private void linkNet(final String allKey,final String wayKey){
        final String userId = IOSharedPreferences.inString(getActivity(),getString(R.string.user),getString(R.string.userId));
        if(null == userId)return;
        MultipartBody.Builder requestBody = new MultipartBody.Builder().setType(MultipartBody.FORM);
        requestBody.addFormDataPart("userId",userId).addFormDataPart("status",alls[allIndex]).addFormDataPart("type",ways[wayIndex]).
                addFormDataPart("startPage",page+"").addFormDataPart("pageSize",8+"");
        new OkHttpClient().newCall(new Request.Builder().url(getString(R.string.ip)+getString(R.string.ordersUrl)).post(requestBody.build()).build()).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                getTypeView().hintHoast(R.string.getDataDefeated,null, Toast.LENGTH_SHORT);
                ((OrderV)getTypeView()).updateRecycler(order.getList(allKey,wayKey),false);
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                try {
                    JSONObject result = new JSONObject(response.body().string());
                    Log.i("OrderFragment", "linkNet->" + result);
                    if (200 == result.getInt("statusCode")) {
                        order.toList(result.getJSONObject("ordersInfo").getJSONArray("aaData"),1 == page);
                        ((OrderV) getTypeView()).updateRecycler(order.getList(allKey,wayKey),0== result.getJSONObject("ordersInfo").getJSONArray("aaData").length());
                    }else
                        ((OrderV)getTypeView()).updateRecycler(order.getList(allKey,wayKey),false);
                } catch (JSONException e) {
                    e.printStackTrace();
                    ((OrderV)getTypeView()).updateRecycler(order.getList(allKey,wayKey),false);
                }
            }
        });
    }
    @Override
    protected BaseView createTypeView() {
        return new OrderV();
    }
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        order = new Order();
        pullDownList = new PullDownList((BaseActivity) getActivity(),null, new TextAdapter.ItemOnClickListener() {
            @Override
            public void itemOnClick(View view) {
                if(isAll){
                    allIndex = view.getId();
                    String key = ((CharSequence[]) view.getTag())[1].toString();
                    ((OrderV) getTypeView()).setAllText(key, order.getList(getActivity().getString(R.string.all).equals(key) ?"": key,((OrderV)getTypeView()).getCommodityOrderText()));
                }else {
                    wayIndex = view.getId();
                    ((OrderV) getTypeView()).setCommodityOrderText(((CharSequence[]) view.getTag())[1].toString(), order.getList(((OrderV) getTypeView()).getAllText(), ((CharSequence[]) view.getTag())[1].toString()));
                }
                ((OrderV) getTypeView()).updateRecycler(order.getList(((OrderV)getTypeView()).getAllText(),((OrderV)getTypeView()).getCommodityOrderText()),false);
                pullDownList.dismissPopupWindow();
            }
        });
        page = 1;
        linkNet("",getString(R.string.homeDelivery));
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View layout = super.onCreateView(inflater, container, savedInstanceState);
        ((OrderV)getTypeView()).updateRecycler(order.getList("",getString(R.string.homeDelivery)),false);
        return layout;
    }

    public List<CharSequence[]> initRecyclerWay(@ArrayRes int iconIds, @ArrayRes int keyIds){
        String resource = "android.resource://"+getActivity().getPackageName()+"/";
        List<CharSequence[]> list = new ArrayList<>();
        TypedArray icons = getResources().obtainTypedArray(iconIds);
        TypedArray titles = getResources().obtainTypedArray(keyIds);
        if(null != icons && null != titles) {
            int iconsLen = icons.length();
            int titlesLen = titles.length();
            int lenght =  iconsLen <= titlesLen ? iconsLen : titlesLen;
            for (int index = 0; index < lenght; index++)
                list.add(new String[]{resource +icons.getResourceId(index, 0), getString(titles.getResourceId(index, 0))});
        }
        return list;
    }
    public View.OnClickListener getClickListener(){
        return new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                switch (v.getId()) {
                    case R.id.text_all:
                        isAll = true;
                        ((OrderV) getTypeView()).viewChangeColor(v, 0.6f, R.color.scarlet, R.mipmap.icon_triangle_up_red);
                        pullDownList.setList(initRecyclerWay(R.array.orderIcons, R.array.orderKeys));
                        pullDownList.displayPopupWindow((View) v.getParent());
                        break;
                    case R.id.text_commodityOrder:
                        isAll = false;
                        ((OrderV) getTypeView()).viewChangeColor(v, 0.6f, R.color.scarlet, R.mipmap.icon_triangle_up_red);
                        pullDownList.setList(initRecyclerWay(R.array.wayIcons, R.array.wayTitles));
                        pullDownList.displayPopupWindow((View) v.getParent());
                        break;
                    default:
                        if(getString(R.string.pay).equals(((TextView)v).getText().toString().trim())) {
                            Map<String, String> map = new HashMap<>();
                            map.put("orderId",((KeyValue<CharSequence[],List<CharSequence[]>>)v.getTag()).getKey()[30].toString());
                            new PayPopupWindow((BaseActivity) getActivity(), map, new PayPopupWindow.PayResultListener() {
                                @Override
                                public void inform(byte result) {
                                    if(PayPopupWindow.SUCCEED == result) {
                                        order.setListStatus(v.getId(), getString(R.string.inYheDistribution));
                                        ((OrderV) getTypeView()).updateRecycler(order.getList(((OrderV)getTypeView()).getAllText(),((OrderV)getTypeView()).getCommodityOrderText()),false);
                                    }
                                }
                            }).displayPopupWindow();
                        }else if(getString(R.string.cancelation).equals(((TextView)v).getText().toString().trim())){
                            MultipartBody.Builder requestBody = new MultipartBody.Builder().setType(MultipartBody.FORM);
                            requestBody.addFormDataPart("userId",IOSharedPreferences.inString(getActivity(),getString(R.string.user),getString(R.string.userId))).
                                    addFormDataPart("orderId",((KeyValue<CharSequence[],List<CharSequence[]>>)v.getTag()).getKey()[30].toString());
                            new OkHttpClient().newCall(new Request.Builder().url(getString(R.string.ip)+getString(R.string.cancelOrder)).post(requestBody.build()).build()).enqueue(new Callback() {
                                @Override
                                public void onFailure(Call call, IOException e) {
                                    getTypeView().hintHoast(R.string.getDataDefeated,null, Toast.LENGTH_SHORT);
                                }

                                @Override
                                public void onResponse(Call call, Response response) throws IOException {
                                    try {
                                        JSONObject result = new JSONObject(response.body().string());
                                        Log.i("OrderFragment", "getClickListener-取消订单->" + result);
                                        if (200 == result.getInt("statusCode"))
                                            getActivity().runOnUiThread(new Runnable() {
                                                @Override
                                                public void run() {
                                                    if(null != getTypeView())
                                                        ((OrderV) getTypeView()).updateRecycler(order.getList(((OrderV) getTypeView()).getAllText(), ((OrderV) getTypeView()).getCommodityOrderText()),false);
                                                }
                                            });
                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }
                                }
                            });
                        }else if(getString(R.string.makeSurtTheGoods).equals(((TextView)v).getText().toString().trim())){
                            MultipartBody.Builder requestBody = new MultipartBody.Builder().setType(MultipartBody.FORM);
                            requestBody.addFormDataPart("userId",IOSharedPreferences.inString(getActivity(),getString(R.string.user),getString(R.string.userId))).
                                    addFormDataPart("orderId",((KeyValue<CharSequence[],List<CharSequence[]>>)v.getTag()).getKey()[30].toString());
                            new OkHttpClient().newCall(new Request.Builder().url(getString(R.string.ip)+getString(R.string.receiving)).post(requestBody.build()).build()).enqueue(new Callback() {
                                @Override
                                public void onFailure(Call call, IOException e) {
                                    getTypeView().hintHoast(R.string.getDataDefeated,null, Toast.LENGTH_SHORT);
                                }

                                @Override
                                public void onResponse(Call call, Response response) throws IOException {
                                    try {
                                        JSONObject result = new JSONObject(response.body().string());
                                        Log.i("OrderFragment", "getClickListener-确定收货->" + result);
                                        if (200 == result.getInt("statusCode"))
                                            getActivity().runOnUiThread(new Runnable() {
                                                @Override
                                                public void run() {
                                                    if(null != getTypeView())
                                                        ((OrderV) getTypeView()).updateRecycler(order.getList(((OrderV)getTypeView()).getAllText(),((OrderV)getTypeView()).getCommodityOrderText()),false);
                                                }
                                            });
                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }
                                }
                            });
                        }else if(getString(R.string.pendingReviews).equals(((TextView)v).getText().toString().trim())){
                            position = v.getId();
                            HashMap hashMap = new HashMap();
                            hashMap.put(((KeyValue<CharSequence[],List<CharSequence[]>>)v.getTag()).getKey(),((KeyValue<CharSequence[],List<CharSequence[]>>)v.getTag()).getValue());
                            Intent test = new Intent(getActivity(), AppraiseActivity.class);
                            test.putExtra(getString(R.string.title),hashMap);
                            startActivityForResult(test, OrderInfoActivity.EVALUATED);
                        }else if(getString(R.string.contactWithTheRider).equals(((TextView)v).getText().toString().trim()))
                            startActivity(new Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + ((CharSequence[])v.getTag())[15])));
                }
            }
        };
    }
    public void setStatus(boolean bool){
        if(bool) {
            order.setListStatus(position, getString(R.string.offTheStocks));
            ((OrderV) getTypeView()).updateRecycler(order.getList(((OrderV)getTypeView()).getAllText(),((OrderV)getTypeView()).getCommodityOrderText()),false);
        }
    }
    public BaseRecycleAdapter.FootLoadListener getFootLoadListener(){
        return new BaseRecycleAdapter.FootLoadListener() {
            @Override
            public void loadMoreListener() {
                page++;
                linkNet(((OrderV)getTypeView()).getAllText(),((OrderV)getTypeView()).getCommodityOrderText());
            }
        };
    }
    public SwipeRefreshLayout.OnRefreshListener getUpdateListener(){
        return new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                page = 1;
                linkNet(((OrderV)getTypeView()).getAllText(),((OrderV)getTypeView()).getCommodityOrderText());
                ((OrderV)getTypeView()).updateFinish();
            }
        };
    }
}
