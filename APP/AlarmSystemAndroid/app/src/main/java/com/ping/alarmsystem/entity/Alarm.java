package com.ping.alarmsystem.entity;

import java.util.Date;

public class Alarm {
    private Integer alarmId;

    private String createTime;

    private String alarmTime;

    private String alarmAddress;

    private Integer userId;

    private String userName;

    private Integer alarmStatus;

    private String updateTime;

    private String alarmImage;

    private String alarmRemark;

    private String completeTime;

    public String getAlarmRemark() {
        return alarmRemark;
    }

    public void setAlarmRemark(String alarmRemark) {
        this.alarmRemark = alarmRemark;
    }

    public String getCompleteTime() {
        return completeTime;
    }

    public void setCompleteTime(String completeTime) {
        this.completeTime = completeTime;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public Integer getAlarmId() {
        return alarmId;
    }

    public void setAlarmId(Integer alarmId) {
        this.alarmId = alarmId;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public String getAlarmTime() {
        return alarmTime;
    }

    public void setAlarmTime(String alarmTime) {
        this.alarmTime = alarmTime;
    }

    public String getAlarmAddress() {
        return alarmAddress;
    }

    public void setAlarmAddress(String alarmAddress) {
        this.alarmAddress = alarmAddress == null ? null : alarmAddress.trim();
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public Integer getAlarmStatus() {
        return alarmStatus;
    }

    public void setAlarmStatus(Integer alarmStatus) {
        this.alarmStatus = alarmStatus;
    }

    public String getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(String updateTime) {
        this.updateTime = updateTime;
    }

    public String getAlarmImage() {
        return alarmImage;
    }

    public void setAlarmImage(String alarmImage) {
        this.alarmImage = alarmImage == null ? null : alarmImage.trim();
    }
}