package com.zsh.shopclient.aPresenter;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Toast;

import com.xxx.skynet.sharedPreference.IOSharedPreferences;
import com.zsh.shopclient.R;
import com.zsh.shopclient.aView.BaseView;
import com.zsh.shopclient.aView.CloseAccountV;
import com.zsh.shopclient.adapter.CommodityAdapter;
import com.zsh.shopclient.model.CloseAccount;
import com.zsh.shopclient.model.ShoppingCart;
import com.zsh.shopclient.popupWindow.AddressPopupWindow;
import com.zsh.shopclient.popupWindow.DatePickerPopupWindow;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by Administrator on 2017/12/3.
 */

public class CloseAccountActivity extends BaseActivity {
    private static final int ADDRESS = 2;
    private static final int DISCOUNTcOUPON = 3;
    private static final int BOOKINGsEATS = 4;
    private Map<String,String> bodyMap;
    private CloseAccount closeAccount;
    private ShoppingCart shoppingCart;
    private float restCost;
    private List<CharSequence[]>list;
    private void goBack(){
        Intent resultIntent = getIntent();
        resultIntent.putExtra(getString(R.string.result),null == shoppingCart ? 0:shoppingCart.getTotal());
        resultIntent.putExtra(getString(R.string.attach),null == shoppingCart ? 0:shoppingCart.getTotalPrice());
        resultIntent.putExtra(getString(R.string.send),true);
        setResult(RESULT_OK,resultIntent);
        finish();
    }
    private void submit(final int index,final CharSequence[]array,final CharSequence way) {
        MultipartBody.Builder requestBody = new MultipartBody.Builder().setType(MultipartBody.FORM);
        requestBody.addFormDataPart("advanceOrderId",bodyMap.get("advanceOrderId")).addFormDataPart("goodsId", array[7].toString()).addFormDataPart("type",way.toString());
        new OkHttpClient().newCall(new Request.Builder().url(getString(R.string.ip) + getString(R.string.updateOrder)).post(requestBody.build()).build()).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
                getTypeView().hintHoast(R.string.getDataDefeated, null, Toast.LENGTH_SHORT);
            }
            @Override
            public void onResponse(Call call, Response response) throws IOException {
                try {
                    JSONObject result = new JSONObject(response.body().string());
                    Log.i("ShoppingCartActivity", "linkNet->" + result);
                    if (200 == result.getInt("statusCode")) {
                        shoppingCart.getList().get(index)[5] = Integer.valueOf((String) array[5]) + ("minus".equals(way) ? -1 : 1) + "";
                        if("0".equals(array[5]))
                            shoppingCart.getList().remove(index);
                        ((CloseAccountV)getTypeView()).updateAdapter(shoppingCart.getList(),(float)result.getDouble("UserActualFee")+ restCost);
                    }else
                        getTypeView().hintHoast(R.string.submitDataDefeated,null, Toast.LENGTH_SHORT);
                } catch (JSONException e) {
                    e.printStackTrace();
                    getTypeView().hintHoast(R.string.submitDataDefeated,null, Toast.LENGTH_SHORT);
                }
            }
        });
    }
    private void getRestsCost(){
        MultipartBody.Builder requestBody = new MultipartBody.Builder().setType(MultipartBody.FORM);
        requestBody.addFormDataPart("userId",IOSharedPreferences.inString(this,getString(R.string.user),getString(R.string.userId))).
        addFormDataPart("shopId",getIntent().getStringExtra(getString(R.string.sendHead)));
        new OkHttpClient().newCall(new Request.Builder().url(getString(R.string.ip)+getString(R.string.advanceOrder)).post(requestBody.build()).build()).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {}

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                try {
                    JSONObject result = new JSONObject(response.body().string());
                    Log.i("CloseAccountActivity", "onCreate->" + result);
                    if (200 == result.getInt("statusCode")) {
                        JSONObject jsonObject = result.getJSONObject("order");
                        shoppingCart.toList(jsonObject.getJSONArray("goods"));
                        restCost = (float)(jsonObject.getDouble("packingprice") + jsonObject.getDouble("deliveryFee"));
                        ((CloseAccountV)getTypeView()).updateAdapter(shoppingCart.getList(),(float)jsonObject.getDouble("userActualFee")+restCost);
                        ((CloseAccountV)getTypeView()).updateRest(jsonObject.getString("enjoyActivity"),jsonObject.getString("packingprice"),jsonObject.getString("deliveryFee"));
                        bodyMap.put("advanceOrderId",jsonObject.getString("id"));
                        deliveryCost(closeAccount.getDefaultTarget()[7],closeAccount.getDefaultTarget()[8]);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }
    @Override
    protected BaseView createTypeView() {
        return new CloseAccountV();
    }
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        bodyMap = new HashMap<String, String>();
        shoppingCart = new ShoppingCart();
        MultipartBody.Builder requestBody = new MultipartBody.Builder().setType(MultipartBody.FORM);
        requestBody.addFormDataPart("userId", IOSharedPreferences.inString(this, getString(R.string.user), getString(R.string.userId)));
        new OkHttpClient().newCall(new Request.Builder().url(getString(R.string.ip) + getString(R.string.getAddress)).post(requestBody.build()).build()).enqueue(new Callback() {
                @Override
                public void onFailure(Call call, IOException e) {}

                @Override
                public void onResponse(Call call, Response response) throws IOException {
                    try {
                        JSONObject result = new JSONObject(response.body().string());
                        Log.i("CloseAccountActivity", "onCreate->" + result);
                        if (200 == result.getInt("statusCode")) {
                            closeAccount = new CloseAccount(result.getJSONArray("address"));
                            ((CloseAccountV) getTypeView()).updateConsignee(closeAccount.mergetAddress());
                            bodyMap.put("addressStr", closeAccount.toJsonString(closeAccount.getTarget().get(closeAccount.getPosition())));
                            bodyMap.put("shopTransport", "no");
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            });
        getIntent().putExtra(getString(R.string.sendHead), ((Map<CharSequence[], List<CharSequence[]>>) getIntent().getExtras().get(getString(R.string.send))).keySet().iterator().next()[14]);
        getRestsCost();//getShoppingCartData();
        requestBody.addFormDataPart("shopId", getIntent().getStringExtra(getString(R.string.sendHead)));
        new OkHttpClient().newCall(new Request.Builder().url(getString(R.string.ip) + getString(R.string.discountCoupon)).post(requestBody.build()).build()).enqueue(new Callback() {
                @Override
                public void onFailure(Call call, IOException e) {}

                @Override
                public void onResponse(Call call, Response response) throws IOException {
                    try {
                        JSONObject result = new JSONObject(response.body().string());
                        Log.i("CloseAccountActivity", "onCreate->" + result);
                        if (200 == result.getInt("statusCode")) {
                            list = new ArrayList<CharSequence[]>();
                            JSONArray jsonArray = result.getJSONArray("list");
                            for (int index = 0; index < jsonArray.length(); index++) {
                                JSONObject jsonObject = jsonArray.getJSONObject(index);
                                list.add(new CharSequence[]{jsonObject.getString("cost"), jsonObject.getString("limitCost"), jsonObject.getString("endTime"),
                                        jsonObject.getString("id"), jsonObject.getString("couponName"), jsonObject.getString("gainDate"), jsonObject.getString("unlimited"),
                                        jsonObject.getString("type"), jsonObject.getString("couponId"), jsonObject.getString("startTime"), jsonObject.getString("shopName"),
                                        jsonObject.getString("shopId"), jsonObject.getString("userId"), jsonObject.getString("userPhone")});
                            }
                            ((CloseAccountV) getTypeView()).updateDiscountCoupon(list.size());
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            });
        if(null != getIntent().getExtras().get(getString(R.string.attach))) {
            bodyMap.put("advanceOrderId",((CharSequence[])getIntent().getExtras().get(getString(R.string.attach)))[6].toString());
            shoppingCart.getList().add((CharSequence[])getIntent().getExtras().get(getString(R.string.attach)));
            shoppingCart.getTotal();
            ((CloseAccountV) getTypeView()).updateAdapter(shoppingCart.getList(),shoppingCart.getTotalPrice());
        }
    }
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if(KeyEvent.KEYCODE_BACK == keyCode)
            goBack();
        return super.onKeyDown(keyCode, event);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(null != data)
            switch (requestCode){
                case ADDRESS:
                    //CharSequence[] array = ((Map<CharSequence[],List<CharSequence[]>>)getIntent().getExtras().get(getString(R.string.send))).keySet().iterator().next();
                    CharSequence[] result = data.getStringArrayExtra(getString(R.string.send));
                    bodyMap.put("addressStr",closeAccount.toJsonString(result));
                    bodyMap.put("shopTransport",data.getBooleanExtra(getString(R.string.attach),false) ? "yes" : "no");
                    result[2] = ""+ result[2]+ result[3]+ result[4]+ result[5]+ result[6];
                    ((CloseAccountV) getTypeView()).updateConsignee(result);
                    deliveryCost(result[7],result[8]);
                    break;
                case ShopActivity.SHOPPINGcART:
                    /*if(data.getBooleanExtra(getString(R.string.send),false)) {
                        ((CloseAccountV) getTypeView()).replacePay(data.getFloatExtra(getString(R.string.attach), 0.0f));
                        shoppingCart.getList().get(data.getIntExtra(getString(R.string.position),0))[5] = getIntent().getIntExtra(getString(R.string.result),0) +"";
                    }else {
                        ((CloseAccountV) getTypeView()).addPay(data.getFloatExtra(getString(R.string.attach), 0.0f));
                        shoppingCart.getList().get(data.getIntExtra(getString(R.string.position),0))[5] = Integer.valueOf((String) shoppingCart.getList().get(data.getIntExtra(getString(R.string.position),0))[5])+getIntent().getIntExtra(getString(R.string.result),0)+"";
                    }*/
                    //getShoppingCartData();
                    break;
                case BOOKINGsEATS:
                    CharSequence[] bookingSeats = data.getCharSequenceArrayExtra(getString(R.string.send));
                    if(null != bookingSeats) {
                        bodyMap.put("name",(String)bookingSeats[0]);
                        bodyMap.put("phone",(String)bookingSeats[1]);
                        bodyMap.put("privateRoom",(String) bookingSeats[2]);
                        bookingSeats[2] = "yes".equals(bookingSeats[2]) ? getString(R.string.privateRoom):"";
                        ((CloseAccountV) getTypeView()).updateConsignee(bookingSeats);
                    }
                    break;
                case DISCOUNTcOUPON:
                    CharSequence[] discountCoupon = data.getCharSequenceArrayExtra(getString(R.string.send));
                    if(null != discountCoupon) {
                        bodyMap.put("couponId",(String) discountCoupon[3]);
                        ((CloseAccountV) getTypeView()).addFree(restCost - Float.valueOf((String) discountCoupon[0]));
                    }
                    break;
            }
    }
    private void deliveryCost(CharSequence longitude,CharSequence latitude){
        MultipartBody.Builder requestBody = new MultipartBody.Builder().setType(MultipartBody.FORM);
        requestBody.addFormDataPart("advanceOrderId",bodyMap.get("advanceOrderId")).addFormDataPart("lng",longitude.toString()).addFormDataPart("lat",latitude.toString()).addFormDataPart("shopTransport",bodyMap.get("shopTransport"));
        new OkHttpClient().newCall(new Request.Builder().url(getString(R.string.ip)+getString(R.string.deliveryCost)).post(requestBody.build()).build()).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {}

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                try {
                    JSONObject result = new JSONObject(response.body().string());
                    Log.i("CloseAccountActivity", "deliveryCost->" + result);
                    if (200 == result.getInt("statusCode"))
                        ((CloseAccountV)getTypeView()).setFreight((float) result.getDouble("deliveryfee"),(float)result.getDouble("distance"),(float)result.getDouble("UserActualFee"));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }
    public View.OnClickListener getClickListener(){
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (v.getId()){
                    case R.id.image_goBack:
                        goBack();
                        break;
                    case R.id.text_homeDelivery:
                        ((CloseAccountV)getTypeView()).selected(true);
                        ((CloseAccountV) getTypeView()).addFree(restCost);
                        ((CloseAccountV) getTypeView()).updateConsignee(closeAccount.mergetAddress());
                        break;
                    case R.id.text_toShop:
                        ((CloseAccountV)getTypeView()).selected(false);
                        ((CloseAccountV) getTypeView()).addFree(-restCost);
                        CharSequence[] array = new CharSequence[3];
                        array[0] = null == bodyMap.get("name") ? "" : bodyMap.get("name");
                        array[1] = null == bodyMap.get("phone") ? "" : bodyMap.get("phone");
                        array[2] = null == bodyMap.get("privateRoom") ? "" : bodyMap.get("privateRoom");
                        array[2] = "yes".equals(array[2]) ? getString(R.string.privateRoom) : "";
                        ((CloseAccountV) getTypeView()).updateConsignee(array);
                        break;
                    case R.id.text_linkmanContacts:
                        if(!((CloseAccountV)getTypeView()).isClickable()) {
                            Intent target = new Intent(CloseAccountActivity.this, ReceiptAddressActivity.class);
                            LinkedHashMap<String, List<CharSequence[]>> map = new LinkedHashMap<>();
                            map.put(getString(R.string.send), closeAccount.getTarget());
                            Bundle bundle = new Bundle();
                            bundle.putSerializable(getString(R.string.send), map);
                            target.putExtras(bundle);
                            startActivityForResult(target, ADDRESS);
                        }else
                            startActivityForResult(new Intent(CloseAccountActivity.this, BookingSeatsActivity.class),BOOKINGsEATS);
                        break;
                    case R.id.text_deliveryTime:
                        final DatePickerPopupWindow pickerPopupWindow = new DatePickerPopupWindow(CloseAccountActivity.this);
                        pickerPopupWindow.setSumbit(new DatePickerPopupWindow.Sumbit(){
                            @Override
                            public void commit(int year, int month, int day, int hour, int minute, int second) {
                                bodyMap.put("time",new SimpleDateFormat("yyyy-MM-dd HH:mm : ss").format(new GregorianCalendar(year,month,day,hour,minute,second).getTime()));
                                ((CloseAccountV)getTypeView()).updateDeliveryTime(bodyMap.get("time"));
                                pickerPopupWindow.dismiss();
                            }
                        });
                        pickerPopupWindow.displayPopupWindow();
                        break;
                    case R.id.text_shopName:
                        Intent shopInfoIntent = new Intent(CloseAccountActivity.this,MerchantParticularsActivity.class);
                        Bundle particulars = new Bundle();
                        particulars.putCharSequenceArray(getString(R.string.send),((Map<CharSequence[],List<CharSequence[]>>)getIntent().getExtras().get(getString(R.string.send))).keySet().iterator().next());
                        particulars.putSerializable(getString(R.string.sendBody),(HashMap<List<CharSequence[]>, List<CharSequence[]>>)getIntent().getExtras().get(getString(R.string.sendBody)));
                        shopInfoIntent.putExtras(particulars);
                        startActivity(shopInfoIntent);
                        break;
                    case R.id.text_discountCoupon:
                        Intent dcIntent = new Intent(CloseAccountActivity.this,DiscountCouponActivity.class);
                        HashMap<List<CharSequence[]>,List<CharSequence[]>> hashMap = new HashMap<>();
                        hashMap.put(list,null);
                        dcIntent.putExtra(getString(R.string.send),hashMap);
                        startActivityForResult(dcIntent,DISCOUNTcOUPON);
                        break;
                    case R.id.text_freeShipping:
                        Map<String,List<CharSequence[]>> map1 = (HashMap<String,List<CharSequence[]>>)getIntent().getExtras().get(getString(R.string.sendTail));
                        new AddressPopupWindow(CloseAccountActivity.this,map1.get(map1.keySet().iterator().next()) , new AddressPopupWindow.AddressListener() {
                            @Override
                            public void setAddress(CharSequence[] array) {
                                Intent intent = new Intent(CloseAccountActivity.this,AddRegionActivity.class);
                                intent.putExtra(getString(R.string.send),array);
                                startActivityForResult(intent,ADDRESS);
                            }
                        }).displayPopupWindow();
                        break;
                    case R.id.text_pay:
                        bodyMap.put("comment",((CloseAccountV)getTypeView()).getRemark());
                        Iterator iterator = bodyMap.keySet().iterator();
                        if(((CloseAccountV) getTypeView()).isClickable() && (null == bodyMap.get("name") || "".equals(bodyMap.get("name"))))
                            Toast.makeText(CloseAccountActivity.this,getString(R.string.nameError),Toast.LENGTH_SHORT).show();
                        else if(((CloseAccountV) getTypeView()).isClickable() && (null == bodyMap.get("phone") || "".equals(bodyMap.get("phone"))))
                            Toast.makeText(CloseAccountActivity.this,getString(R.string.phoneError),Toast.LENGTH_SHORT).show();
                        else if(((CloseAccountV) getTypeView()).isClickable() && (null == bodyMap.get("time") || "".equals(bodyMap.get("time"))))
                            Toast.makeText(CloseAccountActivity.this,getString(R.string.reservationTimeError),Toast.LENGTH_SHORT).show();
                        else {
                            MultipartBody.Builder requestBody = new MultipartBody.Builder().setType(MultipartBody.FORM);
                            while (iterator.hasNext()) {
                                String key = (String) iterator.next();
                                requestBody.addFormDataPart(key, bodyMap.get(key));
                            }
                            new OkHttpClient().newCall(new Request.Builder().url(getString(R.string.ip) + getString(((CloseAccountV) getTypeView()).isClickable() ? R.string.reserve : R.string.delivery)).
                                    post(requestBody.build()).build()).enqueue(new Callback() {
                                @Override
                                public void onFailure(Call call, IOException e) {}

                                @Override
                                public void onResponse(Call call, Response response) throws IOException {
                                    try {
                                        JSONObject result = new JSONObject(response.body().string());
                                        Log.i("CloseAccountActivity", "onCreate->" + result);
                                        if (200 == result.getInt("statusCode")) {
                                            Map<String,String> map = new HashMap<String, String>();
                                            map.put("orderId",bodyMap.get("advanceOrderId"));
                                            map.put("orderIds",bodyMap.get("advanceOrderId"));
                                            map.put("way","alipayofzshuser");
                                            ((CloseAccountV) getTypeView()).payPopupWindow(map);
                                        }else
                                            getTypeView().hintHoast(R.string.inputIncomplete, null, Toast.LENGTH_SHORT);
                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }
                                }
                            });
                        }
                        break;
                }
            }
        };
    }
    public CommodityAdapter.ItemOnClickListener getInfoListener(){
        return new CommodityAdapter.ItemOnClickListener() {
            @Override
            public int onClickAdd(View itemView, CharSequence[] array) {
                submit(itemView.getId(),array,"add");
                return 0;
            }

            @Override
            public int onClickReduce(View itemView, CharSequence[] array) {
                submit(itemView.getId(),array,"minus");
                return 0;
            }
        };
    }
}
