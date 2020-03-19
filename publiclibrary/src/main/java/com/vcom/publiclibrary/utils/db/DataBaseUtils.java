package com.vcom.publiclibrary.utils.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import com.j256.ormlite.android.apptools.OpenHelperManager;
import com.j256.ormlite.android.apptools.OrmLiteSqliteOpenHelper;
import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.table.TableUtils;
import com.vcom.publiclibrary.utils.SysEnv;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2017-01-14.
 */
public class DataBaseUtils extends OrmLiteSqliteOpenHelper {

    private static String databaseName;
    private static int databaseVersion;
    private static List<Class> table = new ArrayList<Class>();
    private static DataBaseUtils dbHelper = null;

    /**
     * 必须对外提供Public构造函数（实例化不用该方法）
     *
     * @param context 上下文
     */
    public DataBaseUtils(Context context) {
        super(context, databaseName, null, databaseVersion);
    }

    /**
     * 实例化对象
     *
     * @param dbName  数据库名称
     * @param version 数据库版本
     * @return
     */
    public static DataBaseUtils gainInstance(String dbName, int version) {
        if (dbHelper == null) {
            databaseName = dbName;
            databaseVersion = version;
            //会隐式调用public构造方法
            dbHelper = OpenHelperManager.getHelper(
                    SysEnv.context, DataBaseUtils.class);
        }
        return dbHelper;
    }

    /**
     * 释放数据库连接
     */
    public void releaseAll() {
        if (dbHelper != null) {
            OpenHelperManager.releaseHelper();
            dbHelper = null;
        }
    }

    /**
     * 配置实体
     *
     * @param cls 实体
     */
    public void save(Class cls) {
        table.add(cls);
    }


    /**
     * 删除表
     *
     * @param entity 实体
     */
    public void delete(Class entity) {
        try {
            TableUtils.dropTable(getConnectionSource(), entity, true);
        } catch (SQLException e) {
        }
    }

    /**
     * 创建表
     *
     * @param entity 实体
     */
    public void createTable(Class entity) {
        try {
            TableUtils.createTableIfNotExists(getConnectionSource(), entity);
        } catch (SQLException e) {
        }
    }

    /**
     * 创建SQLite数据库
     */
    @Override
    public void onCreate(SQLiteDatabase sqliteDatabase,
                         ConnectionSource connectionSource) {
        try {
            for (Class entity : table) {
                TableUtils.createTableIfNotExists(connectionSource, entity);
            }
        } catch (SQLException e) {
        }
    }

    /**
     * 更新SQLite数据库
     */
    @Override
    public void onUpgrade(SQLiteDatabase sqliteDatabase,
                          ConnectionSource connectionSource, int oldVer, int newVer) {
        try {
            for (Class entity : table) {
                TableUtils.dropTable(connectionSource, entity, true);
            }
            onCreate(sqliteDatabase, connectionSource);
        } catch (SQLException e) {
        }
    }
}