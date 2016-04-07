package com.qzj.logistics.bean;

import com.qzj.logistics.base.BaseBean;

import java.util.ArrayList;

/**
 * Created by Administrator on 2015/12/25.
 */
public class CityData extends BaseBean {

    private int id;
    private ArrayList<HatProvince> hatProvinceList;
    private ArrayList<HatCity> hatCityList;
    private ArrayList<HatArea> hatAreaList;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public ArrayList<HatProvince> getHatProvinceList() {
        return hatProvinceList;
    }

    public void setHatProvinceList(ArrayList<HatProvince> hatProvinceList) {
        this.hatProvinceList = hatProvinceList;
    }

    public ArrayList<HatCity> getHatCityList() {
        return hatCityList;
    }

    public void setHatCityList(ArrayList<HatCity> hatCityList) {
        this.hatCityList = hatCityList;
    }

    public ArrayList<HatArea> getHatAreaList() {
        return hatAreaList;
    }

    public void setHatAreaList(ArrayList<HatArea> hatAreaList) {
        this.hatAreaList = hatAreaList;
    }

    @Override
    public String toString() {
        return "CityData{" +
                "id=" + id +
                ", hatProvinceList=" + hatProvinceList +
                ", hatCityList=" + hatCityList +
                ", hatAreaList=" + hatAreaList +
                '}';
    }
}
