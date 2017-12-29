package cn.com.inlee.merchant.zbar.activity;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.zbar.lib.R;

import org.json.JSONException;
import org.json.JSONObject;

import cn.com.inlee.merchant.zbar.tool.NetworkLink;


/**
 * Created by Administrator on 2017/7/28.
 * 去标题报错You need to use a Theme.AppCompat theme (or descendant) with this activity
 * 原因:MainActivity继承自v7包中的ActionBarActivity或者AppCompatActivity导致
 * 修改android:theme为parent="Theme.AppCompat.Light.NoActionBar"
 */

public class ScanResultActivity extends AppCompatActivity {
    private ImageView banner;
    private TextView message;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result);
        initView();
        new Async().execute(getIntent().getStringExtra("url"),getIntent().getStringExtra("parameter")+getIntent().getStringExtra("scanResult"));
    }
    private void initView(){
        banner = (ImageView)findViewById(R.id.image_result);
        message = (TextView) findViewById(R.id.text_result);
        findViewById(R.id.text_back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(R.id.text_back == v.getId())
                    finish();
            }
        });
    }
    private class Async extends AsyncTask<String ,Integer,String> {
        @Override
        protected String doInBackground(String... params) {
            Log.i("ScanResultActivity","url>>>"+params[0]+"---param[1]>>>"+params[1]);
            return NetworkLink.postJson(params[0],params[1].toString().trim().getBytes());
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            if(null != result)
                try {
                    JSONObject json = new JSONObject(result);
                    //banner.setSelected("succeed".equals(json.getString("statesValue")));
                    banner.setSelected("200".equals(json.getString("statesCode")));
                    message.setText(json.getString("message"));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
        }
    }
}
