package com.zsh.shopclient.widget;

import android.animation.Animator;
import android.animation.AnimatorInflater;
import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.AttrRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.util.Log;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.zsh.shopclient.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2017/11/6.
 * 使用ViewPager实现图片轮播 http://www.jianshu.com/p/c083aa9ddd83
 * demo下载 https://github.com/Jun2011/ImageSlideshow
 */

public class RecyclerImage extends FrameLayout {
    private static final String TAG = "RecyclerImage";
    private Context context;
    private View contentView;
    private ViewPager vpImageTitle;
    private LinearLayout indicator;
    private int count;
    private List<View> views;
    private boolean isAutoPlay;
    private int currentItem;
    private Animator animatorToLarge;
    private Animator animatorToSmall;
    private SparseBooleanArray isLarge;
    private List<ImageTitleBean> imageTitleBeanList;
    private OnItemClickListener onItemClickListener;// 声明监听器
    private int dotSize = 12;
    private int dotSpace = 12;
    private int delay = 3000;
    private Handler handler;
    private Runnable task = new Runnable() {
        @Override
        public void run() {
            if (isAutoPlay) {
                // 位置循环
                currentItem = currentItem % (count + 1) + 1;
                // 正常每隔3秒播放一张图片
                vpImageTitle.setCurrentItem(currentItem);
                handler.postDelayed(task, delay);
            } else {
                // 如果处于拖拽状态停止自动播放，会每隔5秒检查一次是否可以正常自动播放。
                handler.postDelayed(task, 5000);
            }
        }
    };
    public RecyclerImage(@NonNull Context context) {
        super(context,null);
    }

