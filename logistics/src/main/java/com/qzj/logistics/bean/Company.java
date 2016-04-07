package com.qzj.logistics.bean;

import com.qzj.logistics.base.BaseBean;
import com.qzj.logistics.bean.impl.SpinnerAdapterIf;
import com.qzj.logistics.templete.BaseBill;

/**
 * 快递公司
 */
public class Company extends BaseBean implements SpinnerAdapterIf {

    private int company_id;
    private String company_name;
    // 对应的快递单
    private Class<? extends BaseBill> billClazz;

    public int getCompany_id() {
        return company_id;
    }

    public void setCompany_id(int company_id) {
        this.company_id = company_id;
    }

    public String getCompany_name() {
        return company_name;
    }

    public void setCompany_name(String company_name) {
        this.company_name = company_name;
    }

    public Class<? extends BaseBill> getBillClazz() {
        return billClazz;
    }

    public void setBillClazz(Class<? extends BaseBill> billClazz) {
        this.billClazz = billClazz;
    }

    @Override
    public String findSpinnerText() {
        return company_name;
    }

    @Override
    public int findSpinnerId() {
        return company_id;
    }

    @Override
    public BaseBill findBill() {
       if (billClazz != null){
           try {
               return billClazz.newInstance();
           } catch (InstantiationException e) {
               e.printStackTrace();
           } catch (IllegalAccessException e) {
               e.printStackTrace();
           }
       }

        return null;
    }

    @Override
    public String toString() {
        return "Company{" +
                "company_id=" + company_id +
                ", company_name='" + company_name + '\'' +
                '}';
    }
}
