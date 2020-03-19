package com.xykhvf.app.model;

public class Result<T> {
    private Integer code;
    private String msg;
    private T data;

    public Result<T> error(String msg) {
        this.code = 9;
        this.msg = msg;
        return this;
    }

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

}
