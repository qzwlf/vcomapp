package com.xykhvf.app.view;

import com.xykhvf.app.model.Result;
import com.xykhvf.app.model.response.ResCheckPay;
import com.xykhvf.app.model.response.ResSubmitPay;

public interface IPayView {
    void setPayInfo(ResCheckPay res);

    void payResult(ResSubmitPay result);
}
