package com.vcom.publiclibrary.model;

import com.github.yoojia.anyversion.Version;

public class VersionExt extends Version {
    private String clientNo;

    public String getClientNo() {
        return clientNo;
    }

    public void setClientNo(String clientNo) {
        this.clientNo = clientNo;
    }

    public VersionExt(String name, String note, String url, int code) {
        super(name, note, url, code);
    }
}
