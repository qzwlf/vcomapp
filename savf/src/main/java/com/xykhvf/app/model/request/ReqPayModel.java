package com.xykhvf.app.model.request;

import java.util.List;

public class ReqPayModel {
    private String orderid;
    private String ums_orderid;
    private String pay_type;
    private String amount;
    private String data;

    public String getOrderid() {
        return orderid;
    }

    public void setOrderid(String orderid) {
        this.orderid = orderid;
    }

    public String getUms_orderid() {
        return ums_orderid;
    }

    public void setUms_orderid(String ums_orderid) {
        this.ums_orderid = ums_orderid;
    }

    public String getPay_type() {
        return pay_type;
    }

    public void setPay_type(String pay_type) {
        this.pay_type = pay_type;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }
}
