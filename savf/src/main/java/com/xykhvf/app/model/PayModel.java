package com.xykhvf.app.model;

import java.io.Serializable;

public class PayModel implements Serializable {
    private String appId;
    private String extOrderNo;
    private String extBillNo;

    public String getAppId() {
        return appId;
    }

    public void setAppId(String appId) {
        this.appId = appId;
    }

    public String getExtOrderNo() {
        return extOrderNo;
    }

    public void setExtOrderNo(String extOrderNo) {
        this.extOrderNo = extOrderNo;
    }

    public String getExtBillNo() {
        return extBillNo;
    }

    public void setExtBillNo(String extBillNo) {
        this.extBillNo = extBillNo;
    }
}
