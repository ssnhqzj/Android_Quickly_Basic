package com.qzj.logistics.ui;

import android.app.Dialog;
import android.content.Context;
import android.content.res.Resources;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;

public class CustomDialog extends Dialog {
    public static int WRAP_CONTENT = ViewGroup.LayoutParams.WRAP_CONTENT;

    private LayoutInflater inflater = null;
    private View view = null;
    private static int default_width = 160; // 默认宽度
    private static int default_height = 120;// 默认高度

    public CustomDialog(Context context) {
        super(context);
    }

    public CustomDialog(Context context, int layout, int style) {
        this(context, default_width, default_height, layout, style);
    }

    public CustomDialog(Context context, int width, int height, int layout, int style) {
        super(context, style);
        if(view == null){
            inflater = LayoutInflater.from(context);
            view = inflater.inflate(layout,null);
        }

        // 设置内容
        setContentView(view);
        // 设置窗口属性
        Window window = getWindow();
        WindowManager.LayoutParams params = window.getAttributes();
        // 设置宽度、高度、对齐方式
        float density = getDensity(context);
        params.width = (int) (width * density);
        if(height == WRAP_CONTENT){
            params.height = height;
        }else{
            params.height = (int) (height * density);
        }
        params.gravity = Gravity.CENTER;
        window.setAttributes(params);

    }

    public View getView(){
        return view;
    }

    /**
     * 获取显示密度
     *
     * @param context
     * @return
     */
    public float getDensity(Context context) {
        Resources res = context.getResources();
        DisplayMetrics dm = res.getDisplayMetrics();
        return dm.density;
    }
}
