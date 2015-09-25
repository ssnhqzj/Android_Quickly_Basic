package comm.lib.volley.utils;

import android.graphics.Bitmap;
import android.support.v4.util.LruCache;

import comm.lib.volley.toolbox.ImageLoader;


public class MemoryCache implements ImageLoader.ImageCache {

	private static final String TAG = "MemoryCache";
	private LruCache<String, Bitmap> mCache;

	public MemoryCache() {
		// 取单个应用最大内存的1/8
		int maxSize = getUsableMemory()*1 / 8;
		mCache = new LruCache<String, Bitmap>(maxSize) {
			@Override
			protected int sizeOf(String key, Bitmap value) {
				// 必须重新此方法，不然没的缓存效果
				return value.getHeight() * value.getRowBytes() / 1024;
			}
		};
	}

	@Override
	public Bitmap getBitmap(String key) {
		return mCache.get(key);
	}

	@Override
	public void putBitmap(String key, Bitmap value) {
		if(getBitmap(key) == null && value != null){
			mCache.put(key, value);
		}
	}
	
	public static int getUsableMemory(){
        int maxMemory = (int) (Runtime.getRuntime().maxMemory() / 1024);  
        return maxMemory;
    }
}
