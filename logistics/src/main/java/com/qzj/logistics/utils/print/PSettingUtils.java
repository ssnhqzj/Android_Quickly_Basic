package com.qzj.logistics.utils.print;

/**
 * 打印位置工具类
 */
public class PSettingUtils {

    private static final String TAG = "PPositionUtils";
    public static final int UP = 1;
    public static final int DOWN = 2;
    public static final int LEFT = 3;
    public static final int RIGHT = 4;

    public static final int LINE_SPACE_180 = 5;
    public static final int LINE_SPACE_360 = 6;

    /**
     * 回车
     */
    public static final byte ENTER = 13;

    /**
     * 换行
     */
    public static final byte NEXT_LINE = 10;

    /**
     * 反向换行
     */
    public static final byte END_NEXT_LINE = 14;

    /**
     * 换页
     */
    public static final byte[] NEXT_PAGE = { 12 };

    /**
     * 定义单位
     * @param m 有效值为：10，20，30，40，50，60
     * @return
     */
    public static byte[] setUnit(byte m){
        final byte result[] = { 27, 40, 85, 1, 0, m };

        return result;
    }

    /**
     * 水平绝对定位位置
     * 0 ≤ n1 ≤ 255
     * 0 ≤ n2 ≤ 3
     * 用户所能规定的的最大点位置则为816。若指令使打印头移出右边界，则此指令被忽略
     * @param n 水平移动的点位
     * @return
     */
    public static byte[] horizontalPosition(int n){
        byte n1 = (byte) (n % 256);
        byte n2 = (byte) (n / 256);
        final byte result[] = { 27, 36, n1, n2 };

        return result;
    }

    /**
     * 垂直绝对定位位置
     * @param n 垂直移动的点位
     * @return
     */
    public static byte[] verticalPosition(int n){
        byte n1 = (byte) (n % 256);
        byte n2 = (byte) (n / 256);
        final byte result[] = { 27, 40, 86, 2, 0, n1, n2 };

        return result;
    }

    /**
     * 水平和垂直绝对定位位置
     * @param x 水平移动的点位
     * @param y 垂直移动的点位
     * @return
     */
    public static byte[] horizontalVerticalPosition(int x, int y){
        byte x_n1 = (byte) (x % 256);
        byte x_n2 = (byte) (x / 256);
        final byte x_result[] = { 27, 36, x_n1, x_n2 };

        byte y_n1 = (byte) (y % 256);
        byte y_n2 = (byte) (y / 256);
        final byte y_result[] = { 27, 40, 86, 2, 0, y_n1, y_n2 };

        final byte[] result = ArrayUtils.twoToOne(x_result,y_result);

        return result;
    }

    /**
     * 水平方向上的相对位置移动
     * 只能相对右移，不能相对左移
     * @param n 相对移动点位
     * @return
     */
    public static byte[] horizontalRelativePosition(int direction,int n){
        int n1 = 0;
        int n2 = 0;
        if(direction == RIGHT){
            n1 = (byte) (n % 256);
            n2 = (byte) (n / 256);
        }else if(direction == LEFT){
            n2 = (65536 - n) / 256;
            n1 = (65536 - n) - n2 * 256;
        }
        final byte result[] = { 27, 92, (byte) n1, (byte) n2};

        return result;
    }

    /**
     * 垂直方向上的相对位置移动
     * @param direction 移动方向
     * @param n 相对移动点位
     * @return
     */
    public static byte[] verticalRelativePosition(int direction, int n){
        int n1 = 0;
        int n2 = 0;
        if(direction == DOWN){
            n1 = (byte) (n % 256);
            n2 = (byte) (n / 256);
        }else if(direction == UP){
            n2 = (65536 - n) / 256;
            n1 = (65536 - n) - n2 * 256;
        }
        final byte result[] = { 27, 40, 118, 2, 0, (byte) n1, (byte) n2};

        return result;
    }

    /**
     * 根据当前的字符宽度，把左边界设定为n列
     * 设定比例间距时，以10CPI 宽度处理。
     * 英文方式，以英文字符宽度设定；当使用ESC S 设定英文字符的间距，注意把加入的间距值算入
     * 字符宽度。汉字方式，则以汉字宽度设定。
     * @param n
     * @return
     */
    public static byte[] leftMargin(int n){
        final byte result[] = { 27, 108, (byte) n};
        return result;
    }

    /**
     * 根据当前的字符宽度，把右边界设定为n列
     * 设定比例间距时，以10CPI 宽度处理。
     * 英文方式，以英文字符宽度设定；当使用ESC S 设定英文字符的间距，注意把加入的间距值算入
     * 字符宽度。汉字方式，则以汉字宽度设定。
     * @param n
     * @return
     */
    public static byte[] rightMargin(int n){
        final byte result[] = { 27, 81, (byte) n};
        return result;
    }

    /**
     * 页顶偏移
     * 按1/180inch 单位设置打印机顶部的一个偏移，为固定不可打印区域。
     * 该偏移量不算入页顶空白。
     * 页顶偏移量：n / 180 inch , 0 ≤ n ≤ 127
     * @param n
     * @return
     */
    public static byte[] topMargin(int n){
        final byte result[] = { 27, 65, (byte) n};
        return result;
    }

    /**
     * 按定义的单位设定页长
     * 0 < n < 22 英寸
     * @param n
     * @return
     */
    public static byte[] setPageHeight(int n){
        byte n1 = (byte) (n % 256);
        byte n2 = (byte) (n / 256);
        final byte result[] = { 27, 40, 67, 2, 0, n1, n2 };

        return result;
    }

    /**
     * 设置页顶/页低空白
     * 顶部空白量<底部空白量，max(顶部空白量) = 22 英寸
     * @param top
     * @param bottom
     * @return
     */
    public static byte[] setTopAndBottomSpace(int top, int bottom){
        byte t1 = (byte) (top % 256);
        byte t2 = (byte) (top / 256);
        byte b1 = (byte) (bottom % 256);
        byte b2 = (byte) (bottom / 256);

        final byte result[] = { 27, 40, 99, 4, 0, t1, t2, b1, b2 };

        return result;
    }

    /**
     * 顺向走纸n个单位
     * @param n 0 < n < 255
     * @return
     */
    public static byte[] paperSkip(int n){
        final byte result[] = { 27, 74, (byte) n};

        return result;
    }

    /**
     * 设定行间距
     * @param type 分n/180,n/360行间距
     * @param n
     * @return
     */
    public static byte[] lineSpacing(int type,int n){
        if(type == LINE_SPACE_180){
            final byte[] result_180 = { 27, 51, (byte) n};
            return result_180;
        }else if(type == LINE_SPACE_360){
            final byte[] result_360 = { 27, 43, (byte) n};
            return result_360;
        }

        return null;
    }

    /**
     * n = 0：选择单页纸模式
     *    1：选择连续纸模式
     *   2：选择厚纸模式
     * @param n
     * @return
     */
    public static byte[] changePaperType(int n){
        final byte result[] = { 27, 88, (byte) n};

        return result;
    }

    /**
     * 设置的页面的长度为n 英寸
     * @param n 1 ≤ n ≤ 22
     * @return
     */
    public static byte[] setPaperSizeHeightInch(int n){
        final byte result[] = { 27, 67, 00, (byte) n};

        return result;
    }
}
