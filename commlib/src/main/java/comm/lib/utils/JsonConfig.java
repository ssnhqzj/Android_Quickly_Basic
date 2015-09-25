package comm.lib.utils;

import android.content.Context;

import com.alibaba.fastjson.JSONObject;

import java.util.Map;

import comm.lib.volley.VolleyError;

public class JsonConfig {

	/*** request success  1 */
	public static final int STATE_HOST_SUCCESS = 1;
	/*** request fail -1 */
	public static final int STATE_HOST_ERROR = -1;
	/*** other exception -3 */
	public static final int STATE_ERROR_OTHER = -3;
	/*** conn time out */
	private int initialTimeoutMs = 2500;
	/*** Maximum number of repeated requests */
	private int maxNumRetries = 1;
	private float backoffMultiplier = 1f;
	
	/*** conn url */
	private String url;
	/*** a JSON object of returns */
	private JSONObject jsonObject;
	/***  */
	private VolleyError volleyError;
	/***  */
	private VolleyUtil.onJsonReturnListener returnListener;
	/***  */
	private Map<String, Object> params;
	/*** */
	private Context context;
	/*** */
	private int state;
	/***  */
	private int what;
	
	public int getInitialTimeoutMs() {
		return initialTimeoutMs;
	}
	public void setInitialTimeoutMs(int initialTimeoutMs) {
		this.initialTimeoutMs = initialTimeoutMs;
	}
	public int getMaxNumRetries() {
		return maxNumRetries;
	}
	public void setMaxNumRetries(int maxNumRetries) {
		this.maxNumRetries = maxNumRetries;
	}
	public float getBackoffMultiplier() {
		return backoffMultiplier;
	}
	public void setBackoffMultiplier(float backoffMultiplier) {
		this.backoffMultiplier = backoffMultiplier;
	}
	public VolleyUtil.onJsonReturnListener getReturnListener() {
		return returnListener;
	}
	public void setReturnListener(VolleyUtil.onJsonReturnListener returnListener) {
		this.returnListener = returnListener;
	}
	public int getState() {
		return state;
	}
	public void setState(int state) {
		this.state = state;
	}
	public JSONObject getJsonObject() {
		return jsonObject;
	}
	public void setJsonObject(JSONObject jsonObject) {
		this.jsonObject = jsonObject;
	}
	public VolleyError getVolleyError() {
		return volleyError;
	}
	public void setVolleyError(VolleyError volleyError) {
		this.volleyError = volleyError;
	}
	public int getWhat() {
		return what;
	}
	
	/**
	 *
	 * @param what
	 */
	public void setWhat(int what) {
		this.what = what;
	}
	public Map<String, Object> getParams() {
		return params;
	}
	public void setParams(Map<String, Object> params) {
		this.params = params;
	}
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}
	public Context getContext() {
		return context;
	}
	public void setContext(Context context) {
		this.context = context;
	}
	
	@Override
	public String toString() {
		return "JsonConfig [initialTimeoutMs=" + initialTimeoutMs
				+ ", maxNumRetries=" + maxNumRetries + ", backoffMultiplier="
				+ backoffMultiplier + ", url=" + url + ", jsonObject="
				+ jsonObject + ", volleyError=" + volleyError
				+ ", returnListener=" + returnListener + ", params=" + params
				+ ", context=" + context + ", state=" + state + ", what="
				+ what + "]";
	}
	
	
}
