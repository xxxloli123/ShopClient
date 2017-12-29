package com.zsh.shopclient.aView;

import android.os.CountDownTimer;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.zsh.shopclient.R;
import com.zsh.shopclient.aPresenter.BaseActivity;
import com.zsh.shopclient.aPresenter.LoginActivity;
import com.zsh.shopclient.receiver.TimeBr;

import butterknife.BindView;

/**
 * Created by Administrator on 2017/10/30.
 */

public class LoginV extends BaseView {
    public static String ACTION_TIME;
    private String getVerigCodeText;
    private TimeBr timeBr;
    private CountDownTimer timer;
    private AlertDialog loading;
    private String login;
    @BindView(R.id.edit_phone) EditText phone;
    @BindView(R.id.text_getVerigyCode) TextView getVerigyCode;
    @BindView(R.id.edit_verifyCode) EditText verifyCode;
    @BindView(R.id.text_signIn) TextView signIn;
    @BindView(R.id.checkbox_iAgree) CheckBox iAgree;
    @BindView(R.id.text_agreement) TextView agreement;
    public String getPhone(){
        return phone.getText().toString().trim();
    }
    public void setVerifyCode(){
        //getVerigyCode.setClickable(text.equals(getVerigCodeText));
        //getVerigyCode.setText(text);
       final String txt = getVerigyCode.getText().toString().trim();
        timer = new CountDownTimer(60000,1000){
            @Override
            public void onTick(long millisUntilFinished) {
                getVerigyCode.setText(millisUntilFinished/1000+"s");
                getVerigyCode.setClickable(false);
            }

            @Override
            public void onFinish() {
                getVerigyCode.setText(txt);
                getVerigyCode.setClickable(true);
            }
        }.start();
    }
    public String getVerifyCode(){
        return verifyCode.getText().toString().trim();
    }
    public boolean getIAgree(){
        return iAgree.isChecked();
    }

    @Override
    public void initView() {
        BaseActivity activity = (BaseActivity) getRootView().getContext();
        ACTION_TIME = activity.getPackageName()+".actionTime";
        ((TextView)activity.findViewById(R.id.text_title)).setText(R.string.signIn);
        View.OnClickListener listener = ((LoginActivity)activity).getClickListener();
        activity.findViewById(R.id.image_goBack).setOnClickListener(listener);
        phone.addTextChangedListener(((LoginActivity)activity).getInListener(getVerigyCode));
        getVerigyCode.setOnClickListener(listener);
        signIn.setOnClickListener(listener);
        agreement.setOnClickListener(listener);
        getVerigyCode.setClickable(false);
        login = activity.getString(R.string.login);
        getVerigCodeText = getVerigyCode.getText().toString().trim();
        /*timeBr = new TimeBr(activity);
        IntentFilter filter = new IntentFilter();
        filter.addAction(ACTION_TIME);
        activity.registerReceiver(timeBr,filter);*/
    }

    @Override
    public void initWidget() {
        super.initWidget();
        BaseActivity activity = (BaseActivity) getRootView().getContext();
        View view = LayoutInflater.from(activity).inflate(R.layout.window_loading_progress_bar,null);
        View pro = view.findViewById(R.id.progress_bar);
        LinearLayout.LayoutParams vParams = (LinearLayout.LayoutParams) pro.getLayoutParams();
        vParams.width = (int)activity.getResources().getDimension(R.dimen.space_between64);
        vParams.height = (int)activity.getResources().getDimension(R.dimen.space_between64);
        pro.setLayoutParams(vParams);
        loading = new AlertDialog.Builder(activity).setView(view).create();
        /*loadingDisplay();
        WindowManager.LayoutParams params = loading.getWindow().getAttributes();
        params.width = (int)activity.getResources().getDimension(R.dimen.width128);
        params.height = (int)activity.getResources().getDimension(R.dimen.width128);
        params.gravity = Gravity.CENTER;//这种方式不能居中
        loading.getWindow().setAttributes(params);*/
    }

    public void loadingDisplay(){
        loading.show();
    }
    public void loadingDismiss(){
        loading.dismiss();
    }
    public String getLogin() {
        return login;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        //activity.unregisterReceiver(timeBr);
        if(null != timer)
            timer.cancel();
        loadingDismiss();
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_login;
    }
}
