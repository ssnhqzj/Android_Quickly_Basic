package com.qzj.logistics.bean.impl;

import com.qzj.logistics.templete.BaseBill;

/**
 * spinner adapter实体接口
 */
public interface SpinnerAdapterIf {
    /**
     * 返回显示在spinner中的item的文字
     * @return
     */
    public String findSpinnerText();

    /**
     * 返回每个item的唯一id
     * @return
     */
    public int findSpinnerId();

    /**
     * 返回快递公司对应的快递单，只针对Company对象
     * @return
     */
    public BaseBill findBill();

}
