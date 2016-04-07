package com.qzj.logistics.utils.wifi;

import android.content.Context;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiConfiguration;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Handler;
import android.util.Log;
import android.widget.Toast;

import com.qzj.logistics.view.deliver.DeliverGoodsFragment;

import java.util.List;

public class WifiController implements OnWifiReceiverListener {

	// 无密码方式连接
	public static final int CONNECT_NONE = 1;
	// wep方式连接
	public static final int CONNECT_WEP = 2;
	// wpa方式连接
	public static final int CONNECT_WPA = 3;
	// 扫描完成
	public static final int SCAN_FINISHED = 4;
	// wifi连接成功
	public static final int CONN_SUCCESS = 5;

	private Context context;
	private WifiReceiver wifiReceiver;

	private List<ScanResult> mWifiList;
	private WifiManager mWifiManager;
	private ConnectivityManager connManager;
	private WifiInfo mWifiInfo;
	private WifiInfo oldWifiInfo;
	private Handler handler;
	private boolean isScan = false;
	private boolean isConnected = false;
	private Handler dialogHandler;

	public WifiController(Context context, Handler handler) {
		this.context = context;
		this.handler = handler;
		mWifiManager=(android.net.wifi.WifiManager) context.getSystemService(Context.WIFI_SERVICE);
		connManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
		wifiReceiver = new WifiReceiver();
		wifiReceiver.setWifiReceiverListener(this);
	}

	/**
	 * 创建一个wifi连接配置
	 * @param SSID
	 * @param Password
	 * @param Type
	 * @return
	 */
	public WifiConfiguration createWifiInfo(String SSID, String Password, int Type) {
		WifiConfiguration config = new WifiConfiguration();
		config.allowedAuthAlgorithms.clear();
		config.allowedGroupCiphers.clear();
		config.allowedKeyManagement.clear();
		config.allowedPairwiseCiphers.clear();
		config.allowedProtocols.clear();
		config.SSID = "\"" + SSID + "\"";

		WifiConfiguration tempConfig = this.isExist(SSID);

		if (tempConfig != null) {
			mWifiManager.removeNetwork(tempConfig.networkId);
		} else {
		}

		// 无密码
		if (Type == CONNECT_NONE) {
			config.allowedKeyManagement.set(WifiConfiguration.KeyMgmt.NONE);
		}

		// wep
		if (Type == CONNECT_WEP) {
			config.hiddenSSID = true;
			config.wepKeys[0] = "\"" + Password + "\"";
			config.allowedAuthAlgorithms.set(WifiConfiguration.AuthAlgorithm.OPEN);
			config.allowedAuthAlgorithms.set(WifiConfiguration.AuthAlgorithm.SHARED);
			config.allowedGroupCiphers.set(WifiConfiguration.GroupCipher.CCMP);
			config.allowedGroupCiphers.set(WifiConfiguration.GroupCipher.TKIP);
			config.allowedGroupCiphers.set(WifiConfiguration.GroupCipher.WEP40);
			config.allowedGroupCiphers.set(WifiConfiguration.GroupCipher.WEP104);
			config.allowedKeyManagement.set(WifiConfiguration.KeyMgmt.NONE);
			config.wepTxKeyIndex = 0;
		}

		// wpa
		if (Type == CONNECT_WPA) {
			config.preSharedKey = "\"" + Password + "\"";
			config.hiddenSSID = true;
			config.allowedAuthAlgorithms.set(WifiConfiguration.AuthAlgorithm.OPEN);
			config.allowedGroupCiphers.set(WifiConfiguration.GroupCipher.TKIP);
			config.allowedKeyManagement.set(WifiConfiguration.KeyMgmt.WPA_PSK);
			config.allowedPairwiseCiphers.set(WifiConfiguration.PairwiseCipher.TKIP);
			config.allowedGroupCiphers.set(WifiConfiguration.GroupCipher.CCMP);
			config.allowedPairwiseCiphers.set(WifiConfiguration.PairwiseCipher.CCMP);
			config.status = WifiConfiguration.Status.ENABLED;
		}

		return config;
	}

	/**
	 * 检测指定wifi是否存在
	 * @param SSID
	 * @return
	 */
	public WifiConfiguration isExist(String SSID) {
		List<WifiConfiguration> existingConfigs = mWifiManager.getConfiguredNetworks();
		if (existingConfigs != null) {
			for (WifiConfiguration existingConfig : existingConfigs) {
				if (existingConfig.SSID.equals("\"" + SSID + "\"") || existingConfig.SSID.equals(SSID)) {
					return existingConfig;
				}
			}
		}

		return null;
	}

	/**
	 * 断开wifi
	 */
	public void disConnectWifi() {
		mWifiInfo = mWifiManager.getConnectionInfo();
		mWifiManager.disableNetwork(mWifiInfo.getNetworkId());
		mWifiManager.disconnect();
	}

	/**
	 * 断开指定wifi
	 */
	public void disConnectWifi(String SSID) {
		mWifiInfo = mWifiManager.getConnectionInfo();
		if (mWifiInfo != null && mWifiInfo.getSSID().equals(SSID)){
			mWifiManager.disableNetwork(mWifiInfo.getNetworkId());
			mWifiManager.disconnect();
		}
	}

	/**
	 * 扫描wifi
	 * @return
	 */
	public boolean scanWifi(Handler handler) {
		isScan = true;
		return mWifiManager.startScan();
	}

