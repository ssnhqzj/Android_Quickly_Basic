package gzt.com.apptest;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

/**
 * guaguaka
 */
public class GuaGuaKaView extends View {

    private String bText = "500000000";
    private Canvas gCanvas;
    private Paint gPaint;
    private Bitmap gBitmap;
    private Path gPath;

    private Canvas bCanvas;
    private Paint bPaint;
    private Bitmap bBitmap;

    private int gWidth = 300;
    private int gHeight = 100;
    private int gLastX;
    private int gLastY;
    private Rect bTextBound;

    public GuaGuaKaView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initPaint();
    }

    private void initPaint(){
        gPaint = new Paint();
        gPaint.setColor(Color.RED);
        gPaint.setAntiAlias(true);
        gPaint.setDither(true);
        gPaint.setStyle(Paint.Style.STROKE);
        gPaint.setStrokeJoin(Paint.Join.ROUND); // 圆角
        gPaint.setStrokeCap(Paint.Cap.ROUND); // 圆角
        // 设置画笔宽度
        gPaint.setStrokeWidth(20);
        gPath = new Path();

        bPaint = new Paint();
        bTextBound = new Rect();
        bPaint.setStyle(Paint.Style.FILL);
        bPaint.setTextScaleX(2f);
        bPaint.setColor(Color.DKGRAY);
        bPaint.setTextSize(22);
        bPaint.getTextBounds(bText, 0, bText.length(), bTextBound);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        gWidth = getMeasuredWidth();
        gHeight = getMeasuredHeight();

        gBitmap = Bitmap.createBitmap(gWidth, gHeight, Bitmap.Config.ARGB_8888);
        gCanvas = new Canvas(gBitmap);
        gCanvas.drawColor(Color.parseColor("#C0C0C0"));
    }

    @Override
    protected void onDraw(Canvas canvas) {
        canvas.drawText(bText, getWidth() / 2 - bTextBound.width() / 2,
                getHeight() / 2 + bTextBound.height() / 2, bPaint);

        gPaint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.DST_OUT));
        gCanvas.drawPath(gPath, gPaint);
        canvas.drawBitmap(gBitmap, 0, 0, null);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event)
    {
        int action = event.getAction();
        int x = (int) event.getX();
        int y = (int) event.getY();
        switch (action)
        {
            case MotionEvent.ACTION_DOWN:
                gLastX = x;
                gLastY = y;
                gPath.moveTo(gLastX, gLastY);
                break;
            case MotionEvent.ACTION_MOVE:

                int dx = Math.abs(x - gLastX);
                int dy = Math.abs(y - gLastY);

                if (dx > 3 || dy > 3)
                    gPath.lineTo(x, y);

                gLastX = x;
                gLastY = y;
                break;
        }

        invalidate();
        return true;
    }
}
