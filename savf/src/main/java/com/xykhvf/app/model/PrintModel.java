package com.xykhvf.app.model;

import java.util.Date;

public class PrintModel {
    private String orderNo;
    private String mchName;
    private String roomName;
    private String feeDetail;
    private String payTime;
    private String payType;
    private String amount;

    public String getOrderNo() {
        return orderNo;
    }

    public void setOrderNo(String orderNo) {
        this.orderNo = orderNo;
    }

    public String getMchName() {
        return mchName;
    }

    public void setMchName(String mchName) {
        this.mchName = mchName;
    }

    public String getRoomName() {
        return roomName;
    }

    public void setRoomName(String roomName) {
        this.roomName = roomName;
    }

    public String getFeeDetail() {
        return feeDetail;
    }

    public void setFeeDetail(String feeDetail) {
        this.feeDetail = feeDetail;
    }

    public String getPayTime() {
        return payTime;
    }

    public void setPayTime(String payTime) {
        this.payTime = payTime;
    }

    public String getPayType() {
        return payType;
    }

    public void setPayType(String payType) {
        this.payType = payType;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }
}
