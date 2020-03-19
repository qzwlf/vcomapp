package com.xykhvf.app.view;

import android.content.Context;
import android.support.v7.widget.RecyclerView;

import com.xykhvf.app.model.FeeSummary;
import com.xykhvf.app.model.response.Fee;

import java.util.List;

public interface IFeeView {
    void setList(List<Fee> list, Context context, RecyclerView recyclerView);

    void setMonthFee(List<FeeSummary> list);
}
