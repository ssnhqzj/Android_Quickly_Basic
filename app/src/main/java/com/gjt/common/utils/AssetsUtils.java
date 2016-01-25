package com.gjt.common.utils;

import android.content.Context;

import java.io.InputStream;

/**
 * Created by Administrator on 2015/6/3.
 */
public class AssetsUtils {
    public static String getFromAssets(Context context, String fileName){
        String result = "";
        try {
            InputStream in = context.getResources().getAssets().open(fileName);
            //获取文件的字节数
            int length = in.available();
            //创建byte数组
            byte[]  buffer = new byte[length];
            //将文件中的数据读到byte数组中
            in.read(buffer);
            result = new String(buffer, "UTF-8");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }
}
