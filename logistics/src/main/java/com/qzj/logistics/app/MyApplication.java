package com.qzj.logistics.app;

import android.app.Application;
import android.content.Context;

public class MyApplication extends Application {
	
	public static Context mContext;

	@Override
	public void onCreate() {
		super.onCreate();
		// 注册crashHandler(自定义异常处理)
		CrashHandler crashHandler = CrashHandler.getInstance();
		crashHandler.init(getApplicationContext());
		
		getAppContext();
	}
	
	
	/**
	 * 
	 * @Description: TODO(获取工程的Context) 
	 * @return
	 */
	public Context getAppContext()
	{
		if (mContext==null) {
			mContext=getApplicationContext();
		}
		return mContext;
	}
}