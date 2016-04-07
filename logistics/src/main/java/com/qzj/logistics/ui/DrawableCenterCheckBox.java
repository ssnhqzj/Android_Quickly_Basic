package com.qzj.logistics.ui;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.widget.CheckBox;

/**
 * drawableLeft和文字一起水平居中的CheckBox
 */
public class DrawableCenterCheckBox extends CheckBox {

    public DrawableCenterCheckBox(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    public DrawableCenterCheckBox(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public DrawableCenterCheckBox(Context context) {
        super(context);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        Drawable[] drawables = getCompoundDrawables();
        if (drawables != null) {
            Drawable drawableLeft = drawables[0];
            Drawable drawableTop = drawables[1];
            if (drawableLeft != null) {
                float textWidth = getPaint().measureText(getText().toString());
                int drawablePadding = getCompoundDrawablePadding();
                int drawableWidth = 0;
                drawableWidth = drawableLeft.getIntrinsicWidth();
                float bodyWidth = textWidth + drawableWidth + drawablePadding;
                canvas.translate((getWidth() - bodyWidth) / 2, 0);
            }else if(drawableTop != null){
                float textHeight = getPaint().descent() - getPaint().ascent();
                int drawablePadding = getCompoundDrawablePadding();
                int drawableHeight = 0;
                drawableHeight = drawableTop.getIntrinsicHeight();
                float bodyHeight = textHeight + drawableHeight + drawablePadding;
                canvas.translate(0, (getHeight() - bodyHeight) / 2);
            }
        }

        super.onDraw(canvas);
    }

}
