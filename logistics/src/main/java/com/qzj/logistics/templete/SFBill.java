package com.qzj.logistics.templete;

import android.content.Context;

import com.qzj.logistics.bean.AddrMoare;
import com.qzj.logistics.bean.HisReceiver;
import com.qzj.logistics.utils.print.PrintConnector;

/**
 * 顺丰快递单
 */
public class SFBill extends BaseBill {

    public SFBill() {}

    public SFBill(Context context, PrintConnector printConnector) {
        super(context, printConnector);
    }

    @Override
    protected void buildBill(AddrMoare am,HisReceiver hr) {
        // 顺向走纸200单位,走纸到打印位置
        skipPaper(200);

        // [[-->
        moveDown(165);
        fixedX(330);
        //打印寄件人姓名
        printString(am.getName() == null ? "" : am.getName());
        // <--]]

        // 寄件人地址
        resetHeadToLeft();
        moveDown(35);
        moveRight(55);
        printString(am.getFullAddr() == null ? "" : am.getFullAddr());

        // 寄件人电话
        resetHeadToLeft();
        moveDown(35);
        moveRight(70);
        printString(am.getTelephone() == null ? "" : am.getTelephone());

        // 收件人姓名
        resetHeadToLeft();
        moveDown(35);
        fixedX(330);
        printString(hr.getName() == null ? "" : hr.getName());

        // 收件人地址
        resetHeadToLeft();
        moveDown(35);
        fixedX(10);
        printString(hr.getP_name() == null ? "" : hr.getP_name());
        fixedX(100);
        printString(hr.getC_name() == null ? "" : hr.getC_name());
        fixedX(210);
        printString(hr.getR_name() == null ? "" : hr.getR_name());

        resetHeadToLeft();
        moveDown(35);
        fixedX(10);
        printString(hr.getAddr() == null ? "" : hr.getAddr());

        // 收件人联系电话
        resetHeadToLeft();
        moveDown(35);
        moveRight(70);
        printString(hr.getTelephone() == null ? "" : hr.getTelephone());
    }
}
