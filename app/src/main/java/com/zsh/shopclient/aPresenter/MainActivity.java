package com.zsh.shopclient.aPresenter;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.provider.MediaStore;
import android.support.annotation.IdRes;
import android.support.v4.content.FileProvider;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationListener;
import com.xxx.skynet.sharedPreference.IOSharedPreferences;
import com.zsh.shopclient.BuildConfig;
import com.zsh.shopclient.R;
import com.zsh.shopclient.fPresenter.HomeFragment;
import com.zsh.shopclient.aView.BaseView;
import com.zsh.shopclient.aView.MainV;
import com.zsh.shopclient.fPresenter.OrderFragment;
import com.zsh.shopclient.fView.MyV;
import com.zsh.shopclient.interfaceconfig.Config;
import com.zsh.shopclient.tool.InstallUtils;
import com.zsh.shopclient.tool.SimpleCallback;
import com.zsh.shopclient.tool.UnitUtil;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.UUID;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;


public class MainActivity extends BaseActivity {
    public static final int SCAN_QR = 1;
    public static final int ADDRESS = 2;
    public static final int RETURN_USER = 3;
    private AMapLocation location;
    private AMapLocationClient locationClient;
    private AMapLocationClientOption locationOption;
    private static String mImagePath = Environment.getExternalStorageDirectory()+"/meta/";
    private static File camera, clipping;
    private static final int CROP_CODE = 233;

    public void update(final int versionCode) {
        Request req = new Request.Builder()
                .tag("")
                .url(Config.Url.getUrl(Config.UPDATE)).build();
        new OkHttpClient().newCall(req).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                try {
                    JSONObject json = new JSONObject(response.body().string());
                    int ver = json.getJSONObject("appVersion").getInt("version");
                    if (ver > versionCode) {
                        handler.post(new Runnable() {
                            @Override
                            public void run() {
                                upData2();
                            }
                        });
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

        });
    }

    private static Handler handler = new Handler();

    public void upData2() {
        Toast.makeText(this, "检测到新的版本，自动为您下载。。。", Toast.LENGTH_SHORT).show();
        new InstallUtils(this, Config.Url.getUrl("/slowlife/share/appdownload?type=android"), "惠递",
                new InstallUtils.DownloadCallBack() {
                    @Override
                    public void onStart() {
                    }

                    @Override
                    public void onComplete(String path) {
                        InstallUtils.installAPK(MainActivity.this, path, getPackageName() + ".provider", new InstallUtils.InstallCallBack() {
                            @Override
                            public void onSuccess() {
                                Toast.makeText(MainActivity.this, "正在安装程序", Toast.LENGTH_SHORT).show();
                            }

                            @Override
                            public void onFail(Exception e) {
                                Toast.makeText(MainActivity.this, "安装失败:" + e.toString(), Toast.LENGTH_SHORT).show();
                                Log.e("onFail", "安装失败:" + e.toString());
                            }
                        });
                    }

                    @Override
                    public void onLoading(long total, long current) {
//                            orderHandle.setText((int) (current * 100 / total) + "%");
                    }

                    @Override
                    public void onFail(Exception e) {
                        Toast.makeText(MainActivity.this, "下载失败:" + e.toString(), Toast.LENGTH_SHORT).show();
                        Log.e("onFail", "下载失败:" + e.toString());
                    }
                }).downloadAPK();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        aMapLocatioin();
        update(UnitUtil.getVersionCode(this));
        HashMap<String,Object> hashMap = new HashMap<>();
        //hashMap.put("phone","13456789999");
        //List<List<String>> lists = new TypeSqliteHelper(this,getString(R.string.sqlLibrary),null,1,null).query("user",new String[]{"id","type"},hashMap);
        //List<List<String>> lists = new TypeSqliteHelper(this,getString(R.string.sqlLibrary),null,1,null).rawQuery("user",hashMap);
       /*List<List<String>> lists = new TypeSqliteHelper(this,getString(R.string.sqlLibrary),null,1,null).query("user",new String[]{"id"},null);
        Log.d("MainActivity","onCreate->"+lists);
        if(0< lists.size()) {
            userId = lists.get(0).get(0);
            IOSharedPreferences.outString(this,getString(R.string.user),getString(R.string.userId),userId);
        }*/
        //startActivityForResult(new Intent(this,LoginActivity.class),RETURN_USER);
    }

