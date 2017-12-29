package com.zsh.shopclient.widget;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import com.zsh.shopclient.R;

/**
 * Created by 12260 on 2016/3/18.
 */
public class SlideSwitchView extends View {
    //底部样式图、当前样式图、无操作样式图、触摸样式图
    private Bitmap bottom,thumb,thumbNormal,thumbPressed;
    private Bitmap switchFrame,mask;
    private float currentX = 0;
    private boolean switchOn;//开关状态
    private int moveLength;//最大移动距离
    private float lastX = 0;//第一次按下的有效区域
    private Rect dest = null;//绘制目标区域大小
    private Rect src = null;//获取图片的大小
    private int moveDeltX = 0;//移动偏移量
    private Paint paint = null;
    private OnSwitchChangedListener switchListener = null;
    private boolean flag = false;
    private boolean enabled = true;
    private final int MAX_ALPHA = 255;//最大透明度
    private int alpha = MAX_ALPHA;
    private boolean isScrolled = false;

    public SlideSwitchView(Context context) {
        this(context,null);
    }

    public SlideSwitchView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public SlideSwitchView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }
    public void init(){
        thumbPressed = BitmapFactory.decodeResource(getResources(), R.mipmap.checkswitch_btn_pressed);
        thumbNormal = BitmapFactory.decodeResource(getResources(),R.mipmap.checkswitch_btn_unpressed);
        bottom = BitmapFactory.decodeResource(getResources(),R.mipmap.checkswitch_bottom);
        switchFrame = BitmapFactory.decodeResource(getResources(), R.mipmap.checkswitch_frame);
        mask = BitmapFactory.decodeResource(getResources(),R.mipmap.checkswitch_mask);
        thumb = thumbNormal;
        moveLength = bottom.getWidth() - switchFrame.getWidth();
        //绘制区域大小
        dest = new Rect(0,0,switchFrame.getWidth(),switchFrame.getHeight());
        src = new Rect();
        paint = new Paint();
        paint.setAntiAlias(true);
        paint.setAlpha(255);
        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.DST_IN));
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        setMeasuredDimension(switchFrame.getWidth(), switchFrame.getHeight());
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if(0< moveDeltX || 0 == moveDeltX && switchOn) {
            if (null != src)
                src.set(moveLength - moveDeltX, 0, bottom.getWidth() - moveDeltX, switchFrame.getHeight());
        }else if(0> moveDeltX || 0 == moveDeltX && !switchOn)
            if(null != src)
                src.set(-moveDeltX,0,switchFrame.getWidth() - moveDeltX,switchFrame.getHeight());
        canvas.saveLayerAlpha(new RectF(dest),alpha, Canvas.MATRIX_SAVE_FLAG | Canvas.CLIP_SAVE_FLAG |
                Canvas.HAS_ALPHA_LAYER_SAVE_FLAG | Canvas.FULL_COLOR_LAYER_SAVE_FLAG | Canvas.CLIP_TO_LAYER_SAVE_FLAG);
        canvas.drawBitmap(bottom, src, dest, null);
        canvas.drawBitmap(thumb, src, dest, null);
        canvas.drawBitmap(switchFrame, 0, 0, null);
        canvas.drawBitmap(mask, 0, 0, paint);
        canvas.restore();
    }
    @Override
    public boolean onTouchEvent(MotionEvent event){
        if(!enabled)//如果Enabled属性设定为true，触摸才有效果
            return true;
        switch(event.getAction()){
            case MotionEvent.ACTION_DOWN:
                thumb = thumbPressed;
                lastX = event.getX();
                break;
            case MotionEvent.ACTION_MOVE:
                currentX = event.getX();
                moveDeltX = (int)(currentX - lastX);
                if(10< moveDeltX)
                    isScrolled = true;//设置了10这个误差距离，可以更好的实现点击效果
                // 如果开关开着向左滑动，或者开关关着向右滑动（这时候是不需要处理的）
                if((switchOn && moveDeltX <0)||(!switchOn && moveDeltX > 0)){
                    flag = true;
                    moveDeltX = 0;
                }
                if(Math.abs(moveDeltX) > moveLength)
                    moveDeltX = moveDeltX > 0 ? moveLength : -moveLength;
                invalidate();
                break;
            case MotionEvent.ACTION_UP:
                thumb = thumbNormal;
                //如果没有滑动过，就看作一次点击事件
                if(!isScrolled){
                    moveDeltX = switchOn ? moveLength : -moveLength;
                    switchOn =! switchOn;
                    if(null != switchListener)
                        switchListener.onSwitchChange(this,switchOn);
                    invalidate();
                    moveDeltX = 0;
                    break;
                }
                isScrolled = false;
                if(Math.abs(moveDeltX) >0 && Math.abs(moveDeltX) < moveLength /2){
                    moveDeltX = 0;
                    invalidate();
                }else if(Math.abs(moveDeltX) > moveLength /2 && Math.abs(moveDeltX) <= moveLength){
                    moveDeltX = moveDeltX >0 ? moveLength : -moveLength;
                    switchOn =! switchOn;
                    if(null != switchListener)
                        switchListener.onSwitchChange(this,switchOn);
                    invalidate();
                    moveDeltX = 0;
                }else if(0 == moveDeltX && flag) {
                    moveDeltX = 0;
                    flag = false;
                }
        }
        invalidate();
        return true;
    }
    public void setOnChangeListener(OnSwitchChangedListener listener){
        switchListener = listener;
    }

    @Override
    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
        alpha = enabled ? MAX_ALPHA : MAX_ALPHA /2;
        super.setEnabled(enabled);
        invalidate();
    }
    /** 设置选中的状态（选中:true   非选中: false） */
    public void togglr(){
        switchOn = !switchOn;
        invalidate();
    }
    public boolean getStatus(){
        return switchOn;
    }

    ///////////开关监听接口
    public interface OnSwitchChangedListener{
        public void onSwitchChange(SlideSwitchView switchView, boolean isChecked);
    }
}
