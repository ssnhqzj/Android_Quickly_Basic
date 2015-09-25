package com.gjt.view.apguide;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;

import com.gjt.R;
import com.gjt.common.BaseActivity;
import com.gjt.common.utils.PreferenceUtils;
import com.gjt.view.MainActivity;

public class BootActivity extends BaseActivity {

    private static final int TOMINA=0;//跳转到主界面
    private static final int TOGUIDE=1;//跳转到导航界面
    private int STARTFALG=0;


    private Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            Intent intent =new Intent();
            switch(msg.what){
                case TOGUIDE:
                    intent.setClass(BootActivity.this,AppGuide_Activity.class);
                    break;
                case TOMINA:
                    intent.setClass(BootActivity.this, MainActivity.class);
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
        STARTFALG= PreferenceUtils.getPrefInt(this, "STARTFALG", 1);
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
        if (STARTFALG==1){
            PreferenceUtils.setPrefInt(this,"STARTFALG",TOMINA);
        }
    }

}
