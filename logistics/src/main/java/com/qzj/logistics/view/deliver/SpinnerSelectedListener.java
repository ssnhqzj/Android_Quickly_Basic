package com.qzj.logistics.view.deliver;

import android.view.View;
import android.widget.AdapterView;
import android.widget.Spinner;

import com.qzj.logistics.bean.HisReceiver;

/**
 * Created by Administrator on 2015/12/21.
 */
public class SpinnerSelectedListener implements Spinner.OnItemSelectedListener {

    // 城市
    public static final int TYPE_CITIES = 1;
     // 货物类别
    public static final int TYPE_THING_TYPES = 2;

    private HisReceiver hisReceiver;
    private int type;

    public SpinnerSelectedListener(HisReceiver hisReceiver,int type){
        this.hisReceiver = hisReceiver;
        this.type = type;
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
       switch (type){
           case TYPE_THING_TYPES:
               hisReceiver.setType(position);
               break;
       }
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
}
