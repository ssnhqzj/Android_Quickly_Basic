package com.qzj.logistics.view;

import android.os.Bundle;

import com.qzj.logistics.R;
import com.qzj.logistics.base.BaseActivity;

public class MainActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initToolBar("main",false);
    }

}
