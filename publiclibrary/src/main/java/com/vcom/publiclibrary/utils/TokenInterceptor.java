package com.vcom.publiclibrary.utils;

import android.util.Log;


import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;
import com.google.gson.reflect.TypeToken;

import java.io.IOException;
import java.lang.reflect.Type;
import java.nio.charset.Charset;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;

import okhttp3.Interceptor;
import okhttp3.MediaType;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okhttp3.ResponseBody;
import okio.Buffer;
import okio.BufferedSource;

/**
 * Created by Administrator on 2017-10-22.
 */
public abstract class TokenInterceptor implements Interceptor {
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
        //如果是版本更新的请求无需判断session
        if (request.url().uri().getPath().indexOf(".txt") > -1 || request.url().uri().getPath().indexOf(".apk") > -1)
            return response;
        MediaType mediaType = response.body().contentType();
        String content = response.body().string();
        try {
            if (content.indexOf("过期") >= 0 || content.indexOf("超时") >= 0) {//根据和服务端的约定判断token过期          content.indexOf("过期") >
                Log.d("TokenInterceptor", "静默自动刷新Token,然后重新请求数据");
                //同步请求方式，获取最新的Token
                getNewToken();
                MediaType reqMediaType = request.body().contentType();
                RequestBody reqBody = request.body();
                Buffer buffer = new Buffer();
                reqBody.writeTo(buffer);
                Charset charset = Charset.forName("UTF-8");
                String paramsStr = buffer.readString(charset);
                Map<String, Object> map = mGson.fromJson(paramsStr, new TypeToken<HashMap<String, Object>>() {
                }.getType());
                map.remove("sessionKey");
                map.put("sessionKey", SysEnv.SETTING_MODEL.getSessionKey());
                String str = mGson.toJson(map);
                Request newRequest = request.newBuilder()
                        .post(RequestBody.create(reqMediaType, str))
                        .build();

                return chain.proceed(newRequest);
            }
        } catch (Exception e) {
            return response.newBuilder()
                    .body(ResponseBody.create(mediaType, content))
                    .build();
        }
        return response.newBuilder()
                .body(ResponseBody.create(mediaType, content))
                .build();
    }

    /**
     * 同步请求方式，获取最新的Token
     *
     * @return
     */
    public abstract void getNewToken();
}
