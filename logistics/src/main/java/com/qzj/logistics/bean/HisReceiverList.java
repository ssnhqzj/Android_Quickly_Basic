package com.qzj.logistics.bean;

import com.qzj.logistics.base.BaseBean;

import java.util.List;

/**
 * Created by Administrator on 2016/1/4.
 */
public class HisReceiverList extends BaseBean {

    private int dqpage;
    private int pagecount;
    private List<HisReceiver> hisReceiverList;

    public int getDqpage() {
        return dqpage;
    }

    public void setDqpage(int dqpage) {
        this.dqpage = dqpage;
    }

    public int getPagecount() {
        return pagecount;
    }

    public void setPagecount(int pagecount) {
        this.pagecount = pagecount;
    }

    public List<HisReceiver> getHisReceiverList() {
        return hisReceiverList;
    }

    public void setHisReceiverList(List<HisReceiver> hisReceiverList) {
        this.hisReceiverList = hisReceiverList;
    }

    @Override
    public String toString() {
        return "HisReceiverList{" +
                "dqpage=" + dqpage +
                ", pagecount=" + pagecount +
                ", hisReceiverList=" + hisReceiverList +
                '}';
    }
}
