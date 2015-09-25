package com.gjt.common;

import android.app.Activity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;

import com.alibaba.fastjson.JSONObject;
import com.gjt.common.bean.PageBean;
import com.gjt.common.uitools.EmptyLayout;
import com.gjt.common.utils.Logger;
import com.gjt.common.utils.http.JsonConfig;


/**
 * 应用程序Activity的基类
 */
public class BaseActivity extends Activity{

	//	分页
	private PageBean pageBean = new PageBean(this);
	// 是否允许全屏
	private boolean allowFullScreen = true;
	// 是否允许销毁
	private boolean allowDestroy = true;
	
	private View view;
	//空视图

	public EmptyLayout mEmpty ;
	private final String TAG="com.app.common.app.BaseActivity";

	public void initEmptyLayout(){
//		mEmpty=(EmptyLayout)findViewById(R.id.empty);
	}
	public EmptyLayout getEmptyLayout(){
		return  mEmpty;
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		allowFullScreen = true;
		// 添加Activity到堆栈
		AppManager.getAppManager().addActivity(this);
	}

	@Override
	protected void onResume() {
		super.onResume();
	}
	@Override
	protected void onPause() {
		super.onPause();
	}
	@Override
	protected void onDestroy() {
		super.onDestroy();
		// 结束Activity&从堆栈中移除
		AppManager.getAppManager().finishActivity(this);
	}

	public boolean isAllowFullScreen() {
		return allowFullScreen;
	}

	/**
	 * 设置是否可以全屏
	 * 
	 * @param allowFullScreen
	 */
	public void setAllowFullScreen(boolean allowFullScreen) {
		this.allowFullScreen = allowFullScreen;
	}

	public void setAllowDestroy(boolean allowDestroy) {
		this.allowDestroy = allowDestroy;
	}

	public void setAllowDestroy(boolean allowDestroy, View view) {
		this.allowDestroy = allowDestroy;
		this.view = view;
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK && view != null) {
			view.onKeyDown(keyCode, event);
			if (!allowDestroy) {
				return false;
			}
		}
		return super.onKeyDown(keyCode, event);
	}
	/**
	* <分页控件需要初始化，调用getPageBean().setmIpage(IPage mIpage)初始化控件> 
	* @throw 
	* @author: yb 
	* @return PageBean
	 */
	protected PageBean getPageBean() {
		return pageBean;
	}

	protected void setPageBean(PageBean pageBean) {
		this.pageBean = pageBean;
	}

	//检测空视图状态
	public boolean checkState(JSONObject response){

		if (response!=null) {
			Object code = response.get(JsonConfig.RESULTCODE);
			Object net_msg = response.get(JsonConfig.RESULTMSG);
			if (code != null && !code.toString().equals("1")) {
				Logger.i(TAG, "ServiceCoder:" + code.toString() + ":" + net_msg.toString());
				if (mEmpty!=null)
				mEmpty.setErrorType(Integer.parseInt(code.toString()));
				return false;
			}
		}else{
			Logger.i(TAG, "ServiceCoder:"+"response null you need check NETWork");
			if (mEmpty!=null)
			mEmpty.setErrorType(EmptyLayout.NODATA);
			return false;
		}
		return true;
	}
	
	
}

