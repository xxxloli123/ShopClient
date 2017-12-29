package com.zsh.shopclient.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by Administrator on 2017/11/28.
 * 仿淘宝属性标签 http://blog.csdn.net/u010924834/article/details/50353955
 * Android 仿淘宝选中商品不同尺寸的按钮组（一）http://blog.csdn.net/qq_30552993/article/details/52304744
 */

public class AttributeTagView extends ViewGroup {
    public static final byte TOWARDSrIGHT = 0,TOWARDSlEFT = 1;
    private int horizontalSpacing,verticalInterval;
    private byte orientation;
    public AttributeTagView(Context context) {
        this(context,null);
    }

    public AttributeTagView(Context context, AttributeSet attrs) {
        this(context, attrs,0);
    }

    public AttributeTagView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        horizontalSpacing = 4;
        verticalInterval = 4;
    }

   @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int stages = 1;
        int stageWidth = 0;
        int wholeWidth = MeasureSpec.getSize(widthMeasureSpec);
        for(int index = 0; index< getChildCount();index++){
            final View childView = getChildAt(index);
            measureChild(childView,widthMeasureSpec,heightMeasureSpec);
            stageWidth += childView.getMeasuredWidth() + horizontalSpacing;
            if(stageWidth >= wholeWidth) {
                stages++;
                stageWidth = childView.getMeasuredWidth();
            }
        }
        int wholeHeight = getChildAt(0).getMeasuredHeight()* stages +(stages-1)* verticalInterval;
        setMeasuredDimension(resolveSize(wholeWidth,widthMeasureSpec),resolveSize(wholeHeight,heightMeasureSpec));
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        final int count = getChildCount();
        int row = 0;
        int lenghtX = l;
        int lenghtY = t;
        for(int index = 0;index <count;index++){
            final View childView = getChildAt(index);
            int width = childView.getMeasuredWidth();
            int height = childView.getMeasuredHeight();
            if(0 == index)
                lenghtX +=  width + horizontalSpacing;
            else
                lenghtX += width + 2* horizontalSpacing;
            if(r < lenghtX){
                lenghtX = width + horizontalSpacing + l;
                lenghtY  = row++ *(height + verticalInterval)+ verticalInterval + height + t;
            }
            childView.layout(lenghtX - width,lenghtY - height +2,lenghtX,lenghtY);//调整childView和parentView距离
        }
    }
}
