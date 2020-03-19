package com.xykhvf.app.presenter;

import android.content.Context;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.vcom.publiclibrary.util.okhttp.callback.StringCallback;
import com.vcom.publiclibrary.utils.SharedPreferencesUtils;
import com.vcom.publiclibrary.utils.SysEnv;
import com.vcom.publiclibrary.utils.WifiLManager;
import com.xykhvf.app.Factory;
import com.xykhvf.app.model.Result;
import com.xykhvf.app.model.request.ReqLoginModel;
import com.xykhvf.app.model.response.Dispense;
import com.xykhvf.app.model.response.ResLoginModel;
import com.xykhvf.app.util.Constants;
import com.xykhvf.app.view.ILoginView;

import java.util.List;

import okhttp3.Call;

/**
 * Created by Administrator on 2017-08-28.
 */
public class LoginPresenter {
    private static final String TAG = "LoginPresenter";
    private Context mContext = null;
    private ILoginView mLoginView = null;
    private Gson mGson = new GsonBuilder().serializeNulls()
            .setDateFormat("yyyy-MM-dd HH:mm:ss").create();

    public LoginPresenter(Context context, ILoginView view) {
        mContext = context;
        mLoginView = view;
    }

    public void Login(String loginName, String loginPwd) {
        ReqLoginModel model = new ReqLoginModel();
        model.setLoginname(loginName);
        model.setLoginpwd(loginPwd);
        SysEnv.SETTING_MODEL.setLoginName(loginName);
        SysEnv.SETTING_MODEL.setLoginPwd(loginPwd);
        Factory.getIApiDaoInstance().login(model, new StringCallback() {

            @Override
            public void onError(Call call, Exception e) {
                mLoginView.loginResult(new Result<ResLoginModel>().error("登录失败"));
            }

            @Override
            public void onResponse(String response) {
                Log.i(TAG, "onResponse: " + response);
                mLoginView.loginResult(mGson.fromJson(response, new TypeToken<Result<ResLoginModel>>() {
                }.getType()));
            }
        });

    }

    public void getUri() {
        Factory.getIApiDaoInstance().getUri(new StringCallback() {
            @Override
            public void onError(Call call, Exception e) {
                Log.i(TAG, "onError: " + e.getMessage());
            }

            @Override
            public void onResponse(String response) {
                Log.i(TAG, "onResponse: " + response);
                Result<List<Dispense>> result = mGson.fromJson(response, new TypeToken<Result<List<Dispense>>>() {
                }.getType());
                if (result.getCode() == 0 && result.getData() != null) {
                    mLoginView.setList(result.getData());
                }
            }
        });
    }

    
}
