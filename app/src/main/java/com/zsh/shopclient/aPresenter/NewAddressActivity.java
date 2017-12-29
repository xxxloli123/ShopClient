package com.zsh.shopclient.aPresenter;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.Parcelable;
import android.provider.ContactsContract;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.amap.api.location.AMapLocation;
import com.amap.api.services.core.LatLonPoint;
import com.amap.api.services.core.PoiItem;
import com.amap.api.services.geocoder.RegeocodeAddress;
import com.xxx.skynet.sharedPreference.IOSharedPreferences;
import com.zsh.shopclient.R;
import com.zsh.shopclient.adapter.SelectStreetAdapter;
import com.zsh.shopclient.interfaceconfig.Config;
import com.zsh.shopclient.model.AddressEntity;
import com.zsh.shopclient.tool.SimpleCallback;
import com.zsh.shopclient.view.CheckLinearLayout;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Locale;

import okhttp3.Call;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**     AddressStreetEntity
 * Created by Administrator on 2017/1/23 0023.
 * <p>
 * TODO 添加收货地址页面
 */

public class NewAddressActivity extends BaseActivity2 implements View.OnClickListener {
    private EditText houseNumberEdit;
    private RelativeLayout go_back, delete;
    private EditText name, phone, addressEdit, houseNum;
    private TextView address;
    private LinearLayout address_layout;
    private Button ensure;
    private LinearLayout add_book;
    private RadioGroup radioGroup, radioGroup1;
    private String sex = "";//记录选择的是男是女
    private String addressed = "";//记录选择的地址
    private RegeocodeAddress resAddr;
    private LatLonPoint point;
    public static final String ADDRESS = "addr";
    private AddressEntity addr;
    private CheckLinearLayout checkable;
    private TextView street;
    private JSONArray streets;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addr = getIntent().getParcelableExtra(ADDRESS);
        setContentView(R.layout.activity_new_address);
        initView();
        initData1();
    }

    public static String sHA1(Context context) {
        try {
            PackageInfo info = context.getPackageManager().getPackageInfo(
                    context.getPackageName(), PackageManager.GET_SIGNATURES);
            byte[] cert = info.signatures[0].toByteArray();
            MessageDigest md = MessageDigest.getInstance("SHA1");
            byte[] publicKey = md.digest(cert);
            StringBuffer hexString = new StringBuffer();
            for (int i = 0; i < publicKey.length; i++) {
                String appendString = Integer.toHexString(0xFF & publicKey[i])
                        .toUpperCase(Locale.US);
                if (appendString.length() == 1)
                    hexString.append("0");
                hexString.append(appendString);
                hexString.append(":");
            }
            String result = hexString.toString();
            return result.substring(0, result.length() - 1);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 初始化控件
     */
    private void initView() {
        go_back = (RelativeLayout) findViewById(R.id.back_rl);
        go_back.setOnClickListener(this);
        name = (EditText) findViewById(R.id.name_edit);
        phone = (EditText) findViewById(R.id.phone_edit);
        addressEdit = (EditText) findViewById(R.id.address_edit);
        address = (TextView) findViewById(R.id.address);
        address_layout = (LinearLayout) findViewById(R.id.address_layout);
        address_layout.setOnClickListener(this);
        ensure = (Button) findViewById(R.id.ensure_bt);
        ensure.setOnClickListener(this);
        add_book = (LinearLayout) findViewById(R.id.add_phone_book_layout);
        add_book.setOnClickListener(this);
        houseNumberEdit = (EditText) findViewById(R.id.addr_des);
        houseNum = (EditText) findViewById(R.id.house_number_edit);
        radioGroup = (RadioGroup) findViewById(R.id.radioGroup);
        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId) {
                    case R.id.sir_text:
                        sex = "男";
                        break;
                    case R.id.madam_text:
                        sex = "女";
                        break;
                }
            }
        });
        radioGroup1 = (RadioGroup) findViewById(R.id.radioGroup1);
        radioGroup1.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId) {
                    case R.id.home_text:
                        break;
                    case R.id.company_text:
                        break;
                    case R.id.school_text:
                        break;
                }
            }
        });

        checkable = findViewById(R.id.addr_defult);
        checkable.setCanCheck(true);
        checkable.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkable.toggle();
            }
        });
        if (addr != null) checkable.setChecked(addr.isDefault());
        street= (TextView) findViewById(R.id.street);
    }

    @Override
    public void onClick(View v) {
        Intent intent = null;
        switch (v.getId()) {
            case R.id.back_rl:
                finish();
                break;
            case R.id.address_layout:   //  定位
                intent = new Intent(this, EditAddressActivity.class);
                intent.putExtra("latlonPoint", point);
                startActivityForResult(intent, 1);
                break;
            case R.id.ensure_bt:        // 提交
                submit();
                break;
            case R.id.add_phone_book_layout://通讯录
                intent = new Intent(Intent.ACTION_PICK,
                        ContactsContract.Contacts.CONTENT_URI);
                startActivityForResult(intent, 2);
                break;
            case R.id.addr_defult:
                break;
            case R.id.street:
                if (streets==null){
                    Toast.makeText(this, "请选择地址", Toast.LENGTH_SHORT).show();
                    return;
                }
                selectStreetDialog();
                break;
        }
    }

    private void initData1() {
        if (addr == null) return;
        Toast.makeText(NewAddressActivity.this, "收货地址不能为空", Toast.LENGTH_SHORT).show();
        name.setText(addr.getPersonname());
        phone.setText(addr.getPersonphone());
        address.setText(addr.getPro());
        address.append(" ");
        address.append(addr.getCity());
        address.append(" ");
        address.append(addr.getDistrict());
        street.setText(addr.getStreet());
        Log.e("定位",""+addr.getLat()+addr.getLng());
        houseNumberEdit.setText(addr.getHouseNumber());
        point = new LatLonPoint(addr.getLat(), addr.getLng());


    }

    /**
     * 提交收货地址
     */
    private void submit() {
        String uName = name.getText().toString().trim();
        String uPhone = phone.getText().toString().trim();
        String uAddrGps = address.getText().toString().trim();
        String uAddr = addressEdit.getText().toString().trim();
        String house = houseNumberEdit.getText().toString().trim();
        if (TextUtils.isEmpty(uName)) {
            Toast.makeText(this, "用户名不能为空", Toast.LENGTH_SHORT).show();
            return;
        }
        if (TextUtils.isEmpty(uPhone)) {
            Toast.makeText(this, "手机号不能为空", Toast.LENGTH_SHORT).show();
            return;
        }
        if (isEmpty(street.getText())){
            Toast.makeText(this, "请选择区域", Toast.LENGTH_SHORT).show();
            return;
        }
        if (TextUtils.isEmpty(uAddrGps)) {
            Toast.makeText(this, "收货地址不能为空", Toast.LENGTH_SHORT).show();
            return;
        }
//        if (poiItem == null) {
//            Toast.makeText(this, "请使用地图定位大致地址", Toast.LENGTH_SHORT).show();
//            return;
//        }
        JSONObject addr = new JSONObject();

//          addressStr： pro 省 , city 市 , district 区县, street 街道, houseNumber 门牌号, lat 纬度 , lng 经度,
//          personname 收货人姓名, personphone 收货人电话 defaultAddress:是否默认[yes/no]

        try {
            String url;
            if (resAddr != null) {

                if (this.addr == null)
                    url = Config.Url.getUrl(Config.NEWADDR);
                else {
                    url = Config.Url.getUrl(Config.EDITADDR);
                    addr.put("id", this.addr.getId());
                }
                addr.put("pro", resAddr.getProvince());
                addr.put("city", resAddr.getCity());
                addr.put("district", resAddr.getDistrict());
                addr.put("street", street.getText());
                addr.put("lat", point.getLatitude());
                addr.put("lng", point.getLongitude());
            } else {
                url = Config.Url.getUrl(Config.EDITADDR);
                addr.put("id", this.addr.getId());
                addr.put("pro", this.addr.getPro());
                addr.put("city", this.addr.getCity());
                addr.put("district", this.addr.getDistrict());
                addr.put("street", this.addr.getStreet());
                addr.put("lat", this.addr.getLat());
                addr.put("lng", this.addr.getLng());
            }
            addr.put("defaultAddress", checkable.isChecked() ? "yes" : "no");
            addr.put("houseNumber", (house + houseNum.getText().toString()).replace("null",""));
            addr.put("personname", uName);
            addr.put("personphone", uPhone);
            RequestBody requestBody = new MultipartBody.Builder().setType(MultipartBody.FORM)
                    .addFormDataPart("userId", IOSharedPreferences.inString(this,getString(R.string.user),getString(R.string.userId)))
                    .addFormDataPart("addressStr", addr.toString()).build();
            Request request = new Request.Builder().url(url)
                    .post(requestBody).build();
            new OkHttpClient().newCall(request).enqueue(this);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }


    /**
     * 接收回传过来的值
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode != Activity.RESULT_OK) return;
        if (requestCode == 1) {
            if (data == null || data.getExtras() == null) return;
            Parcelable parcelable = data.getParcelableExtra("addr");
            if (parcelable instanceof AMapLocation) {
                AMapLocation location = (AMapLocation) parcelable;
                resAddr = new RegeocodeAddress();
                resAddr.setProvince(location.getProvince());
                resAddr.setCity(location.getCity());
                resAddr.setDistrict(location.getDistrict());
                resAddr.setTownship(location.getStreet() + location.getStreetNum());
                this.point = new LatLonPoint(location.getLatitude(), location.getLongitude());
                Log.e("定位1",""+point.getLatitude()+point.getLongitude());
                StringBuilder sb = new StringBuilder();
                sb.append(resAddr.getProvince())
                        .append(" ")
                        .append(resAddr.getCity())
                        .append(" ")
                        .append(resAddr.getDistrict());
                address.setText(sb.toString());
                houseNumberEdit.setText(resAddr.getTownship());
                getStreetData(resAddr);

            } else {
                PoiItem item = (PoiItem) parcelable;
                resAddr = new RegeocodeAddress();
                resAddr.setProvince(item.getProvinceName());
                resAddr.setCity(item.getCityName());
                resAddr.setDistrict(item.getAdName());
                resAddr.setTownship(item.getSnippet());
                point = item.getLatLonPoint();
                Log.e("定位1",""+point.getLatitude()+point.getLongitude());
                StringBuilder sb = new StringBuilder();
                sb.append(resAddr.getProvince())
                        .append(" ")
                        .append(resAddr.getCity())
                        .append(" ")
                        .append(resAddr.getDistrict());
                address.setText(sb.toString());
                houseNumberEdit.setText(item.getTitle());
                getStreetData(resAddr);
            }
            if (resAddr == null) return;
        } else if (requestCode == 2) {      // 选择联系人Uri
            if (data == null || data.getExtras() == null) {
                Toast.makeText(this, "未获取到联系人信息", Toast.LENGTH_SHORT).show();
                return;
            }
            Uri contactData = data.getData();
            Cursor cursor = managedQuery(contactData, null, null, null, null);
            cursor.moveToFirst();
            String contactId = cursor.getString(cursor
                    .getColumnIndex(ContactsContract.Contacts._ID));
            // 获得DATA表中的电话号码，条件为联系人ID,因为手机号码可能会有多个
            Cursor phone = getContentResolver().query(
                    ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null,
                    ContactsContract.CommonDataKinds.Phone.CONTACT_ID + " = "
                            + contactId, null, null);
            if (phone.moveToNext()) {
                String usernumber = phone
                        .getString(phone
                                .getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
                this.phone.setText(usernumber.replace(" ", "").replace("-", ""));
            }
        }
    }

    @Override
    protected void onFail(Call call, IOException e) {
        super.onFail(call, e);
        if (addr == null)
            Toast.makeText(this, "添加失败", Toast.LENGTH_SHORT).show();
        else Toast.makeText(this, "修改失败", Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void onSuccess(Call call, Response response, JSONObject json) throws JSONException {
        if (addr == null)
            Toast.makeText(this, "添加成功", Toast.LENGTH_SHORT).show();
        else Toast.makeText(this, "修改成功", Toast.LENGTH_SHORT).show();
        super.onSuccess(call, response, json);
        setResult(Activity.RESULT_OK);
        finish();
    }

    @Override
    public void finish() {
        setResult(Activity.RESULT_OK);
        super.finish();
    }
    private void getStreetData(RegeocodeAddress resAddr){
        RequestBody requestBody2 = new MultipartBody.Builder().setType(MultipartBody.FORM)
                .addFormDataPart("pro",resAddr.getProvince())
                .addFormDataPart("city",resAddr.getCity())
                .addFormDataPart("district",resAddr.getDistrict()).build();
        Request request = new Request.Builder().url(Config.Url.getUrl(Config.AddressStreet)).post(requestBody2).build();
        new OkHttpClient().newCall(request).enqueue(new SimpleCallback(this,true) {
            @Override
            public void onSuccess(String tag, JSONObject json) throws JSONException {
                streets=json.getJSONArray("tarifflist");
            }
        });
    }
    /**
     * 选择区域dialog窗口
     */
    private void selectStreetDialog() {
        LayoutInflater inflater = LayoutInflater.from(NewAddressActivity.this);
        View mDialog = inflater.inflate(R.layout.dialog_select_area, null);
        final Dialog areaDialog = new AlertDialog.Builder(NewAddressActivity.this, R.style.DialogStyle).create();
        View delete = mDialog.findViewById(R.id.back_rl);
        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                areaDialog.dismiss();
            }
        });
        final SelectStreetAdapter adapter = new SelectStreetAdapter(NewAddressActivity.this, streets);
        ListView listView = (ListView) mDialog.findViewById(R.id.listview);
        listView.setAdapter(adapter);
        listView.setItemsCanFocus(false);
        areaDialog.show();
        areaDialog.setContentView(mDialog);
//        if (selectedPosition != -1) listView.setItemChecked(selectedPosition, true);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                areaDialog.dismiss();
                try {
//                    selectedPosition = position;
                    JSONObject addr = streets.getJSONObject(position);
                    street.setText(addr.getString("startStreet"));
                } catch (JSONException e) {
                }
            }
        });
    }
}
