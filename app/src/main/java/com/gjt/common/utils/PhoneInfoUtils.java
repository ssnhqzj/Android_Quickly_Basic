package com.gjt.common.utils;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.telephony.TelephonyManager;

import java.net.NetworkInterface;
import java.util.Collections;
import java.util.List;

/**
 * @Title: CreateTempId.java
 * @Package com.villageassistant.ui.tools
 * @Description: 返回一个传入长度的随即数
 * @author Administrator
 * @modifier 修改人名字
 * @date 2014-4-13 上午11:12:06
 * @version V1.0
 */
public final class PhoneInfoUtils {

	private static int nextIP = 0;

	/**
	 * 得到临时IP
	 * 
	 * @return
	 */
//	public static String getTempIP() {
//		String ip = GlobalVariable.staticP;
//		int leght = GlobalVariable.IP.length;
//		if (leght != 0) {
//			ip = GlobalVariable.IP[nextIP];
//			if (nextIP >= leght - 1) {
//				nextIP = 0;
//			} else {
//				nextIP++;
//			}
//		}
//		return ip;
//	}


	/**
	 * @Title: intToIp
	 * @Description: 得到wifi转换成192.168.137.1的形式ip
	 * @param i
	 * @return
	 * @author yb
	 * @return String
	 */
	public static String intToIp(int i) {

		return (i & 0xFF) + "." +

		((i >> 8) & 0xFF) + "." +

		((i >> 16) & 0xFF) + "." +

		(i >> 24 & 0xFF);

	}

	/**
	 * @Title: getMACAddress
	 * @Description: 获得MAC地址
	 * @param interfaceName
	 * @return
	 * @author yb
	 * @return String
	 */
	public static String getMACAddress(String interfaceName) {
		try {
			List<NetworkInterface> interfaces = Collections.list(NetworkInterface.getNetworkInterfaces());
			for (NetworkInterface intf : interfaces) {
				if (interfaceName != null) {
					if (!intf.getName().equalsIgnoreCase(interfaceName))
						continue;
				}
				byte[] mac = intf.getHardwareAddress();
				if (mac == null)
					return "";
				StringBuilder buf = new StringBuilder();
				for (int idx = 0; idx < mac.length; idx++)
					buf.append(String.format("%02X:", mac[idx]));
				if (buf.length() > 0)
					buf.deleteCharAt(buf.length() - 1);
				return buf.toString();
			}
		} catch (Exception ex) {
		} // for now eat exceptions
		return "";
		/*
		 * try { // this is so Linux hack return
		 * loadFileAsString("/sys/class/net/" +interfaceName +
		 * "/address").toUpperCase().trim(); } catch (IOException ex) { return
		 * null; }
		 */
	}

	public static String getIMEI(Context context) {
		TelephonyManager tm = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
		String imei = tm.getDeviceId();
		if (null == imei)
			return "";
		else
			return imei;
	}

	// sim卡唯一标识
	public static String getSIMEI(Context context) {
		TelephonyManager tm = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
		String simei = tm.getSubscriberId();
		if (null == simei)
			return "";
		else
			return simei;
	}

	public static String getPhoneNum(Context context) {
		TelephonyManager tm = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
		String num = tm.getLine1Number();
		if (null == num)
			return "";
		else
			return num;
	}

	public static String getPhoneProvider(Context context) {
		String simei = getSIMEI(context);
		// IMSI号前面3位460是国家，紧接着后面2位00 02是中国移动，01是中国联通，03是中国电信。
		if (simei.startsWith("4600001")) {
			return "中国联通";
		} else if (simei.startsWith("4600002")) {
			return "中国移动";
		} else if (simei.startsWith("4600003")) {
			return "中国电信";
		} else {
			return "";
		}
	}

	public static boolean isNetWorkAvailable(Context context) {
		ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo net = null;
		if (null != cm) {
			net = cm.getActiveNetworkInfo();
			if (net != null && net.isAvailable())
				return true;
			else
				return false;
		} else {
			return false;
		}
	}

	public static boolean isWifiAvailable(Context context) {
		ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo net = null;
		if (null != cm) {
			net = cm.getActiveNetworkInfo();
			if (net != null && net.isAvailable() && net.getType() == ConnectivityManager.TYPE_WIFI)
				return true;
			else
				return false;
		} else {
			return false;
		}
	}

	public static boolean isMobileAvailable(Context context) {
		ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo net = null;
		if (null != cm) {
			net = cm.getActiveNetworkInfo();
			if (net != null && net.isAvailable() && net.getType() == ConnectivityManager.TYPE_MOBILE)
				return true;
			else
				return false;
		} else {
			return false;
		}
	}

	
}
