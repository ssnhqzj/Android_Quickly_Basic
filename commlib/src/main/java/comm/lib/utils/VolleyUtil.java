package comm.lib.utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.widget.ImageView;

import com.alibaba.fastjson.JSONObject;

import java.io.ByteArrayOutputStream;

import comm.lib.volley.Cache;
import comm.lib.volley.DefaultRetryPolicy;
import comm.lib.volley.Request.Method;
import comm.lib.volley.RequestQueue;
import comm.lib.volley.Response.ErrorListener;
import comm.lib.volley.Response.Listener;
import comm.lib.volley.VolleyError;
import comm.lib.volley.toolbox.ImageLoader;
import comm.lib.volley.toolbox.ImageLoader.ImageListener;
import comm.lib.volley.toolbox.JsonObjectRequest;
import comm.lib.volley.toolbox.Volley;
import comm.lib.volley.utils.MemoryCache;
import gzt.com.commlib.R;

/**
 * Volley网络访问类
 * @author qzj
 */
public class VolleyUtil {

	/**
	 * 默认缓存目录
	 */
	private static final String DEFAULT_CACHE_DIR = "gztDir";
	/**
	 * 默认加载图片最大宽度
	 */
	private static final int DEFAULT_MAX_IMAGE_WIDTH = 800;
	/**
	 * 默认加载图片最大高度
	 */
	private static final int DEFAULT_MAX_IMAGE_HEIGHT = 800;

	private static VolleyUtil instance;
	public static RequestQueue jsonQueue;
	public static RequestQueue imageQueue;
	private Context context;
	public JsonConfig jsonConfig;
	public MemoryCache cache;
	public ImageLoader imageLoader;

	private VolleyUtil(Context context){
		this.context = context;
	}

	public static VolleyUtil getInstance(Context context){
		if (instance==null){
			synchronized (VolleyUtil.class){
				instance = new VolleyUtil(context);
			}
		}

		return instance;
	}

	/**
	 * 初始化Volley的Json
	 * @return
	 */
	public VolleyUtil initVolleyJson(){
		if(jsonQueue == null){
			jsonQueue = Volley.newRequestQueue(context);
		}

		return this;
	}

	/**
	 * 初始化Volley的ImageLoader
	 * @return
	 */
	public VolleyUtil initImageLoader(){
		if(imageLoader == null){
			imageQueue = Volley.newRequestQueueWithDiskDir(context, DEFAULT_CACHE_DIR);
			cache = new MemoryCache();
			imageLoader = new ImageLoader(imageQueue, cache);
		}

		return this;
	}

	/**
	 * 请求Json数据
	 */
	public void requestJson(final JsonConfig jsonConfig) {

		JSONObject paramsJson = null;
		if(jsonConfig.getParams() != null){
			paramsJson = new JSONObject(jsonConfig.getParams());
		}
		JsonObjectRequest request_Json = new JsonObjectRequest(Method.POST,jsonConfig.getUrl(), paramsJson,
				new Listener<JSONObject>() {

					@Override
					public void onResponse(JSONObject dataJSON) {
						jsonConfig.setState(jsonConfig.STATE_HOST_SUCCESS);
						jsonConfig.setJsonObject(dataJSON);
						if(jsonConfig.getReturnListener() != null){
							jsonConfig.getReturnListener().returnSuccess(jsonConfig);
						}
					}
				},

				new ErrorListener() {

					@SuppressWarnings("static-access")
					@Override
					public void onErrorResponse(VolleyError errorData) {
						jsonConfig.setState(jsonConfig.STATE_HOST_ERROR);
						jsonConfig.setVolleyError(errorData);
						if(jsonConfig.getReturnListener() != null){
							jsonConfig.getReturnListener().returnError(jsonConfig);
						}
					}
				}
		);

		request_Json.setRetryPolicy(new DefaultRetryPolicy(jsonConfig.getInitialTimeoutMs(), jsonConfig.getMaxNumRetries(), jsonConfig.getBackoffMultiplier()));
		jsonQueue.add(request_Json);
	}

	/**
	 * 加载图片,使用默认加载图片的宽高
	 * @param imageView
	 * @param url
	 * @param isCache
	 */
	public void loadImage(ImageView imageView,String url, boolean isCache){
		this.loadImage(imageView, url, DEFAULT_MAX_IMAGE_WIDTH, DEFAULT_MAX_IMAGE_HEIGHT,isCache);
	}

	/**
	 * 加载图片,指定加载图片的宽高
	 * @param imageView
	 * @param url
	 * @param preWidth
	 * @param preHeight
	 */
	public void loadImage(ImageView imageView,String url, int preWidth, int preHeight, boolean isCache){
		if(imageView == null){
			throw new NullPointerException(VolleyUtil.class.getName()+":loadImage方法中参数ImageView不能为NULL");
		}
		if(url == null || "".equals(url)){
			throw new NullPointerException(VolleyUtil.class.getName()+":loadImage方法中参数url不能为NULL");
		}

		ImageListener listener = ImageLoader.getImageListener(imageView, R.mipmap.image_load_failed, R.mipmap.image_load_failed);
		imageLoader.get(url, listener, preWidth, preHeight, isCache);
	}

	/**
	 * 加载Volley磁盘缓存中的图片
	 * @param url
	 * @return
	 */
	public Bitmap loadDiskCacheImage(String url,String diskCacheDir){
		imageQueue = Volley.newRequestQueueWithDiskDir(context, diskCacheDir);
		Cache mCache = imageQueue.getCache();
		Cache.Entry mEntry = mCache.get(url);
		if(mEntry != null){
			return Bytes2Bimap(mEntry.data);
		}

		return null;
	}

	/**
	 * 移除Volley磁盘缓存中的缓存图片
	 * @param url
	 * @param diskCacheDir
	 */
	public void removeDiskCacheImage(String url,String diskCacheDir){
		imageQueue = Volley.newRequestQueueWithDiskDir(context, diskCacheDir);
		Cache mCache = imageQueue.getCache();
		mCache.remove(url);
	}

	/**
	 * Bitmap转换成byte[]
	 * @param bm
	 * @return
	 */
	public byte[] Bitmap2Bytes(Bitmap bm) {
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		bm.compress(Bitmap.CompressFormat.PNG, 100, baos);
		return baos.toByteArray();
	}

	/**
	 * byte[]转换成bitmap
	 * @param b
	 * @return
	 */
	public Bitmap Bytes2Bimap(byte[] b) {
		if (b.length != 0) {
			return BitmapFactory.decodeByteArray(b, 0, b.length);
		} else {
			return null;
		}
	}

	/**
	 * 回调监听接口
	 * @author Administrator
	 */
	public interface onJsonReturnListener{
		/**
		 * 返回成功
		 * @param jsonConfig
		 */
		public void returnSuccess(JsonConfig jsonConfig);
		/**
		 * 执行错误
		 * @param jsonConfig
		 */
		public void returnError(JsonConfig jsonConfig);
	}

}
