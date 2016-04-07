package com.qzj.logistics.utils.wifi;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiManager;
import android.util.Log;

/**
 * wifi广播接受者
 */
public class WifiReceiver extends BroadcastReceiver {

	private OnWifiReceiverListener wifiReceiverListener;

	@Override
	public void onReceive(Context context, Intent intent) {
		String action = intent.getAction();
		Log.e("qzj","-------------------action--:"+action);
		// 连接状态改变
		if(action.equals(ConnectivityManager.CONNECTIVITY_ACTION)){
			ConnectivityManager connManager = (ConnectivityManager)context.getSystemService(Context.CONNECTIVITY_SERVICE);
			NetworkInfo wifiInfo = connManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
			
			if(wifiInfo.getState().equals(NetworkInfo.State.CONNECTED)){
				if(wifiReceiverListener != null){
					wifiReceiverListener.wifiConnected();
				}
			}
			
			if(wifiInfo.getState().equals(NetworkInfo.State.DISCONNECTED)){
				if(wifiReceiverListener != null){
					wifiReceiverListener.wifiDisconnected();
				}
			}
		}
		// 扫描结束
		else if(action.equals(WifiManager.SCAN_RESULTS_AVAILABLE_ACTION)){
			if(wifiReceiverListener != null){
				wifiReceiverListener.wifiScanFinished();
			}
		}

	}
	
	public void setWifiReceiverListener(OnWifiReceiverListener wifiReceiverListener) {
		this.wifiReceiverListener = wifiReceiverListener;
	}
}
