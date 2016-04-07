package com.qzj.logistics.templete;

import android.content.Context;

import com.qzj.logistics.bean.AddrMoare;
import com.qzj.logistics.bean.HisReceiver;
import com.qzj.logistics.utils.print.PrintConnector;

/**
 * 快捷快递单
 */
public class FastBill extends BaseBill {

    public FastBill() {}

    public FastBill(Context context, PrintConnector printConnector) {
        super(context, printConnector);
    }

    @Override
    protected void buildBill(AddrMoare am,HisReceiver hr) {
        // 顺向走纸200单位,走纸到打印位置
        skipPaper(200);

        // [[-->
        moveDown(230);
        // <--]]

        // 寄件人地址
        printAutoChangeLines(am.getFullAddr() == null ? "" : am.getFullAddr(), 14, 55);

        // 收件人地址
        printAutoChangeLines(hr.getFullAddr() == null ? "" : hr.getFullAddr(), 14, 350);

        // 寄件人
        resetHeadToLeft();
        moveDown(75);
        moveRight(35);
        printString(am.getName() == null ? "" : am.getName());

        // 寄件人电话
        fixedX(190);
        printString(am.getTelephone() == null ? "" : am.getTelephone());

        // 收件人
        fixedX(335);
        printString(hr.getName() == null ? "" : hr.getName());

        // 收件人电话
        fixedX(490);
        printString(hr.getTelephone() == null ? "" : hr.getTelephone());
    }
}
