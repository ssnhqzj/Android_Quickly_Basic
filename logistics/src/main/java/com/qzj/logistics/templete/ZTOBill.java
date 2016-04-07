package com.qzj.logistics.templete;

import android.content.Context;

import com.qzj.logistics.bean.AddrMoare;
import com.qzj.logistics.bean.HisReceiver;
import com.qzj.logistics.utils.print.PrintConnector;

/**
 * 中通快递单
 */
public class ZTOBill extends BaseBill {

    public ZTOBill() {}

    public ZTOBill(Context context, PrintConnector printConnector) {
        super(context, printConnector);
    }

    @Override
    protected void buildBill(AddrMoare am,HisReceiver hr) {
        // 顺向走纸200单位,走纸到打印位置
        skipPaper(200);

        // [[-->
        moveDown(175);
        moveRight(80);
        //打印寄件人姓名
        printString(am.getName() == null ? "" : am.getName());
        // <--]]

        // 始发地
        fixedX(270);
        printString(am.getcName() == null ? "" : am.getcName());

        // 收件人姓名
        fixedX(525);
        printString(hr.getName() == null ? "" : hr.getName());

        // 目的地
        fixedX(700);
        printString(hr.getC_name() == null ? "" : hr.getC_name());

        // 寄件人地址
        resetHeadToLeft();
        moveDown(40);
        printAutoChangeLines(am.getFullAddr() == null ? "" : am.getFullAddr(), 18, 80);

        // 收件人地址
        printAutoChangeLines(hr.getFullAddr() == null ? "" : hr.getFullAddr(), 18, 525);

        // 寄件人电话
        resetHeadToLeft();
        moveDown(120);
        fixedX(80);
        printString(am.getTelephone() == null ? "" : am.getTelephone());

        // 收件人电话
        fixedX(525);
        printString(hr.getTelephone() == null ? "" : hr.getTelephone());
    }
}
