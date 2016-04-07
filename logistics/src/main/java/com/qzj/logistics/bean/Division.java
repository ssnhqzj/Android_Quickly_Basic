package com.qzj.logistics.bean;

import com.qzj.logistics.base.BaseBean;

/**
 * Created by Administrator on 2015/12/25.
 */
public class Division extends BaseBean {
    private CityData cityData;

    public CityData getCityData() {
        return cityData;
    }

    public void setCityData(CityData cityData) {
        this.cityData = cityData;
    }

    @Override
    public String toString() {
        return "Division{" +
                "cityData=" + cityData +
                '}';
    }
}
