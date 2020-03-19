package com.xykhvf.app.view;

import com.xykhvf.app.model.response.Report;
import com.xykhvf.app.model.response.ResReport;

import java.util.List;

public interface IReportView {
    void setData(List<ResReport> list);
}
