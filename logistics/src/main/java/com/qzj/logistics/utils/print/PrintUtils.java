package com.qzj.logistics.utils.print;

import android.util.Log;

import com.jolimark.printerlib.RemotePrinter;
import com.jolimark.printerlib.VAR;

/**
 * Created by Administrator on 2015/11/23.
 */
public class PrintUtils {

    private static final String TAG = "PrintUtils";

   static byte[] bData = {27, 119};

    public static void measurePaperWidth(){
        new Thread(){
            @Override
            public void run() {
                String tmpIP = "192.168.0.222";
                String tmpPort = "9100";
                // 第一步：创建RemotePrinter对象，传入参数，包含传输数据的方式、打印机的IP地址和端口号。
                RemotePrinter myPrinter = new RemotePrinter(VAR.TransType.TRANS_WIFI, tmpIP + ":" + tmpPort);
                // 第二步：调用 open()方法开启连接
                if (myPrinter.open()) {

                    // 第三步：调用sendData()方法发送数据
                    if (myPrinter.sendData(bData) == bData.length) {
                        Log.e(TAG, "send data success!");
                        byte[] recData = {};
                        myPrinter.recvData(bData);
                        Log.e(TAG, "recvData = " + bData);
                        if(bData != null && bData.length > 0){
                            for(int i=0; i < bData.length; i++){
                                Log.e(TAG, "recvData sub data = " + bData[i]);
                            }
                        }
                    } else {
                        Log.e(TAG, "send data failed!");
                    }
                } else {
                    Log.e(TAG, "printer connection error!");
                }

                myPrinter.close();
                myPrinter = null;
            }
        }.start();
    }

}
