package gzt.com.apptest.Chat;

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
import android.util.AttributeSet;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.widget.LinearLayout;

/**
 * 聊天item的根布局
 * Created by qzj on 2015/12/5.
 */
public class ChatItemLayout extends LinearLayout {
    private static final int CLICK_INTERVAL_TIME = 80;
    private static final int LONG_CLICK_INTERVAL_TIME = 500;

    /**
     * 是否显示遮罩层
     */
    private boolean isShowCovering;
    /**
     * 遮罩层颜色
     */
    private int coveringColor = 0x44FF0000;
    /**
     * 遮罩层透明度
     */
    private int coveringAlpha = 80;
    private int[] ids = {android.R.attr.background};
    private int w;
    private int h;
    private int bgId;
    private float lastY;
    private GestureDetector detector;

    public ChatItemLayout(Context context) {
        this(context,null);
    }

    public ChatItemLayout(Context context, AttributeSet attrs) {
        this(context,attrs,0);
    }

    public ChatItemLayout(Context context, AttributeSet attrs, int defStyleAttr) {
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
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        return true;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        boolean result = super.onTouchEvent(event);
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

        return result;
    }

    @Override
    protected synchronized void onDraw(Canvas canvas) {
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
            paint.setColor(coveringColor);this.getBackground();
            paint.setAlpha(coveringAlpha);
            canvas.drawPaint(paint);
            paint.setXfermode(null);
            canvas.restoreToCount(saveCount);
        }
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        this.w = w;
        this.h = h;
    }

    private void drawNinePath(Canvas canvas, int id, Rect rect){
        Bitmap bmp= BitmapFactory.decodeResource(getResources(), id);
        NinePatch patch = new NinePatch(bmp, bmp.getNinePatchChunk(), null);
        patch.draw(canvas, rect);
    }

    public ChatItemLayout setCoveringColor(int coveringColor) {
        if (coveringColor != 0)
            this.coveringColor = coveringColor;
        return this;
    }

    public ChatItemLayout setCoveringAlpha(int coveringAlpha) {
        this.coveringAlpha = coveringAlpha;
        return this;
    }

    public ChatItemLayout setBgId(int bgId) {
        if (bgId != 0)
            this.bgId = bgId;
        return this;
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
