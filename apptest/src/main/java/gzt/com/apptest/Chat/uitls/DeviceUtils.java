package gzt.com.apptest.Chat.uitls;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;

import comm.lib.downloader.log.Log;

/**
 * Created by qzj on 2015/12/8.
 */
public class DeviceUtils {

    public static void toggleSoftKeyBoard(Context context){
        InputMethodManager imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.toggleSoftInput(InputMethodManager.SHOW_IMPLICIT, InputMethodManager.HIDE_NOT_ALWAYS);
    }

    /**
     * 隐藏软键盘
     * @param context
     * @param view
     */
    public static void hideSoftKeyBoard(Context context,View view){
        InputMethodManager imm = (InputMethodManager)context.getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }

    /**
     * 显示软键盘
     * @param context
     * @param view
     */
    public static void showSoftKeyBoard(Context context,View view){
        InputMethodManager imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.showSoftInput(view, 0);
    }

    /**
     * 保存键盘高度
     * @param context
     * @param height
     */
    public static void saveKeyBroadHeight(Context context, int height){
        SharedPreferences preferences = context.getSharedPreferences("user", Context.MODE_PRIVATE | Context.MODE_MULTI_PROCESS);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putInt("key_broad_height",height);
        editor.commit();
    }

    /**
     * 获取保存的键盘高度
     * @param context
     * @return
     */
    public static int getKeyBroadHeight(Context context){
        SharedPreferences preferences = context.getSharedPreferences("user", Context.MODE_PRIVATE | Context.MODE_MULTI_PROCESS);
        int height = preferences.getInt("key_broad_height", 0);
        if (height == 0){
            height = DisplayUtils.getDisplayHeight(context)*2/5;
        }
        return height;
    }

    /**
     * 是否调整窗口
     * @param activity
     * @param isAdjust
     */
    public static void isAdjustWindow(Activity activity,boolean isAdjust){
        Log.e("qzj","isAdjust:"+isAdjust);
        if (isAdjust){
            activity.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
        }else{
            activity.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_NOTHING);
        }
    }

}
