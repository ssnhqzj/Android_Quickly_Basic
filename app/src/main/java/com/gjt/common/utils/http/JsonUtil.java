package com.gjt.common.utils.http;


import android.app.Activity;
import android.content.Context;

import com.alibaba.fastjson.JSONObject;
import com.gjt.common.utils.Logger;

import comm.lib.volley.DefaultRetryPolicy;
import comm.lib.volley.Request.Method;
import comm.lib.volley.RequestQueue;
import comm.lib.volley.Response.ErrorListener;
import comm.lib.volley.Response.Listener;
import comm.lib.volley.VolleyError;
import comm.lib.volley.toolbox.JsonObjectRequest;
import comm.lib.volley.toolbox.Volley;

/**
 * JSON请求类
 * 
 * @author Administrator
 * 
 */
public class JsonUtil {

	public static RequestQueue queue;
	public JsonConfig jsonConfig;
	private String TAG="JsonUtil";
	private static JsonUtil instance;

	public RequestQueue getQueue() {
		return queue;
	}
	public void setQueue(RequestQueue queue) {
		this.queue = queue;
	}
	public JsonConfig getJsonConfig() {
		return jsonConfig;
	}
	public void setJsonConfig(JsonConfig jsonConfig) {
		this.jsonConfig = jsonConfig;
	}

	public static JsonUtil getInstance(Context context){
		if (queue==null||instance==null){
			synchronized (JsonUtil.class){
				if (queue==null||instance==null)
				instance = new JsonUtil(context);
			}
		}

		return instance;
	}

	private JsonUtil(Context context) {
		queue = Volley.newRequestQueue(context.getApplicationContext());
	}

	/**
	 * 请求json数据方法
	 * "[3609] NetworkDispatcher.run: Unhandled exception com.alibaba.fastjson.JSONException: syntax error, pos 1"-- 表示路径错误!
	 */
	public void requestJson(final JsonConfig jsonConfig){

		JSONObject paramsJson = null;
		if (jsonConfig.getParams() != null) {
			paramsJson = new JSONObject(jsonConfig.getParams());
		} else if (jsonConfig.getJsonObject() != null) {
			paramsJson = jsonConfig.getJsonObject();
		}
		//System.out.println(jsonConfig);
		JsonObjectRequest request_Json = new JsonObjectRequest(Method.POST, jsonConfig.getUrl(), paramsJson, new Listener<JSONObject>() {

			@Override
			public void onResponse(JSONObject dataJSON) {
				Logger.i(TAG, "onResponse" + dataJSON);
				if (jsonConfig.getContext()== null) {
					Logger.i(TAG, "this content is destory");
					return;
				}

				jsonConfig.setState(JsonConfig.SUCCESS);
				jsonConfig.setJsonObject(dataJSON);
				if (jsonConfig.getReturnListener() != null) {
					jsonConfig.getReturnListener().returnSuccess(jsonConfig);
				}
			}
		},
				new ErrorListener() {

					@Override
					public void onErrorResponse(VolleyError errorData) {
						if (jsonConfig.getContext()== null) {
							return;
						}
						if(errorData.networkResponse!=null){
							int Status = errorData.networkResponse.statusCode;
							switch (Status){
								case 500:
									jsonConfig.setState(JsonConfig.NETWORK_ERROR);
									break;
								default:
									jsonConfig.setState(JsonConfig.NO_LOGIN);
									break;

							}
//				jsonConfig.setState(JsonConfig.STATE_HOST_ERROR);
						}else{
							jsonConfig.setState(JsonConfig.NETWORK_ERROR);
						}

						jsonConfig.setVolleyError(errorData);
						if (jsonConfig.getReturnListener() != null) {
							jsonConfig.getReturnListener().returnError(jsonConfig);
						}
					}
				});
		//System.out.println(jsonConfig);
		if (request_Json.getBody()!=null)
			Logger.d(TAG,"JsonObject = " + new String(request_Json.getBody()) + "\ngetUrl = " + jsonConfig.getUrl() + "\ngetWhat = " + jsonConfig.getWhat());
		request_Json.setRetryPolicy(new DefaultRetryPolicy(jsonConfig.getInitialTimeoutMs(), jsonConfig.getMaxNumRetries(), jsonConfig.getBackoffMultiplier()));
		request_Json.setTag(jsonConfig.getContext().getClass().getName());
		queue.add(request_Json);
	}
	/**
	 * 请求json数据方法
	 * 这个方法每次使用时必须重新创建JsonUtil不然只会访问最后一次配置
	 */
	@Deprecated
	public void requestJson() {
		requestJson(this.jsonConfig);
	}
	public void cancelAll(Activity context){
		if (queue!=null)
		{
			queue.cancelAll(context.getClass().getName());
		}
	}
	/**
	 * 返回监听
	 * 
	 * @author Administrator
	 */
	public interface onJsonReturnListener {
		/**
		 * 请求成功
		 * 
		 * @param jsonConfig
		 */
		public void returnSuccess(JsonConfig jsonConfig);

		/**
		 * ִ请求失败
		 * 
		 * @param jsonConfig
		 */
		public void returnError(JsonConfig jsonConfig);
	}
}
