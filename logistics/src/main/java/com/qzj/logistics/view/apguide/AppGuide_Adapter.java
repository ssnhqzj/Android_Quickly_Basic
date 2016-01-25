package com.qzj.logistics.view.apguide;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.view.View;

import java.util.List;

public class AppGuide_Adapter extends FragmentPagerAdapter
{

//	private final FragmentManager mFM;
	//	public static final String[] TITLES = new String[] { "1", "2 ", "3 ", "4"};
	private  List<Integer> mUris;

	public AppGuide_Adapter(FragmentManager fm,List<Integer> uris)
	{
		super(fm);
//		this.mFM=fm;
		this.mUris=uris;
	}

	@Override
	public Fragment getItem(int arg0)
	{
		AppGuideFragment fragment = new AppGuideFragment(mUris.get(arg0));
		return fragment;
	}

	@Override
	public CharSequence getPageTitle(int position)
	{
		return mUris.get(position % mUris.size())+"";
	}
	@Override
	public boolean isViewFromObject(View view, Object obj) {
		return view == ((Fragment) obj).getView();
	}


//		@Override
//	public void destroyItem(ViewGroup container, int position, Object object) {
//		AppGuideFragment fragment = ((AppGuideFragment) object);
//		fragment.rootView=null;
////		container.removeView(fragment.getView());
//	}
	@Override
	public int getCount()
	{
		return mUris.size();
	}

}
