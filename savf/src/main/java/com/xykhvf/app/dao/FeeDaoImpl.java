package com.xykhvf.app.dao;

import com.easydblib.dao.BaseDao;
import com.j256.ormlite.dao.GenericRawResults;
import com.j256.ormlite.dao.RawRowMapper;
import com.j256.ormlite.stmt.QueryBuilder;
import com.j256.ormlite.stmt.RawResultsImpl;
import com.j256.ormlite.stmt.SelectIterator;
import com.vcom.publiclibrary.utils.SysEnv;
import com.vcom.publiclibrary.utils.db.EasyDBHelper;
import com.xykhvf.app.model.FeeSummary;
import com.xykhvf.app.model.response.Fee;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;

public class FeeDaoImpl implements IFeeDao {
    private BaseDao<Fee> mDao = null;

    public FeeDaoImpl() {
        if (mDao == null) {
            mDao = EasyDBHelper.getInstance(SysEnv.context).dao(Fee.class);
        }
    }


    @Override
    public void update(List<Fee> list) {
        mDao.clearTable();
        mDao.create(list);
    }

    @Override
    public Observable<List<FeeSummary>> findSummaryMonth() {
        return Observable.create(new ObservableOnSubscribe<List<FeeSummary>>() {
            @Override
            public void subscribe(ObservableEmitter<List<FeeSummary>> emitter) throws Exception {
                List<FeeSummary> feeSummaryList = new ArrayList<>();
                String strSql = "select id,date,type,merchant,selected,sum(amount) as amount from fee_info GROUP BY date";
                GenericRawResults<Fee> results = mDao.fetchDao().queryRaw(strSql, mDao.fetchDao().getRawRowMapper());
                List<Fee> fees = results.getResults();
                if (fees != null) {
                    for (Fee f : fees) {
                        FeeSummary feeSummary = new FeeSummary();
                        feeSummary.setTotalFee(f.getAmount().toString());
                        feeSummary.setDate(f.getDate());
                        feeSummary.setSelected(true);
                        feeSummaryList.add(feeSummary);
                    }
                }
                emitter.onNext(feeSummaryList);
                emitter.onComplete();
            }
        });
    }

    @Override
    public Observable<List<Fee>> findFeeByMonth(String month) {
        return Observable.create(new ObservableOnSubscribe<List<Fee>>() {
            @Override
            public void subscribe(ObservableEmitter<List<Fee>> observableEmitter) throws Exception {
                QueryBuilder<Fee, Integer> queryBuilder = mDao.fetchDao().queryBuilder();
                queryBuilder.where().eq("date", month);
                List<Fee> list = mDao.query(queryBuilder);
                observableEmitter.onNext(list);
                observableEmitter.onComplete();
            }
        });
    }
}
