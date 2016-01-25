package com.qzj.logistics.app;

import android.app.Application;
import android.content.Context;
import android.util.Base64;

import com.alibaba.fastjson.JSON;
import com.qzj.logistics.R;
import com.qzj.logistics.bean.AddrMoare;
import com.qzj.logistics.bean.Company;
import com.qzj.logistics.bean.PrintWifi;
import com.qzj.logistics.bean.User;
import com.qzj.logistics.bean.impl.SpinnerAdapterIf;
import com.qzj.logistics.db.AssetsDatabaseManager;
import com.qzj.logistics.templete.BestBill;
import com.qzj.logistics.templete.EMSBill;
import com.qzj.logistics.templete.FastBill;
import com.qzj.logistics.templete.QFBill;
import com.qzj.logistics.templete.SFBill;
import com.qzj.logistics.templete.STOBill;
import com.qzj.logistics.templete.SinforBill;
import com.qzj.logistics.templete.USBill;
import com.qzj.logistics.templete.YTBill;
import com.qzj.logistics.templete.YunDaBill;
import com.qzj.logistics.templete.ZTOBill;
import com.qzj.logistics.utils.PreferenceUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class MyApplication extends Application {
	
	public static Context mContext;
	private static User user;
	// 打印机wifi配置
	private static PrintWifi printWifi;
	// 产品类别
	public static List<String> productTypes;
	// 发件人列表
	private static List<SpinnerAdapterIf> senders;
	// 快递公司列表
	private static List<SpinnerAdapterIf> companies;
	// 是否有版本更新
	private static boolean hasVersionChange = false;
	// 打印机wifi配置是否有改变
	private static boolean isPrintSettingChanged = false;
	// 默认地址是否有改变
	private static boolean isDefaultAddrChanged = false;

	private AssetsDatabaseManager am;

	@Override
	public void onCreate() {
		super.onCreate();
		// 注册crashHandler(自定义异常处理)
		CrashHandler crashHandler = CrashHandler.getInstance();
		crashHandler.init(getApplicationContext());
		
		getAppContext();
		productTypes = Arrays.asList(getResources().getStringArray(R.array.product_types));
		initCompanies();

		// 初始化数据库文件
		AssetsDatabaseManager.initManager(this);
		am = AssetsDatabaseManager.getManager();
		am.createDatabase();
	}
	
	
	/**
	 * 
	 * @Description: TODO(获取工程的Context) 
	 * @return
	 */
	public Context getAppContext() {
		if (mContext==null) {
			mContext=getApplicationContext();
		}
		return mContext;
	}

	/**
	 * 初始化本地的物流公司
	 */
	private void initCompanies() {
		this.companies = new ArrayList<>();

		Company sf = new Company();
		sf.setCompany_id(1);
		sf.setCompany_name("顺丰");
		sf.setBillClazz(SFBill.class);
		this.companies.add(sf);

		Company sto = new Company();
		sto.setCompany_id(2);
		sto.setCompany_name("申通");
		sto.setBillClazz(STOBill.class);
		this.companies.add(sto);

		Company yt = new Company();
		yt.setCompany_id(3);
		yt.setCompany_name("圆通");
		yt.setBillClazz(YTBill.class);
		this.companies.add(yt);

		Company zto = new Company();
		zto.setCompany_id(4);
		zto.setCompany_name("中通");
		zto.setBillClazz(ZTOBill.class);
		this.companies.add(zto);

		Company yunda = new Company();
		yunda.setCompany_id(5);
		yunda.setCompany_name("韵达");
		yunda.setBillClazz(YunDaBill.class);
		this.companies.add(yunda);

		Company sinfor = new Company();
		sinfor.setCompany_id(6);
		sinfor.setCompany_name("信丰物流");
		sinfor.setBillClazz(SinforBill.class);
		this.companies.add(sinfor);

		Company us = new Company();
		us.setCompany_id(7);
		us.setCompany_name("优速快递");
		us.setBillClazz(USBill.class);
		this.companies.add(us);

		Company qf = new Company();
		qf.setCompany_id(8);
		qf.setCompany_name("全峰快递");
		qf.setBillClazz(QFBill.class);
		this.companies.add(qf);

		Company fast = new Company();
		fast.setCompany_id(9);
		fast.setCompany_name("快捷快递");
		fast.setBillClazz(FastBill.class);
		this.companies.add(fast);

		Company best = new Company();
		best.setCompany_id(10);
		best.setCompany_name("百世汇通");
		best.setBillClazz(BestBill.class);
		this.companies.add(best);

		Company ems = new Company();
		ems.setCompany_id(11);
		ems.setCompany_name("EMS");
		ems.setBillClazz(EMSBill.class);
		this.companies.add(ems);
	}

	/**
	 * 获取user，user为null就从Preference中获取
	 * @return
	 */
	public User getUser() {
		if (user == null){
			String userJson = PreferenceUtils.getPrefString(mContext,"user","");
			if (userJson != null && !userJson.equals("")){
				String decodeJson = new String(Base64.decode(userJson, Base64.DEFAULT));
				user = JSON.parseObject(decodeJson,User.class);
			}
		}
		return user;
	}

	/**
	 * 设置user并存入Preference中
	 * @param user
	 */
	public void setUser(User user) {
		MyApplication.user = user;
		String userJson = JSON.toJSONString(user);
		PreferenceUtils.setPrefString(mContext,"user", Base64.encodeToString(userJson.getBytes(),Base64.DEFAULT));
	}

	public List<SpinnerAdapterIf> getSenders() {
		return senders;
	}

	public void setSenders(List<AddrMoare> senders) {
		if (senders != null && senders.size() > 0){
			this.senders = new ArrayList<>();
			for (AddrMoare am : senders){
				am.findAddress(mContext);
				this.senders.add(am);
			}
		}
	}

	public List<SpinnerAdapterIf> getCompanies() {
		return companies;
	}

	/**
	 * 初始化从网络上加载的物流公司
	 * @param companies
	 */
	public void setCompanies(List<Company> companies) {
		if (companies != null && companies.size() > 0){
			this.companies = new ArrayList<>();
			for (Company company : companies){
				this.companies.add(company);
			}
		}
	}

	public boolean isHasVersionChange() {
		return hasVersionChange;
	}

	public void setHasVersionChange(boolean hasVersionChange) {
		MyApplication.hasVersionChange = hasVersionChange;
	}

	/**
	 * 获取printWifi，printWifi为null就从Preference中获取
	 * @return
	 */
	public PrintWifi getPrintWifi() {
		if (printWifi == null){
			String pwJson = PreferenceUtils.getPrefString(mContext,"print_wifi","");
			if (pwJson != null && !pwJson.equals("")){
				String decodeJson = new String(Base64.decode(pwJson, Base64.DEFAULT));
				printWifi = JSON.parseObject(decodeJson,PrintWifi.class);
			}
		}
		return printWifi;
	}

	/**
	 * 设置打印机wifi配置
	 * @param printWifi
	 */
	public void setPrintWifi(PrintWifi printWifi) {
		if (printWifi == null) return;
		MyApplication.printWifi = printWifi;
		String pwJson = JSON.toJSONString(printWifi);
		PreferenceUtils.setPrefString(mContext,"print_wifi", Base64.encodeToString(pwJson.getBytes(),Base64.DEFAULT));
		isPrintSettingChanged = true;
	}

	public boolean isPrintSettingChanged() {
		return isPrintSettingChanged;
	}

	public boolean isDefaultAddrChanged() {
		return isDefaultAddrChanged;
	}

	public void setIsDefaultAddrChanged(boolean isDefaultAddrChanged) {
		MyApplication.isDefaultAddrChanged = isDefaultAddrChanged;
	}

	public AssetsDatabaseManager getAm() {
		return am;
	}
}