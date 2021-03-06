package com.zsh.shopclient.tool;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.support.annotation.DrawableRes;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.zsh.shopclient.R;

/**
 * Created by Administrator on 2017/10/25.
 * RecyclerView的万能分割线 http://blog.csdn.net/lmj623565791/article/details/45059587
 *                          http://www.cnblogs.com/huolongluo/p/5879292.html
 *          orrer           http://blog.csdn.net/pengkv/article/details/50538121
 * RecyclerView系列之（2）：为RecyclerView添加分隔线 http://www.jianshu.com/p/4eff036360da
 * RecyclerView 添加分割线 http://www.jianshu.com/p/fe41428ca2f3
 *  Android RecyclerView 使用完全解析 体验艺术般的控件 http://blog.csdn.net/lmj623565791/article/details/45059587
 *  Android 中RecyclerView使用详解（一）http://blog.csdn.net/yuminfeng728/article/details/52020708
 *  这种方式设计RecyclerView分界线如果itemView设置背景后分界线无效,针对RecyclerView设置背景色然后itemView不设置背景色有效
 *  如果设置itemView背景色这正方式分界线无效了
 */

public class RecyclerLimit extends RecyclerView.ItemDecoration {
    public final static byte LEVEL = LinearLayoutManager.HORIZONTAL;
    public final static byte VERTICAL = LinearLayoutManager.VERTICAL;
    public final static byte LEVELvERTICAL = 2;
    private static final int[] ATTRS = new int[]{android.R.attr.listDivider};
    private byte orientation;
    private Paint paint;
    private Drawable divider;
    private int dividerHeight;//设置为0时RecyclerView的itemView设置背景色分界线不显示了
    public RecyclerLimit(Context context, byte orientation) {//默认分割线
        super();
        this.orientation = orientation;
        dividerHeight = 0;//(int)context.getResources().getDimension(R.dimen.line1);
        final TypedArray typedArray = context.obtainStyledAttributes(ATTRS);
        divider = typedArray.getDrawable(0);
        typedArray.recycle();
    }
    /*
    * 自定义分割线
    * @param drawableId 分割线图片
    * */
    public RecyclerLimit(Context context, byte orientation, @DrawableRes int drawableId){
        this(context, orientation);
        divider = ContextCompat.getDrawable(context, drawableId);
        dividerHeight = divider.getIntrinsicHeight();
    }
    /*
    * 自定义分割线
    * @param dividerColor  分割线颜色
    * */
    public RecyclerLimit(Context context, byte orientation, int dividerHeight, int dividerColor) {
        this(context, orientation);
        this.dividerHeight = dividerHeight;
        paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        paint.setColor(dividerColor);
        paint.setStyle(Paint.Style.FILL);
    }

    @Override
    public void onDraw(Canvas c, RecyclerView parent, RecyclerView.State state) {
        super.onDraw(c, parent, state);
        switch (orientation){
            case LEVEL:
                drawHorizontal(c,parent);
                break;
            case VERTICAL:
                drawVertical(c,parent);
                break;
            case LEVELvERTICAL:
                drawHv(c,parent);
                break;
        }
    }

    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
        super.getItemOffsets(outRect, view, parent, state);
        switch (orientation){
            case LEVEL:
                outRect.set(0, 0, 0, dividerHeight);
                break;
            case VERTICAL:
                outRect.set(0, 0, divider.getIntrinsicWidth(), 0);
                break;
            case LEVELvERTICAL:
                outRect.set(0, 0, divider.getIntrinsicWidth(), divider.getIntrinsicHeight());
                break;
        }
    }
    private void drawHorizontal(Canvas canvas, RecyclerView parent){
        final int left = parent.getPaddingLeft();
        final int right = parent.getMeasuredWidth()- parent.getPaddingRight();
        final int childSize = parent.getChildCount();
        for(int index = 0;index < childSize;index++){
            final View child = parent.getChildAt(index);
            RecyclerView.LayoutParams layoutParams = (RecyclerView.LayoutParams)child.getLayoutParams();
            final int top = child.getBottom() + layoutParams.bottomMargin;
            final int bottom = top + divider.getIntrinsicHeight();//dividerHeight;
            if(null != divider){
                divider.setBounds(left,top,right,bottom);
                divider.draw(canvas);
            }
            if(null != paint)
                canvas.drawRect(left,top,right,bottom,paint);
        }
    }
    private void drawVertical(Canvas canvas, RecyclerView parent){
        final int top = parent.getPaddingTop();
        final int bottom = parent.getMeasuredHeight() - parent.getPaddingBottom();
        final int childSize = parent.getChildCount();
        for (int index = 0; index < childSize; index++) {
            final View child = parent.getChildAt(index);
            RecyclerView.LayoutParams layoutParams = (RecyclerView.LayoutParams) child.getLayoutParams();
            final int left = child.getRight() + layoutParams.rightMargin;
            final int right = left + divider.getIntrinsicHeight();//dividerHeight;
            if (null != divider) {
                divider.setBounds(left, top, right, bottom);
                divider.draw(canvas);
            }
            if (null != paint)
                canvas.drawRect(left, top, right, bottom,paint);
        }
    }
    private void drawHv(Canvas canvas, RecyclerView parent){
        final int left = parent.getPaddingLeft();
        final int top = parent.getPaddingTop();
        final int right = parent.getWidth() - parent.getPaddingRight();
        final int bottom = parent.getMeasuredHeight() - parent.getPaddingBottom();
        final int childSize = parent.getChildCount();
        for (int index = 0; index < childSize; index++) {
            final View chlid = parent.getChildAt(index);
            RecyclerView.LayoutParams layoutParams = (RecyclerView.LayoutParams)chlid.getLayoutParams();
            int lineTop = chlid.getBottom() + layoutParams.bottomMargin;
            int lineBottom = lineTop + divider.getIntrinsicHeight();
            int lineLeft = chlid.getRight() + layoutParams.rightMargin;
            int lineRight = lineLeft + divider.getIntrinsicWidth();
            if(3== index%4) {
                lineLeft = left;
                lineRight = right;
            }else{
                lineTop = top;
                lineBottom = bottom;
            }
            if (divider != null) {
                divider.setBounds(lineLeft, lineTop, lineRight, lineBottom);
                divider.draw(canvas);
            }
            if (paint != null)
                canvas.drawRect(lineLeft, lineTop, lineRight, bottom,paint);
        }
    }
}
