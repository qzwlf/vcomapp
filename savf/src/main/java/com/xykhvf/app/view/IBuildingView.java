package com.xykhvf.app.view;

import com.xykhvf.app.model.AreaModel;

import java.util.List;

public interface IBuildingView {
    void setList(List<AreaModel> list);

    void selectArea(List<AreaModel> list);

    void setAreaName(String str);
}
