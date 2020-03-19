package com.xykhvf.app.presenter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.vcom.publiclibrary.util.okhttp.callback.StringCallback;
import com.vcom.publiclibrary.utils.SharedPreferencesUtils;
import com.vcom.publiclibrary.utils.SysEnv;
import com.xykhvf.app.Factory;
import com.xykhvf.app.model.Result;
import com.xykhvf.app.model.response.Fee;
import com.xykhvf.app.util.Constants;
import com.xykhvf.app.view.IFeeView;

import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import okhttp3.Call;

public class FeePresenter {
    private static final String TAG = BuildingPresenter.class.getName();
    private Context mContext = null;
    private IFeeView mFeeView = null;
    private Gson mGson = new GsonBuilder().serializeNulls()
            .setDateFormat("yyyy-MM-dd HH:mm:ss").create();

    public FeePresenter(Context context, IFeeView view) {
        mContext = context;
        mFeeView = view;
    }

    public void getFees(Long id) {
        Factory.getIApiDaoInstance().getOrders(id, SharedPreferencesUtils.getValue(SysEnv.context, Constants.TOKEN, ""),
                new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e) {
                        Log.i(TAG, "onError: ", e);
                    }

                    @Override
                    public void onResponse(String response) {
                        Log.i(TAG, "response:" + response);
                        Result<List<Fee>> result = mGson.fromJson(response, new TypeToken<Result<List<Fee>>>() {
                        }.getType());
                        if (result.getCode() == 0) {
                            Factory.getIFeeDaoInstance().update(result.getData());
                            getFeeFromDB();
                            //mFeeView.setList(result.getData());
                        }
                    }
                });
    }

    public void getFeeFromDB() {
        Factory.getIFeeDaoInstance().findSummaryMonth()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(feeSummaries -> {
                    mFeeView.setMonthFee(feeSummaries);
                });
    }

    public void getFeeItems(String date, Context context, RecyclerView recyclerView) {
        Factory.getIFeeDaoInstance().findFeeByMonth(date)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(fees -> mFeeView.setList(fees, context, recyclerView));

    }
}
