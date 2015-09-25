package com.gjt.view.apguide;

import android.os.Bundle;
import android.support.v4.view.ViewPager;

import com.gjt.R;
import com.gjt.common.BaseFragmentActivity;
import com.viewpagerindicator.CirclePageIndicator;

import java.util.ArrayList;
import java.util.List;


public class AppGuide_Activity extends BaseFragmentActivity {
    private CirclePageIndicator mIndicator ;
    private ViewPager mViewPager ;
    private AppGuide_Adapter mAdapter ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_app_guide);
        if(getActionBar() != null) getActionBar().hide();
        initView();
    }

    private void initView() {
        mIndicator = (CirclePageIndicator) findViewById(R.id.guide_indicator);
        mViewPager = (ViewPager) findViewById(R.id.guide_pager);
        List<Integer> lists = new ArrayList<Integer>();
          lists.add(R.layout.frament_app_guide1);
          lists.add(R.layout.frament_app_guide2);
          lists.add(R.layout.frament_app_guide3);
          lists.add(R.layout.frament_app_guide4);
        mAdapter = new AppGuide_Adapter(getSupportFragmentManager(),lists);
        mViewPager.setAdapter(mAdapter);
        mIndicator.setViewPager(mViewPager, 0);
    }


}
