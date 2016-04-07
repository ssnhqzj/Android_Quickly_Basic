package com.qzj.logistics.view.books.model;


import com.qzj.logistics.base.BaseBean;
import com.qzj.logistics.view.books.widget.ContactItemInterface;

public class BookItem extends BaseBean implements ContactItemInterface
{
	private int c_id;
	private int role;
	private int user_id;
	private String user_name;
	private String fullName;
	private String telephone;
	private boolean isChecked;

	public BookItem(){}

	public BookItem(String nickName, String fullName, String phoneNumber, boolean isChecked)
	{
		super();
		this.user_name = nickName;
		this.setFullName(fullName);
		this.telephone = phoneNumber;
		this.isChecked=isChecked;
	}

	@Override
	public String getItemForIndex()
	{
		return fullName;
	}

	@Override
	public String getDisplayInfo()
	{
		return user_name;
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

	public int getC_id() {
		return c_id;
	}

	public void setC_id(int c_id) {
		this.c_id = c_id;
	}

	public int getRole() {
		return role;
	}

	public void setRole(int role) {
		this.role = role;
	}

	public int getUser_id() {
		return user_id;
	}

	public void setUser_id(int user_id) {
		this.user_id = user_id;
	}

	public String getUser_name() {
		return user_name;
	}

	public void setUser_name(String user_name) {
		this.user_name = user_name;
	}

	public String getFullName() {
		return fullName;
	}

	public void setFullName(String fullName) {
		this.fullName = fullName;
	}

	public String getTelephone() {
		return telephone;
	}

	public void setTelephone(String telephone) {
		this.telephone = telephone;
	}

	public boolean isChecked() {
		return isChecked;
	}

	public void setIsChecked(boolean isChecked) {
		this.isChecked = isChecked;
	}

	@Override
	public String toString() {
		return "BookItem{" +
				"c_id=" + c_id +
				", role=" + role +
				", user_id=" + user_id +
				", user_name='" + user_name + '\'' +
				", fullName='" + fullName + '\'' +
				", telephone='" + telephone + '\'' +
				", isChecked=" + isChecked +
				'}';
	}
}
