package com.zsh.shopclient.aPresenter;

import android.support.v7.widget.SearchView;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.zsh.shopclient.R;
import com.zsh.shopclient.aView.BaseView;
import com.zsh.shopclient.aView.SearchV;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by Administrator on 2017/11/29.
 */

public class SearchActivity extends BaseActivity {
    @Override
    protected BaseView createTypeView() {
        return new SearchV();
    }
    public View.OnClickListener getClickListener(){
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (v.getId()){
                    case R.id.image_goBack:
                        finish();
                        break;
                }
            }
        };
    }
    public SearchView.OnQueryTextListener getOnQueryTextViewListener(){//v7.widget.SearchView的使用 http://blog.csdn.net/jxxfzgy/article/details/46055857
        return new SearchView.OnQueryTextListener(){
            @Override
            public boolean onQueryTextSubmit(String query) {
                getIntent().setClass(SearchActivity.this,CommodityParticularsActivity.class);
                ((SearchV)getTypeView()).hindKeyboard();
                MultipartBody.Builder requestBody = new MultipartBody.Builder().setType(MultipartBody.FORM);
                Map<String,String> map = (Map<String, String>) getIntent().getExtras().getSerializable(getString(R.string.sendBody));
                Iterator iterator = map.keySet().iterator();
                while(iterator.hasNext()) {
                    String key = (String) iterator.next();
                    requestBody.addFormDataPart(key,map.get(key));
                }
                requestBody.addFormDataPart(getIntent().getStringExtra(getString(R.string.attach)),query);
                new OkHttpClient().newCall(new Request.Builder().url(getString(R.string.ip) + getString(getIntent().getIntExtra(getString(R.string.path),0))).post(requestBody.build()).build()).enqueue(new Callback() {
                    @Override
                    public void onFailure(Call call, IOException e) {
                        e.printStackTrace();
                        getTypeView().hintHoast(R.string.getDataDefeated,null,Toast.LENGTH_SHORT);
                    }
                    @Override
                    public void onResponse(Call call, Response response) throws IOException {
                        try {
                            JSONObject result = new JSONObject(response.body().string());
                            Log.i("SearchActivity", "getOnQueryTextViewListener-onQueryTextChange->" + result);
                            if (200 == result.getInt("statusCode")) {
                                List<CharSequence[]>list = new ArrayList<CharSequence[]>();
                                JSONArray jsonArray = result.getJSONObject("productsInfo").getJSONArray("aaData");
                                for(int index = 0;index< jsonArray.length();index++){
                                    JSONObject jsonObject = jsonArray.getJSONObject(index);
                                    list.add(new CharSequence[]{jsonObject.getString("smallImg"), jsonObject.getString("productName"),
                                            jsonObject.getString("singlePrice"),jsonObject.getString("id")});
                                }
                                ((SearchV)getTypeView()).searchResult(list);
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                            getTypeView().hintHoast(R.string.parseFailure,null,Toast.LENGTH_SHORT);
                        }
                    }
                });
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                if(0 < newText.length())
                    ;
                return false;
            }
        };
    }
    public SearchView.OnCloseListener getCloseListener(){
        return new SearchView.OnCloseListener() {
            @Override
            public boolean onClose() {
                ((SearchV)getTypeView()).searchResult(null);
                return true;
            }
        };
    }
}
