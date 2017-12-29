package com.zsh.shopclient.aPresenter;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import com.DisplayUtil;
import com.xxx.skynet.sharedPreference.IOSharedPreferences;
import com.zsh.shopclient.R;
import com.zsh.shopclient.interfaceconfig.Config;
import com.zsh.shopclient.tool.SimpleCallback;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;

public class ShareDetailActivity extends BaseActivity2 {

    @BindView(R.id.sum)
    TextView sum;
    @BindView(R.id.money)
    TextView textView;
    @BindView(R.id.rule)
    TableLayout table;
    @BindView(R.id.convert)
    TextView convert;
    private OkHttpClient httpClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_share_destail);
        ButterKnife.bind(this);
        init();

        httpClient = new OkHttpClient();
        RequestBody requestBody = new MultipartBody.Builder()
                .setType(MultipartBody.FORM)
                .addFormDataPart("userId", IOSharedPreferences.inString(this,getString(R.string.user),getString(R.string.userId)))
                .addFormDataPart("tab", "tab")
                .build();
        Request request = new Request.Builder()
                .post(requestBody)
                .url(Config.Url.getUrl(Config.RECOMMEND))
                .tag(Config.RECOMMEND)
                .build();
        new OkHttpClient().newCall(request).enqueue(new Callback(this));
    }

    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.back_rl:
                finish();
                break;
            default: {
                RequestBody requestBody = new MultipartBody.Builder()
                        .setType(MultipartBody.FORM)
                        .addFormDataPart("userId", IOSharedPreferences.inString(this,getString(R.string.user),getString(R.string.userId)))
                        .build();
                Request request = new Request.Builder()
                        .url(Config.Url.getUrl(Config.RECOMMENDNUMBEREXCHANGEBALANCE))
                        .tag(Config.RECOMMENDNUMBEREXCHANGEBALANCE)
                        .post(requestBody)
                        .build();
                httpClient.newCall(request).enqueue(new Callback(this));
            }
        }
    }

    @Override
    protected void init() {
        super.init();
        table.removeAllViews();
        TableRow row = new TableRow(this);
        int height = DisplayUtil.dip2px(this, 30);
        int width = table.getMeasuredWidth();

        TextView tv1 = new TextView(this);
        tv1.setText("人数");
        tv1.setGravity(Gravity.CENTER);
        View line = new View(this);
        TextView tv2 = new TextView(this);
        tv2.setText("单个金额/元");
        tv2.setGravity(Gravity.CENTER);
        row.addView(tv1, (width - 1) / 2, height);
        row.addView(line, 1, height);
        row.addView(tv2, (width - 1) / 2, height);
        table.addView(row, -1, height);
        final View view = new View(this);
        view.setBackgroundColor(Color.BLACK);
        table.addView(view, -1, 1);
    }

    private void initRule(JSONObject json) throws JSONException {
        sum.setText(json.getString("NormalNumber") + "人");
        textView.setText(json.getString("cost"));
        int height = DisplayUtil.dip2px(this, 30);
        int width = table.getMeasuredWidth();
        JSONArray arr = json.getJSONArray("recommendAll");
        for (int i = 0; i < arr.length(); i++) {
            final TableRow row = new TableRow(this);
            final JSONObject jsonObject = arr.getJSONObject(i);
            final TextView tv1 = new TextView(this);
            tv1.setText(jsonObject.getString("number") + "人以内");
            tv1.setGravity(Gravity.CENTER);
            final View line = new View(this);
            line.setBackgroundColor(Color.RED);
            final TextView tv2 = new TextView(this);
            tv2.setText(jsonObject.getString("unitPrice"));
            tv2.setGravity(Gravity.CENTER);
            row.addView(tv1, (width - 1) / 2, height);
            row.addView(line, 1, height);
            row.addView(tv2, (width - 1) / 2, height);
            table.addView(row, -1, height);
            final View view = new View(this);
            view.setBackgroundColor(Color.BLACK);
            table.addView(view, -1, 1);
        }
        table.requestLayout();
    }

    @OnClick(R.id.back_rl)
    public void onViewClicked() {
    }

    class Callback extends SimpleCallback {
        Callback(Context context) {
            super(context,false);
        }

        @Override
        public void onSuccess(String tag, JSONObject json) throws JSONException {
            switch (tag) {
                case Config.RECOMMEND:
                    initRule(json);
                    break;
                case Config.RECOMMENDNUMBEREXCHANGEBALANCE:
                    Toast.makeText(ShareDetailActivity.this, json.getString("message"), Toast.LENGTH_SHORT).show();
                    finish();
                    break;
            }
        }
    }
}
