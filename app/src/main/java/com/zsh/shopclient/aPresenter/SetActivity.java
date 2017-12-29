package com.zsh.shopclient.aPresenter;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;

import com.xxx.skynet.sharedPreference.IOSharedPreferences;
import com.xxx.skynet.sqlSave.TypeSqliteHelper;
import com.zsh.shopclient.R;


/**
 * Created by Administrator on 2017/1/25 0025.
 */

public class SetActivity extends BaseActivity2 implements View.OnClickListener {
    private RelativeLayout go_back, account, common, regards;
    private Button edit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_set);
        initView();
    }

    /**
     * 初始化控件
     */
    private void initView() {
        go_back = (RelativeLayout) findViewById(R.id.back_rl);
        go_back.setOnClickListener(this);
        account = (RelativeLayout) findViewById(R.id.account_rl);
        account.setOnClickListener(this);
        common = (RelativeLayout) findViewById(R.id.common_rl);
        common.setOnClickListener(this);
        regards = (RelativeLayout) findViewById(R.id.regards_rl);
        regards.setOnClickListener(this);
        edit = (Button) findViewById(R.id.edit_bt);
        edit.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        Intent intent = null;
        switch (v.getId()) {
            case R.id.back_rl://返回
                finish();
                break;
            case R.id.regards_rl://关于掌升活
                 intent = new Intent(this, HelpActivity.class);
                intent.putExtra(HelpActivity.URI,"slowlife/app/invoking/aboutZsh.html");
                startActivity(intent);
                break;
            case R.id.edit_bt://退出登录
                new TypeSqliteHelper(this,getString(R.string.sqlLibrary), null,1,null).emptySql(getString(R.string.user));
                IOSharedPreferences.remove(this,getString(R.string.user));
                setResult(Activity.RESULT_OK);
                finish();
                break;
        }
    }
}
