package com.vcom.publiclibrary.utils.db;

import android.content.Context;

import com.easydblib.helper.BaseDBHelper;

import java.sql.SQLException;

/**
 * Created by Administrator on 2017-01-14.
 */
public class EasyDBHelper extends BaseDBHelper {
    //版本号
    private static final int DB_VERSION = 5;

    //数据库名称
    private static final String DB_NAME = "vcom_bus.db";

    private static Context mContext = null;

    //数据表清单
    private static final Class<?>[] tables = {

    };
    private static EasyDBHelper helper = null;

    public static EasyDBHelper getInstance(Context context) {
        if (null == helper) {
            synchronized (EasyDBHelper.class) {
                if (null == helper) {
                    helper = new EasyDBHelper(context);
                }
            }
        }
        return helper;
    }

    private EasyDBHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION, tables);
    }


    @Override
    protected BaseDBHelper initHelper() {
        return helper;
    }

    @Override
    protected boolean upgrade(int oldVersion, int newVersion) throws SQLException {
        if (oldVersion < 2) {
            //增加字段ext
        }
        return true;
    }
}