	/**
	 * 获取扫描后的wifi列表
	 * 先调用scanWifi(Handler handler)
	 * @return
	 */
	public List<ScanResult> getScanWifiList(){
		return mWifiList;
	}

	/**
	 * 在扫描结果中查看是否存在指定wifi
	 * 先调用scanWifi(Handler handler)
	 * @param SSID
	 * @return
	 */
	public boolean isSpecifyWifiExist(String SSID){
		if(mWifiList != null){
			for (ScanResult sr : mWifiList) {
				if (sr.SSID.equals(SSID) || sr.SSID.equals("\""+SSID+"\"")) {
					return true;
				}
			}
		}
		return false;
	}

	// 开启 wifi
	public boolean openWifi() {
		// 没有开启wifi则开启
		if (!mWifiManager.isWifiEnabled()) {
			mWifiManager.setWifiEnabled(true);
		}
		// 已经打开wifi则保存目前连接的wifi信息
		else {
			NetworkInfo ni = connManager.getActiveNetworkInfo();
			if (ni != null && ni.isAvailable() && ni.getType() == ConnectivityManager.TYPE_WIFI){
				oldWifiInfo = mWifiManager.getConnectionInfo();
			}
		}

		return mWifiManager.isWifiEnabled();
	}

	// 关闭 wifi
	public boolean closeWifi() {
		if (mWifiManager.isWifiEnabled()) {
			mWifiManager.setWifiEnabled(false);
		}

		return mWifiManager.isWifiEnabled();
	}

	/**
	 * 是否连接的指定wifi
	 * @param SSID
	 * @return
	 */
	public boolean isSpecifyWifi(String SSID){
		if (mWifiManager.isWifiEnabled()){
			NetworkInfo ni = connManager.getActiveNetworkInfo();
			if (ni != null && ni.isAvailable() && ni.getType() == ConnectivityManager.TYPE_WIFI){
				WifiInfo wi = mWifiManager.getConnectionInfo();
				if (wi.getSSID().equals(SSID) || wi.getSSID().equals("\""+SSID+"\"")) return true;
			}
		}

		return false;
	}

	/**
	 * 连接到给定信息的wifi网络
	 * @param SSID
	 * @param pw
	 * @param type 1，无密码方式连接 2，wep方式 3，wpa方式
	 * @return
	 */
	public boolean connect(String SSID, String pw, int type) {
		WifiConfiguration config = createWifiInfo(SSID, pw, type);
		try {
			int id = mWifiManager.addNetwork(config);
			boolean isEn = mWifiManager.enableNetwork(id, true);
			isConnected = true;
			if (dialogHandler != null)
				dialogHandler.obtainMessage(DeliverGoodsFragment.DIALOG_SHOW,"正在连接WIFI...").sendToTarget();
			return isEn;
		} catch (Exception e) {
			e.printStackTrace();
		}

		return false;
	}

	/**
	 * 重置到之前的wifi状态
	 */
	public void resetOldWifi(){
		if(oldWifiInfo != null) {
			// 连接到之前连接的wifi
			mWifiManager.enableNetwork(oldWifiInfo.getNetworkId(), true);
		} else {
			closeWifi();
		}
	}

	public boolean isWifiAvailable() {
		NetworkInfo net = null;
		if (null != connManager) {
			net = connManager.getActiveNetworkInfo();
			if (net != null && net.isAvailable() && net.getType() == ConnectivityManager.TYPE_WIFI) return true;
			else return false;
		} else {
			return false;
		}
	}

	/**
	 * 注册wifi广播接受者
	 */
	public void registerWifiReceiver() {
		IntentFilter filter = new IntentFilter();
		filter.addAction(WifiManager.SCAN_RESULTS_AVAILABLE_ACTION);
		filter.addAction(ConnectivityManager.CONNECTIVITY_ACTION);
		context.registerReceiver(wifiReceiver, filter);
	}


	/**
	 * 反注册wifi广播接受者
	 */
	public void unregisterWifiReceiver() {
		try{
			if (wifiReceiver != null)
				context.unregisterReceiver(wifiReceiver);
		}catch (IllegalArgumentException e){
			Log.e("qzj","wifiReceiver is not unregister!");
		}
	}

	@Override
	public void wifiConnected() {
		if (handler != null && isConnected){
			handler.obtainMessage(CONN_SUCCESS).sendToTarget();
			isConnected = false;
		}
	}

	@Override
	public void wifiDisconnected() {
		if (dialogHandler != null && isConnected){
			dialogHandler.obtainMessage(DeliverGoodsFragment.DIALOG_HIDEEN).sendToTarget();
			dialogHandler.obtainMessage(DeliverGoodsFragment.RESET_WIFI).sendToTarget();
			Toast.makeText(context, "WIFI连接失败", Toast.LENGTH_SHORT).show();
			isConnected = false;
		}
	}

	@Override
	public void wifiScanFinished() {
		if (!isScan) return;
		Log.e("WifiController", "----------------WIFI扫描完成");
		mWifiList = mWifiManager.getScanResults();
		if (handler != null) handler.obtainMessage(SCAN_FINISHED).sendToTarget();
		isScan = false;
	}

	public void setDialogHandler(Handler dialogHandler) {
		this.dialogHandler = dialogHandler;
	}
}
