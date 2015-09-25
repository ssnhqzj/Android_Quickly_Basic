package com.gjt.common.utils;

import com.gjt.common.GJTApplication;

/**
 * Created by yb on 2015/5/6.
 */
public class GeneralConfiguration {
    public static String APP_CACHE_PATH = StorageUtils.getCacheDirectory(GJTApplication.mContext, true).getAbsolutePath();
    public static String DATA_CACHE_PATH = APP_CACHE_PATH + "/cache/";// 存放数据缓存文件，如xml数据等
    public static String IMAGE_CACHE_PATH = APP_CACHE_PATH + "/image/";// 存放图片文件缓存文件
    public static String IMAGE_CACHE_HEADPATH = APP_CACHE_PATH + "/hendimage/";// 存放头像图片文件缓存文件
    public static String IMAGE_UP_PATH = APP_CACHE_PATH + "/upimage/";// 存放上传压缩图片文件
    public static String USER_UP_IMG_PATH = APP_CACHE_PATH + "/user/";// 存放用户文件
}
