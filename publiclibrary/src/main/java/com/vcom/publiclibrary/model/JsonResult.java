package com.vcom.publiclibrary.model;

import java.io.Serializable;

/**
 * Created by Lifa on 2016-01-27.
 */
public class JsonResult<T> implements Serializable {
    private int code;
    private String message;
    private T data;


    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

}
