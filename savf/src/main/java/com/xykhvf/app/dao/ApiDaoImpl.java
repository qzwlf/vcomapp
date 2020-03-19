package com.xykhvf.app.dao;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.vcom.publiclibrary.util.okhttp.OkHttpUtils;
import com.vcom.publiclibrary.util.okhttp.callback.StringCallback;
import com.vcom.publiclibrary.utils.SharedPreferencesUtils;
import com.vcom.publiclibrary.utils.SysEnv;
import com.xykhvf.app.model.Result;
import com.xykhvf.app.model.request.ReqLoginModel;
import com.xykhvf.app.model.request.ReqPayModel;
import com.xykhvf.app.model.request.ReqReport;
import com.xykhvf.app.model.response.ResLoginModel;
import com.xykhvf.app.util.Constants;

import java.io.IOException;

import okhttp3.MediaType;
import okhttp3.Response;

public class ApiDaoImpl implements IApiDao {
    private static Gson mGson = new GsonBuilder().serializeNulls()
            .setDateFormat("yyyy-MM-dd HH:mm:ss").create();
    private final static MediaType JSON = MediaType.parse("application/json; charset=utf8");
    private final static String BASEURL = "https://xykjvf.com/";

    @Override
    public void getUri(StringCallback callback) {
        String url = "https://xykjvf.com/dispense/index/index";
        OkHttpUtils.get().url(url).build().execute(callback);
    }

    @Override
    public void login(ReqLoginModel model, StringCallback callback) {
        String url = SharedPreferencesUtils.getValue(SysEnv.context, Constants.URI) + "/api/common/login";
        OkHttpUtils.postString().url(url).mediaType(JSON).content(mGson.toJson(model))
                .build().execute(callback);
    }

    @Override
    public String loginSync(ReqLoginModel model) {
        String url = SharedPreferencesUtils.getValue(SysEnv.context, Constants.URI) + "/api/common/login";
        String token = "";
        try {
            Response response = OkHttpUtils.postString().url(url).mediaType(JSON).content(mGson.toJson(model))
                    .build().execute();
            Result<ResLoginModel> result = mGson.fromJson(response.body().string(), new TypeToken<Result<ResLoginModel>>() {
            }.getType());
            SysEnv.SETTING_MODEL.setLogin(true);
            SysEnv.SETTING_MODEL.setSessionKey(result.getData().getToken());
            SysEnv.SETTING_MODEL.setExpiresn(result.getData().getExpires_in());
            SharedPreferencesUtils.putValue(SysEnv.context, Constants.TOKEN, result.getData().getToken());
            token = result.getData().getToken();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return token;
    }

    @Override
    public void getBuilding(Long id, String token, StringCallback callback) {
        String url = SharedPreferencesUtils.getValue(SysEnv.context, Constants.URI) + "/balconys/" + id;
        OkHttpUtils.get().url(url).addHeader("authorization", token).build().execute(callback);
    }

    @Override
    public void getHouses(Long id, String token, StringCallback callback) {
        String url = SharedPreferencesUtils.getValue(SysEnv.context, Constants.URI) + "/houses_batch/" + id;
        OkHttpUtils.get().url(url).addHeader("authorization", token).build().execute(callback);
    }

    @Override
    public void getOrders(Long id, String token, StringCallback callback) {
        String url = SharedPreferencesUtils.getValue(SysEnv.context, Constants.URI) + "/order_detail/" + id;
        OkHttpUtils.get().url(url).addHeader("authorization", token).build().execute(callback);
    }

    @Override
    public void checkPay(String ids, String token, StringCallback callback) {
        String url = SharedPreferencesUtils.getValue(SysEnv.context, Constants.URI) + "/check_pay";
        OkHttpUtils.get().url(url).addHeader("authorization", token)
                .addParams("ids", ids).build().execute(callback);
    }

    @Override
    public void submitPay(ReqPayModel model, String token, StringCallback callback) {
        String url = SharedPreferencesUtils.getValue(SysEnv.context, Constants.URI) + "/pay_success";
        OkHttpUtils.postString().url(url).mediaType(JSON).addHeader("authorization", token).content(mGson.toJson(model)).build().execute(callback);
    }

    @Override
    public void getReport(ReqReport report, String token, StringCallback callback) {
        String url = SharedPreferencesUtils.getValue(SysEnv.context, Constants.URI) + "/order_stat";
        OkHttpUtils.postString().url(url).mediaType(JSON).addHeader("authorization", token).content(mGson.toJson(report)).build().execute(callback);
    }

    @Override
    public void getMerchat(String mac, String token, StringCallback callback) {
        String url = SharedPreferencesUtils.getValue(SysEnv.context, Constants.URI) + "/merchant/mac/" + mac;
        OkHttpUtils.get().url(url).addHeader("authorization", token)
                .build().execute(callback);
    }
}
