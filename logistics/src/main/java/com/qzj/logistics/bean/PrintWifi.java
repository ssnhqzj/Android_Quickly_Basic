package com.qzj.logistics.bean;

import java.io.Serializable;

/**
 * Created by Administrator on 2016/1/7.
 */
public class PrintWifi implements Serializable {

    private String ssid;
    private String pw;
    private String ip;
    private String port;
    private int pwMode;
    // 是否直连,true为直连
    private boolean checked;

    public String getSsid() {
        return ssid;
    }

    public void setSsid(String ssid) {
        this.ssid = ssid;
    }

    public String getPw() {
        return pw;
    }

    public void setPw(String pw) {
        this.pw = pw;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public String getPort() {
        return port;
    }

    public void setPort(String port) {
        this.port = port;
    }

    public boolean isChecked() {
        return checked;
    }

    public void setChecked(boolean checked) {
        this.checked = checked;
    }

    public int getPwMode() {
        return pwMode;
    }

    public void setPwMode(int pwMode) {
        this.pwMode = pwMode;
    }

    @Override
    public String toString() {
        return "PrintWifi{" +
                "ssid='" + ssid + '\'' +
                ", pw='" + pw + '\'' +
                ", ip='" + ip + '\'' +
                ", port='" + port + '\'' +
                ", pwMode='" + pwMode + '\'' +
                ", checked=" + checked +
                '}';
    }
}
