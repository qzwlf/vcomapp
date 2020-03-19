package com.vcom.publiclibrary.util.okhttp;

import android.os.Handler;
import android.os.Looper;

import com.vcom.publiclibrary.util.okhttp.builder.GetBuilder;
import com.vcom.publiclibrary.util.okhttp.builder.HeadBuilder;
import com.vcom.publiclibrary.util.okhttp.builder.OtherRequestBuilder;
import com.vcom.publiclibrary.util.okhttp.builder.PostFileBuilder;
import com.vcom.publiclibrary.util.okhttp.builder.PostFormBuilder;
import com.vcom.publiclibrary.util.okhttp.builder.PostStringBuilder;
import com.vcom.publiclibrary.util.okhttp.callback.Callback;
import com.vcom.publiclibrary.util.okhttp.cookie.CookieJarImpl;
import com.vcom.publiclibrary.util.okhttp.cookie.store.CookieStore;
import com.vcom.publiclibrary.util.okhttp.cookie.store.HasCookieStore;
import com.vcom.publiclibrary.util.okhttp.cookie.store.MemoryCookieStore;
import com.vcom.publiclibrary.util.okhttp.log.LoggerInterceptor;
import com.vcom.publiclibrary.util.okhttp.request.RequestCall;
import com.vcom.publiclibrary.util.okhttp.utils.Exceptions;
import com.vcom.publiclibrary.utils.SysEnv;
import com.vcom.publiclibrary.utils.TokenInterceptor;

import java.io.IOException;
import java.io.InputStream;
import java.math.BigInteger;
import java.security.KeyStore;
import java.security.PublicKey;
import java.security.SecureRandom;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;
import java.util.concurrent.TimeUnit;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;
import javax.net.ssl.TrustManagerFactory;
import javax.net.ssl.X509TrustManager;

import okhttp3.Call;
import okhttp3.CookieJar;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Response;

/**
 * Created by zhy on 15/8/17.
 */
public class OkHttpUtils {

    public static final long DEFAULT_MILLISECONDS = 10000;
    private static OkHttpUtils mInstance;
    private OkHttpClient mOkHttpClient;
    private Handler mDelivery;

    public OkHttpUtils(OkHttpClient okHttpClient) {
        if (okHttpClient == null) {
            OkHttpClient.Builder okHttpClientBuilder = new OkHttpClient.Builder();
            //cookie enabled
            okHttpClientBuilder.cookieJar(new CookieJarImpl(new MemoryCookieStore()));
            okHttpClientBuilder.sslSocketFactory(createSSLSocketFactory());
            okHttpClientBuilder.hostnameVerifier(new HostnameVerifier() {
                @Override
                public boolean verify(String hostname, SSLSession session) {
                    return true;
                }
            });

            mOkHttpClient = okHttpClientBuilder.build();
        } else {
            mOkHttpClient = okHttpClient;
        }

        init();
    }

    public OkHttpUtils(OkHttpClient okHttpClient, Interceptor interceptor) {
        if (okHttpClient == null) {
            OkHttpClient.Builder okHttpClientBuilder = new OkHttpClient.Builder();
            okHttpClientBuilder.addNetworkInterceptor(interceptor);
            okHttpClientBuilder.cookieJar(new CookieJarImpl(new MemoryCookieStore()));
            okHttpClientBuilder.hostnameVerifier(new HostnameVerifier() {
                @Override
                public boolean verify(String hostname, SSLSession session) {
                    return true;
                }
            });

            mOkHttpClient = okHttpClientBuilder.build();
        } else {
            mOkHttpClient = okHttpClient;
        }

        init();
    }

    private void init() {
        mDelivery = new Handler(Looper.getMainLooper());
    }

    /**
     * 生成安全套接字工厂，用于https请求的证书跳过
     *
     * @return
     */
    private SSLSocketFactory createSSLSocketFactory() {
        SSLSocketFactory ssfFactory = null;
        try {
            SSLContext sc = SSLContext.getInstance("TLS");
            sc.init(null, new TrustManager[]{new TrustAllCerts()}, new SecureRandom());
            ssfFactory = sc.getSocketFactory();
        } catch (Exception e) {
        }
        return ssfFactory;
    }

