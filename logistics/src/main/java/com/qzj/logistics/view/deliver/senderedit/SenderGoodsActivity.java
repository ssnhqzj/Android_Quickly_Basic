package com.qzj.logistics.view.deliver.senderedit;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;

import com.qzj.logistics.R;
import com.qzj.logistics.base.BaseActivity;
import com.qzj.logistics.bean.MessageEvent;
import com.qzj.logistics.bean.Toolbar;

import java.util.ArrayList;

import de.greenrobot.event.EventBus;

public class SenderGoodsActivity extends BaseActivity {

    private  ArrayList<Fragment> fragments;
    private ViewPager viewPager;
    private SenderPagerAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_deliver_sender);
        Toolbar toolbar = new Toolbar();
        toolbar.setBg(R.color.common_main);
        toolbar.setLeftResId(R.mipmap.return_up);
        toolbar.setCenterText("发货人编辑");
        toolbar.setRightText("完成");
        toolbar.setRightBgResId(R.drawable.common_btn_main_selector);
        initToolBar(toolbar);
        initView();
    }

    private void initView(){
        fragments = new ArrayList<>();
        SenderGoodsFragment oneFragment = new SenderGoodsFragment();
        fragments.add(oneFragment);

        viewPager = (ViewPager) findViewById(R.id.deliver_goods_viewpager);
        adapter = new SenderPagerAdapter(getSupportFragmentManager());
        viewPager.setAdapter(adapter);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.toolbar_left:
                this.finish();
                break;
            case R.id.toolbar_right:
                EventBus.getDefault().post(new MessageEvent(MessageEvent.FLAG_SENDER_ADDRESS));
                break;
        }
    }

    class SenderPagerAdapter extends FragmentPagerAdapter {

        public SenderPagerAdapter(FragmentManager fm)
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
