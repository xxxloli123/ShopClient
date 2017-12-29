package com.zsh.shopclient.aPresenter;

import android.content.res.TypedArray;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.AbsoluteSizeSpan;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.amap.api.location.AMapLocation;
import com.zsh.shopclient.R;
import com.zsh.shopclient.aView.BaseView;
import com.zsh.shopclient.aView.SpeedyV;
import com.zsh.shopclient.adapter.TextAdapter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by Administrator on 2017/11/13.
 */

public class SpeedyActivity extends BaseActivity {
    private int page;
    private List<CharSequence[]> list;
    private LinkedHashMap<String, List<CharSequence[]>> listLinkedHashMap;
    private CharSequence operation(String original,String str){
        SpannableString ss = new SpannableString(original+"("+str+")");
        ss.setSpan(new AbsoluteSizeSpan((int)getResources().getDimension(R.dimen.size16)),0,original.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        return ss;
    }
    private void analysisShops(JSONArray jsonArray) throws JSONException{
        for(int index = 0;index < jsonArray.length();index++){
            JSONObject jsonObject = jsonArray.getJSONObject(index).getJSONObject("shop");
            list.add(new CharSequence[]{jsonObject.getString("shopImage"),jsonObject.getString("shopName"), jsonObject.getString("lineConsume"),
                    jsonObject.getString("delivery"),jsonObject.getString("deliveryFee"),jsonObject.getString("grade"), jsonObject.getString("ordercount"),
                    jsonObject.getString("lng"),jsonObject.getString("lat"),jsonObject.getString("id"), jsonObject.getString("shopUserDistance")});
            List<CharSequence[]> arrayList = new ArrayList<>();
            JSONArray jsonA = jsonArray.getJSONObject(index).getJSONArray("activity");
            for(int i = 0;i< jsonA.length();i++){
                JSONObject jsonObj = jsonA.getJSONObject(i);
                arrayList.add(new String[]{jsonObj.getString("activityName")});
            }
            listLinkedHashMap.put((String) list.get(index)[0],arrayList);
        }
        ((SpeedyV)getTypeView()).update(list,listLinkedHashMap);
    }
    @Override
    protected BaseView createTypeView() {
        return new SpeedyV();
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        page = 0;
        list = new ArrayList<CharSequence[]>();
        listLinkedHashMap = new LinkedHashMap<String, List<CharSequence[]>>();
        getIntent().putExtra(getString(R.string.requestParam),"GeneralGoods");
        CharSequence[] charSequence = getIntent().getCharSequenceArrayExtra(getString(R.string.title));
        AMapLocation location = (AMapLocation) getIntent().getExtras().get(getString(R.string.location));
        MultipartBody.Builder requestBody = new MultipartBody.Builder().setType(MultipartBody.FORM);
        requestBody.addFormDataPart("type",(String) charSequence[2]).addFormDataPart("pro",location.getProvince()).addFormDataPart("city",location.getCity()).
                addFormDataPart("district",location.getDistrict()).addFormDataPart("lng",""+location.getLongitude()).addFormDataPart("lat",""+location.getLatitude());
        new OkHttpClient().newCall(new Request.Builder().url(getString(R.string.ip)+getString(R.string.shopUrl)).post(requestBody.build()).build()).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
                getTypeView().hintHoast(R.string.getDataDefeated,null,Toast.LENGTH_SHORT);
                ((SpeedyV)getTypeView()).update(null,null);
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                try {
                    JSONObject result = new JSONObject(response.body().string());
                    Log.i("SpeedyActivity", "onCreate-onResponse->" + result);
                    //if (200 == result.getInt("statusCode")) {
                        List<CharSequence[]> list = new ArrayList<CharSequence[]>();
                        JSONArray jsonArray = result.getJSONArray("listclass");
                        for(int index = 0;index< jsonArray.length();index++) {
                            JSONObject jsonObject = jsonArray.getJSONObject(index);
                            list.add(new String[]{jsonObject.getString("genericClassName"),jsonObject.getString("id"),jsonObject.getString("grade"),
                                    jsonObject.getString("shopgrade"),jsonObject.getString("genericClassName"),jsonObject.getString("sort"),jsonObject.getString("fatherId")});
                        }
                        ((SpeedyV)getTypeView()).initClassList(list);
                        analysisShops(result.getJSONArray("listshop"));
                    //}
                } catch (JSONException e) {
                    e.printStackTrace();
                    getTypeView().hintHoast(R.string.parseFailure,null,Toast.LENGTH_SHORT);
                    ((SpeedyV)getTypeView()).update(null,null);
                }
            }
        });
    }
    public List<CharSequence[]> initRecyclerWay(){
        String resource = "android.resource://"+getPackageName()+"/";
        List<CharSequence[]> list = new ArrayList<>();
        TypedArray icons = getResources().obtainTypedArray(R.array.wayIcons);
        TypedArray titles = getResources().obtainTypedArray(R.array.wayTitles);
        if(null != icons && null != titles) {
            int iconsLen = icons.length();
            int titlesLen = titles.length();
            int lenght =  iconsLen <= titlesLen ? iconsLen : titlesLen;
            for (int index = 0; index < lenght; index++)
                list.add(new String[]{resource +icons.getResourceId(index, 0), getString(titles.getResourceId(index, 0))});
        }
        return list;
    }
    public View.OnClickListener getListener(){
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (v.getId()){
                    case R.id.image_goBack:
                        finish();
                        break;
                    case R.id.text_consumptionWay:
                        ((SpeedyV)getTypeView()).pitchOn(0);
                        break;
                }
            }
        };
    }
    public TextAdapter.ItemOnClickListener getClassItemListener(){
        return new TextAdapter.ItemOnClickListener(){
            @Override
            public void itemOnClick(View view) {
                AMapLocation location = (AMapLocation) getIntent().getExtras().get(getString(R.string.location));
                MultipartBody.Builder requestBody = new MultipartBody.Builder().setType(MultipartBody.FORM);
                requestBody.addFormDataPart("condition","").addFormDataPart("classId",((SpeedyV)getTypeView()).getRclassAdapter().getItemCharSequence(1).toString())
                        .addFormDataPart("pro",location.getProvince()).addFormDataPart("city",location.getCity()).addFormDataPart("district",location.getDistrict()).
                        addFormDataPart("lng","" + location.getLongitude()).addFormDataPart("lat","" + location.getLatitude());
                new OkHttpClient().newCall(new Request.Builder().url(getString(R.string.ip)+getString(R.string.selectQuery)).post(requestBody.build()).build()).enqueue(new Callback() {
                    @Override
                    public void onFailure(Call call, IOException e) {
                        e.printStackTrace();
                        getTypeView().hintHoast(R.string.getDataDefeated,null,Toast.LENGTH_SHORT);
                    }

                    @Override
                    public void onResponse(Call call, Response response) throws IOException {
                        try {
                            JSONObject result = new JSONObject(response.body().string());
                            Log.i("SpeedyActivity","getClassItemListener->"+result);
                            list.clear();
                            analysisShops(result.getJSONArray("applistshop"));
                        } catch (JSONException e) {
                            e.printStackTrace();
                            getTypeView().hintHoast(R.string.parseFailure,null,Toast.LENGTH_SHORT);
                        }
                    }
                });
            }
        };
    }
    public TextAdapter.ItemOnClickListener getWayItemListener(){
        return new TextAdapter.ItemOnClickListener(){
            @Override
            public void itemOnClick(View view) {
                int  index = getString(R.string.toShop).equals(((CharSequence[])view.getTag())[1].toString())?2 :3;
                List<CharSequence[]> selectList = new ArrayList<>();
                for(CharSequence[] array : list)
                    if(null != array && "yes".equals(array[index]))
                        selectList.add(array);
                ((SpeedyV)getTypeView()).dismissPopupWindow(selectList,listLinkedHashMap);
            }
        };
    }
    public SwipeRefreshLayout.OnRefreshListener getUpdateListener(){
        return new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                ((SpeedyV)getTypeView()).updateFinish();
            }
        };
    }
}
