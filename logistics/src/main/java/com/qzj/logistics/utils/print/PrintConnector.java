package com.qzj.logistics.utils.print;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.widget.Toast;

import com.jolimark.printerlib.RemotePrinter;
import com.jolimark.printerlib.VAR;
import com.qzj.logistics.view.deliver.DeliverGoodsFragment;

/**
 * 打印机连接器
 */
public class PrintConnector {

    // 打印机连接成功
    public static final int PRINT_CONN_SUCCESS = 8;

    //  打印机连接成功
    protected static final int STATE_CONNECT_OK = 0;
    // 打印机连接失败
    protected static final int STATE_CONNECT_ER = 1;
    // 打印完成
    protected static final int STATE_OK = 2;
    // 通讯失败
    protected static final int STATE_COMM_ER = 3;

    /**
     * 打印机IP
     */
    private static String IP = null;
    /**
     * 打印机端口
     */
    private static String PORT = null;

    /**
     * 打印机类型
     */
    static VAR.PrinterType printerType =null;
    private RemotePrinter wifiPrinter;

    private Context context;
    private Handler noticeHandler;
    private Handler dialogHandler;

    public PrintConnector(Context context){
        this(context, null, null);
    }

    public PrintConnector(Context context,String ip, String port){
        this.context = context;
        if (ip != null) this.IP = ip;
        if (port != null) this.PORT = port;
    }

    public void connect() {
        if (dialogHandler != null)
            dialogHandler.obtainMessage(DeliverGoodsFragment.DIALOG_SHOW,"正在连接打印机...").sendToTarget();
        new Thread() {
            public void run() {
                // 第一步：创建RemotePrinter对象，传入参数，包含传输数据的方式、打印机的IP地址和端口号。
                wifiPrinter = new RemotePrinter(VAR.TransType.TRANS_WIFI, IP + ":" + PORT);
                // 第二步：调用 open()方法开启连接
                try {
                    if (wifiPrinter.open()) {
                        printerType = wifiPrinter.getPrinterType();
                        mHandler.obtainMessage(STATE_CONNECT_OK, this).sendToTarget();
                    } else {
                        mHandler.obtainMessage(STATE_CONNECT_ER, this).sendToTarget();
//                        ((Activity)context).startActivityForResult(new Intent(android.provider.Settings.ACTION_WIFI_SETTINGS), 0);
                    }
                    wifiPrinter.close();
                    wifiPrinter = null;
                }catch (Exception e){
                    e.printStackTrace();
                    mHandler.obtainMessage(STATE_CONNECT_ER, this).sendToTarget();
                }
            }
        }.start();
    }

    /**
     * 打印数据
     * @param buff
     */
    public void print(byte[] buff) {
        if (dialogHandler != null)
            dialogHandler.obtainMessage(DeliverGoodsFragment.DIALOG_SHOW,"打印机正在执行...").sendToTarget();
        final byte[] bData = buff;
        new Thread() {
            public void run() {
                // 第一步：创建RemotePrinter对象，传入参数，包含传输数据的方式、打印机的IP地址和端口号。
                wifiPrinter = new RemotePrinter(VAR.TransType.TRANS_WIFI, IP + ":" + PORT);
                // 第二步：调用 open()方法开启连接
                try {
                    if (wifiPrinter.open()) {
                        // 第三步：调用sendData()方法发送数据
                        if (wifiPrinter.sendData(bData) == bData.length) {
                            mHandler.obtainMessage(STATE_OK).sendToTarget();
                        } else {
                            mHandler.obtainMessage(STATE_COMM_ER).sendToTarget();
                        }
                    } else {
                        mHandler.obtainMessage(STATE_CONNECT_ER).sendToTarget();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    if(wifiPrinter != null){
                        wifiPrinter.close();
                        wifiPrinter = null;
                    }
                }

                if(wifiPrinter != null){
                    wifiPrinter.close();
                    wifiPrinter = null;
                }

                // 暂停一段时间后关闭wifi
                try {
                    sleep(5000);
                    dialogHandler.obtainMessage(DeliverGoodsFragment.RESET_WIFI).sendToTarget();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }.start();
    }

    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            if (dialogHandler != null)
                dialogHandler.obtainMessage(DeliverGoodsFragment.DIALOG_HIDEEN).sendToTarget();
            switch (msg.what) {
                case STATE_CONNECT_OK:
                    Toast.makeText(context, "打印机连接成功", Toast.LENGTH_SHORT).show();
                    if (noticeHandler != null)
                        noticeHandler.obtainMessage(PRINT_CONN_SUCCESS).sendToTarget();
                    break;
                case STATE_CONNECT_ER:
                    Toast.makeText(context, "打印机连接失败", Toast.LENGTH_SHORT).show();
                    if (dialogHandler != null)
                        dialogHandler.obtainMessage(DeliverGoodsFragment.RESET_WIFI).sendToTarget();
                    break;
                case STATE_OK:
                    break;
                case STATE_COMM_ER:
                    Toast.makeText(context, "通讯失败", Toast.LENGTH_SHORT).show();
                    if (dialogHandler != null)
                        dialogHandler.obtainMessage(DeliverGoodsFragment.RESET_WIFI).sendToTarget();
                    break;
                default:
                    Toast.makeText(context, "其它错误", Toast.LENGTH_SHORT).show();
                    if (dialogHandler != null)
                        dialogHandler.obtainMessage(DeliverGoodsFragment.RESET_WIFI).sendToTarget();
                    break;
            }
        }
    };

    public void setNoticeHandler(Handler handler){
        noticeHandler = handler;
    }

    public void setDialogHandler(Handler dialogHandler) {
        this.dialogHandler = dialogHandler;
    }
}
