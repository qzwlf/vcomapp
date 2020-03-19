package com.xykhvf.app.model.response;

import com.xykhvf.app.model.AreaModel;

import java.util.List;

public class UserModel {
    private Long login_id;
    private String log_uri;
    private List<AreaModel> areas;

    public Long getLogin_id() {
        return login_id;
    }

    public void setLogin_id(Long login_id) {
        this.login_id = login_id;
    }

    public String getLog_uri() {
        return log_uri;
    }

    public void setLog_uri(String log_uri) {
        this.log_uri = log_uri;
    }

    public List<AreaModel> getAreas() {
        return areas;
    }

    public void setAreas(List<AreaModel> areas) {
        this.areas = areas;
    }
}
