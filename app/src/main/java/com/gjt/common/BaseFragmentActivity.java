package com.gjt.common;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSONObject;
import com.gjt.common.bean.PageBean;
import com.gjt.common.uitools.EmptyLayout;
import com.gjt.common.utils.Logger;
import com.gjt.common.utils.http.JsonConfig;


/**
 * 应用程序Activity的基类
 */
public class BaseFragmentActivity extends FragmentActivity {

	// ActionBar
	protected ImageView abLeft;
	public TextView abCenter;
	protected ImageView abRight;
	protected RelativeLayout abRightRoot;

	//	分页
	private PageBean pageBean = new PageBean(this);
	// 是否允许全屏
	private boolean allowFullScreen = true;
	// 是否允许销毁
	private boolean allowDestroy = true;
	private View view;
	protected View loadingMore;
	//空视图

	public EmptyLayout mEmpty ;
	private final String TAG="com.app.common.app.BaseFragmentActivity";

	public void initEmptyLayout(){
//		mEmpty=(EmptyLayout) findViewById(R.id.empty);
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
		LayoutInflater inflater = LayoutInflater.from(this);
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

	protected PageBean getPageBean() {
		return pageBean;
	}
}

