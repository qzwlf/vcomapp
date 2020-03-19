package com.vcom.publiclibrary.utils;

import android.content.Context;
import android.os.AsyncTask;

import java.io.InputStream;
import java.util.Map;
import java.util.Properties;

/**
 * Created by Lifa on 2016-05-25 09:50.
 */
public class PropertiesUtils extends Properties {
    private final static String SETFILE = "setting.properties";
    private static Properties mProperties = new Properties();


    public static String readAssetsProp(String fileName, String key) {
        String value = "";
        try {
            InputStream inputStream = SysEnv.context.getAssets().open(fileName);
            mProperties.load(inputStream);
            value = mProperties.getProperty(key);
            inputStream.close();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return value;
    }

    public static String readAssetsProp(Context context, String fileName, String key) {
        String value = "";
        try {
            InputStream inputStream = context.getAssets().open(fileName);
            mProperties.load(inputStream);
            value = mProperties.getProperty(key);
            inputStream.close();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return value;
    }

    public static String readAssetsProp(String fileName, String key, String defaultValue) {
        String value = "";
        try {
            InputStream in = SysEnv.context.getAssets().open(fileName);
            mProperties.load(in);
            value = mProperties.getProperty(key, defaultValue);
            in.close();
        } catch (Exception e1) {
            e1.printStackTrace();
        }

        return value;
    }

    public static String readAssetsProp(Context context, String fileName, String key, String defaultValue) {
        String value = "";
        try {
            InputStream in = context.getAssets().open(fileName);
            mProperties.load(in);
            value = mProperties.getProperty(key, defaultValue);
            in.close();
        } catch (Exception e1) {
            e1.printStackTrace();
        }

        return value;
    }

    public static String readAssetsSetting(String key) {
        String value = "";
        try {
            InputStream in = SysEnv.context.getAssets().open(SETFILE);
            mProperties.load(in);
            value = mProperties.getProperty(key);
            in.close();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return value;
    }

    public static void readAssetsSettingTask(String key) {
        String result = "";

        new AsyncTask<String, Integer, String>() {
            @Override
            protected String doInBackground(String... params) {
                String value = "";
                try {
                    InputStream in = SysEnv.context.getAssets().open(SETFILE);
                    mProperties.load(in);
                    value = mProperties.getProperty(params[0]);
                    in.close();
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
                return value;
            }

            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
            }
        }.execute(key, null);
    }

    public static void waitAssetsSetting(String key, String value) {
        try {
            InputStream in = SysEnv.context.getAssets().open(SETFILE);
            mProperties.load(in);
            // mProperties.remove(key);
            mProperties.setProperty(key, value);
            in.close();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public static void waitAssetsSetting(Map<String, String> params) {
        try {
            new AsyncTask<Map<String, String>, Integer, Void>() {
                @Override
                protected Void doInBackground(Map<String, String>... params) {
                    try {
                        InputStream in = SysEnv.context.getAssets().open(SETFILE);
                        mProperties.load(in);
                        for (String key : params[0].keySet()) {
                            //mProperties.remove(key);
                            mProperties.setProperty(key, params[0].get(key));
                        }
                        in.close();
                    } catch (Exception ex) {
                        ex.printStackTrace();
                    }

                    return null;
                }
            }.execute(params, null);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}
