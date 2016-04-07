package com.qzj.logistics.utils;

import android.graphics.drawable.Drawable;
import android.widget.TextView;

/**
 * TextView Drawable工具类
 */
public class TextViewDrawableUtils {

    public static void setDrawable(TextView view,Drawable left,Drawable top,Drawable right,Drawable bottom){
        if(left != null){
            left.setBounds(0, 0, left.getMinimumWidth(), left.getMinimumHeight());
        }

        if(top != null){
            top.setBounds(0, 0, top.getMinimumWidth(), top.getMinimumHeight());
        }

        if(right != null){
            right.setBounds(0, 0, right.getMinimumWidth(), right.getMinimumHeight());
        }

        if(bottom != null){
            bottom.setBounds(0, 0, bottom.getMinimumWidth(), bottom.getMinimumHeight());
        }

        view.setCompoundDrawables(left,top,right,bottom);
    }

    public static void clearDrawable(TextView view){
        view.setCompoundDrawables(null,null,null,null);
    }
}
