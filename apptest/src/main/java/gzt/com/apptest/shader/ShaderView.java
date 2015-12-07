package gzt.com.apptest.shader;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Shader;
import android.util.AttributeSet;
import android.view.View;

import gzt.com.apptest.R;
import gzt.com.apptest.utils.DisplayUtils;

/**
 * Created by Administrator on 2015/11/9.
 */
public class ShaderView extends View {

    private static final int RECT_SIZE = 300;// 矩形尺寸的一半

    private Paint mPaint;// 画笔

    private int left, top, right, bottom;// 矩形坐上右下坐标

    public ShaderView(Context context, AttributeSet attrs) {
        super(context, attrs);

        // 获取屏幕中点坐标
        int screenX = DisplayUtils.getDisplayWidth(context) / 2;
        int screenY = DisplayUtils.getDisplayHeight(context) / 2;

        // 计算矩形左上右下坐标值
        left = screenX - RECT_SIZE;
        top = screenY - RECT_SIZE;
        right = screenX + RECT_SIZE;
        bottom = screenY + RECT_SIZE;

        // 实例化画笔
        mPaint = new Paint(Paint.ANTI_ALIAS_FLAG | Paint.DITHER_FLAG);

        // 获取位图
        Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.mipmap.test_1);

        // 设置着色器
        mPaint.setShader(new BitmapShader(bitmap, Shader.TileMode.REPEAT, Shader.TileMode.REPEAT));

    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec,heightMeasureSpec);
        setMeasuredDimension(RECT_SIZE*2, RECT_SIZE*2);
    }

    @Override
    protected void onDraw(Canvas canvas) {

        // 绘制矩形
        canvas.drawRect(0, 0, RECT_SIZE*2, RECT_SIZE*2, mPaint);

    }
}
