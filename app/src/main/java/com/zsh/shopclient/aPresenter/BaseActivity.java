package com.zsh.shopclient.aPresenter;

import android.annotation.TargetApi;
import android.app.ActivityManager;
import android.content.ComponentName;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import com.zsh.shopclient.App;
import com.zsh.shopclient.R;
import com.zsh.shopclient.aView.BaseView;
import com.zsh.shopclient.tool.SystemBarTintManager;

import java.util.List;

/**
 * Created by Administrator on 2017/10/29.
 */

public abstract class BaseActivity<TypeView extends BaseView> extends AppCompatActivity {
    private TypeView typeView;
    protected abstract TypeView createTypeView();
    //public abstract View.OnClickListener getClickListener();
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        /*
        * Android 软键盘弹出时把布局顶上去，控件乱套解决方法 http://www.cnblogs.com/zhujiabin/p/5853083.html
        * 方法1.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
        * 方法2.在AndroidManifest文件activity中添加android:windowSoftInputMode="stateVisible|adjustResize"这样会让屏幕整体上移
        * adjustPan这样键盘就会覆盖屏幕
        *方法2.把顶级的layout替换成ScrollView，或者说在顶级的Layout上面再加一层ScrollView的封装。
        * 这样就会把软键盘和输入框一起滚动了，软键盘会一直处于底部
        * */
        typeView = createTypeView();
        typeView.onCreate(this,null,savedInstanceState);
        setContentView(typeView.getRootView());
        //setStates();
        typeView.initView();
        typeView.initWidget();
        App.getInstance().addActivity(this);
    }
    /*
    * android状态栏颜色修改 http://www.cnblogs.com/leon-hm/p/5131323.html
    * android状态栏一体化(沉浸式状态栏) http://blog.csdn.net/jdsjlzx/article/details/41643587
    * Android 5.0 如何实现将布局的内容延伸到状态栏? https://www.zhihu.com/question/31468556
    * 去掉Activity上面的状态栏getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
    * Android 沉浸式状态栏完美实现 http://blog.csdn.net/tyzlmjj/article/details/50890847
    * <item name="android:fitsSystemWindows">true</item>如果你想让一个View的图像显示在状态栏下,那么就在View的XML布局文件中添加属性android:fitsSystemWindows="true"
    *  <item name="android:windowNoTitle">true</item>//去除title另一种方式requestWindowFeature(Window.FEATURE_NO_TITLE);
    * */
    private void setStates(){
        Window window = getWindow();
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);//取消设置透明状态栏,使 ContentView 内容不再覆盖状态栏
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);//需要设置这个 flag 才能调用 setStatusBarColor 来设置状态栏颜色
        if(Build.VERSION.SDK_INT > Build.VERSION_CODES.KITKAT_WATCH) {
            window.setStatusBarColor(ContextCompat.getColor(this,R.color.scarlet));
            window.setNavigationBarColor(0x00000000);
        }
        ViewGroup contentView = findViewById(Window.ID_ANDROID_CONTENT);
        View childView = contentView.getChildAt(0);
        if(null != childView)
            childView.setFitsSystemWindows(true);//ViewCompat.setFitsSystemWindows(childView,true);
    }
    private void setStatus(){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            setTranslucentStatus(true);
            SystemBarTintManager tintManager = new SystemBarTintManager(this);
            tintManager.setStatusBarTintEnabled(true);
            tintManager.setStatusBarTintResource(R.color.scarlet);//状态栏设置为透明色
            tintManager.setNavigationBarTintEnabled(true);
            tintManager.setNavigationBarTintResource(Color.TRANSPARENT);//导航栏设置为透明色
        }
    }
    @TargetApi(19)
    private void setTranslucentStatus(boolean on) {
        Window win = getWindow();
        WindowManager.LayoutParams winParams = win.getAttributes();
        final int bits = WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS;
        if (on) {
            winParams.flags |= bits;
        } else {
            winParams.flags &= ~bits;
        }
        win.setAttributes(winParams);
    }
    public TypeView getTypeView() {
        return typeView;
    }
    //判断某一个类是否存在任务栈里面 http://blog.csdn.net/u010020280/article/details/52999325
    private boolean isExistMainActivity(Class<?> activity){
        Intent intent = new Intent(this, activity);
        ComponentName cmpName = intent.resolveActivity(getPackageManager());
        boolean flag = false;
        if (cmpName != null) { // 说明系统中存在这个activity
            ActivityManager am = (ActivityManager) getSystemService(ACTIVITY_SERVICE);
            List<ActivityManager.RunningTaskInfo> taskInfoList = am.getRunningTasks(10);  //获取从栈顶开始往下查找的10个activity
            for (ActivityManager.RunningTaskInfo taskInfo : taskInfoList) {
                if (taskInfo.baseActivity.equals(cmpName)) { // 说明它已经启动了
                    flag = true;
                    break;  //跳出循环，优化效率
                }
            }
        }
        return flag;
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        typeView.onDestroy();
        App.getInstance().removeActivity(this);
    }
}
