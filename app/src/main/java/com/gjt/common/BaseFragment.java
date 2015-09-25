package com.gjt.common;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.alibaba.fastjson.JSONObject;
import com.gjt.common.bean.PageBean;
import com.gjt.common.uitools.EmptyLayout;
import com.gjt.common.utils.Logger;
import com.gjt.common.utils.http.JsonConfig;


/**
 *@autoer yb -yb498869020@hotmail.com
 *@Time 下午2:07:03
 *package com.villageassistant.uhuo.app;
 *@Description TODO
 **/
public class BaseFragment extends Fragment{

	private final String TAG="com.app.common.app.BaseFragment";

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		return super.onCreateView(inflater, container, savedInstanceState);
	}
	//空视图

	public EmptyLayout mEmpty ;
	public void initEmptyLayout(View container){
//		mEmpty=(EmptyLayout) container.findViewById(R.id.empty);
	}
	public EmptyLayout getEmptyLayout(){
		return  mEmpty;
	}

	private PageBean pageBean = new PageBean(getActivity());
	/**
	* <分页控件需要初始化，调用getPageBean().setmIpage(IPage mIpage)初始化控件> 
	* @throw 
	* @author: yb 
	* @return PageBean
	 */
	public PageBean getPageBean() {
		return pageBean;
	}

	public void setPageBean(PageBean pageBean) {
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
