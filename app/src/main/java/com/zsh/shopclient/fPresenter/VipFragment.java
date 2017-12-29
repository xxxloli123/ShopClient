package com.zsh.shopclient.fPresenter;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.xxx.skynet.sharedPreference.IOSharedPreferences;
import com.zsh.shopclient.R;
import com.zsh.shopclient.aPresenter.MainActivity;
import com.zsh.shopclient.aView.BaseView;
import com.zsh.shopclient.fView.VipV;
import com.zsh.shopclient.adapter.BaseRecycleAdapter;
import com.zsh.shopclient.tool.JsonHelper;

import org.json.JSONArray;
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
 * Created by Administrator on 2017/11/5.
 */

public class VipFragment extends BaseFragment {
    private int page;

    private void linkNet(final boolean isRefresh) {
        final String userId = IOSharedPreferences.inString(getActivity(), getString(R.string.user), getString(R.string.userId));
        if (null != ((MainActivity) getActivity()).getLocation() && null != userId) {
            MultipartBody.Builder requestBody = new MultipartBody.Builder();
            requestBody.addFormDataPart("pro", ((MainActivity) getActivity()).getLocation().getProvince()).addFormDataPart("city", ((MainActivity) getActivity()).getLocation().getCity()).
                    addFormDataPart("district", ((MainActivity) getActivity()).getLocation().getDistrict()).addFormDataPart("lng", ((MainActivity) getActivity()).getLocation().getLongitude() + "").
                    addFormDataPart("lat", ((MainActivity) getActivity()).getLocation().getLatitude() + "").addFormDataPart("startPage", "" + page).addFormDataPart("pageSize", "" + 8).
                    addFormDataPart("userId", userId);
            new OkHttpClient().newCall(new Request.Builder().url(getString(R.string.ip) + getString(R.string.vipShop)).post(requestBody.build()).build()).enqueue(new Callback() {
                @Override
                public void onFailure(Call call, IOException e) {
                    e.printStackTrace();
                    getTypeView().hintHoast(R.string.getDataDefeated, null, Toast.LENGTH_SHORT);
                }

                @Override
                public void onResponse(Call call, Response response) throws IOException {
                    try {
                        JSONObject result = new JSONObject(response.body().string());
                        Log.i("VipFragment", "linkNet->" + result);
                        if (200 == result.getInt("statusCode")) {
                            List<Map<String, Object>> listMap = JsonHelper.jsonArray2List(result.getJSONArray("list"));
                            List<Map<Map<String, Object>, List<Map<String, Object>>>> lists = new ArrayList<Map<Map<String, Object>, List<Map<String, Object>>>>();
                            for (Map<String, Object> map : listMap) {
                                Map<Map<String, Object>, List<Map<String, Object>>> maps = new HashMap<Map<String, Object>, List<Map<String, Object>>>();
                                maps.put(JsonHelper.jsonObject2Map(new JSONObject(map.get("shop").toString())), JsonHelper.jsonArray2List(new JSONArray(map.get("activity").toString())));
                                lists.add(maps);
                            }
                            ((VipV) getTypeView()).update(lists, isRefresh);
                        } else
                            getTypeView().hintHoast(BaseView.X, result.getString("message"), Toast.LENGTH_SHORT);
                    } catch (JSONException e) {
                        e.printStackTrace();
                        getTypeView().hintHoast(R.string.parseFailure, null, Toast.LENGTH_SHORT);
                    }
                }
            });
        }else
            ((VipV) getTypeView()).update(null, isRefresh);
    }
    @Override
    protected BaseView createTypeView() {
        return new VipV();
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        page = 1;
        linkNet(true);
    }

    public SwipeRefreshLayout.OnRefreshListener getRefreshListener(){
        return new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                page = 1;
                linkNet(true);
            }
        };
    }

    public BaseRecycleAdapter.FootLoadListener getFootLoadListener(){
        return new BaseRecycleAdapter.FootLoadListener() {
            @Override
            public void loadMoreListener() {
                page++;
                linkNet(false);
            }
        };
    }
}
