package com.qzj.logistics.view.deliver.senderedit;

import android.text.Editable;
import android.text.TextWatcher;

import com.qzj.logistics.bean.AddrMoare;

/**
 * Created by Administrator on 2015/12/18.
 */
public class SenderEditTextWatcher implements TextWatcher {
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

    private AddrMoare addrMoare;
    private int field;

    public SenderEditTextWatcher(AddrMoare addrMoare, int field){
        this.addrMoare = addrMoare;
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
                addrMoare.setName(s.toString());
                break;

            case FIELD_PHONE:
                addrMoare.setTelephone(s.toString());
                break;

            case FIELD_ADDR:
                addrMoare.setAddr(s.toString());
                break;

            case FIELD_POSTCODE:
                addrMoare.setMail_code(s.toString());
                break;
        }
    }

    public void setSenderEdit(AddrMoare addrMoare) {
        this.addrMoare = addrMoare;
    }
}
