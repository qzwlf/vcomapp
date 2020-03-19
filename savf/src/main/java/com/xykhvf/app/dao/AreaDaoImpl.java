package com.xykhvf.app.dao;


import com.easydblib.dao.BaseDao;
import com.vcom.publiclibrary.utils.SysEnv;
import com.vcom.publiclibrary.utils.db.EasyDBHelper;
import com.xykhvf.app.model.AreaModel;

import java.util.List;

import io.reactivex.Observable;

/**
 * Created by Administrator on 2017-09-13.
 */
public class AreaDaoImpl implements IAreaDao {
    private BaseDao<AreaModel> mDao = null;

    public AreaDaoImpl() {
        if (mDao == null)
            mDao = EasyDBHelper.getInstance(SysEnv.context).dao(AreaModel.class);
    }

    @Override
    public void save(AreaModel area) {
        mDao.create(area);
    }

    @Override
    public void save(List<AreaModel> list) {
        mDao.clearTable();
        mDao.create(list);
    }

    @Override
    public List<AreaModel> findAll() {
        return mDao.queryForAll();
    }

    @Override
    public Observable<List<AreaModel>> findAllByObs() {
        return Observable.create(observableEmitter -> {
            observableEmitter.onNext(mDao.queryForAll());
            observableEmitter.onComplete();
        });
    }

    @Override
    public void delAll() {
        mDao.clearTable();
    }

    @Override
    public void obsSave(List<AreaModel> area) {
        Observable.create(o -> {
            mDao.create(area);
            o.onComplete();
        });
    }
}
