package gzt.com.apptest;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.View;

/**
 * Created by Administrator on 2015/11/10.
 */
public class WeixinMenu extends View {

    private Bitmap iconBitMap;
    private Bitmap mBitmap;
    private Canvas mCanvas;
    private Paint mPaint;
    private Paint mTextPaint;
    private int mColor = 0xFF45C01A;
    private float mAlpha = 0f;

    private String mText = "微信";
    private int mTextSize;
    private Rect mTextBound = new Rect();
    private Rect mIconRect;

    public WeixinMenu(Context context, AttributeSet attrs) {
        super(context, attrs);

        init();
    }

    private void init(){
        iconBitMap = BitmapFactory.decodeResource(getResources(), R.mipmap.ic_menu_start_conversation);
        mTextSize = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, 10, getResources().getDisplayMetrics());

        mPaint = new Paint();
        mTextPaint = new Paint();
        mTextPaint.setTextSize(mTextSize);
        mTextPaint.setColor(0xff555555);
        // 得到text绘制范围
        mTextPaint.getTextBounds(mText, 0, mText.length(), mTextBound);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        // 得到绘制icon的宽
        int bitmapWidth = Math.min(getMeasuredWidth() - getPaddingLeft()
                - getPaddingRight(), getMeasuredHeight() - getPaddingTop()
                - getPaddingBottom() - mTextBound.height());

        int left = getMeasuredWidth() / 2 - bitmapWidth / 2;
        int top = (getMeasuredHeight() - mTextBound.height()) / 2 - bitmapWidth
                / 2;
        // 设置icon的绘制范围
        mIconRect = new Rect(left, top, left + bitmapWidth, top + bitmapWidth);
    }

    @Override
    protected void onDraw(Canvas canvas) {
//        int alpha = (int) Math.ceil((255 * mAlpha));
        canvas.drawBitmap(iconBitMap, null, mIconRect, null);
        setupTargetBitmap(255);
        canvas.drawBitmap(mBitmap, 0, 0, null);
    }

    private void setupTargetBitmap(int alpha)
    {

    }

}
