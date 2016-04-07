package com.qzj.logistics.bean;

import com.qzj.logistics.base.BaseBean;

public class User extends BaseBean {

	private Integer user_id;

	private String user_name;

	private String telephone;

	private String email;

	private Integer is_admin;

	private String password;

	private String mail_code;

	private String create_date;

	private String cid;
	private String rid;
	private String  addr;

	private AddrMoare addrMore;

	public Integer getUser_id() {
		return user_id;
	}

	public void setUser_id(Integer user_id) {
		this.user_id = user_id;
	}

	public String getUser_name() {
		return user_name;
	}

	public void setUser_name(String user_name) {
		this.user_name = user_name;
	}

	public String getTelephone() {
		return telephone;
	}

	public void setTelephone(String telephone) {
		this.telephone = telephone;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public Integer getIs_admin() {
		return is_admin;
	}

	public void setIs_admin(Integer is_admin) {
		this.is_admin = is_admin;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getMail_code() {
		return mail_code;
	}

	public void setMail_code(String mail_code) {
		this.mail_code = mail_code;
	}

	public String getCreate_date() {
		return create_date;
	}

	public void setCreate_date(String create_date) {
		this.create_date = create_date;
	}

	public AddrMoare getAddrMore() {
		return addrMore;
	}

	public void setAddrMore(AddrMoare addrMore) {
		this.addrMore = addrMore;
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

	@Override
	public String toString() {
		return "User{" +
				"user_id=" + user_id +
				", user_name='" + user_name + '\'' +
				", telephone='" + telephone + '\'' +
				", email='" + email + '\'' +
				", is_admin=" + is_admin +
				", password='" + password + '\'' +
				", mail_code='" + mail_code + '\'' +
				", create_date='" + create_date + '\'' +
				", cid='" + cid + '\'' +
				", rid='" + rid + '\'' +
				", addr='" + addr + '\'' +
				", addrMore=" + addrMore +
				'}';
	}
}
