package gzt.com.apptest.customui;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.MotionEvent;

/**
 * Created by Administrator on 2015/11/12.
 */
public class VscrollViewPager extends ViewPager {

    private float lastY;

    public VscrollViewPager(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
//        onTouchEvent(ev);
        return super.dispatchTouchEvent(ev);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()){
            case MotionEvent.ACTION_DOWN:
                lastY = event.getRawY();
                break;

            case MotionEvent.ACTION_MOVE:
                float disY = event.getRawY() - lastY;
                scrollBy(0, -(int) disY);

                lastY = event.getRawY();

                break;

            case MotionEvent.ACTION_UP:
                break;
        }
        return super.onTouchEvent(event);
    }
}
