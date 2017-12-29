package com.zsh.shopclient.aView;

import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.zsh.shopclient.R;
import com.zsh.shopclient.aPresenter.AddRegionActivity;

import butterknife.BindView;

/**
 * Created by Administrator on 2017/12/11.
 */

public class AddRegionV extends BaseView {
    @BindView(R.id.edit_houseNumber)EditText houseNumber;
    @BindView(R.id.edit_nane)EditText name;
    @BindView(R.id.edit_contactPhone)EditText contactPhone;
    private AlertDialog loading;
    @Override
    protected int getLayoutId() {
        return R.layout.activity_add_region;
    }

    @Override
    public void initView() {
        AddRegionActivity activity = (AddRegionActivity)getRootView().getContext();
        View.OnClickListener listener = activity.getClickListener();
        getRootView().findViewById(R.id.image_goBack).setOnClickListener(listener);
        ((TextView)getRootView().findViewById(R.id.text_title)).setText(R.string.addAddress);
        getRootView().findViewById(R.id.text_sumbit).setOnClickListener(listener);
        View view = LayoutInflater.from(activity).inflate(R.layout.window_loading_progress_bar,null);
        View pro = view.findViewById(R.id.progress_bar);
        LinearLayout.LayoutParams vParams = (LinearLayout.LayoutParams) pro.getLayoutParams();
        vParams.width = (int)activity.getResources().getDimension(R.dimen.space_between64);
        vParams.height = (int)activity.getResources().getDimension(R.dimen.space_between64);
        pro.setLayoutParams(vParams);
        loading = new AlertDialog.Builder(activity).setView(view).create();
    }
    public String[] getEdits(){
        String[] edits = new String[3];
        edits[0] = name.getText().toString().trim();
        edits[1] = contactPhone.getText().toString().trim();
        edits[2] = houseNumber.getText().toString().trim();
        if("".equals(edits[0]) || "".equals(edits[1]) || "".equals(edits[2])) {
            Toast.makeText(getRootView().getContext(), getRootView().getContext().getString(R.string.inputIncomplete), Toast.LENGTH_SHORT).show();
            return null;
        }
        return edits;
    }
    public void loadingDisplay(){
        loading.show();
    }
    public void loadingDismiss(){
        loading.dismiss();
    }
}
