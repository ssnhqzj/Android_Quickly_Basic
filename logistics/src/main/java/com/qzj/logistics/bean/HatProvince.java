package com.qzj.logistics.bean;


import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;
import com.qzj.logistics.base.BaseBean;

/**
 * 省份
 */
@DatabaseTable(tableName = "hat_province")
public class HatProvince extends BaseBean {

	@DatabaseField(columnName = "sid")
	private Integer sid;

	@DatabaseField(id = true,columnName = "provinceid")
	private String provinceid;

	@DatabaseField(columnName = "province")
	private String province;

	public Integer getSid() {
		return sid;
	}

	public void setSid(Integer sid) {
		this.sid = sid;
	}

	public String getProvinceid() {
		return provinceid;
	}

	public void setProvinceid(String provinceid) {
		this.provinceid = provinceid;
	}

	public String getProvince() {
		return province;
	}

	public void setProvince(String province) {
		this.province = province;
	}

	@Override
	public String toString() {
		return "HatProvince{" +
				", sid=" + sid +
				", provinceid='" + provinceid + '\'' +
				", province='" + province + '\'' +
				'}';
	}
}
