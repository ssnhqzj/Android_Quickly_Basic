package com.qzj.logistics.base;

import java.io.Serializable;

public class BaseBean implements Serializable {
	private static final long serialVersionUID = 1L;

	/**
	 * 服务器请求结果返回码
	 */
	protected int resultCode;
	/**
	 * 服务器请求结果返回信息
	 */
	protected String resultMsg;

	public int getResultCode() {
		return resultCode;
	}

	public void setResultCode(int resultCode) {
		this.resultCode = resultCode;
	}

	public String getResultMsg() {
		return resultMsg;
	}

	public void setResultMsg(String resultMsg) {
		this.resultMsg = resultMsg;
	}

	@Override
	public String toString() {
		return "BaseBean{" +
				"resultCode=" + resultCode +
				", resultMsg='" + resultMsg + '\'' +
				'}';
	}
}
