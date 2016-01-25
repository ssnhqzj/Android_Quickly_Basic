package com.qzj.logistics.view.apguide;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.qzj.logistics.R;
import com.qzj.logistics.app.Host;
import com.qzj.logistics.base.BaseActivity;
import com.qzj.logistics.bean.CityData;
import com.qzj.logistics.bean.Division;
import com.qzj.logistics.db.dao.HatAreaDao;
import com.qzj.logistics.db.dao.HatCityDao;
import com.qzj.logistics.db.dao.HatProvinceDao;
import com.qzj.logistics.utils.PreferenceUtils;
import com.qzj.logistics.view.usercenter.LoginActivity;
import com.squareup.okhttp.Request;
import com.zhy.http.okhttp.JsonOkHttpUtils;
import com.zhy.http.okhttp.callback.JsonCallBack;

public class BootActivity extends BaseActivity {
    private static final String TAG = "BootActivity";

    private static final int TOMINA=0;//跳转到主界面
    private static final int TOGUIDE=1;//跳转到导航界面
    private int STARTFALG=0;

    private TextView hint;

    private Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            Intent intent =new Intent();
            switch(msg.what){
                case TOGUIDE:
                    intent.setClass(BootActivity.this,AppGuide_Activity.class);
                    break;
                case TOMINA:
                    intent.setClass(BootActivity.this, LoginActivity.class);
                    break;
            }
            startActivity(intent);
            finish();
            overridePendingTransition(R.anim.bootactivity_open_in_anim, R.anim.bootactivity_close_out_anim);
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_boot);
        initView();
    }

    private void initView() {
        hint = (TextView) findViewById(R.id.boot_update_area_hint);
        STARTFALG= PreferenceUtils.getPrefInt(this, "STARTFALG", 1);
        if (STARTFALG == TOGUIDE){
            skipActivity();
        }else{
            checkAreaUpdate();
        }
    }

    /**
     * 检测城市区划更新
     */
    private void checkAreaUpdate() {
        JsonOkHttpUtils.getInstance().post(Host.CHECK_VERSION_AREA, null).execute(new JsonCallBack() {

            @Override
            public void onError(Request request, Exception e) {
                Log.e(TAG, "onError:" + e.getMessage());
                skipActivity();
            }

            @Override
            public void onResponse(JSONObject response) {
                if (response != null && response.getIntValue("resultCode")==1){
                    Log.e(TAG, "division:" + response.toString());
                    int updateCode = response.getIntValue("editionCode");
                    if (updateCode == 2){
                        getAreaUpdate();
                        return;
                    }
                }
                skipActivity();
            }
        });
    }

    /**
     * 获取城市区划更新
     */
    private void getAreaUpdate() {
        JsonOkHttpUtils.getInstance().post(Host.GET_AREA, null).execute(new JsonCallBack() {

            @Override
            public void onBefore(Request request) {
                super.onBefore(request);
                hint.setVisibility(View.VISIBLE);
            }

            @Override
            public void onError(Request request, Exception e) {
                Log.e(TAG, "onError:" + e.getMessage());
                Toast.makeText(BootActivity.this,"区划更新失败",Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onResponse(JSONObject response) {
                Division division = JSON.toJavaObject(response,Division.class);
                Log.e(TAG, "division:" + division.toString());
                CityData cityData = division.getCityData();
                if (cityData != null){
                    // 更新省份
                    if (cityData.getHatProvinceList() != null && cityData.getHatProvinceList().size() > 0) {
                        HatProvinceDao hatProvinceDao = new HatProvinceDao(BootActivity.this);
                        Log.e(TAG, "clear table 'hat_province':" + hatProvinceDao.clearTable());
                        hatProvinceDao.addAll(cityData.getHatProvinceList());
                    }

                    // 更新城市
                    if (cityData.getHatCityList() != null && cityData.getHatCityList().size() > 0) {
                        HatCityDao hatCityDao = new HatCityDao(BootActivity.this);
                        Log.e(TAG, "clear table 'hat_city':" + hatCityDao.clearTable());
                        hatCityDao.addAll(cityData.getHatCityList());
                    }

                    // 更新城区
                    if (cityData.getHatAreaList() != null && cityData.getHatAreaList().size() > 0) {
                        HatAreaDao hatAreaDao = new HatAreaDao(BootActivity.this);
                        Log.e(TAG, "clear table 'hat_area':" + hatAreaDao.clearTable());
                        hatAreaDao.addAll(cityData.getHatAreaList());
                    }
                }
            }

            @Override
            public void onAfter() {
                super.onAfter();
                hint.setVisibility(View.GONE);
                skipActivity();
            }
        });
    }

    /**
     * 主页面和引导页面选择性跳转
     */
    private void skipActivity(){
        Message msg = handler.obtainMessage();
        switch (STARTFALG){
            case TOMINA:
                msg.what=TOMINA;
                handler.sendMessageDelayed(msg, 2000);
                break;
            case TOGUIDE:
                msg.what=TOGUIDE;
                handler.sendMessageDelayed(msg, 2000);
                break;
        }

        if (STARTFALG == TOGUIDE){
            PreferenceUtils.setPrefInt(this,"STARTFALG",TOMINA);
        }
    }

}
