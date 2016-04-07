package com.qzj.logistics.templete;

import android.content.Context;
import android.util.Log;

import com.qzj.logistics.bean.AddrMoare;
import com.qzj.logistics.bean.HisReceiver;
import com.qzj.logistics.bean.RecordAndReceiver;
import com.qzj.logistics.utils.print.ArrayUtils;
import com.qzj.logistics.utils.print.Instruction;
import com.qzj.logistics.utils.print.PSettingUtils;
import com.qzj.logistics.utils.print.PrintConnector;

import java.io.UnsupportedEncodingException;
import java.util.List;

/**
 * 基础打印模板
 */
public abstract class BaseBill {

    // 自动换行行高
    private static final int LINE_HEIGHT = 25;
    protected Context context;
    protected PrintConnector printConnector;

    protected byte billOne[] = null;

    public BaseBill(){}

    public BaseBill(Context context, PrintConnector printConnector){
        init(context,printConnector);
    }

    public BaseBill init(Context context, PrintConnector printConnector){
        this.context = context;
        this.printConnector = printConnector;

        return this;
    }

    /**
     * 打印快递单
     * @param rar
     */
    public void printBill(RecordAndReceiver rar){
        // 发件人信息
        AddrMoare am = rar.getAm();
        // 收件人列表
        List<HisReceiver> receivers = rar.getHisReceivers();

        // 打印中文，需要发送打印中文指令
        billOne = ArrayUtils.twoToOne(billOne, Instruction.a14);

        // 设置页面高度为5英寸
        billOne = ArrayUtils.twoToOne(billOne, PSettingUtils.setPaperSizeHeightInch(5));

        // 定义单位
        billOne = ArrayUtils.twoToOne(billOne, PSettingUtils.setUnit((byte) 30));

        // 左边间距
        billOne = ArrayUtils.twoToOne(billOne, PSettingUtils.leftMargin(1));

        // 构建打印单
        for (HisReceiver hr : receivers) {
            // 打印机初始化
//		    billOne = ArrayUtils.twoToOne(billOne, Instruction.a17);

            // 设置切换纸张类型
//		    billOne = ArrayUtils.twoToOne(billOne, PSettingUtils.changePaperType(1));

            // 调用子类构建的打印单
            buildBill(am,hr);

            // 换页
            billOne = ArrayUtils.twoToOne(billOne, PSettingUtils.NEXT_PAGE);

            //初始化
//		    billOne = ArrayUtils.twoToOne(billOne, Instruction.a17);
        }

        // 取消中文打印命令
        billOne = ArrayUtils.twoToOne(billOne, Instruction.a15);

        if (billOne != null) {
            // 发送打印指令给打印机。
            if (printConnector != null) printConnector.print(billOne);
        }
    }

    /**
     * 构建快递单
     * @param am 发件人
     * @param hr 收件人
     */
    protected abstract void buildBill(AddrMoare am,HisReceiver hr);

    /**
     * 字符串转换成GB2312编码的byte数组
     * @param str
     * @return
     */
    public byte[] stringToByte(String str){
        byte[] s2byte = null;
        try {
            s2byte = str.getBytes("GB2312");
        } catch (UnsupportedEncodingException e2) {
            // TODO Auto-generated catch block
            e2.printStackTrace();
        }
        return s2byte;
    }

    /**
     * 自动换行
     * @param text 打印的文本
     * @param length 每行打印的字数
     * @param x 横向偏移
     */
    protected void printAutoChangeLines(String text, int length, int x) {
        // 总字数
        int totalLength = text.length();
        // 行数
        int lines = totalLength%length==0?totalLength/length:totalLength/length+1;
        String lineStr;
        for (int i=0; i<lines; i++) {
            fixedX(x);
            if (i > 0) {
                moveDown(LINE_HEIGHT);
            }

            int end = (i+1)*length;
            if (end >= totalLength) {
                end = totalLength;
            }
            lineStr = text.substring(i*length,end);
            Log.e("qzj","第"+i+"行:"+lineStr);
            printString(lineStr);
        }

        moveUp(LINE_HEIGHT*(lines-1));
    }

    /**
     * 横向绝对定位
     * @param x
     */
    protected void fixedX(int x) {
        billOne = ArrayUtils.twoToOne(billOne, PSettingUtils.horizontalPosition(x));
    }

    /**
     * 垂直绝对定位
     * @param y
     */
    protected void fixedY(int y) {
        billOne = ArrayUtils.twoToOne(billOne, PSettingUtils.verticalPosition(y));
    }

    /**
     * 绝对定位
     * @param x
     * @param y
     */
    protected void fixedPosition(int x, int y){
        billOne = ArrayUtils.twoToOne(billOne, PSettingUtils.horizontalVerticalPosition(x,y));
    }

    /**
     * 左移value点位
     * @param value
     */
    protected void moveLeft(int value) {
        billOne = ArrayUtils.twoToOne(billOne, PSettingUtils.horizontalRelativePosition(PSettingUtils.LEFT, value));
    }

    /**
     * 右移value点位
     * @param value
     */
    protected void moveRight(int value) {
        billOne = ArrayUtils.twoToOne(billOne, PSettingUtils.horizontalRelativePosition(PSettingUtils.RIGHT, value));
    }

    /**
     * 上移value点位
     * @param value
     */
    protected void moveUp(int value) {
        billOne = ArrayUtils.twoToOne(billOne, PSettingUtils.verticalRelativePosition(PSettingUtils.UP, value));
    }

    /**
     * 下移value点位
     * @param value
     */
    protected void moveDown(int value) {
        billOne = ArrayUtils.twoToOne(billOne, PSettingUtils.verticalRelativePosition(PSettingUtils.DOWN, value));
    }

    /**
     * 重置打印头到左边
     */
    protected void resetHeadToLeft(){
        billOne = ArrayUtils.twoToOne(billOne, PSettingUtils.ENTER);
    }

    /**
     * 重置打印头到下一行左边
     */
    protected void resetHeadToNextLine(){
        billOne = ArrayUtils.twoToOne(billOne, Instruction.a12);
    }

    /**
     * 顺向走纸value点位
     * 0 < value < 255
     * @param value
     */
    protected void skipPaper(int value){
        billOne = ArrayUtils.twoToOne(billOne, PSettingUtils.paperSkip(value));
    }

    /**
     * 打印字符串
     * @param str
     */
    protected void printString(String str){
        billOne = ArrayUtils.twoToOne(billOne, stringToByte(str));
    }



}
