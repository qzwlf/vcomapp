package com.vcom.publiclibrary.utils;

import android.content.Context;
import android.content.SharedPreferences;

import com.vcom.publiclibrary.MyApplication;

import java.math.BigDecimal;
import java.util.Map;

/**
 * Created by Lifa on 2016-06-24 12:24.
 */
public class SharedPreferencesUtils {
    private static final String FIELDNAME = "Setting";
    private static SharedPreferences mSharedPreferences = null;

    public static void putValues(Context context, Map<String, String> params) {
        SharedPreferences.Editor sp = getEditor(context);
        for (String key : params.keySet()) {
            sp.putString(key, params.get(key));
        }
    }

    public static void putValues(Map<String, String> params) {
        SharedPreferences.Editor sp = getEditor();
        for (String key : params.keySet()) {
            sp.putString(key, params.get(key));
        }
    }

    /**
     * 向SharedPreferences中写入int类型数据
     *
     * @param key   键
     * @param value 值
     */
    public static void putValue(String key, int value) {
        SharedPreferences.Editor sp = getEditor();
        sp.putInt(key, value);
        sp.commit();
        sp.apply();
    }

    public static void putValue(Context context, String key, int value) {
        SharedPreferences.Editor sp = getEditor(context);
        sp.putInt(key, value);
        sp.commit();
        sp.apply();
    }

    /**
     * 向SharedPreferences中写入boolean类型的数据
     *
     * @param key   键
     * @param value 值
     */
    public static void putValue(String key, boolean value) {
        SharedPreferences.Editor sp = getEditor();
        sp.putBoolean(key, value);
        sp.commit();
        sp.apply();
    }

    public static void putValue(Context context, String key, boolean value) {
        SharedPreferences.Editor sp = getEditor(context);
        sp.putBoolean(key, value);
        sp.commit();
        sp.apply();
    }

    /**
     * 向SharedPreferences中写入String类型的数据
     *
     * @param key   键
     * @param value 值
     */
    public static void putValue(String key, String value) {
        SharedPreferences.Editor sp = getEditor();
        sp.putString(key, value);
        sp.commit();
        sp.apply();
    }

    public static void putValue(Context context, String key, String value) {
        SharedPreferences.Editor sp = getEditor(context);
        sp.putString(key, value);
        sp.commit();
        sp.apply();
    }

    /**
     * 向SharedPreferences中写入float类型的数据
     *
     * @param key   键
     * @param value 值
     */
    public static void putValue(String key, float value) {
        SharedPreferences.Editor sp = getEditor();
        sp.putFloat(key, value);
        sp.commit();
        sp.apply();
    }

    public static void putValue(Context context, String key, float value) {
        SharedPreferences.Editor sp = getEditor(context);
        sp.putFloat(key, value);
        sp.commit();
        sp.apply();
    }

    /**
     * 向SharedPreferences中写入long类型的数据
     *
     * @param key   键
     * @param value 值
     */
    public static void putValue(String key, long value) {
        SharedPreferences.Editor sp = getEditor();
        sp.putLong(key, value);
        sp.commit();
        sp.apply();
    }

    public static void putValue(Context context, String key, long value) {
        SharedPreferences.Editor sp = getEditor(context);
        sp.putLong(key, value);
        sp.commit();
        sp.apply();
    }

    public static void putValue(String key, double value) {
        SharedPreferences.Editor sp = getEditor();
        BigDecimal bigDecimal = new BigDecimal(Double.toString(value));
        sp.putFloat(key, bigDecimal.floatValue());
        sp.commit();
        sp.apply();
    }

    public static void putValue(Context context, String key, double value) {
        SharedPreferences.Editor sp = getEditor(context);
        BigDecimal bigDecimal = new BigDecimal(Double.toString(value));
        sp.putFloat(key, bigDecimal.floatValue());
        sp.commit();
        sp.apply();
    }

