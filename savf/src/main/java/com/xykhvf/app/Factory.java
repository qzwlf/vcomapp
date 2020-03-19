package com.xykhvf.app;

import com.xykhvf.app.dao.ApiDaoImpl;
import com.xykhvf.app.dao.AreaDaoImpl;
import com.xykhvf.app.dao.FeeDaoImpl;
import com.xykhvf.app.dao.HouseDaoImpl;
import com.xykhvf.app.dao.IApiDao;
import com.xykhvf.app.dao.IAreaDao;
import com.xykhvf.app.dao.IFeeDao;
import com.xykhvf.app.dao.IHouseDao;

public class Factory {
    public static IApiDao getIApiDaoInstance() {
        return new ApiDaoImpl();
    }

    public static IAreaDao getIAreaDaoInstance() {
        return new AreaDaoImpl();
    }

    public static IHouseDao getIHouseDaoInstance() {
        return new HouseDaoImpl();
    }

    public static IFeeDao getIFeeDaoInstance() {
        return new FeeDaoImpl();
    }
}
