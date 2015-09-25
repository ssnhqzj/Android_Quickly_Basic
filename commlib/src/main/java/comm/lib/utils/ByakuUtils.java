package comm.lib.utils;

import android.content.Context;
import android.content.Intent;

import comm.lib.byaku.TouchImageActivity;
import comm.lib.byaku.TouchImageViewPagerActivity;

/**
 * qzj
 */
public class ByakuUtils {

    /**
     * 根据rawid查看单张大图
     * @param context
     * @param rawId
     */
    public static void openSingleActivityWithRaw(Context context,int rawId){
        Intent intent = new Intent();
        intent.setClass(context, TouchImageActivity.class);
        intent.putExtra(TouchImageActivity.TYPE, TouchImageActivity.TYPE_RAW);
        intent.putExtra(TouchImageActivity.TYPE_VALUE_RAW,rawId);
        context.startActivity(intent);
    }

    /**
     * 根据sdcard url查看单张大图
     * @param context
     * @param url
     */
    public static void openSingleActivityWithUrl(Context context,String url){
        Intent intent = new Intent();
        intent.setClass(context, TouchImageActivity.class);
        intent.putExtra(TouchImageActivity.TYPE, TouchImageActivity.TYPE_URL);
        intent.putExtra(TouchImageActivity.TYPE_VALUE_URL,url);
        context.startActivity(intent);
    }

    /**
     * 根据rawid查看多张大图
     * @param context
     * @param rawIds
     */
    public static void openViewPagerActivityWithRaw(Context context, int[] rawIds){
        Intent intent = new Intent();
        intent.setClass(context, TouchImageViewPagerActivity.class);
        intent.putExtra(TouchImageViewPagerActivity.TYPE, TouchImageViewPagerActivity.TYPE_RAW);
        intent.putExtra(TouchImageViewPagerActivity.TYPE_VALUE_RAW,rawIds);
        context.startActivity(intent);
    }

    /**
     * 根据sdcard url查看多张大图
     * @param context
     * @param urls
     */
    public static void openViewPagerActivityWithUrl(Context context, String[] urls){
        Intent intent = new Intent();
        intent.setClass(context, TouchImageViewPagerActivity.class);
        intent.putExtra(TouchImageViewPagerActivity.TYPE, TouchImageViewPagerActivity.TYPE_URL);
        intent.putExtra(TouchImageViewPagerActivity.TYPE_VALUE_URL,urls);
        context.startActivity(intent);
    }

}
