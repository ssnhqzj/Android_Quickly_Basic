package com.qzj.logistics.templete;

import android.content.Context;

import com.qzj.logistics.bean.AddrMoare;
import com.qzj.logistics.bean.HisReceiver;
import com.qzj.logistics.utils.print.PrintConnector;

/**
 * 全峰快递单
 */
public class QFBill extends BaseBill {

    public QFBill() {}

    public QFBill(Context context, PrintConnector printConnector) {
        super(context, printConnector);
    }

    @Override
    protected void buildBill(AddrMoare am,HisReceiver hr) {
        // 顺向走纸200单位,走纸到打印位置
        skipPaper(200);

        // [[-->
        moveDown(165);
        // <--]]

        //打印寄件人姓名
        moveRight(70);
        printString(am.getName() == null ? "" : am.getName());

        // 始发城市
        fixedX(310);
        printString(am.getcName() == null ? "" : am.getcName());

        // 收件人姓名
        fixedX(520);
        printString(hr.getName() == null ? "" : hr.getName());

        // 目的城市
        fixedX(750);
        printString(hr.getC_name() == null ? "" : hr.getC_name());

        // 寄件人电话
        resetHeadToLeft();
        moveDown(30);
        fixedX(310);
        printString(am.getTelephone() == null ? "" : am.getTelephone());

        // 收件人电话
        fixedX(750);
        printString(hr.getTelephone() == null ? "" : hr.getTelephone());

        // 寄件地址
        resetHeadToLeft();
        moveDown(30);
        printAutoChangeLines(am.getFullAddr() == null ? "" : am.getFullAddr(), 19, 55);

        // 收件地址
        printAutoChangeLines(hr.getFullAddr() == null ? "" : hr.getFullAddr(),19,500);

        // 寄件人邮编
        resetHeadToLeft();
        moveDown(30);
        fixedX(310);
        printString(am.getMail_code() == null ? "" : am.getMail_code());

        // 收件人邮编
        fixedX(750);
        printString(hr.getMail_code() == null ? "" : hr.getMail_code());
    }
}
