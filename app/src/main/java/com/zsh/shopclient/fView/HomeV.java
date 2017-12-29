package com.zsh.shopclient.fView;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.zsh.shopclient.R;
import com.zsh.shopclient.aPresenter.BaseActivity;
import com.zsh.shopclient.aPresenter.CommodityParticularsActivity;
import com.zsh.shopclient.aPresenter.MainActivity;
import com.zsh.shopclient.aPresenter.ShopActivity;
import com.zsh.shopclient.aView.BaseView;
import com.zsh.shopclient.aView.MainV;
import com.zsh.shopclient.adapter.PageAdapter;
import com.zsh.shopclient.adapter.RecommendShopsAdapter;
import com.zsh.shopclient.adapter.TopImageBelowAdapter;
import com.zsh.shopclient.adapter.UpImageBelowTextAdapter;
import com.zsh.shopclient.fPresenter.BaseFragment;
import com.zsh.shopclient.fPresenter.HomeFragment;
import com.zsh.shopclient.fPresenter.ShotcutFragment;
import com.zsh.shopclient.tool.RecyclerLimit;
import com.zsh.shopclient.widget.RecyclerImage;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import butterknife.BindView;

/**
 * Created by Administrator on 2017/11/5.
 */

public class HomeV extends BaseView {
    //@BindView(R.id.spinner_loaction)Spinner spinner;
    @BindView(R.id.text_location)TextView location;
    @BindView(R.id.search)SearchView search;
    @BindView(R.id.recyclerImage)RecyclerImage imageSlide;
    @BindView(R.id.pager)ViewPager shotcut;
    @BindView(R.id.recycler_vipMerchant)RecyclerView vipMerchant;
    @BindView(R.id.recycler_flashSale)RecyclerView flashSale;
    @BindView(R.id.recycler_recommendMerchant)RecyclerView recommendMerchant;
    private PopupWindow popupWindow;
    private PageAdapter pagerAdapter;
    private List<BaseFragment> fragments;
    @Override
    protected int getLayoutId() {
        return R.layout.fragment_home;
    }

