package com.zsh.shopclient.fPresenter;

import android.content.Intent;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v4.content.FileProvider;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;

import com.xxx.skynet.sharedPreference.IOSharedPreferences;
import com.xxx.skynet.sqlSave.TypeSqliteHelper;
import com.zsh.shopclient.R;
import com.zsh.shopclient.aPresenter.ComplainActivity;
import com.zsh.shopclient.aPresenter.DiscountCouponActivity;
import com.zsh.shopclient.aPresenter.HelpActivity;
import com.zsh.shopclient.aPresenter.LoginActivity;
import com.zsh.shopclient.aPresenter.MainActivity;
import com.zsh.shopclient.aPresenter.ReceiptAddressActivity;
import com.zsh.shopclient.aPresenter.RecommentdAwardActivity;
import com.zsh.shopclient.aPresenter.SbShopActivity;
import com.zsh.shopclient.aPresenter.SetActivity;
import com.zsh.shopclient.aPresenter.VIPActivity;
import com.zsh.shopclient.aView.BaseView;
import com.zsh.shopclient.fView.MyV;
import com.zsh.shopclient.fView.VipV;
import com.zsh.shopclient.interfaceconfig.Config;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.UUID;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by Administrator on 2017/11/5.
 */

public class MyFragment extends BaseFragment {
    @Override
    protected BaseView createTypeView() {
        return new MyV();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    public View.OnClickListener getClickListener(){
        return new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final String userId = IOSharedPreferences.inString(getActivity(), getString(R.string.user), getString(R.string.userId));
                if (null == userId)
                    startActivityForResult(new Intent(getActivity(), LoginActivity.class), MainActivity.RETURN_USER);
                else
                switch (view.getId()) {
                    case R.id.shop_rl:
                        startActivity(new Intent(getActivity(), SbShopActivity.class));
                        break;
                    case R.id.set_rl:
                        startActivity(new Intent(getActivity(), SetActivity.class));
                        break;
                    case R.id.name_text:
                        startActivity(new Intent(getActivity(), LoginActivity.class));
                        break;
                    case R.id.balance_ll:
                        break;
                    case R.id.discount_coupon_ll:
                        startActivity(new Intent(getActivity(), DiscountCouponActivity.class));
                        break;
                    case R.id.sb_ll:
                        startActivity(new Intent(getActivity(), SbShopActivity.class));
                        break;
                    case R.id.address_rl:
                        startActivity(new Intent(getActivity(), ReceiptAddressActivity.class));
                        break;
                    case R.id.vip_rl:
                        startActivity(new Intent(getActivity(), VIPActivity.class));
                        break;
                    case R.id.recommended_rl:
                        startActivity(new Intent(getActivity(), RecommentdAwardActivity.class));
                        break;
                    case R.id.service_center_rl:
                        Intent intent = new Intent(getContext(), HelpActivity.class);
                        intent.putExtra(HelpActivity.URI, "slowlife/app/invoking/userHelpInfo.html");
                        startActivity(intent);
                        break;
                    case R.id.feedback_rl:
                        startActivity(new Intent(getActivity(), ComplainActivity.class));
                        break;
                    case R.id.join_in_rl:
                        //用intent启动拨打电话
                        intent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:72721515"));
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(intent);
                        break;
                }
            }
        };
    }

    @Override
    public void onResume() {
        if (IOSharedPreferences.inString(getActivity(), getString(R.string.user),
                getString(R.string.userId))!=null)
            ((MyV) getTypeView()).initData();
        super.onResume();
    }
}
