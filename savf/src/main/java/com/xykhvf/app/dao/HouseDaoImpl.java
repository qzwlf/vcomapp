package com.xykhvf.app.dao;

import com.easydblib.dao.BaseDao;
import com.j256.ormlite.stmt.QueryBuilder;
import com.vcom.publiclibrary.utils.SysEnv;
import com.vcom.publiclibrary.utils.db.EasyDBHelper;
import com.xykhvf.app.model.response.House;

import java.util.List;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;

public class HouseDaoImpl implements IHouseDao {

    private BaseDao<House> mDao = null;

    public HouseDaoImpl() {
        if (mDao == null)
            mDao = EasyDBHelper.getInstance(SysEnv.context).dao(House.class);
    }

    @Override
    public void update(List<House> list) {
        mDao.clearTable();
        mDao.create(list);
    }

    @Override
    public Observable<List<House>> findNumbere(String no) {

        return Observable.create(new ObservableOnSubscribe<List<House>>() {
            @Override
            public void subscribe(ObservableEmitter<List<House>> emitter) throws Exception {
                List<House> list = findByNumbere(no);
                emitter.onNext(list);
                emitter.onComplete();
            }
        });
    }

    @Override
    public List<House> findByNumbere(String no) {
        try {
            QueryBuilder<House, Integer> queryBuilder = mDao.fetchDao().queryBuilder();
            queryBuilder.where().like("number", "%" + no + "%");
            return mDao.query(queryBuilder);
        } catch (Exception ex) {
        }
        return null;
    }
}
