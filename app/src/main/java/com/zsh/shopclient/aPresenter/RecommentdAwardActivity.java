package com.zsh.shopclient.aPresenter;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.DisplayUtil;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.common.BitMatrix;
import com.umeng.socialize.ShareAction;
import com.umeng.socialize.UMShareListener;
import com.umeng.socialize.bean.SHARE_MEDIA;
import com.umeng.socialize.media.UMImage;
import com.umeng.socialize.media.UMWeb;
import com.xxx.skynet.sharedPreference.IOSharedPreferences;
import com.zsh.shopclient.R;
import com.zsh.shopclient.interfaceconfig.Config;
import com.zsh.shopclient.tool.SimpleCallback;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileOutputStream;
import java.util.Hashtable;

import okhttp3.Callback;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;

/**
 * Created by Administrator on 2017/1/23 0023.
 */

public class RecommentdAwardActivity extends BaseActivity2 implements View.OnClickListener, UMShareListener {
    private RelativeLayout backRl, activeRule;
    private LinearLayout wechatLayout, qqLayout, scanCodeLayout, earningsLayout, successInvitedLayout, noInviteLayout;
    private TextView earningsText, numberPeopleText;
    private ListView earningsListview;
    private Adapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recommend_award_winning);
        //ButterKnife.bind(this);
        initView();
        RequestBody requestBody = new MultipartBody.Builder()
                .setType(MultipartBody.FORM)
                .addFormDataPart("userId", IOSharedPreferences.inString(this,getString(R.string.user),getString(R.string.userId)))
                .addFormDataPart("startPage", "1")
                .addFormDataPart("pageSize", "1000")
                .build();
        Request request = new Request.Builder()
                .post(requestBody)
                .url(Config.Url.getUrl(Config.RECOMMEND))
                .build();
        new OkHttpClient().newCall(request).enqueue(callback);
    }

    /**
     * 初始化控件
     */
    private void initView() {
        backRl = (RelativeLayout) findViewById(R.id.back_rl);
        backRl.setOnClickListener(this);
        activeRule = (RelativeLayout) findViewById(R.id.active_rule_rl);
        activeRule.setOnClickListener(this);
        wechatLayout = (LinearLayout) findViewById(R.id.wechat_layout);
        wechatLayout.setOnClickListener(this);
        qqLayout = (LinearLayout) findViewById(R.id.qq_layout);
        qqLayout.setOnClickListener(this);
        scanCodeLayout = (LinearLayout) findViewById(R.id.scan_code_layout);
        scanCodeLayout.setOnClickListener(this);
        earningsLayout = (LinearLayout) findViewById(R.id.earnings_layout);
        earningsLayout.setOnClickListener(this);
        successInvitedLayout = (LinearLayout) findViewById(R.id.success_invited_layout);
        successInvitedLayout.setOnClickListener(this);
        earningsText = (TextView) findViewById(R.id.earnings_text);
        numberPeopleText = (TextView) findViewById(R.id.number_people_text);
        noInviteLayout = (LinearLayout) findViewById(R.id.no_invite_layout);
        earningsListview = (ListView) findViewById(R.id.earnings_listview);
    }

    public void onClick(View view) {
        Intent intent = null;
        switch (view.getId()) {
            case R.id.back_rl:
                finish();
                break;
            case R.id.active_rule_rl://活动规则
                intent = new Intent(this, HelpActivity.class);
                intent.putExtra(HelpActivity.URI, "slowlife/app/invoking/recommendFriends.html");
                startActivity(intent);
                break;
            case R.id.wechat_layout://微信邀请

                share(SHARE_MEDIA.WEIXIN);
                break;
            case R.id.qq_layout://QQ邀请

                share(SHARE_MEDIA.QQ);
                break;
            case R.id.friend_layout:    // 朋友圈

                share(SHARE_MEDIA.WEIXIN_CIRCLE);
                break;
            case R.id.qzone_layout:    // 空间

                share(SHARE_MEDIA.QZONE);
                break;
            case R.id.scan_code_layout://面对面邀请
                showZxImg();
                break;
            case R.id.earnings_layout://累计收益
                break;
            case R.id.success_invited_layout://成功邀请
                break;
            case R.id.fjoaiej:
                startActivity(new Intent(this, ShareDetailActivity.class));
                break;
        }
    }



    private void share(SHARE_MEDIA platform) {
        String url = "";
        url = Config.Url.getUrl(Config.DOWNLOAD1);
        UMWeb web = new UMWeb(url);
        web.setTitle("送你15元新人大礼包，惠递请你寄快递啦！");
        web.setThumb(new UMImage(this, R.mipmap.ic_launcher));
        web.setDescription("同城快送低至4元，区外快递低至7元，统统半小时上门收件。手机APP下单，上门服务，乐享生活，等你加入");
        ShareAction share = new ShareAction(this)
//                .withText("hello world")
                .setPlatform(platform)
                .withMedia(web)
                .setCallback(this);
        share.share();
    }


    private void showZxImg() {
        try {
            String url = "";
             url = Config.Url.getUrl(Config.DOWNLOAD);
//            else url = Config.Url.getUrl(Config.SHARE + info.getId());


            DisplayMetrics metrics = getResources().getDisplayMetrics();


            Hashtable<EncodeHintType, String> hints = new Hashtable<>();
            hints.put(EncodeHintType.CHARACTER_SET, "utf-8");
            BitMatrix matrix = new MultiFormatWriter().encode(url,
                    BarcodeFormat.QR_CODE, metrics.widthPixels / 5 * 4, metrics.widthPixels / 5 * 4);
            int width = matrix.getWidth();
            int height = matrix.getHeight();
            int[] pixels = new int[width * height];

            for (int y = 0; y < height; y++) {
                for (int x = 0; x < width; x++) {
                    if (matrix.get(x, y)) {
                        pixels[y * width + x] = 0xff000000;
                    }
                }
            }
            Bitmap bitmap = Bitmap.createBitmap(width, height,
                    Bitmap.Config.ARGB_8888);
            bitmap.setPixels(pixels, 0, width, 0, 0, width, height);


            bitmap.compress(Bitmap.CompressFormat.PNG, 100, new FileOutputStream(new File(getExternalCacheDir(), "a.png")));
            final Dialog dialog = new Dialog(this);
            Window win = dialog.getWindow();
            win.requestFeature(Window.FEATURE_NO_TITLE);
            WindowManager.LayoutParams attr = win.getAttributes();
            attr.width = metrics.widthPixels;
            attr.height = -2;
            win.setAttributes(attr);
            LinearLayout content = new LinearLayout(this);
            content.setGravity(Gravity.CENTER_HORIZONTAL);
            content.setOrientation(LinearLayout.VERTICAL);
            ImageView imageView = new ImageView(this);
            imageView.setImageBitmap(bitmap);
            content.addView(imageView);
            View view = new View(this);
            view.setBackgroundColor(Color.parseColor("#e9e9e9"));
            content.addView(view, -1, 2);
            TextView textView = new TextView(this);
            textView.setText("确定");
            int pad = DisplayUtil.dip2px(this, 8);
            textView.setPadding(pad, pad, pad, pad);
            textView.setGravity(Gravity.CENTER);
            textView.setTextSize(16);
            content.addView(textView);
            content.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dialog.dismiss();
                }
            });
            dialog.setContentView(content);
            dialog.show();
        } catch (Exception e) {

        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        //ButterKnife.unbind(this);
    }

    @Override
    public void onStart(SHARE_MEDIA share_media) {

    }

    @Override
    public void onResult(SHARE_MEDIA share_media) {
        Toast.makeText(this, "分享成功", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onError(SHARE_MEDIA share_media, Throwable throwable) {
        Toast.makeText(this, "分享失败", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onCancel(SHARE_MEDIA share_media) {
        Toast.makeText(this, "取消分享", Toast.LENGTH_SHORT).show();
    }

    private Callback callback = new SimpleCallback() {
        @Override
        public void onSuccess(String tag, JSONObject json) throws JSONException {
            numberPeopleText.setText(json.getString("NormalNumber"));
            int p = json.getInt("NormalNumber");
            double count = json.getJSONObject("recommend").getInt("unitPrice");
            earningsText.setText(String.valueOf(p * count));
            if (json.has("nextrecommend"))
                ((TextView) findView(R.id.price)).setText(
                        (json.getJSONObject("nextrecommend").getDouble("unitPrice") *
                                json.getJSONObject("nextrecommend").getInt("number")) + "元");
            else
                ((TextView) findView(R.id.price)).setText("*元");
            JSONArray arr = json.getJSONObject("recommendsInfo").getJSONArray("aaData");
            adapter = new Adapter(RecommentdAwardActivity.this, arr);
            earningsListview.setAdapter(adapter);
            noInviteLayout.setVisibility(View.GONE);
            earningsListview.setVisibility(View.VISIBLE);

        }
    };


    class Adapter extends BaseAdapter {
        Context context;
        JSONArray arr;
        int pad;

        Adapter(Context context, JSONArray arr) {
            this.context = context;
            this.arr = arr;
            pad = DisplayUtil.dip2px(context, 8);
        }

        @Override
        public int getCount() {
            return arr == null ? 0 : arr.length();
        }

        @Override
        public JSONObject getItem(int position) {
            try {
                return arr == null ? null : arr.getJSONObject(position);
            } catch (JSONException e) {
                return null;
            }
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            LinearLayout layout;
            TextView phone;
            TextView type;
            if (convertView == null) {
                layout = new LinearLayout(context);
                layout.setOrientation(LinearLayout.VERTICAL);
                layout.setPadding(pad, pad, pad, pad);
                phone = new TextView(context);
                type = new TextView(context);
                layout.addView(phone);
                layout.addView(type);
            } else {
                layout = (LinearLayout) convertView;
                phone = (TextView) layout.getChildAt(0);
                type = (TextView) layout.getChildAt(1);
            }
            try {
                phone.setText(arr.getJSONObject(position).getString("phone"));
                type.setText(arr.getJSONObject(position).getString("status_value"));
            } catch (JSONException e) {
                phone.setText("");
                type.setText("");
            }
            return layout;
        }
    }
}
