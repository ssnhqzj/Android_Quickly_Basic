package com.qzj.logistics.utils.wifi;

/**
 * Created by Administrator on 2016/1/8.
 */
public interface OnWifiReceiverListener {
    // 连接
    public void wifiConnected();
    // 断开连接
    public void wifiDisconnected();
    // 扫描结束
    public void wifiScanFinished();
}
