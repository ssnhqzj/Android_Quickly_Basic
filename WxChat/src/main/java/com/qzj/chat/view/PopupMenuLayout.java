package com.qzj.chat.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.drawable.BitmapDrawable;
import android.util.AttributeSet;
import android.util.Log;
import android.widget.LinearLayout;

import gzt.com.apptest.R;

/**
 * Created by qzj on 2015/12/24.
 */
public class PopupMenuLayout extends LinearLayout {

    public PopupMenuLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        Log.e("qzj", "----------onDraw-----------" + canvas.getHeight());
        Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        paint.setColor(Color.RED);
        paint.setStyle(Paint.Style.FILL);
        canvas.drawBitmap(((BitmapDrawable) getResources().getDrawable(R.mipmap.chat_voice)).getBitmap(),
                100, 100, paint);
        Path path1 = new Path();
        path1.moveTo(10, 330);
        path1.lineTo(70, 330);
        path1.lineTo(40, 270);
        path1.close();
        canvas.clipPath(path1);
//        int saveCount = canvas.saveLayer(0, 0, 600, 600, paint, Canvas.ALL_SAVE_FLAG);
//        Path path = new Path();
//        path.moveTo(10, 330);
//        path.lineTo(70, 330);
//        path.lineTo(40, 270);
//        path.close();
//        canvas.drawPath(path, paint);
//        canvas.restoreToCount(saveCount);
    }
}
