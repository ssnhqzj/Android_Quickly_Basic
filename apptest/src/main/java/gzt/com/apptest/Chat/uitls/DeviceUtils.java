package gzt.com.apptest.Chat.uitls;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.view.MotionEvent;
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
     */
    public static void showSoftKeyBoard(Context context){
//        InputMethodManager imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
//        imm.showSoftInput(view, 0);
        Activity activity = (Activity) context;
        if(activity != null){
            InputMethodManager imm = (InputMethodManager) activity.getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.showSoftInputFromInputMethod(activity.getCurrentFocus().getWindowToken(), 0);
            imm.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);
        }
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

    /**
     * 判断当前触摸点是否在v上
     * @param ev
     * @param v
     * @return
     */
    public static boolean isTouchThisView(MotionEvent ev, View v) {
        if (v != null) {
            int[] leftTop = { 0, 0 };
            //获取输入框当前的location位置
            v.getLocationInWindow(leftTop);
            int left = leftTop[0];
            int top = leftTop[1];
            int bottom = top + v.getHeight();
            int right = left + v.getWidth();
            if ( ev.getY()>top && ev.getY()<bottom && ev.getX()>left && ev.getX()<right) {
                return true;
            } else {
                return false;
            }
        }
        return false;
    }

    //        if (ev.getAction() == MotionEvent.ACTION_DOWN) {
//            View v = getCurrentFocus();
//            Log.e("qzj", isShouldHideInput(v, ev) + "");
//            if (isShouldHideInput(v, ev)) {
//                DeviceUtils.hideSoftKeyBoard(this,v);
//            }
//            return super.dispatchTouchEvent(ev);
//        }
//        // 必不可少，否则所有的组件都不会有TouchEvent了
//        if (getWindow().superDispatchTouchEvent(ev)) {
//            return true;
//        }
//        return onTouchEvent(ev);
}
