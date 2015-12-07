package gzt.com.apptest.utils;

import android.content.Context;
import android.util.DisplayMetrics;
import android.view.WindowManager;

/**
 * 获取屏幕信息类
 * @author Administrator
 *
 */
public class DisplayUtils {
	
	private static WindowManager wm = null;
	private static DisplayMetrics dm = null;
	
	/**
	 * 获取屏幕宽度
	 * @return
	 */
	public static int getDisplayWidth(Context context){
		if(wm == null){
			wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
		}
		
		if(dm == null){
			dm = new DisplayMetrics();
		}
		 
		wm.getDefaultDisplay().getMetrics(dm);

        return dm.widthPixels;
	}
	
	/**
	 * 获取屏幕高度
	 * @return
	 */
	public static int getDisplayHeight(Context context){
		if(wm == null){
			wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
		}
		
		if(dm == null){
			dm = new DisplayMetrics();
		}
		
		wm.getDefaultDisplay().getMetrics(dm);

        return dm.heightPixels;
	}
}
