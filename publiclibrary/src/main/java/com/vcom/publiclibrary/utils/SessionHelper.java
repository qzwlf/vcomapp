package com.vcom.publiclibrary.utils;

import com.vcom.publiclibrary.model.SettingModel;

/**
 * Created by Administrator on 2017-09-11.
 */
public class SessionHelper<T extends SettingModel> {
    private Class<T> clazz;
    private static SessionHelper mInstance;
    // private T data;

    public SessionHelper() {
    }

    public T newInstance() {
        try {
            return clazz.newInstance(); // this will throw checked exceptions
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static SessionHelper getInstance() {
        if (mInstance == null)
            mInstance = new SessionHelper();
        return mInstance;
    }

    public Class<T> getClazz() {
        return clazz;
    }

    public void setClazz(Class<T> clazz) {
        this.clazz = clazz;
    }
}
