package gzt.com.apptest.customui;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.widget.OverScroller;
import android.widget.RelativeLayout;

/**
 * Created by Administrator on 2015/11/11.
 */
public class ScrollContentLayout extends RelativeLayout {
    private static final String TAG = "ScrollContentLayout";

    private OverScroller mScroller;

    private float lastY;

    private float startX;
    private float startY;

    public ScrollContentLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        mScroller = new OverScroller(context);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        super.onLayout(changed, l, t, r, b);
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        Log.e(TAG, ">>>>>>>>>>>>>>>>>");
        return true;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {

        switch (event.getAction()){
            case MotionEvent.ACTION_DOWN:
                lastY = event.getRawY();
                break;

            case MotionEvent.ACTION_MOVE:
                float disY = event.getRawY() - lastY;
                Log.e(TAG, ">>>>>>>>>>>>>>>>>" + disY);
                scrollBy(0, -(int) disY);
                offsetTopAndBottom((int) disY);

                lastY = event.getRawY();
                break;

            case MotionEvent.ACTION_UP:
//                mScroller.startScroll((int) getX(), (int) getY(), -(int) (getX() - startX), -(int) (getY() - startY));
//                invalidate();
                break;
        }
        return true;
    }

    @Override
    public void computeScroll() {

        /*if (mScroller.computeScrollOffset()) {
            setX(mScroller.getCurrX());
            setY(mScroller.getCurrY());
            invalidate();
        }*/

    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        startX = getX();
        startY = getY();
    }
}
