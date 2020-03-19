package com.xykhvf.app.dao;

import com.xykhvf.app.model.AreaModel;

import java.util.List;

import io.reactivex.Observable;

/**
 * Created by Administrator on 2017-09-13.
 */
public interface IAreaDao {
    void save(AreaModel area);

    void save(List<AreaModel> area);

    List<AreaModel> findAll();

    Observable<List<AreaModel>> findAllByObs();

    void delAll();

    void obsSave(List<AreaModel> area);
}
