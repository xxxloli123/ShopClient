package com.zsh.shopclient.aPresenter;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.text.TextUtils;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import com.google.gson.Gson;
import com.xxx.skynet.sharedPreference.IOSharedPreferences;
import com.zsh.shopclient.R;
import com.zsh.shopclient.interfaceconfig.Config;
import com.zsh.shopclient.model.AddressEntity;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * Created by Administrator on 2017/1/23 0023.
 */

public class ReceiptAddressActivity extends BaseActivity2 implements View.OnClickListener,
        SwipeRefreshLayout.OnRefreshListener, AdapterView.OnItemClickListener {

    private RelativeLayout go_back;
    private TextView add_address;
    private ListView addressListview;
    private AddressListviewAdapter adapter;
    private SwipeRefreshLayout refreshLayout;
    private List<AddressEntity> addrs = new ArrayList<>();
    public static final String SELECTED_ADDRESS = "address";
    public static final String STARTED_ADDRESS = "startedAddress";
    public static final String START_ADDRESS = "START_ADDRESS";
    public static final String SELECTED_ITEM = "SELECTED_ITEM";
    public static final String SELECT_ADDRESS = "SELECT_ADDRESS";
    private int selectedPosition = -1;
    private String selectedItemId;
    private boolean signChioce;
    private boolean startChioce;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_receipt_address);
        selectedItemId = getIntent().getStringExtra(SELECTED_ITEM);
        signChioce = getIntent().getBooleanExtra(SELECT_ADDRESS, false);
        startChioce=getIntent().getBooleanExtra(START_ADDRESS, false);
        initView();
        if (signChioce){
            adapter = new AddressListviewAdapter(this, null, signChioce);
        }else {
            adapter = new AddressListviewAdapter(this, null, signChioce);
        }
        if (startChioce){
            adapter = new AddressListviewAdapter(this, null, startChioce);
        }else {
            adapter = new AddressListviewAdapter(this, null, startChioce);
        }
        addressListview.setAdapter(adapter);
        onRefresh();
    }

    /**
     * 初始化控件
     */
    private void initView() {
        go_back = (RelativeLayout) findViewById(R.id.back_rl);
        go_back.setOnClickListener(this);
        add_address = (TextView) findViewById(R.id.add_address_text);
        add_address.setOnClickListener(this);
        addressListview = (ListView) findViewById(R.id.address_listview);
        refreshLayout = findView(R.id.srl);
        refreshLayout.setOnRefreshListener(this);
        if (!isEmpty(SELECTED_ITEM)) {
            addressListview.setItemsCanFocus(false);
            addressListview.setChoiceMode(AbsListView.CHOICE_MODE_SINGLE);
            addressListview.setOnItemClickListener(this);
        }
    }

    /**
     * listview数据
     */
    private void getAddressInformation() {

        RequestBody requestBody = new MultipartBody.Builder().setType(MultipartBody.FORM)
                .addFormDataPart("userId", IOSharedPreferences.inString(this,getString(R.string.user),getString(R.string.userId))).build();
        final Request request = new Request.Builder()
                .url(Config.Url.getUrl(Config.ADDRLIST)).tag("get").post(requestBody).build();
        new OkHttpClient().newCall(request).enqueue(this);
    }

    @Override
    public void onClick(View v) {
        Intent intent = null;
        switch (v.getId()) {
            case R.id.back_rl:
                finish();
                break;
            //  添加收货地址
            case R.id.add_address_text:
                intent = new Intent(this, NewAddressActivity.class);
                startActivityForResult(intent, 20003);
                break;
        }
    }

    @Override
    protected void onSuccess(Call call, Response response, JSONObject json) throws JSONException {
        super.onSuccess(call, response, json);
        switch (response.request().tag().toString()) {
            case "del":
                onRefresh();
                break;
            case "get":
                JSONArray arr = json.getJSONArray("address");
                addrs.clear();
                Gson gson = new Gson();
                for (int i = 0; i < arr.length(); i++) {
                    addrs.add(gson.fromJson(arr.getString(i), AddressEntity.class));
                }
                adapter.notifyDataSetChanged(addrs);
                if (selectedPosition == -1) {
                    for (int i = 0; i < addrs.size(); i++) {
                        final AddressEntity ae = addrs.get(i);
                        if (!isEmpty(selectedItemId)) {
                            if (TextUtils.equals(ae.getId(), selectedItemId)) {
                                addressListview.setItemChecked(i, true);
                                break;
                            }
                        } else if (ae.isDefault()) {
                            addressListview.setItemChecked(i, true);
                            break;
                        }
                    }
                }
                if (refreshLayout.isRefreshing()) refreshLayout.setRefreshing(false);
                break;
        }
    }

    @Override
    protected void onFail(Call call, IOException e) {
        super.onFail(call, e);
        switch (call.request().tag().toString()) {
            case "get":
                refreshLayout.setRefreshing(false);
                Toast.makeText(this, "获取失败", Toast.LENGTH_SHORT).show();
                break;
            case "del":
                Toast.makeText(this, "删除失败", Toast.LENGTH_SHORT).show();
                break;
        }
        if (refreshLayout.isRefreshing()) refreshLayout.setRefreshing(false);
    }

    @Override
    public void onRefresh() {
        getAddressInformation();
    }

    public void delAddr(AddressEntity addressEntity) {
        RequestBody requestBody = new MultipartBody.Builder().setType(MultipartBody.FORM)
                .addFormDataPart("addressId", addressEntity.getId()).build();
        final Request request = new Request.Builder()
                .url(Config.Url.getUrl(Config.DELADDR)).tag("del").post(requestBody).build();
        new OkHttpClient().newCall(request).enqueue(this);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        addressListview.setItemChecked(selectedPosition, false);
        selectedPosition = position;
        addressListview.setItemChecked(selectedPosition, true);
        if (signChioce) {
            Intent intent = new Intent();
            AddressEntity ae = adapter.getItem(selectedPosition);
            intent.putExtra(SELECTED_ADDRESS, ae);
            setResult(Activity.RESULT_OK, intent);
            finish();
        }
        if (startChioce){
            Intent intent = new Intent();
            AddressEntity ae = adapter.getItem(selectedPosition);
            intent.putExtra(STARTED_ADDRESS, ae);
            setResult(Activity.RESULT_OK, intent);
            finish();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == Activity.RESULT_OK) {
            onRefresh();
        }
        super.onActivityResult(requestCode, resultCode, data);
    }
}
