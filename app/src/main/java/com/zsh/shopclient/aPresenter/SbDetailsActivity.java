package com.zsh.shopclient.aPresenter;

import android.app.Activity;
import android.content.Intent;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.gson.Gson;
import com.xxx.skynet.sharedPreference.IOSharedPreferences;
import com.zsh.shopclient.R;
import com.zsh.shopclient.interfaceconfig.Config;
import com.zsh.shopclient.model.AddressEntity;
import com.zsh.shopclient.model.Intro;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.OnClick;

public class SbDetailsActivity extends BaseActivity1 {

    @BindView(R.id.name_tv)
    TextView nameTv;
    @BindView(R.id.img)
    ImageView img;
    @BindView(R.id.name1_tv)
    TextView name1Tv;
    @BindView(R.id.price_tv)
    TextView priceTv;
    @BindView(R.id.transportation_expense_tv)
    TextView transportationExpenseTv;
    @BindView(R.id.address_tv)
    TextView addressTv;
    @BindView(R.id.intro_tv)
    TextView introTv;

    private Intro intro;
    private AddressEntity address;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_sb_details;
    }

    @Override
    protected void init() {
        if (getIntent().getStringExtra("integralProductId")==null){
            Toast.makeText(this, "数据读取错误", Toast.LENGTH_SHORT).show();
            finish();
        }
        Map<String, Object> map1 = new HashMap<>();
        map1.put("integralProductId", getIntent().getStringExtra("integralProductId"));
        newCall(Config.Url.getUrl(Config.GET_SbIntro), map1);
    }

    @Override
    public void onSuccess(Object tag, JSONObject json) throws JSONException {
        if (tag.equals(Config.Commit_SbOrder)){
            Toast.makeText(this, "兑换成功", Toast.LENGTH_SHORT).show();
            finish();
        }
        intro=new Gson().fromJson(json.getString("IntegralProduct"),Intro.class);
        name1Tv.setText(intro.getProductName());
        nameTv.setText(intro.getProductName());
        priceTv.setText(intro.getSingleIntegral()+"");
        introTv.setText(intro.getDetails());
        Glide.with(this).load(Config.Url.getUrl(Config.IMG_Commodity) + intro.getSmallImg()).into(img);
    }

    @OnClick({R.id.back_rl, R.id.address_ll, R.id.commit_bt})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.back_rl:
                finish();
                break;
            case R.id.address_ll:
                Intent intent = new Intent(this, ReceiptAddressActivity.class);
                intent.putExtra(ReceiptAddressActivity.SELECT_ADDRESS, true);
                intent.putExtra(ReceiptAddressActivity.SELECTED_ITEM, address == null ? null : address.getId());
                startActivityForResult(intent, 10003);
                break;
            case R.id.commit_bt:
                commit();
                break;
        }
    }

    private void commit() {
//        用户Id userId 商品所需掌币数量 singleIntegral 商品名称 productName 商品id productId
//		 * 收件人姓名 receiverName 收件人电话 receiverPhone 收件人地址 省 endPro 收件人地址 市 endCity
//		 * 收件人 区县 endDistrict 收件人 街道 endStreet 收件人 详细地址 endHouseNumber
        if (address==null) Toast.makeText(this, "请选择收货地址", Toast.LENGTH_SHORT).show();
        else {
            Map<String, Object> map1 = new HashMap<>();
            JSONObject exchangeOrderStr = new JSONObject();
            try {
                exchangeOrderStr.put("userId", IOSharedPreferences.inString(this,getString(R.string.user),getString(R.string.userId)));
                exchangeOrderStr.put("singleIntegral", intro.getSingleIntegral());
                exchangeOrderStr.put("productName", intro.getProductName());
                exchangeOrderStr.put("productId", intro.getId());
                exchangeOrderStr.put("receiverName", address.getPersonname());
                exchangeOrderStr.put("receiverPhone", address.getPersonphone());
                exchangeOrderStr.put("endPro", address.getPro());
                exchangeOrderStr.put("endCity", address.getCity());
                exchangeOrderStr.put("endDistrict", address.getDistrict());
                exchangeOrderStr.put("endStreet", address.getStreet());
                exchangeOrderStr.put("endHouseNumber", address.getHouseNumber());
                map1.put("exchangeOrderStr",exchangeOrderStr);
                newCall(Config.Url.getUrl(Config.Commit_SbOrder), map1);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode != Activity.RESULT_OK)
            return;
        switch (requestCode) {
            case 10003:         //  发货地址
                address = data.getParcelableExtra(ReceiptAddressActivity.SELECTED_ADDRESS);
            addressTv.setText(address.getHouseNumber());
                break;
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

}
