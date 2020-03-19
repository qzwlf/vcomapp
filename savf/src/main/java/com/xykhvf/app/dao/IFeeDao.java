package com.xykhvf.app.dao;

import com.xykhvf.app.model.FeeSummary;
import com.xykhvf.app.model.response.Fee;

import java.util.List;

import io.reactivex.Observable;

public interface IFeeDao {
    void update(List<Fee> list);

    Observable<List<FeeSummary>> findSummaryMonth();

    Observable<List<Fee>> findFeeByMonth(String month);
}
