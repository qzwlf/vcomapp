package com.vcom.publiclibrary.niohttp;

import android.os.Handler;
import android.os.Message;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.koushikdutta.async.http.server.AsyncHttpServer;
import com.koushikdutta.async.http.server.AsyncHttpServerRequest;
import com.koushikdutta.async.http.server.AsyncHttpServerResponse;
import com.koushikdutta.async.http.server.HttpServerRequestCallback;
import com.vcom.publiclibrary.model.HttpPostModel;
import com.vcom.publiclibrary.model.JsonResult;
import com.vcom.publiclibrary.utils.Base64Object;

public class NIOHttpServer implements HttpServerRequestCallback {
    private static final String TAG = "NIOHttpServer";
    private static NIOHttpServer mInstance;
    private Handler mHandler;
    public static int PORT_LISTEN_DEFALT = 5000;
    public static int HTTPHANDELMSG = 10;
    HttpActionListener httpActionListener;
    AsyncHttpServer server = new AsyncHttpServer();
    private static Gson mGson = new GsonBuilder().serializeNulls()
            .setDateFormat("yyyy-MM-dd HH:mm:ss").create();

    public static enum Status {
        REQUEST_OK(200, "请求成功"),
        REQUEST_ERROR(500, "请求失败"),
        REQUEST_ERROR_API(501, "无效的请求接口"),
        REQUEST_ERROR_CMD(502, "无效命令"),
        REQUEST_ERROR_DEVICEID(503, "不匹配的设备ID"),
        REQUEST_ERROR_ENV(504, "不匹配的服务环境");

        private final int requestStatus;
        private final String description;

        Status(int requestStatus, String description) {
            this.requestStatus = requestStatus;
            this.description = description;
        }

        public String getDescription() {
            return description;
        }

        public int getRequestStatus() {
            return requestStatus;
        }
    }

    public static NIOHttpServer getInstance(Handler handler) {
        if (mInstance == null) {
            // 增加类锁,保证只初始化一次
            synchronized (NIOHttpServer.class) {
                if (mInstance == null) {
                    mInstance = new NIOHttpServer(handler);
                }
            }
        }
        return mInstance;
    }

    public static NIOHttpServer getInstance(HttpActionListener listener) {
        if (mInstance == null) {
            // 增加类锁,保证只初始化一次
            synchronized (NIOHttpServer.class) {
                if (mInstance == null) {
                    mInstance = new NIOHttpServer(listener);
                }
            }
        }
        return mInstance;
    }

    public static NIOHttpServer getInstance() {
        if (mInstance == null) {
            // 增加类锁,保证只初始化一次
            synchronized (NIOHttpServer.class) {
                if (mInstance == null) {
                    mInstance = new NIOHttpServer();
                }
            }
        }
        return mInstance;
    }

    public NIOHttpServer() {

    }

    public NIOHttpServer(Handler handler) {
        this.mHandler = handler;
    }

    public NIOHttpServer(HttpActionListener listener) {
        httpActionListener = listener;
    }


    @Override
    public void onRequest(AsyncHttpServerRequest request, AsyncHttpServerResponse response) {
        Log.d(TAG, "进来了，哈哈");
        String data = request.getBody().get().toString();
        HttpPostModel model = mGson.fromJson(data, HttpPostModel.class);
        String str = Base64Object.base64ToString(model.getData());
        Status status = httpActionListener.reciver(str);
        JsonResult<String> result = new JsonResult<>();
        if (status == Status.REQUEST_OK) {
            result.setCode(0);
            result.setMessage("发送成功！");
        } else {
            result.setCode(1);
            result.setMessage("发送失败！");
        }
        response.send(mGson.toJson(result));
    }

    /**
     * 开启本地服务
     */
    public void startServer() {
        //如果有其他的请求方式，例如下面一行代码的写法
        server.addAction("OPTIONS", "[\\d\\D]*", this);
        server.get("[\\d\\D]*", this);
        server.post("[\\d\\D]*", this);
        server.listen(PORT_LISTEN_DEFALT);
    }

    public void stopServer() {
        if (server != null) {
            server.removeAction("OPTIONS", "[\\d\\D]*");
            server.stop();
        }
    }
}
