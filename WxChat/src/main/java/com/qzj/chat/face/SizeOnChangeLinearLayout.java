package com.qzj.chat.face;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.util.AttributeSet;
import android.widget.LinearLayout;

/**
 * Created by Administrator on 2015/9/14.
 */
public class SizeOnChangeLinearLayout extends LinearLayout {
    private SizeOnChangeListener sizeOnChangeListener;
    public SizeOnChangeLinearLayout(Context context) {
        super(context);
    }

    public SizeOnChangeLinearLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    public SizeOnChangeLinearLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public SizeOnChangeLinearLayout(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    public void setSizeOnChangeListener(SizeOnChangeListener sizeOnChangeListener) {
        this.sizeOnChangeListener = sizeOnChangeListener;
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        if (sizeOnChangeListener!=null)
            sizeOnChangeListener.onSizeChanged(w, h, oldw, oldh);
    }

    public interface  SizeOnChangeListener{
        void onSizeChanged(int w, int h, int oldw, int oldh);
    }
}
