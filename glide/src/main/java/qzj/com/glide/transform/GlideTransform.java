package qzj.com.glide.transform;

import android.content.Context;
import android.graphics.Bitmap;

import com.bumptech.glide.load.Transformation;
import com.bumptech.glide.load.resource.bitmap.CenterCrop;

import jp.wasabeef.glide.transformations.CropCircleTransformation;
import jp.wasabeef.glide.transformations.CropSquareTransformation;
import jp.wasabeef.glide.transformations.CropTransformation;
import jp.wasabeef.glide.transformations.MaskTransformation;
import jp.wasabeef.glide.transformations.RoundedCornersTransformation;

/**
 * Created by Administrator on 2016/1/25.
 * Glide转换器
 */
public class GlideTransform {

    /**
     * 剪切类型
     */
    enum Crop {
        TOP,
        CENTER,
        BOTTOM,
        SQUARE,
        CIRCLE
    }

    /**
     * 遮罩,9patch遮罩
     * @param context
     * @param maskResId
     * @return
     */
    public static Transformation<Bitmap>[] getMask(Context context,int maskResId){
        return new Transformation[]{
                new CenterCrop(context),
                new MaskTransformation(context, maskResId)
        };
    }

    /**
     * 剪切
     * @param context
     * @param width
     * @param height
     * @param crop
     * @return
     */
    public static Transformation<Bitmap>[] getCrop(Context context,int width,int height,Crop crop){
        // 剪切顶部
        if(crop == Crop.TOP){
            return new Transformation[]{
                    new CropTransformation(context, width, height, CropTransformation.CropType.TOP)
            };
        }
        // 剪切中心
        else if(crop == Crop.CENTER){
            return new Transformation[]{
                    new CropTransformation(context, width, height, CropTransformation.CropType.CENTER)
            };
        }
        // 剪切底部
        else if(crop == Crop.BOTTOM){
            return new Transformation[]{
                    new CropTransformation(context, width, height, CropTransformation.CropType.BOTTOM)
            };
        }
        // 剪切正方形
        else if(crop == Crop.SQUARE){
            return new Transformation[]{
                    new CropSquareTransformation(context)
            };
        }
        // 剪切圆
        else if(crop == Crop.CIRCLE){
            return new Transformation[]{
                    new CropCircleTransformation(context)
            };
        }

        return null;
    }

    /**
     * 转化成圆角图片
     * @param context
     * @param radius 圆角半径
     * @param margin
     * @param cornerType 圆角类型
     * @return
     */
    public static Transformation<Bitmap>[] getRound(Context context, int radius, int margin, RoundedCornersTransformation.CornerType cornerType){
        return new Transformation[]{
                new RoundedCornersTransformation(context, radius, margin, cornerType)
        };
    }

}
