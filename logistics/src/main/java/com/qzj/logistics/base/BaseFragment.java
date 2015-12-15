package com.qzj.logistics.base;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

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

}
