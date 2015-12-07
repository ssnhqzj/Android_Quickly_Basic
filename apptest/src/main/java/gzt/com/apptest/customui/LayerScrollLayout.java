package gzt.com.apptest.customui;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.widget.OverScroller;
import android.widget.RelativeLayout;

/**
 * Created by Administrator on 2015/11/11.
 */
public class LayerScrollLayout extends RelativeLayout {
    private static final String TAG = "LayerScrollLayout";

    /**
     * 头部layout
     */
    private RelativeLayout mHeaderLayout;

    private RelativeLayout mContentLayout;

    // header的高度
    private int mHeaderHeight;

    private OverScroller mScroller;

    public LayerScrollLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        mScroller = new OverScroller(context);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        mHeaderLayout = (RelativeLayout) getChildAt(0);
        mContentLayout = (RelativeLayout) getChildAt(1);
        mHeaderHeight = mHeaderLayout.getMeasuredHeight();
        Log.e(TAG, ">>>>>>>>>>>>>>>>>"+mHeaderHeight);
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        super.onLayout(changed, l, t, r, b);
    }
}
