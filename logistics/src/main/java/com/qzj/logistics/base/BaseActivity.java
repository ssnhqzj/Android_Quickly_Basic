package com.qzj.logistics.base;

import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import com.qzj.empty.EmptyLayout;
import com.qzj.logistics.R;
import com.qzj.logistics.app.AppManager;
import com.qzj.logistics.app.SystemBarTintManager;
import com.qzj.logistics.utils.TextViewDrawableUtils;
import com.qzj.logistics.view.apguide.AppGuide_Activity;

public class BaseActivity extends AppCompatActivity implements View.OnClickListener{

    protected TextView tbCenterView;
    protected TextView tbRightView;
    protected EmptyLayout emptyLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        changeTintBarColor();
        // 添加Activity到堆栈
        AppManager.getAppManager().addActivity(this);
    }

    /**
     * 初始化ToolBar
     */
    protected void initToolBar(com.qzj.logistics.bean.Toolbar tb) {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        android.support.v7.app.ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            //actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setDisplayShowTitleEnabled(false);
        }

        if (tb == null) return;
        TextView tbLeftView = (TextView) toolbar.findViewById(R.id.toolbar_left);
        tbCenterView = (TextView) toolbar.findViewById(R.id.toolbar_center);
        tbRightView = (TextView) toolbar.findViewById(R.id.toolbar_right);
        if (tb.getBg() != 0){
            toolbar.setBackgroundResource(tb.getBg());
        }

        if (tb.getLeftResId() != 0){
            TextViewDrawableUtils.setDrawable(tbLeftView, getResources().getDrawable(tb.getLeftResId()), null, null, null);
            tbLeftView.setVisibility(View.VISIBLE);
        }

        if (tb.getCenterResId() != 0){
            TextViewDrawableUtils.setDrawable(tbCenterView,getResources().getDrawable(tb.getCenterResId()),null,null,null);
            tbCenterView.setVisibility(View.VISIBLE);
        }

        if (tb.getRightResId() != 0){
            TextViewDrawableUtils.setDrawable(tbRightView,getResources().getDrawable(tb.getRightResId()),null,null,null);
            tbRightView.setVisibility(View.VISIBLE);
        }

        if (tb.getRightBgResId() != 0){
            tbRightView.setBackgroundResource(tb.getRightBgResId());
        }

        if (tb.getLeftText() != null && !"".equals(tb.getLeftText())){
            tbLeftView.setText(tb.getLeftText());
            tbLeftView.setVisibility(View.VISIBLE);
        }

        if (tb.getCenterText() != null && !"".equals(tb.getCenterText())){
            tbCenterView.setText(tb.getCenterText());
            tbCenterView.setVisibility(View.VISIBLE);
        }

        if (tb.getRightText() != null && !"".equals(tb.getRightText())){
            tbRightView.setText(tb.getRightText());
            tbRightView.setVisibility(View.VISIBLE);
        }

        tbLeftView.setOnClickListener(this);
        tbCenterView.setOnClickListener(this);
        tbRightView.setOnClickListener(this);
    }

    /**
     * 改变状态栏颜色
     */
    private void changeTintBarColor() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            Window window = getWindow();
            window.setFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS,
                    WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.setFlags(
                    WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION,
                    WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);

            if(!(this instanceof AppGuide_Activity)){
                // 创建状态栏的管理实例
                SystemBarTintManager tintManager = new SystemBarTintManager(this);
                // 激活状态栏设置
                tintManager.setStatusBarTintEnabled(true);
                // 激活导航栏设置
                tintManager.setNavigationBarTintEnabled(true);
                tintManager.setTintColor(Color.parseColor("#FFF72901"));
            }
        }
    }

    /**
     * 初始化空视图
     */
    protected void initEmptyLayout(){
        emptyLayout = (EmptyLayout) findViewById(R.id.empty_layout);
    }

    /**
     * 设置toolbar标题
     * @param text
     */
    protected void setTbCenterView(String text){
        if (tbCenterView != null)
            tbCenterView.setText(text);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        // 结束Activity&从堆栈中移除
        AppManager.getAppManager().finishActivity(this);
    }

    @Override
    public void onClick(View v) {

    }
}
