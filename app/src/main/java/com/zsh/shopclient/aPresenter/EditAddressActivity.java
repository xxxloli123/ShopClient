package com.zsh.shopclient.aPresenter;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.ListFragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.DisplayUtil;
import com.amap.api.location.AMapLocation;
import com.amap.api.maps2d.AMap;
import com.amap.api.maps2d.AMapOptions;
import com.amap.api.maps2d.CameraUpdate;
import com.amap.api.maps2d.CameraUpdateFactory;
import com.amap.api.maps2d.MapView;
import com.amap.api.maps2d.model.BitmapDescriptorFactory;
import com.amap.api.maps2d.model.CameraPosition;
import com.amap.api.maps2d.model.LatLng;
import com.amap.api.maps2d.model.Marker;
import com.amap.api.maps2d.model.MarkerOptions;
import com.amap.api.services.core.LatLonPoint;
import com.amap.api.services.core.PoiItem;
import com.amap.api.services.poisearch.PoiResult;
import com.amap.api.services.poisearch.PoiSearch;
import com.slowlife.map.interfaces.APSImpl;
import com.slowlife.map.interfaces.APSInterface;
import com.zsh.shopclient.R;
import com.zsh.shopclient.adapter.Baseadapter;
import com.zsh.shopclient.widget.PagerSlidingTabStrip;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

import static android.text.TextUtils.isEmpty;

/**
 * Created by Administrator on 2017/1/23 0023.
 */

