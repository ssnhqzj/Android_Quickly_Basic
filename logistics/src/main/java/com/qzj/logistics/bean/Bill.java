package com.qzj.logistics.bean;

import com.qzj.logistics.base.BaseBean;

import java.util.ArrayList;

/**
 * 发货单
 */
public class Bill extends BaseBean{
    /**
     * 发货人列表
     */
    private ArrayList<String> persons;
    /**
     * 物流公司列表
     */
    private ArrayList<String> companies;

    /**
     * 发件人地址
     */
    private String selfAddress;

    /**
     * 城市列表
     */
    private ArrayList<String> cities;

    /**
     * 发货类别列表
     */
    private ArrayList<String> thingTypes;

    public ArrayList<String> getPersons() {
        return persons;
    }

    public void setPersons(ArrayList<String> persons) {
        this.persons = persons;
    }

    public ArrayList<String> getCompanies() {
        return companies;
    }

    public void setCompanies(ArrayList<String> companies) {
        this.companies = companies;
    }

    public String getSelfAddress() {
        return selfAddress;
    }

    public void setSelfAddress(String selfAddress) {
        this.selfAddress = selfAddress;
    }

    public ArrayList<String> getCities() {
        return cities;
    }

    public void setCities(ArrayList<String> cities) {
        this.cities = cities;
    }

    public ArrayList<String> getThingTypes() {
        return thingTypes;
    }

    public void setThingTypes(ArrayList<String> thingTypes) {
        this.thingTypes = thingTypes;
    }

    @Override
    public String toString() {
        return "Bill{" +
                "persons=" + persons +
                ", companies=" + companies +
                ", selfAddress='" + selfAddress + '\'' +
                ", cities=" + cities +
                ", thingTypes=" + thingTypes +
                '}';
    }
}
