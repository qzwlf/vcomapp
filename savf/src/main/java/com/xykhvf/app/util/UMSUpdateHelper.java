package com.xykhvf.app.util;

import android.content.Context;

import com.appinterface.update.AppJson;
import com.appinterface.update.AppState;
import com.appinterface.update.ReturnCode;
import com.appinterface.update.UpdateHelper;
import com.appinterface.update.UpdateListener;

public class UMSUpdateHelper {
    private static final String PACKAGE_NAME = "com.xykhvf.app";
    private static String STATE_DOWNLOAD_ING = "正在下载";
    private static String STATE_DOWNLOAD_COMPLETED = "下载完成";
    private static String STATE_DOWNLOAD_FAILED = "下载失败";
    private static String STATE_INSTALL_ING = "正在安装";
    private static String STATE_INSTALL_COMPLETED = "安装完成";
    private static String STATE_INSTALL_FAILED = "安装失败";
    private final static String APP_ID = "64c60a69396b4a64bcc45fc329e9f29e";

    public interface UpdateStateCallback {
        void onSuccess(String info);

        void onFail(String info);

        void onStateChange(String stateInfo);

        void onProgress(int i);
    }

    public static void isNeedUpdate(Context context, UpdateStateCallback updateStateCallback) {
        UpdateHelper.checkUpdate(context, PACKAGE_NAME, new UpdateListener() {
            @Override
            public void onCheckUpdateResult(int i, AppJson appJson) {
                super.onCheckUpdateResult(i, appJson);
                switch (i) {
                    case ReturnCode.CODE_NO_UPDATE:
                        updateStateCallback.onFail("无需升级");
                        break;
                    case ReturnCode.CODE_GET_UPDATE:
                        updateStateCallback.onSuccess(appJson.getUpdateInfo());
                        break;
                    default:
                        updateStateCallback.onFail("检查更新失败");
                        break;
                }
            }
        });
    }

    public static void downloadFile(Context context, UpdateStateCallback updateStateCallback) {
        UpdateHelper.download(context, APP_ID, PACKAGE_NAME, true, new UpdateListener() {

            @Override
            public void onStateChanged(int i) {
                super.onStateChanged(i);
                switch (i) {
                    case AppState.STATE_DOWNLOAD_ING:
                        updateStateCallback.onStateChange(STATE_DOWNLOAD_ING);
                        break;
                    case AppState.STATE_DOWNLOAD_COMPLETED:
                        updateStateCallback.onStateChange(STATE_DOWNLOAD_COMPLETED);
                        break;
                    case AppState.STATE_DOWNLOAD_FAILED:
                        updateStateCallback.onFail(STATE_DOWNLOAD_FAILED);
                        break;
                    case AppState.STATE_INSTALL_FAILED:
                        updateStateCallback.onFail(STATE_INSTALL_FAILED);
                        break;
                    case AppState.STATE_INSTALL_ING:
                        updateStateCallback.onStateChange(STATE_INSTALL_ING);
                        break;
                    case AppState.STATE_INSTALL_COMPLETED:
                        updateStateCallback.onSuccess(STATE_INSTALL_COMPLETED);
                        break;
                }
            }

            @Override
            public void onProgress(int i) {
                super.onProgress(i);
                updateStateCallback.onProgress(i);
            }
        });
    }
}
