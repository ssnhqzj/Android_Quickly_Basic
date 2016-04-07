package com.qzj.logistics.templete;

import android.content.Context;

import com.qzj.logistics.bean.AddrMoare;
import com.qzj.logistics.bean.HisReceiver;
import com.qzj.logistics.utils.print.PrintConnector;

/**
 * EMS快递单
 */
public class EMSBill extends BaseBill {

    public EMSBill() {}

    public EMSBill(Context context, PrintConnector printConnector) {
        super(context, printConnector);
    }

    @Override
    protected void buildBill(AddrMoare am,HisReceiver hr) {
        // 顺向走纸200单位,走纸到打印位置
        skipPaper(200);

        // [[-->
        moveDown(130);
        // <--]]

        // 打印寄件人姓名
        moveRight(70);
        printString(am.getName() == null ? "" : am.getName());

        // 寄件人电话
        fixedX(290);
        printString(am.getTelephone() == null ? "" : am.getTelephone());

        // 寄件人地址
        resetHeadToLeft();
        moveDown(60);
        printAutoChangeLines(am.getFullAddr() == null ? "" : am.getFullAddr(),20,70);

        // 寄件人邮编
        resetHeadToLeft();
        moveDown(35);
        fixedX(350);
        printString(am.getMail_code() == null ? "" : am.getMail_code());

        // 收件人姓名
        resetHeadToLeft();
        moveDown(50);
        moveRight(70);
        printString(hr.getName() == null ? "" : hr.getName());

        // 收件人电话
        fixedX(290);
        printString(hr.getTelephone() == null ? "" : hr.getTelephone());

        // 收件人地址
        resetHeadToLeft();
        moveDown(60);
        printAutoChangeLines(hr.getFullAddr() == null ? "" : hr.getFullAddr(),20,70);

        // 寄达城市
        resetHeadToLeft();
        moveDown(65);
        moveRight(90);
        printString(hr.getC_name() == null ? "" : hr.getC_name());

        // 收件人邮编
        fixedX(350);
        printString(hr.getMail_code() == null ? "" : hr.getMail_code());

    }
}
