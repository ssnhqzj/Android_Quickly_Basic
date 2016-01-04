package com.qzj.chat.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.NinePatch;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.os.Handler;
import android.util.AttributeSet;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.widget.ImageView;

/**
 * 点击后显示遮罩层的ImageView
 * Created by qzj on 2015/12/5.
 */
public class ChatImageView extends ImageView {
    private static final int CLICK_INTERVAL_TIME = 80;
    private static final int LONG_CLICK_INTERVAL_TIME = 500;

    private static final int MAX_WIDTH = 100;
    /**
     * 是否显示遮罩层
     */
    private boolean isShowCovering;
    /**
     * 遮罩层颜色
     */
    private int coveringColor = 0x44FF0000;
    /**
     * 进度颜色
     */
    private int progressColor = 0x88000000;
    /**
     * 是否显示进度
     */
    private boolean isShowProgress;
    /**
     * 是否完成
     */
    private boolean isProgressFinished = false;
    /**
     * 是否正在进行
     */
    private boolean isProgressing = false;
    /**
     * 遮罩层透明度
     */
    private int coveringAlpha = 80;
    private int[] ids = {android.R.attr.background};
    private int w;
    private int h;
    private int bgId;
    private float lastY;
    private float progress;
    private GestureDetector detector;

    public ChatImageView(Context context) {
        this(context, null);
    }

    public ChatImageView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ChatImageView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.setClickable(true);
        this.setLongClickable(true);
        detector = new GestureDetector(context,new GestureListener());
        detector.setIsLongpressEnabled(true);
        if(attrs != null){
            TypedArray ta = context.obtainStyledAttributes(attrs, ids);
            if (ta != null && ta.getIndexCount() > 0)
                bgId = ta.getResourceId(0,-1);
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        super.onTouchEvent(event);
        if(detector.onTouchEvent(event)){
            event.setAction(MotionEvent.ACTION_CANCEL);
        }
        switch (event.getAction()){
            case MotionEvent.ACTION_DOWN:
                lastY = event.getRawY();
                getParent().requestDisallowInterceptTouchEvent(true);
                break;

            case MotionEvent.ACTION_MOVE:
                float disY = Math.abs(event.getRawY() - lastY);
                if (Math.abs(disY) > 0){
                    isShowCovering = false;
                    invalidate();
                    getParent().requestDisallowInterceptTouchEvent(false);
                }

                break;
            case MotionEvent.ACTION_UP:
                isShowCovering = false;
                invalidate();
                break;
        }

        return true;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        if (isShowCovering){
            // 保存为单独的层
            int saveCount = canvas.saveLayer(0, 0, w, h, paint, Canvas.ALL_SAVE_FLAG);
            // 绘制目标图
            drawNinePath(canvas, bgId, new Rect(0, 0, w, h));
            // 设置混合模式
            paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
            // 绘制源图
            paint.setColor(coveringColor);
            paint.setAlpha(coveringAlpha);
            canvas.drawPaint(paint);
            paint.setXfermode(null);
            canvas.restoreToCount(saveCount);
        }

        if (isShowProgress){
            int progressHeight = (int) (h*progress);
            canvas.clipRect(new Rect(0,h-progressHeight,w,h));
            int saveCount = canvas.saveLayer(0, 0, w, h, paint, Canvas.ALL_SAVE_FLAG);
            drawNinePath(canvas, bgId, new Rect(0, 0, w, h));
            paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
            paint.setColor(progressColor);
            canvas.drawPaint(paint);
            paint.setXfermode(null);
            canvas.restoreToCount(saveCount);
        }
    }

    public void drawNinePath(Canvas canvas, int id, Rect rect){
        Bitmap bmp= BitmapFactory.decodeResource(getResources(), id);
        NinePatch patch = new NinePatch(bmp, bmp.getNinePatchChunk(), null);
        patch.draw(canvas, rect);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        this.w = w;
        this.h = h;
    }

    @Override
    public boolean performClick() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                isShowCovering = true;
                postInvalidate();
            }
        }).start();
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(CLICK_INTERVAL_TIME);
                    isShowCovering = false;
                    postInvalidate();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }).start();
        super.performClick();
        return true;
    }

    @Override
    public boolean performLongClick() {
        super.performLongClick();
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(LONG_CLICK_INTERVAL_TIME);
                    isShowCovering = false;
                    postInvalidate();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }).start();

        return true;
    }

    public float getProgress() {
        return progress;
    }

    /**
     * 设置当前进度
     * @param progress value in 0~1.0
     */
    public void setProgress(float progress) {
        if (isProgressFinished) return;
        if (progress >= 1.0f){
            this.progress = 1.0f;
            invalidate();
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    postInvalidate();
                    isShowProgress = false;
                    isProgressFinished = true;
                }
            },1000);
        }else{
            this.progress = progress;
            isShowProgress = true;
            invalidate();
        }
    }

    public void setIsShowProgress(boolean isShowProgress) {
        this.isShowProgress = isShowProgress;
    }

    public void setCoveringColor(int coveringColor) {
        this.coveringColor = coveringColor;
    }

    public void setCoveringAlpha(int coveringAlpha) {
        this.coveringAlpha = coveringAlpha;
    }

    class GestureListener extends GestureDetector.SimpleOnGestureListener {

        @Override
        public void onShowPress(MotionEvent e) {
            new Thread(new Runnable() {
                @Override
                public void run() {
                    isShowCovering = true;
                    postInvalidate();
                }
            }).start();
        }

        @Override
        public boolean onDoubleTap(MotionEvent e) {
            return super.onDoubleTap(e);
        }

        @Override
        public boolean onDoubleTapEvent(MotionEvent e) {
            return super.onDoubleTapEvent(e);
        }

    }
}
