package com.gjt.common.utils.http;

import android.content.Context;
import android.support.v4.app.Fragment;

import com.alibaba.fastjson.JSONObject;
import com.gjt.common.uitools.EmptyLayout;

import java.util.Map;

import comm.lib.volley.VolleyError;


public class JsonConfig {

//	/*** 请求成功 1 */
	public static final int SUCCESS = 1;
//	/*** 服务器返回码 */
	public static final String RESULTCODE = "resultCode";
	public static final String RESULTMSG="resultMsg";
//	/*** 其他错误-3 */
//	public static final int STATE_ERROR_OTHER = -3;

	public static final int NETWORK_ERROR = EmptyLayout.NETWORK_ERROR;

	public static final int NO_LOGIN = EmptyLayout.NO_LOGIN;

	/*** 超时时间 */
	private int initialTimeoutMs = 2500;
	/*** 最大重复发送请求数 */
	private int maxNumRetries = 1;
	private float backoffMultiplier = 1f;

	/*** url */
	private String url;
	/*** 请求成功返回的json对象 */
	private JSONObject jsonObject;
	/*** 请求失败返回的错误信息对象 */
	private VolleyError volleyError;
	/*** 返回监听 */
	private JsonUtil.onJsonReturnListener returnListener;
	/*** 请求参数 */
	private Map<String, Object> params;
	/*** 上下文 */
	private Context context;
	/*** 返回状态 */
	private int state;
	/*** 请求标识 */
	private int what;
	/** 标示 */
	private Object flag;

	public JsonConfig() {}

	/**
	 * 
	 * @param url
	 * @param params
	 * @param context
	 */
	public JsonConfig(String url, Map<String, Object> params, Context context) {
		this.url = url;
		this.params = params;
		this.context = context;
		if (context instanceof JsonUtil.onJsonReturnListener)
			returnListener = (JsonUtil.onJsonReturnListener) context;
	}
	
	/**
	 * @param url
	 * @param params
	 * @param context
	 * @param what
	 */
	public JsonConfig(String url, Map<String, Object> params, Context context, int what) {
		this.url = url;
		this.params = params;
		this.context = context;
		if (context instanceof JsonUtil.onJsonReturnListener)
			returnListener = (JsonUtil.onJsonReturnListener) context;
		this.what = what;
	}
	public JsonConfig(String url, Map<String, Object> params, Fragment fragment, int what) {
		this.url = url;
		this.params = params;
		this.context = fragment.getActivity();
		if (fragment instanceof JsonUtil.onJsonReturnListener)
			returnListener = (JsonUtil.onJsonReturnListener) fragment;
		this.what = what;
	}
	
	public JsonConfig(String url, Map<String, Object> params, Context context,JsonUtil.onJsonReturnListener returnListener, int what) {
		this.url = url;
		this.params = params;
		this.context = context;
		this.returnListener = returnListener;
		this.what = what;
	}

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

	public JsonUtil.onJsonReturnListener getReturnListener() {
		return returnListener;
	}

	public void setReturnListener(JsonUtil.onJsonReturnListener returnListener) {
		this.returnListener = returnListener;
	}

	public int getState() {
		return state;
	}

	public void setState(int state) {
		this.state = state;
	}

	public com.alibaba.fastjson.JSONObject getJsonObject() {
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

	public Object getFlag() {
		return flag;
	}

	public void setFlag(Object flag) {
		this.flag = flag;
	}

	@Override
	public String toString() {
		return "JsonConfig [initialTimeoutMs=" + initialTimeoutMs + ", maxNumRetries=" + maxNumRetries + ", backoffMultiplier=" + backoffMultiplier + ", url=" + url + ", jsonObject=" + jsonObject
				+ ", volleyError=" + volleyError + ", returnListener=" + returnListener + ", params=" + params + ", context=" + context + ", state=" + state + ", what=" + what + "]";
	}

}
