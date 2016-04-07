package com.qzj.logistics.bean;

import com.qzj.logistics.base.BaseBean;

import java.util.List;

/**
 * Created by Administrator on 2015/12/28.
 */
public class UserData extends BaseBean {

    private User userData;

    private List<AddrMoare> addrMores;

    private List<Company> companies;

    public User getUserData() {
        return userData;
    }

    public void setUserData(User userData) {
        this.userData = userData;
    }

    public List<AddrMoare> getAddrMores() {
        return addrMores;
    }

    public void setAddrMores(List<AddrMoare> addrMores) {
        this.addrMores = addrMores;
    }

    public List<Company> getCompanies() {
        return companies;
    }

    public void setCompanies(List<Company> companies) {
        this.companies = companies;
    }

    @Override
    public String toString() {
        return "UserData{" +
                "userData=" + userData +
                ", addrMores=" + addrMores +
                ", companies=" + companies +
                '}';
    }
}