    /**
     * 从SharedPreferences中读取int类型的数据
     *
     * @param key      键
     * @param defValue 如果读取不成功则使用默认值
     * @return 返回读取的值
     */
    public static int getValue(String key, int defValue) {
        int value = 0;
        try {
            SharedPreferences sp = getSharedPreferences();
            value = sp.getInt(key, defValue);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return value;
    }

    public static int getValue(Context context, String key, int defValue) {
        int value = 0;
        try {
            SharedPreferences sp = getSharedPreferences(context);
            value = sp.getInt(key, defValue);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return value;
    }

    /**
     * 从SharedPreferences中读取boolean类型的数据
     *
     * @param key      键
     * @param defValue 如果读取不成功则使用默认值
     * @return 返回读取的值
     */
    public static boolean getValue(String key, boolean defValue) {
        boolean value = false;
        try {
            SharedPreferences sp = getSharedPreferences();
            value = sp.getBoolean(key, defValue);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return value;
    }

    public static boolean getValue(Context context, String key, boolean defValue) {
        boolean value = false;
        try {
            SharedPreferences sp = getSharedPreferences(context);
            value = sp.getBoolean(key, defValue);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return value;
    }

    /**
     * 从SharedPreferences中读取String类型的数据
     *
     * @param key      键
     * @param defValue 如果读取不成功则使用默认值
     * @return 返回读取的值
     */
    public static String getValue(String key, String defValue) {
        String value = "";
        try {
            SharedPreferences sp = getSharedPreferences();
            value = sp.getString(key, defValue);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return value;
    }

    public static String getValue(Context context, String key) {
        String value = "";
        try {
            SharedPreferences sp = getSharedPreferences(context);
            value = sp.getString(key, "");
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return value;
    }

    public static String getValue(Context context, String key, String defValue) {
        String value = "";
        try {
            SharedPreferences sp = getSharedPreferences(context);
            value = sp.getString(key, defValue);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return value;
    }

    /**
     * 从SharedPreferences中读取float类型的数据
     *
     * @param key      键
     * @param defValue 如果读取不成功则使用默认值
     * @return 返回读取的值
     */
    public static float getValue(String key, float defValue) {
        float value = 0f;
        try {
            SharedPreferences sp = getSharedPreferences();
            value = sp.getFloat(key, defValue);
        } catch (Exception ex) {
            value = defValue;
            ex.printStackTrace();
        }
        return value;
    }

    public static float getValue(Context context, String key, float defValue) {
        float value = 0f;
        try {
            SharedPreferences sp = getSharedPreferences(context);
            value = sp.getFloat(key, defValue);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return value;
    }

    /**
     * 从SharedPreferences中读取long类型的数据
     *
     * @param key      键
     * @param defValue 如果读取不成功则使用默认值
     * @return 返回读取的值
     */
    public static long getValue(String key, long defValue) {
        long value = defValue;
        try {
            SharedPreferences sp = getSharedPreferences();
            value = sp.getLong(key, defValue);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return value;
    }

    public static long getValue(Context context, String key, long defValue) {
        long value = defValue;
        try {
            SharedPreferences sp = getSharedPreferences(context);
            value = sp.getLong(key, defValue);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return value;
    }

    //获取Editor实例
    private static SharedPreferences.Editor getEditor() {
        return getSharedPreferences().edit();
    }

    //获取Editor实例
    private static SharedPreferences.Editor getEditor(Context context) {
        return getSharedPreferences(context).edit();
    }

    //获取SharedPreferences实例
    private static SharedPreferences getSharedPreferences() {
//        if (mSharedPreferences == null)
//            mSharedPreferences = SysEnv.context.getSharedPreferences(FIELDNAME, Context.MODE_PRIVATE);
//        return mSharedPreferences;
        return MyApplication.gainContext().getSharedPreferences(FIELDNAME, Context.MODE_PRIVATE);
    }

    //获取SharedPreferences实例
    private static SharedPreferences getSharedPreferences(Context context) {
//        if (mSharedPreferences == null)
//            mSharedPreferences = SysEnv.context.getSharedPreferences(FIELDNAME, Context.MODE_PRIVATE);
//        return mSharedPreferences;
        return context.getSharedPreferences(FIELDNAME, Context.MODE_PRIVATE);
    }
}
