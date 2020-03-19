package com.xykhvf.app.presenter;

import android.content.Context;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.vcom.publiclibrary.util.okhttp.callback.StringCallback;
import com.vcom.publiclibrary.utils.SharedPreferencesUtils;
import com.vcom.publiclibrary.utils.SysEnv;
import com.xykhvf.app.Factory;
import com.xykhvf.app.model.Result;
import com.xykhvf.app.model.request.ReqReport;
import com.xykhvf.app.model.response.Report;
import com.xykhvf.app.model.response.ResReport;
import com.xykhvf.app.util.Constants;
import com.xykhvf.app.view.IReportView;

import java.util.List;

import okhttp3.Call;

public class ReportPresenter {
    private static final String TAG = BuildingPresenter.class.getName();
    private Context mContext = null;
    private IReportView mReportView = null;
    private Gson mGson = new GsonBuilder().serializeNulls()
            .setDateFormat("yyyy-MM-dd HH:mm:ss").create();

    public ReportPresenter(Context context, IReportView view) {
        mContext = context;
        mReportView = view;
    }

    public void getReport(ReqReport req) {
        Factory.getIApiDaoInstance().getReport(req, SharedPreferencesUtils.getValue(SysEnv.context, Constants.TOKEN), new StringCallback() {
            @Override
            public void onError(Call call, Exception e) {
                Log.e(TAG, "onError: ", e);
            }

            @Override
            public void onResponse(String response) {
                Log.i(TAG, "onResponse: " + response);
                Result<List<ResReport>> result = mGson.fromJson(response, new TypeToken<Result<List<ResReport>>>() {
                }.getType());
                if (result.getCode() == 0)
                    mReportView.setData(result.getData());
            }
        });
    }
}
