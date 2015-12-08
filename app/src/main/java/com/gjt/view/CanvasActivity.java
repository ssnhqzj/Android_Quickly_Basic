package com.gjt.view;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.RectF;
import android.os.Bundle;
import android.widget.ImageView;

import com.gjt.R;
import com.gjt.common.BaseFragmentActivity;

/**
 * Created by Administrator on 2015/10/21.
 */
public class CanvasActivity extends BaseFragmentActivity{
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_canvas);

        ImageView imageView = (ImageView) findViewById(R.id.canvas_img);
//        imageView.setImageBitmap(createCircleImage(400,PorterDuff.Mode.SRC_IN));
        imageView.setImageBitmap(createCanvasImage(400));
    }

    // create a bitmap with a circle, used for the "dst" image
    static Bitmap makeDst(int w, int h) {
        Bitmap bm = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888);
        Canvas c = new Canvas(bm);
        Paint p = new Paint(Paint.ANTI_ALIAS_FLAG);

        p.setColor(0xFFFFCC44);
        c.drawOval(new RectF(0, 0, w * 3 / 4, h * 3 / 4), p);
        return bm;
    }

    // create a bitmap with a rect, used for the "src" image
    static Bitmap makeSrc(int w, int h) {
        Bitmap bm = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888);
        Canvas c = new Canvas(bm);
        Paint p = new Paint(Paint.ANTI_ALIAS_FLAG);

        p.setColor(0xFF66AAFF);
        c.drawRect(w / 3, h / 3, w * 19 / 20, h * 19 / 20, p);
        return bm;
    }

    private Bitmap createCircleImage(int min,PorterDuff.Mode mode){
        final Paint paint = new Paint();
        paint.setColor(0xFF00FF00);
        paint.setAntiAlias(true);
        Bitmap target = Bitmap.createBitmap(min, min, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(target);

        Bitmap dst = makeDst(min, min);
        Bitmap src = makeSrc(min, min);
        canvas.drawBitmap(dst, 0, 0, paint);
        paint.setXfermode(new PorterDuffXfermode(mode));
        canvas.drawBitmap(src, 0, 0, paint);
        paint.setXfermode(null);

        return target;
    }

    private Bitmap createCanvasImage(int min){
        Bitmap target = Bitmap.createBitmap(min, min, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(target);

        Paint pOval = new Paint(Paint.ANTI_ALIAS_FLAG);
        pOval.setColor(0xFFFFCC44);
        canvas.drawOval(new RectF(0, 0, min * 3 / 4, min * 3 / 4), pOval);

        Paint pRect = new Paint(Paint.ANTI_ALIAS_FLAG);
        pRect.setColor(0xFF66AAFF);
        pRect.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
        canvas.drawRect(min / 3, min / 3, min * 19 / 20,min * 19 / 20, pRect);

        return target;
    }
}
