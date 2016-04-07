package com.qzj.logistics.bean;

import android.graphics.drawable.Drawable;

import com.qzj.logistics.base.BaseBean;


/**
 * 设置item实体
 */
public class SettingItem extends BaseBean {

    private int itemId;
    private String itemName;
    private String itemData;
    private Drawable itemDataIcon;
    private boolean isShowTopMargin;
    private boolean isShowRedTip;

    public int getItemId() {
        return itemId;
    }

    public void setItemId(int itemId) {
        this.itemId = itemId;
    }

    public String getItemName() {
        return itemName;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

    public String getItemData() {
        return itemData;
    }

    public void setItemData(String itemData) {
        this.itemData = itemData;
    }

    public Drawable getItemDataIcon() {
        return itemDataIcon;
    }

    public void setItemDataIcon(Drawable itemDataIcon) {
        this.itemDataIcon = itemDataIcon;
    }

    public boolean isShowTopMargin() {
        return isShowTopMargin;
    }

    public void setIsShowTopMargin(boolean isShowTopMargin) {
        this.isShowTopMargin = isShowTopMargin;
    }

    public boolean isShowRedTip() {
        return isShowRedTip;
    }

    public void setIsShowRedTip(boolean isShowRedTip) {
        this.isShowRedTip = isShowRedTip;
    }

    @Override
    public String toString() {
        return "SettingItem{" +
                "itemId=" + itemId +
                ", itemName='" + itemName + '\'' +
                ", itemData='" + itemData + '\'' +
                ", itemDataIcon=" + itemDataIcon +
                ", isShowTopMargin=" + isShowTopMargin +
                ", isShowRedTip=" + isShowRedTip +
                '}';
    }
}
