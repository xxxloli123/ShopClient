package com.zsh.shopclient.aPresenter;

import android.content.Intent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.xxx.skynet.sharedPreference.IOSharedPreferences;
import com.zsh.shopclient.R;
import com.zsh.shopclient.adapter.SbCommodityAdapter;
import com.zsh.shopclient.interfaceconfig.Config;
import com.zsh.shopclient.model.SbCommodity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.OnClick;

public class SbShopActivity extends BaseActivity1 {

    @BindView(R.id.sb_tv)
    TextView sbTv;
    @BindView(R.id.commodity_list)
    GridView commodityList;

    private ArrayList<SbCommodity> sbCommodities;

    @Override
    protected void init() {
        Map<String, Object> map = new HashMap<>();
        map.put("","");
        newCall(Config.Url.getUrl(Config.GET_SbCommodity), map);
        Map<String, Object> map1 = new HashMap<>();
        map1.put("userId", IOSharedPreferences.inString(this,getString(R.string.user),getString(R.string.userId)));
        newCall(Config.Url.getUrl(Config.GET_Sb), map1);
    }

    @Override
    protected void initListener() {
        commodityList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent = new Intent(SbShopActivity.this, SbDetailsActivity.class);
                intent.putExtra("integralProductId", sbCommodities.get(i).getId());
                startActivity(intent);
            }
        });
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_sb_shop;
    }

    @Override
    public void onSuccess(Object tag, JSONObject json) throws JSONException {
        switch (tag.toString()) {
            case Config.GET_SbCommodity:
                JSONArray arr = json.getJSONArray("listproduct");
                if (arr.length() == 0) return;
                sbCommodities = new ArrayList<>();
                Gson gson = new Gson();
                for (int i = 0; i < arr.length(); i++) {
                    sbCommodities.add(gson.fromJson(arr.getString(i), SbCommodity.class));
                }
                SbCommodityAdapter sbCommodityAdapter = new SbCommodityAdapter(this, sbCommodities);
                commodityList.setAdapter(sbCommodityAdapter);
                break;
            case Config.GET_Sb:
                sbTv.setText(" "+json.getString("Integral"));
                break;
        }
    }

    @OnClick({R.id.back_rl, R.id.record_ll})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.back_rl:
                finish();
                break;
            case R.id.record_ll:
                startActivity(new Intent(this, ExchangeRecordActivity.class));
                break;
        }
    }

}
