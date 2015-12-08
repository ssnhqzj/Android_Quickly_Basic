package gzt.com.apptest.Chat.uitls;

import android.content.Context;
import android.graphics.drawable.BitmapDrawable;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.PopupWindow;

import gzt.com.apptest.R;

/**
 * Created by qzj on 2015/12/7.
 */
public class PopupWindowUtil {

    private static PopupWindow popupWindow = null;

    public static void showPopUp(Context context, View parent) {
        View layout = LayoutInflater.from(context).inflate(R.layout.chat_pw_long_lick,null);
        popupWindow = new PopupWindow(layout,ViewGroup.LayoutParams.WRAP_CONTENT,ViewGroup.LayoutParams.WRAP_CONTENT);
        popupWindow.setFocusable(true);
        popupWindow.setOutsideTouchable(true);
        popupWindow.setBackgroundDrawable(new BitmapDrawable());

        int[] location = new int[2];
        parent.getLocationOnScreen(location);

        popupWindow.showAtLocation(parent, Gravity.NO_GRAVITY, location[0], location[1] - popupWindow.getHeight());
    }

    public static void openPopupWin(Context context, View parent) {
        LayoutInflater mLayoutInflater = (LayoutInflater) context.getSystemService(context.LAYOUT_INFLATER_SERVICE);
        ViewGroup menuView = (ViewGroup) mLayoutInflater.inflate(R.layout.chat_pw_long_lick, null, true);
        popupWindow = new PopupWindow(menuView, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT, true);
        popupWindow.setBackgroundDrawable(new BitmapDrawable());
        popupWindow.setAnimationStyle(R.style.PopupAnimation);
        popupWindow.showAtLocation(parent, Gravity.CENTER | Gravity.CENTER, 0, 0);
        popupWindow.update();
    }


}
