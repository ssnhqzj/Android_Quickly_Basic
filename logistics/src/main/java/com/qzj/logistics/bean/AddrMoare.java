package com.qzj.logistics.bean;

import android.content.Context;

import com.qzj.logistics.base.BaseBean;
import com.qzj.logistics.bean.impl.SpinnerAdapterIf;
import com.qzj.logistics.db.dao.HatAreaDao;
import com.qzj.logistics.templete.BaseBill;

/**
 * Created by Administrator on 2015/12/30.
 */
public class AddrMoare extends BaseBean implements Cloneable,SpinnerAdapterIf {

    private int id;
    private int addr_id;
    private String addr;
    private String cid;
    private String rid;
    private String mail_code;
    private String name;
    // state=2为默认地址
    private int state = 1;
    private String telephone;
    private int user_id;

    private String pName;
    private String cName;
    private String rName;
    private String selectedArea;
    // 组标题
    private String groupTitle;
    // 收件人信息串
    private String groupMsg;

    public int getAddr_id() {
        return addr_id;
    }

    public void setAddr_id(int addr_id) {
        this.addr_id = addr_id;
    }

    public String getAddr() {
        return addr;
    }

    public void setAddr(String addr) {
        this.addr = addr;
    }

    public String getCid() {
        return cid;
    }

    public void setCid(String cid) {
        this.cid = cid;
    }

    public String getRid() {
        return rid;
    }

    public void setRid(String rid) {
        this.rid = rid;
    }

    public String getMail_code() {
        return mail_code;
    }

    public void setMail_code(String mail_code) {
        this.mail_code = mail_code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getState() {
        return state;
    }

    public void reSetState(boolean isChecked) {
        this.state = isChecked?2:1;
    }

    public boolean isDefault() {
        return state==2?true:false;
    }

    public void setState(int state) {
        this.state = state;
    }

    public String getTelephone() {
        return telephone;
    }

    public void setTelephone(String telephone) {
        this.telephone = telephone;
    }

    public int getUser_id() {
        return user_id;
    }

    public void setUser_id(int user_id) {
        this.user_id = user_id;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getGroupTitle() {
        return groupTitle;
    }

    public void setGroupTitle(String groupTitle) {
        this.groupTitle = groupTitle;
    }

    public void setGroupMsg(String groupMsg) {
        this.groupMsg = groupMsg;
    }

    public String getpName() {
        return pName;
    }

    public void setpName(String pName) {
        this.pName = pName;
    }

    public String getrName() {
        return rName;
    }

    public void setrName(String rName) {
        this.rName = rName;
    }

    public String getcName() {
        return cName;
    }

    public void setcName(String cName) {
        this.cName = cName;
    }

    // 完整发货人信息
    public String getGroupMsg() {
        groupMsg = "";
        groupMsg += (name==null?"":name);
        groupMsg += (telephone==null?"":"," +telephone);
        groupMsg += (getFullAddr()==null?"":"," +getFullAddr());
        groupMsg += (mail_code==null?"":"," +mail_code);
        if (groupMsg.startsWith(",")) return groupMsg.substring(1);
        return groupMsg;
    }

    // 完整地址
    public String getFullAddr(){
        String fullAddr = "";
        if (selectedArea != null)
            fullAddr += selectedArea;
        if (addr != null)
            fullAddr += addr;

        return fullAddr;
    }

    public String getSelectedArea() {
        return selectedArea;
    }

    public void setSelectedArea(String selectedArea) {
        this.selectedArea = selectedArea;
    }

    @Override
    public AddrMoare clone() {
        AddrMoare o = null;
        try {
            o = (AddrMoare) super.clone();
        } catch (CloneNotSupportedException e) {
            e.printStackTrace();
        }
        return o;
    }

    @Override
    public String findSpinnerText() {
        return name;
    }

    @Override
    public int findSpinnerId() {
        return user_id;
    }

    @Override
    public BaseBill findBill() {
        return null;
    }

    /**
     * 查询发件人完整地址
     * @param context
     * @return
     */
    public String findAddress(Context context) {
        String[] pcr = new HatAreaDao(context).queryPcr(rid);
        String result = "";
        if (pcr != null) {
            String joinAddr = "";
            if (pcr.length > 0) {
                pName = pcr[0];
                joinAddr += pName;
            }
            if (pcr.length > 1) {
                cName = pcr[1];
                joinAddr += cName;
            }
            if (pcr.length > 2) {
                rName = pcr[2];
                joinAddr += rName;
            }

            setSelectedArea(joinAddr);
            result = joinAddr + addr;
        }

        return result;
    }

    @Override
    public String toString() {
        return "AddrMoare{" +
                "id=" + id +
                ", addr_id=" + addr_id +
                ", addr='" + addr + '\'' +
                ", cid='" + cid + '\'' +
                ", rid='" + rid + '\'' +
                ", mail_code='" + mail_code + '\'' +
                ", name='" + name + '\'' +
                ", state=" + state +
                ", telephone='" + telephone + '\'' +
                ", user_id=" + user_id +
                ", pName='" + pName + '\'' +
                ", cName='" + cName + '\'' +
                ", rName='" + rName + '\'' +
                ", selectedArea='" + selectedArea + '\'' +
                ", groupTitle='" + groupTitle + '\'' +
                ", groupMsg='" + groupMsg + '\'' +
                '}';
    }

}
