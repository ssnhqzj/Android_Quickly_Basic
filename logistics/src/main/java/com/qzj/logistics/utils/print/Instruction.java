package com.qzj.logistics.utils.print;

/**
 * 打印机指令
 */
public class Instruction {
    // -------------ESPON打印机指令--------------------
    // 回车换行
    public static final byte a12[] = { (byte) 0x0D, (byte) 0x0A };

    // 汉字打印命令
    public static final byte a14[] = { (byte) 0x1C, (byte) 0x26 };
    // 取消汉字打印命令
    public static final byte a15[] = { (byte) 0x1c, (byte) 0x2e };
    // 打印机初始化
    public static final byte a17[] = { (byte) 0x1B, (byte) 0x40 };
    // 斜体
    public static final byte a18[] = { (byte) 0x1B, (byte) 0x34 };
    // 解除斜体
    public static final byte a19[] = { (byte) 0x1B, (byte) 0x35 };
    // 粗体
    public static final byte a20[] = { (byte) 0x1B, (byte) 0x45 };
    // 解除粗体
    public static final byte a21[] = { (byte) 0x1B, (byte) 0x46 };
    // 重叠打印
    final byte a22[] = { (byte) 0x1B, (byte) 0x47 };
    // 解除重叠打印
    public static final byte a23[] = { (byte) 0x1B, (byte) 0x48 };
    // 下划线 一条实线
    public static final byte a24[] = { (byte) 0x1B, (byte) 0x28, (byte) 0x2D, (byte) 0x3,
            (byte) 0x0, (byte) 0x1, (byte) 0x1, (byte) 0x1 };
    // 下划线 一条虚线
    public static final byte a25[] = { (byte) 0x1B, (byte) 0x28, (byte) 0x2D, (byte) 0x3,
            (byte) 0x0, (byte) 0x1, (byte) 0x1, (byte) 0x5 };
    // 解除下划线
    public static final byte a26[] = { (byte) 0x1B, (byte) 0x28, (byte) 0x2D, (byte) 0x3,
            (byte) 0x0, (byte) 0x1, (byte) 0x1, (byte) 0x0 };
    // 倍宽打印
    public static final byte a27[] = { (byte) 0x1B, (byte) 0x57, (byte) 0x1 };
    // 解除倍宽打印
    public static final byte a28[] = { (byte) 0x1B, (byte) 0x57, (byte) 0x0 };
    // 倍高倍宽打印
    public static final byte a29[] = { (byte) 0x1C, (byte) 0x57, (byte) 0x1 };
    // 解除倍高倍宽打印
    public static final byte a30[] = { (byte) 0x1C, (byte) 0x57, (byte) 0x0 };
    // 倍高打印
    public static final byte a31[] = { (byte) 0x1C, (byte) 0x21, (byte) 0x8 };
    // 解除倍高打印
    public static final byte a32[] = { (byte) 0x1C, (byte) 0x21, (byte) 0x0 };
    //推出
    public static final byte a33[] = { (byte) 0xC};
    // 间距 {28,118,1}
    public static final byte a35[] = { (byte) 0x1C, (byte) 0x76,(byte) 0x1};
    // 解除间距
    public static final byte a36[] = { (byte) 0x1C, (byte) 0x76,(byte) 0x0};

    // -------------爱普生pos指令--------------------
    //中文倍宽
    public static final byte b1[] = { (byte) 0x1C, (byte) 0x21,(byte) 0x4};
    //中文倍高
    public static final byte b2[] = { (byte) 0x1C, (byte) 0x21,(byte) 0x8};
    //中文倍宽、倍高字体
    public static final byte b3[] = { (byte) 0x1C, (byte) 0x21,(byte) 0xC};
    //关闭中文倍宽倍高
    public static final byte b11[] = { (byte) 0x1C, (byte) 0x21,(byte) 0x0};

    //英文倍宽
    public static final byte b4[] = { (byte) 0x1B, (byte) 0x21,(byte) 0x20};
    //英文倍高
    public static final byte b5[] = { (byte) 0x1B, (byte) 0x21,(byte) 0x10};
    //英文倍宽、倍高字体
    public static final byte b6[] = { (byte) 0x1B, (byte) 0x21,(byte) 0x30};
    //关闭英文倍宽倍高
    public static final byte b12[] = { (byte) 0x1B, (byte) 0x21,(byte) 0x0};
}
