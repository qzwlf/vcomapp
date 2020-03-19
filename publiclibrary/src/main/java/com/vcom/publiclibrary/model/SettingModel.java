package com.vcom.publiclibrary.model;

import com.vcom.publiclibrary.utils.DateUtils;
import com.vcom.publiclibrary.utils.SharedPreferencesUtils;
import com.vcom.publiclibrary.utils.StringUtils;
import com.vcom.publiclibrary.utils.SysEnv;

import java.io.Serializable;
import java.util.Date;

/**
 * Created by Lifa on 2016-06-03 09:56.
 */
public class SettingModel implements Serializable {
    private long userId;
    private String userName;
    private String loginName; //用于sessionKey过期自动登录
    private String loginPwd;  //用于sessionKey过期自动登录
    private String serverUrl;
    private String updateUrl;
    private int batieryLevel;
    private String sessionKey;
    private String remenberName;//记住用户名
    private String remenberPwd; //记住密码
    private String clientMac;
    private boolean isLogin;
    private int scanType;
    private long expiresn;
    private String logUri;

    public SettingModel() {
        this.updateUrl = "http://mmc.e-bus.co/apk/citybusRelease.txt";
    }

    public String getSessionKey() {
        if (StringUtils.isEmpty(this.sessionKey))
            sessionKey = SharedPreferencesUtils.getValue(SysEnv.context, "sessionKey", "");
        return sessionKey;
    }

    public void setSessionKey(String sessionKey) {
        this.sessionKey = sessionKey;
        SharedPreferencesUtils.putValue(SysEnv.context, "sessionKey", sessionKey);
    }

    public void setLogin(boolean login) {
        isLogin = login;
        SharedPreferencesUtils.putValue(SysEnv.context, "isLogin", login);
    }

    public boolean isLogin() {
        return SharedPreferencesUtils.getValue(SysEnv.context, "isLogin", false);
    }

    public long getUserId() {
        if (userId == 0)
            userId = SharedPreferencesUtils.getValue(SysEnv.context, "userId", Long.parseLong("0"));
        return userId;
    }

    public void setUserId(long userId) {
        this.userId = userId;
        SharedPreferencesUtils.putValue(SysEnv.context, "userId", userId);
    }

    public String getUserName() {
        if (StringUtils.isEmpty(userName))
            userName = SharedPreferencesUtils.getValue(SysEnv.context, "userName", "");
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
        SharedPreferencesUtils.putValue(SysEnv.context, "userName", userName);
    }

    public String getLoginName() {
        if (StringUtils.isEmpty(loginName))
            loginName = SharedPreferencesUtils.getValue(SysEnv.context, "loginName", "");
        return loginName;
    }

    public void setLoginName(String loginName) {
        this.loginName = loginName;
        SharedPreferencesUtils.putValue(SysEnv.context, "loginName", loginName);
    }

    public String getLoginPwd() {
        if (StringUtils.isEmpty(loginPwd))
            loginPwd = SharedPreferencesUtils.getValue(SysEnv.context, "loginPwd", "");
        return loginPwd;
    }

    public void setLoginPwd(String loginPwd) {
        this.loginPwd = loginPwd;
        SharedPreferencesUtils.putValue(SysEnv.context, "loginPwd", loginPwd);
    }


    public String getServerUrl() {
        if (StringUtils.isEmpty(serverUrl))
            serverUrl = SharedPreferencesUtils.getValue(SysEnv.context, "serverUrl", "http://wapi.ck.cc/api/");
        return serverUrl;
    }

    public void setServerUrl(String serverUrl) {
        this.serverUrl = serverUrl;
        SharedPreferencesUtils.putValue(SysEnv.context, "serverUrl", serverUrl);
    }

    public String getUpdateUrl() {
        if (StringUtils.isEmpty("updateUrl"))
            updateUrl = SharedPreferencesUtils.getValue(SysEnv.context, "updateUrl", "http://mmc.e-bus.co/apk/citybusRelease.txt");
        return updateUrl;
    }

    public void setUpdateUrl(String updateUrl) {
        this.updateUrl = updateUrl;
        SharedPreferencesUtils.putValue(SysEnv.context, "updateUrl", updateUrl);
    }

    public int getBatieryLevel() {
        if (batieryLevel != 0)
            return batieryLevel;
        else
            return SharedPreferencesUtils.getValue(SysEnv.context, "batieryLevel", 0);
    }

    public void setBatieryLevel(int batieryLevel) {
        this.batieryLevel = batieryLevel;
        SharedPreferencesUtils.putValue(SysEnv.context, "batieryLevel", batieryLevel);
    }

    public String getRemenberName() {
        return SharedPreferencesUtils.getValue(SysEnv.context, "remenberName", "");
    }

    public void setRemenberName(String remenberName) {
        this.remenberName = remenberName;
        SharedPreferencesUtils.putValue(SysEnv.context, "remenberName", remenberName);
    }

    public String getRemenberPwd() {
        return SharedPreferencesUtils.getValue(SysEnv.context, "remenberPwd", "");
    }

    public void setRemenberPwd(String remenberPwd) {
        this.remenberPwd = remenberPwd;
        SharedPreferencesUtils.putValue(SysEnv.context, "remenberPwd", remenberPwd);
    }

    public String getClientMac() {
        return SharedPreferencesUtils.getValue(SysEnv.context, "clientMac", "");
    }

    public void setClientMac(String clientMac) {
        this.clientMac = clientMac;
        SharedPreferencesUtils.putValue(SysEnv.context, "clientMac", clientMac);

    }

    public int getScanType() {
        return SharedPreferencesUtils.getValue(SysEnv.context, "scanType", 0);
    }

    public void setScanType(int scanType) {
        this.scanType = scanType;
        SharedPreferencesUtils.putValue(SysEnv.context, "scanType", scanType);
    }


    public long getExpiresn() {
        return expiresn;
    }

    public void setExpiresn(long expiresn) {
        this.expiresn = expiresn;
    }

    public String getLogUri() {
        return logUri;
    }

    public void setLogUri(String logUri) {
        this.logUri = logUri;
    }
}
