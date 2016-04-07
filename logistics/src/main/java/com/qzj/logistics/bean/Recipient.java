package com.qzj.logistics.bean;

import com.qzj.logistics.base.BaseBean;

import java.util.ArrayList;

/**
 * 收件人信息
 */
public class Recipient extends BaseBean {

    public static final int STATE_EDIT = 1;
    public static final int STATE_DELETE = 2;

    private int id;
    // 城市列表
    private ArrayList<String> cities;
    // 货物类别列表
    private ArrayList<String> thingTypes;
    // 选择的城市
    private String selectedCity;
    // 选择的货物类别
    private String selectedThingType;
    // 选择的城市Position
    private int selectedCityPosition;
    // 选择的货物类别Position
    private int selectedThingTypePosition;
    // 收件人姓名
    private String name;
    // 收件人电话
    private String phone;
    // 收件人地址
    private String address;
    // 收件人邮编
    private String postCode;
    // 快递单号
    private String number;

    // 组标题
    private String groupTitle;
    // 收件人信息串
    private String groupMsg;

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

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPostCode() {
        return postCode;
    }

    public void setPostCode(String postCode) {
        this.postCode = postCode;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public String getGroupTitle() {
        return groupTitle;
    }

    public void setGroupTitle(String groupTitle) {
        this.groupTitle = groupTitle;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getSelectedCity() {
        return selectedCity;
    }

    public void setSelectedCity(String selectedCity) {
        this.selectedCity = selectedCity;
    }

    public String getSelectedThingType() {
        return selectedThingType;
    }

    public void setSelectedThingType(String selectedThingType) {
        this.selectedThingType = selectedThingType;
    }

    public int getSelectedCityPosition() {
        return selectedCityPosition;
    }

    public void setSelectedCityPosition(int selectedCityPosition) {
        this.selectedCityPosition = selectedCityPosition;
    }

    public int getSelectedThingTypePosition() {
        return selectedThingTypePosition;
    }

    public void setSelectedThingTypePosition(int selectedThingTypePosition) {
        this.selectedThingTypePosition = selectedThingTypePosition;
    }

    public String getGroupMsg() {
        groupMsg = "";
        groupMsg += (name==null?"":name);
        groupMsg += (phone==null||phone.equals("")?"":"," +phone);
        groupMsg += (address==null||address.equals("")?"":"," +address);
        groupMsg += (postCode==null||postCode.equals("")?"":"," +postCode);
        groupMsg += (number==null||number.equals("")?"":"," +number);
        if (groupMsg.startsWith(",")) return groupMsg.substring(1);
        return groupMsg;
    }

    public void setGroupMsg(String groupMsg) {
        this.groupMsg = groupMsg;
    }

    @Override
    public String toString() {
        return "Recipient{" +
                "id=" + id +
                ", cities=" + cities +
                ", thingTypes=" + thingTypes +
                ", selectedCity='" + selectedCity + '\'' +
                ", selectedThingType='" + selectedThingType + '\'' +
                ", selectedCityPosition=" + selectedCityPosition +
                ", selectedThingTypePosition=" + selectedThingTypePosition +
                ", name='" + name + '\'' +
                ", phone='" + phone + '\'' +
                ", address='" + address + '\'' +
                ", postCode='" + postCode + '\'' +
                ", number='" + number + '\'' +
                ", groupTitle='" + groupTitle + '\'' +
                ", groupMsg='" + groupMsg + '\'' +
                '}';
    }
}
