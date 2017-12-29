package com.zsh.shopclient.fPresenter;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.xxx.skynet.sharedPreference.IOSharedPreferences;
import com.zsh.shopclient.R;
import com.zsh.shopclient.aPresenter.LoginActivity;
import com.zsh.shopclient.aPresenter.MainActivity;
import com.zsh.shopclient.aPresenter.RecommentdAwardActivity;
import com.zsh.shopclient.aPresenter.SbShopActivity;
import com.zsh.shopclient.aPresenter.SpecialOfferActivity;
import com.zsh.shopclient.aView.BaseView;
import com.zsh.shopclient.fView.DiscoverV;
import com.zsh.shopclient.model.Luck;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by Administrator on 2017/11/5.
 */

public class DiscoverFragment extends BaseFragment {
    @Override
    protected BaseView createTypeView() {
        return new DiscoverV();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //getIntent().putExtra(getString(R.string.requestParam),"ActivityGoods");
        if(null == ((MainActivity)getActivity()).getLocation())return;
        final String  commodityUrl = getString(R.string.ip)+ getString(R.string.pictureLibraryPath);
        MultipartBody.Builder requestBody = new MultipartBody.Builder().setType(MultipartBody.FORM);
        requestBody.addFormDataPart("pro",((MainActivity)getActivity()).getLocation().getProvince()).addFormDataPart("city",((MainActivity) getActivity()).
                getLocation().getCity()).addFormDataPart("district",((MainActivity)getActivity()).getLocation().getDistrict());
        new OkHttpClient().newCall(new Request.Builder().url(getString(R.string.ip)+getString(R.string.everyDaySpecial)).post(requestBody.addFormDataPart("type","Bargain").build()).build()).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
                getTypeView().hintHoast(R.string.getDataDefeated,null, Toast.LENGTH_SHORT);
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                try {
                    JSONObject result = new JSONObject(response.body().string());
                    Log.i("DiscoverFragment","onViewCreated-specialOffer->"+result);
                    if(200 == result.getInt("statusCode"))
                        ((DiscoverV) getTypeView()).initSpecialOffer((MainActivity) getActivity(), new Luck(commodityUrl,result.getJSONObject
                                ("BargainGoodsInfo").getJSONArray("aaData")).getList());
                    else
                        getTypeView().hintHoast(BaseView.X,result.getString("message"), Toast.LENGTH_SHORT);
                } catch (JSONException e) {
                    e.printStackTrace();
                    getTypeView().hintHoast(R.string.parseFailure,null, Toast.LENGTH_SHORT);
                }
            }
        });
        new OkHttpClient().newCall(new Request.Builder().url(getString(R.string.ip)+ getString(R.string.flashSaleUrl)).post(requestBody.build()).build()).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                try {
                    JSONObject result = new JSONObject(response.body().string());
                    Log.i("DiscoverFragment", "onViewCreated-flashSale->" + result);
                    if (200 == result.getInt("statusCode"))
                        ((DiscoverV) getTypeView()).initFlashSale((MainActivity) getActivity(), new Luck(commodityUrl,result.getJSONObject
                                ("BargainGoodsInfo").getJSONArray("aaData")).getList());
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }
    public View.OnClickListener getClickListener(){
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String userId = IOSharedPreferences.inString(getActivity(),getString(R.string.user),getString(R.string.userId));
                switch (v.getId()){
                    case R.id.image_currencyShop:
                        if(null != userId)
                            startActivity(new Intent(getActivity(), SbShopActivity.class));
                        else
                            startActivityForResult(new Intent(getActivity(), LoginActivity.class),MainActivity.RETURN_USER);
                        break;
                    case R.id.image_referralBonuses:
                        if(null != userId)
                            startActivity(new Intent(getActivity(), RecommentdAwardActivity.class));
                        else
                            startActivityForResult(new Intent(getActivity(), LoginActivity.class),MainActivity.RETURN_USER);
                        break;
                    case R.id.text_everyDaySspecial:
                        if(null != ((MainActivity)getActivity()).getLocation()) {
                            Intent specialOffer = new Intent(getActivity(), SpecialOfferActivity.class);
                            specialOffer.putExtra(getString(R.string.paramValue), "Bargain");
                            specialOffer.putExtra(getString(R.string.location), ((MainActivity)getActivity()).getLocation());
                            startActivity(specialOffer);
                        }
                        break;
                    case R.id.text_flashSale:
                        if(null != ((MainActivity)getActivity()).getLocation()) {
                            Intent flashSale = new Intent(getActivity(), SpecialOfferActivity.class);
                            flashSale.putExtra(getString(R.string.paramValue), "Seckill");
                            flashSale.putExtra(getString(R.string.location), ((MainActivity)getActivity()).getLocation());
                            startActivity(flashSale);
                        }
                        break;
                }
            }
        };
    }
}
