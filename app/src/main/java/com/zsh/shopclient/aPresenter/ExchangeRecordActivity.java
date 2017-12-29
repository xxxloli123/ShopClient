package com.zsh.shopclient.aPresenter;

import android.widget.ListView;

import com.google.gson.Gson;
import com.xxx.skynet.sharedPreference.IOSharedPreferences;
import com.zsh.shopclient.R;
import com.zsh.shopclient.adapter.ExchangeRecordAdapter;
import com.zsh.shopclient.interfaceconfig.Config;
import com.zsh.shopclient.model.Record;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.OnClick;

public class ExchangeRecordActivity extends BaseActivity1 {

    @BindView(R.id.record_list)
    ListView recordList;
    private ArrayList<Record> records;

    @Override
    protected void init() {
        Map<String, Object> map1 = new HashMap<>();
        map1.put("userId", IOSharedPreferences.inString(this,getString(R.string.user),getString(R.string.userId)));
        newCall(Config.Url.getUrl(Config.GET_ExchangeRecord), map1);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_exchange_record;
    }

    @Override
    public void onSuccess(Object tag, JSONObject json) throws JSONException {
        JSONArray arr = json.getJSONArray("listorder");
        if (arr.length() == 0) return;
        records = new ArrayList<>();
        Gson gson = new Gson();
        for (int i = 0; i < arr.length(); i++) {
            records.add(gson.fromJson(arr.getString(i), Record.class));
        }
        ExchangeRecordAdapter sbCommodityAdapter = new ExchangeRecordAdapter(this, records);
        recordList.setAdapter(sbCommodityAdapter);
    }

    @OnClick(R.id.back_rl)
    public void onViewClicked() {
        finish();
    }
}
