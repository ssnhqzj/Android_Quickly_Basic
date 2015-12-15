package com.qzj.logistics.ui;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.LinearLayout;

/**
 * 用于页面遇上错误的时候调用
 */
public class EmptyLayout extends LinearLayout {

    public static final int HIDE_LAYOUT = 4;
    public static final int NETWORK_ERROR = 7;
    public static final int NETWORK_LOADING = 2;
    public static final int NODATA = 3;
    public static final int NODATA_ENABLE_CLICK = 5;
    public static final int NO_LOGIN = 6;

    private final Context context;

    public EmptyLayout(Context context) {
        this(context,null);
    }

    public EmptyLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
    }


}