    public OkHttpUtils debug(String tag) {
        mOkHttpClient = getOkHttpClient().newBuilder().addInterceptor(new LoggerInterceptor(tag, false)).build();
        return this;
    }

    public OkHttpUtils Token(TokenInterceptor interceptor) {
        mOkHttpClient = getOkHttpClient().newBuilder().addInterceptor(interceptor)
                .build();
        return this;
    }

    public OkHttpUtils Token(Interceptor interceptor) {
        mOkHttpClient = getOkHttpClient().newBuilder().addInterceptor(interceptor)
                .build();
        return this;
    }

    /**
     * showResponse may cause error, but you can try .
     *
     * @param tag
     * @param showResponse
     * @return
     */
    public OkHttpUtils debug(String tag, boolean showResponse) {
        mOkHttpClient = getOkHttpClient().newBuilder().addInterceptor(new LoggerInterceptor(tag, showResponse)).build();
        return this;
    }

    public static OkHttpUtils getInstance(OkHttpClient okHttpClient) {
        if (mInstance == null) {
            synchronized (OkHttpUtils.class) {
                if (mInstance == null) {
                    mInstance = new OkHttpUtils(okHttpClient);
                }
            }
        }
        return mInstance;
    }

    public static OkHttpUtils getInstance() {
        if (mInstance == null) {
            synchronized (OkHttpUtils.class) {
                if (mInstance == null) {
                    mInstance = new OkHttpUtils(null);
                }
            }
        }
        return mInstance;
    }

    public static OkHttpUtils getInstance(TokenInterceptor interceptor) {
        if (mInstance == null) {
            synchronized (OkHttpUtils.class) {
                if (mInstance == null) {
                    mInstance = new OkHttpUtils(null, interceptor);
                }
            }
        }
        return mInstance;
    }


    public Handler getDelivery() {
        return mDelivery;
    }

    public OkHttpClient getOkHttpClient() {
        return mOkHttpClient;
    }

    public static GetBuilder get() {
        return new GetBuilder();
    }

    public static PostStringBuilder postString() {
        return new PostStringBuilder();
    }

    public static PostFileBuilder postFile() {
        return new PostFileBuilder();
    }

    public static PostFormBuilder post() {
        return new PostFormBuilder();
    }

    public static OtherRequestBuilder put() {
        return new OtherRequestBuilder(METHOD.PUT);
    }

    public static HeadBuilder head() {
        return new HeadBuilder();
    }

    public static OtherRequestBuilder delete() {
        return new OtherRequestBuilder(METHOD.DELETE);
    }

    public static OtherRequestBuilder patch() {
        return new OtherRequestBuilder(METHOD.PATCH);
    }

