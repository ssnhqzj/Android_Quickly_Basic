package com.qzj.logistics.bean;

import java.util.List;

public class RecordAndReceiver {
	
	/**
	 * 发件人ID
	 */
	private Integer user_id;
	/**
	 * 公司ID
	 */
	private Integer company_id;
	/**
	 * 发件人城市ID
	 */
	private String cid;
	/**
	 * 发件人城区
	 */
	private String rid;
	
	/**
	 * 发件人地址
	 */
	private String addr;

	private AddrMoare am;

	/**
	 * 收件人列表
	 */
	private List<HisReceiver> hisReceivers;

	public Integer getUser_id() {
		return user_id;
	}

	public void setUser_id(Integer user_id) {
		this.user_id = user_id;
	}

	public Integer getCompany_id() {
		return company_id;
	}

	public void setCompany_id(Integer company_id) {
		this.company_id = company_id;
	}

	public String getCid() {
		return cid;
	}

	public void setCid(String cid) {
		this.cid = cid;
	}

	public String getRid() {
		return rid;
	}

	public void setRid(String rid) {
		this.rid = rid;
	}

	public String getAddr() {
		return addr;
	}

	public void setAddr(String addr) {
		this.addr = addr;
	}

	public List<HisReceiver> getHisReceivers() {
		return hisReceivers;
	}

	public void setHisReceivers(List<HisReceiver> hisReceivers) {
		this.hisReceivers = hisReceivers;
	}

	public AddrMoare getAm() {
		return am;
	}

	public void setAm(AddrMoare am) {
		this.am = am;
	}

	@Override
	public String toString() {
		return "RecordAndReceiver{" +
				"user_id=" + user_id +
				", company_id=" + company_id +
				", cid='" + cid + '\'' +
				", rid='" + rid + '\'' +
				", addr='" + addr + '\'' +
				", am=" + am +
				", hisReceivers=" + hisReceivers +
				'}';
	}
}
