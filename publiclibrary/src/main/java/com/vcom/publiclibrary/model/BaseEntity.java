package com.vcom.publiclibrary.model;

import java.io.Serializable;

/**
 * Created by Administrator on 2017-01-14.
 */
public class BaseEntity<T> implements Serializable {
    private String result;
    private String msg;
    private T data;

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
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