    @Override
    public void initView() {
        MainActivity activity = (MainActivity)getRootView().getContext();
        /*ArrayAdapter adapter = new ArrayAdapter<>(activity,R.layout.item_spinner_location,new String[]{"c/c++","java","python","objectC","html"});
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);//android.R.layout.simple_spinner_item);
        spinner.setAdapter(adapter);
        spinner.setSelection(0,true);*/
        View.OnClickListener clickListener = ((HomeFragment)((MainV)activity.getTypeView()).getIndexFragment(0)).getClickListener();
        location.setOnClickListener(clickListener);
        getRootView().findViewById(R.id.image_share).setOnClickListener(clickListener);
        getRootView().findViewById(R.id.text_scanQr).setOnClickListener(clickListener);
        search.setIconified(false);
        search.clearFocus();
        search.setOnQueryTextListener(((HomeFragment)((MainV)activity.getTypeView()).getIndexFragment(0)).getOnQueryTextViewListener());
        getRootView().findViewById(R.id.image_signIn).setOnClickListener(clickListener);
        fragments = new ArrayList<>();
        fragments.add(new ShotcutFragment());
        pagerAdapter = new PageAdapter(activity.getSupportFragmentManager(),fragments);
        shotcut.setAdapter(pagerAdapter);
        getRootView().findViewById(R.id.image_specialOffer).setOnClickListener(clickListener);
        getRootView().findViewById(R.id.image_currencyShop).setOnClickListener(clickListener);
        getRootView().findViewById(R.id.image_activity).setOnClickListener(clickListener);
        getRootView().findViewById(R.id.image_luck).setOnClickListener(clickListener);
    }
    public void clearFocus(){
        search.clearFocus();//必调否则每次打开或退回键盘都弹出来
    }
    @Override
    public void initWidget() {
        super.initWidget();
        BaseActivity activity = (BaseActivity) getRootView().getContext();
        View view = LayoutInflater.from(activity).inflate(R.layout.popupwindow_recycler,null);
        ((RecyclerView)view).setLayoutManager(new LinearLayoutManager(activity,LinearLayoutManager.VERTICAL,false));
        ((RecyclerView)view).setItemAnimator(new DefaultItemAnimator());
        ((RecyclerView)view).addItemDecoration(new RecyclerLimit(activity,(byte) LinearLayoutManager.HORIZONTAL));
        popupWindow = new PopupWindow(view,ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT,true);
        popupWindow.setOutsideTouchable(true);//设置popwindow如果点击外面区域，便关闭。
        popupWindow.setTouchable(true);
        popupWindow.setBackgroundDrawable(new ColorDrawable(ContextCompat.getColor(activity,R.color.white)));//不设置背景点击外面没办法关闭
        popupWindow.setAnimationStyle(R.style.DrawerAlphaAnimation);
        popupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                backgroundAlpha(1f);
            }
        });
    }
    public void displayPopupWindow(final List<Map<Map<String, Object>, List<Map<String, Object>>>> list){
        ((BaseActivity)getRootView().getContext()).runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if(null == popupWindow) return;
                ((RecyclerView)popupWindow.getContentView()).setAdapter(new RecommendShopsAdapter((BaseActivity) getRootView().getContext(),list));
                popupWindow.showAsDropDown(search,0, (int)getRootView().getContext().getResources().getDimension(R.dimen.margin8));
                backgroundAlpha(0.6f);
                search.clearFocus();
            }
        });
    }
    private void backgroundAlpha(final float alpha){
        WindowManager.LayoutParams lp = ((BaseActivity)getRootView().getContext()).getWindow().getAttributes();
        lp.alpha = alpha;
        ((BaseActivity)getRootView().getContext()).getWindow().setAttributes(lp);
    }
    public void hindKeyboard(){//细解读Android中的搜索框（三）—— SearchView https://www.cnblogs.com/tianzhijiexian/p/4226675.html
        InputMethodManager imm = (InputMethodManager)getRootView().getContext().getSystemService(Context.INPUT_METHOD_SERVICE);//得到输入管理对象
        if(null != imm)
            imm.hideSoftInputFromWindow(search.getWindowToken(),0);//输入法如果是显示状态，那么就隐藏输入法
        search.clearFocus();//不获取焦点
    }
    public void initRecyclerImage(final List<String[]> imageInfos){
        /*imageSlide.addImageUrl("https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1509855153634&di=2d899520318632442fb037fcc5e80cd3&imgtype=0&src=http%3A%2F%2Fimg.kutoo8.com%2Fupload%2Fimage%2F98723394%2F28_960x540.jpg");
        imageSlide.addImageUrl("https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1509964620465&di=650c6f7a42e80b30f549c03b529f734d&imgtype=0&src=http%3A%2F%2Fwww.pp3.cn%2Fuploads%2F201510%2F2015100507.jpg");
        imageSlide.addImageUrl("https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1509964656953&di=5b093b72411725c7e8760bcf8d2582e5&imgtype=0&src=http%3A%2F%2Fwww.pp3.cn%2Fuploads%2F201410%2F2014102406.jpg");
        imageSlide.addImageUrl("https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1509964725798&di=52430771c2f1141776194568278972da&imgtype=0&src=http%3A%2F%2Fwww.pp3.cn%2Fuploads%2F201411%2F2014110406.jpg");
        imageSlide.addImageUrl("https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1509964760055&di=85edaaf1d028bc544b382a943848fd18&imgtype=0&src=http%3A%2F%2Fbpic.ooopic.com%2F16%2F09%2F03%2F16090347-66e506bde737aa4c806fe24f47062ab0.jpg");
        imageSlide.addImageUrl("https://ss3.baidu.com/-fo3dSag_xI4khGko9WTAnF6hhy/image/h%3D220/sign=7486be2e9b8fa0ec60c7630f1697594a/b17eca8065380cd78775def0ab44ad3459828147.jpg");
        imageSlide.addImageUrl("https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1509964837552&di=5b600d4855da9e152f97aedc87c32db5&imgtype=0&src=http%3A%2F%2Fwww.pp3.cn%2Fuploads%2F201411%2F2014110104.jpg");
        imageSlide.addImageUrl("https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1509964875521&di=b7936c3bf783a4a6fb7966c0168f7240&imgtype=0&src=http%3A%2F%2Fimg5.web07.cn%2FUPics%2FBizhi%2F2016%2F1208%2F237534081150231.jpg");*/
        ((BaseActivity)getRootView().getContext()).runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if(null == fragments.get(0).getTypeView())return;
                String url = getRootView().getContext().getString(R.string.ip) + getRootView().getContext().getString(R.string.recyclerImagePath);
                for (String[] array : imageInfos)
                    imageSlide.addImageUrl(url + array[0]);
                imageSlide.commit();
            }
        });
    }
    public void initVipMerchant(final MainActivity activity,final List<CharSequence[]> list){
        activity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if(null == fragments.get(0).getTypeView())return;
                if(0< list.size()) {
                    UpImageBelowTextAdapter vipMerchantAdapter = new UpImageBelowTextAdapter(activity, list, R.layout.item_image_text1);
                    Intent intent = new Intent(getRootView().getContext(), ShopActivity.class);
                    intent.putExtra(activity.getString(R.string.requestParam),"GeneralGoods");
                    vipMerchantAdapter.setIntent(intent);
                    vipMerchant.setLayoutManager(new GridLayoutManager(activity, list.size() >= 4 ? 4 : list.size(), GridLayoutManager.VERTICAL, false));
                    vipMerchant.setItemAnimator(new DefaultItemAnimator());
                    vipMerchant.setAdapter(vipMerchantAdapter);
                }
            }
        });
    }
    public void initFlashSale(final MainActivity activity,final List<CharSequence[]> list){
        activity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if(null == fragments.get(0).getTypeView())return;
                UpImageBelowTextAdapter flashSaleAdapter = new TopImageBelowAdapter(activity,list,R.layout.item_image_text1);
                flashSaleAdapter.setIntent(new Intent(getRootView().getContext(), CommodityParticularsActivity.class));
                flashSale.setLayoutManager(new LinearLayoutManager(activity,LinearLayoutManager.HORIZONTAL,false));
                flashSale.setItemAnimator(new DefaultItemAnimator());
                flashSale.setAdapter(flashSaleAdapter);
            }
        });
    }
    public void initRecommendMerchant(final MainActivity activity, final List<Map<Map<String,Object>,List<Map<String,Object>>>> map){
        activity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if(null == fragments.get(0).getTypeView())return;
                recommendMerchant.setLayoutManager(new LinearLayoutManager(activity,LinearLayoutManager.VERTICAL,false));
                recommendMerchant.setItemAnimator(new DefaultItemAnimator());
                recommendMerchant.addItemDecoration(new RecyclerLimit(activity,(byte) LinearLayoutManager.HORIZONTAL));
                recommendMerchant.setAdapter(new RecommendShopsAdapter(activity,map));
            }
        });
    }
    public void signIn(final int signInDay){
        ((MainActivity)getRootView().getContext()).runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if(null == popupWindow)return;
                LinearLayout view =  (LinearLayout) LayoutInflater.from(getRootView().getContext()).inflate(R.layout.popupwindow_signin,null);
                ((TextView)view.getChildAt(0)).setText("+"+signInDay+getRootView().getContext().getString(R.string.gold));
                ((TextView)view.getChildAt(1)).setText(getRootView().getContext().getString(R.string.tomorrowSignIn)+"+"+(1+signInDay)+getRootView().getContext().getString(R.string.gold));
                popupWindow = new PopupWindow(view,ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT,true);
                popupWindow.setOutsideTouchable(true);//设置popwindow如果点击外面区域，便关闭。
                popupWindow.setTouchable(true);
                popupWindow.setBackgroundDrawable(new ColorDrawable(ContextCompat.getColor(getRootView().getContext(),R.color.lucency)));//不设置背景点击外面没办法关闭
                popupWindow.setAnimationStyle(R.style.DrawerAlphaAnimation);
                popupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
                    @Override
                    public void onDismiss() {
                        backgroundAlpha(1f);
                    }
                });
                popupWindow.showAtLocation(((MainActivity)getRootView().getContext()).getWindow().getDecorView(), Gravity.CENTER,0,0);
                backgroundAlpha(0.6f);
            }
        });
    }
    @Override
    public void onDestroy() {
        imageSlide.releaseResource();
        super.onDestroy();
    }
    public void setLocation(String address){
        location.setText(address);
    }
    public BaseFragment getFragment(int index){
        return  fragments.get(index);
    }
}
