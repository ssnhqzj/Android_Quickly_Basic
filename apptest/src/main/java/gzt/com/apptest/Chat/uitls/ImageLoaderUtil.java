package gzt.com.apptest.Chat.uitls;

import android.graphics.Bitmap;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.display.RoundedBitmapDisplayer;

import gzt.com.apptest.R;

/**
 * @Description: TODO(--) 
 * @author wsx - heikepianzi@qq.com 
 * @date 2014-12-8 下午3:31:06 
 */

public class ImageLoaderUtil {
	
	private static ImageLoaderConfiguration otherConfig;// 加载图片到sdCard配置信息
	private static DisplayImageOptions headOptions,headCacheOptions,otherOptions,otherCacheOptions; //--加载配置

	/**
	 *
	 * @Description: TODO(其他图片配置器--带缓存)
	 * @return
	 */
	public static DisplayImageOptions getPhoneBookHeadOptions()
	{
		if (otherOptions==null) {
			otherOptions=initOptions(R.mipmap.ic_launcher,R.mipmap.ic_launcher,R.mipmap.ic_launcher,true);
		}
		return otherOptions;
	}
	
	/**
	 * 
	 * @Description: TODO(头像配置器--带缓存) 
	 * @return
	 */
	public static DisplayImageOptions getHeadOptions()
	{
		if (headOptions==null) {
			headOptions=initOptions(R.mipmap.ic_launcher,R.mipmap.ic_launcher,R.mipmap.ic_launcher,true);
		}
		return headOptions;
	}
	
	/**
	 * 
	 * @Description: TODO(头像配置器--不带缓存) 
	 * @return
	 */
	public static DisplayImageOptions getHeadNoCacheOptions()
	{
		if (headCacheOptions==null) {
			headCacheOptions=initOptions(R.mipmap.ic_launcher,R.mipmap.ic_launcher,R.mipmap.ic_launcher,false);
		}
		return headCacheOptions;
	}
	
	/**
	 * 
	 * @Description: TODO(其他图片配置器--带缓存) 
	 * @return
	 */
	public static DisplayImageOptions getOtherOptions()
	{
		if (otherOptions==null) {
			otherOptions=initOptions(R.mipmap.ic_launcher,R.mipmap.ic_launcher,R.mipmap.ic_launcher,true);
		}
		return otherOptions;
	}

	public static DisplayImageOptions getOtherOptions2()
	{
		DisplayImageOptions options = new DisplayImageOptions.Builder()
				.showImageOnFail(R.mipmap.ic_launcher)  //
				.cacheInMemory(true)  //设置下载的图片是否缓存在内存中
				.cacheOnDisk(true) //设置下载的图片是否缓存在SD卡中
				.imageScaleType(ImageScaleType.EXACTLY)
				.bitmapConfig(Bitmap.Config.RGB_565)  //
				.build();
		return options;
	}

	public static DisplayImageOptions getOtherOptionsRounded()
	{
		DisplayImageOptions options = new DisplayImageOptions.Builder()
				.cacheInMemory(true)  //设置下载的图片是否缓存在内存中
				.cacheOnDisk(true)
				.imageScaleType(ImageScaleType.EXACTLY)
				.bitmapConfig(Bitmap.Config.RGB_565)
				.displayer(new RoundedBitmapDisplayer(15))
				.resetViewBeforeLoading(true)
				.build();

		return options;
	}
	
	/**
	 * 
	 * @Description: TODO(其他图片配置器--不带缓存) 
	 * @return
	 */
	public static DisplayImageOptions getOtherNoCacheOptions()
	{
		if (otherCacheOptions==null) {
			otherCacheOptions=initOptions(R.mipmap.ic_launcher,R.mipmap.ic_launcher,R.mipmap.ic_launcher,false);
		}
		return otherCacheOptions;
	}

	/**
	 * 
	 * @Description: TODO(初始化图片配置器) 
	 * @param dLoading
	 * @param dEmpty
	 * @param dFail
	 * @return
	 */
	private static DisplayImageOptions initOptions(int dLoading,int dEmpty,int dFail,boolean isOnDisk){
		
		DisplayImageOptions options = new DisplayImageOptions.Builder()
			.showImageOnLoading(dLoading)
			.showImageForEmptyUri(dEmpty)  //
			.showImageOnFail(dFail)  //
			.cacheInMemory(true)  //设置下载的图片是否缓存在内存中  
			.cacheOnDisk(isOnDisk) //设置下载的图片是否缓存在SD卡中  
			.imageScaleType(ImageScaleType.EXACTLY)
			.bitmapConfig(Bitmap.Config.RGB_565)  //
			.build();
		return options;
	}
}
