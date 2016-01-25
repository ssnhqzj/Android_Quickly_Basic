package com.qzj.chat.uitls.popup;

import android.content.Context;
import android.graphics.drawable.BitmapDrawable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;

import com.qzj.chat.adapter.PopupListAdapter;
import com.qzj.chat.uitls.DensityUtils;
import com.qzj.chat.uitls.DisplayUtils;

import java.util.List;

import gzt.com.apptest.R;

/**
 * 列表项的长按弹出菜单
 */
public class PopupList {

    // 三角形宽高dp
    private static final int TRIANGLE_WIDTH = 15;
    private static final int TRIANGLE_HEIGHT = 10;
    private Context context;

    PopupListAdapter.OnItemClickListener onItemClickListener;

    public PopupListAdapter.OnItemClickListener getOnItemClickListener() {
        return onItemClickListener;
    }

    public void setOnItemClickListener(PopupListAdapter.OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    /**
     * 在手指按下的位置显示弹出菜单
     *
     * @param context   上下文，一般为Activity
     * @param parent    上下文view，同时也是用来寻找窗口token的view
     * @param position  点击的列表项在列表数据集中的位置
     * @param popupList 要显示的列表
     * @param rawX      手指按下的屏幕绝对坐标X
     * @param rawY      手指按下的屏幕绝对坐标Y
     */
    public void showPopupWindow(final Context context, View parent, int position, List popupList, float rawX, float rawY) {
        this.context = context;
        // popupWindow要显示的内容
        ViewGroup layoutView = (ViewGroup) LayoutInflater.from(context).inflate(R.layout.chat_pw_long_lick, null);
        RecyclerView recyclerView = (RecyclerView) layoutView.findViewById(R.id.rv_popup);
        //如果item view内容的改变不会影响RecyclerView大小，设置成true以提升表现
        //contentView.setHasFixedSize(true);
        //设置布局管理器，LinearLayoutManager，水平线性布局
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(context);
        linearLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        recyclerView.setLayoutManager(linearLayoutManager);
        //设置适配器
        PopupListAdapter popupListAdapter = new PopupListAdapter(popupList);
        //设置item点击监听
        popupListAdapter.setContextView(parent);
        popupListAdapter.setContextPosition(position);
        popupListAdapter.setOnItemClickListener(this.onItemClickListener);
        recyclerView.setAdapter(popupListAdapter);
        //设置列表分割线
        recyclerView.addItemDecoration(new DividerItemDecoration(context, LinearLayoutManager.HORIZONTAL));
        //recyclerView的item view宽50dp高40dp，列表分割线宽1dp，箭头宽15dp高7dp
        //可计算出弹出窗口的宽高
        int PopupWindowWidth = recyclerView.getAdapter().getItemCount() * dp2px(50 + 1);
        int PopupWindowHeight = dp2px(40 + TRIANGLE_HEIGHT);
        //为水平列表添加箭头指示器，并计算其位置为手指按下位置
        ImageView iv = new ImageView(context);
        iv.setImageResource(R.mipmap.triangle);
        float leftEdgeOffset = rawX;
        float rightEdgeOffset = DisplayUtils.getDisplayWidth(context) - rawX;
        if (leftEdgeOffset < PopupWindowWidth / 2) {
            iv.setTranslationX(leftEdgeOffset - dp2px(TRIANGLE_WIDTH / 2.0f));
        } else if (rightEdgeOffset < PopupWindowWidth / 2) {
            iv.setTranslationX(PopupWindowWidth - rightEdgeOffset - dp2px(TRIANGLE_WIDTH / 2.0f));
        } else {
            iv.setTranslationX(PopupWindowWidth / 2 - dp2px(TRIANGLE_WIDTH / 2.0f));
        }
        iv.setScaleType(ImageView.ScaleType.FIT_XY);
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(dp2px(TRIANGLE_WIDTH), dp2px(TRIANGLE_HEIGHT));
        layoutView.addView(iv, layoutParams);
        //实例化弹出窗口并显示
        final PopupWindow popupWindow = new PopupWindow(layoutView, PopupWindowWidth, PopupWindowHeight, true);
        popupWindow.setTouchable(true);
        //设置背景以便在外面包裹一层可监听触屏等事件的容器
        popupWindow.setBackgroundDrawable(new BitmapDrawable());
        popupWindow.showAtLocation(parent, Gravity.CENTER,
                (int) rawX - DisplayUtils.getDisplayWidth(context) / 2,
                (int) rawY - DisplayUtils.getDisplayHeight(context) / 2 - PopupWindowHeight / 2);
    }
    
    private int dp2px(float value){
        return DensityUtils.dip2px(context, value);
    }
}
