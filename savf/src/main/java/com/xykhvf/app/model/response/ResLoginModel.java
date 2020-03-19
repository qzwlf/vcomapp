package com.xykhvf.app.model.response;

public class ResLoginModel {
    private UserModel user_data;
    private String token;
    private Long expires_in;

    public UserModel getUser_data() {
        return user_data;
    }

    public void setUser_data(UserModel user_data) {
        this.user_data = user_data;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public Long getExpires_in() {
        return expires_in;
    }

    public void setExpires_in(Long expires_in) {
        this.expires_in = expires_in;
    }
}
