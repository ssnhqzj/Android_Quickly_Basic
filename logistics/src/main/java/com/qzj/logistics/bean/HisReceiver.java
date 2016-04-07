package com.qzj.logistics.bean;

import com.qzj.logistics.view.books.widget.ContactItemInterface;

import java.io.Serializable;

/**
 * 收件人
 */
public class HisReceiver implements Serializable,ContactItemInterface {

	
	private static final long serialVersionUID = 1L;

	private int receiver_id;
	
	/**
	 * 收件人姓名
	 */
	private String name;

	/**
	 * 省份
	 */
	private String  p_name = "";
	
	/**
	 * 城市
	 */
	private String  c_name = "";
	
	/**
	 * 城区
	 */
	private String  r_name = "";
	
	/**
	 * 发件人ID 来自表f_user
	 */
	private Integer user_id;
	
	/**
	 * 收件人地址
	 */
	private String addr;
	
	/**
	 * 收件人邮编
	 */
	private String mail_code;
	
	/**
	 * 收件人电话
	 */
	private String telephone;
	
	/**
	 * 最新使用时间：年月日 时分秒
	 */
	private String time;
	
	//扩展字段
	/**
	 * 使用次数
	 */
	private Integer used;
	/**
	 * 快递单号
	 */
	private String courier_number;
	/**
	 * 货品种类待定
	 */
	private int type;

	private String selectedArea;

	// 组标题
	private String groupTitle;

	// 收件人信息串
	private String groupMsg;

	public Integer getReceiver_id() {
		return receiver_id;
	}

	public void setReceiver_id(Integer receiver_id) {
		this.receiver_id = receiver_id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getC_name() {
		return c_name;
	}

	public void setC_name(String c_name) {
		this.c_name = c_name;
	}

	public String getR_name() {
		return r_name;
	}

	public void setR_name(String r_name) {
		this.r_name = r_name;
	}

	public Integer getUser_id() {
		return user_id;
	}

	public void setUser_id(Integer user_id) {
		this.user_id = user_id;
	}

	public String getAddr() {
		return addr;
	}

	public void setAddr(String addr) {
		this.addr = addr;
	}

	public String getMail_code() {
		return mail_code;
	}

	public void setMail_code(String mail_code) {
		this.mail_code = mail_code;
	}

	public String getTelephone() {
		return telephone;
	}

	public void setTelephone(String telephone) {
		this.telephone = telephone;
	}

	public String getTime() {
		return time;
	}

	public void setTime(String time) {
		this.time = time;
	}

	public Integer getUsed() {
		return used;
	}

	public void setUsed(Integer used) {
		this.used = used;
	}

	public String getCourier_number() {
		return courier_number;
	}

	public void setCourier_number(String courier_number) {
		this.courier_number = courier_number;
	}

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}

	public String getSelectedArea() {
		selectedArea = p_name + c_name + r_name;
		return selectedArea;
	}

	public void setSelectedArea(String selectedArea) {
		this.selectedArea = selectedArea;
	}

	public String getGroupTitle() {
		return groupTitle;
	}

	public void setGroupTitle(String groupTitle) {
		this.groupTitle = groupTitle;
	}

	public void setGroupMsg(String groupMsg) {
		this.groupMsg = groupMsg;
	}

	// 完整发货人信息
	public String getGroupMsg() {
		groupMsg = "";
		groupMsg += (name==null?"":name);
		groupMsg += (telephone==null?"":"," +telephone);
		groupMsg += (getFullAddr()==null?"":"," +getFullAddr());
		groupMsg += (mail_code==null?"":"," +mail_code);
		if (groupMsg.startsWith(",")) return groupMsg.substring(1);
		return groupMsg;
	}

	// 完整地址
	public String getFullAddr(){
		String fullAddr = "";
		if (selectedArea != null)
			fullAddr += selectedArea;
		if (addr != null)
			fullAddr += addr;

		return fullAddr;
	}

	public String getP_name() {
		return p_name;
	}

	public void setP_name(String p_name) {
		this.p_name = p_name;
	}

	private boolean isChecked;

	@Override
	public String toString() {
		return "HisReceiver{" +
				"receiver_id=" + receiver_id +
				", name='" + name + '\'' +
				", p_name='" + p_name + '\'' +
				", c_name='" + c_name + '\'' +
				", r_name='" + r_name + '\'' +
				", user_id=" + user_id +
				", addr='" + addr + '\'' +
				", mail_code='" + mail_code + '\'' +
				", telephone='" + telephone + '\'' +
				", time='" + time + '\'' +
				", used=" + used +
				", courier_number='" + courier_number + '\'' +
				", type=" + type +
				", selectedArea='" + selectedArea + '\'' +
				", groupTitle='" + groupTitle + '\'' +
				", groupMsg='" + groupMsg + '\'' +
				'}';
	}

	@Override
	public String getItemForIndex() {
		return name;
	}

	@Override
	public String getDisplayInfo() {
		return name;
	}

	@Override
	public String getDisplayInfo_PhoneNumber() {
		return telephone;
	}

	@Override
	public boolean getRbState() {
		return isChecked;
	}

	@Override
	public void setRbState(boolean state) {
		isChecked = state;
	}
}
