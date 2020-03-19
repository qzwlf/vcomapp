package com.vcom.publiclibrary.dao;

import android.content.Context;

import com.easydblib.dao.BaseDao;
import com.vcom.publiclibrary.model.ErrLog;
import com.vcom.publiclibrary.utils.db.EasyDBHelper;

import java.util.List;

public class ErrLogDaoImpl implements IErrLogDao {
    private BaseDao<ErrLog> mDao = null;

    public ErrLogDaoImpl(Context context) {
        if (mDao == null)
            mDao = EasyDBHelper.getInstance(context).dao(ErrLog.class);
    }

    @Override
    public void save(ErrLog model) {
        mDao.create(model);
    }

    @Override
    public void removeAll() {
        mDao.clearTable();
    }

    @Override
    public void removeById(ErrLog model) {
        mDao.delete(model);
    }

    @Override
    public List<ErrLog> findAll() {
        return mDao.queryForAll();
    }
}
