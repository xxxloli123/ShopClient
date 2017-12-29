package com.zsh.shopclient.fPresenter;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.zsh.shopclient.R;
import com.zsh.shopclient.aPresenter.ShopActivity;
import com.zsh.shopclient.aView.BaseView;
import com.zsh.shopclient.adapter.BaseRecycleAdapter;
import com.zsh.shopclient.fView.CommentV;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by Administrator on 2017/11/17.
 */

public class CommentFragment extends BaseFragment {
    private int page;
    @Override
    protected BaseView createTypeView() {
        return new CommentV();
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        page = 1;
        linkNet();
    }
    public SwipeRefreshLayout.OnRefreshListener getRefreshListener(){
        return new SwipeRefreshLayout.OnRefreshListener(){
            @Override
            public void onRefresh() {
                page = 1;
                linkNet();
            }
        };
    }
    public BaseRecycleAdapter.FootLoadListener getLoadMoreListener(){
        return new BaseRecycleAdapter.FootLoadListener(){
            @Override
            public void loadMoreListener() {
                page++;
                linkNet();
            }
        };
    }
    private void linkNet(){
        MultipartBody.Builder body = new MultipartBody.Builder().setType(MultipartBody.FORM);
        body.addFormDataPart("type","shop").addFormDataPart("typeId",((ShopActivity)getActivity()).getShopInfo()[9].toString()).
                addFormDataPart("startPage",""+page).addFormDataPart("pageSize","8");
        new OkHttpClient().newCall(new Request.Builder().url(getString(R.string.ip)+getString(R.string.shopComment)).post(body.build()).build()).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
                getTypeView().hintHoast(R.string.submitDataDefeated,null, Toast.LENGTH_SHORT);
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                try {
                    JSONObject result = new JSONObject(response.body().string());
                    Log.i("CommentFragment","clickListener--->"+result);
                    if(200 == result.getInt("statusCode")) {
                        List<CharSequence[]>list = new ArrayList<CharSequence[]>();
                        JSONArray jsonArray = result.getJSONObject("evaluateInfo").getJSONArray("aaData");
                        for(int index = 0;index < jsonArray.length();index++) {
                            JSONObject jsonObject = jsonArray.getJSONObject(index);
                            list.add(new CharSequence[]{jsonObject.getString("headerImg"),jsonObject.getString("userNickName"),jsonObject.getString("createDate"),
                                    jsonObject.getString("grade"), jsonObject.getString("comment")});
                        }
                        ((CommentV)getTypeView()).update(list,1 == page);
                    }else
                        getTypeView().hintHoast(BaseView.X,result.getString("message"), Toast.LENGTH_SHORT);
                }catch (JSONException e){
                    e.printStackTrace();
                    getTypeView().hintHoast(R.string.submitDataDefeated,null, Toast.LENGTH_SHORT);
                }
            }
        });
    }
}
