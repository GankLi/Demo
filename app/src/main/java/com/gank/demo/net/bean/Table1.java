package com.gank.demo.net.bean;

public class Table1 {
    private String DeviceNo;

    private String deviceStatus;

    private String paperStatus;

    public void setDeviceNo(String DeviceNo){
        this.DeviceNo = DeviceNo;
    }
    public String getDeviceNo(){
        return this.DeviceNo;
    }
    public void setDeviceStatus(String deviceStatus){
        this.deviceStatus = deviceStatus;
    }
    public String getDeviceStatus(){
        return this.deviceStatus;
    }
    public void setPaperStatus(String paperStatus){
        this.paperStatus = paperStatus;
    }
    public String getPaperStatus(){
        return this.paperStatus;
    }

}
