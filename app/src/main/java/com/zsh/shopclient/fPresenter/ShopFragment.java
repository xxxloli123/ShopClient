package com.zsh.shopclient.fPresenter;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.xxx.skynet.sharedPreference.IOSharedPreferences;
import com.zsh.shopclient.R;
import com.zsh.shopclient.aPresenter.BaseActivity;
import com.zsh.shopclient.aPresenter.CloseAccountActivity;
import com.zsh.shopclient.aPresenter.LoginActivity;
import com.zsh.shopclient.aPresenter.MainActivity;
import com.zsh.shopclient.aPresenter.ShopActivity;
import com.zsh.shopclient.aView.BaseView;
import com.zsh.shopclient.adapter.CommodityAdapter;
import com.zsh.shopclient.adapter.TextAdapter;
import com.zsh.shopclient.fView.ShopV;
import com.zsh.shopclient.model.CommodityClassify;
import com.zsh.shopclient.model.ShoppingCart;
import com.zsh.shopclient.popupWindow.ShoppingPopupWindow;
import com.zsh.shopclient.popupWindow.PropertyPopupWindow;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by Administrator on 2017/11/17.
 */

public class ShopFragment extends BaseFragment {
    private ShoppingCart shoppingCart;
    private CommodityClassify commodity;

    private void linkNet(final CharSequence[]libraryKey) {
        if (null != libraryKey && (null == commodity.getKindKeys() || 0== commodity.toList(commodity.getKindKeys()).size())) {
            MultipartBody.Builder requestBody = new MultipartBody.Builder().setType(MultipartBody.FORM);
            requestBody.addFormDataPart("shopId", ((ShopActivity) getActivity()).getShopInfo()[9].toString()).addFormDataPart("classId", libraryKey[1].toString());
            new OkHttpClient().newCall(new Request.Builder().url(getString(R.string.ip) + getString(R.string.commodity)).post(requestBody.build()).build()).enqueue(new Callback() {
                @Override
                public void onFailure(Call call, IOException e) {
                    e.printStackTrace();
                    getTypeView().hintHoast(R.string.getDataDefeated, null, Toast.LENGTH_SHORT);
                }

                @Override
                public void onResponse(Call call, Response response) throws IOException {
                    try {
                        JSONObject result = new JSONObject(response.body().string());
                        Log.i("ShopFragment", "linkNet->" + result);
                        if (200 == result.getInt("statusCode")) {
                            commodity.putKind(result.getJSONObject("productsInfo").getJSONArray("aaData"));
                            ((ShopV) getTypeView()).updateInfo(commodity.toList(commodity.getKindKeys()));
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            });
        }else
            ((ShopV) getTypeView()).updateInfo(commodity.toList(commodity.getKindKeys()));
    }
    private void getShoppingCartData(final boolean isfirst,String userId){
        MultipartBody.Builder body = new MultipartBody.Builder().setType(MultipartBody.FORM);
        body.addFormDataPart("shopId",((ShopActivity)getActivity()).getShopInfo()[9].toString()).addFormDataPart("userId",userId);
        new OkHttpClient().newCall(new Request.Builder().url(getString(R.string.ip)+ getString(R.string.shoppingTrolley)).post(body.build()).build()).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                getTypeView().hintHoast(R.string.getDataDefeated,null, Toast.LENGTH_SHORT);
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                try {
                    JSONObject result = new JSONObject(response.body().string());
                    Log.i("ShoppingCartActivity", "linkNet->" + result);
                    if (200 == result.getInt("statusCode")) {
                        if(0< result.getJSONArray("listsCart").length()) {
                            shoppingCart.getList().clear();
                            shoppingCart.toList(result.getJSONArray("listsCart").getJSONArray(0));
                            getActivity().runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    if(null == getTypeView())return;
                                    if(!isfirst) {
                                        ShoppingPopupWindow popupWindow = new ShoppingPopupWindow((BaseActivity) getActivity(), ((ShopActivity) getActivity()).getShopInfo()[9].toString(), shoppingCart);
                                        popupWindow.setNotification(new ShoppingPopupWindow.Notification() {
                                            @Override
                                            public void updateShoppingCart(int total, float totalPrice) {
                                                ((ShopV) getTypeView()).updateShoppingCart(total, totalPrice);
                                            }
                                        });
                                        popupWindow.displayPopupWindow();
                                    }else
                                        ((ShopV)getTypeView()).updateShoppingCart(shoppingCart.getTotal(),shoppingCart.getTotalPrice());
                                }
                            });
                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }
    private void addShoppingCart(final CharSequence[] array,final CharSequence[] screen,final int total){
        final String userId = IOSharedPreferences.inString(getActivity(),getString(R.string.user),getString(R.string.userId));
        if(null != userId)
            if(null != screen[0] && null != screen[1]) {
            MultipartBody.Builder body = new MultipartBody.Builder().setType(MultipartBody.FORM);
            body.addFormDataPart("userId", userId).addFormDataPart("productId", (String) array[3]).addFormDataPart("productcategory", (String) screen[0]).
                    addFormDataPart("standardName", (String) screen[1]).addFormDataPart("goodsnum",total+"").addFormDataPart("type", getActivity().getIntent().getStringExtra(getString(R.string.requestParam)));
            new OkHttpClient().newCall(new Request.Builder().url(getString(R.string.ip) + getString(R.string.addShoppingCartPath)).post(body.build()).build()).enqueue(new Callback() {
                @Override
                public void onFailure(Call call, IOException e) {
                    getTypeView().hintHoast(R.string.submitDataDefeated, null, Toast.LENGTH_SHORT);
                }

                @Override
                public void onResponse(Call call, Response response) throws IOException {
                    try {
                        JSONObject result = new JSONObject(response.body().string());
                        Log.i("PropertyPopupWindow", "clickListener--->" + result);
                        if (200 == result.getInt("statusCode")) {
                            shoppingCart.add(array,total);
                            getActivity().runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    ((ShopV) getTypeView()).updateShoppingCart(shoppingCart.getTotal(),((ShopV)getTypeView()).getPaySum() + Math.round(Float.valueOf((String) array[2])*100)/100.0f);
                                }
                            });
                        }else
                            getTypeView().hintHoast(BaseView.X, result.getString("message"), Toast.LENGTH_SHORT);
                    } catch (JSONException e) {
                        e.printStackTrace();
                        getTypeView().hintHoast(R.string.submitDataDefeated, null, Toast.LENGTH_SHORT);
                    }
                }
            });
        }else
            Toast.makeText(getActivity(),getString(R.string.selectCommodityAttribute),Toast.LENGTH_SHORT).show();
        else
            startActivityForResult(new Intent(getActivity(),LoginActivity.class),MainActivity.RETURN_USER);
    }
    @Override
    protected BaseView createTypeView() {
        return new ShopV();
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        commodity = new CommodityClassify();
        shoppingCart = new ShoppingCart();
        MultipartBody.Builder requestBody = new MultipartBody.Builder().setType(MultipartBody.FORM);
        requestBody.addFormDataPart("shopId",((ShopActivity)getActivity()).getShopInfo()[9].toString()).addFormDataPart("fatherId","1");
        new OkHttpClient().newCall(new Request.Builder().url(getString(R.string.ip)+getString(R.string.shopClass)).post(requestBody.build()).build()).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
                getTypeView().hintHoast(R.string.getDataDefeated,null, Toast.LENGTH_SHORT);
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                try {
                    JSONObject result = new JSONObject(response.body().string());
                    Log.i("ShopFragment","onViewCreated->"+result);
                    if(200 == result.getInt("statusCode")){
                        commodity.putClass(result.getJSONArray("listclass"));
                        ((ShopV)getTypeView()).updateRclass(commodity.toList(commodity.getClassKeys()));
                        ((ShopV) getTypeView()).updateLibrary(commodity.toList(commodity.getLibraryKeys()));
                        linkNet(commodity.getLibraryKey());
                    }else
                        getTypeView().hintHoast(BaseView.X,result.getString("message"), Toast.LENGTH_SHORT);
                } catch (JSONException e) {
                    e.printStackTrace();
                    getTypeView().hintHoast(R.string.parseFailure,null, Toast.LENGTH_SHORT);
                }
            }
        });
        String userId = IOSharedPreferences.inString(getActivity(),getString(R.string.user),getString(R.string.userId));
        if(null != userId)
            getShoppingCartData(true,userId);
    }
    public TextAdapter.ItemOnClickListener getRclassListener(){
        return new TextAdapter.ItemOnClickListener(){
            @Override
            public void itemOnClick(View view) {
                commodity.setClassKey((CharSequence[])view.getTag());
                ((ShopV)getTypeView()).updateLibrary(commodity.toList(commodity.getLibraryKeys()));
                linkNet(commodity.getLibraryKey());
            }
        };
    }
    public TextAdapter.ItemOnClickListener getLibraryListener(){
        return new TextAdapter.ItemOnClickListener() {
            @Override
            public void itemOnClick(View view) {
                commodity.setLibraryKey((CharSequence[]) view.getTag());
                linkNet(commodity.getLibraryKey());
            }
        };
    }
    public CommodityAdapter.ItemOnClickListener getInfoListener(){
        return new CommodityAdapter.ItemOnClickListener(){
            @Override
            public int onClickAdd(View itemView,final CharSequence[] array) {
                int count = 1;
                /*if(null == shoppingCart)
                    shoppingCart = new LinkedList<>();
                float totalPrice = 0;
                if(0< shoppingCart.size()) {
                    boolean sole = true;
                    for(int index = 0;index < shoppingCart.size();index++) {
                        int total = Integer.valueOf((String)shoppingCart.get(index)[5]);
                        if(shoppingCart.get(index)[6].equals(array[6])) {
                            count = ++total;
                            sole = false;
                        }
                        totalPrice += Float.valueOf((String) shoppingCart.get(index)[3])* total;
                        shoppingCart.get(index)[5] = ""+ total;
                    }
                    if(sole) {
                        totalPrice += Float.valueOf((String) array[3]);
                        shoppingCart.add(array);
                    }
                }else{
                    shoppingCart.add(array);//warnning
                    totalPrice = Float.valueOf((String) array[3]);
                }*/
                commodity.setKindKey(array);
                if(0< commodity.getKindValue().size()) {
                    final PropertyPopupWindow property = new PropertyPopupWindow((BaseActivity) getActivity(), array, commodity.getKindValue());
                    property.setStubmit(new PropertyPopupWindow.Submit() {
                        @Override
                        public void commit(CharSequence[] charSequences,int total) {
                            addShoppingCart(array,charSequences,total);
                            property.dismiss();
                        }
                    });
                    property.displayPopupWindow();
                }else
                    addShoppingCart(array,new String[]{"",""},1);
                    //((ShopV) getTypeView()).updateShoppingCart(1,Math.round(Float.valueOf((String) array[3])*100)/100.0f);
                return count;
            }

            @Override
            public int onClickReduce(View itemView,CharSequence[] array) {
                int count = shoppingCart.reduce(array[6]);
                ((ShopV)getTypeView()).updateShoppingCart(shoppingCart.getList().size(),shoppingCart.getTotalPrice());
                return count;
            }
        };
    }
    public View.OnClickListener getClickListener(){
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (v.getId()){
                    case R.id.image_shoppingTrolley:
                        /*Intent intent = new Intent(getActivity(), ShoppingCartActivity.class);
                        intent.putExtra(getString(R.string.send),((ShopActivity) getActivity()).getCharSequences()[7].toString());
                        getActivity().startActivityForResult(intent,ShopActivity.SHOPPINGcART);*/
                        String userId = IOSharedPreferences.inString(getActivity(),getString(R.string.user),getString(R.string.userId));
                        if(0< shoppingCart.getList().size())
                            getShoppingCartData(false,userId);
                        break;
                    case R.id.text_closeAccount:
                        if(0== shoppingCart.getList().size()) break;
                        Intent closeAccount = new Intent(getActivity(), CloseAccountActivity.class);
                        HashMap<CharSequence[],List<CharSequence[]>> hashMap = new HashMap<>();
                        CharSequence[] infos = ((ShopActivity) getActivity()).getShopInfos();
                        hashMap.put(infos,shoppingCart.getList());
                        Bundle bundle = new Bundle();
                        bundle.putSerializable(getString(R.string.send),hashMap);
                        bundle.putSerializable(getString(R.string.sendBody),((ShopActivity) getActivity()).getHashMap());
                        closeAccount.putExtras(bundle);
                        closeAccount.putExtra(getString(R.string.sendTail),((ShopActivity)getActivity()).getFreeShipping());
                        getActivity().startActivityForResult(closeAccount,ShopActivity.SHOPPINGcART);
                        break;
                }
            }
        };
    }
}
