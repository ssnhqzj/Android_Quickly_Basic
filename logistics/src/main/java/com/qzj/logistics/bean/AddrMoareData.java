package com.qzj.logistics.bean;

import com.qzj.logistics.base.BaseBean;

import java.util.ArrayList;

/**
 * Created by Administrator on 2015/12/30.
 */
public class AddrMoareData extends BaseBean {

    private ArrayList<AddrMoare> addrMoareData;

    public ArrayList<AddrMoare> getAddrMoareData() {
        return addrMoareData;
    }

    public void setAddrMoareData(ArrayList<AddrMoare> addrMoareData) {
        this.addrMoareData = addrMoareData;
    }

    @Override
    public String toString() {
        return "AddrMoareData{" +
                "addrMoareData=" + addrMoareData +
                '}';
    }
}
