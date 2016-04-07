package com.qzj.logistics.templete;

import android.content.Context;

import com.qzj.logistics.bean.AddrMoare;
import com.qzj.logistics.bean.HisReceiver;
import com.qzj.logistics.utils.print.PrintConnector;

/**
 * 优速快递单
 */
public class USBill extends BaseBill {

    public USBill(){}

    public USBill(Context context, PrintConnector printConnector) {
        super(context, printConnector);
    }

    @Override
    protected void buildBill(AddrMoare am, HisReceiver hr) {
        // 顺向走纸200单位,走纸到打印位置
        skipPaper(200);

        // [[-->
        moveDown(140);
        // <--]]

        // 收寄站
        moveRight(120);
        printString(am.getcName() == null ? "" : am.getcName());

        // 目的站
        fixedX(480);
        printString(hr.getC_name() == null ? "" : hr.getC_name());

        // 寄件人地址
        resetHeadToLeft();
        moveDown(90);
        printAutoChangeLines(am.getFullAddr() == null ? "" : am.getFullAddr(), 14, 50);

        // 收件人地址
        printAutoChangeLines(hr.getFullAddr() == null ? "" : hr.getFullAddr(), 14, 360);

        // 寄件人
        resetHeadToLeft();
        moveDown(75);
        moveRight(50);
        printString(am.getName() == null ? "" : am.getName());

        // 寄件人电话
        fixedX(180);
        printString(am.getTelephone() == null ? "" : am.getTelephone());

        // 收件人
        fixedX(360);
        printString(hr.getName() == null ? "" : hr.getName());

        // 收件人电话
        fixedX(540);
        printString(hr.getTelephone() == null ? "" : hr.getTelephone());
    }

}
