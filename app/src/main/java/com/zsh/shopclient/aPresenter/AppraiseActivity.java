package com.zsh.shopclient.aPresenter;

import android.content.Intent;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.xxx.skynet.sharedPreference.IOSharedPreferences;
import com.xxx.skynet.sqlSave.TypeSqliteHelper;
import com.zsh.shopclient.R;
import com.zsh.shopclient.aView.AppraiseV;
import com.zsh.shopclient.aView.BaseView;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by Administrator on 2017/12/20.
 */

public class AppraiseActivity extends BaseActivity {
    private List<CharSequence[]> list;
    private String shopJson(String typeName,float grade,String comment,String typeId,String evaluateType){
        String anonymity = ((AppraiseV)getTypeView()).getAnonymity();
        HashMap<String ,Object> hashMap = new HashMap<>();
        hashMap.put("id",IOSharedPreferences.inString(this,getString(R.string.user),getString(R.string.userId)));
        List<List<String>> lists = new TypeSqliteHelper(this,getString(R.string.sqlLibrary),null,1,null).query("user",new String[]{"name","phone","headerImg","id"},hashMap);
        String userName = "null".equals(lists.get(0).get(0))? getString(R.string.zshUser): lists.get(0).get(0);
        return new StringBuilder("{\"name\":\"").append(userName).append("\",\"phone\":\"").append(lists.get(0).get(1)).append("\",\"headerImg\":\"").
                append(lists.get(0).get(2)).append("\",\"userId\":\"").append(lists.get(0).get(3)).append("\",\"shopId\":\"").
                append(((Map<CharSequence[], List<CharSequence[]>>)getIntent().getExtras().get(getString(R.string.title))).keySet().iterator().next()[12]).
                append("\",\"grade\":\"").append(0 < grade ? (int)Math.ceil(grade): 1).append("\",\"comment\":\"").append(comment).append("\",\"typeName\":\"").
                append(typeName).append("\",\"evaluateType\":\"").append(evaluateType).append("\",\"typeId\":\"").append(typeId).append("\",\"anonymity\":\"")
                .append(anonymity).append("\",\"userNickName\":\"").append(userName).append("\",\"orderId\":\"").append(((Map<CharSequence[], List<CharSequence[]>>) getIntent().getExtras().get(getString(R.string.title))).keySet().iterator().next()[30]).append("\"}").toString();
    }
    @Override
    protected BaseView createTypeView() {
        return new AppraiseV();
    }
    public View.OnClickListener getClickListener(){
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (v.getId()){
                    case R.id.image_goBack:
                        finish();
                        break;
                    case R.id.text_commit:
                        final String stutas = getString(R.string.result);
                        float shopStar = ((AppraiseV)getTypeView()).getRatingBarShop();
                        String shopComment = ((AppraiseV)getTypeView()).getShopCommentInput();
                        float shippingStar = ((AppraiseV)getTypeView()).getShippingServiceStar();
                        String shippingComment = ((AppraiseV)getTypeView()).getShippingComment();
                        StringBuilder sb = new StringBuilder("[");
                        for(CharSequence[] array : list)
                            sb.append(shopJson(array[1].toString(), Float.valueOf(array[2].toString()), "".equals(array[3])? getString(R.string.goodReputation):
                                    array[3].toString(), array[4].toString(), "product")).append(",");
                        sb.replace(sb.length()-1, sb.length(),"]");
                        MultipartBody.Builder requestBody = new MultipartBody.Builder();
                        requestBody.addFormDataPart("shopStr",shopJson(((Map<CharSequence[], List<CharSequence[]>>)getIntent().getExtras().get(getString(
                                R.string.title))).keySet().iterator().next()[1].toString(),shopStar,"".equals(shopComment)? getString(R.string.goodReputation): shopComment,
                                ((Map<CharSequence[], List<CharSequence[]>>) getIntent().getExtras().get(getString(R.string.title))).keySet().iterator().next()[12].toString(),"Shop"))
                                .addFormDataPart("productStrS",sb.toString()).addFormDataPart("expressStr", shopJson(((Map<CharSequence[], List<CharSequence[]>>)
                                        getIntent().getExtras().get(getString(R.string.title))).keySet().iterator().next()[22].toString(), shippingStar,"".equals(shippingComment)? getString(R.string.goodReputation): shippingComment,
                                        ((Map<CharSequence[], List<CharSequence[]>>)getIntent().getExtras().get(getString(R.string.title))).keySet().iterator()
                                                .next()[24].toString(),"express")).addFormDataPart("collection",((AppraiseV)getTypeView()).getCollect());
                        new OkHttpClient().newCall(new Request.Builder().url(getString(R.string.ip)+getString(R.string.comment)).post(requestBody.build()).build()).enqueue(new Callback() {
                            @Override
                            public void onFailure(Call call, IOException e) {
                                getTypeView().hintHoast(R.string.submitDataDefeated,null, Toast.LENGTH_SHORT);
                                e.printStackTrace();
                            }

                            @Override
                            public void onResponse(Call call, Response response) throws IOException {
                                try {
                                    JSONObject result = new JSONObject(response.body().string());
                                    Log.i("AppraiseActivity", "getClickListener->" + result);
                                    if (200 == result.getInt("statusCode")) {
                                        Intent intent = new Intent();
                                        intent.putExtra(stutas,true);
                                        setResult(RESULT_OK,intent);
                                        finish();
                                    }
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }
                        });
                        break;
                }
            }
        };
    }
    public List<CharSequence[]>getList(){
        list = new ArrayList();
        Map map = (Map<CharSequence[], List<CharSequence[]>>)getIntent().getExtras().get(getString(R.string.title));
        List<CharSequence[]> souce = (List<CharSequence[]>)map.get(map.keySet().iterator().next());
        for(CharSequence[] array : souce)
            list.add(new CharSequence[]{array[0],array[1],"1","",array[4]});
        return list;
    }
}