    public void execute(final RequestCall requestCall, Callback callback) {
        if (callback == null)
            callback = Callback.CALLBACK_DEFAULT;
        final Callback finalCallback = callback;

        requestCall.getCall().enqueue(new okhttp3.Callback() {
            @Override
            public void onFailure(Call call, final IOException e) {
                sendFailResultCallback(call, e, finalCallback);
            }

            @Override
            public void onResponse(final Call call, final Response response) {
                if (response.code() >= 400 && response.code() <= 599) {
                    try {
                        sendFailResultCallback(call, new RuntimeException(response.body().string()), finalCallback);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    return;
                }

                try {
                    Object o = finalCallback.parseNetworkResponse(response);
                    sendSuccessResultCallback(o, finalCallback);
                } catch (Exception e) {
                    sendFailResultCallback(call, e, finalCallback);
                }

            }
        });
    }

    public CookieStore getCookieStore() {
        final CookieJar cookieJar = mOkHttpClient.cookieJar();
        if (cookieJar == null) {
            Exceptions.illegalArgument("you should invoked okHttpClientBuilder.cookieJar() to set a cookieJar.");
        }
        if (cookieJar instanceof HasCookieStore) {
            return ((HasCookieStore) cookieJar).getCookieStore();
        } else {
            return null;
        }
    }


    public void sendFailResultCallback(final Call call, final Exception e, final Callback callback) {
        if (callback == null) return;

        mDelivery.post(new Runnable() {
            @Override
            public void run() {
                callback.onError(call, e);
                callback.onAfter();
            }
        });
    }

    public void sendSuccessResultCallback(final Object object, final Callback callback) {
        if (callback == null) return;
        mDelivery.post(new Runnable() {
            @Override
            public void run() {
                callback.onResponse(object);
                callback.onAfter();
            }
        });
    }

    public void cancelTag(Object tag) {
        for (Call call : mOkHttpClient.dispatcher().queuedCalls()) {
            if (tag.equals(call.request().tag())) {
                call.cancel();
            }
        }
        for (Call call : mOkHttpClient.dispatcher().runningCalls()) {
            if (tag.equals(call.request().tag())) {
                call.cancel();
            }
        }
    }





    public void setHostNameVerifier(HostnameVerifier hostNameVerifier) {
        mOkHttpClient = getOkHttpClient().newBuilder()
                .hostnameVerifier(hostNameVerifier)
                .build();
    }

    public void setConnectTimeout(int timeout, TimeUnit units) {
        mOkHttpClient = getOkHttpClient().newBuilder()
                .connectTimeout(timeout, units)
                .build();
    }

    public void setReadTimeout(int timeout, TimeUnit units) {
        mOkHttpClient = getOkHttpClient().newBuilder()
                .readTimeout(timeout, units)
                .build();
    }

    public void setRetryOnConnectionFailure(boolean flag) {
        mOkHttpClient = getOkHttpClient().newBuilder()
                .retryOnConnectionFailure(flag)
                .build();
    }

    public void setWriteTimeout(int timeout, TimeUnit units) {
        mOkHttpClient = getOkHttpClient().newBuilder()
                .writeTimeout(timeout, units)
                .build();
    }


    public static class METHOD {
        public static final String HEAD = "HEAD";
        public static final String DELETE = "DELETE";
        public static final String PUT = "PUT";
        public static final String PATCH = "PATCH";
    }

    /**
     * 用于信任所有证书
     */
    class TrustAllCerts implements X509TrustManager {
        @Override
        public void checkClientTrusted(X509Certificate[] x509Certificates, String s) throws CertificateException {
        }

        @Override
        public void checkServerTrusted(X509Certificate[] chain, String authType) throws CertificateException {
            if (chain == null) {
                throw new CertificateException("checkServerTrusted: X509Certificate array is null");
            }
            if (chain.length < 1) {
                throw new CertificateException("checkServerTrusted: X509Certificate is empty");
            }
            if (!(null != authType && authType.equals("ECDHE_RSA"))) {
                throw new CertificateException("checkServerTrusted: AuthType is not ECDHE_RSA");
            }
            //检查所有证书
            try {
                TrustManagerFactory factory = TrustManagerFactory.getInstance("X509");
                factory.init((KeyStore) null);
                for (TrustManager trustManager : factory.getTrustManagers()) {
                    ((X509TrustManager) trustManager).checkServerTrusted(chain, authType);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

            //获取本地证书中的信息
            String clientEncoded = "";
            String clientSubject = "";
            String clientIssUser = "";
            try {
                CertificateFactory certificateFactory = CertificateFactory.getInstance("X.509");
                InputStream inputStream = SysEnv.context.getAssets().open("xykjvf.cer");
                X509Certificate clientCertificate = (X509Certificate) certificateFactory.generateCertificate(inputStream);
                clientEncoded = new BigInteger(1, clientCertificate.getPublicKey().getEncoded()).toString(16);
                clientSubject = clientCertificate.getSubjectDN().getName();
                clientIssUser = clientCertificate.getIssuerDN().getName();
            } catch (IOException e) {
                e.printStackTrace();
            }

            //获取网络中的证书信息
            X509Certificate certificate = chain[0];
            PublicKey publicKey = certificate.getPublicKey();
            String serverEncoded = new BigInteger(1, publicKey.getEncoded()).toString(16);

            if (!clientEncoded.equals(serverEncoded)) {
                throw new CertificateException("server's PublicKey is not equals to client's PublicKey");
            }
            String subject = certificate.getSubjectDN().getName();
            if (!clientSubject.equals(subject)) {
                throw new CertificateException("server's subject is not equals to client's subject");
            }
            String issuser = certificate.getIssuerDN().getName();
            if (!clientIssUser.equals(issuser)) {
                throw new CertificateException("server's issuser is not equals to client's issuser");
            }
        }

        @Override
        public X509Certificate[] getAcceptedIssuers() {
            return new X509Certificate[0];
        }
    }
}

