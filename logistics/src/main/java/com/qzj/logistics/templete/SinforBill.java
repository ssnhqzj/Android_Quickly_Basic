package com.qzj.logistics.templete;

import android.content.Context;

import com.qzj.logistics.bean.AddrMoare;
import com.qzj.logistics.bean.HisReceiver;
import com.qzj.logistics.utils.print.PrintConnector;

/**
 * 信丰物流快递单
 */
public class SinforBill extends BaseBill {

    public SinforBill(){}

    public SinforBill(Context context, PrintConnector printConnector) {
        super(context, printConnector);
    }

    @Override
    protected void buildBill(AddrMoare am, HisReceiver hr) {
        // 顺向走纸200单位,走纸到打印位置
        skipPaper(200);

        // [[-->
        moveDown(100);
        moveRight(110);
        //打印寄件人姓名
        printString(am.getName() == null ? "" : am.getName());
        // <--]]

        // 寄件人电话号码
        fixedX(250);
        printString(am.getTelephone() == null ? "" : am.getTelephone());

        // 寄件人地址
        resetHeadToLeft();
        moveDown(120);
        printAutoChangeLines(am.getFullAddr() == null ? "" : am.getFullAddr(), 15, 80);

        // 收件人姓名
        resetHeadToLeft();
        moveDown(80);
        moveRight(110);
        printString(hr.getName() == null ? "" : hr.getName());

        // 收件人手机号码
        fixedX(250);
        printString(hr.getTelephone() == null ? "" : hr.getTelephone());

        // 收件人地址
        resetHeadToLeft();
        moveDown(120);
        printAutoChangeLines(hr.getFullAddr() == null ? "" : hr.getFullAddr(), 15, 80);

        // 始发站
        resetHeadToLeft();
        moveUp(230);
        fixedX(710);
        printString(am.getcName() == null ? "" : am.getcName());

        // 目的地
        resetHeadToLeft();
        moveDown(45);
        fixedX(710);
        printString(hr.getC_name() == null ? "" : hr.getC_name());
    }

}
