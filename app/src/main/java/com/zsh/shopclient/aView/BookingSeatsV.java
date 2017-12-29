package com.zsh.shopclient.aView;

import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.zsh.shopclient.R;
import com.zsh.shopclient.aPresenter.BookingSeatsActivity;

import butterknife.BindView;

/**
 * Created by Administrator on 2017/12/12.
 */

public class BookingSeatsV extends BaseView {
    @BindView(R.id.edit_linkmanContacts)EditText linkmanContacts;
    @BindView(R.id.edit_contactPhone)EditText contactPhone;
    @BindView(R.id.checkbox)CheckBox checkBox;
    @Override
    protected int getLayoutId() {
        return R.layout.activity_booking_seats;
    }

    @Override
    public void initView() {
        View.OnClickListener listener = ((BookingSeatsActivity)getRootView().getContext()).getClickListener();
        getRootView().findViewById(R.id.image_goBack).setOnClickListener(listener);
        ((TextView)getRootView().findViewById(R.id.text_title)).setText(R.string.tableReservation);
        getRootView().findViewById(R.id.text_sumbit).setOnClickListener(listener);
    }
    public CharSequence[] getRelation(){
        String name =  linkmanContacts.getText().toString().trim();
        String phone =  contactPhone.getText().toString().trim();
        if("".equals(name) || "".equals(phone)) {
            Toast.makeText(getRootView().getContext(),getRootView().getContext().getString(R.string.inputIncomplete),Toast.LENGTH_SHORT).show();
            return null;
        }
        return new CharSequence []{name,phone,checkBox.isChecked() ? "yes":"no"};
    }
}
