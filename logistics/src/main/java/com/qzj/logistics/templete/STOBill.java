package com.qzj.logistics.templete;

import android.content.Context;

import com.qzj.logistics.bean.AddrMoare;
import com.qzj.logistics.bean.HisReceiver;
import com.qzj.logistics.utils.print.PrintConnector;

/**
 * 申通快递单
 */
public class STOBill extends BaseBill {

    public STOBill() {}

    public STOBill(Context context, PrintConnector printConnector) {
        super(context, printConnector);
    }

    @Override
    protected void buildBill(AddrMoare am,HisReceiver hr) {
        // 顺向走纸200单位,走纸到打印位置
        skipPaper(200);

        // [[-->
        moveDown(140);
        moveRight(85);
        //打印寄件人姓名
        printString(am.getName() == null ? "" : am.getName());
        // <--]]

        // [[-->
        fixedX(300);
        // 打印始发地城市
        printString(am.getcName() == null ? "" : am.getcName());
        // <--]]

        // 收件人姓名
        fixedX(530);
        printString(hr.getName() == null ? "" : hr.getName());

        // 目的地
        fixedX(730);
        printString(hr.getC_name() == null ? "" : hr.getC_name());

        // 打印头移到最左边
        resetHeadToLeft();
        moveDown(80);

        // [[-->
        // 打印寄件人地址
        fixedX(60);
        printString(am.getpName());
        fixedX(180);
        printString(am.getcName());
        fixedX(300);
        printString(am.getrName());
        // <--]]

        // 收件人地址
        fixedX(510);
        printString(hr.getP_name() == null ? "" : hr.getP_name());
        fixedX(635);
        printString(hr.getC_name() == null ? "" : hr.getC_name());
        fixedX(750);
        printString(hr.getR_name() == null ? "" : hr.getR_name());

        // 寄件人详细地址
        resetHeadToLeft();
        moveDown(55);
        moveRight(30);
        printString(am.getAddr());

        // 收件人详细地址
        fixedX(480);
        printString(hr.getAddr()==null?"":hr.getAddr());

        // [[-->
        resetHeadToNextLine();
        moveDown(15);
        // 打印寄件人电话
        moveRight(150);
        printString(am.getTelephone());
        // <--]]

        // 收件人电话
        fixedX(600);
        printString(hr.getTelephone()==null?"":hr.getTelephone());
    }
}
