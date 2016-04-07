package com.qzj.logistics.bean;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;
import com.qzj.logistics.base.BaseBean;

/**
 * 城区
 */
@DatabaseTable(tableName = "hat_area")
public class HatArea extends BaseBean {

	@DatabaseField(columnName = "rid")
	private Integer rid;

	@DatabaseField(id = true,columnName = "areaid")
	private String areaid;

	@DatabaseField(columnName = "area")
	private String area;

	@DatabaseField(canBeNull = true, foreign = true, columnName = "city_id")
	private HatCity hatCity;

	private String father;

	public Integer getRid() {
		return rid;
	}

	public void setRid(Integer rid) {
		this.rid = rid;
	}

	public String getAreaid() {
		return areaid;
	}

	public void setAreaid(String areaid) {
		this.areaid = areaid;
	}

	public String getArea() {
		return area;
	}

	public void setArea(String area) {
		this.area = area;
	}

	public String getFather() {
		return father;
	}

	public void setFather(String father) {
		this.father = father;
	}

	public HatCity getHatCity() {
		return hatCity;
	}

	public void setHatCity(HatCity hatCity) {
		this.hatCity = hatCity;
	}

	@Override
	public String toString() {
		return "HatArea{" +
				", rid=" + rid +
				", areaid='" + areaid + '\'' +
				", area='" + area + '\'' +
				", hatCity=" + hatCity +
				", father='" + father + '\'' +
				'}';
	}
}
