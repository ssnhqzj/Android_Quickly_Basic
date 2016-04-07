package com.qzj.logistics.view.deliver;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.KeyEvent;
import android.view.View;
import android.widget.CheckBox;
import android.widget.Toast;

import com.qzj.logistics.R;
import com.qzj.logistics.app.AppManager;
import com.qzj.logistics.app.MyApplication;
import com.qzj.logistics.base.BaseActivity;
import com.qzj.logistics.bean.PrintWifi;
import com.qzj.logistics.bean.Toolbar;
import com.qzj.logistics.utils.print.PrintConnector;
import com.qzj.logistics.view.usercenter.SettingActivity;
import com.qzj.logistics.view.usercenter.WifiSettingActivity;

import java.util.ArrayList;

public class DeliverGoodsActivity extends BaseActivity {
    private static final int REQUEST_CODE_WIFI_SETTING = 1;

    private ArrayList<CheckBox> menus = new ArrayList<CheckBox>();
    private CheckBox menuOne;
    private CheckBox menuMore;
    private  ArrayList<Fragment> fragments;
    private DeliverGoodsFragment oneFragment;
    private DeliverGoodsFragment moreFragment;
    private ViewPager viewPager;
    private DeliverPagerAdapter adapter;
    private long exitTime = 0;
    private PrintConnector printConnector;
    private MyApplication myApp;
    private boolean isFirstIn = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_deliver_goods);
        myApp = (MyApplication) getApplication();
        Toolbar toolbar = new Toolbar();
        toolbar.setBg(R.color.common_main);
        toolbar.setCenterText("收件人填写");
        toolbar.setRightResId(R.mipmap.user);
        toolbar.setRightBgResId(R.drawable.common_btn_main_selector);
        initToolBar(toolbar);

        initView();
    }

    @Override
    protected void onResume() {
        super.onResume();
        // 第一次进入或者wifi配置信息有改变时重新初始化打印机
        if (isFirstIn || myApp.isPrintSettingChanged()){
            initPrint();
        }

        if (isFirstIn) isFirstIn = false;
    }

    private void initView(){
        menuOne = (CheckBox) findViewById(R.id.deliver_goods_menu_one);
        menuMore = (CheckBox) findViewById(R.id.deliver_goods_menu_more);
        menuOne.setOnClickListener(this);
        menuMore.setOnClickListener(this);
        menus.add(0, menuOne);
        menus.add(1,menuMore);

        fragments = new ArrayList<>();
        oneFragment = new DeliverGoodsFragment();
        Bundle oneBundle = new Bundle();
        oneBundle.putInt("mode", DeliverGoodsFragment.MODE_ONE);
        oneFragment.setArguments(oneBundle);
        fragments.add(oneFragment);

        moreFragment = new DeliverGoodsFragment();
        Bundle moreBundle = new Bundle();
        moreBundle.putInt("mode", DeliverGoodsFragment.MODE_MORE);
        moreFragment.setArguments(moreBundle);
        fragments.add(moreFragment);

        viewPager = (ViewPager) findViewById(R.id.deliver_goods_viewpager);
        adapter = new DeliverPagerAdapter(getSupportFragmentManager());
        viewPager.addOnPageChangeListener(new DeliverPageChangeListener());
        viewPager.setAdapter(adapter);
    }

    /**
     * 初始化打印机
     */
    private void initPrint(){
        if (myApp != null){
            PrintWifi printWifi = myApp.getPrintWifi();
            if (printWifi == null){
                openWifiSettingActivity();
                return;
            }

            if (!printWifi.isChecked()){
                if (printWifi.getIp()==null || "".equals(printWifi.getIp())
                    ||printWifi.getPort()==null ||"".equals(printWifi.getPort())){
                    openWifiSettingActivity();
                    return;
                }

                // 连接打印机
                printConnector = new PrintConnector(this,printWifi.getIp(),printWifi.getPort());
                printConnector.connect();

                // 打印机设置给fragment
                oneFragment.setPrintConnector(printConnector);
                moreFragment.setPrintConnector(printConnector);
            }
        }
    }

    /**
     * 打开wifi配置界面
     */
    private void openWifiSettingActivity(){
        Intent intent = new Intent();
        intent.setClass(this, WifiSettingActivity.class);
        startActivity(intent);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            // 用户中心
            case R.id.toolbar_right:
                Intent settingIntent = new Intent();
                settingIntent.setClass(this,SettingActivity.class);
                startActivity(settingIntent);
                break;

            // 单人发货
            case R.id.deliver_goods_menu_one:
                changeMenuState(v);
                break;

            // 多人发货
            case R.id.deliver_goods_menu_more:
                changeMenuState(v);
                break;
        }
    }

    private void changeMenuState(View view){
        for (int i=0;i<menus.size();i++){
            if (menus.get(i) == view){
                viewPager.setCurrentItem(i);
                menus.get(i).setChecked(true);
                continue;
            }
            menus.get(i).setChecked(false);
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK){
            exit();
        }else{
            return super.onKeyDown(keyCode, event);
        }
        return true;
    }

    public void exit() {
        if ((System.currentTimeMillis() - exitTime) > 2000) {
            Toast.makeText(getApplicationContext(), "再按一次退出程序", Toast.LENGTH_SHORT).show();
            exitTime = System.currentTimeMillis();
        } else {
            AppManager.getAppManager().finishAllActivity();
        }
    }

    class DeliverPageChangeListener implements ViewPager.OnPageChangeListener{

        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

        }

        @Override
        public void onPageSelected(int position) {
            changeMenuState(menus.get(position));
        }

        @Override
        public void onPageScrollStateChanged(int state) {

        }
    }

    class DeliverPagerAdapter extends FragmentPagerAdapter {

        public DeliverPagerAdapter(FragmentManager fm)
        {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            return fragments.get(position);
        }

        @Override
        public int getCount() {
            return fragments.size();
        }

        @Override
        public int getItemPosition(Object object) {
            return POSITION_NONE;
        }
        @Override
        public boolean isViewFromObject(View view, Object obj) {
            return view == ((Fragment) obj).getView();
        }
    }
}
