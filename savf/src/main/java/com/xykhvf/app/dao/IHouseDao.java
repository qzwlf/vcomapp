package com.xykhvf.app.dao;

import com.xykhvf.app.model.response.House;

import java.util.List;

import io.reactivex.Observable;

public interface IHouseDao {
    void update(List<House> list);

    Observable<List<House>> findNumbere(String no);

    List<House> findByNumbere(String no);
}
