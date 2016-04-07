package com.qzj.logistics.bean;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;
import com.qzj.logistics.base.BaseBean;

/**
 * 城市
 */
@DatabaseTable(tableName = "hat_city")
public class HatCity extends BaseBean {

	@DatabaseField(columnName = "cid")
	private Integer cid;

	@DatabaseField(id = true, columnName = "cityid")
	private String cityid;

	@DatabaseField(columnName = "city")
	private String city;

	@DatabaseField(canBeNull = false, foreign = true, columnName = "province_id")
	private HatProvince hatProvince;

	private String father;

	public Integer getCid() {
		return cid;
	}

	public void setCid(Integer cid) {
		this.cid = cid;
	}

	public String getCityid() {
		return cityid;
	}

	public void setCityid(String cityid) {
		this.cityid = cityid;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public String getFather() {
		return father;
	}

	public void setFather(String father) {
		this.father = father;
	}

	public HatProvince getHatProvince() {
		return hatProvince;
	}

	public void setHatProvince(HatProvince hatProvince) {
		this.hatProvince = hatProvince;
	}

	@Override
	public String toString() {
		return "HatCity{" +
				", cid=" + cid +
				", cityid='" + cityid + '\'' +
				", city='" + city + '\'' +
				", hatProvince=" + hatProvince +
				", father='" + father + '\'' +
				'}';
	}
}