public class EditAddressActivity extends AppCompatActivity implements View.OnClickListener,
        AdapterView.OnItemClickListener, APSInterface.OnApsChanged,
        PoiSearch.OnPoiSearchListener, TextWatcher{
    @BindView(R.id.mapView)
    MapView mapView;
    @BindView(R.id.pager_tabs)
    PagerSlidingTabStrip tabs;
    @BindView(R.id.addr_pager)
    ViewPager pager;
    @BindView(R.id.addr_search)
    EditText search;
    @BindView(R.id.listView)
    ListView listView;
    @BindView(R.id.layout_default)
    View defView;
    PoiSearch poiSearch;
    private PopupWindow win;
    private ListView winListView;
    private Baseadapter<PoiItem> winAdapter;
    private APSImpl aps;
    private AMap aMap;
    private PagerAdapter pagerAdapter;
    private AMapLocation location;

    private Baseadapter<PoiItem> adapter;
    private Object object = new Object();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_address);
        ButterKnife.bind(this);
        mapView.onCreate(savedInstanceState);
        init();
    }


    /**
     * 初始化控件
     */
    protected void init() {
        aps = new APSImpl(this);
        aps.onCreate();
        aps.addListener(this);
        pagerAdapter = new PagerAdapter(getSupportFragmentManager());
        pager.setAdapter(pagerAdapter);
        tabs.setViewPager(pager);
        if (aMap == null) {
            if (mapView == null) return;
            aMap = mapView.getMap();
        }
        search.addTextChangedListener(this);
        listView.setOnItemClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.back_rl:
                finish();
                break;
        }
    }

    public void finish(Intent intent) {
        setResult(Activity.RESULT_OK, intent);
        finish();
    }


    @Override
    public void onChanged(AMapLocation location) {
        LatLng latLng = new LatLng(location.getLatitude(), location.getLongitude());
        CameraPosition cameraPosition = new CameraPosition(latLng, 18, 30, 30);
        AMapOptions aOptions = new AMapOptions();
        aOptions.camera(cameraPosition);
        CameraUpdate cameraUpdate = CameraUpdateFactory.newCameraPosition(cameraPosition);
        aMap.animateCamera(cameraUpdate);
        Marker marker = aMap.addMarker(new MarkerOptions().position(latLng)
                .icon(BitmapDescriptorFactory
                        .defaultMarker(BitmapDescriptorFactory.HUE_RED)));
        this.location = location;
        pagerAdapter.getItem(pager.getCurrentItem()).loadData();
        search();
    }

    @Override
    public void Fail() {
        Toast.makeText(this, "定位失败", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Intent intent = new Intent();
        intent.putExtra(getString(R.string.returnData), adapter.getData().get(position));
        setResult(Activity.RESULT_OK, intent);
        finish();
    }


    class PagerAdapter extends FragmentPagerAdapter {
        public PagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public int getCount() {
            return frags.length;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return titles[position];
        }

        @Override
        public Frag getItem(int position) {
            return frags[position];
        }

        Frag[] frags = new Frag[]{
                new Frag(""),
                new Frag("写字楼|楼宇|建筑"),
                new Frag("小区|住宅"),
                new Frag("学校|高校")
        };
        String[] titles = new String[]{
                "全部", "写字楼", "小区", "学校"
        };
    }


    @SuppressLint("ValidFragment")
    public static class Frag extends ListFragment implements PoiSearch.OnPoiSearchListener {
        PoiSearch poiSearch;
        private PoiSearch.Query query;
        Adapter adapter;
        String arg;

        public Frag() {
            super();
        }

        public Frag(String arg) {
            this();
            Bundle bundle = new Bundle();
            bundle.putString("param", arg);
            setArguments(bundle);
        }

        @Override
        public void onViewCreated(View view, Bundle savedInstanceState) {
            super.onViewCreated(view, savedInstanceState);
            // 第一个参数表示搜索字符串，第二个参数表示poi搜索类型，第三个参数表示poi搜索区域（空字符串代表全国）
            loadData();
        }

        public void loadData() {
            List<PoiItem> pois = getArguments().getParcelableArrayList("pois");
            arg = getArguments().getString("param");
            if (!getUserVisibleHint() || getView() == null || pois != null)
                return;
            if (adapter == null) {
                adapter = new Adapter(getView().getContext(), null);
                setListAdapter(adapter);
            }
            if (arg == "") {
                adapter.local = ((EditAddressActivity) getActivity()).location;
                if (adapter.local == null) return;
            }
            if (pois != null) {
                adapter.notifyDataSetChanged(pois);
                return;
            }
            search();
        }

        @Override
        public void onListItemClick(ListView l, View v, int position, long id) {
            EditAddressActivity act = (EditAddressActivity) getActivity();
            Intent intent = new Intent();
            if (adapter.local != null) {
                if (position != 0)
                    intent.putExtra("addr", adapter.getItem(--position));
                else {
                    intent.putExtra("addr", adapter.local);
                }
            } else intent.putExtra("addr", adapter.getItem(position));
            act.finish(intent);
        }

        @Override
        public void setUserVisibleHint(boolean isVisibleToUser) {
            super.setUserVisibleHint(isVisibleToUser);
            if (adapter == null || adapter.getCount() == 0) loadData();
        }

        public void search() {
            AMapLocation addr = ((EditAddressActivity) getActivity()).location;
            if (addr == null) return;
            search(addr.getCity(), new LatLonPoint(addr.getLatitude(), addr.getLongitude()));
        }

        public void search(String city, LatLonPoint point) {
            query = new PoiSearch.Query(arg, "", city);
            query.setPageSize(30);// 设置每页最多返回多少条poiitem
            query.setPageNum(1);// 设置查第一页
            poiSearch = new PoiSearch(getContext(), query);
            poiSearch.setOnPoiSearchListener(this);
            poiSearch.searchPOIAsyn();// 异步搜索
            // 设置搜索区域为以lp点为圆心，其周围3000米范围
            poiSearch.setBound(new PoiSearch.SearchBound(new LatLonPoint(point.getLatitude(),
                    point.getLongitude()), 1000));//设置周边搜索的中心点以及半径
        }

        @Override
        public void onPoiSearched(PoiResult poiResult, int i) {
            adapter.notifyDataSetChanged(poiResult.getPois());
            getArguments().putParcelableArrayList("pois", poiResult.getPois());
        }

        @Override
        public void onPoiItemSearched(PoiItem poiItem, int i) {

        }

    }


    static class Adapter extends Baseadapter<PoiItem> {
        AMapLocation local;

        public Adapter(Context context, List<PoiItem> list) {
            super(context, list);
        }

        @Override
        public int getCount() {
            if (local != null) return super.getCount() + 1;
            return super.getCount();
        }

        @Override
        protected View getView(View view, int position) {
            ViewHolder vh = (ViewHolder) view.getTag();
            if (vh == null) {
                vh = new ViewHolder(view);
                view.setTag(vh);
            }
            if (local != null && position == 0) {
                position++;
                vh.describe.setText(String.format("%s%s", local.getCity(), local.getDistrict()));
                vh.addr.setText(local.getStreet());
                vh.local.setVisibility(View.VISIBLE);
                return view;
            } else {
                if (vh.local.getVisibility() == View.VISIBLE)
                    vh.local.setVisibility(View.GONE);
            }
            if (local != null) position--;
            PoiItem item = list.get(position);
            vh.describe.setText(String.format("%s%s%s", item.getCityName(), item.getDirection(), item.getSnippet()));
            vh.addr.setText(item.getTitle());
            return view;
        }

        @Override
        protected int getLayoutId(int position) {
            return R.layout.item_addr;
        }
    }

    static class ViewHolder {
        @BindView(R.id.local)
        View local;
        @BindView(R.id.address)
        TextView addr;
        @BindView(R.id.addr_describe)
        TextView describe;

        public ViewHolder(View v) {
            ButterKnife.bind(this, v);
        }
    }

    /**
     * poisearch popupwindow
     */
    private void showPoiPopWin(final List<PoiItem> pois) {
        if (win == null) {
            win = new PopupWindow(this);
            winListView = new ListView(this);
            final int itemPad = DisplayUtil.dip2px(this, 5);
            winAdapter = new Baseadapter<PoiItem>(this, pois) {

                @Override
                public int getCount() {
                    return list.size();
                }

                @Override
                public View getView(int position, View convertView, ViewGroup parent) {
                    if (convertView == null) {
                        convertView = new TextView(EditAddressActivity.this);
                        convertView.setPadding(itemPad, itemPad, itemPad, itemPad);
                        convertView.setBackgroundColor(Color.WHITE);
                    }
                    TextView tv = (TextView) convertView;
                    PoiItem poi = list.get(position);
                    tv.setText(poi.getTitle());
                    return tv;
                }

                @Override
                protected View getView(View view, int position) {
                    return null;
                }

                @Override
                protected int getLayoutId(int position) {
                    return 0;
                }
            };
            winListView.setAdapter(winAdapter);
            win.setContentView(winListView);
            win.setWidth(search.getMeasuredWidth());
            win.setHeight(getResources().getDisplayMetrics().heightPixels / 3);
            win.setBackgroundDrawable(new ColorDrawable(Color.parseColor("#00000000")));
            win.setOutsideTouchable(true);
            win.setFocusable(true);
            win.setTouchable(true);
            winListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    Intent intent = new Intent();
                    intent.putExtra("addr", pois.get(position));
                    setResult(Activity.RESULT_OK, intent);
                    win.dismiss();
                }
            });
            win.setTouchInterceptor(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    if (event.getAction() == MotionEvent.ACTION_OUTSIDE
                            || event.getAction() == MotionEvent.ACTION_CANCEL) {
                        win.dismiss();
                        return true;
                    }
                    return false;
                }
            });
        } else {
            winAdapter.notifyDataSetChanged(pois);
        }
        win.showAsDropDown(search);
    }

    @Override
    protected void onResume() {
        super.onResume();
        mapView.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (mapView != null)
            mapView.onPause();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mapView != null)
            mapView.onDestroy();
    }


    private void search() {
        synchronized (object) {
            String keyWord = search.getText().toString().trim();
            if (isEmpty(keyWord)) return;
            PoiSearch.Query query = new PoiSearch.Query(keyWord, "",
                    location == null ? "" : location.getCity());
            query.setPageSize(30);// 设置每页最多返回多少条poiitem
            query.setPageNum(1);// 设置查第一页
            poiSearch = new PoiSearch(this, query);
            poiSearch.setOnPoiSearchListener(this);
            poiSearch.searchPOIAsyn();// 异步搜索
            // 设置搜索区域为以lp点为圆心，其周围3000米范围
//        poiSearch.setBound(new PoiSearch.SearchBound(new LatLonPoint(location.getLatitude(),
//                location.getLongitude()), 1000));//设置周边搜索的中心点以及半径
        }
    }

    @Override
    public void onPoiSearched(PoiResult poiResult, int i) {
        List<PoiItem> list = poiResult.getPois();
        if (adapter == null) {
            adapter = new Adapter(this, list);
            listView.setAdapter(adapter);
        } else adapter.notifyDataSetChanged(list);
        if (listView.getVisibility() != View.VISIBLE) {
            listView.setVisibility(View.VISIBLE);
            defView.setVisibility(View.GONE);
        }
    }

    @Override
    public void onPoiItemSearched(PoiItem poiItem, int i) {

    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {

    }

    @Override
    public void afterTextChanged(Editable s) {
        search();
    }
}
