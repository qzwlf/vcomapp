package com.vcom.publiclibrary.util.okhttp.callback;

import java.io.File;

/**
 * Created by zhy on 15/12/15.
 */
public abstract class FileCallBack extends Callback<File> {
    /**
     * 目标文件存储的文件夹路径
     */
    private String destFileDir;
    /**
     * 目标文件存储的文件名
     */
    private String destFileName;

    public abstract void inProgress(float progress, long total);

    public FileCallBack(String destFileDir, String destFileName) {
        this.destFileDir = destFileDir;
        this.destFileName = destFileName;
    }
}
