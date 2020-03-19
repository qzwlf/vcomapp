package com.xykhvf.app.model;

import java.io.Serializable;

public class PayResult implements Serializable {
    private String resCode;
    private String resDesc;

    public String getResCode() {
        return resCode;
    }

    public void setResCode(String resCode) {
        this.resCode = resCode;
    }

    public String getResDesc() {
        return resDesc;
    }

    public void setResDesc(String resDesc) {
        this.resDesc = resDesc;
    }
}
