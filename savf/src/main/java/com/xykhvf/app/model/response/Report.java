package com.xykhvf.app.model.response;

public class Report {
    private int bank_count;
    private double bank_amount;
    private int sao_count;
    private double sao_amount;
    private int cash_count;
    private double cash_amount;
    private int online_count;
    private double online_amount;

    public int getBank_count() {
        return bank_count;
    }

    public void setBank_count(int bank_count) {
        this.bank_count = bank_count;
    }

    public double getBank_amount() {
        return bank_amount;
    }

    public void setBank_amount(double bank_amount) {
        this.bank_amount = bank_amount;
    }

    public int getSao_count() {
        return sao_count;
    }

    public void setSao_count(int sao_count) {
        this.sao_count = sao_count;
    }

    public double getSao_amount() {
        return sao_amount;
    }

    public void setSao_amount(double sao_amount) {
        this.sao_amount = sao_amount;
    }

    public int getCash_count() {
        return cash_count;
    }

    public void setCash_count(int cash_count) {
        this.cash_count = cash_count;
    }

    public double getCash_amount() {
        return cash_amount;
    }

    public void setCash_amount(double cash_amount) {
        this.cash_amount = cash_amount;
    }

    public int getOnline_count() {
        return online_count;
    }

    public void setOnline_count(int online_count) {
        this.online_count = online_count;
    }

    public double getOnline_amount() {
        return online_amount;
    }

    public void setOnline_amount(double online_amount) {
        this.online_amount = online_amount;
    }
}
