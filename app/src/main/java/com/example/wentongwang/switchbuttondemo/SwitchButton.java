package com.example.wentongwang.switchbuttondemo;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

/**
 *
 * Created by Wentong WANG on 2016/4/12.
 */
public class SwitchButton extends View {


    private final static int STATE_DEFAUT = 0;
    private final static int STATE_DOWN = 1;
    private final static int STATE_MOVE = 2;
    private final static int STATE_UP = 3;


    private Bitmap mSwitchBackground;
    private Bitmap mSwitchSlide;
    private int width;
    private int height;
    private Paint mpaint = new Paint();
    ;
    private float downX;
    private int slideWidth;
    private int slideHeight;

    private int mState = STATE_DEFAUT;
    private boolean isOpened = false;
    private float moveX;
    private float max;

    private OnSwitchListenr mListener;

    public SwitchButton(Context context) {
        this(context, null);
    }

    public SwitchButton(Context context, AttributeSet attrs) {
        super(context, attrs);

    }

    /**
     * 设置开关背景
     *
     * @param resId
     */
    public void setSwitchBackground(int resId) {
        mSwitchBackground = BitmapFactory.decodeResource(getResources(), resId);
    }

    /**
     * 设置滑块图片
     *
     * @param resId
     */
    public void setSwitchSlide(int resId) {
        mSwitchSlide = BitmapFactory.decodeResource(getResources(), resId);
    }

    /**
     * 测量控件，来调整大小
     *
     * @param widthMeasureSpec
     * @param heightMeasureSpec
     */
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {

        if (mSwitchSlide != null) {
            slideWidth = mSwitchSlide.getWidth();
            slideHeight = mSwitchSlide.getHeight();
        }
        //获得背景的大小，来设置该控件的大小
        if (mSwitchBackground != null) {
            width = mSwitchBackground.getWidth();
            height = mSwitchBackground.getHeight();
            max = width - slideWidth;
            setMeasuredDimension(width, height);
        } else {
            super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        }
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        //绘制背景
        if (mSwitchBackground != null) {
            //drawBitmap(Bitmap,left,top,paint)
            //left: 外侧左侧到该背景左侧 top:上到上
            int left = 0;
            int top = 0;
            canvas.drawBitmap(mSwitchBackground, left, top, mpaint);
        }
        if (mSwitchSlide == null) {
            return;
        }
        switch (mState) {
            case STATE_DEFAUT:
            case STATE_UP:
                //绘制滑块
                if (!isOpened) {
                    canvas.drawBitmap(mSwitchSlide, 0, 0, mpaint);
                } else {
                    canvas.drawBitmap(mSwitchSlide, max, 0, mpaint);
                }
                break;
            case STATE_DOWN:
            case STATE_MOVE:
                //滑块处于关闭状态
                if (!isOpened) {
                    //点击滑块中线左侧，滑块不懂；右侧，滑块开始移动
                    if (downX > slideWidth / 2f) {
                        //滑块滑动，绘制
                        float left = downX - slideWidth / 2f;
                        //避免超出最大滑动范围
                        if (left > max) {
                            left = max;
                        }
                        canvas.drawBitmap(mSwitchSlide, left, 0, mpaint);
                    } else {
                        //滑块不动，绘制滑块
                        canvas.drawBitmap(mSwitchSlide, 0, 0, mpaint);
                    }
                } else {
                    float mid = width - slideWidth / 2f;
                    if (downX > mid) {
                        canvas.drawBitmap(mSwitchSlide, max, 0, mpaint);
                    } else {
                        float left = downX - slideWidth / 2;
                        left = left < 0 ? 0 : left;
                        canvas.drawBitmap(mSwitchSlide, left, 0, mpaint);
                    }
                }
                break;
        }

    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                mState = STATE_DOWN;
                downX = event.getX();
                invalidate();//触发刷新（主线程） 回去触发 onDraw()
                //postInvalidate(); 也是刷新（子线程）
                break;
            case MotionEvent.ACTION_MOVE:
                mState = STATE_MOVE;
                downX = event.getX();
                invalidate();
                break;
            case MotionEvent.ACTION_UP:
                mState = STATE_UP;
                moveX = event.getX();
                if (width / 2f > moveX && isOpened) {
                    // 关闭状态
                    isOpened = false;
                    if (mListener != null) {
                        mListener.onSwitchChanged(isOpened);
                    }
                } else if (width / 2f <= moveX && !isOpened) {
                    isOpened = true;
                    if (mListener != null) {
                        mListener.onSwitchChanged(isOpened);
                    }
                }
                invalidate();
                break;
        }
        return true;
    }


    public void setOnSwitchListener(OnSwitchListenr listener) {
        this.mListener = listener;
    }

    /**
     * 回调接口
     */
    public interface OnSwitchListenr {
        // 开关状态改变--->
        void onSwitchChanged(boolean isOpened);
    }
}
