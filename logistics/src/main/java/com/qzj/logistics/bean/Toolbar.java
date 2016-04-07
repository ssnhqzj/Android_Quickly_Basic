package com.qzj.logistics.bean;

import com.qzj.logistics.base.BaseBean;

/**
 * toolbar bean
 */
public class Toolbar extends BaseBean{

    // toolbar左侧资源ID和文本
    private int leftResId;
    private int leftBgResId;
    private String leftText;

    // toolbar中间资源ID和文本
    private int centerResId;
    private int centerBgResId;
    private String centerText;

    // toolbar右侧资源ID和文本
    private int rightResId;
    private int rightBgResId;
    private String rightText;

    // 背景色
    private int bg;

    public Toolbar() {}

    public Toolbar(String centerText, int rightResId) {
        this.centerText = centerText;
        this.rightResId = rightResId;
    }

    public Toolbar(int leftResId, String centerText) {
        this.leftResId = leftResId;
        this.centerText = centerText;
    }

    public void setLeftResId(int leftResId) {
        this.leftResId = leftResId;
    }

    public void setLeftText(String leftText) {
        this.leftText = leftText;
    }

    public void setCenterResId(int centerResId) {
        this.centerResId = centerResId;
    }

    public void setCenterText(String centerText) {
        this.centerText = centerText;
    }

    public void setRightResId(int rightResId) {
        this.rightResId = rightResId;
    }

    public void setRightText(String rightText) {
        this.rightText = rightText;
    }

    public void setBg(int bg) {
        this.bg = bg;
    }

    public int getLeftResId() {
        return leftResId;
    }

    public String getLeftText() {
        return leftText;
    }

    public int getCenterResId() {
        return centerResId;
    }

    public String getCenterText() {
        return centerText;
    }

    public int getRightResId() {
        return rightResId;
    }

    public String getRightText() {
        return rightText;
    }

    public int getBg() {
        return bg;
    }

    public int getRightBgResId() {
        return rightBgResId;
    }

    public int getLeftBgResId() {
        return leftBgResId;
    }

    public void setLeftBgResId(int leftBgResId) {
        this.leftBgResId = leftBgResId;
    }

    public int getCenterBgResId() {
        return centerBgResId;
    }

    public void setCenterBgResId(int centerBgResId) {
        this.centerBgResId = centerBgResId;
    }

    public void setRightBgResId(int rightBgResId) {
        this.rightBgResId = rightBgResId;

    }
}
