package com.qzj.logistics.templete;

import android.content.Context;

import com.qzj.logistics.bean.AddrMoare;
import com.qzj.logistics.bean.HisReceiver;
import com.qzj.logistics.utils.print.PrintConnector;

/**
 * 圆通快递单
 */
public class YTBill extends BaseBill {

    public YTBill() {}

    public YTBill(Context context, PrintConnector printConnector) {
        super(context, printConnector);
    }

    @Override
    protected void buildBill(AddrMoare am,HisReceiver hr) {
        // 顺向走纸200单位,走纸到打印位置
        skipPaper(200);

        // [[-->
        moveDown(130);
        moveRight(55);
        //打印寄件人姓名
        printString(am.getName() == null ? "" : am.getName());
        // <--]]

        // 始发地
        fixedX(220);
        printString(am.getcName() == null ? "" : am.getcName());

        // 收件人姓名
        fixedX(500);
        printString(hr.getName() == null ? "" : hr.getName());

        // 寄件人地址
        resetHeadToLeft();
        moveDown(80);
        printAutoChangeLines(am.getFullAddr() == null ? "" : am.getFullAddr(),18,5);

        // 收件人地址
        fixedX(450);
        printString(hr.getFullAddr() == null ? "" : hr.getFullAddr());

        // 寄件人电话
        resetHeadToLeft();
        moveDown(60);
        moveRight(90);
        printString(am.getTelephone() == null ? "" : am.getTelephone());

        // 寄件人邮编
        fixedX(350);
        printString(am.getMail_code() == null ? "" : am.getMail_code());

        // 收件人电话
        fixedX(550);
        printString(hr.getTelephone() == null ? "" : hr.getTelephone());

        // 收件人邮编
        fixedX(800);
        printString(hr.getMail_code() == null ? "" : hr.getMail_code());

    }
}
