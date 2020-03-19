package com.vcom.publiclibrary.util;

import java.io.IOException;

import okhttp3.FormBody;
import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by Administrator on 2018-03-23.
 * @description: 请求拦截器(设置请求头以及一些公共参数)
 */

public class RequestInterceptor implements Interceptor {
    @Override
    public Response intercept(Chain chain) throws IOException {
        Request original = chain.request();
        //请求定制：添加请求头
        Request.Builder requestBuilder = original
                .newBuilder()
                .addHeader("Content-Type", "application/x-www-form-urlencoded; charset=UTF-8");
        //set Cookie
       /* String sessionId = App.getSessionId();
        if (StringUtil.checkStr(sessionId)) {
            requestBuilder.addHeader("Cookie", "PHPSESSID=" + sessionId);
        }*/
        //请求体定制：统一添加参数
        if (original.body() instanceof FormBody) {
            FormBody.Builder newFormBody = new FormBody.Builder();
            FormBody oidFormBody = (FormBody) original.body();
            for (int i = 0; i < oidFormBody.size(); i++) {
                newFormBody.addEncoded(oidFormBody.encodedName(i), oidFormBody.encodedValue(i));
            }
            requestBuilder.method(original.method(), newFormBody.build());
        }
        return chain.proceed(requestBuilder.build());
    }
}