    public View.OnClickListener getClickListener(){
        return new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                popwindowsshow();
            }
        };
    }

    public void popwindowsshow() {
        final PopupWindow pop = new PopupWindow();
        View view = getLayoutInflater().inflate(R.layout.popwindows_head, null);
        final LinearLayout popwindows = (LinearLayout) view.findViewById(R.id.ll_popup);
        pop.setWidth(ViewGroup.LayoutParams.MATCH_PARENT);
        pop.setHeight(ViewGroup.LayoutParams.WRAP_CONTENT);
        pop.setBackgroundDrawable(new BitmapDrawable());
        pop.setFocusable(true);
        pop.setOutsideTouchable(true);
        pop.setContentView(view);
        RelativeLayout parent = (RelativeLayout) view.findViewById(R.id.parent);
        Button bt1 = (Button) view.findViewById(R.id.item_popupwindows_camera);
        Button bt2 = (Button) view.findViewById(R.id.item_popupwindows_photo);
        Button bt3 = (Button) view.findViewById(R.id.item_popupwindows_cancel);
        parent.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                pop.dismiss();
                popwindows.clearAnimation();
            }
        });
        bt1.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {//拍照
                Intent it = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                if (it.resolveActivity(getPackageManager()) != null) {
                    File path = new File(mImagePath);
                    if (!path.exists()) {
                        path.mkdir();
                    }
                    camera = new File(mImagePath, UUID.randomUUID().toString()+".jpg");
                    Uri photoUri = FileProvider.getUriForFile(MainActivity.this,
                            getPackageName() + ".provider", camera);

                    it.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                    it.putExtra(MediaStore.EXTRA_OUTPUT, photoUri);
                    startActivityForResult(it, 55);
                }
                pop.dismiss();
                popwindows.clearAnimation();
            }
        });
        bt2.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {//从相册中选择
                Intent local =new  Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                local.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/*");
                startActivityForResult(local, 22);
                pop.dismiss();
                popwindows.clearAnimation();
            }
        });
        bt3.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                pop.dismiss();
                popwindows.clearAnimation();
            }
        });
        pop.showAtLocation(view, Gravity.BOTTOM, 0, 0);
    }

    private void aMapLocatioin(){
        locationClient = new AMapLocationClient(this);
        locationOption = new AMapLocationClientOption();
        locationOption.setLocationMode(AMapLocationClientOption.AMapLocationMode.Hight_Accuracy);
        locationOption.setInterval(0);
        locationClient.setLocationOption(locationOption);
        locationClient.setLocationListener(new AMapLocationListener() {
            @Override
            public void onLocationChanged(AMapLocation aMapLocation) {
                if(null != aMapLocation)
                    if(0 == aMapLocation.getErrorCode()){
                        location = aMapLocation;
                        getIntent().putExtra(getString(R.string.province),aMapLocation.getProvince());
                        getIntent().putExtra(getString(R.string.city),location.getCity());
                        getIntent().putExtra(getString(R.string.district),aMapLocation.getDistrict());
                        getIntent().putExtra(getString(R.string.street),aMapLocation.getStreet());
                        getIntent().putExtra(getString(R.string.streetNum),aMapLocation.getStreetNum());
                        getIntent().putExtra(getString(R.string.longitude),aMapLocation.getLongitude());
                        getIntent().putExtra(getString(R.string.latitude),aMapLocation.getLatitude());
                        ((HomeFragment)((MainV)getTypeView()).getIndexFragment(0)).linkNet(location,IOSharedPreferences.inString(MainActivity.this,getString(R.string.user),getString(R.string.userId)));
                        locationClient.stopLocation();
                        locationClient.onDestroy();
                    }else
                        Log.e("MainActivity", "location Error, ErrCode:" + aMapLocation.getErrorCode() + ", errInfo:" + aMapLocation.getErrorInfo());
            }
        });
        locationClient.startLocation();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        locationClient.stopLocation();
        locationClient.onDestroy();
    }

    @Override
    protected BaseView createTypeView() {
        return new MainV();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(null != data)
            switch (requestCode){
                case SCAN_QR:
                    Log.i("MainActivity",data.getStringExtra("scanResult"));
                    break;
                case ADDRESS:
                    Log.i("MainActivity",data.getStringExtra(getString(R.string.returnData)));
                    break;
                case RETURN_USER:
                    Log.i("MainActivity",data.getExtras().get(getString(R.string.returnData)).toString());
                    break;
                case OrderInfoActivity.EVALUATED:
                    ((OrderFragment)((MainV)getTypeView()).getIndexFragment(3)).setStatus(data.getBooleanExtra(getString(R.string.result),false));
                    break;
            }
        if (resultCode == Activity.RESULT_OK) {
            switch (requestCode) {
                case 22:
                    Uri uri = data.getData();
                    if (uri == null) {
                        Toast.makeText(this, "选择图片文件读取出错", Toast.LENGTH_LONG).show();
                        return;
                    }
                    startImageZoom(uri);
                    break;
                case 55:
                    startImageZoom(FileProvider.getUriForFile(this, BuildConfig.APPLICATION_ID  +
                            ".provider", camera));
                    break;
                case CROP_CODE:
                    if (data != null) {
                        try {
                            String fils = clipping.getAbsolutePath();
                            compressPicture(fils,clipping.getAbsoluteFile());
                            submitImg();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }else Toast.makeText(this, "图片文件剪裁出错", Toast.LENGTH_LONG).show();
                    break;
            }
        }
    }

    private void submitImg() {
        FileInputStream fis = null;
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        try {
            fis = new FileInputStream(clipping);
            int len;
            byte[] buf = new byte[512];
            while ((len = fis.read(buf)) > 0) {
                baos.write(buf, 0, len);
                baos.flush();
            }
            byte[] fileData = baos.toByteArray();
            fis.close();
            baos.close();
            RequestBody fileRequest1 = RequestBody.create(MediaType.parse("application/octet-stream"), fileData);
            RequestBody requestBody = new MultipartBody.Builder().setType(MultipartBody.FORM)
//            userId :(必填)，type:(必填 0-头像，1-身份证，2，其他),pictures(必填)
//            参数：[userId, type, pictures]
                    .addFormDataPart("userId", IOSharedPreferences.inString(this,
                            getString(R.string.user), getString(R.string.userId)))
                    .addFormDataPart("type", "0")
                    .addFormDataPart("pictures", clipping.getName(), fileRequest1).build();
            Request request = new Request.Builder().url(Config.Url.getUrl(Config.Uploading_Img)).post(requestBody).build();
            new OkHttpClient().newCall(request).enqueue(new SimpleCallback(MainActivity.this,true) {
                @Override
                public void onSuccess(String tag, JSONObject json) throws JSONException {
                    MyV.headImage.setImageURI(Uri.fromFile(clipping));
                }
            });
        } catch (FileNotFoundException e) {
            Toast.makeText(MainActivity.this, "解析数据失败", Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        } catch (IOException e) {
            Toast.makeText(MainActivity.this, "读取图片出错", Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        }
    }

    /**
     * 按照图片尺寸压缩：
     */
    public void compressPicture(String srcPath, File desPath) {
        FileOutputStream fos = null;
        BitmapFactory.Options op = new BitmapFactory.Options();

        // 开始读入图片，此时把options.inJustDecodeBounds 设回true了
        op.inJustDecodeBounds = true;
        Bitmap bitmap = BitmapFactory.decodeFile(srcPath, op);
        op.inJustDecodeBounds = false;
        if (op.outWidth == 200f) return;
        // 缩放图片的尺寸
        float w = op.outWidth;
        float h = op.outHeight;
        float hh = 400f;//
        float ww = 400f;//
        // 最长宽度或高度1024
        float be = 1.0f;
//        if (w > h || w > ww) {
//            be = (float) (w / ww);
//        } else if (w < h || h > hh) {
//            be = (float) (h / hh);
//        }
        if (h > hh && w > ww) be = (float) (w / ww);
        if (be <= 0) {
            be = 1.0f;
        }
        op.inSampleSize = (int) be;// 设置缩放比例,这个数字越大,图片大小越小.
        // 重新读入图片，注意此时已经把options.inJustDecodeBounds 设回false了
        bitmap = BitmapFactory.decodeFile(srcPath, op);
        int desWidth = (int) (w / be);
        int desHeight = (int) (h / be);
        bitmap = Bitmap.createScaledBitmap(bitmap, desWidth, desHeight, true);
        try {
            fos = new FileOutputStream(desPath);
            if (bitmap != null) {
                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, fos);
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    private void startImageZoom(Uri uri) {
        clipping = new File(getExternalCacheDir(), UUID.randomUUID().toString() + ".png");
        //构建隐式Intent来启动裁剪程序
        Intent intent = new Intent("com.android.camera.action.CROP");
        //添加这一句表示对目标应用临时授权该Uri所代表的文件
        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        intent.addFlags(Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
        //设置数据uri和类型为图片类型
        intent.setDataAndType(uri, "image/*");
        //显示View为可裁剪的
        intent.putExtra("crop", true);
        //裁剪的宽高的比例为1:1
        intent.putExtra("aspectX", 1);
        intent.putExtra("aspectY", 1);
        //输出图片的宽高均为200
        intent.putExtra("outputX", 200);
        intent.putExtra("outputY", 200);
        //裁剪之后的数据是通过Intent返回
//        Uri clippingUri = FileProvider.getUriForFile(XXRZActivity.this,
//                getPackageName() + ".provider",
//                clipping);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(clipping));
        intent.putExtra("outputFormat", Bitmap.CompressFormat.JPEG.toString());
        intent.putExtra("noFaceDetection", true);
        startActivityForResult(intent, CROP_CODE);
    }

    public RadioGroup.OnCheckedChangeListener getOnCheckedChangeListener(){
        return new RadioGroup.OnCheckedChangeListener(){
            @Override
            public void onCheckedChanged(RadioGroup group, @IdRes int checkedId) {
                switch (checkedId){
                    case R.id.radio_homePage:
                        ((MainV)getTypeView()).setFragment(0);
                        break;
                    case R.id.radio_vip:
                        ((MainV)getTypeView()).setFragment(1);
                        break;
                    case R.id.radio_discover:
                        ((MainV)getTypeView()).setFragment(2);
                        break;
                    case R.id.radio_order:
                        ((MainV)getTypeView()).setFragment(3);
                        break;
                    case R.id.radio_my:
                        ((MainV)getTypeView()).setFragment(4);
                        break;
                }
            }
        };
    }
    public AMapLocation getLocation(){return location;}
}
