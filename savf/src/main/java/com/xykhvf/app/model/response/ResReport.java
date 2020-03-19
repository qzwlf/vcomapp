package com.xykhvf.app.model.response;

public class ResReport {
    /**
     * {
     * "pay_type": "200",
     * "pay_string": "POST扫一扫",
     * "amount": "230.03"
     * }
     */
    private String pay_type;
    private String pay_string;
    private Double amount;
    private Integer count;

    public String getPay_type() {
        return pay_type;
    }

    public void setPay_type(String pay_type) {
        this.pay_type = pay_type;
    }

    public String getPay_string() {
        return pay_string;
    }

    public void setPay_string(String pay_string) {
        this.pay_string = pay_string;
    }

    public Double getAmount() {
        return amount;
    }

    public void setAmount(Double amount) {
        this.amount = amount;
    }

    public Integer getCount() {
        return count;
    }

    public void setCount(Integer count) {
        this.count = count;
    }
}
