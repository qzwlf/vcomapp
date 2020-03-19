package com.xykhvf.app.util;


import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;
import com.vcom.publiclibrary.utils.SysEnv;
import com.xykhvf.app.Factory;
import com.xykhvf.app.model.request.ReqLoginModel;

import java.io.IOException;
import java.lang.reflect.Type;


import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by Administrator on 2017-10-22.
 */
public class TokenInterceptor implements Interceptor {
    private static final String TAG = TokenInterceptor.class.getName();
    private Integer mTryTimes = 0;
    private Gson mGson = new GsonBuilder().serializeNulls()
            .registerTypeAdapter(Double.class, new JsonSerializer<Double>() {
                @Override
                public JsonElement serialize(Double src, Type typeOfSrc, JsonSerializationContext context) {
                    if (src == src.longValue())
                        return new JsonPrimitive(src.longValue());
                    return new JsonPrimitive(src);
                }
            })
            .setDateFormat("yyyy-MM-dd HH:mm:ss").create();

    @Override
    public Response intercept(Chain chain) throws IOException {
        Request request = chain.request();
        Response response = chain.proceed(request);
        Log.d(TAG, "response.code=" + response.code());

        //根据和服务端的约定判断token过期
        if (isTokenExpired(response) && mTryTimes < 3) {
            mTryTimes++;
            Log.d(TAG, "自动刷新Token,然后重新请求数据");
            //同步请求方式，获取最新的Token
            String token = getNewToken();
            //使用新的Token，创建新的请求
            Request newRequest = chain.request()
                    .newBuilder()
                    .header("authorization", token)
                    .build();
            //重新请求
            return chain.proceed(newRequest);
        }
        mTryTimes = 0;
        return response;
    }

    private boolean isTokenExpired(Response response) {
        if (response.code() == 500) {
            return false;
        }
        return false;
    }

    private String getNewToken() {
        ReqLoginModel model = new ReqLoginModel();
        model.setLoginname(SysEnv.SETTING_MODEL.getLoginName());
        model.setLoginpwd(SysEnv.SETTING_MODEL.getLoginPwd());
        return Factory.getIApiDaoInstance().loginSync(model);
    }
}
