package com.qzj.logistics.base;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.qzj.empty.EmptyLayout;
import com.qzj.logistics.R;

/**
 *@autoer yb -yb498869020@hotmail.com
 *@Time 下午2:07:03
 *package com.villageassistant.uhuo.app;
 *@Description TODO
 **/
public class BaseFragment extends Fragment{

	private final String TAG="com.app.common.app.BaseFragment";
	protected EmptyLayout emptyLayout;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		return super.onCreateView(inflater, container, savedInstanceState);
	}

	/**
	 * 初始化空视图
	 */
	protected void initEmptyLayout(View view){
		if (view == null) return;
		emptyLayout = (EmptyLayout) view.findViewById(R.id.empty_layout);
	}

}
