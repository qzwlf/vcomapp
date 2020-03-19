package com.vcom.publiclibrary.dao;


import com.vcom.publiclibrary.model.ErrLog;

import java.util.List;

public interface IErrLogDao {
    void save(ErrLog model);

    void removeAll();

    void removeById(ErrLog model);

    List<ErrLog> findAll();
}
