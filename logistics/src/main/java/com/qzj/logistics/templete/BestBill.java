package com.qzj.logistics.templete;

import android.content.Context;

import com.qzj.logistics.bean.AddrMoare;
import com.qzj.logistics.bean.HisReceiver;
import com.qzj.logistics.utils.print.PrintConnector;

/**
 * 百世汇通快递单
 */
public class BestBill extends BaseBill {

    public BestBill() {}

    public BestBill(Context context, PrintConnector printConnector) {
        super(context, printConnector);
    }

    @Override
    protected void buildBill(AddrMoare am,HisReceiver hr) {
        // 顺向走纸200单位,走纸到打印位置
        skipPaper(200);

        // [[-->
        moveDown(135);
        // <--]]

        // 打印寄件人姓名
        moveRight(90);
        printString(am.getName() == null ? "" : am.getName());

        // 始发地
        fixedX(300);
        printString(am.getcName() == null ? "" : am.getcName());

        // 收件人姓名
        fixedX(530);
        printString(hr.getName() == null ? "" : hr.getName());

        // 目的地
        fixedX(760);
        printString(hr.getC_name() == null ? "" : hr.getC_name());

        // 寄件人地址
        resetHeadToLeft();
        moveDown(100);
        printAutoChangeLines(am.getFullAddr() == null ? "" : am.getFullAddr(),20,30);

        // 收件人地址
        printAutoChangeLines(hr.getFullAddr() == null ? "" : hr.getFullAddr(),20,480);

        // 寄件人电话
        resetHeadToLeft();
        moveDown(60);
        moveRight(100);
        printString(am.getTelephone() == null ? "" : am.getTelephone());

        // 收件人电话
        fixedX(550);
        printString(hr.getTelephone() == null ? "" : hr.getTelephone());

    }
}
