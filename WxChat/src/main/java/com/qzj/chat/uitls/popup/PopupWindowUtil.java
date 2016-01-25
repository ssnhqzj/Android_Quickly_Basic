package com.qzj.chat.uitls.popup;

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

    /**
     * 聊天室中的toolbar中的更多下拉pw
     * @param context
     * @param parent
     */
    public static void showPwMore(Context context, View parent) {
        View layout = LayoutInflater.from(context).inflate(R.layout.chat_room_toolbar_more_item,null);
        popupWindow = new PopupWindow(layout,ViewGroup.LayoutParams.WRAP_CONTENT,ViewGroup.LayoutParams.WRAP_CONTENT);
        popupWindow.setFocusable(true);
        popupWindow.setOutsideTouchable(true);
        popupWindow.setBackgroundDrawable(new BitmapDrawable());

        int[] location = new int[2];
        parent.getLocationOnScreen(location);

        popupWindow.showAtLocation(parent, Gravity.NO_GRAVITY,
                location[0], location[1]+parent.getHeight()+5);
    }

    /**
     * 聊天室中的长按弹出菜单
     * @param context
     * @param parent
     */
    public static void openPwMenu(Context context, View parent) {
//        PointF pointF = (PointF) parent.getTag();
//        ViewGroup menuView = (ViewGroup) LayoutInflater.from(context).inflate(R.layout.chat_pw_long_lick, null);
//        ImageView imageView = (ImageView) menuView.findViewById(R.id.sanjiao);
//        int w = View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED);
//        int h = View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED);
//        menuView.measure(w, h);
//        int viewWidth = menuView.getMeasuredWidth();
//        int viewHeight = menuView.getMeasuredHeight();
//        Log.e("qzj", "---------getMeasuredHeight-------" + viewWidth +","+viewHeight);
//
//        RelativeLayout.LayoutParams params= (RelativeLayout.LayoutParams) imageView.getLayoutParams();
//        params.leftMargin = (int) Math.abs(menuView.getX()-pointF.x);
//
//        popupWindow = new PopupWindow(menuView, ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT, true);
//        popupWindow.setFocusable(true);
//        popupWindow.setOutsideTouchable(true);
//        popupWindow.setBackgroundDrawable(new BitmapDrawable());
//        popupWindow.showAtLocation(parent, Gravity.NO_GRAVITY, (int)pointF.x-viewWidth/2, (int)pointF.y-viewHeight);
    }

}
