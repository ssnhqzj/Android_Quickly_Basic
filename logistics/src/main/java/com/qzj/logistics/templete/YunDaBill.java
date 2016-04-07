package com.qzj.logistics.templete;

import android.content.Context;

import com.qzj.logistics.bean.AddrMoare;
import com.qzj.logistics.bean.HisReceiver;
import com.qzj.logistics.utils.print.PrintConnector;

/**
 * 韵达快递单
 */
public class YunDaBill extends BaseBill {

    public YunDaBill(){}

    public YunDaBill(Context context, PrintConnector printConnector) {
        super(context, printConnector);
    }

    @Override
    protected void buildBill(AddrMoare am, HisReceiver hr) {
        // 顺向走纸200单位,走纸到打印位置
        skipPaper(200);

        // [[-->
        moveDown(130);
        moveRight(100);
        //打印寄件人姓名
        printString(am.getName() == null ? "" : am.getName());
        // <--]]

        // 始发城市
        fixedX(270);
        printString(am.getcName() == null ? "" : am.getcName());

        // 收件人姓名
        fixedX(500);
        printString(hr.getName() == null ? "" : hr.getName());

        // 目的城市
        fixedX(700);
        printString(hr.getC_name() == null ? "" : hr.getC_name());

        // 寄件人地址
        resetHeadToLeft();
        moveDown(70);
        printAutoChangeLines(am.getFullAddr() == null ? "" : am.getFullAddr(),15,100);

        // 收件人地址
        printAutoChangeLines(hr.getFullAddr() == null ? "" : hr.getFullAddr(),16,500);

        // 寄件人邮编
        resetHeadToLeft();
        moveDown(80);
        moveRight(100);
        printString(am.getMail_code() == null ? "" : am.getMail_code());

        // 寄件人联系电话
        fixedX(270);
        printString(am.getTelephone() == null ? "" : am.getTelephone());

        // 收件人邮编
        fixedX(500);
        printString(hr.getMail_code() == null ? "" : hr.getMail_code());

        // 收件人联系电话
        fixedX(700);
        printString(hr.getTelephone() == null ? "" : hr.getTelephone());
    }

}
