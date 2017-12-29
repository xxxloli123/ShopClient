package com.zsh.shopclient.aView;

import android.content.Context;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.view.inputmethod.InputMethodManager;
import android.widget.LinearLayout;

import com.zsh.shopclient.R;
import com.zsh.shopclient.aPresenter.SearchActivity;
import com.zsh.shopclient.adapter.SearchAdapter;
import com.zsh.shopclient.tool.RecyclerLimit;

import java.util.List;

import butterknife.BindView;

/**
 * Created by Administrator on 2017/11/29.
 */

public class SearchV extends BaseView {
    @BindView(R.id.search)SearchView search;
    @BindView(R.id.recycler)RecyclerView recycler;
    @Override
    protected int getLayoutId() {
        return R.layout.activity_search;
    }

    @Override
    public void initView() {
        SearchActivity activity = (SearchActivity) getRootView().getContext();
        getRootView().findViewById(R.id.image_goBack).setOnClickListener(activity.getClickListener());
        search.setIconified(false);
        search.setOnQueryTextListener(activity.getOnQueryTextViewListener());
        search.setOnCloseListener(activity.getCloseListener());
        recycler.setLayoutManager(new LinearLayoutManager(activity,LinearLayoutManager.VERTICAL,false));
        recycler.addItemDecoration(new RecyclerLimit(activity, (byte) LinearLayout.HORIZONTAL));
        recycler.setItemAnimator(new DefaultItemAnimator());
        recycler.setAdapter(new SearchAdapter(activity,null));
    }
    public void searchResult(final List<CharSequence[]>list){
        ((SearchActivity)getRootView().getContext()).runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if(null == recycler)return;
                ((SearchAdapter)recycler.getAdapter()).update(list);
            }
        });
    }
    public void hindKeyboard(){//细解读Android中的搜索框（三）—— SearchView https://www.cnblogs.com/tianzhijiexian/p/4226675.html
        InputMethodManager imm = (InputMethodManager)getRootView().getContext().getSystemService(Context.INPUT_METHOD_SERVICE);//得到输入管理对象
        if(null != imm)
            imm.hideSoftInputFromWindow(search.getWindowToken(),0);//输入法如果是显示状态，那么就隐藏输入法
        search.clearFocus();//不获取焦点
    }
}