    public RecyclerImage(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs,0);
        this.context = context;
        contentView = LayoutInflater.from(context).inflate(R.layout.recycler_image,this,true);
        vpImageTitle = findViewById(R.id.viewPage_imageTitle);
        indicator = findViewById(R.id.linear_indicator);
        animatorToLarge = AnimatorInflater.loadAnimator(context,R.animator.scale_to_large);
        animatorToSmall = AnimatorInflater.loadAnimator(context,R.animator.scale_to_small);
        imageTitleBeanList = new ArrayList<>();
    }
    // 设置小圆点的大小
    public void setDotSize(int dotSize) {
        this.dotSize = dotSize;
    }
    // 设置小圆点的间距
    public void setDotSpace(int dotSpace) {
        this.dotSpace = dotSpace;
    }
    // 设置图片轮播间隔时间
    public void setDelay(int delay) {
        this.delay = delay;
    }
    // 添加图片
    public void addImageUrl(String imageUrl) {
        ImageTitleBean imageTitleBean = new ImageTitleBean();
        imageTitleBean.setImageUrl(imageUrl);
        imageTitleBeanList.add(imageTitleBean);
    }
    // 添加图片和标题
    public void addImageTitle(String imageUrl, String title) {
        ImageTitleBean imageTitleBean = new ImageTitleBean();
        imageTitleBean.setImageUrl(imageUrl);
        imageTitleBean.setTitle(title);
        imageTitleBeanList.add(imageTitleBean);
    }
    // 添加图片和标题的JavaBean
    public void addImageTitleBean(ImageTitleBean imageTitleBean) {
        imageTitleBeanList.add(imageTitleBean);
    }

    // 设置图片和标题的JavaBean数据列表
    public void setImageTitleBeanList(List<ImageTitleBean> imageTitleBeanList) {
        this.imageTitleBeanList = imageTitleBeanList;
    }
    // 设置完后最终提交
    public void commit() {
        if (imageTitleBeanList != null) {
            count = imageTitleBeanList.size();
            // 设置ViewPager
            setViewPager(imageTitleBeanList);
            // 设置指示器
            setIndicator();
            // 开始播放
            starPlay();
        } else {
            Log.e(TAG, "数据为空");
        }
    }
    /**
     * 设置指示器
     */
    private void setIndicator() {
        isLarge = new SparseBooleanArray();
        // 记得创建前先清空数据，否则会受遗留数据的影响。
        indicator.removeAllViews();
        for (int i = 0; i < count; i++) {
            View view = new View(context);
            view.setBackgroundResource(R.drawable.shape_dot_unselected);
            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(dotSize, dotSize);
            layoutParams.leftMargin = dotSpace / 2;
            layoutParams.rightMargin = dotSpace / 2;
            layoutParams.topMargin = dotSpace / 2;
            layoutParams.bottomMargin = dotSpace / 2;
            indicator.addView(view, layoutParams);
            isLarge.put(i, false);
        }
        indicator.getChildAt(0).setBackgroundResource(R.drawable.shape_dot_selected);
        animatorToLarge.setTarget(indicator.getChildAt(0));
        animatorToLarge.start();
        isLarge.put(0, true);
    }
    /**
     * 开始自动播放图片
     */
    private void starPlay() {
        // 如果少于2张就不用自动播放了
        if (count < 2) {
            isAutoPlay = false;
        } else {
            isAutoPlay = true;
            handler = new Handler();
            handler.postDelayed(task, delay);
        }
    }
    /**
     * 设置ViewPager
     *
     * @param imageTitleBeanList
     */
    private void setViewPager(List<ImageTitleBean> imageTitleBeanList) {
        // 设置View列表
        setViewList(imageTitleBeanList);
        vpImageTitle.setAdapter(new ImageTitlePagerAdapter());
        // 从第1张图片开始（位置刚好也是1，注意：0位置现在是最后一张图片）
        currentItem = 1;
        vpImageTitle.setCurrentItem(1);
        vpImageTitle.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }

            @Override
            public void onPageSelected(int position) {
                // 遍历一遍子View，设置相应的背景。
                for (int i = 0; i < indicator.getChildCount(); i++) {
                    if (i == position - 1) {// 被选中
                        indicator.getChildAt(i).setBackgroundResource(R.drawable.shape_dot_selected);
                        if (!isLarge.get(i)) {
                            animatorToLarge.setTarget(indicator.getChildAt(i));
                            animatorToLarge.start();
                            isLarge.put(i, true);
                        }
                    } else {// 未被选中
                        indicator.getChildAt(i).setBackgroundResource(R.drawable.shape_dot_unselected);
                        if (isLarge.get(i)) {
                            animatorToSmall.setTarget(indicator.getChildAt(i));
                            animatorToSmall.start();
                            isLarge.put(i, false);
                        }
                    }
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {
                switch (state) {
                    // 闲置中
                    case ViewPager.SCROLL_STATE_IDLE:
                        // “偷梁换柱”
                        if (vpImageTitle.getCurrentItem() == 0) {
                            vpImageTitle.setCurrentItem(count, false);
                        } else if (vpImageTitle.getCurrentItem() == count + 1) {
                            vpImageTitle.setCurrentItem(1, false);
                        }
                        currentItem = vpImageTitle.getCurrentItem();
                        isAutoPlay = true;
                        break;
                    // 拖动中
                    case ViewPager.SCROLL_STATE_DRAGGING:
                        isAutoPlay = false;
                        break;
                    // 设置中
                    case ViewPager.SCROLL_STATE_SETTLING:
                        isAutoPlay = true;
                        break;
                }
            }
        });
    }
    /**
     * 根据出入的数据设置View列表
     *
     * @param imageTitleBeanList
     */
    private void setViewList(List<ImageTitleBean> imageTitleBeanList) {
        views = new ArrayList<>();
        for (int i = 0; i < count + 2; i++) {
            View view = LayoutInflater.from(context).inflate(R.layout.recycler_image_title, null);
            ImageView ivImage = (ImageView) view.findViewById(R.id.image);
            TextView tvTitle = (TextView) view.findViewById(R.id.text);
            if (i == 0) {// 将最前面一页设置成本来最后的那页
                Glide.with(context).load(imageTitleBeanList.get(count - 1).getImageUrl()).into(ivImage);
                tvTitle.setText(imageTitleBeanList.get(count - 1).getTitle());
            } else if (i == count + 1) {// 将最后面一页设置成本来最前的那页
                Glide.with(context).load(imageTitleBeanList.get(0).getImageUrl()).into(ivImage);
                tvTitle.setText(imageTitleBeanList.get(0).getTitle());
            } else {
                Glide.with(context).load(imageTitleBeanList.get(i - 1).getImageUrl()).into(ivImage);
                tvTitle.setText(imageTitleBeanList.get(i - 1).getTitle());
            }
            // 将设置好的View添加到View列表中
            views.add(view);
        }
    }
    /**
     * 释放资源
     */
    public void releaseResource() {
        if(null != handler)
            handler.removeCallbacksAndMessages(null);
        context = null;
    }
    // 提供设置监听器的公共方法
    public void setOnItemClickListener(OnItemClickListener listener) {
        onItemClickListener = listener;
    }
    // 创建监听器接口
    public interface OnItemClickListener {
        void onItemClick(View view, int position);
    }
    public class ImageTitleBean {

        private String imageUrl;
        private String title;

        public String getImageUrl() {
            return imageUrl;
        }

        public void setImageUrl(String imageUrl) {
            this.imageUrl = imageUrl;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }
    }

    class ImageTitlePagerAdapter extends PagerAdapter {
        @Override
        public int getCount() {
            return views.size();
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }

        @Override
        public Object instantiateItem(ViewGroup container, final int position) {
            View view = views.get(position);
            // 设置Item的点击监听器
            view.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    // 注意：位置是position-1
                    if(null != onItemClickListener)
                        onItemClickListener.onItemClick(v, position - 1);
                }
            });
            container.addView(view);
            return view;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView(views.get(position));
        }
    }
}
