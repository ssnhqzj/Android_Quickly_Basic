package com.qzj.logistics.view.deliver;

import android.text.Editable;
import android.text.TextWatcher;

import com.qzj.logistics.bean.HisReceiver;

/**
 * Created by Administrator on 2015/12/18.
 */
public class DeliverEditTextWatcher implements TextWatcher {
    // 姓名
    public static final int FIELD_NAME = 1;
    // 电话
    public static final int FIELD_PHONE = 2;
    // 地址
    public static final int FIELD_ADDR = 3;
    // 邮编
    public static final int FIELD_POSTCODE = 4;
    // 快递单号
    public static final int FIELD_NUMBER = 5;

    private HisReceiver hisReceiver;
    private int field;

    public DeliverEditTextWatcher(HisReceiver hisReceiver,int field){
        this.hisReceiver = hisReceiver;
        this.field = field;
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {

    }

    @Override
    public void afterTextChanged(Editable s) {
        switch (field){
            case FIELD_NAME:
                hisReceiver.setName(s.toString());
                break;

            case FIELD_PHONE:
                hisReceiver.setTelephone(s.toString());
                break;

            case FIELD_ADDR:
                hisReceiver.setAddr(s.toString());
                break;

            case FIELD_POSTCODE:
                hisReceiver.setMail_code(s.toString());
                break;

            case FIELD_NUMBER:
                hisReceiver.setCourier_number(s.toString());
                break;
        }
    }

    public void setHisReceiver(HisReceiver hisReceiver) {
        this.hisReceiver = hisReceiver;
    }
}
