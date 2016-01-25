package com.qzj.logistics.view.apguide;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.qzj.logistics.R;
import com.qzj.logistics.base.BaseFragment;
import com.qzj.logistics.view.usercenter.LoginActivity;

@SuppressLint("ValidFragment")
public class AppGuideFragment extends BaseFragment implements View.OnClickListener
{

	private Integer newsType ;

	public AppGuideFragment(Integer newsType)
	{
		this.newsType = newsType;
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState)
	{
		super.onActivityCreated(savedInstanceState);
	}


	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}


	public View rootView;
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
	{
		if (null != rootView) {
			ViewGroup parent = (ViewGroup) rootView.getParent();
			if (null != parent) {
				parent.removeView(rootView);
			}
		} else {
			rootView =initView(inflater);
		}
		return rootView;
	}

	private View initView(LayoutInflater inflater){
		View view = inflater.inflate(newsType, null);
		if (newsType== R.layout.frament_app_guide4){
			view.findViewById(R.id.guide_end).setOnClickListener(this);
		}
		if (newsType==0)
			return null;
		return view;
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()){
			case R.id.guide_end:
				startActivity(new Intent(this.getActivity(), LoginActivity.class));
				getActivity().finish();
				getActivity().overridePendingTransition(R.anim.activity_open_in_anim, R.anim.activity_open_out_anim);
				break;
		}
	}
}
